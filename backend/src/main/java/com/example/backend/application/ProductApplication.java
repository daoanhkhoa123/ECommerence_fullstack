package com.example.backend.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.config.JwtService;
import com.example.backend.dto.ProductVendorRequest;
import com.example.backend.dto.VendorProductRequest;
import com.example.backend.dto.VendorProductRespond;
import com.example.backend.entity.Product;
import com.example.backend.entity.VendorProduct;
import com.example.backend.kafka.producer.ProductProducer;
import com.example.backend.service.ProductService;
import com.example.backend.service.VendorProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductApplication {
    private final JwtService jwtService;
    private final ProductProducer productProducer;
    private final ProductService productService;
    private final VendorProductService vendorProductService;

    private VendorProductRespond buildFromVendorProduct(VendorProduct vp)
    {
        return new VendorProductRespond(
            vp.getId(),
            vp.getProduct().getId(),
            vp.getVendor().getId(),
            vp.getProduct().getName(),
            vp.getProduct().getDescription(),
            vp.getProduct().getBrand(),
            vp.getProduct().getImageUrl(),
            vp.getPrice(),
            vp.getStock(),
            vp.getSku(),
            vp.getIsFeatured()
        );
    }

    public List<VendorProductRespond> findByVendorId(Integer vendorId)
    {
        List<VendorProduct> vendorProducts = vendorProductService.findByVendorId(vendorId);

        productProducer.sendVendorProductRead(jwtService.getCurrentUserId(), vendorId);

        return vendorProducts.stream().map(this::buildFromVendorProduct).toList();
    }

    public VendorProductRespond createVendorProduct(Integer vendorId, 
        VendorProductRequest request)
    {
        Product product = productService.createProduct(request);
        VendorProduct vendorProduct = vendorProductService.createVendorProduct(vendorId, product, request);

        productProducer.sendVendorProudctCreate(jwtService.getCurrentUserId(), vendorProduct);

        return buildFromVendorProduct(vendorProduct);
    }

    public VendorProductRespond updateVendorProduct(Integer vendorProductId, 
        ProductVendorRequest request)
    {
        // No update on categories
        Product product = productService.findProductByVendorProductId(vendorProductId);
        VendorProduct vendorProduct = vendorProductService.updateVendorProduct(vendorProductId, product, request);

        productProducer.sendVendorProudctUpdate(jwtService.getCurrentUserId(), vendorProduct);

        return buildFromVendorProduct(vendorProduct);
    }

    public void deleteVendorProduct(Integer vendorProductId)
    {
       vendorProductService.deleteVendorProduct(vendorProductId);
       productProducer.sendVendorProductDelete(jwtService.getCurrentUserId(), vendorProductId);
    }
}
