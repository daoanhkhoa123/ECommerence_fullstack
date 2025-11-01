package com.example.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.function.Function;

import com.example.backend.dto.CategoryRequestRespond;
import com.example.backend.dto.ProductVendorRequest;
import com.example.backend.dto.VendorProductRequest;
import com.example.backend.dto.VendorProductRespond;
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


    private VendorProductRespond buildByVendorProduct(VendorProduct vp,
        Function<Integer, List<CategoryRequestRespond>> mapper)
    {
        return new VendorProductRespond(
            vp.getId(),
            vp.getProduct().getId(),
            vp.getVendor().getId(),
            vp.getProduct().getName(),
            vp.getProduct().getDescription(),
            vp.getProduct().getBrand(),
            vp.getProduct().getImageUrl(),
            mapper.apply(vp.getProduct().getId()),
            vp.getPrice(),
            vp.getStock(),
            vp.getSku(),
            vp.getIsFeatured()
        );
    }

    public List<VendorProductRespond> findByVendorId(Integer vendorId,
    Function<Integer, List<CategoryRequestRespond>> mapper) {
    List<VendorProduct> vps = vendorProductRepository.findByVendorId(vendorId);

    return vps.stream().map(vp -> buildByVendorProduct(vp, mapper)).toList();
    }


    public VendorProductRespond createVendorProduct(Integer vendorId, Product product, 
    VendorProductRequest request, Function<Integer, List<CategoryRequestRespond>> mapper) {
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
        vendorProductRepository.save(vp);
        
        return buildByVendorProduct(vp, mapper);
    }

    @Transactional
    public VendorProductRespond updateVendorProduct(Integer vendorProductId, Product product, ProductVendorRequest request,
    Function<Integer, List<CategoryRequestRespond>> mapper) {
        VendorProduct vendorProduct = vendorProductRepository.findById(vendorProductId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor product not found"));


        product.setName(request.name());
        product.setDescription(request.description());
        product.setBrand(request.brand());
        product.setImageUrl(request.imageUrl());
        productRepository.save(product);

        vendorProduct.setPrice(request.price());
        vendorProduct.setStock(request.stock());
        vendorProduct.setSku(request.sku());
        vendorProduct.setIsFeatured(request.isFeatured());
        vendorProductRepository.save(vendorProduct);

        return buildByVendorProduct(vendorProduct, mapper);
    }

    @Transactional
    public void deleteVendorProduct(Integer vendorProductId) {
        if (!vendorProductRepository.existsById(vendorProductId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor product not found");
        }
        // Only delete the vendor product, preserve the base product
        vendorProductRepository.deleteById(vendorProductId);
    }
}
