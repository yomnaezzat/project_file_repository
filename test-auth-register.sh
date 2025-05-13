#!/bin/bash

# Test the auth register endpoint
echo "Testing /api/v1/auth/register endpoint..."
curl -X POST \
  http://localhost:8080/api/v1/auth/register \
  -H 'Content-Type: application/json' \
  -d '{
    "username": "testuser",
    "password": "Password123!",
    "email": "test@example.com",
    "fullName": "Test User",
    "roles": ["Student"]
}' \
-v

echo -e "\n\nTesting completed."
