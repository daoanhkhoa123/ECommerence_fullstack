package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.VendorProduct;

@Repository
public interface VendorProductRepository extends JpaRepository<VendorProduct, Integer> {
       List<VendorProduct> findByVendorId(Integer vendorId);
       void deleteByProductId(Integer productId);

       // @Query("SELECT vp.product FROM VendorProduct vp WHERE vp.vendor.id = :vendorId")
       // List<Product> findProductsByVendorId(@Param("vendorId") Integer vendorId);


}
