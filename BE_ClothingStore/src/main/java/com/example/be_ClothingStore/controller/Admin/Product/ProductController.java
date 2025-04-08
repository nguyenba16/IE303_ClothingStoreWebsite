package com.example.be_ClothingStore.controller.Admin.Product;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.be_ClothingStore.domain.Categrories;
import com.example.be_ClothingStore.domain.Image;
import com.example.be_ClothingStore.domain.Products;
import com.example.be_ClothingStore.domain.RestResponse.RestResponse;
import com.example.be_ClothingStore.repository.CategroryRepository;
import com.example.be_ClothingStore.repository.ProductRepository;
import com.example.be_ClothingStore.service.CloudinaryService;
import com.example.be_ClothingStore.service.ProductService;
import com.example.be_ClothingStore.service.error.IdInvalidException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/admin")
public class ProductController {

    private final ProductRepository productRepository;

    private final CategroryRepository categroryRepository;
    private final CloudinaryService cloudinaryService;
    private final ProductService productService;

    @Autowired
    private CategroryRepository categoryRepository;
    public ProductController(ProductService productService, CloudinaryService cloudinaryService, CategroryRepository categroryRepository, ProductRepository productRepository){
        this.productService = productService;
        this.cloudinaryService = cloudinaryService;
        this.categroryRepository = categroryRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/products/add")
    public ResponseEntity<?> postAProduct(@RequestParam("categroryID") String categroryID,
                            @RequestParam("productName") String productName,
                            @RequestParam("price") Double price,
                            @RequestParam("stock") Integer stock,
                            @RequestParam("desc") String desc,
                            @RequestParam("rating") Float rating,
                            @RequestParam("sizes") List<String> sizes,
                            @RequestParam("colors") List<String> colors,
                            @RequestPart("productImage") List<MultipartFile> productImages) throws IdInvalidException {
        
        Optional<Categrories> categoryOpt = categoryRepository.findById(categroryID);
        if (categoryOpt.isEmpty()) {
            throw new IdInvalidException("Không tìm thấy categrory");
        }

        Categrories category = categoryOpt.get();

        Products newProduct = new Products();
        newProduct.setCategrory(category);
        newProduct.setProductName(productName);
        newProduct.setPrice(price);
        newProduct.setStock(stock);
        newProduct.setDesc(desc);
        newProduct.setRating(rating);
        newProduct.setSizes(sizes);
        newProduct.setColors(colors);

        List<Image> uploadedImages = new ArrayList<>();
        System.out.println("productImages.size()==========================: " + productImages.size());
        for (MultipartFile image : productImages) {
            System.out.println("Received image: " + image.getOriginalFilename());
        }
        for (int i = 0; i < productImages.size(); i++) {
            MultipartFile image = productImages.get(i);
            String color = colors.get(i);
            Image img = cloudinaryService.uploadImage(image, color, null);
            uploadedImages.add(img);
        }
        newProduct.setProductImage(uploadedImages.toArray(new Image[0]));
        Products productAdded = productService.addAProduct(newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(productAdded);
    }

    @PatchMapping(value = "/products/update/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(@PathVariable("productId") String productId,
                            @RequestParam(value = "sizes", required = false) List<String> sizes,
                            @RequestParam(value = "colors", required = false) List<String> colors, 
                            @RequestParam(value = "categroryID", required = false) String categroryID,
                            @RequestParam(value = "productName", required = false) String productName,
                            @RequestParam(value = "price", required = false) Double price,
                            @RequestParam(value = "stock", required = false) Integer stock,
                            @RequestParam(value = "desc", required = false) String desc,
                            @RequestParam(value = "rating", required = false) Float rating,
                            @RequestParam(value = "productImage", required = false) List<MultipartFile> productImage)throws IdInvalidException{
        Products products = productService.handleFetchProduct(productId);
        if (products == null) {
            throw new IdInvalidException("Không tìm thấy sản phẩm nào hợp lệ với id!");
        }
        Image[] oldImages = products.getProductImage();
        
        if (categroryID != null){
            Optional<Categrories> categoryOpt = categoryRepository.findById(categroryID);
            if (categoryOpt.isEmpty()) {
                throw new IdInvalidException("Không tìm thấy categrory với id trên!");
            }
            Categrories category = categoryOpt.get();
            products.setCategrory(category);
        }
        if (colors != null){
            products.setColors(colors);
            for(int i =0; i<colors.size(); i++){
                Image[] images = products.getProductImage();
                Image img = images[i];
                img.setColor(colors.get(i));
            }
        }
        if (productName!= null){
            products.setProductName(productName);
        }
        if (price != null){
            products.setPrice(price);
        }
        if (stock != null){
            products.setStock(stock);
        }
        if (desc != null){
            products.setDesc(desc);
        }
        if (rating != null){
            products.setRating(rating);
        }
        if (sizes != null){
            products.setSizes(sizes);
        }
        if (productImage != null && !productImage.isEmpty()){
            List<Image> uploadedImages = new ArrayList<>();
            for (int i = 0; i < productImage.size(); i++) {
                if(productImage.get(i) != oldImages[i]){
                    MultipartFile image = productImage.get(i);
                    String color = colors.get(i);
                    String oldImageId = (oldImages != null && i < oldImages.length) ? oldImages[i].getPublicId() : null;
                    Image img = cloudinaryService.uploadImage(image, color, oldImageId);
                    uploadedImages.add(img);
                }
            }
            products.setProductImage(uploadedImages.toArray(new Image[0]));
        }
        Products updatedProduct = productService.addAProduct(products);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @DeleteMapping("/products/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") String productId) {
        try {
            if (this.productService.deleteProduct(productId)) {
                RestResponse<?> response = new RestResponse<>(200, null, "Đã xóa sản phẩm thành công!", null);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (IdInvalidException ex) {
            RestResponse<?> response = new RestResponse<>(400, ex.getMessage(), ex.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
