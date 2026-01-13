#!/bin/bash

# Convenience script to run the bindings generator

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

cd "$SCRIPT_DIR"

# Build if needed
if [ ! -d "build/classes" ]; then
    echo "Building bindings generator..."
    ./gradlew build
fi

# Run the generator
./gradlew run --args="$*"

