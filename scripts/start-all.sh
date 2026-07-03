#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

echo "Starting Service Registry (8761)..."
./gradlew :service-registry:bootRun &
REGISTRY_PID=$!

sleep 8

echo "Starting microservices..."
./gradlew :user-service:bootRun &
USER_PID=$!
./gradlew :restaurant-service:bootRun &
RESTAURANT_PID=$!
./gradlew :order-service:bootRun &
ORDER_PID=$!
./gradlew :delivery-service:bootRun &
DELIVERY_PID=$!

sleep 15

echo "Starting API Gateway (8080)..."
./gradlew :api-gateway:bootRun &
GATEWAY_PID=$!

sleep 12

cleanup() {
  echo "Stopping services..."
  kill $GATEWAY_PID $DELIVERY_PID $ORDER_PID $RESTAURANT_PID $USER_PID $REGISTRY_PID 2>/dev/null || true
}
trap cleanup EXIT

echo "All services started. Gateway: http://localhost:8080"
wait $GATEWAY_PID
