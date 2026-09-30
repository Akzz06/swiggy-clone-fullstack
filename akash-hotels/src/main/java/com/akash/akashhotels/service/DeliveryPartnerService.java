package com.akash.akashhotels.service;

import com.akash.akashhotels.dto.DeliveryPartnerRequest;
import com.akash.akashhotels.dto.DeliveryPartnerResponse;
import com.akash.akashhotels.dto.DeliveryResponse;
import com.akash.akashhotels.dto.PartnerEarningsResponse;
import com.akash.akashhotels.entity.*;
import com.akash.akashhotels.exception.BadRequestException;
import com.akash.akashhotels.exception.ResourceNotFoundException;
import com.akash.akashhotels.repository.DeliveryPartnerRepository;
import com.akash.akashhotels.repository.DeliveryRepository;
import com.akash.akashhotels.repository.OrderRepository;
import com.akash.akashhotels.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryPartnerService {

    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DeliveryRuleService deliveryRuleService;

    public DeliveryPartnerService(DeliveryPartnerRepository deliveryPartnerRepository,
                                  DeliveryRepository deliveryRepository,
                                  OrderRepository orderRepository,
                                  UserRepository userRepository,
                                  DeliveryRuleService deliveryRuleService) {
        this.deliveryPartnerRepository = deliveryPartnerRepository;
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.deliveryRuleService = deliveryRuleService;
    }

    public List<DeliveryPartnerResponse> getAllPartners() {
        return deliveryPartnerRepository.findAll().stream()
                .map(this::mapToPartnerResponse)
                .collect(Collectors.toList());
    }

    public List<DeliveryPartnerResponse> getAvailablePartners() {
        return deliveryPartnerRepository.findByAvailabilityStatusAndActiveTrue(AvailabilityStatus.AVAILABLE).stream()
                .map(this::mapToPartnerResponse)
                .collect(Collectors.toList());
    }

    public DeliveryPartnerResponse getPartnerById(Long id) {
        DeliveryPartner partner = deliveryPartnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not macha found with id: " + id));
        return mapToPartnerResponse(partner);
    }

    public DeliveryPartnerResponse getPartnerByUserId(Long userId) {
        DeliveryPartner partner = deliveryPartnerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner profile not found for user id: " + userId));
        return mapToPartnerResponse(partner);
    }

    @Transactional
    public DeliveryPartnerResponse createPartner(DeliveryPartnerRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        if (deliveryPartnerRepository.findByUserId(user.getId()).isPresent()) {
            throw new BadRequestException("User already has a delivery partner profile");
        }

        user.setRole(Role.ROLE_DELIVERY_PARTNER);
        userRepository.save(user);

        DeliveryPartner partner = new DeliveryPartner();
        partner.setUser(user);
        partner.setVehicleType(request.getVehicleType());
        partner.setVehicleNumber(request.getVehicleNumber());
        partner.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        partner.setActive(request.getActive() != null ? request.getActive() : true);

        return mapToPartnerResponse(deliveryPartnerRepository.save(partner));
    }

    @Transactional
    public DeliveryPartnerResponse updatePartner(Long id, DeliveryPartnerRequest request) {
        DeliveryPartner partner = deliveryPartnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not found with id: " + id));

        partner.setVehicleType(request.getVehicleType());
        partner.setVehicleNumber(request.getVehicleNumber());
        if (request.getActive() != null) {
            partner.setActive(request.getActive());
        }

        return mapToPartnerResponse(deliveryPartnerRepository.save(partner));
    }

    @Transactional
    public void deletePartner(Long id) {
        DeliveryPartner partner = deliveryPartnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not found with id: " + id));
        partner.setActive(false);
        partner.setAvailabilityStatus(AvailabilityStatus.OFFLINE);
        deliveryPartnerRepository.save(partner);
    }

    @Transactional
    public DeliveryPartnerResponse updateAvailability(Long partnerId, AvailabilityStatus newStatus) {
        DeliveryPartner partner = deliveryPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not found with id: " + partnerId));

        // Business Rule (Section 3): A delivery partner can handle only one active delivery at a time
        // If partner is currently handling an active delivery, they cannot mark themselves AVAILABLE
        if (newStatus == AvailabilityStatus.AVAILABLE) {
            List<DeliveryStatus> activeStatuses = Arrays.asList(
                    DeliveryStatus.ASSIGNED,
                    DeliveryStatus.ACCEPTED,
                    DeliveryStatus.PICKED_UP,
                    DeliveryStatus.DELIVERING
            );
            List<Delivery> activeDeliveries = deliveryRepository.findByDeliveryPartnerIdAndDeliveryStatusIn(partner.getId(), activeStatuses);
            if (!activeDeliveries.isEmpty()) {
                throw new BadRequestException("Cannot set status to AVAILABLE while handling an active delivery (Order #" + activeDeliveries.get(0).getOrder().getId() + ")");
            }
        }

        partner.setAvailabilityStatus(newStatus);
        return mapToPartnerResponse(deliveryPartnerRepository.save(partner));
    }

    public List<DeliveryResponse> getPartnerDeliveries(Long partnerId) {
        return deliveryRepository.findByDeliveryPartnerId(partnerId).stream()
                .map(this::mapToDeliveryResponse)
                .collect(Collectors.toList());
    }

    public PartnerEarningsResponse getPartnerEarnings(Long partnerId) {
        DeliveryPartner partner = deliveryPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not found with id: " + partnerId));

        List<Delivery> deliveries = deliveryRepository.findByDeliveryPartnerId(partnerId);

        List<DeliveryResponse> deliveryResponses = deliveries.stream()
                .map(this::mapToDeliveryResponse)
                .collect(Collectors.toList());

        double totalBase = 0.0;
        double totalBonus = 0.0;
        double totalEarnings = 0.0;
        int completedCount = 0;

        for (Delivery delivery : deliveries) {
            if (delivery.getDeliveryStatus() == DeliveryStatus.DELIVERED) {
                completedCount++;
                if (delivery.getBaseEarning() != null) totalBase += delivery.getBaseEarning();
                if (delivery.getBonusAmount() != null) totalBonus += delivery.getBonusAmount();
                if (delivery.getTotalEarning() != null) totalEarnings += delivery.getTotalEarning();
            }
        }

        return new PartnerEarningsResponse(
                partner.getId(),
                partner.getUser().getName(),
                completedCount,
                totalBase,
                totalBonus,
                totalEarnings,
                deliveryResponses
        );
    }

    @Transactional
    public DeliveryResponse acceptDelivery(Long deliveryId, User currentUser) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + deliveryId));

        validatePartnerOwnership(delivery, currentUser);

        if (delivery.getDeliveryStatus() != DeliveryStatus.ASSIGNED) {
            throw new BadRequestException("Delivery is not in ASSIGNED state");
        }

        delivery.setDeliveryStatus(DeliveryStatus.ACCEPTED);
        return mapToDeliveryResponse(deliveryRepository.save(delivery));
    }

    @Transactional
    public DeliveryResponse pickupDelivery(Long deliveryId, User currentUser) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + deliveryId));

        validatePartnerOwnership(delivery, currentUser);

        if (delivery.getDeliveryStatus() != DeliveryStatus.ACCEPTED && delivery.getDeliveryStatus() != DeliveryStatus.ASSIGNED) {
            throw new BadRequestException("Delivery cannot be picked up from current state: " + delivery.getDeliveryStatus());
        }

        LocalDateTime now = LocalDateTime.now();
        delivery.setPickupTime(now);
        delivery.setDeliveryStartTime(now);
        delivery.setDeliveryStatus(DeliveryStatus.DELIVERING);

        Order order = delivery.getOrder();
        order.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
        orderRepository.save(order);

        return mapToDeliveryResponse(deliveryRepository.save(delivery));
    }

    @Transactional
    public DeliveryResponse completeDelivery(Long deliveryId, User currentUser) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + deliveryId));

        validatePartnerOwnership(delivery, currentUser);

        if (delivery.getDeliveryStatus() != DeliveryStatus.DELIVERING && delivery.getDeliveryStatus() != DeliveryStatus.PICKED_UP) {
            throw new BadRequestException("Delivery cannot be completed from current state: " + delivery.getDeliveryStatus());
        }

        LocalDateTime deliveredTime = LocalDateTime.now();
        delivery.setDeliveredTime(deliveredTime);
        delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);

        // Duration calculation
        LocalDateTime startTime = delivery.getDeliveryStartTime() != null ? delivery.getDeliveryStartTime() : delivery.getPickupTime();
        long durationMinutes = (startTime != null) ? Math.max(1, Duration.between(startTime, deliveredTime).toMinutes()) : 15;

        // Base distance calculation (Section 5)
        Double baseEarning = deliveryRuleService.calculateBaseEarning(delivery.getDistanceKm());
        delivery.setBaseEarning(baseEarning);

        // Time bonus calculation (Section 6)
        Double bonusAmount = deliveryRuleService.calculateBonusEarning(durationMinutes);
        delivery.setBonusAmount(bonusAmount);

        // Total earning
        delivery.setTotalEarning(baseEarning + bonusAmount);

        // Update Order
        Order order = delivery.getOrder();
        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setDeliveredAt(deliveredTime);
        orderRepository.save(order);

        // Change partner: BUSY -> AVAILABLE (Section 25, Step 15)
        DeliveryPartner partner = delivery.getDeliveryPartner();
        partner.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        deliveryPartnerRepository.save(partner);

        return mapToDeliveryResponse(deliveryRepository.save(delivery));
    }

    private void validatePartnerOwnership(Delivery delivery, User currentUser) {
        if (currentUser.getRole() == Role.ROLE_HOTEL_ADMIN) {
            return;
        }
        if (!delivery.getDeliveryPartner().getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You are not authorized to update this delivery");
        }
    }

    public DeliveryPartnerResponse mapToPartnerResponse(DeliveryPartner partner) {
        return new DeliveryPartnerResponse(
                partner.getId(),
                partner.getUser().getId(),
                partner.getUser().getName(),
                partner.getUser().getEmail(),
                partner.getUser().getPhone(),
                partner.getVehicleType(),
                partner.getVehicleNumber(),
                partner.getAvailabilityStatus(),
                partner.getActive(),
                partner.getJoinedAt()
        );
    }

    public DeliveryResponse mapToDeliveryResponse(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getOrder().getId(),
                delivery.getDeliveryPartner().getId(),
                delivery.getDeliveryPartner().getUser().getName(),
                delivery.getPickupTime(),
                delivery.getDeliveryStartTime(),
                delivery.getDeliveredTime(),
                delivery.getDistanceKm(),
                delivery.getBaseEarning(),
                delivery.getBonusAmount(),
                delivery.getTotalEarning(),
                delivery.getDeliveryStatus()
        );
    }
}
