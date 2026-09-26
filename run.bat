@echo off
title Warmindo Digital Express - Kasir Racikan Mie
echo ============================================================
echo   Menjalankan Aplikasi Warmindo Digital Express (Java Swing)
echo ============================================================
java WarmindoApp
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Gagal menjalankan dengan 'java'. Mencoba kompilasi ulang terlebih dahulu...
    javac --release 8 *.java
    java WarmindoApp
)
pause
