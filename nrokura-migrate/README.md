# README — Server game + Web `nrokura.site`

Tài liệu này gồm 2 phần: **(A) Cách khởi động / quản lý server** và **(B) Chuyển sang VPS mới**.

> Tổng quan hệ thống + code nằm ở nhánh nào + hướng dẫn chuyển VPS từng bước:
> xem **[README.md ở gốc repo](../README.md)** (trang chính trên GitHub).
> File này đi sâu vào vận hành, xử lý sự cố và bộ script migrate.

---

# PHẦN A — Khởi động & quản lý server

## A1. Hệ thống gồm gì, và tại sao "máy bật là chạy hết"

Mỗi khi **bật nguồn / Restart Windows**, mọi thứ tự chạy, **không cần bấm gì**:

| Thành phần | Cơ chế tự chạy | Cổng |
|---|---|---|
| **Web** (Apache 2.4 + PHP) | Windows Service `Apache2.4` (AUTO_START) | 80, 443 |
| **Database** (MySQL) | Windows Service `MySQL` (AUTO_START) | 127.0.0.1:3306 |
| **Server game** | Task `GameServer-DBS` chạy `run-watchdog.bat` | 14445 |
| **Gia hạn cert HTTPS** | Task `acme-renew-nrokura` lúc 02:30 hàng ngày | — |

- Web và game **độc lập**: cái này chết cái kia vẫn chạy.
- `run-watchdog.bat`: nếu java bị kill/crash → **tự khởi động lại sau 10 giây** (xem `watchdog.log`).
- Apache/MySQL có **auto-recovery**: service lỗi → Windows tự restart sau 5s/15s/60s.

> ⚠️ **Đừng bấm Start/Stop trên XAMPP Control Panel** — nó đụng vào service đang chạy.
> Muốn dừng/bật thì dùng lệnh bên dưới.

## A2. Lệnh điều khiển (cmd chạy quyền Administrator)

```cmd
:: --- WEB ---
net start Apache2.4          :: bật web
net stop  Apache2.4          :: tắt web
net restart Apache2.4        :: không có - dùng stop rồi start

:: --- DATABASE ---
net start MySQL
net stop  MySQL

:: --- SERVER GAME ---
schtasks /Run  /TN GameServer-DBS      :: bật game
schtasks /End  /TN GameServer-DBS      :: dừng task (java cũng chết theo)
powershell -Command "Get-Process java | Stop-Process -Force"   :: kill java (watchdog tự bật lại sau 10s)

:: --- TRẠNG THÁI ---
sc query Apache2.4
sc query MySQL
schtasks /Query /TN GameServer-DBS /V /FO LIST | findstr /C:"Status"
```

## A3. Kiểm tra nhanh mọi thứ có sống không

```cmd
netstat -ano | findstr ":80 :443 :3306 :14445" | findstr LISTENING
curl -k https://localhost/            (hoặc mở trình duyệt: https://localhost)
```
Có `LISTENING` ở cả 4 port = web + DB + game OK.

## A4. Log nằm ở đâu

| Việc | File |
|---|---|
| Server game (chính) | `C:\Dragonballsaga\BeMeoGaming\server_latest.log` (hoặc thư mục game bạn cài) |
| Watchdog (java chết/bật lại) | `...\BeMeoGaming\watchdog.log` |
| Apache lỗi | `C:\xampp\apache\logs\error.log` |
| Web truy cập | `C:\xampp\apache\logs\nrokura_ssl_access.log` |
| Gia hạn cert | `C:\xampp\acme-renew.log` |
| Cài đặt (khi migrate) | `install.log` |

Console mangled tiếng Việt → **mở file log bằng Notepad++, đừng đọc trên console**.

## A4b. Cập nhật code lên GitHub

```bash
./sync-all.sh                 # main + web + database  (hoac click sync-all.bat)
bash sync-web.sh              # CHI web (nhanh, khong clone lai repo)
```

`sync-all.sh` mặc định lấy web từ `C:\Users\Administrator\Downloads\Web Nro KOL`.
Web đang chạy ở chỗ khác thì đặt biến:
```bash
SYNC_WEB_SRC="/c/xampp/htdocs/nrokura" bash sync-all.sh
```

> Web trên server dùng **cache-busting theo `filemtime`** (`head.php` → `?v=<mtime>`),
> nên sau khi sửa css/js chỉ cần tải lại trang là client nhận bản mới (HTML không bị cache).

## A5. Quản lý database

- **GUI**: mở trình duyệt **trên máy chủ** → `http://localhost/phpmyadmin`
  (chỉ máy chủ vào được; từ ngoài internet là **403**).
- **Công cụ**: DBeaver/HeidiSQL kết nối `127.0.0.1:3306`, user `root`, pass trống.
- **CMD**: `C:\xampp\mysql\bin\mysql.exe -u root hondaodragon`

## A6. Xử lý sự cố thường gặp

| Triệu chứng | Làm ngay |
|---|---|
| Web không mở | `net start Apache2.4` → xem `apache\logs\error.log` |
| Game không vào được | `schtasks /Run /TN GameServer-DBS` → xem `watchdog.log` + `server_latest.log` |
| MySQL không chạy | `net start MySQL` |
| Mất HTTPS / cert sắp hết hạn | `C:\xampp\acme-renew.bat` (chạy tay), xem `C:\xampp\acme-renew.log` |
| Apache đổi config mà không ăn | `C:\xampp\apache\bin\httpd.exe -t` (test cú pháp) rồi `net stop/start Apache2.4` |
| Bị tấn công (HTTP flood/bot) | Cloudflare → Security → **Enable Under Attack Mode** |
| Cloudflare đổi dải IP | Cập nhật rule firewall (xem A7) |

## A7. Cloudflare — 2 việc cần nhớ

1. **Dashboard nên bật**: SSL/TLS → **Full (strict)**; Edge Certificates → **Always Use HTTPS**; WAF → Managed Rules + Bot Fight Mode.
2. **Cập nhật dải IP Cloudflare** (khi CF đổi list, site sẽ timeout nếu không cập nhật):
   ```cmd
   curl -s https://www.cloudflare.com/ips-v4 > %TEMP%\cf4.txt
   curl -s https://www.cloudflare.com/ips-v6 > %TEMP%\cf6.txt
   :: gop lai thanh 1 chuoi phan cach bang dau phay roi:
   netsh advfirewall firewall delete rule name="CF-HTTP-80"
   netsh advfirewall firewall delete rule name="CF-HTTPS-443"
   netsh advfirewall firewall add rule name="CF-HTTP-80" dir=in action=allow protocol=TCP localport=80 remoteip=<CHUOI-IP>
   netsh advfirewall firewall add rule name="CF-HTTPS-443" dir=in action=allow protocol=TCP localport=443 remoteip=<CHUOI-IP>
   ```
   (Cách tự động: chạy lại `install.bat` — nó sẽ cập nhật lại toàn bộ.)

## A8. Bảo mật còn 2 điểm cần lưu ý

1. **MySQL root chưa có mật khẩu** — giờ đã chặn ngoài (chỉ 127.0.0.1) nên an toàn hơn, nhưng nên đặt mật khẩu:
   đặt xong phải cập nhật `data/config/config.properties` (game) và `C:\xampp\htdocs\nrokura\config.php` (web).
2. **Mật khẩu gửi plaintext trong packet game (14445)** — chỉ sửa được khi có source client.
   (Phần DB thì **đã băm PBKDF2**, xong.)

---

# PHẦN B — Chuyển sang VPS mới

## B1. Chuẩn bị trên VPS mới (Windows)
1. Cài **JDK 21** (chọn bản 21.x — máy hiện tại dùng 21.0.9 LTS) vào `C:\Program Files\Java\jdk-21`.
2. Cài **XAMPP 8.0.x** — để mặc định `C:\xampp`.
   Máy hiện tại: Apache **2.4.58** + PHP **8.0.30** + MariaDB **10.4.32**,
   PHP cần bật: `curl, mysqli, pdo_mysql, openssl, mbstring, fileinfo, zip, xml`.
3. Cài **Git for Windows** (cần `bash.exe` + `curl` cho acme.sh).
4. Mở RDP → **cmd quyền Administrator**.
5. **Máy CŨ: dừng ghi dữ liệu trước khi đóng gói**, không thì DB bị lệch:
   ```cmd
   schtasks /End /TN GameServer-DBS
   net stop Apache2.4        :: MySQL de chay vi con phai dump
   ```

## B1b. Không dùng gói backup — lấy code từ GitHub

Toàn bộ code nằm trên GitHub (`main` = game, `web` = web, `database` = dump DB):

```cmd
:: 1. Game
 git clone https://github.com/Theace-vip/Dragonballsaga.git C:\Dragonballsaga
:: 2. Web (noi dung nhanh "web" chinh la thu muc web)
 git clone -b web --depth 1 https://github.com/Theace-vip/Dragonballsaga.git %TEMP%\web-src
 robocopy %TEMP%\web-src C:\xampp\htdocs\nrokura /E /XD .git
:: 3. Database
 git clone -b database --depth 1 https://github.com/Theace-vip/Dragonballsaga.git %TEMP%\db-src
 C:\xampp\mysql\bin\mysql.exe -u root -e "CREATE DATABASE IF NOT EXISTS hondaodragon DEFAULT CHARACTER SET utf8mb4;"
 for %f in (%TEMP%\db-src\tables\*.sql) do C:\xampp\mysql\bin\mysql.exe -u root --default-character-set=utf8mb4 hondaodragon < "%f"
```

> Config Apache/MySQL + cert + script (`acme-renew.bat`, `reload-apache.sh`) **không có trên GitHub**
> → lấy trong gói `make-backup.ps1` (`configs/`, `certs/`, `scripts/`) hoặc làm lại theo mục B1c.

**Bắt buộc sửa sau khi clone:** IP game hardcode trong `data\config\config.properties`:
```
server.sv1=hondaodragon:<IP VPS MOI>:14445
server.port=14445
```

Nếu IP mới: mở firewall (3389 RDP trước, rồi 14445, 80/443 chỉ cho dải Cloudflare) — xem B4 và mục 11 của `install.ps1`.

## B1c. Cấu hình Apache/MySQL tối thiểu (nếu không có thư mục `configs/`)

1. `C:\xampp\apache\conf\httpd.conf`: bật `mod_rewrite`, `mod_ssl`, `mod_headers`, `mod_reqtimeout`;
   thêm `ServerTokens Prod`, `ServerSignature Off`, `TraceEnable Off`.
2. `C:\xampp\apache\conf\extra\httpd-vhosts.conf`:
   - `:80` → `ServerName nrokura.site`, `DocumentRoot "C:/xampp/htdocs/nrokura"`, `AllowOverride All`,
     chặn `/sql/`, **301 sang HTTPS**.
   - `:443` → `SSLCertificateFile "conf/ssl.crt/nrokura.site.crt"`, `SSLCertificateKeyFile "conf/ssl.key/nrokura.site.key"`.
   - Test: `C:\xampp\apache\bin\httpd.exe -t` → `net stop Apache2.4` + `net start Apache2.4`.
3. `C:\xampp\mysql\bin\my.ini`: `bind-address="127.0.0.1"` (chỉ sửa **dòng đầu tiên**, không sửa dòng `::1`).

## B2. Trên MÁY CŨ — đóng gói
```cmd
powershell -ExecutionPolicy Bypass -File make-backup.ps1            :: tao thu muc goi
powershell -ExecutionPolicy Bypass -File make-backup.ps1 -Archive   :: tao ca 1 file .tar
```
→ Tạo `Downloads\nrokura-backup-<ngày>` (~1.9GB: game + web + config + cert + DB dump).
Nếu thêm `-Archive` sẽ có thêm `.tar` (không nén — ổ nhiều file ảnh nên nén `.zip` rất chậm, cứ để .tar).

## B3. Chuyển sang VPS mới
1. Copy **thư mục goi** (hoặc file `.tar`) sang VPS mới.
   - Nếu là `.tar`: giải nén bằng lệnh `tar -xf nrokura-backup-....tar` (Win10/11 có sẵn) hoặc dùng 7-Zip.
2. Vào thư mục đã giải nén → **click `install.bat`** (quyền Administrator).
   Script tự làm hết: copy game/web → vá config → cài service Apache/MySQL → import DB →
   tạo task (game + renew cert) → cấu firewall (chỉ Cloudflare vào 80/443) → bật server game.
3. Nếu chưa kịp đổi DNS → chạy lại `install.bat` sau khi đã trỏ DNS (hoặc thêm tham số `-SkipAcme` lúc đầu).

## B4. Trên Cloudflare (sau khi cài xong)
- Vào **DNS** → sửa record `A @` → **IP của VPS mới** (giữ **Proxied** ✅), record `www` giữ CNAME.
- Chờ 1–2 phút rồi test: `https://nrokura.site` → 200.

## B5. Kiểm tra sau migrate
```cmd
netstat -ano | findstr ":80 :443 :3306 :14445" | findstr LISTENING
curl -k https://localhost/
schtasks /Query /TN GameServer-DBS
```
- Web: `https://nrokura.site` mở được từ **điện thoại dùng 4G** (kiểm tra DNS + Cloudflare + firewall từ bên ngoài).
- Game: client vào được (IP VPS mới — **nhớ cập nhật `server.sv1` trong `data\config\config.properties`,
  và file config của client nếu client hardcode IP cũ, rồi phát lại link tải**).
- Đăng ký 1 tài khoản thử trên web → đăng nhập được vào game = web và game đang dùng **chung** database `hondaodragon`.

## B6. Sau migrate nên làm
- Chạy `netsh advfirewall show allprofiles` → chắc chắn `State: ON`.
- Vào `C:\xampp\acme-renew.bat` 1 lần để xác nhận cert renew chạy (xem `acme-renew.log`).
- **Gỡ/hủy map IP cũ** (nếu VPS cũ vẫn còn) để không double-run server.
- Cập nhật IP mới ở nhà cung cấp/domain nếu có chỗ nào trỏ thẳng IP.

---

### Cấu trúc gói migrate
```
nrokura-backup-<ngày>/
├── README.md            ← file này
├── install.bat          ← chạy trên VPS mới
├── install.ps1
├── cf-ip-ranges.txt     ← dải IP Cloudflare cho firewall
├── MANIFEST.txt
├── BeMeoGaming/         ← server game (src, build, lib, data 1.8GB, Eff, script)
├── htdocs/nrokura/      ← web
├── configs/             ← httpd.conf, httpd-vhosts.conf, httpd-default.conf, my.ini
├── certs/               ← nrokura.site.crt/.key (Let's Encrypt)
├── scripts/             ← acme-renew.bat, reload-apache.sh
└── db/hondaodragon.sql  ← dump database
```
