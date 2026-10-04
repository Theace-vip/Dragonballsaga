# TODO: Web nrokura.site - HTTPS + bảo mật (hoàn tất 25/09/2026)

Tên miền thật: **`nrokura.site`** (NS `ns61/ns62.domaincontrol.com`, A -> `160.191.242.240` - IP public thẳng, không NAT).
Lưu ý cũ về `nrokura.com` + Cloudflare tunnel **đã bỏ** - không cần tunnel nữa vì 80/443 mở ngoài trực tiếp.

## Đã làm xong

### Web (đã có từ trước)
- Web copy sang `C:\xampp\htdocs\nrokura`, `config.php` -> DB `hondaodragon` (chung với game), cờ `CF_ENABLED` (đang `false`).
- `register.php`/`login.php`: prepared statement, validate giống game, test pass (register, login, chặn SQL injection).
- DB: bảng `history_bank`, `napthe` giữ nguyên.
- Pretty URL qua `.htaccess` (AllowOverride All, mod_rewrite bật): `/register`, `/login`, `/naptien`, `/power`...

### HTTPS / chứng chỉ (25/09/2026)
- **acme.sh** (`C:\Users\Administrator\.acme.sh`, v3.1.6, CA mặc định = Let's Encrypt) cấp cert **EC-256** cho
  `nrokura.site` + `www.nrokura.site`:hiệu lực 25/09/2026 -> **24/12/2026**, ARI renewal 24/11/2026.
  - Store: `C:\Users\Administrator\.acme.sh\nrokura.site_ecc\`
  - Webroot: `C:/xampp/htdocs/nrokura` (HTTP-01, file token ở `.well-known/acme-challenge/`).
- Cài vào Apache: `C:\xampp\apache\conf\ssl.crt\nrokura.site.crt` (fullchain) + `C:\xampp\apache\conf\ssl.key\nrokura.site.key`.
- `extra/httpd-vhosts.conf` (backup `httpd-vhosts.conf.bak-20260925`):
  - vhost :80 -> 301 sang HTTPS (trừ `/.well-known/acme-challenge/`), chặn `/sql/`.
  - vhost :443 -> TLS1.2/1.3, cipher ECDHE-GCM/CHACHA20, HSTS 1 năm, X-Content-Type-Options,
    X-Frame-Options, Referrer-Policy, Permissions-Policy, bỏ `Server`/`X-Powered-By` version.
- **Tự động gia hạn**: `C:\xampp\acme-renew.bat` + Scheduled Task **`acme-renew-nrokura`**
  (SYSTEM, hàng ngày 02:30, log `C:\xampp\acme-renew.log`; acme.sh tự skip khi chưa tới hạn).
  reloadcmd = `sh C:/xampp/apache/bin/reload-apache.sh` (ưu tiên `httpd -k restart` theo service).
  - Đã chạy thử: task OK, acme.sh báo "Next renewal time is: 2026-11-24".

### Bảo mật hệ thống
- **MySQL bị mở ra internet với root không mật khẩu -> đã đóng**: `my.ini` bật `bind-address="127.0.0.1"`
  (backup `my.ini.bak-20260925`); ngoài vào 3306 giờ "Connection refused" (kiểm tra từ ES/IR/UK).
  Game (JDBC `localhost`) và PHP (`localhost`) đều kết nối lại bình thường.
- Apache: `ServerTokens Prod`, `ServerSignature Off`, `TraceEnable Off` -> header chỉ còn `Server: Apache`.
- PHP: `expose_php=Off`, `display_errors=Off`, `session.cookie_httponly=1`, `session.cookie_samesite=Lax`.

### Windows Service (25/09/2026)
- `Apache2.4` (`httpd -k install`) và `MySQL` (`mysqld --install MySQL --defaults-file=c:/xampp/mysql/bin/my.ini`)
  -> cả 2 **AUTO_START**, sống lại khi reboot. Dùng `net start/stop Apache2.4|MySQL` hoặc services.msc,
  **không** bấm Start/Stop trên XAMPP Control (sẽ đụng service chạy sẵn).
- Verify: 80/443/3306 listening, HTTPS 200 từ nhiều nước (check-host), game server 14445 không lỗi DB.

## Việc còn lại / gợi ý
1. Bật `CF_ENABLED = true` trong `config.php` + thay key Turnstile của riêng mình (key hiện tại của người khác).
2. Thay key Doithe1s + số tài khoản ngân hàng trong `config.php` nếu dùng nạp thẻ.
3. **Đặt mật khẩu MySQL cho root** (hiện vẫn trống - giờ đã chặn ngoài nên an toàn hơn, nhưng nên đặt).
4. Cài service cho **game server** (`server.ServerManager`, port 14445) để sống lại khi reboot.
5. Test public thật: `https://nrokura.site/register` -> đăng ký -> vào game login.

## Ghi chú kỹ thuật
- IP public `160.191.242.240` (không NAT), 80/443 mở ngoài -> LE HTTP-01 trực tiếp, KHÔNG cần Cloudflare tunnel.
- Apache/MySQL giờ là service; restart bằng `httpd -k restart` / `net restart MySQL`.
- Server game: `server.ServerManager`, port 14445; deploy = build `/tmp/deploy` -> `build/classes` -> kill java -> Start-Process.
- Cert check nhanh: `openssl s_client -connect 127.0.0.1:443 -servername nrokura.site`

## Bảo mật nâng cao (26/09/2026)

### Firewall Windows (lỗ hổng nghiêm trọng đã đóng)
- Trước: **Private/Public profile OFF** → SMB 445, RDP 3389, WinRM 5985, RPC 135/139, 47001... đều lộ ra internet.
- Đã: tạo rule allow `Nrokura-HTTP-80`, `Nrokura-HTTPS-443` (rule `14445` game và RDP 3389 đã có sẵn),
  rồi `netsh advfirewall set allprofiles state on` (BlockInbound).
- Verify ngoài: 80/443/14445 OK; 445/5985/135 **Connection refused/timed out**.
- Thao tác: `netsh advfirewall firewall add rule name=... dir=in action=allow protocol=TCP localport=<port>`

### Chống flood cổng game 14445 (code server)
- `ServerManager.canConnectWithIp` trước luôn `return true` (bị comment) → giờ bật 3 lớp:
  1. global cap: `MAX_PLAYER + 1000` session;
  2. tối đa `Manager.MAX_PER_IP` (mặc định 10) kết nối đồng thời/IP (dem tu SessionManager);
  3. rate limit 15 kết nối/10s/IP (map tự hết hạn, không rò rỉ bộ nhớ), log throttle 10s.
- Test flooder 40 kết nối → log `Tu choi ket noi: ... toi da 15/10 giay`, server 0 lỗi.
- Chỉnh `MAX_PER_IP` ngay trên Panel (mục MAX_PLAYER/MAX_PER_IP).

### Mật khẩu băm PBKDF2-SHA256 (trước là plaintext trong DB)
- Định dạng (cùng nhau giữa game & web): `pbkdf2-sha256$100000$<salt_hex16>$<hash_hex32>` (118 ký tự).
- Java: `utils/PasswordUtil.java` + sửa `NDVSqlFetcher.login` (kiểm tra hash, **tự băm lại tài khoản cũ
  ngay lần đăng nhập đầu**), `Service.registerAccount`, `Service.changePassword`, `PanelService` (đặt MK từ panel).
- PHP: hàm `pw_hash/pw_verify/pw_check` trong `config.php`; sửa `register.php`, `login.php`, `change-password.php`.
- DB: `ALTER TABLE account MODIFY password VARCHAR(255) NOT NULL` (trước varchar(100) không đủ).
- Test: đăng ký web→lưu hash 118 ký tự; login web đúng/sai; Java verify hash do PHP tạo và ngược lại;
  2 player thật (truonggiang1, hac2906) tự migrate khi login game.
- **Lưu ý**: password vẫn truyền plaintext trong packet game 14445 (client gửi `readUTF`) → chỉ sửa được
  khi có source client để đổi sang hash/TLS. Wire-level xem lại khi nào có client.

### Apache chống slowloris/scan
- Bật `mod_reqtimeout` (httpd.conf), `Timeout 300→30`, `KeepAliveTimeout 5→3`, `ServerSignature Off`.
- Vhost 80+443: chặn path scan (`.env/.git/wp-admin/phpmyadmin/...`) → 403, UA sqlmap/nikto... → 403,
  `LimitRequestBody 10MB`. Đường `/.well-known/acme-challenge/` vẫn OK (test 404, không 403).

### Watchdog server game (java hay bị kill khi tool restart)
- Task **`GameServer-DBS`** (SYSTEM, ONSTART) chạy `BeMeoGaming/run-watchdog.bat`: java chết → 10s khởi động lại.
- Deploy giờ chỉ cần: copy `build/classes` → kill java → watchdog tự lên lại (khớp `schtasks /Query`).

### Việc cần làm tay (user)
1. **Cloudflare free cho web** (đã chọn): đăng ký dash.cloudflare.com → Add site `nrokura.site` → Free plan
   → bật proxied cho A `@` và CNAME `www` → copy 2 nameserver CF → vào GoDaddy đổi NS → báo lại để verify.
   Sau đó: SSL/TLS = Full (strict), Always Use HTTPS ON, WAF Managed Rules ON, khi bị đánh chọn Under Attack mode.
   Lưu ý: game 14445 vẫn đi IP trực tiếp → DDoS L4 vào IP thì CF không che được (cần tách IP cho game).
2. Đặt mật khẩu MySQL root (giờ đã chặn cổn ra ngoài nhưng root vẫn trống).

## Cloudflare (hoàn tất 26/09/2026)

- NS zone đã chuyển: `aurora.ns.cloudflare.com` + `yahir.ns.cloudflare.com` (chỉ Cloudflare trả lời DNS).
- Record `@` và `www` đều **Proxied** -> DNS trả IP CF (`104.21.92.46`, `172.67.186.135`), **IP gốc ẩn**.
  (Lưu ý: Google DNS còn cache IP gốc vài phút - không phải lỗi.)
- Site qua CF: `Server: cloudflare`, `CF-RAY` present, HTTPS 200 từ DE/HU/US/..., cert edge = Universal SSL (LE, CF tự gia hạn).
- Kết nối CF -> origin đã mã hóa (log cho thấy https qua CF vào thẳng port 443, port 80 chỉ có request http thường).

### Firewall: chặn mọi truy cập thẳng vào IP gốc (chỉ Cloudflare được vào)
- Đã **xóa** 2 rule mở toàn bộ `Nrokura-HTTP-80`/`Nrokura-HTTPS-443`.
- Thêm `CF-HTTP-80` + `CF-HTTPS-443`: chỉ cho qua 22 dải IP Cloudflare (15 IPv4 + 7 IPv6, nguồn `https://www.cloudflare.com/ips-v4|ips-v6`).
- Verify: vào thẳng `160.191.242.240:80/443` từ ngoài -> **Connection timed out**; qua CF -> 200; localhost -> 200 (loopback không bị lọc).
- Game 14445 KHÔNG đổi (vẫn public, client dùng IP trực tiếp) - 2 player vẫn kết nối bình thường sau khi đổi.
- ACME renewal vẫn OK vì DNS trỏ CF -> LE cũng đi qua CF (test file challenge trả 200 qua cả port 80 lẫn 443).
- **Cập nhật dải IP CF sau này** (CF thỉnh thoảng đổi list):
  ```cmd
  curl -s https://www.cloudflare.com/ips-v4 > cf-v4.txt
  curl -s https://www.cloudflare.com/ips-v6 > cf-v6.txt
  netsh advfirewall firewall delete rule name="CF-HTTP-80"
  netsh advfirewall firewall delete rule name="CF-HTTPS-443"
  netsh advfirewall firewall add rule name="CF-HTTP-80" dir=in action=allow protocol=TCP localport=80 remoteip=<dan,dau,phay>
  netsh advfirewall firewall add rule name="CF-HTTPS-443" dir=in action=allow protocol=TCP localport=443 remoteip=<dan,dau,phay>
  ```

### Cần bấm trên Dashboard Cloudflare (user)
1. SSL/TLS -> Encryption mode = **Full (strict)** (origin có cert LE hợp lệ, hiện đang là Full).
2. SSL/TLS -> Edge Certificates -> **Always Use HTTPS** = ON.
3. Security -> WAF -> **Managed Rules** (free) = ON; **Bot Fight Mode** = ON.
4. Khi bị tấn công: Security -> Overview -> **Enable Under Attack Mode** (chặn bot bằng challenge JS).

### Giới hạn còn lại (biết trước)
- Cổng game **14445 vẫn public** -> DDoS L4 (hết băng thông) vào IP gốc thì CF không che được;
  muốn che thì phải tách game sang IP khác hoặc đặt proxy TCP phía trước.
- IP gốc vẫn lộ qua game port (Shodan quét được) - web thì đã ẩn hẳn.
