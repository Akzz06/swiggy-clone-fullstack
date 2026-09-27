package com.akash.akashhotels.service;

import com.akash.akashhotels.dto.CartItemRequest;
import com.akash.akashhotels.dto.CartItemResponse;
import com.akash.akashhotels.dto.CartResponse;
import com.akash.akashhotels.entity.Cart;
import com.akash.akashhotels.entity.CartItem;
import com.akash.akashhotels.entity.Dish;
import com.akash.akashhotels.entity.User;
import com.akash.akashhotels.exception.BadRequestException;
import com.akash.akashhotels.exception.ResourceNotFoundException;
import com.akash.akashhotels.repository.CartItemRepository;
import com.akash.akashhotels.repository.CartRepository;
import com.akash.akashhotels.repository.DishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final DishRepository dishRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       DishRepository dishRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.dishRepository = dishRepository;
    }

    @Transactional
    public Cart getOrCreateCart(User customer) {
        return cartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional(readOnly = true)
    public CartResponse getCartResponse(User customer) {
        Cart cart = getOrCreateCart(customer);
        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse addItemToCart(User customer, CartItemRequest request) {
        Dish dish = dishRepository.findById(request.getDishId())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id: " + request.getDishId()));

        if (Boolean.FALSE.equals(dish.getAvailable())) {
            throw new BadRequestException("Dish is currently not available");
        }

        Cart cart = getOrCreateCart(customer);

        CartItem existingItem = cartItemRepository.findByCartIdAndDishId(cart.getId(), dish.getId())
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setDish(dish);
            newItem.setQuantity(request.getQuantity());
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse updateCartItem(User customer, Long itemId, int quantity) {
        Cart cart = getOrCreateCart(customer);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to your cart");
        }

        if (quantity <= 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse removeCartItem(User customer, Long itemId) {
        Cart cart = getOrCreateCart(customer);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to your cart");
        }

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        return mapToResponse(cart);
    }

    @Transactional
    public void clearCart(User customer) {
        Cart cart = getOrCreateCart(customer);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getId(),
                        item.getDish().getId(),
                        item.getDish().getName(),
                        item.getDish().getPrice(),
                        item.getQuantity(),
                        item.getDish().getPrice() * item.getQuantity()
                ))
                .collect(Collectors.toList());

        double totalAmount = itemResponses.stream()
                .mapToDouble(CartItemResponse::getSubtotal)
                .sum();

        return new CartResponse(cart.getId(), cart.getCustomer().getId(), itemResponses, totalAmount);
    }
}
