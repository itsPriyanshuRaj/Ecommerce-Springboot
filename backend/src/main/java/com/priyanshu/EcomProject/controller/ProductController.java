package com.priyanshu.EcomProject.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.priyanshu.EcomProject.model.Product;
import com.priyanshu.EcomProject.service.ProductService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
    
    @Autowired
    private ProductService service;

    @RequestMapping("/")
    public String greet(){
        return "Hello Priyanshu Raj";
    }

    @GetMapping("/products")
    public ResponseEntity< List<Product>> getAllProducts(){
        return new ResponseEntity<>(service.getAllProdcuts(),HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductbyID(@PathVariable int id){

        Product prod = service.getProductbyID(id);

        if(prod != null){
            return new ResponseEntity<>(service.getProductbyID(id), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product , @RequestPart MultipartFile imageFile){
        try{
            Product product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){

        Product product = service.getProductbyID(productId);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageFile);

    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product ,@RequestPart MultipartFile imageFile){
        
        Product product1=null;
        try {
            product1 = service.updateProduct(id,product,imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }

        if(product1 != null){
            return new ResponseEntity<>("updated", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){

        Product product = service.getProductbyID(id);

        if(product != null){
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Unable to delete", HttpStatus.NOT_FOUND);
        }
    }

}
