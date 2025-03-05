package com.example.be_ClothingStore.domain.RestResponse;

public class RestResponse<T> {
    private int statusCode;
    private String error;
    // Vì mess có thể là string hoặc arrayList
    private Object message;
    // Vì data chưa biết nó sẽ ntn nên dùng generate
    private T data;
    
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public Object getMessage() {
        return message;
    }
    public void setMessage(Object message) {
        this.message = message;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

    
}
