package com.example.be_ClothingStore.service;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import com.example.be_ClothingStore.domain.Reviews;
import com.example.be_ClothingStore.repository.ReviewRepository;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    public ReviewService (ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    public Reviews addAReview( Reviews reviews){
        Reviews review = this.reviewRepository.save(reviews);
        return review;
    }

    public Boolean deleteAReview(String reviewId, String userId){
        ObjectId objReviewId = new ObjectId(reviewId);
        Reviews review = this.reviewRepository.findById(objReviewId).get();
        if (review == null){
            return false;
        }
        if (!review.getUserID().getId().equals(userId)){
            return false;
        }
        this.reviewRepository.deleteById(reviewId);
        return true;
    }

    public List<Reviews> getReviewsOfProduct(String productId){
        return this.reviewRepository.findByProductID(productId);
    
    }
}
