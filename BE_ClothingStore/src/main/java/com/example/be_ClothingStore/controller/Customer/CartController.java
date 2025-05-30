package com.example.be_ClothingStore.controller.Customer;

import com.example.be_ClothingStore.domain.Carts;
import com.example.be_ClothingStore.service.CartService;
import com.example.be_ClothingStore.service.error.IdInvalidException;
import com.example.be_ClothingStore.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/cart")
public class CartController {

    private final CartService cartService;
    private final SecurityUtil securityUtil;

    public CartController(CartService cartService, SecurityUtil securityUtil) {
        this.cartService = cartService;
        this.securityUtil = securityUtil;
    }

    // 1. Lấy giỏ hàng hiện tại của người dùng
    @GetMapping
    public ResponseEntity<Carts> getCart(HttpServletRequest request) throws IdInvalidException {
        String token = securityUtil.getTokenFromCookie(request);
        String userId = securityUtil.getUserIdFromToken(token);
        return ResponseEntity.ok(cartService.getCartByUser(userId));
    }

    // 2. Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add")
    public ResponseEntity<Carts> addItemToCart(
            @RequestParam(name = "productId") String productId,
            @RequestParam(name = "size") String size,
            @RequestParam(name = "color") String color,
            @RequestParam(name = "quantity") int quantity,
            HttpServletRequest request) throws IdInvalidException {
        String token = securityUtil.getTokenFromCookie(request);
        String userId = securityUtil.getUserIdFromToken(token);
        return ResponseEntity.ok(cartService.addItemToCart(userId, productId, size, color, quantity));
    }

    // 3. Xóa sản phẩm khỏi giỏ hàng
    @DeleteMapping("/remove")
    public ResponseEntity<Carts> removeItemFromCart(
            @RequestParam(name = "productId") String productId,
            @RequestParam(name = "size") String size,
            @RequestParam(name = "color") String color,
            HttpServletRequest request) throws IdInvalidException {
        String token = securityUtil.getTokenFromCookie(request);
        String userId = securityUtil.getUserIdFromToken(token);
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, productId, size, color));
    }

    // 4. Cập nhật số lượng sản phẩm
    @PutMapping("/update")
    public ResponseEntity<Carts> updateItemQuantity(
            @RequestParam(name = "productId") String productId,
            @RequestParam(name = "size") String size,
            @RequestParam(name = "color") String color,
            @RequestParam(name = "quantity") int quantity,
            HttpServletRequest request) throws IdInvalidException {
        String token = securityUtil.getTokenFromCookie(request);
        String userId = securityUtil.getUserIdFromToken(token);
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, productId, size, color, quantity));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(HttpServletRequest request) throws IdInvalidException {
        String token = securityUtil.getTokenFromCookie(request);
        String userId = securityUtil.getUserIdFromToken(token);
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCart(HttpServletRequest request) throws IdInvalidException {
        String token = securityUtil.getTokenFromCookie(request);
        String userId = securityUtil.getUserIdFromToken(token);
        cartService.deleteCart(userId);
        return ResponseEntity.ok().build();
    }
}