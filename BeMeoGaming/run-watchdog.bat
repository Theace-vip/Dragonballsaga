@echo off
REM Watchdog server game Dragon Ball - chay duoi Scheduled Task "GameServer-DBS"
REM Neu java bi kill/cla -> khoi dong lai sau 10 giay. Chi chay 1 instance (IgnoreNew).
cd /d C:\Users\Administrator\Downloads\Dragonballsaga\BeMeoGaming

:loop
"C:\Program Files\Java\jdk-21\bin\java.exe" -Xms512m -Xmx2g -Dfile.encoding=UTF-8 --enable-preview -classpath "lib/*;build/classes" --module-path build/classes server.ServerManager >> "server_latest.log" 2>&1
echo [%date% %time%] java ket thuc bat thuong - khoi dong lai sau 10s >> "watchdog.log"
REM dung ping thay timeout: timeout bi abort khi chay duoi scheduled task khong co console
ping -n 11 127.0.0.1 >nul 2>&1
goto loop
