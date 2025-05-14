package com.example.be_ClothingStore.domain.ChatBox;

public class PromptRequest {
    private String role;
    private String requestText;
    private String sessionId;
    private String responseText;
    
    public PromptRequest(){}
    public PromptRequest( String sessionId, String role, String requestText,String responseText){
        this.responseText =responseText;
        this.role = role;
        this.requestText = requestText;
        this.sessionId = sessionId;
    }
   

    public String getRequestText() {
        return requestText;
    }


    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }


    public String getResponseText() {
        return responseText;
    }


    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
