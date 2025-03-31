package com.example.be_ClothingStore.domain;

public class Image {
        private String url;
        private String publicId;
        private String color;

        public Image(String url) {
            this.url = url;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
        public Image(){}
        public Image(String url, String publicId, String color) {
            this.url = url;
            this.publicId = publicId;
            this.color = color;
        }
        public Image (String url, String publicId){
            this.publicId= publicId;
            this.url = url;
        }
        public String getUrl() {
            return url;
        }
        public String getPublicId() {
            return publicId;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }
        
}
