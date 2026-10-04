# ============================================================
# install.ps1 - Cai toan bo server len VPS MOI (Windows)
# Chay voi quyen Administrator:  install.bat (double-click)
# Goi nay gom: BeMeoGaming (game), htdocs\nrokura (web), configs,
#              certs, scripts, db\hondaodragon.sql
# Yeu cau: da cai XAMPP (C:\xampp), JDK 21, Git for Windows
# ============================================================
param(
    [string]$GameDst = 'C:\Dragonballsaga\BeMeoGaming',
    [switch]$SkipAcme,     # bo qua cap/chung chi (chay lai sau khi da doi DNS)
    [switch]$SkipImportDb  # bo qua import database
)

$ErrorActionPreference = 'Stop'
$pkg  = $PSScriptRoot
$xampp = 'C:\xampp'
$domain = 'nrokura.site'
$logFile = Join-Path $pkg 'install.log'
$webDst = Join-Path $xampp 'htdocs\nrokura'

function Log($m) {
    $l = "[{0}] {1}" -f (Get-Date -Format 'HH:mm:ss'), $m
    Write-Host $l
    Add-Content -Path $logFile -Value $l
}
function Die($m) { Log "LOI: $m"; Write-Host "`nNhan Enter de thoat..."; [void](Read-Host); exit 1 }

# ===== 0. TIEN KIEM TRA =====
Log "== 0. Tien kiem tra =="
$id = [Security.Principal.WindowsPrincipal]::new([Security.Principal.WindowsIdentity]::GetCurrent())
if (-not $id.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) { Die "Phai chay duoc quyen Administrator (click install.bat)." }
if (-not (Test-Path "$xampp\apache\bin\httpd.exe")) { Die "Khong tim thay XAMPP tai $xampp. Hay cai XAMPP truoc." }
if (-not (Test-Path (Join-Path $pkg 'BeMeoGaming'))) { Die "Thieu thu muc BeMeoGaming trong goi (ban chua chay make-backup.ps1?)." }

# tim java 21
$java = $null
$cands = @("$env:JAVA_HOME\bin\java.exe", 'C:\Program Files\Java\jdk-21\bin\java.exe',
           'C:\Program Files\Microsoft\jdk-21*\bin\java.exe', 'C:\Program Files\Eclipse Adoptium\jdk-21*\bin\java.exe')
foreach ($c in $cands) { $hit = Get-Item $c -ErrorAction SilentlyContinue | Select-Object -First 1; if ($hit) { $java = $hit.FullName; break } }
if (-not $java) { $g = Get-Command java -ErrorAction SilentlyContinue; if ($g) { $java = $g.Source } }
if (-not $java) { Die "Khong tim thay JDK 21. Hay cai JDK 21 truoc." }
Log "Java: $java"

# ===== 1. COPY SERVER GAME =====
Log "== 1. Copy server game -> $GameDst =="
New-Item -ItemType Directory -Force -Path $GameDst | Out-Null
& robocopy (Join-Path $pkg 'BeMeoGaming') $GameDst /E /NFL /NDL /NJH /NJS /NP | Out-Null
if ($LASTEXITCODE -ge 8) { Die "Copy game that bai (robocopy $LASTEXITCODE)" }

# ===== 2. COPY WEB =====
Log "== 2. Copy web -> $webDst =="
if (Test-Path $webDst) { $bak = "$webDst.bak-$(Get-Date -Format yyyyMMdd)"; Rename-Item $webDst $bak; Log "  (da luu web cu -> $bak)" }
& robocopy (Join-Path $pkg 'htdocs\nrokura') $webDst /E /NFL /NDL /NJH /NJS /NP | Out-Null
if ($LASTEXITCODE -ge 8) { Die "Copy web that bai (robocopy $LASTEXITCODE)" }

# ===== 3. CAU HINH APACHE =====
Log "== 3. Cau hinh Apache =="
# 3a. vhost: thay toan bo (chua vhost 80 + 443 + chan scanner + header)
$vhostDst = "$xampp\apache\conf\extra\httpd-vhosts.conf"
Copy-Item $vhostDst "$vhostDst.preinstall" -Force -ErrorAction SilentlyContinue
Copy-Item (Join-Path $pkg 'configs\httpd-vhosts.conf') $vhostDst -Force
Log "  da thay httpd-vhosts.conf (ban cu luu .preinstall)"

# 3b. httpd.conf: bo comment reqtimeout + them hardening (append cuoi)
$httpd = "$xampp\apache\conf\httpd.conf"
$c = Get-Content -Raw $httpd
$c = $c -replace '(?m)^#LoadModule reqtimeout_module', 'LoadModule reqtimeout_module'
if ($c -notmatch '(?m)^ServerTokens\s+Prod') {
    $c += "`r`n# === Nrokura hardening (install.ps1) ===`r`nServerTokens Prod`r`nServerSignature Off`r`nTraceEnable Off`r`n<IfModule php_module>`r`n    php_admin_flag expose_php Off`r`n    php_admin_flag display_errors Off`r`n    php_value session.cookie_httponly 1`r`n    php_value session.cookie_samesite Lax`r`n</IfModule>`r`n"
    Log "  da them ServerTokens/Security + PHP flags vao httpd.conf"
}
Set-Content -Path $httpd -Value $c -NoNewline
Log "  da bat mod_reqtimeout (chong slowloris)"

# 3c. httpd-default.conf: Timeout/KeepAlive/ServerSignature
$def = "$xampp\apache\conf\extra\httpd-default.conf"
$d = Get-Content -Raw $def
$d = $d -replace '(?m)^Timeout\s+\d+', 'Timeout 30'
$d = $d -replace '(?m)^KeepAliveTimeout\s+\d+', 'KeepAliveTimeout 3'
$d = $d -replace '(?m)^ServerTokens\s+\S+', 'ServerTokens Prod'
$d = $d -replace '(?m)^ServerSignature\s+\S+', 'ServerSignature Off'
Set-Content -Path $def -Value $d -NoNewline
Log "  da chinh Timeout=30, KeepAliveTimeout=3, ServerTokens=Prod"

# ===== 4. CAU HINH MYSQL (bind localhost) =====
Log "== 4. Cau hinh MySQL (chi nghe 127.0.0.1) =="
$myini = "$xampp\mysql\bin\my.ini"
$m = Get-Content -Raw $myini
if ($m -match '(?m)^#?\s*bind-address') {
    # chi sua 1 dong dau tien (khong sua dong ::1 tranh trung bind-address 2 lan -> MySQL loi)
    $m = ([regex]'(?m)^#?\s*bind-address.*$').Replace($m, 'bind-address="127.0.0.1"', 1)
} else {
    $m = ([regex]'(?m)^\[mysqld\]').Replace($m, "[mysqld]`r`nbind-address=`"127.0.0.1`"", 1)
}
Set-Content -Path $myini -Value $m -NoNewline
Log "  da set bind-address=127.0.0.1 (chan NGOAI vao 3306)"

# ===== 5. CERT HTTPS =====
Log "== 5. Cert Let's Encrypt =="
$hasCert = Test-Path (Join-Path $pkg 'certs\nrokura.site.crt')
if ($hasCert) {
    Copy-Item (Join-Path $pkg 'certs\nrokura.site.crt') "$xampp\apache\conf\ssl.crt\" -Force
    Copy-Item (Join-Path $pkg 'certs\nrokura.site.key')  "$xampp\apache\conf\ssl.key\"  -Force
    Log "  da copy cert hieu luc (se duoc acme.sh thay tu dong khi den han)"
} else { Log "  (thua) goi khong co cert - se cap moi o buoc 9" }

# ===== 6. SCRIPT + JAVA =====
Log "== 6. Script chay =="
Copy-Item (Join-Path $pkg 'scripts\reload-apache.sh') "$xampp\apache\bin\reload-apache.sh" -Force -ErrorAction SilentlyContinue
# tao lai run-watchdog.bat dung duong dan moi
$wd = @"
@echo off
REM Watchdog server game - tu khoi dong lai neu java chet. Duoc chay boi Task "GameServer-DBS"
cd /d $GameDst
:loop
"$java" -Dfile.encoding=UTF-8 --enable-preview -classpath "lib/*;build/classes" --module-path build/classes server.ServerManager >> "server_latest.log" 2>&1
echo [%date% %time%] java ket thuc bat thuong - khoi dong lai sau 10s >> "watchdog.log"
timeout /t 10 /nobreak >nul
goto loop
"@
Set-Content -Path (Join-Path $GameDst 'run-watchdog.bat') -Value $wd -Encoding ASCII
Log "  da tao run-watchdog.bat (java: $java)"

# tao lai acme-renew.bat dung HOME cua may nay
$unixHome = $env:USERPROFILE -replace '\\', '/'
$acmeBat = @"
@echo off
set HOME=$env:USERPROFILE
"C:\Program Files\Git\bin\bash.exe" -c "export HOME=$unixHome; export PATH=/mingw64/bin:/usr/bin:/c/Windows/System32:/c/Windows; $unixHome/.acme.sh/acme.sh --renew -d $domain" >> "C:\xampp\acme-renew.log" 2>&1
"@
Set-Content -Path "$xampp\acme-renew.bat" -Value $acmeBat -Encoding ASCII
Log "  da tao C:\xampp\acme-renew.bat"

# ===== 7. DICH VU (service) =====
Log "== 7. Cai dich vu Apache + MySQL =="
if (-not (Get-Service 'Apache2.4' -ErrorAction SilentlyContinue)) {
    & "$xampp\apache\bin\httpd.exe" -k install | Out-Host
    Start-Sleep -Seconds 2
}
if (-not (Get-Service 'MySQL' -ErrorAction SilentlyContinue)) {
    & "$xampp\mysql\bin\mysqld.exe" --install MySQL --defaults-file='c:/xampp/mysql/bin/my.ini' | Out-Host
    Start-Sleep -Seconds 2
}
& sc.exe failure Apache2.4 reset= 86400 actions= restart/5000/restart/15000/restart/60000 | Out-Null
& sc.exe failureflag Apache2.4 1 | Out-Null
& sc.exe failure MySQL reset= 86400 actions= restart/5000/restart/15000/restart/60000 | Out-Null
& sc.exe failureflag MySQL 1 | Out-Null
Start-Service MySQL -ErrorAction SilentlyContinue
Start-Service Apache2.4 -ErrorAction SilentlyContinue
Start-Sleep -Seconds 5
Log "  Apache2.4 = $((Get-Service Apache2.4).Status); MySQL = $((Get-Service MySQL).Status)"

# ===== 8. DATABASE =====
if (-not $SkipImportDb) {
    Log "== 8. Import database hondaodragon =="
    # cho MySQL san sang
    for ($i = 0; $i -lt 20; $i++) {
        & "$xampp\mysql\bin\mysql.exe" -u root -e "SELECT 1" *> $null
        if ($LASTEXITCODE -eq 0) { break }
        Start-Sleep -Seconds 3
    }
    $sql = Join-Path $pkg 'db\hondaodragon.sql'
    if (Test-Path $sql) {
        $p = Start-Process -FilePath "$xampp\mysql\bin\mysql.exe" -ArgumentList '-u', 'root' `
            -RedirectStandardInput $sql -RedirectStandardError (Join-Path $pkg 'db\import.err') `
            -Wait -NoNewWindow -PassThru
        if ($p.ExitCode -ne 0) { Log "  CAUTION: import loi (co the DB da co san) - xem db\import.err" }
        else { Log "  import OK" }
    } else { Log "  (thua) khong thay db\hondaodragon.sql" }
}

# ===== 9. CHUNG CHI + ACME =====
Log "== 9. Cap chung chi (acme.sh) =="
$bash = 'C:\Program Files\Git\bin\bash.exe'
if (-not $SkipAcme -and (Test-Path $bash)) {
    $envHOME = $unixHome
    if (-not (Test-Path "$env:USERPROFILE\.acme.sh\acme.sh")) {
        Log "  cai dat acme.sh..."
        & $bash -c "export HOME=$envHOME; export PATH=/mingw64/bin:/usr/bin; curl -fsSL https://get.acme.sh | sh" 2>&1 | ForEach-Object { Log "    $_" }
    }
    if (Test-Path "$env:USERPROFILE\.acme.sh\acme.sh") {
        Log "  set CA = letsencrypt..."
        & $bash -c "export HOME=$envHOME; export PATH=/mingw64/bin:/usr/bin; `$HOME/.acme.sh/acme.sh --set-default-ca --server letsencrypt" 2>&1 | ForEach-Object { Log "    $_" }
        Log "  issue cert cho $domain (can DNS da tro IP VPS nay)..."
        $issue = & $bash -c "export HOME=$envHOME; export PATH=/mingw64/bin:/usr/bin; `$HOME/.acme.sh/acme.sh --issue -d $domain -d www.$domain --webroot $($webDst -replace '\','/')" 2>&1
        $issue | ForEach-Object { Log "    $_" }
        if (Test-Path "$env:USERPROFILE\.acme.sh\${domain}_ecc\${domain}.cer") {
            Log "  install cert vao Apache..."
            & $bash -c "export HOME=$envHOME; export PATH=/mingw64/bin:/usr/bin; `$HOME/.acme.sh/acme.sh --install-cert -d $domain --fullchain-file C:/xampp/apache/conf/ssl.crt/$domain.crt --key-file C:/xampp/apache/conf/ssl.key/$domain.key --reloadcmd `"sh C:/xampp/apache/bin/reload-apache.sh`"" 2>&1 | ForEach-Object { Log "    $_" }
            Restart-Service Apache2.4
            Log "  cert OK - da restart Apache"
        } else {
            Log "  CHUA CAP DUOC CERT. Kiem tra: DNS $domain da tro IP VPS nay chua? (Cloudflare -> A record)."
            Log "  Chay lai sau: install.bat  hoac  vao C:\xampp\acme-renew.bat de chay thu."
        }
    } else { Log "  acme.sh chua cai duoc (can Internet + Git for Windows)" }
} else { Log "  bo qua (-SkipAcme hoac khong co Git)" }

# ===== 10. TASK TU DONG =====
Log "== 10. Tao task tu dong =="
& schtasks /Create /TN 'GameServer-DBS' /SC ONSTART /TR "`"$GameDst\run-watchdog.bat`"" /RU SYSTEM /F | Out-Null
Log "  GameServer-DBS (khoi dong cung Windows, chet -> tu restart)"
& schtasks /Create /TN 'acme-renew-nrokura' /SC DAILY /ST 02:30 /TR "C:\xampp\acme-renew.bat" /RU SYSTEM /F | Out-Null
Log "  acme-renew-nrokura (02:30 hang ngay)"

# ===== 11. FIREWALL =====
Log "== 11. Firewall =="
# rule cho phep truoc khi bat firewall (tranh mat ket noi remote)
& netsh advfirewall firewall add rule name='VPS-RDP-3389' dir=in action=allow protocol=TCP localport=3389 | Out-Null
& netsh advfirewall firewall delete rule name='14445' | Out-Null
& netsh advfirewall firewall add rule name='14445' dir=in action=allow protocol=TCP localport=14445 | Out-Null
# chi Cloudflare duoc vao 80/443
$cfFile = Join-Path $pkg 'cf-ip-ranges.txt'
$ips = @()
if (Test-Path $cfFile) { $ips = Get-Content $cfFile | Where-Object { $_ -match '^\S+' } }
if ($ips.Count -eq 0) {
    try { $ips = ((Invoke-WebRequest -Uri 'https://www.cloudflare.com/ips-v4' -UseBasicParsing).Content + "`n" +
                  (Invoke-WebRequest -Uri 'https://www.cloudflare.com/ips-v6' -UseBasicParsing).Content) -split "`n" } catch {}
}
$ips = $ips | ForEach-Object { $_.Trim() } | Where-Object { $_ }
$cfList = ($ips -join ',')
& netsh advfirewall firewall delete rule name='CF-HTTP-80' | Out-Null
& netsh advfirewall firewall delete rule name='CF-HTTPS-443' | Out-Null
& netsh advfirewall firewall add rule name='CF-HTTP-80' dir=in action=allow protocol=TCP localport=80 remoteip=$cfList | Out-Null
& netsh advfirewall firewall add rule name='CF-HTTPS-443' dir=in action=allow protocol=TCP localport=443 remoteip=$cfList | Out-Null
Log "  cho phep $($ips.Count) dai IP Cloudflare vao 80/443 (14445 + RDP mo rong)"
& netsh advfirewall set allprofiles state on | Out-Null
Log "  firewall = ON (BlockInbound)"

# ===== 12. KHOI DONG SERVER GAME =====
Log "== 12. Khoi dong server game =="
& schtasks /Run /TN 'GameServer-DBS' | Out-Null
Start-Sleep -Seconds 3
Log "  da chay task GameServer-DBS (khoi dong mat ~60s, xem log)"

# ===== 13. KIEM TRA CUOI =====
Log "== 13. Trang thai =="
Start-Sleep -Seconds 6
$svc = Get-Service Apache2.4, MySQL -ErrorAction SilentlyContinue | ForEach-Object { "$($_.Name)=$($_.Status)" }
Log ("  dich vu: " + ($svc -join ', '))
foreach ($t in @('GameServer-DBS', 'acme-renew-nrokura')) {
    $st = (& schtasks /Query /TN $t /FO CSV /NH 2>$null) -split ',' | Select-Object -Index 2
    Log "  task ${t}: $st"
}
$ports = (netstat -ano | Select-String ':80\s|:443\s|:3306\s|:14445\s' | Select-String 'LISTENING') -join "`n"
Log "  port dang lang nghe:`n$ports"
try {
    $r = Invoke-WebRequest -Uri 'http://localhost/' -UseBasicParsing -TimeoutSec 10
    Log "  web localhost -> HTTP $($r.StatusCode)"
} catch { Log "  web localhost -> loi (xem Apache log)" }

Write-Host ""
Write-Host "=========== HOAN TAT ==========="
Write-Host "Web    : https://$domain (khi DNS da tro)"
Write-Host "Game   : chay duoc roi - task GameServer-DBS"
Write-Host "Log    : $logFile"
Write-Host "Doc    : README.md (cach khoi dong / xu ly su co)"
Write-Host "Nhan Enter de thoat..."
[void](Read-Host)
