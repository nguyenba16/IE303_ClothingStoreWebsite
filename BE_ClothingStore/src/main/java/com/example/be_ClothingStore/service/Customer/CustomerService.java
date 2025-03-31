package com.example.be_ClothingStore.service.Customer;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.example.be_ClothingStore.domain.Users;
import com.example.be_ClothingStore.repository.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Users handleFetchUser(String id){
        // Ép id thành kiểu objectid
        ObjectId objectId = new ObjectId(id);
        Optional<Users> userOptional = this.customerRepository.findById(objectId);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        return null;
    }
    public Users updateUser (Users user){
        return customerRepository.save(user);
    }
}
