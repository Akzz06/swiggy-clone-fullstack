package com.akash.akashhotels.service;

import com.akash.akashhotels.dto.AuthResponse;
import com.akash.akashhotels.dto.LoginRequest;
import com.akash.akashhotels.dto.RegisterRequest;
import com.akash.akashhotels.entity.Cart;
import com.akash.akashhotels.entity.CustomerProfile;
import com.akash.akashhotels.entity.Role;
import com.akash.akashhotels.entity.User;
import com.akash.akashhotels.exception.BadRequestException;
import com.akash.akashhotels.exception.ResourceNotFoundException;
import com.akash.akashhotels.repository.CartRepository;
import com.akash.akashhotels.repository.CustomerProfileRepository;
import com.akash.akashhotels.repository.UserRepository;
import com.akash.akashhotels.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       CustomerProfileRepository customerProfileRepository,
                       CartRepository cartRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.customerProfileRepository = customerProfileRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use: " + request.getEmail());
        }

        Role role = request.getRole() != null ? request.getRole() : Role.ROLE_CUSTOMER;

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(role);
        user.setActive(true);

        User savedUser = userRepository.save(user);

        // If customer, initialize profile and cart
        if (role == Role.ROLE_CUSTOMER) {
            CustomerProfile profile = new CustomerProfile();
            profile.setUser(savedUser);
            customerProfileRepository.save(profile);

            Cart cart = new Cart();
            cart.setCustomer(savedUser);
            cartRepository.save(cart);
        }

        String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getId(), savedUser.getRole().name());

        return new AuthResponse(token, savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(user.getEmail(), user.getId(), user.getRole().name());

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }
}
