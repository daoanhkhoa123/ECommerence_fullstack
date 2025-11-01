@echo off
setlocal enabledelayedexpansion

rem ============================================================
rem GLOBAL CONFIG
rem ============================================================
set BASE_URL=http://localhost:3100/api/v1
set AUTH=-u admin_backend:admin123

rem ============================================================
rem ACCOUNTS (Customer + Vendor)
rem ============================================================
set ACC_URL=%BASE_URL%/accounts

echo === Testing account GET APIs ===
echo.

for /L %%C in (1,1,3) do (
    echo Getting customer %%C...
    curl -s %ACC_URL%/customer/%%C %AUTH%
    echo.
)

for /L %%V in (1,1,3) do (
    echo Getting vendor %%V...
    curl -s %ACC_URL%/vendor/%%V %AUTH%
    echo.
)

echo done.
pause

rem ============================================================
rem CATEGORIES
rem ============================================================
set CAT_URL=%BASE_URL%/categories

echo === Testing category GET APIs ===
echo.

for /L %%C in (1,1,3) do (
    echo Getting category %%C...
    curl -s %CAT_URL%/%%C %AUTH%
    echo.
)

for /L %%P in (1,1,3) do (
    echo Getting product %%P categories...
    curl -s "%CAT_URL%/product/%%P?productId=%%P" %AUTH%
    echo.
)

echo done.
pause

rem ============================================================
rem ORDERS
rem ============================================================
set ORD_URL=%BASE_URL%/orders

echo === Testing order GET APIs ===
echo.

for /L %%C in (1,1,3) do (
    echo Getting orders for customer %%C...
    curl -s %ORD_URL%/customer/%%C %AUTH%
    echo.
)

for /L %%O in (1,1,3) do (
    echo Getting full order %%O for customer 1...
    curl -s %ORD_URL%/customer/1/get/%%O %AUTH%
    echo.
)

for /L %%C in (1,1,3) do (
    echo Getting cart for customer %%C...
    curl -s %ORD_URL%/customer/%%C/cart %AUTH%
    echo.
)

for /L %%I in (1,1,3) do (
    echo Getting order item %%I for customer 1...
    curl -s %ORD_URL%/customer/1/get-items/%%I %AUTH%
    echo.
)

echo done.
pause

rem ============================================================
rem VENDOR PRODUCTS
rem ============================================================
set VEN_URL=%BASE_URL%/products/vendor-product

echo === Testing vendor product GET API ===
echo.

for /L %%V in (1,1,3) do (
    echo Getting products for vendor %%V...
    curl -s %VEN_URL%/%%V %AUTH%
    echo.
)

echo done.
pause
