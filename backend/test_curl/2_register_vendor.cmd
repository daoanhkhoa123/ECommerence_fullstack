@echo off
set BASE_URL=http://localhost:3100/api/v1

echo.
echo === Testing Vendor Registration ===
echo.

curl -v -X POST %BASE_URL%/accounts/register/vendor ^
-H "Content-Type: application/json" ^
-d "{\"accountDto\":{\"email\":\"vendor@test.com\",\"passwordHash\":\"password123\",\"role\":\"VENDOR\"},\"shopName\":\"Test Shop\",\"description\":\"Test Shop Description\",\"phone\":\"0987654321\"}"

echo.
pause