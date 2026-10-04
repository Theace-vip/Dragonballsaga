@echo off
REM =========================================================
REM  Cai dat toan bo server len VPS Windows MOI
REM  - Click chuot phai -> Run as administrator  (hoac chay truc tiep)
REM  - Yeu cau: da cai XAMPP (C:\xampp), JDK 21, Git for Windows
REM =========================================================
cd /d "%~dp0"
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0install.ps1"
pause
