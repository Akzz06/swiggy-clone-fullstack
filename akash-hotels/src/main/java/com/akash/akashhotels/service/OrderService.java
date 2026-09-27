package com.akash.akashhotels.service;

import com.akash.akashhotels.dto.CreateOrderRequest;
import com.akash.akashhotels.dto.OrderItemResponse;
import com.akash.akashhotels.dto.OrderResponse;
import com.akash.akashhotels.entity.*;
import com.akash.akashhotels.exception.BadRequestException;
import com.akash.akashhotels.exception.DeliveryPartnerNotAvailableException;
import com.akash.akashhotels.exception.InvalidOrderStateException;
import com.akash.akashhotels.exception.ResourceNotFoundException;
import com.akash.akashhotels.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final DeliveryLocationRepository locationRepository;
    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;
    private final DeliveryRuleService deliveryRuleService;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        CartService cartService,
                        DeliveryLocationRepository locationRepository,
                        DeliveryPartnerRepository deliveryPartnerRepository,
                        DeliveryRepository deliveryRepository,
                        PaymentRepository paymentRepository,
                        DeliveryRuleService deliveryRuleService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.locationRepository = locationRepository;
        this.deliveryPartnerRepository = deliveryPartnerRepository;
        this.deliveryRepository = deliveryRepository;
        this.paymentRepository = paymentRepository;
        this.deliveryRuleService = deliveryRuleService;
    }

    @Transactional
    public OrderResponse placeOrder(User customer, CreateOrderRequest request) {
        Cart cart = cartService.getOrCreateCart(customer);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Cannot place order with an empty cart");
        }

        DeliveryLocation location = locationRepository.findById(request.getDeliveryLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Delivery location not found with id: " + request.getDeliveryLocationId()));

        if (Boolean.FALSE.equals(location.getActive())) {
            throw new BadRequestException("Delivery location is not currently serviceable");
        }

        // Validate items and compute subtotal
        double subtotal = 0.0;
        Hotel hotel = null;

        for (CartItem item : cart.getItems()) {
            Dish dish = item.getDish();
            if (Boolean.FALSE.equals(dish.getAvailable())) {
                throw new BadRequestException("Dish '" + dish.getName() + "' is currently unavailable");
            }
            if (hotel == null) {
                hotel = dish.getHotel();
            }
            subtotal += dish.getPrice() * item.getQuantity();
        }

        // Calculate delivery fee from distance rules
        Double deliveryFee = deliveryRuleService.calculateBaseEarning(location.getDistanceFromHotelKm());
        double totalAmount = subtotal + deliveryFee;

        // Create Order
        Order order = new Order();
        order.setCustomer(customer);
        order.setHotel(hotel);
        order.setDeliveryLocation(location);
        order.setOrderStatus(OrderStatus.PLACED);
        order.setSubtotal(subtotal);
        order.setDeliveryFee(deliveryFee);
        order.setTotalAmount(totalAmount);
        order.setPlacedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // Create OrderItems with frozen unit price
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setDish(cartItem.getDish());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getDish().getPrice());
            orderItem.setSubtotal(cartItem.getDish().getPrice() * cartItem.getQuantity());
            orderItems.add(orderItemRepository.save(orderItem));
        }
        savedOrder.setItems(orderItems);

        // Create Payment
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setAmount(totalAmount);
        PaymentMethod method = request.getPaymentMethod() != null ? request.getPaymentMethod() : PaymentMethod.MOCK_PAYMENT;
        payment.setPaymentMethod(method);
        payment.setPaymentStatus(PaymentStatus.SUCCESS); // simulated success for learning
        payment.setTransactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // Clear cart
        cartService.clearCart(customer);

        return mapToResponse(savedOrder);
    }

    public List<OrderResponse> getCustomerOrders(User customer) {
        return orderRepository.findByCustomerIdOrderByPlacedAtDesc(customer.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByPlacedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (order.getOrderStatus() == OrderStatus.DELIVERED || order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException("Cannot update status of an order that is already " + order.getOrderStatus());
        }

        if (newStatus == OrderStatus.CONFIRMED && order.getAcceptedAt() == null) {
            order.setAcceptedAt(LocalDateTime.now());
        } else if (newStatus == OrderStatus.DELIVERED && order.getDeliveredAt() == null) {
            order.setDeliveredAt(LocalDateTime.now());
        }

        order.setOrderStatus(newStatus);
        return mapToResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse assignDeliveryPartner(Long orderId, Long partnerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (order.getOrderStatus() == OrderStatus.DELIVERED || order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException("Cannot assign partner to order in status: " + order.getOrderStatus());
        }

        DeliveryPartner partner = deliveryPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not found with id: " + partnerId));

        if (Boolean.FALSE.equals(partner.getActive())) {
            throw new BadRequestException("Delivery partner is inactive");
        }

        // Business Rule (Section 3): A delivery partner can handle only one active delivery at a time
        if (partner.getAvailabilityStatus() != AvailabilityStatus.AVAILABLE) {
            throw new DeliveryPartnerNotAvailableException(
                    "Delivery partner " + partner.getUser().getName() + " is currently " +
                            partner.getAvailabilityStatus() + ". Only AVAILABLE partners can be assigned."
            );
        }

        // Change partner: AVAILABLE → BUSY (Section 25, Step 6)
        partner.setAvailabilityStatus(AvailabilityStatus.BUSY);
        deliveryPartnerRepository.save(partner);

        // Change order: → ASSIGNED (Section 25, Step 7)
        order.setDeliveryPartner(partner);
        order.setOrderStatus(OrderStatus.ASSIGNED);
        Order updatedOrder = orderRepository.save(order);

        // Create delivery record (Section 25, Step 8)
        Delivery delivery = deliveryRepository.findByOrderId(order.getId()).orElse(new Delivery());
        delivery.setOrder(order);
        delivery.setDeliveryPartner(partner);
        delivery.setDistanceKm(order.getDeliveryLocation().getDistanceFromHotelKm());
        delivery.setDeliveryStatus(DeliveryStatus.ASSIGNED);
        deliveryRepository.save(delivery);

        return mapToResponse(updatedOrder);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // If customer, verify ownership
        if (user.getRole() == Role.ROLE_CUSTOMER && !order.getCustomer().getId().equals(user.getId())) {
            throw new BadRequestException("You are not authorized to cancel this order");
        }

        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOrderStateException("Cannot cancel an already delivered order");
        }
        if (order.getOrderStatus() == OrderStatus.OUT_FOR_DELIVERY) {
            throw new InvalidOrderStateException("Cannot cancel an order that is already out for delivery");
        }

        // If partner was assigned, free partner back to AVAILABLE
        if (order.getDeliveryPartner() != null) {
            DeliveryPartner partner = order.getDeliveryPartner();
            partner.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
            deliveryPartnerRepository.save(partner);

            deliveryRepository.findByOrderId(order.getId()).ifPresent(delivery -> {
                delivery.setDeliveryStatus(DeliveryStatus.FAILED);
                deliveryRepository.save(delivery);
            });
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        return mapToResponse(orderRepository.save(order));
    }

    public OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setCustomerName(order.getCustomer().getName());
        response.setHotelId(order.getHotel() != null ? order.getHotel().getId() : null);
        response.setHotelName(order.getHotel() != null ? order.getHotel().getName() : null);
        response.setDeliveryLocationId(order.getDeliveryLocation() != null ? order.getDeliveryLocation().getId() : null);
        response.setDeliveryLocationName(order.getDeliveryLocation() != null ? order.getDeliveryLocation().getName() : null);

        if (order.getDeliveryPartner() != null) {
            response.setDeliveryPartnerId(order.getDeliveryPartner().getId());
            response.setDeliveryPartnerName(order.getDeliveryPartner().getUser().getName());
        }

        response.setOrderStatus(order.getOrderStatus());
        response.setSubtotal(order.getSubtotal());
        response.setDeliveryFee(order.getDeliveryFee());
        response.setTotalAmount(order.getTotalAmount());
        response.setPlacedAt(order.getPlacedAt());
        response.setAcceptedAt(order.getAcceptedAt());
        response.setDeliveredAt(order.getDeliveredAt());

        if (order.getItems() != null) {
            response.setItems(order.getItems().stream()
                    .map(item -> new OrderItemResponse(
                            item.getId(),
                            item.getDish().getId(),
                            item.getDish().getName(),
                            item.getQuantity(),
                            item.getUnitPrice(),
                            item.getSubtotal()
                    ))
                    .collect(Collectors.toList()));
        }

        return response;
    }
}
