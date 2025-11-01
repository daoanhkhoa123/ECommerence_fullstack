@echo off
set BASE_URL=http://localhost:3100/api/v1

echo.
echo === Testing Customer Registration ===
echo.

curl -v -X POST %BASE_URL%/accounts/register/customer ^
-H "Content-Type: application/json" ^
-d "{\"accountDto\":{\"email\":\"customer@test.com\",\"passwordHash\":\"password123\",\"role\":\"CUSTOMER\"},\"fullName\":\"Test Customer\",\"phone\":\"1234567890\",\"address\":\"123 Test St\",\"birthDate\":\"2000-01-01\"}"

echo.
pause