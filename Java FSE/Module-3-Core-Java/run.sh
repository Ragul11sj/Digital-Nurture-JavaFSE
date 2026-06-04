#!/bin/bash
set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC_DIR="$PROJECT_DIR/src/main/java"
OUT_DIR="$PROJECT_DIR/out"
MAIN_CLASS="com.ragulsj.eventmanagement.Main"

echo "=============================================="
echo " Community Event Management System"
echo " Author: Ragul SJ | sjragul555@gmail.com"
echo "=============================================="
echo ""

if ! command -v javac &> /dev/null; then
    echo "[ERROR] javac not found. Install JDK 17+ and ensure it is on PATH."
    exit 1
fi

JAVA_VER=$(javac -version 2>&1 | awk '{print $2}' | cut -d. -f1)
if [ "$JAVA_VER" -lt 17 ]; then
    echo "[ERROR] JDK 17 or higher required. Found: $JAVA_VER"
    exit 1
fi

echo "[INFO] Compiling sources..."
mkdir -p "$OUT_DIR"

find "$SRC_DIR" -name "*.java" > "$OUT_DIR/sources.txt"

CLASSPATH="$OUT_DIR"
if ls "$PROJECT_DIR"/lib/*.jar &>/dev/null 2>&1; then
    for jar in "$PROJECT_DIR"/lib/*.jar; do
        CLASSPATH="$CLASSPATH:$jar"
    done
fi

javac --release 17 -encoding UTF-8 -d "$OUT_DIR" -cp "$CLASSPATH" @"$OUT_DIR/sources.txt"

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed."
    exit 1
fi

echo "[INFO] Compilation successful."
echo "[INFO] Starting application..."
echo ""

java -cp "$CLASSPATH" "$MAIN_CLASS"
