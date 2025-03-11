package com.example.be_ClothingStore.service.error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.be_ClothingStore.domain.RestResponse.RestResponse;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleIdException (IdInvalidException idException){
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(idException.getMessage());
        res.setMessage("Id Invalid Exception");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
