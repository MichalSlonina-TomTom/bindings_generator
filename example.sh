#!/bin/bash

# Example: Generate bindings for audio_instruction.proto

PROTO_DIR="/Users/slonina/repo/go-sdk-android/native/src/commonMain/proto/navigation-verbal-message-generation-binding"
OUTPUT_DIR="/tmp/generated_bindings"

./generate.sh \
  -p "$PROTO_DIR/audio_instruction.proto" \
  -o "$OUTPUT_DIR" \
  -I "$PROTO_DIR" \
  -v

echo ""
echo "Generated files are in: $OUTPUT_DIR"
ls -lh "$OUTPUT_DIR"

