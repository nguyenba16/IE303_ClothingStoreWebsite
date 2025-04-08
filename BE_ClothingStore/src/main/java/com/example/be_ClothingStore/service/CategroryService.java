package com.example.be_ClothingStore.service;

import org.springframework.stereotype.Service;

import com.example.be_ClothingStore.domain.Categrories;
import com.example.be_ClothingStore.repository.CategroryRepository;

@Service
public class CategroryService {
    private final CategroryRepository categroryRepository;
    public CategroryService (CategroryRepository categroryRepository){
        this.categroryRepository = categroryRepository;
    }

    public Categrories addANewCategrory(Categrories categrories){
        return this.categroryRepository.save(categrories);
    }
}
