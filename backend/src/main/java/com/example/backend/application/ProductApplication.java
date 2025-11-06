package com.example.backend.application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.backend.dto.ProductVendorRequest;
import com.example.backend.dto.VendorProductRequest;
import com.example.backend.dto.VendorProductRespond;
import com.example.backend.entity.Product;
import com.example.backend.entity.VendorProduct;
import com.example.backend.kafka.producer.ProductProducer;
import com.example.backend.security.JwtService;
import com.example.backend.service.ProductService;
import com.example.backend.service.VendorProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductApplication {

    private static final Logger log = LoggerFactory.getLogger(ProductApplication.class);

    private final JwtService jwtService;
    private final ProductProducer productProducer;
    private final ProductService productService;
    private final VendorProductService vendorProductService;

    private VendorProductRespond buildFromVendorProduct(VendorProduct vp) {
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

    // ---------- FIND ----------
    public List<VendorProductRespond> findByVendorId(Integer vendorId) {
        log.info("[Product] Fetching vendor products for vendorId={}", vendorId);

        List<VendorProduct> vendorProducts = vendorProductService.findByVendorId(vendorId);
        log.info("[Product] Successfully fetched {} vendor products from service layer for vendorId={}",
                 vendorProducts.size(), vendorId);

        Integer actorId = jwtService.getCurrentUserId();
        log.info("[Product] Sending Kafka event: vendorProduct.read by actor={}", actorId);
        productProducer.sendVendorProductRead(actorId, vendorId);
        log.info("[Product] Kafka event sent successfully for vendorProduct.read vendorId={}", vendorId);

        return vendorProducts.stream().map(this::buildFromVendorProduct).toList();
    }

    // ---------- CREATE ----------
    public VendorProductRespond createVendorProduct(Integer vendorId, VendorProductRequest request) {
        log.info("[Product] Creating vendor product for vendorId={} with name={}", vendorId, request.name());

        Product product = productService.createProduct(request);
        log.info("[Product] Product created successfully in service layer: id={}, name={}",
                 product.getId(), product.getName());

        VendorProduct vendorProduct = vendorProductService.createVendorProduct(vendorId, product, request);
        log.info("[Product] Vendor product created successfully in service layer: id={}, vendorId={}, price={}",
                 vendorProduct.getId(), vendorId, vendorProduct.getPrice());

        Integer actorId = jwtService.getCurrentUserId();
        log.info("[Product] Sending Kafka event: vendorProduct.create by actor={}", actorId);
        productProducer.sendVendorProductCreate(actorId, vendorProduct);
        log.info("[Product] Kafka event sent successfully for vendorProduct.create id={}", vendorProduct.getId());

        return buildFromVendorProduct(vendorProduct);
    }

    // ---------- UPDATE ----------
    public VendorProductRespond updateVendorProduct(Integer vendorProductId, ProductVendorRequest request) {
        log.info("[Product] Updating vendor product id={} with new price={}, stock={}",
                 vendorProductId, request.price(), request.stock());

        Product product = productService.findProductByVendorProductId(vendorProductId);
        log.info("[Product] Fetched associated product from service layer: id={}, name={}",
                 product.getId(), product.getName());

        VendorProduct vendorProduct = vendorProductService.updateVendorProduct(vendorProductId, product, request);
        log.info("[Product] Vendor product updated successfully in service layer: id={}, new price={}, new stock={}",
                 vendorProduct.getId(), vendorProduct.getPrice(), vendorProduct.getStock());

        Integer actorId = jwtService.getCurrentUserId();
        log.info("[Product] Sending Kafka event: vendorProduct.update by actor={}", actorId);
        productProducer.sendVendorProductUpdate(actorId, vendorProduct);
        log.info("[Product] Kafka event sent successfully for vendorProduct.update id={}", vendorProductId);

        return buildFromVendorProduct(vendorProduct);
    }

    // ---------- DELETE ----------
    public void deleteVendorProduct(Integer vendorProductId) {
        log.warn("[Product] Deleting vendor product id={}", vendorProductId);

        vendorProductService.deleteVendorProduct(vendorProductId);
        log.info("[Product] Vendor product deleted successfully in service layer: id={}", vendorProductId);

        Integer actorId = jwtService.getCurrentUserId();
        log.info("[Product] Sending Kafka event: vendorProduct.delete by actor={}", actorId);
        productProducer.sendVendorProductDelete(actorId, vendorProductId);
        log.info("[Product] Kafka event sent successfully for vendorProduct.delete id={}", vendorProductId);
    }
}
