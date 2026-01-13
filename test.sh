#!/bin/bash

# Quick test of the bindings generator

echo "Testing Bindings Generator..."
echo "=============================="
echo ""

# Create test proto file
TEST_DIR="/tmp/test_bindings"
mkdir -p "$TEST_DIR"

cat > "$TEST_DIR/test.proto" << 'EOF'
syntax = "proto3";

package com.test;

enum Status {
  kStatusUnknown = 0;
  kStatusActive = 1;
  kStatusInactive = 2;
}

message Person {
  string name = 1;
  int32 age = 2;
  Status status = 3;
  repeated string hobbies = 4;
}

message Company {
  string name = 1;
  repeated Person employees = 2;
}
EOF

echo "Created test proto file"
echo ""

# Generate bindings
OUTPUT_DIR="$TEST_DIR/generated"
mkdir -p "$OUTPUT_DIR"

cd /tmp/bindings_generator

echo "Generating bindings..."
./gradlew run --no-daemon --quiet --args="-p $TEST_DIR/test.proto -o $OUTPUT_DIR -v"

echo ""
echo "Generated files:"
echo "================<br>ls -lh "$OUTPUT_DIR"

echo ""
echo "Preview of generated C++ header:"
echo "================================="
head -50 "$OUTPUT_DIR/test_protobuf_helpers.hpp"

echo ""
echo "✓ Test completed successfully!"

