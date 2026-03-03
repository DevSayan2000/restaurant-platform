@echo off
REM Quick start script for local development (PostgreSQL installed locally)

echo.
echo ============================================
echo  Restaurant Platform - Local Development
echo ============================================
echo.

REM Default local PostgreSQL settings
set LOCAL_DB_HOST=localhost
set LOCAL_DB_PORT=5432
set LOCAL_DB_NAME=restaurant_db
set LOCAL_DB_USER=postgres
set LOCAL_DB_PASS=password

echo [1/5] Checking PostgreSQL connection...
psql -U %LOCAL_DB_USER% -h %LOCAL_DB_HOST% -p %LOCAL_DB_PORT% -c "SELECT 1" >nul 2>&1
if errorlevel 1 (
    echo.
    echo ERROR: Cannot connect to PostgreSQL on %LOCAL_DB_HOST%:%LOCAL_DB_PORT%
    echo Make sure PostgreSQL is running and password is correct.
    echo If your password is different, run this first:
    echo   set SPRING_DATASOURCE_PASSWORD=your_password
    echo Then re-run this script.
    pause
    exit /b 1
)
echo PostgreSQL is running.

echo.
echo [2/5] Checking database '%LOCAL_DB_NAME%'...
psql -U %LOCAL_DB_USER% -h %LOCAL_DB_HOST% -p %LOCAL_DB_PORT% -tc "SELECT 1 FROM pg_database WHERE datname='%LOCAL_DB_NAME%'" 2>nul | findstr /C:"1" >nul
if errorlevel 1 (
    echo Database not found. Creating '%LOCAL_DB_NAME%'...
    psql -U %LOCAL_DB_USER% -h %LOCAL_DB_HOST% -p %LOCAL_DB_PORT% -c "CREATE DATABASE %LOCAL_DB_NAME%;" 2>nul
    if errorlevel 1 (
        echo ERROR: Could not create database. Create it manually:
        echo   psql -U postgres -c "CREATE DATABASE restaurant_db;"
        pause
        exit /b 1
    )
    echo Database created successfully!
) else (
    echo Database '%LOCAL_DB_NAME%' exists.
)

echo.
echo [3/5] Building Backend...
cd backend
call ..\mvnw.cmd clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: Backend build failed!
    cd ..
    pause
    exit /b 1
)
echo Backend built successfully.
cd ..

echo.
echo [4/5] Starting Backend on http://localhost:8080/api ...
start "Restaurant Backend" cmd /c "cd backend && java -jar target\restaurant-platform-0.0.1-SNAPSHOT.jar"

echo Waiting for backend to start...
set RETRIES=0
:wait_backend
timeout /t 3 /nobreak >nul
set /a RETRIES+=1
curl -s http://localhost:8080/api/actuator/health >nul 2>&1
if errorlevel 1 (
    if %RETRIES% LSS 10 (
        echo   Still starting... (%RETRIES%/10)
        goto wait_backend
    ) else (
        echo WARNING: Backend may not have started yet. Continuing anyway...
    )
) else (
    echo Backend is live!
)

echo.
echo [5/5] Starting Frontend...
cd frontend
call npm install --silent 2>nul
if errorlevel 1 (
    echo ERROR: npm install failed!
    cd ..
    pause
    exit /b 1
)

echo.
echo ============================================
echo  Everything is running!
echo   Frontend: http://localhost:4200
echo   Backend:  http://localhost:8080/api
echo   Health:   http://localhost:8080/api/actuator/health
echo ============================================
echo.
call npm start
