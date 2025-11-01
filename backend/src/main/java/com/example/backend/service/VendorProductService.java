package com.example.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import com.example.backend.dto.ProductVendorRequest;
import com.example.backend.dto.VendorProductRequest;
import com.example.backend.entity.Product;
import com.example.backend.entity.Vendor;
import com.example.backend.entity.VendorProduct;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.VendorProductRepository;
import com.example.backend.repository.VendorRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VendorProductService {

    private final ProductRepository productRepository;
    private final VendorProductRepository vendorProductRepository;
    private final VendorRepository vendorRepository;

    public List<VendorProduct> findByVendorId(Integer vendorId) {
        return vendorProductRepository.findByVendorId(vendorId);
    }

    public VendorProduct createVendorProduct(
            Integer vendorId,
            Product product, VendorProductRequest request)
    {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vendor not found with id " + vendorId));

        VendorProduct vp = new VendorProduct();
        vp.setVendor(vendor);
        vp.setProduct(product);
        vp.setPrice(request.price());
        vp.setStock(request.stock());
        vp.setSku(request.sku());
        vp.setIsFeatured(request.isFeatured());

        return vendorProductRepository.save(vp);
    }

    @Transactional
    public VendorProduct updateVendorProduct(
            Integer vendorProductId, Product product,
            ProductVendorRequest request) 
    {
        VendorProduct vendorProduct = vendorProductRepository.findById(vendorProductId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vendor product not found"));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setBrand(request.brand());
        product.setImageUrl(request.imageUrl());
        productRepository.save(product);

        vendorProduct.setPrice(request.price());
        vendorProduct.setStock(request.stock());
        vendorProduct.setSku(request.sku());
        vendorProduct.setIsFeatured(request.isFeatured());

        return vendorProductRepository.save(vendorProduct);
    }

    @Transactional
    public void deleteVendorProduct(Integer vendorProductId) 
    {
        if (!vendorProductRepository.existsById(vendorProductId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor product not found");
        }
        
        // Only delete the vendor product, preserve the base product
        vendorProductRepository.deleteById(vendorProductId);
    }
}
