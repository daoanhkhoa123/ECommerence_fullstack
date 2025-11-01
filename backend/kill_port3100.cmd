@echo off
echo Checking for process on port 3100...

for /f "tokens=5" %%a in ('netstat -ano ^| find ":3100" ^| find "LISTENING"') do (
    echo Found process ID %%a using port 3100. Killing it...
    taskkill /PID %%a /F >nul 2>&1
    echo Process %%a terminated.
)

echo Port 3100 is now free.
