@echo off
set BASE_URL=http://localhost:3100/api/v1

echo.
echo === Testing Order Creation ===
echo.

:: Replace with actual customer ID after registration
set CUSTOMER_ID=1

curl -v -X POST %BASE_URL%/orders/customer/%CUSTOMER_ID% ^
-H "Content-Type: application/json" ^
-d "{\"vendorProductId\":1,\"quantity\":2}"

echo.
pause