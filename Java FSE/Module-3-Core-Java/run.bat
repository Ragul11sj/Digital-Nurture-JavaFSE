@echo off
setlocal EnableDelayedExpansion

set "PROJECT_DIR=%~dp0"
set "SRC_DIR=%PROJECT_DIR%src\main\java"
set "OUT_DIR=%PROJECT_DIR%out"
set "MAIN_CLASS=com.ragulsj.eventmanagement.Main"

echo ==============================================
echo  Community Event Management System
echo  Author: Ragul SJ ^| sjragul555@gmail.com
echo ==============================================
echo.

where javac >nul 2>&1
if errorlevel 1 (
    echo [ERROR] javac not found. Install JDK 17+ and add to PATH.
    pause
    exit /b 1
)

echo [INFO] Compiling sources...
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

set "CLASSPATH=%OUT_DIR%"
if exist "%PROJECT_DIR%lib\*.jar" (
    for %%f in ("%PROJECT_DIR%lib\*.jar") do (
        set "CLASSPATH=!CLASSPATH!;%%f"
    )
)

dir /s /b "%SRC_DIR%\*.java" > "%OUT_DIR%\sources.txt"

javac --release 17 -encoding UTF-8 -d "%OUT_DIR%" -cp "%CLASSPATH%" @"%OUT_DIR%\sources.txt"

if errorlevel 1 (
    echo [ERROR] Compilation failed.
    pause
    exit /b 1
)

echo [INFO] Compilation successful.
echo [INFO] Starting application...
echo.

java -cp "%CLASSPATH%" %MAIN_CLASS%

pause
