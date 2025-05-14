package com.example.be_ClothingStore.controller.Customer;

import com.example.be_ClothingStore.domain.ChatBox.PromptRequest;
import com.example.be_ClothingStore.service.ChatBoxService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/customer")
public class ChatBoxController {
    private final ChatBoxService chatBoxService;
    public ChatBoxController (ChatBoxService chatBoxService){
        this.chatBoxService= chatBoxService;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> chatWithGemini(@RequestBody PromptRequest request) {
        String responseMessage = this.chatBoxService.sendMessage(request.getSessionId(), request.getRequestText());
        PromptRequest res = new PromptRequest();
        res.setRequestText(request.getRequestText());
        res.setResponseText(responseMessage);
        res.setRole(request.getRole());
        res.setSessionId(request.getSessionId());
        return ResponseEntity.ok().body(res);
    }
    
}
