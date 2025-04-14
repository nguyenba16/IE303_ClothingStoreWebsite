package com.example.be_ClothingStore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.be_ClothingStore.domain.Categrories;
import com.example.be_ClothingStore.repository.CategroryRepository;

@Service
public class CategoryService {
    private final CategroryRepository categroryRepository;
    public CategoryService (CategroryRepository categroryRepository){
        this.categroryRepository = categroryRepository;
    }

    public List<Categrories> fetchAllCategories() {
        return this.categroryRepository.findAll();
    }
    public Categrories addANewCategrory(Categrories categrories){
        return this.categroryRepository.save(categrories);
    }

    public Categrories findByCateName(String categroryName){
        Optional<Categrories> cate = this.categroryRepository.findByCategroryName(categroryName);
        if (!cate.isPresent()) {
            return null;
        }
        return cate.get();
    }
}
