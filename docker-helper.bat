@echo off
REM ════════════════════════════════════════════════════════════════════════════════════
REM Docker Helper Script for Windows (SmartLedger)
REM ════════════════════════════════════════════════════════════════════════════════════
REM Windows batch script equivalent of docker-helper.sh

setlocal enabledelayedexpansion

echo 🐳 SmartLedger Docker Helper (Windows)
echo ════════════════════════════════════════

if "%1"=="" goto :show_menu
if "%1"=="help" goto :show_menu
if "%1"=="build" goto :build_app
if "%1"=="start" goto :start_services
if "%1"=="dev" goto :start_dev
if "%1"=="stop" goto :stop_services
if "%1"=="logs" goto :view_logs
if "%1"=="status" goto :status
if "%1"=="info" goto :info
if "%1"=="cleanup" goto :cleanup

echo [ERROR] Unknown command: %1
goto :show_menu

:build_app
echo [INFO] Checking if Docker is running...
docker info >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker is not running. Please start Docker Desktop.
    exit /b 1
)
echo [INFO] Building SmartLedger application...
docker-compose build --no-cache
if errorlevel 1 (
    echo [ERROR] Build failed
    exit /b 1
)
echo [INFO] Build completed successfully ✓
goto :end

:start_services
echo [INFO] Starting all services...
docker-compose up -d
echo [INFO] Services started. Checking status...
timeout /t 5 >nul
docker-compose ps
goto :end

:start_dev
echo [INFO] Starting development services (MySQL + Redis only)...
docker-compose -f docker-compose.dev.yml up -d
echo [INFO] Development services started ✓
echo [WARN] Run your Spring Boot app locally to connect to these services
goto :end

:stop_services
echo [INFO] Stopping all services...
docker-compose down
echo [INFO] Services stopped ✓
goto :end

:view_logs
if "%2"=="" (
    echo [INFO] Showing logs for all services...
    docker-compose logs -f
) else (
    echo [INFO] Showing logs for %2...
    docker-compose logs -f %2
)
goto :end

:status
echo [INFO] Application Status:
echo ─────────────────────
docker-compose ps
echo.
echo [INFO] Container Health:
echo ─────────────────
for %%c in (smartledger-mysql smartledger-redis smartledger-app) do (
    docker ps --format "table {{.Names}}" | findstr /C:"%%c" >nul
    if !errorlevel! equ 0 (
        echo   %%c: running
    ) else (
        echo   %%c: not running
    )
)
goto :end

:info
echo [INFO] SmartLedger Application URLs:
echo ─────────────────────────────
echo   🌐 Application: http://localhost:5000
echo   📊 Health Check: http://localhost:5000/actuator/health
echo   🔧 Nginx: http://localhost (if enabled)
echo.
echo [INFO] Database Connections:
echo ─────────────────────
echo   🗄️  MySQL: localhost:3307
echo      Database: khatabook
echo      Username: smartledger
echo      Password: smartledger123
echo.
echo   🔴 Redis: localhost:6380
echo.
echo [INFO] Useful Commands:
echo ────────────────
echo   View app logs: docker-compose logs -f app
echo   Enter MySQL: docker exec -it smartledger-mysql mysql -u smartledger -p khatabook
echo   Enter Redis: docker exec -it smartledger-redis redis-cli
echo   Rebuild app: docker-compose build app ^&^& docker-compose up -d app
goto :end

:cleanup
echo [WARN] This will remove all containers, images, and volumes. Continue? (y/N)
set /p response=
if /i "!response!"=="y" (
    echo [INFO] Cleaning up...
    docker-compose down -v --remove-orphans
    docker system prune -f
    echo [INFO] Cleanup completed ✓
) else (
    echo [INFO] Cleanup cancelled
)
goto :end

:show_menu
echo.
echo Available commands:
echo   docker-helper.bat build     - Build the application
echo   docker-helper.bat start     - Start all services
echo   docker-helper.bat dev       - Start development services only
echo   docker-helper.bat stop      - Stop all services
echo   docker-helper.bat logs      - View logs
echo   docker-helper.bat status    - Show application status
echo   docker-helper.bat info      - Show URLs and connection info
echo   docker-helper.bat cleanup   - Clean up all containers and volumes
echo   docker-helper.bat help      - Show this menu
echo.

:end
