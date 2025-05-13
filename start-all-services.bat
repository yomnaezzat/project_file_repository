@echo off
setlocal enabledelayedexpansion

echo *******************************************
echo * Microservices Startup Script - Improved *
echo *******************************************

echo.
echo Starting microservices in the correct order...
echo.

rem Create a directory for logs if it doesn't exist
if not exist logs mkdir logs

echo Step 1: Starting Eureka Server (Discovery Service)
echo ----------------------------------------------
echo [%time%] Starting Eureka Server... > logs\eureka-server.log
start "Eureka Server" cmd /c "cd /d "%~dp0eureka-server" && mvn spring-boot:run >> logs\eureka-server.log 2>&1"

echo Waiting for Eureka Server to initialize (30 seconds)...
ping -n 30 127.0.0.1 > nul
echo.

echo Step 2: Starting User Service
echo ----------------------------------------------
echo [%time%] Starting User Service... > logs\user-service.log
start "User Service" cmd /c "cd /d "%~dp0user-service" && mvn spring-boot:run >> logs\user-service.log 2>&1"

echo Waiting for User Service to register (20 seconds)...
ping -n 20 127.0.0.1 > nul
echo.

echo Step 3: Starting File Service
echo ----------------------------------------------
echo [%time%] Starting File Service... > logs\file-service.log
start "File Service" cmd /c "cd /d "%~dp0file-service" && mvn spring-boot:run >> logs\file-service.log 2>&1"

echo Waiting for File Service to register (20 seconds)...
ping -n 20 127.0.0.1 > nul
echo.

echo Step 4: Starting Repository Service
echo ----------------------------------------------
echo [%time%] Starting Repository Service... > logs\repository-service.log
start "Repository Service" cmd /c "cd /d "%~dp0repository-service" && mvn spring-boot:run >> logs\repository-service.log 2>&1"

echo Waiting for Repository Service to register (20 seconds)...
ping -n 20 127.0.0.1 > nul
echo.

echo Step 5: Starting API Gateway
echo ----------------------------------------------
echo [%time%] Starting API Gateway... > logs\api-gateway.log
start "API Gateway" cmd /c "cd /d "%~dp0api-gateway" && mvn spring-boot:run >> logs\api-gateway.log 2>&1"

echo.
echo *******************************************
echo * All services have been started!         *
echo *                                         *
echo * Eureka Dashboard: http://localhost:8761 *
echo * API Gateway:      http://localhost:8080 *
echo *                                         *
echo * Service logs are stored in the logs     *
echo * directory for troubleshooting.          *
echo *******************************************
echo.
echo Press ENTER to shut down all services, or close this window to keep them running.
pause > nul

echo Shutting down all services...
taskkill /f /fi "WINDOWTITLE eq Eureka Server*"
taskkill /f /fi "WINDOWTITLE eq User Service*"
taskkill /f /fi "WINDOWTITLE eq File Service*"
taskkill /f /fi "WINDOWTITLE eq Repository Service*"
taskkill /f /fi "WINDOWTITLE eq API Gateway*"

echo All services have been stopped.
