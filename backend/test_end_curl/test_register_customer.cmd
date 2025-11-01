echo --- Register Customers ---
for %%I in (1 2 3) do (
    echo.
    echo -> Register Customer %%I
    curl -X POST %BASE_URL%/customer/register ^
         -H "Content-Type: application/json" ^
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
