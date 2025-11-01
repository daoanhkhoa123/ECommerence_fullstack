@echo off
REM ======================================================
REM  Test AimoduleOrderController endpoints on port 3100
REM ======================================================

set BASE_URL=http://localhost:8080/api/aimodule

echo.
echo ===== Testing Aimodule API =====
echo.

REM ---- Loop through customerId 1 to 3 ----
for /L %%C in (1,1,3) do (
    echo.
    echo === CUSTOMER %%C ===
    echo.

    echo -> Get order times
    curl -s %BASE_URL%/customers/%%C/orders/times
    echo.

    echo -> Get orders
    curl -s %BASE_URL%/customers/%%C/orders
    echo.
)

REM ---- Loop through orderId 1 to 3 ----
for /L %%O in (1,1,3) do (
    echo.
    echo === ORDER %%O ===
    echo.

    echo -> Get order items
    curl -s %BASE_URL%/orders/%%O/items
    echo.

    echo -> Get full order items
    curl -s %BASE_URL%/orders/%%O/items/full
    echo.
)

echo.
echo ===== All API tests completed =====
pause
