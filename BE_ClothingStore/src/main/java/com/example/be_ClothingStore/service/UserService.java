package com.example.be_ClothingStore.service;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.be_ClothingStore.domain.Users;
import com.example.be_ClothingStore.repository.UserRepository;
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users handleCreateUser(Users user) {
        return userRepository.save(user);
    }

    public Users handleFetchUser(String id){
        // Ép id thành kiểu objectid
        ObjectId objectId = new ObjectId(id);
        Optional<Users> userOptional = this.userRepository.findById(objectId);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        return null;
    }
    public Users handleGetUserbyEmail(String email){
        Optional<Users> user = this.userRepository.findByEmail(email);
        System.out.println("user: " + email);
        if (user.isPresent()){
            return user.get();
        }
        return null;
    }
}
