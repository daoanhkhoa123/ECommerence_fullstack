@echo off
setlocal enabledelayedexpansion
set BASE_URL=http://localhost:3100/api/v1
set AUTH=-u admin:admin123

echo ======================================
echo === STARTING BACKEND API TEST SUITE ===
echo ======================================
echo.

:: -----------------------------
:: REGISTER CUSTOMERS
:: -----------------------------
echo --- Register Customers ---
for %%I in (1 2 3) do (
    echo.
    echo -> Registering Customer %%I...
    curl -X POST %BASE_URL%/accounts/customer/register ^
         -H "Content-Type: application/json" ^
         %AUTH% ^
         -d "{
                \"accountRequest\": {
                    \"email\": \"customer%%I@example.com\",
                    \"password\": \"pass1234\"
                },
                \"fullName\": \"Customer %%I\",
                \"phone\": \"09876543%%I\",
                \"address\": \"123 Main St %%I\",
                \"birthDate\": \"1990-01-0%%I\"
              }"
    echo.
)

pause

:: -----------------------------
:: REGISTER VENDORS
:: -----------------------------
echo --- Register Vendors ---
for %%I in (1 2 3) do (
    echo.
    echo -> Registering Vendor %%I...
    curl -X POST %BASE_URL%/accounts/vendor/register ^
         -H "Content-Type: application/json" ^
         %AUTH% ^
         -d "{
                \"accountRequest\": {
                    \"email\": \"vendor%%I@example.com\",
                    \"password\": \"pass1234\"
                },
                \"shopName\": \"Vendor Shop %%I\",
                \"description\": \"Quality goods from vendor %%I\",
                \"phone\": \"09123456%%I\"
              }"
    echo.
)

pause

:: -----------------------------
:: GET CUSTOMERS & VENDORS
:: -----------------------------
echo --- Fetching Customers and Vendors ---
for /L %%C in (1,1,3) do (
    echo Getting Customer %%C...
    curl -s %BASE_URL%/accounts/customer/%%C %AUTH%
    echo.
)

for /L %%V in (1,1,3) do (
    echo Getting Vendor %%V...
    curl -s %BASE_URL%/accounts/vendor/%%V %AUTH%
    echo.
)

pause

:: -----------------------------
:: CATEGORY TESTS
:: -----------------------------
echo --- Testing Category APIs ---
for /L %%C in (1,1,3) do (
    echo Getting Category %%C...
    curl -s %BASE_URL%/categories/%%C %AUTH%
    echo.
)

for /L %%P in (1,1,3) do (
    echo Getting Product %%P Categories...
    curl -s "%BASE_URL%/categories/product/%%P?productId=%%P" %AUTH%
    echo.
)

pause

:: -----------------------------
:: ORDER TESTS
:: -----------------------------
echo --- Testing Order APIs ---
for /L %%C in (1,1,3) do (
    echo Getting Orders for Customer %%C...
    curl -s %BASE_URL%/orders/customer/%%C %AUTH%
    echo.
)

for /L %%O in (1,1,3) do (
    echo Getting Full Order %%O for Customer 1...
    curl -s %BASE_URL%/orders/customer/1/get/%%O %AUTH%
    echo.
)

for /L %%C in (1,1,3) do (
    echo Getting Cart for Customer %%C...
    curl -s %BASE_URL%/orders/customer/%%C/cart %AUTH%
    echo.
)

for /L %%I in (1,1,3) do (
    echo Getting Order Item %%I for Customer 1...
    curl -s %BASE_URL%/orders/customer/1/get-items/%%I %AUTH%
    echo.
)

pause

:: -----------------------------
:: VENDOR PRODUCT TESTS
:: -----------------------------
echo --- Testing Vendor Product APIs ---
for /L %%V in (1,1,3) do (
    echo Getting Products for Vendor %%V...
    curl -s %BASE_URL%/vendor-product/%%V %AUTH%
    echo.
)

echo.
echo ======================================
echo ===        ALL TESTS DONE          ===
echo ======================================
pause
