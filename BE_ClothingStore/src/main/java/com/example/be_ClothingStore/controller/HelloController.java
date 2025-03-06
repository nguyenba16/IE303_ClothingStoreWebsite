package com.example.be_ClothingStore.controller;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// import com.example.be_ClothingStore.domain.User;

@RestController
public class HelloController {

    // private final TestRepository testRepository;

    // public HelloController(TestRepository testRepository) {
    //     this.testRepository = testRepository;
    // }

    @GetMapping("/")
    public String getHelloWorld() {
        return "Chào nhé!!";
    }

    // @PostMapping("/test")
    // public String postMethodName(@RequestBody User user) {
    //     testRepository.save(user);
    //     return "ngonn";
    // }
}
