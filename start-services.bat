@echo off
setlocal enabledelayedexpansion

echo Starting microservices in the correct order...

echo.
echo Step 1: Starting Eureka Server
start "Eureka Server" cmd /c "cd /d %~dp0eureka-server && mvn spring-boot:run"

echo Waiting for Eureka Server to start...
ping -n 30 127.0.0.1 > nul

echo.
echo Step 2: Starting User Service
start "User Service" cmd /c "cd /d %~dp0user-service && mvn spring-boot:run"

echo Waiting for User Service to register...
ping -n 20 127.0.0.1 > nul

echo.
echo Step 3: Starting File Service
start "File Service" cmd /c "cd /d %~dp0file-service && mvn spring-boot:run"

echo Waiting for File Service to register...
ping -n 20 127.0.0.1 > nul

echo.
echo Step 4: Starting Repository Service
start "Repository Service" cmd /c "cd /d %~dp0repository-service && mvn spring-boot:run"

echo Waiting for Repository Service to register...
ping -n 20 127.0.0.1 > nul

echo.
echo Step 5: Starting API Gateway
start "API Gateway" cmd /c "cd /d %~dp0api-gateway && mvn spring-boot:run"

echo.
echo All services should now be starting. Please check the individual terminal windows for any errors.
echo.
echo Eureka Dashboard: http://localhost:8761
echo API Gateway: http://localhost:8080
echo.
echo Press any key to terminate all services...
pause > nul

echo Shutting down all services...
taskkill /f /fi "WINDOWTITLE eq Eureka Server*"
taskkill /f /fi "WINDOWTITLE eq User Service*"
taskkill /f /fi "WINDOWTITLE eq File Service*"
taskkill /f /fi "WINDOWTITLE eq Repository Service*"
taskkill /f /fi "WINDOWTITLE eq API Gateway*"

echo All services have been stopped.
