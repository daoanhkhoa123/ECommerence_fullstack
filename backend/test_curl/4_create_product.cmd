@echo off
set BASE_URL=http://localhost:3100/api/v1

echo.
echo === Testing Product Creation (For Vendor) ===
echo.

:: Replace 1 with actual vendor ID after registration
set VENDOR_ID=1

curl -v -X POST %BASE_URL%/products/vendor/%VENDOR_ID% ^
-H "Content-Type: application/json" ^
-d "{\"name\":\"Smartphone\",\"description\":\"Latest model smartphone\",\"brand\":\"TechBrand\",\"imageUrl\":\"https://example.com/phone.jpg\",\"categoryRequest\":{\"addCategoryIds\":[1]},\"price\":999.99,\"stock\":100,\"sku\":\"PHONE001\",\"isFeatured\":true}"

echo.
pause