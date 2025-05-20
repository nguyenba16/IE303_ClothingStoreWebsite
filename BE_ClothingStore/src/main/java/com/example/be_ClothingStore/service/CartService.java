package com.example.be_ClothingStore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.bson.types.ObjectId;

import com.example.be_ClothingStore.domain.Carts;
import com.example.be_ClothingStore.domain.Users;
import com.example.be_ClothingStore.repository.CartRepository;
import com.example.be_ClothingStore.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartService {
    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private UserRepository userRepo;

    public Carts getCartByUser(String userId) {
        log.info("Getting cart for user: {}", userId);
        ObjectId userObjectId = new ObjectId(userId);

        Optional<Carts> existingCart = cartRepo.findByUserID(userObjectId);
        if (existingCart.isPresent()) {
            log.info("Found existing cart for user: {}", userId);
            return existingCart.get();
        }

        log.info("No cart found for user: {}, creating new cart", userId);
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Carts newCart = new Carts();
        newCart.setUserID(user);
        return cartRepo.save(newCart);
    }

    public Carts addItemToCart(String userId, String productId, String size, String color, int quantity) {
        log.info("Adding item to cart - User: {}, Product: {}, Size: {}, Color: {}, Quantity: {}",
                userId, productId, size, color, quantity);
        ObjectId userObjectId = new ObjectId(userId);

        Optional<Carts> existingCart = cartRepo.findByUserID(userObjectId);
        Carts cart;

        if (existingCart.isPresent()) {
            log.info("Found existing cart for user: {}", userId);
            cart = existingCart.get();
        } else {
            log.info("No cart found for user: {}, creating new cart", userId);
            Users user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            cart = new Carts();
            cart.setUserID(user);
        }

        cart.addItem(productId, size, color, quantity);
        return cartRepo.save(cart);
    }

    public Carts removeItemFromCart(String userId, String productId, String size, String color) {
        log.info("Removing item from cart - User: {}, Product: {}, Size: {}, Color: {}",
                userId, productId, size, color);
        ObjectId userObjectId = new ObjectId(userId);

        Carts cart = cartRepo.findByUserID(userObjectId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.removeItem(productId, size, color);
        return cartRepo.save(cart);
    }

    public Carts updateItemQuantity(String userId, String productId, String size, String color, int quantity) {
        log.info("Updating item quantity - User: {}, Product: {}, Size: {}, Color: {}, Quantity: {}",
                userId, productId, size, color, quantity);
        ObjectId userObjectId = new ObjectId(userId);

        Carts cart = cartRepo.findByUserID(userObjectId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.updateItemQuantity(productId, size, color, quantity);
        return cartRepo.save(cart);
    }

    public void clearCart(String userId) {
        log.info("Clearing cart for user: {}", userId);
        ObjectId userObjectId = new ObjectId(userId);

        Carts cart = cartRepo.findByUserID(userObjectId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.clear();
        cartRepo.save(cart);
    }

    public void deleteCart(String userId) {
        log.info("Deleting cart for user: {}", userId);
        ObjectId userObjectId = new ObjectId(userId);
        cartRepo.deleteByUserID(userObjectId);
    }
}
