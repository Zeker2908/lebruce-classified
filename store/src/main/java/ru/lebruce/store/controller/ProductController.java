package ru.lebruce.store.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.lebruce.store.domain.dto.CharacteristicRequest;
import ru.lebruce.store.domain.dto.ProductRequest;
import ru.lebruce.store.domain.dto.ProductSizeRequest;
import ru.lebruce.store.domain.dto.ReviewRequest;
import ru.lebruce.store.domain.model.*;
import ru.lebruce.store.service.*;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService service;
    private final CharacteristicService characteristicService;
    private final ProductSizeService productSizeService;
    private final ReviewService reviewService;
    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<?> findAllProducts(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size,
                                             @RequestParam(defaultValue = "productId,asc") String[] sort) {
        Page<Product> productPage = service.findAllProducts(page, size, sort);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/category")
    public ResponseEntity<?> findAllProductsByCategory(@RequestParam String categoryName,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "20") int size,
                                                       @RequestParam(defaultValue = "productId,asc") String[] sort) {
        Page<Product> productPage = service.findAllProductsByCategory(categoryName, page, size, sort);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByProductId(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> search(@RequestParam String query,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int size,
                                                @RequestParam(defaultValue = "reviewCount,desc") String[] sort) {
        Page<Product> productPage = service.searchProducts(query, page, size, sort);
        return ResponseEntity.ok(productPage);
    }


    @PostMapping
    public ResponseEntity<?> createProduct(@Valid ProductRequest productRequest, @RequestPart MultipartFile[] images) {
        return ResponseEntity.ok(service.createProduct(productRequest, images));
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody @Valid Product product) {
        return ResponseEntity.ok(service.updateProduct(product));
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        service.deleteProduct(productId);
        return ResponseEntity.ok("Товар с ID " + productId + " успешно удален");
    }

    @DeleteMapping("/name/{productName}")
    public ResponseEntity<?> deleteProductByName(@PathVariable String productName) {
        service.deleteProduct(productName);
        return ResponseEntity.ok("Товар с названием " + productName + " успешно удален");
    }

    //Методы, которые относятся к брендам товаров
    @GetMapping("/brand")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "name,asc") String[] sort) {
        return ResponseEntity.ok(brandService.findAll(page, size, sort));
    }

    @GetMapping("/brand/search")
    public ResponseEntity<Page<Brand>> searchBrand(@RequestParam String query,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "5") int size,
                                                   @RequestParam(defaultValue = "name,asc") String[] sort) {
        Page<Brand> productPage = brandService.search(query, page, size, sort);
        return ResponseEntity.ok(productPage);
    }

    @PostMapping("/brand")
    public ResponseEntity<?> create(@RequestBody Brand brand) {
        return ResponseEntity.ok(brandService.saveBrand(brand));
    }

    @PutMapping("/brand")
    public ResponseEntity<?> update(@RequestBody Brand brand) {
        return ResponseEntity.ok(brandService.updateBrand(brand));
    }

    @DeleteMapping("/brand/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        brandService.deleteById(id);
        return ResponseEntity.ok("Бренд успешно удален");
    }

    //Методы, которые относятся к характеристикам товаров

    @PostMapping("/characteristic")
    public Characteristic create(@RequestBody @Valid CharacteristicRequest characteristic) {
        return characteristicService.create(characteristic);
    }

    @PutMapping("/characteristic")
    public Characteristic updateCharacteristic(@RequestBody Characteristic characteristic) {
        return characteristicService.update(characteristic);
    }

    @DeleteMapping("/characteristic/{characteristicId}")
    public void deleteCharacteristic(@PathVariable Long characteristicId) {
        characteristicService.deleteCharacteristic(characteristicId);
    }

    //Методы, которые относятся к размерам товаров

    @PostMapping("/size")
    public ResponseEntity<?> createProductSize(@RequestBody @Valid ProductSizeRequest productSizeRequest) {
        productSizeService.createProductSize(productSizeRequest);
        return ResponseEntity.ok("Размер успешно создан");
    }

    @PutMapping("/size")
    public ResponseEntity<?> updateProductSize(@RequestBody ProductSize productSize) {
        productSizeService.updateProductSize(productSize);
        return ResponseEntity.ok("Размер обновлен");
    }

    @DeleteMapping("/size/{id}")
    public ResponseEntity<?> deleteProductSize(@PathVariable Long id) {
        productSizeService.deleteProductSize(id);
        return ResponseEntity.ok("Размер успешно удален");
    }

    //Методы, которые относятся к отзывам товаров

    @GetMapping("/review")
    public ResponseEntity<Page<Review>> getReviews(@RequestParam Long productId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "reviewId,asc") String[] sort) {
        return ResponseEntity.ok(reviewService.findAllByProductId(productId, page, size, sort));
    }

    @GetMapping("/review/{productId}")
    public ResponseEntity<Review> getReview(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.findByProductIdAndCurrentUser(productId));
    }

    @PostMapping("/review")
    public ResponseEntity<?> createReview(@RequestBody @Valid ReviewRequest review) {
        reviewService.createReview(review);
        return ResponseEntity.ok("Отзыв успешно оставлен");
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Отзыв успешно удален");
    }

}
