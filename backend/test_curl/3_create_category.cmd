@echo off
set BASE_URL=http://localhost:3100/api/v1

echo.
echo === Testing Category Creation ===
echo.

curl -v -X POST %BASE_URL%/categories ^
-H "Content-Type: application/json" ^
-d "{\"name\":\"Electronics\",\"description\":\"Electronic devices and accessories\"}"

echo.
pause