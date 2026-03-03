@echo off
setlocal enabledelayedexpansion
REM Quick start script for local development

echo.
echo ============================================
echo  Restaurant Platform - Local Development
echo ============================================
echo.

REM --- Read DB config from application.properties ---
set PROPS_FILE=backend\src\main\resources\application.properties

for /f "tokens=1,* delims==" %%A in ('findstr /C:"spring.datasource.url" "%PROPS_FILE%"') do set "RAW_URL=%%B"
for /f "tokens=1,* delims==" %%A in ('findstr /C:"spring.datasource.username" "%PROPS_FILE%"') do set "RAW_USER=%%B"
for /f "tokens=1,* delims==" %%A in ('findstr /C:"spring.datasource.password" "%PROPS_FILE%"') do set "RAW_PASS=%%B"

REM Extract default values (text after the colon inside ${...:default})
for /f "tokens=2 delims=:}" %%V in ("!RAW_URL!") do set "DB_URL=%%V"
for /f "tokens=2 delims=:}" %%V in ("!RAW_USER!") do set "DB_USER=%%V"
for /f "tokens=2 delims=:}" %%V in ("!RAW_PASS!") do set "DB_PASS=%%V"

echo Using config from application.properties:
echo   URL:      !DB_URL!
echo   Username: !DB_USER!
echo   Password: ****
echo.

echo [1/3] Building Backend...
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
echo [2/3] Starting Backend on http://localhost:8080/api ...
start "Restaurant Backend" cmd /c "cd backend && java -jar target\restaurant-platform-0.0.1-SNAPSHOT.jar"

echo Waiting for backend to start...
set RETRIES=0
:wait_backend
timeout /t 3 /nobreak >nul
set /a RETRIES+=1
curl -s http://localhost:8080/api/actuator/health >nul 2>&1
if errorlevel 1 (
    if !RETRIES! LSS 10 (
        echo   Still starting... (!RETRIES!/10)
        goto wait_backend
    ) else (
        echo WARNING: Backend may not have started yet. Continuing anyway...
    )
) else (
    echo Backend is live!
)

echo.
echo [3/3] Starting Frontend...
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
