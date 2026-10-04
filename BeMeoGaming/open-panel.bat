@echo off
REM ============================================================
REM  open-panel.bat - Mo "Ngoc Rong - Server Control Panel"
REM
REM  Van de: server chay qua task "GameServer-DBS" la SYSTEM/Session 0
REM  nen cua so Swing (ControlPanel) khong the hien tren man hinh.
REM
REM  - Neu server DANG chay trong phien nay: chi can gui lenh mo panel
REM    (file trigger) -> panel mo lai trong chinh JVM server (khong boot lai).
REM  - Neu server CHUA chay (hoac dang chay duoi SYSTEM/session 0):
REM    dung watchdog task, kill java cu, khoi dong lai trong phien nay
REM    -> ServerManager.main tu dong mo Panel + form gui thu.
REM ============================================================
cd /d "%~dp0"

REM --- 1. Co server ServerManager dang chay trong session nay khong? ---
set "INMYSESSION=0"
for /f "delims=" %%p in ('powershell -NoProfile -Command "$s=(Get-Process -Id $PID).SessionId; $r=@(Get-CimInstance Win32_Process).Where({$_.Name -eq 'java.exe' -and $_.CommandLine -like '*server.ServerManager*' -and $_.SessionId -eq $s}); $r.ProcessId"') do set "INMYSESSION=1"

if "%INMYSESSION%"=="1" (
  echo.
  echo [OK] Server dang chay trong phien nay - dang mo panel...
  type nul > "%~dp0panel.trigger"
  echo Panel se hien sau 1-2 giay. Neu dong panel thi mo lai duoc nhe.
  ping -n 5 127.0.0.1 >nul
  exit /b 0
)

echo.
echo Server khong chay trong phien nay - khoi dong lai de mo panel...

REM --- 2. Dung watchdog task truoc (khong cho no restart lai sau 10s) ---
schtasks /End /TN "GameServer-DBS" >nul 2>&1

REM --- 3. Kill java server + watchdog loop (bat ky session, ke ca SYSTEM) ---
powershell -NoProfile -Command "Get-CimInstance Win32_Process | Where-Object { ($_.Name -eq 'java.exe' -and $_.CommandLine -like '*server.ServerManager*') -or ($_.Name -eq 'cmd.exe' -and $_.CommandLine -like '*run-watchdog.bat*') } | ForEach-Object { Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue }" >nul 2>&1

REM --- 4. Cho port 14445 do rong ---
set TRIES=0
:waitport
netstat -ano | findstr "14445" | findstr "LISTENING" >nul 2>&1
if errorlevel 1 goto portfree
set /a TRIES=TRIES+1
if %TRIES% GEQ 15 goto portfree
ping -n 2 127.0.0.1 >nul
goto waitport
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
