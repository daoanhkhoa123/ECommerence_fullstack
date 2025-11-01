@echo off
set BASE_URL=http://localhost:3100/api/v1
set AUTH_HEADER=-u admin_backend:admin123

echo.
echo === Testing Customer Registration (No auth needed) ===
echo.

:: Test customer registration (public endpoint)
curl -v -X POST %BASE_URL%/accounts/register/customer ^
-H "Content-Type: application/json" ^
-d "{\"accountDto\":{\"email\":\"customer@test.com\",\"passwordHash\":\"password123\",\"role\":\"CUSTOMER\"},\"fullName\":\"Test Customer\",\"phone\":\"1234567890\",\"address\":\"123 Test St\",\"birthDate\":\"2000-01-01\"}"

echo.
echo === Testing Protected Endpoint (With auth) ===
echo.

:: Test getting orders (protected endpoint)
curl -v -X GET %BASE_URL%/orders/customer/1 %AUTH_HEADER%

echo.
pause