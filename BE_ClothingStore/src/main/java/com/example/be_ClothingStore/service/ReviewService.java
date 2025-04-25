package com.example.be_ClothingStore.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import com.example.be_ClothingStore.domain.Reviews;
import com.example.be_ClothingStore.repository.ReviewRepository;
import com.example.be_ClothingStore.service.error.IdInvalidException;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    public ReviewService (ReviewRepository reviewRepository, ProductService productService){
        this.reviewRepository = reviewRepository;
        this.productService = productService;
    }

    public Reviews addAReview(Reviews reviews) throws IdInvalidException{
        Reviews review = this.reviewRepository.save(reviews);

        // Cập nhật lại số rating cho product
        String productID = reviews.getProductID();
         List<Reviews> reviewsOfProduct = this.getReviewsOfProduct(productID);
        this.productService.updateProductRating(productID, reviewsOfProduct);
        return review;
    }

    public Boolean deleteAReview(String reviewId, String userId) throws IdInvalidException{
        ObjectId objReviewId = new ObjectId(reviewId);
        Optional<Reviews> Review = this.reviewRepository.findById(objReviewId);
        if (!Review.isPresent()){
            return false;
        }
        Reviews review = Review.get(); 
        if (!review.getUserID().getId().equals(userId)){
            return false;
        }
        this.reviewRepository.deleteById(reviewId);

        // Cập nhật lại số rating cho product
        String productID = review.getProductID();
        List<Reviews> reviewsOfProduct = this.getReviewsOfProduct(productID);
        this.productService.updateProductRating(productID, reviewsOfProduct);
        return true;
    }

    public List<Reviews> getReviewsOfProduct(String productId){
        return this.reviewRepository.findByProductID(productId);
    }
}
