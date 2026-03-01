@echo off
REM Quick start script for local development

echo.
echo ============================================
echo  Restaurant Platform - Local Development
echo ============================================
echo.

REM Check if Docker is installed
docker --version >nul 2>&1
if errorlevel 1 (
    echo Warning: Docker not found. Skipping database container.
    echo Install Docker from https://www.docker.com/products/docker-desktop
) else (
    echo Starting PostgreSQL database...
    docker run -d --name restaurant_db ^
        -e POSTGRES_USER=postgres ^
        -e POSTGRES_PASSWORD=password ^
        -e POSTGRES_DB=restaurant_db ^
        -p 5432:5432 ^
        postgres:15
    echo PostgreSQL started on port 5432
)

echo.
echo Starting Backend...
cd backend
call mvn clean package -DskipTests
if errorlevel 1 (
    echo Backend build failed!
    pause
    exit /b 1
)
echo Backend built successfully at target\restaurant-platform-*.jar
cd ..

echo.
echo Starting Frontend...
cd frontend
echo Installing dependencies...
call npm install
if errorlevel 1 (
    echo Frontend npm install failed!
    pause
    exit /b 1
)
echo Starting development server...
call npm start

pause

