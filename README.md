# NROKuRA — server game + web nạp thẻ (`nrokura.site`)

Toàn bộ hệ thống đang chạy gồm **3 phần**, trên **cùng một máy Windows**:

| Phần | Là gì | Chạy thế nào |
|---|---|---|
| **Server game** | Java (`BeMeoGaming`), vào cổng **14445** | Scheduled Task `GameServer-DBS` → `run-watchdog.bat` (java chết → tự bật lại sau 10 giây) |
| **Web nạp thẻ** | PHP (`nrokura.site`), Apache cổng **80/443** | Windows Service `Apache2.4` |
| **Database** | MariaDB, database **`hondaodragon`** | Windows Service `MySQL`, chỉ nghe `127.0.0.1:3306` |

Web và game **dùng chung database** (`account`, `player`…): đăng ký trên web là vào được game.

> **Tài liệu chi tiết về vận hành / xử lý sự cố: [`nrokura-migrate/README.md`](nrokura-migrate/README.md)**
> Trang này là điểm bắt đầu: hệ thống có gì, code nằm ở đâu, và **chuyển sang VPS khác phải làm gì**.

---

## 1. Code nằm ở đâu (các nhánh)

| Nhánh | Nội dung | Lấy về thế nào |
|---|---|---|
| `main` | Server game: `BeMeoGaming/` (src, build/classes, lib, data, htdocs, tools) + bộ cài migrate | `git clone` |
| `web` | Mã nguồn web `nrokura.site` (giải nén ra chính là nội dung `htdocs/nrokura`) | `git clone -b web` |
| `database` | Dump MariaDB `hondaodragon` — mỗi bảng 1 file `tables/*.sql` + `restore.sh` | `git clone -b database` |
| `spine-src` | Frame pack cho client (không cần cho server) | chỉ dùng khi build lại client |

Repo: <https://github.com/Theace-vip/Dragonballsaga>

> ⚠️ **Repo đang để PUBLIC.** Trong đó có `config.php` (thông tin kết nối DB) và nhánh `database` chứa toàn bộ tài khoản người chơi.
> Nếu không muốn lộ, vào **Settings → General → Danger Zone → Change visibility → Private** (nhớ `git remote` không đổi, chỉ cần đăng nhập lại khi push).
> Vì `config.php` từng bị public, nên **đổi lại `CF_SECRET_KEY` (Turnstile) trong Cloudflare** và cân nhắc đặt mật khẩu MySQL root (xem `nrokura-migrate/README.md` § A8).

### Cập nhật code lên GitHub (trên máy chủ)

```bash
# 1) Toàn bộ: main + web + database
./sync-all.sh            # hoặc click sync-all.bat

# 2) Chỉ web (nhanh hơn nhiều — không clone lại repo ~2,4GB)
bash sync-web.sh
```

`sync-all.sh` lấy web từ thư mục `C:\Users\Administrator\Downloads\Web Nro KOL` theo mặc định.
Nếu web đang chạy ở chỗ khác (`C:\xampp\htdocs\nrokura`), đặt biến môi trường:

```bash
SYNC_WEB_SRC="/c/xampp/htdocs/nrokura" bash sync-all.sh
```

---

## 2. Thông số hệ thống hiện tại (đã kiểm tra ngày 10/10/2026)

| Thành phần | Phiên bản / giá trị |
|---|---|
| Hệ điều hành | Windows (XAMPP cài ở `C:\xampp`) |
| Java | **JDK 21.0.9 LTS** — `C:\Program Files\Java\jdk-21` |
| Web server | Apache **2.4.58** (Win64) |
| PHP | **8.0.30** (cần: `curl, mysqli, pdo_mysql, openssl, mbstring, fileinfo, zip, xml, json`) |
| Database | MariaDB **10.4.32** — database `hondaodragon`, user `root`, **mật khẩu trống**, chỉ nghe `127.0.0.1` |
| Cổng | `80`, `443` (web) · `14445` (game) · `3306` (DB, chỉ local) |
| Thư mục web | `C:\xampp\htdocs\nrokura` (DocumentRoot của vhost) |
| Thư mục game | `C:\Users\Administrator\Downloads\Dragonballsaga\BeMeoGaming` |
| Lệnh chạy game | `java -Xms512m -Xmx2g -Dfile.encoding=UTF-8 --enable-preview -classpath "lib/*;build/classes" --module-path build/classes server.ServerManager` |
| Cấu hình game | `BeMeoGaming/data/config/config.properties` (`server.port=14445`, `server.sv1=hondaodragon:<IP>:14445`) |
| HTTPS | Let's Encrypt **EC-256**, lưu ở `C:\Users\Administrator\.acme.sh\nrokura.site_ecc` |
| Task tự động | `GameServer-DBS` (khi Windows khởi động) · `acme-renew-nrokura` (02:30 hằng ngày) |
| DNS | `nrokura.site` qua **Cloudflare** (Proxy ✅) → origin `160.191.242.240` |
| Firewall | Chỉ mở 14445 cho mọi IP; **80/443 chỉ cho dải IP Cloudflare** (`CF-HTTP-80`, `CF-HTTPS-443`) |

Kích thước game cần chuyển: `data` ≈ **2,8 GB** (trong đó `data/icon_botnet` 1,3 GB), `htdocs` ≈ **789 MB**, `build` ≈ 44 MB, `lib` ≈ 5,8 MB.

---

## 3. Chuyển sang VPS khác — cần cài gì, làm gì

### 3.0. Chuẩn bị

**Cài trên VPS mới (đúng các bản dưới đây để không phải sửa gì):**

1. **Windows Server 2019/2022** (hoặc Windows 10/11 Pro) + bật **RDP**.
2. **JDK 21** (Microsoft Build of OpenJDK / Eclipse Temurin 21) → cài vào `C:\Program Files\Java\jdk-21`.
3. **XAMPP 8.0.x** (Apache 2.4.58 + PHP 8.0.30 + MariaDB 10.4.32) → cài **mặc định vào `C:\xampp`**.
4. **Git for Windows** (cần `bash.exe` + `curl` cho acme.sh) — cài mặc định.
5. **7-Zip** (nếu chuyển bằng file nén).

**Mở các extension PHP** trong `C:\xampp\php\php.ini` (bỏ dấu `;` đầu dòng): `curl`, `mysqli`, `pdo_mysql`, `openssl`, `mbstring`, `fileinfo`, `zip`, `xml`.
Kiểm tra: `C:\xampp\php\php.exe -m | findstr /I "curl mysqli pdo_mysql openssl mbstring"`

**Việc phải làm trên máy CŨ:** dừng ghi dữ liệu để tránh lệch DB khi chuyển:
```cmd
schtasks /End /TN GameServer-DBS
net stop Apache2.4
```
(dừng web để không phát sinh nạp thẻ trong lúc dump; MySQL **để chạy** vì còn phải dump.)

---

### 3.1. Cách A — dùng bộ cài có sẵn (nhanh nhất, đã chạy thử)

**Trên máy CŨ:**
```powershell
cd C:\Users\Administrator\Downloads\Dragonballsaga\nrokura-migrate
powershell -ExecutionPolicy Bypass -File make-backup.ps1            # tạo thư mục gói
powershell -ExecutionPolicy Bypass -File make-backup.ps1 -Archive   # (tuỳ chọn) gộp thành 1 file .tar
```
→ Tạo `C:\Users\Administrator\Downloads\nrokura-backup-<ngày-giờ>` (~1,9 GB) gồm: game, web, `configs/` (httpd.conf, httpd-vhosts.conf, httpd-default.conf, my.ini), `certs/` (cert Let's Encrypt), `scripts/` (acme-renew.bat, reload-apache.sh), `db/hondaodragon.sql`, `install.bat`, `install.ps1`, `README.md`, `MANIFEST.txt`.

**Chuyển:** copy **cả thư mục** sang VPS mới (hoặc copy file `.tar` rồi `tar -xf nrokura-backup-....tar`).

**Trên VPS mới:** click **`install.bat`** (quyền Administrator). Script tự làm: copy game + web → vá `httpd.conf`/`httpd-vhosts.conf`/`my.ini` (hardening, `bind-address=127.0.0.1`) → cài service Apache2.4 + MySQL (kèm tự-restart khi crash) → import DB → tạo task `GameServer-DBS` + `acme-renew-nrokura` → cấu hình firewall (80/443 chỉ cho Cloudflare, 14445 mở, 3389 cho RDP) → bật server game.

Tham số: `install.bat -SkipAcme` (bỏ bước cấp cert, chạy lại sau khi trỏ DNS) · `-SkipImportDb` (bỏ import DB).

---

### 3.2. Cách B — dựng tay từ GitHub

```cmd
:: 1. Server game
git clone https://github.com/Theace-vip/Dragonballsaga.git C:\Dragonballsaga
::    -> game nằm ở C:\Dragonballsaga\BeMeoGaming

:: 2. Web (nhánh web chính là nội dung thư mục web)
git clone -b web --depth 1 https://github.com/Theace-vip/Dragonballsaga.git %TEMP%\web-src
robocopy %TEMP%\web-src C:\xampp\htdocs\nrokura /E /XD .git

:: 3. Database
git clone -b database --depth 1 https://github.com/Theace-vip/Dragonballsaga.git %TEMP%\db-src
C:\xampp\mysql\bin\mysql.exe -u root -e "CREATE DATABASE IF NOT EXISTS hondaodragon DEFAULT CHARACTER SET utf8mb4;"
for %f in (%TEMP%\db-src\tables\*.sql) do C:\xampp\mysql\bin\mysql.exe -u root --default-character-set=utf8mb4 hondaodragon < "%f"
```

Sau đó:

1. **Sửa IP trong config game** — `BeMeoGaming\data\config\config.properties`:
   `server.sv1=hondaodragon:<IP VPS MỚI>:14445`
2. **Web**: kiểm tra `C:\xampp\htdocs\nrokura\config.php` (`$db_name = "hondaodragon"`, `$db_host = "localhost"`, `$db_user = "root"`).
3. **Apache**: bật `mod_rewrite`, `mod_ssl`, `mod_headers` trong `httpd.conf`; thêm vhost (nội dung y hệt `configs/httpd-vhosts.conf` trong gói migrate):
   - `:80` → `ServerName nrokura.site`, `DocumentRoot "C:/xampp/htdocs/nrokura"`, chặn `/sql/`, **301 sang HTTPS**
   - `:443` → `SSLCertificateFile "conf/ssl.crt/nrokura.site.crt"`, `SSLCertificateKeyFile "conf/ssl.key/nrokura.site.key"`, `AllowOverride All`
   - kiểm tra: `C:\xampp\apache\bin\httpd.exe -t`
4. **MySQL**: `C:\xampp\mysql\bin\my.ini` → `bind-address="127.0.0.1"`.
5. **Service tự chạy + tự phục hồi**:
   ```cmd
   C:\xampp\apache\bin\httpd.exe -k install
   C:\xampp\mysql\bin\mysqld.exe --install MySQL --defaults-file="c:/xampp/mysql/bin/my.ini"
   sc failure Apache2.4 reset= 86400 actions= restart/5000/restart/15000/restart/60000
   sc failure MySQL     reset= 86400 actions= restart/5000/restart/15000/restart/60000
   ```
6. **HTTPS**: chép `certs\nrokura.site.crt/.key` (nếu có) vào `C:\xampp\apache\conf\ssl.crt|ssl.key\` — cert cũ còn hạn tới **24/12/2026**;
   hoặc cấp mới bằng acme.sh trong Git Bash:
   ```bash
   curl -fsSL https://get.acme.sh | sh
   ~/.acme.sh/acme.sh --set-default-ca --server letsencrypt
   ~/.acme.sh/acme.sh --issue -d nrokura.site -d www.nrokura.site --webroot C:/xampp/htdocs/nrokura
   ~/.acme.sh/acme.sh --install-cert -d nrokura.site \
     --fullchain-file C:/xampp/apache/conf/ssl.crt/nrokura.site.crt \
     --key-file       C:/xampp/apache/conf/ssl.key/nrokura.site.key \
     --reloadcmd "sh C:/xampp/apache/bin/reload-apache.sh"
   ```
7. **Task tự động**:
   ```cmd
   schtasks /Create /TN GameServer-DBS /SC ONSTART /TR "C:\Dragonballsaga\BeMeoGaming\run-watchdog.bat" /RU SYSTEM /F
   schtasks /Create /TN acme-renew-nrokura /SC DAILY /ST 02:30 /TR "C:\xampp\acme-renew.bat" /RU SYSTEM /F
   ```
   (`run-watchdog.bat` và `C:\xampp\acme-renew.bat` xem mẫu trong `BeMeoGaming\run-watchdog.bat` và gói migrate `scripts\`.)
8. **Firewall** (làm **RDP trước**, không thì tự khoá mình ra ngoài):
   ```cmd
   netsh advfirewall firewall add rule name="VPS-RDP-3389" dir=in action=allow protocol=TCP localport=3389
   netsh advfirewall firewall add rule name="14445" dir=in action=allow protocol=TCP localport=14445
   curl -s https://www.cloudflare.com/ips-v4 > %TEMP%\cf4.txt
   curl -s https://www.cloudflare.com/ips-v6 > %TEMP%\cf6.txt
   :: gộp 2 file thành 1 chuỗi phân cách bằng dấu phẩy -> <CHUOI-IP>
   netsh advfirewall firewall add rule name="CF-HTTP-80"   dir=in action=allow protocol=TCP localport=80  remoteip=<CHUOI-IP>
   netsh advfirewall firewall add rule name="CF-HTTPS-443" dir=in action=allow protocol=TCP localport=443 remoteip=<CHUOI-IP>
   netsh advfirewall set allprofiles state on
   ```
9. **DNS / Cloudflare**: DNS → record `A @` → **IP VPS mới** (giữ **Proxied** ✅). Chờ 1–2 phút.
10. **Bật game**: `schtasks /Run /TN GameServer-DBS` (khởi động mất ~60 giây, xem `BeMeoGaming\server_latest.log`).

---

### 3.3. Kiểm tra sau khi chuyển

```cmd
netstat -ano | findstr ":80 :443 :3306 :14445" | findstr LISTENING     :: đủ 4 cổng
curl -k https://localhost/                                            :: web nội bộ trả 200
sc query Apache2.4 & sc query MySQL                                   :: service RUNNING
schtasks /Query /TN GameServer-DBS
```

- `https://nrokura.site` mở được **từ điện thoại 4G** (không phải máy chủ) → chứng tỏ DNS + Cloudflare + firewall đúng.
- Vào game bằng client: nếu client **hardcode IP cũ** thì phải cập nhật file config của client rồi phát lại link tải.
- Đăng ký 1 tài khoản thử trên web → đăng nhập được vào game (chứng tỏ chung DB đúng).

**Nhớ sau cùng:** tắt máy chủ cũ (hoặc gỡ DNS cũ) để **không chạy 2 server cùng lúc**; chạy `C:\xampp\acme-renew.bat` một lần để chắc cert tự gia hạn được.

---

## 4. Log & xử lý nhanh

| Việc | File |
|---|---|
| Game (chính) | `BeMeoGaming\server_latest.log` |
| Game bị kill / tự bật lại | `BeMeoGaming\watchdog.log` |
| Apache lỗi | `C:\xampp\apache\logs\nrokura_error.log` · `nrokura_ssl_error.log` |
| Web truy cập | `C:\xampp\apache\logs\nrokura_ssl_access.log` |
| Gia hạn cert | `C:\xampp\acme-renew.log` |
| Khi cài migrate | `install.log` (trong thư mục gói) |

| Triệu chứng | Làm ngay |
|---|---|
| Web không mở | `net start Apache2.4` → xem `nrokura_error.log` |
| Game không vào được | `schtasks /Run /TN GameServer-DBS` → xem `watchdog.log` |
| MySQL không chạy | `net start MySQL` |
| Sửa vhost mà không ăn | `httpd.exe -t` (test cú pháp) → `net stop Apache2.4` + `net start Apache2.4` |
| Bị flood / tấn công | Cloudflare → Security → **Under Attack Mode** |

> Đừng bấm Start/Stop trên **XAMPP Control Panel** — nó đụng vào service đang chạy.
> Log tiếng Việt bị lỗi font trên console → mở bằng **Notepad++**.

---

## 5. Ghi chú bảo mật (nên xử lý)

1. Repo **public** → nên chuyển **private**; đổi lại **Turnstile secret** vì `CF_SECRET_KEY` từng công khai.
2. MySQL `root` **chưa có mật khẩu** — đã chặn ngoài (`bind-address=127.0.0.1`) nhưng nên đặt mật khẩu;
   đặt xong phải sửa lại `BeMeoGaming\data\config\config.properties` **và** `htdocs\nrokura\config.php`.
3. `CF_ENABLED` trong `config.php` đang `false` — **đừng bật `true`** cho tới khi widget Turnstile cấu hình đúng domain (hiện lỗi `110200`).
4. phpMyAdmin chỉ vào được từ **chính máy chủ** (`http://localhost/phpmyadmin`), từ Internet là 403 — giữ nguyên như vậy.
5. Mật khẩu gửi dạng plaintext trong packet game (14445) — chỉ sửa được khi có source client.
