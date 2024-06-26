package ru.lebruce.store.service;

import ru.lebruce.store.domain.dto.ProductSizeRequest;
import ru.lebruce.store.domain.model.ProductSize;

public interface ProductSizeService {
    void createProductSize(ProductSizeRequest productSize);

    ProductSize getProductSizeById(Long id);

    void updateProductSize(ProductSize productSize);

    void updateQuantity(Long ProductSizeId, int quantityChange);

    void deleteProductSize(Long id);

}
