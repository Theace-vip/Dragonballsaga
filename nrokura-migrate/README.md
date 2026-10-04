# README — Server game + Web `nrokura.site`

Tài liệu này gồm 2 phần: **(A) Cách khởi động / quản lý server** và **(B) Chuyển sang VPS mới**.

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
1. Cài **JDK 21** (chọn bản 21.x).
2. Cài **XAMPP** (Apache + MySQL + PHP) — để mặc định `C:\xampp`.
3. Cài **Git for Windows** (cần `bash.exe` + `curl` cho acme.sh).
4. Mở RDP → **cmd quyền Administrator**.

## B2. Trên MẠY CŨ — đóng gói
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
- Web: `https://nrokura.site` mở được từ điện thoại.
- Game: client vào được (IP VPS mới — **nhớ cập nhật IP trong client/config nếu hardcode IP cũ**).

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
