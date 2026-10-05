@echo off
rem Chay sync-all.sh (dong bo main + web + database len GitHub)
where bash >nul 2>nul
if errorlevel 1 (
    echo Khong tim thay bash. Hay cai Git for Windows.
    exit /b 1
)
bash "%~dp0sync-all.sh"
exit /b %errorlevel%
