#!/bin/bash

# Quick start script for local development on Linux/Mac

echo ""
echo "============================================"
echo "  Restaurant Platform - Local Development"
echo "============================================"
echo ""

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Warning: Docker not found. Skipping database container."
    echo "Install Docker from https://www.docker.com/products/docker-desktop"
else
    echo "Starting PostgreSQL database..."
    docker run -d --name restaurant_db \
        -e POSTGRES_USER=postgres \
        -e POSTGRES_PASSWORD=password \
        -e POSTGRES_DB=restaurant_db \
        -p 5432:5432 \
        postgres:15
    echo "PostgreSQL started on port 5432"
fi

echo ""
echo "Starting Backend..."
cd backend || exit
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "Backend build failed!"
    exit 1
fi
echo "Backend built successfully"

# Run backend in background
echo "Starting Spring Boot application..."
java -jar target/*.jar &
BACKEND_PID=$!
echo "Backend running with PID $BACKEND_PID"
cd ..

echo ""
echo "Starting Frontend..."
cd frontend || exit
echo "Installing dependencies..."
npm install
if [ $? -ne 0 ]; then
    echo "Frontend npm install failed!"
    kill $BACKEND_PID
    exit 1
fi
echo "Starting development server..."
npm start

# Cleanup on exit
trap "kill $BACKEND_PID; docker stop restaurant_db 2>/dev/null; exit" INT TERM

