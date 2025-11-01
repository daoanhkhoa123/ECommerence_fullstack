@echo off
set BASE_URL=http://localhost:3100/api/v1

echo.
echo === Running All Tests ===
echo.

echo === 1. Register Customer ===
call 1_register_customer.cmd

echo === 2. Register Vendor ===
call 2_register_vendor.cmd

echo === 3. Create Category ===
call 3_create_category.cmd

echo === 4. Create Product ===
call 4_create_product.cmd

echo === 5. Create Order ===
call 5_create_order.cmd

echo.
echo === All Tests Completed ===
pause