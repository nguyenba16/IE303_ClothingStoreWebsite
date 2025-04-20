package com.example.be_ClothingStore.controller.Customer;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.example.be_ClothingStore.domain.Products;
import com.example.be_ClothingStore.domain.Reviews;
import com.example.be_ClothingStore.domain.Users;
import com.example.be_ClothingStore.domain.RestResponse.RestResponse;
import com.example.be_ClothingStore.repository.UserRepository;
import com.example.be_ClothingStore.service.ProductService;
import com.example.be_ClothingStore.service.ReviewService;
import com.example.be_ClothingStore.service.error.IdInvalidException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.be_ClothingStore.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("/customer/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;
    private final ProductService productService;
    public ReviewController (ProductService productService, SecurityUtil securityUtil,ReviewService reviewService, UserRepository userRepository){
        this.reviewService = reviewService;
        this.userRepository = userRepository;
        this.securityUtil = securityUtil;
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> postMethodName(@RequestBody Reviews reviews, HttpServletRequest request) throws IdInvalidException {
        Reviews Reviews = new Reviews();
        String token = this.securityUtil.getTokenFromCookie(request);
        if (token == null) {
            throw new IdInvalidException("Không tìm thấy token trong cookie!");
        }
        String userId = this.securityUtil.getUserIdFromToken(token);
        ObjectId userIdObj = new ObjectId(userId);
        Optional<Users> owner = this.userRepository.findById(userIdObj);
        if (!owner.isPresent()){
            throw new IdInvalidException("Không tìm thấy user với Id này!");
        }
        Optional<Products> products = this.productService.findProductById(reviews.getProductID());
        if (!products.isPresent()){
            throw new IdInvalidException("Không tìm thấy product với Id này!");
        }
        Reviews.setUserID(owner.get());
        Reviews.setComment(reviews.getComment());
        Reviews.setProductID(reviews.getProductID());
        Reviews.setRating(reviews.getRating());
        Reviews newReview = this.reviewService.addAReview(Reviews);
        return ResponseEntity.ok().body(newReview);
    }
    
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteAReviews(@PathVariable("reviewId") String reviewId, HttpServletRequest request) throws IdInvalidException{
        String token = this.securityUtil.getTokenFromCookie(request);
        if (token == null) {
            throw new IdInvalidException("Không tìm thấy token trong cookie!");
        }
        String userId = this.securityUtil.getUserIdFromToken(token);
        Boolean response = this.reviewService.deleteAReview(reviewId, userId);
        System.out.println("==asd=sad=sa======"+ response);
        if (response) {
            RestResponse<Reviews> res = new RestResponse<>(HttpStatus.OK.value(),
            null,"Đã xóa review thành công!", null);
           return ResponseEntity.ok().body(res);
        } else {
            RestResponse<Users> res = new RestResponse<>(HttpStatus.BAD_REQUEST.value(),
            "Không thể xóa review (sai reviewId hoặc k có quyền xóa id)","Không thể xóa review (sai reviewId hoặc k có quyền xóa id)", null);
            return ResponseEntity.badRequest().body(res);
        }
    }
    
    @GetMapping("/get-reviews/{productId}")
    public ResponseEntity<?> getMethodName(@PathVariable("productId") String productId) {
        List<Reviews> reviews = this.reviewService.getReviewsOfProduct(productId);
        return ResponseEntity.ok().body(reviews);
    }
    
}
