# ============================================================
# make-backup.ps1 - Chay tren MAY CU de dong goi toan bo server
# Su dung:  powershell -ExecutionPolicy Bypass -File make-backup.ps1 [-Zip]
#   -Zip    : sau khi dong goi thi nen thanh .zip (cham hon nhung chuyen 1 file)
# ============================================================
param(
    [string]$OutRoot = (Join-Path $env:USERPROFILE 'Downloads'),
    [string]$GameSrc = 'C:\Users\Administrator\Downloads\Dragonballsaga\BeMeoGaming',
    [string]$WebSrc  = 'C:\xampp\htdocs\nrokura',
    [switch]$Archive   # tao them 1 file .tar (khong nen) de chuyen 1 lan
)

$ErrorActionPreference = 'Stop'
$stamp = Get-Date -Format 'yyyyMMdd-HHmm'
$pkg   = Join-Path $OutRoot "nrokura-backup-$stamp"

function Log($m) { $l = "[{0}] {1}" -f (Get-Date -Format 'HH:mm:ss'), $m; Write-Host $l }
function Copy-Tree($src, $dst, $xd, $xf) {
    $rx = @($src, $dst, '/E', '/NFL', '/NDL', '/NJH', '/NJS', '/NP')
    if ($xd) { $rx += '/XD'; $rx += $xd }
    if ($xf) { $rx += '/XF'; $rx += $xf }
    & robocopy @rx | Out-Null
    if ($LASTEXITCODE -ge 8) { throw "robocopy loi ($LASTEXITCODE): $src -> $dst" }
}

Log "Tao goi: $pkg"
New-Item -ItemType Directory -Force -Path $pkg | Out-Null

# ---------- 1. Server game (loai tru log/temp/backup cu) ----------
Log "1/7 Copy server game (bo qua htdocs, out, dist, build_*, log, .git)..."
$excludeDirs  = @('htdocs', 'out', 'dist', 'build_panel_fresh', 'build_panel_check2', 'build_test', 'build_verify', 'test', '.git', '.dbq-tmp', 'log', 'Private')
$excludeFiles = @('htdocs.rar', 'server_latest.log', 'server_latest.prev.log', 'server_latest_err.log',
                  'server_latest_err.prev.log', 'watchdog.log', 'server_restart.log', 'server_run.log',
                  'delete.txt', 'build_ctrl.txt', '*.out', '*.log')
Copy-Tree $GameSrc (Join-Path $pkg 'BeMeoGaming') $excludeDirs $excludeFiles

# ---------- 2. Web ----------
Log "2/7 Copy web (htdocs/nrokura)..."
Copy-Tree $WebSrc (Join-Path $pkg 'htdocs\nrokura') $null $null

# ---------- 3. Config Apache/MySQL/PHP ----------
Log "3/7 Copy config..."
$cfg = Join-Path $pkg 'configs'
New-Item -ItemType Directory -Force -Path $cfg | Out-Null
foreach ($f in @(
    'C:\xampp\apache\conf\httpd.conf',
    'C:\xampp\apache\conf\extra\httpd-vhosts.conf',
    'C:\xampp\apache\conf\extra\httpd-default.conf',
    'C:\xampp\mysql\bin\my.ini'
)) { if (Test-Path $f) { Copy-Item $f $cfg -Force } else { Write-Host "  (thua) thieu: $f" } }

# ---------- 4. Cert HTTPS ----------
Log "4/7 Copy cert Let's Encrypt..."
$cert = Join-Path $pkg 'certs'
New-Item -ItemType Directory -Force -Path $cert | Out-Null
if (Test-Path 'C:\xampp\apache\conf\ssl.crt\nrokura.site.crt') {
    Copy-Item 'C:\xampp\apache\conf\ssl.crt\nrokura.site.crt' $cert -Force
    Copy-Item 'C:\xampp\apache\conf\ssl.key\nrokura.site.key'  $cert -Force
} else { Write-Host "  (thua) khong tim thay cert (se cap lai o VPS moi)" }

# ---------- 5. Script chay (acme renew, reload apache) ----------
Log "5/7 Copy script..."
$sfx = Join-Path $pkg 'scripts'
New-Item -ItemType Directory -Force -Path $sfx | Out-Null
if (Test-Path 'C:\xampp\acme-renew.bat')       { Copy-Item 'C:\xampp\acme-renew.bat' $sfx -Force }
if (Test-Path 'C:\xampp\apache\bin\reload-apache.sh') { Copy-Item 'C:\xampp\apache\bin\reload-apache.sh' $sfx -Force }

# ---------- 6. Dump database ----------
Log "6/7 Dump database hondaodragon..."
$dbDir = Join-Path $pkg 'db'
New-Item -ItemType Directory -Force -Path $dbDir | Out-Null
$dump = Join-Path $dbDir 'hondaodragon.sql'
$dumpErr = Join-Path $dbDir 'dump.err'
$exe = 'C:\xampp\mysql\bin\mysqldump.exe'
if (-not (Test-Path $exe)) { throw "Khong tim thay mysqldump: $exe" }
$p = Start-Process -FilePath $exe `
    -ArgumentList '-u', 'root', '--databases', '--single-transaction', '--routines', '--triggers', '--skip-dump-date', 'hondaodragon' `
    -RedirectStandardOutput $dump -RedirectStandardError $dumpErr -Wait -NoNewWindow -PassThru
if ($p.ExitCode -ne 0) { throw "Dump that bai: $(Get-Content $dumpErr -Raw)" }
$sz = (Get-Item $dump).Length / 1MB
Log ("  -> {0:N1} MB" -f $sz)

# ---------- 7. Copy bo cai dat + README vao goi ----------
Log "7/7 Copy bo cai dat (install.ps1/install.bat/README)..."
Copy-Item (Join-Path $PSScriptRoot 'README.md')       $pkg -Force -ErrorAction SilentlyContinue
Copy-Item (Join-Path $PSScriptRoot 'install.ps1')     $pkg -Force -ErrorAction SilentlyContinue
Copy-Item (Join-Path $PSScriptRoot 'install.bat')     $pkg -Force -ErrorAction SilentlyContinue
Copy-Item (Join-Path $PSScriptRoot 'cf-ip-ranges.txt') $pkg -Force -ErrorAction SilentlyContinue

# ---------- Manifest ----------
$total = (Get-ChildItem $pkg -Recurse -File | Measure-Object Length -Sum).Sum
@"
NGAY DONG GOI : $(Get-Date -Format 'yyyy-MM-dd HH:mm')
MAY NGUON     : $env:COMPUTERNAME
TONG KICH THUOC: {0:N1} MB
CAC THU MUC   : BeMeoGaming (server game), htdocs\nrokura (web),
                configs (httpd/my.ini), certs (LE), scripts, db\hondaodragon.sql
HUONG DAN      : xem README.md trong goi nay
"@ -f ($total / 1MB) | Set-Content (Join-Path $pkg 'MANIFEST.txt') -Encoding UTF8

Log ("Tong: {0:N1} MB" -f ($total / 1MB))

# ---------- Dong thanh 1 file .tar (tuy chon) ----------
# Cat: khong nen (.zip/.gz) vi goi co 72k+ file anh nho nen rat cham;
# nen that cung khong giam duoc nhieu (anh da nen san). Chay khi may dang rảnh.
if ($Archive) {
    Log "Dang tao file .tar (khong nen - co the mat 10-40 phut voi o cung)..."
    $tarPath = "$pkg.tar"
    if (Test-Path $tarPath) { Remove-Item $tarPath -Force }
    $leaf = Split-Path $pkg -Leaf
    $parent = Split-Path $pkg -Parent
    & tar.exe -c -f $tarPath -C $parent $leaf
    if ($LASTEXITCODE -ne 0) { Write-Host "  Loi (ma $LASTEXITCODE) - van su duoc thu muc: $pkg" }
    else { Log ("Da tao: {0} ({1:N1} MB)" -f $tarPath, ((Get-Item $tarPath).Length / 1MB)) }
}

Write-Host ""
Write-Host "HOAN TAT. Goi tai: $pkg"
Write-Host "Buoc tiep: copy thu muc (hoac file .zip) sang VPS moi, giai nen, chay install.bat"
