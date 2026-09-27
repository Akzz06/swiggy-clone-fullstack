package com.akash.akashhotels.config;

import com.akash.akashhotels.entity.*;
import com.akash.akashhotels.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final HotelRepository hotelRepository;
    private final DeliveryLocationRepository locationRepository;
    private final DeliveryDistanceRuleRepository distanceRuleRepository;
    private final DeliveryBonusRuleRepository bonusRuleRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final CartRepository cartRepository;
    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(HotelRepository hotelRepository,
                           DeliveryLocationRepository locationRepository,
                           DeliveryDistanceRuleRepository distanceRuleRepository,
                           DeliveryBonusRuleRepository bonusRuleRepository,
                           DishRepository dishRepository,
                           UserRepository userRepository,
                           CustomerProfileRepository customerProfileRepository,
                           CartRepository cartRepository,
                           DeliveryPartnerRepository deliveryPartnerRepository,
                           PasswordEncoder passwordEncoder) {
        this.hotelRepository = hotelRepository;
        this.locationRepository = locationRepository;
        this.distanceRuleRepository = distanceRuleRepository;
        this.bonusRuleRepository = bonusRuleRepository;
        this.dishRepository = dishRepository;
        this.userRepository = userRepository;
        this.customerProfileRepository = customerProfileRepository;
        this.cartRepository = cartRepository;
        this.deliveryPartnerRepository = deliveryPartnerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Hotel hotel = initHotel();
        initLocations();
        initDeliveryRules();
        initDishes(hotel);
        initUsers();
    }

    private Hotel initHotel() {
        List<Hotel> existing = hotelRepository.findAll();
        if (!existing.isEmpty()) {
            return existing.get(0);
        }
        Hotel hotel = new Hotel();
        hotel.setName("Akash Hotels");
        hotel.setAddress("Guindy");
        hotel.setCity("Chennai");
        hotel.setLatitude(13.0067);
        hotel.setLongitude(80.2206);
        hotel.setActive(true);
        hotel.setCreatedAt(LocalDateTime.now());
        return hotelRepository.save(hotel);
    }

    private void initLocations() {
        if (locationRepository.count() > 0) {
            return;
        }

        List<DeliveryLocation> locations = Arrays.asList(
                new DeliveryLocation(null, "Guindy", "Chennai", 2.0, true),
                new DeliveryLocation(null, "Saidapet", "Chennai", 4.0, true),
                new DeliveryLocation(null, "T. Nagar", "Chennai", 5.0, true),
                new DeliveryLocation(null, "Adyar", "Chennai", 7.0, true),
                new DeliveryLocation(null, "Velachery", "Chennai", 8.0, true),
                new DeliveryLocation(null, "Kodambakkam", "Chennai", 8.5, true),
                new DeliveryLocation(null, "Besant Nagar", "Chennai", 9.0, true),
                new DeliveryLocation(null, "Mylapore", "Chennai", 9.5, true),
                new DeliveryLocation(null, "Nungambakkam", "Chennai", 10.0, true),
                new DeliveryLocation(null, "Egmore", "Chennai", 11.0, true),
                new DeliveryLocation(null, "Porur", "Chennai", 11.5, true),
                new DeliveryLocation(null, "Anna Nagar", "Chennai", 12.0, true),
                new DeliveryLocation(null, "Tambaram", "Chennai", 14.0, true),
                new DeliveryLocation(null, "OMR", "Chennai", 14.5, true),
                new DeliveryLocation(null, "Sholinganallur", "Chennai", 16.0, true)
        );

        locationRepository.saveAll(locations);
    }

    private void initDeliveryRules() {
        if (distanceRuleRepository.count() == 0) {
            List<DeliveryDistanceRule> distanceRules = Arrays.asList(
                    new DeliveryDistanceRule(null, 0.0, 3.0, 30.0, true),
                    new DeliveryDistanceRule(null, 3.0, 5.0, 40.0, true),
                    new DeliveryDistanceRule(null, 5.0, 8.0, 50.0, true),
                    new DeliveryDistanceRule(null, 8.0, 12.0, 65.0, true),
                    new DeliveryDistanceRule(null, 12.0, 100.0, 80.0, true)
            );
            distanceRuleRepository.saveAll(distanceRules);
        }

        if (bonusRuleRepository.count() == 0) {
            List<DeliveryBonusRule> bonusRules = Arrays.asList(
                    new DeliveryBonusRule(null, 15, 20.0, true),
                    new DeliveryBonusRule(null, 30, 15.0, true),
                    new DeliveryBonusRule(null, 45, 10.0, true),
                    new DeliveryBonusRule(null, 60, 5.0, true)
            );
            bonusRuleRepository.saveAll(bonusRules);
        }
    }

    private void initDishes(Hotel hotel) {
        if (dishRepository.count() > 0) {
            return;
        }

        List<Dish> dishes = Arrays.asList(
                new Dish(null, hotel, "Thalappakatti Chicken Biryani", "Aromatic seeraga samba rice cooked with tender chicken pieces and authentic Chettinad spices.", 240.0, "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8", "Biryani", true),
                new Dish(null, hotel, "Chettinad Mutton Biryani", "Flavor-packed slow-cooked mutton biryani served with spicy gravy and raita.", 320.0, "https://images.unsplash.com/photo-1589302168068-964664d93dc0", "Biryani", true),
                new Dish(null, hotel, "Ghee Roast Dosa", "Crispy golden crepe roasted generously in pure ghee, served with 3 chutneys and sambar.", 95.0, "https://images.unsplash.com/photo-1668236543090-82eba5ee5976", "Tiffin", true),
                new Dish(null, hotel, "Madurai Bun Parotta", "Flaky, fluffy multi-layered parotta crispy outside and tender inside, served with vegetable salna.", 75.0, "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46", "Breads", true),
                new Dish(null, hotel, "Chicken Sukka", "Dry roasted spicy chicken tempered with curry leaves and black pepper.", 190.0, "https://images.unsplash.com/photo-1610057099431-d73a1c9d2f2f", "Starters", true),
                new Dish(null, hotel, "Mutton Chukka", "Boneless succulent mutton cubes pan-roasted in village-style ground spices.", 260.0, "https://images.unsplash.com/photo-1544025162-d76694265947", "Starters", true),
                new Dish(null, hotel, "Paneer Butter Masala", "Cottage cheese simmered in rich velvety tomato cashew gravy with fenugreek.", 180.0, "https://images.unsplash.com/photo-1631452180519-c014fe946bc7", "Curry", true),
                new Dish(null, hotel, "South Indian Special Meals", "Traditional banana leaf feast: rice, sambar, rasam, kootu, poriyal, appalam, curd and payasam.", 160.0, "https://images.unsplash.com/photo-1610192244261-3f33de3f55e4", "Meals", true),
                new Dish(null, hotel, "Chicken 65", "Classic spicy deep-fried chicken cubes garnished with curry leaves and green chilies.", 170.0, "https://images.unsplash.com/photo-1606471191009-63994c53433b", "Starters", true),
                new Dish(null, hotel, "Filter Coffee", "Frothy authentic South Indian filter coffee brewed with chicory-blend decoction and fresh milk.", 35.0, "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd", "Beverages", true),
                new Dish(null, hotel, "Mango Lassi", "Thick chilled yogurt drink blended with sweet Alphonso mango pulp.", 60.0, "https://images.unsplash.com/photo-1571091718767-18b5b1457add", "Beverages", true)
        );

        dishRepository.saveAll(dishes);
    }

    private void initUsers() {
        // Admin
        if (userRepository.findByEmail("admin@akashhotels.com").isEmpty()) {
            User admin = new User();
            admin.setName("Hotel Admin");
            admin.setEmail("admin@akashhotels.com");
            admin.setPasswordHash(passwordEncoder.encode("Admin@123"));
            admin.setPhone("9876543210");
            admin.setRole(Role.ROLE_HOTEL_ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
        }

        // Customer
        if (userRepository.findByEmail("customer@gmail.com").isEmpty()) {
            User customer = new User();
            customer.setName("Akash Customer");
            customer.setEmail("customer@gmail.com");
            customer.setPasswordHash(passwordEncoder.encode("Customer@123"));
            customer.setPhone("9841234567");
            customer.setRole(Role.ROLE_CUSTOMER);
            customer.setActive(true);
            User savedCustomer = userRepository.save(customer);

            CustomerProfile profile = new CustomerProfile();
            profile.setUser(savedCustomer);
            profile.setAddressDetails("Flat 4B, Emerald Apartments, Guindy, Chennai");
            customerProfileRepository.save(profile);

            Cart cart = new Cart();
            cart.setCustomer(savedCustomer);
            cartRepository.save(cart);
        }

        // Delivery Partner 1 (AVAILABLE)
        if (userRepository.findByEmail("partner1@akashhotels.com").isEmpty()) {
            User partnerUser1 = new User();
            partnerUser1.setName("Ramesh Kumar");
            partnerUser1.setEmail("partner1@akashhotels.com");
            partnerUser1.setPasswordHash(passwordEncoder.encode("Partner@123"));
            partnerUser1.setPhone("9840112233");
            partnerUser1.setRole(Role.ROLE_DELIVERY_PARTNER);
            partnerUser1.setActive(true);
            User savedPartner1 = userRepository.save(partnerUser1);

            DeliveryPartner partner1 = new DeliveryPartner();
            partner1.setUser(savedPartner1);
            partner1.setVehicleType("Motorcycle");
            partner1.setVehicleNumber("TN-09-AB-1234");
            partner1.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
            partner1.setActive(true);
            deliveryPartnerRepository.save(partner1);
        }

        // Delivery Partner 2 (AVAILABLE)
        if (userRepository.findByEmail("partner2@akashhotels.com").isEmpty()) {
            User partnerUser2 = new User();
            partnerUser2.setName("Suresh Raina");
            partnerUser2.setEmail("partner2@akashhotels.com");
            partnerUser2.setPasswordHash(passwordEncoder.encode("Partner@123"));
            partnerUser2.setPhone("9840998877");
            partnerUser2.setRole(Role.ROLE_DELIVERY_PARTNER);
            partnerUser2.setActive(true);
            User savedPartner2 = userRepository.save(partnerUser2);

            DeliveryPartner partner2 = new DeliveryPartner();
            partner2.setUser(savedPartner2);
            partner2.setVehicleType("Electric Scooter");
            partner2.setVehicleNumber("TN-09-CD-5678");
            partner2.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
            partner2.setActive(true);
            deliveryPartnerRepository.save(partner2);
        }
    }
}
