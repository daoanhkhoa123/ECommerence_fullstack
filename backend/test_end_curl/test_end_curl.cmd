@echo off
setlocal enabledelayedexpansion

rem -------------------------------
rem accounts (customer + vendor)
rem -------------------------------
set ACC_URL=http://localhost:3100/api/v1/accounts

echo testing account GET apis...
echo.

for /L %%C in (1,1,3) do (
    echo getting customer %%C...
    curl -s %ACC_URL%/customer/%%C
    echo.
)

for /L %%V in (1,1,3) do (
    echo getting vendor %%V...
    curl -s %ACC_URL%/vendor/%%V
    echo.
)

echo done.
pause

rem -------------------------------
rem categories
rem -------------------------------
set CAT_URL=http://localhost:3100/api/v1/categories

echo testing category GET apis...
echo.

for /L %%C in (1,1,3) do (
    echo getting category %%C...
    curl -s %CAT_URL%/%%C
    echo.
)

for /L %%P in (1,1,3) do (
    echo getting product %%P categories...
    curl -s "%CAT_URL%/product/%%P?productId=%%P"
    echo.
)

echo done.
pause

rem -------------------------------
rem orders
rem -------------------------------
set ORD_URL=http://localhost:3100/api/v1/orders

echo testing order GET apis...
echo.

for /L %%C in (1,1,3) do (
    echo getting orders for customer %%C...
    curl -s %ORD_URL%/customer/%%C
    echo.
)

for /L %%O in (1,1,3) do (
    echo getting full order %%O for customer 1...
    curl -s %ORD_URL%/customer/1/get/%%O
    echo.
)

for /L %%C in (1,1,3) do (
    echo getting cart for customer %%C...
    curl -s %ORD_URL%/customer/%%C/cart
    echo.
)

for /L %%I in (1,1,3) do (
    echo getting order item %%I for customer 1...
    curl -s %ORD_URL%/customer/1/get-items/%%I
    echo.
)

echo done.
pause

rem -------------------------------
rem vendor products
rem -------------------------------
set VEN_URL=http://localhost:3100/api/v1

echo testing vendor product GET api...
echo.

for /L %%V in (1,1,3) do (
    echo getting products for vendor %%V...
    curl -s %VEN_URL%/vendor-product/%%V
    echo.
)

echo done.
pause
