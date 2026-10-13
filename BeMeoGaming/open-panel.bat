@echo off
REM ============================================================
REM  open-panel.bat - Mo "Ngoc Rong - Server Control Panel"
REM
REM  Nhan dien CA 2 kieu chay server:
REM    - java -jar dist/BeMeoGaming.jar            (ban dong goi)
REM    - java -classpath "lib/*;build/classes" server.ServerManager
REM
REM  - Neu server dang chay trong PHIEN NAY: chi can ghi file panel.trigger
REM    -> thread PanelTrigger trong chinh JVM server goi ControlPanel.showPanel()
REM       (khong boot lai, khong ngat nguoi choi dang online).
REM  - Neu server chay o phien khac (vd task SYSTEM/session 0) hoac chua chay:
REM    kill server cu + watchdog, doi port 14445 do rong, roi khoi dong lai
REM    trong phien nay -> ServerManager.main tu mo Panel.
REM ============================================================
cd /d "%~dp0"

REM --- 1. Co server (jar hoac class) dang chay trong phien nay khong? ---
set "INMYSESSION=0"
for /f "delims=" %%p in ('powershell -NoProfile -Command "$s=(Get-Process -Id $PID).SessionId; @(Get-CimInstance Win32_Process).Where({$_.Name -eq 'java.exe' -and ($_.CommandLine -like '*server.ServerManager*' -or $_.CommandLine -like '*BeMeoGaming.jar*') -and $_.SessionId -eq $s}).ProcessId"') do set "INMYSESSION=1"

if "%INMYSESSION%"=="1" (
  echo.
  echo [OK] Server dang chay trong phien nay - dang mo panel...
  type nul > "%~dp0panel.trigger"
  echo Panel se hien sau 1-2 giay. Neu dong panel thi chay lai file nay.
  ping -n 5 127.0.0.1 >nul
  exit /b 0
)

echo.
echo Server khong chay trong phien nay - khoi dong lai de mo panel...

REM --- 2. Dung watchdog task truoc (khong cho no restart lai sau 10s) ---
schtasks /End /TN "GameServer-DBS" >nul 2>&1

REM --- 3. Kill java server (jar hoac class) + watchdog loop (bat ky session) ---
powershell -NoProfile -Command "Get-CimInstance Win32_Process | Where-Object { ($_.Name -eq 'java.exe' -and ($_.CommandLine -like '*server.ServerManager*' -or $_.CommandLine -like '*BeMeoGaming.jar*')) -or ($_.Name -eq 'cmd.exe' -and $_.CommandLine -like '*run-watchdog.bat*') } | ForEach-Object { Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue }" >nul 2>&1

REM --- 4. Cho port 14445 do rong (toi da ~30 giay) ---
set TRIES=0
:waitport
netstat -ano | findstr ":14445" | findstr "LISTENING" >nul 2>&1
if errorlevel 1 goto portfree
set /a TRIES=%TRIES%+1
if %TRIES% GEQ 15 goto portbusy
ping -n 2 127.0.0.1 >nul
goto waitport

:portbusy
echo.
echo [LOI] Port 14445 van bi chiem sau 30 giay - khong the mo panel.
echo       Xem tien trinh dang giu port:  netstat -ano ^| findstr 14445
echo       roi kill dung PID do, sau do chay lai file nay.
echo.
pause
exit /b 1

:portfree

REM --- 5. Chac chan MySQL chay (server can DB de boot) ---
tasklist | findstr /I "mysqld.exe" >nul
if errorlevel 1 (
  echo Khoi dong MySQL...
  start "MySQL" /min C:\xampp\mysql\bin\mysqld --defaults-file=C:\xampp\mysql\bin\my.ini --standalone
  ping -n 5 127.0.0.1 >nul
)

REM --- 6. Khoi dong server + watchdog trong phien nay ---
echo Khoi dong server trong phien nay (co watchdog restart)...
start "GameServer-DBS (panel)" /min cmd /c ""%~dp0run-watchdog.bat""
echo Server dang boot... Panel se tu hien sau khoang 25-30 giay.
ping -n 34 127.0.0.1 >nul
exit /b 0
