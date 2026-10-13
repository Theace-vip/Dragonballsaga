$ErrorActionPreference = 'SilentlyContinue'

# Buoc 1: kill vong lap watchdog (cmd.exe chay run-watchdog.bat)
$loops = Get-CimInstance Win32_Process | Where-Object { $_.CommandLine -like '*run-watchdog.bat*' }
foreach ($t in $loops) {
    Write-Output ("WATCHDOG-LOOP " + $t.ProcessId + " " + $t.Name + " created=" + $t.CreationDate)
    Stop-Process -Id $t.ProcessId -Force
}
Start-Sleep -Seconds 1

# Buoc 2: kill server chay bang class (server.ServerManager) - KHONG dung toi ban jar
$servers = Get-CimInstance Win32_Process | Where-Object { $_.Name -eq 'java.exe' -and $_.CommandLine -like '*server.ServerManager*' }
foreach ($t in $servers) {
    Write-Output ("CLASS-SERVER " + $t.ProcessId + " created=" + $t.CreationDate)
    Stop-Process -Id $t.ProcessId -Force
}
Start-Sleep -Seconds 1

Write-Output "--- CON LAI ---"
Get-CimInstance Win32_Process | Where-Object { $_.Name -eq 'java.exe' } | ForEach-Object {
    Write-Output ("java PID=" + $_.ProcessId + " session=" + $_.SessionId + " cmd=" + $_.CommandLine)
}
