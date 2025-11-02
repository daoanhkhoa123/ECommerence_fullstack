@echo off
setlocal

:: Base URL and credentials
set BASE_URL=http://localhost:3100/api/v1/accounts
set AUTH_USER=admin_backend
set AUTH_PASS=admin123

echo ======================================
echo === STARTING BACKEND API TEST SUITE ===
echo ======================================
echo.

:: -----------------------------
:: GET CUSTOMER BY ID
:: -----------------------------
echo --- Get Customer by ID ---
curl -X GET "%BASE_URL%/customer/2" -u %AUTH_USER%:%AUTH_PASS% -H "Accept: application/json"

echo.
echo ======================================
echo === TEST SUITE COMPLETE ===
echo ======================================

endlocal
pause
