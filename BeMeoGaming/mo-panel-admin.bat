@echo off
REM Mo Panel Admin 1 cham - khong can XAMPP Apache, dung PHP co san cua XAMPP
cd /D "%~dp0"
echo Dang kiem tra MySQL...
tasklist | findstr /I "mysqld.exe" >nul
if errorlevel 1 (
  echo Khoi dong MySQL...
  start "MySQL" /min C:\xampp\mysql\bin\mysqld --defaults-file=C:\xampp\mysql\bin\my.ini --standalone
  timeout /t 4 /nobreak >nul
) else (
  echo MySQL dang chay.
)
echo Dang mo web panel (PHP port 8000)...
REM tat server cu tren port 8000 neu co roi mo lai cho sach
for /f "tokens=5" %%a in ('netstat -ano ^| findstr "127.0.0.1:8000" ^| findstr "LISTENING"') do taskkill /F /PID %%a >nul 2>&1
start "Panel PHP" /min C:\xampp\php\php.exe -S 127.0.0.1:8000 -t "d:\Dragonballsaga\BeMeoGaming\htdocs"
timeout /t 3 /nobreak >nul
start "" "http://127.0.0.1:8000/paneladmin/"
echo.
echo Panel: http://127.0.0.1:8000/paneladmin/
echo Giu cua so "Panel PHP" chay ngam - dung tat neu con dung panel.
pause
