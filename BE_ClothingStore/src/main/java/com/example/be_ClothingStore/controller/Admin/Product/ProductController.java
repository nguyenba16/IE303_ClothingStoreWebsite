package com.example.be_ClothingStore.controller.Admin.Product;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.be_ClothingStore.domain.Categrories;
import com.example.be_ClothingStore.domain.Image;
import com.example.be_ClothingStore.domain.Products;
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
import org.springframework.http.ResponseEntity;
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
}
