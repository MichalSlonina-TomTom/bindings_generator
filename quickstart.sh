#!/bin/bash

# Quick Start Guide for Protobuf Bindings Generator
# ==================================================

echo "🚀 Protobuf Bindings Generator - Quick Start"
echo "============================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Step 1: Check prerequisites
echo -e "${BLUE}Step 1: Checking prerequisites...${NC}"
echo ""

if command -v protoc &> /dev/null; then
    PROTOC_VERSION=$(protoc --version)
    echo -e "${GREEN}✓${NC} protoc found: $PROTOC_VERSION"
else
    echo -e "${YELLOW}⚠${NC} protoc not found. Install with:"
    echo "  macOS:   brew install protobuf"
    echo "  Linux:   apt-get install protobuf-compiler"
    echo ""
fi

if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1)
    echo -e "${GREEN}✓${NC} Java found: $JAVA_VERSION"
else
    echo -e "${YELLOW}⚠${NC} Java not found. Install JDK 17 or higher"
    echo ""
fi

echo ""

# Step 2: Build the tool
echo -e "${BLUE}Step 2: Building the bindings generator...${NC}"
echo ""

cd /tmp/bindings_generator

if [ ! -d "build/libs" ]; then
    ./gradlew build --no-daemon --quiet
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓${NC} Build successful!"
    else
        echo -e "${YELLOW}⚠${NC} Build failed. Run: ./gradlew build"
        exit 1
    fi
else
    echo -e "${GREEN}✓${NC} Already built"
fi

echo ""

# Step 3: Show usage examples
echo -e "${BLUE}Step 3: Usage Examples${NC}"
echo ""

echo "Basic usage:"
echo "  ./generate.sh -p your_file.proto -o output_dir"
echo ""

echo "With include paths:"
echo "  ./generate.sh -p proto/file.proto -o output -I proto -I imports"
echo ""

echo "Verbose mode:"
echo "  ./generate.sh -p file.proto -o output -v"
echo ""

echo "Generate C++ only:"
echo "  ./gradlew run --args='-p file.proto -o output --kotlinOutput false'"
echo ""

echo "Generate Kotlin only:"
echo "  ./gradlew run --args='-p file.proto -o output --cppOutput false'"
echo ""

# Step 4: Example with your project
echo -e "${BLUE}Step 4: Example with your project${NC}"
echo ""

PROTO_FILE="/Users/slonina/repo/go-sdk-android/native/src/commonMain/proto/navigation-verbal-message-generation-binding/audio_instruction.proto"
if [ -f "$PROTO_FILE" ]; then
    echo "Run the example script:"
    echo "  ./example.sh"
    echo ""
    echo "Or manually:"
    echo "  ./generate.sh -p $PROTO_FILE -o /tmp/output -I $(dirname $PROTO_FILE) -v"
else
    echo "Proto file not found: $PROTO_FILE"
fi

echo ""

# Step 5: Integration
echo -e "${BLUE}Step 5: Integration with Gradle${NC}"
echo ""

cat << 'EOF'
Add to your build.gradle.kts:

tasks.register<JavaExec>("generateBindings") {
    classpath = files("/tmp/bindings_generator/build/libs/bindings-generator-1.0.0.jar")
    mainClass.set("com.tomtom.sdk.tools.bindingsgenerator.MainKt")
    args(
        "-p", "src/proto/your_file.proto",
        "-o", "build/generated/bindings",
        "-I", "src/proto"
    )
}

tasks.named("compileKotlin") {
    dependsOn("generateBindings")
}
EOF

echo ""
echo ""

# Summary
echo -e "${GREEN}✓ Quick Start Complete!${NC}"
echo ""
echo "Next steps:"
echo "  1. Run: ./example.sh (to test with your proto files)"
echo "  2. Check output in /tmp/generated_bindings"
echo "  3. Integrate into your build system"
echo ""
echo "Documentation:"
echo "  - README.md: Detailed usage guide"
echo "  - PROJECT_SUMMARY.md: Overview and examples"
echo ""

