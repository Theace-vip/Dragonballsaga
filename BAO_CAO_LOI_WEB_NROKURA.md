# Báo cáo lỗi web NROKuRa (https://nrokura.site)

Ngày kiểm: 10/10/2026 · Người kiểm: Buffy
Cách kiểm: mở thật từng trang bằng trình duyệt (đọc DOM + console + network + ảnh chụp), đối chiếu source đang chạy tại `C:\xampp\htdocs\nrokura`.

> TRẠNG THÁI: **CHƯA SỬA gì trên web**. Tài liệu này chỉ mô tả lỗi + cách sửa.

---

## 1. Tóm tắt

| Trang | Kết quả kiểm |
|---|---|
| `/` (trang chủ) | OK — mobile 390px không tràn ngang (`scrollWidth = 390`) |
| `/login` | OK — form POST `username,password`, 0 lỗi console |
| `/register` | ❌ Khung captcha bị lỗi (mục 3) |
| `/dua-top` | OK — 20 bảng xếp hạng, mọi asset 200 |
| `/chucnang` | OK |
| `/huongdan` | OK |
| `/naptien` (chưa đăng nhập) | OK — tự chuyển sang `/login` (đúng) |
| `/mocnap` | ❌ **Trang trắng**, vẫn trả HTTP 200 (mục 2) |
| `/lenhios` | ❌ **404** (mục 4) |
| `/check-bank.php` | 401 khi chưa đăng nhập — đúng thiết kế (endpoint nội bộ) |

Mọi tài nguyên tĩnh (css/js/ảnh/font) đều trả 200, không có ảnh vỡ trên các trang đã kiểm.

---

## 2. `/mocnap` — trang trắng, lỗi fatal PHP (đáng sửa nhất)

**Triệu chứng:** mở `https://nrokura.site/mocnap` chỉ thấy phần đầu trang + menu, phần nội dung trắng hoàn toàn. HTTP vẫn **200** nên không giống 404, rất dễ bị bỏ qua.

**Ai bị ảnh hưởng:** bấm vào các link này đều dẫn tới trang trắng:
- thông báo "Thông Tin Mốc Nạp NROKuRa" ở trang chủ — `index.php:113`
- `chucnang.php:46` và `chucnang.php:57`

**Nguyên nhân (đã tái hiện, không phải suy đoán):**
```
C:\xampp\htdocs\nrokura\cpanel> php moc-nap.php
PHP Warning:  require(../tuanbinh.php): Failed to open stream: No such file or directory
              in C:\xampp\htdocs\nrokura\cpanel\moc-nap.php on line 6
PHP Fatal error:  Uncaught Error: Failed opening required '../tuanbinh.php'
              (include_path='C:\xampp\php\PEAR') in ...\cpanel\moc-nap.php:6
```
File thật sự tồn tại tên là **`AnhTuan.php`** (nằm ngay `C:\xampp\htdocs\nrokura\AnhTuan.php`) và bên trong đúng là các biến trang này cần: `$soLuongMocNap`, `$phanThuong`, `$duongDanAnh`. Trang chết ngay sau khi in `head.php`/`nav.php` nên body trắng.

**Cách sửa (1 dòng):** trong `C:\xampp\htdocs\nrokura\cpanel\moc-nap.php`, dòng 6:
```php
// trước
require('../tuanbinh.php');
// sau
require('../AnhTuan.php');
```

**Cách kiểm tra sau khi sửa:**
1. `php -l C:\xampp\htdocs\nrokura\cpanel\moc-nap.php` → `No syntax errors detected`
2. Mở lại `/mocnap` → phải thấy tiêu đề **"DANH SÁCH CÁC MỐC NẠP"** và 5 mốc, ảnh quà `quylao, karin, bunma, hit, xayda` hiển thị (5 ảnh này có thật trong `assets/images/char/`).
3. Xoá cache Cloudflare cho URL `/mocnap` nếu vẫn thấy bản cũ.

**Lưu ý kèm theo:** `AnhTuan.php` đặt `$soLuongMocNap = 5` nhưng mảng `$phanThuong` có **10 mốc**, và các mốc 6–10 dùng ảnh `qua5` **không tồn tại** trong `assets/images/char/` (thư mục chỉ có: `8, bunma, hit, karin, namec, quylao, rose, traidat, xayda`). Nếu sau này muốn hiện đủ 10 mốc thì phải thêm `qua5.png` trước, nếu không sẽ vỡ ảnh.

---

## 3. Captcha Turnstile hỏng ở `/register` (rủi ro cao)

**Triệu chứng nhìn thấy được:** ngay dưới ô "Mật khẩu..." trên trang Đăng Ký có một khung captcha bị lỗi: hộp xám ghi *"Unable to connect to website — Troubleshoot"*.

**Bằng chứng từ console:** `TurnstileError: [Cloudflare Turnstile] Error: 110200` (lặp lại liên tục khi ở trang đăng ký).

**Nguyên nhân:** theo tài liệu Cloudflare Turnstile, mã **110200 = "Domain not authorized"** — widget `0x4AAAAAABeVdoxrFe0VuhgH` (khai báo ở `config.php`, `CF_SITE_KEY`) chưa được thêm hostname `nrokura.site` trong Hostname Management.

**Vì sao hiện tại chưa gây chết đăng ký:** `config.php` có `CF_ENABLED = false`, và `register.php:38` (login cũng vậy: `login.php:38`) chỉ kiểm tra captcha khi cờ này bật:
```php
if (CF_ENABLED && !verifyCaptcha($captchaResponse)) { ... "Captcha không hợp lệ!" ... }
```

**⚠️ Cảnh báo quan trọng:** **KHÔNG được bật `CF_ENABLED = true` lúc này.** Vì widget không sinh được token, mọi lượt đăng ký sẽ bị chặn với thông báo "Captcha không hợp lệ!" → khoá đăng ký của toàn bộ người chơi mới.

**Cách sửa (làm trên Cloudflare, không phải trong code):**
1. Cloudflare Dashboard → **Turnstile** → mở widget có Site Key `0x4AAAAAABeVdoxrFe0VuhgH`.
2. **Hostname Management** → thêm `nrokura.site` và `www.nrokura.site` (hoặc chọn chế độ cho phép mọi hostname nếu chấp nhận rủi ro).
3. Chờ vài phút, tải lại `/register` → khung captcha phải hiện bình thường (ô tick/nền sáng), console **hết** lỗi 110200.
4. Chỉ sau khi (3) đạt → mới đổi `CF_ENABLED` thành `true` trong `config.php`, rồi thử đăng ký 1 tài khoản test (đúng/sai captcha) để chắc chắn chặn được bot mà không chặn người thật.
5. Nếu không dùng lại widget cũ được thì tạo widget mới rồi thay cả `CF_SITE_KEY` **và** `CF_SECRET_KEY` trong `config.php` (nhớ: secret key là thông tin nhạy cảm, không đưa vào ảnh chụp/công khai).

**Gợi ý gia cố thêm (tùy chọn):** `verifyCaptcha()` trong `register.php` dùng `file_get_contents()` không timeout — nếu Cloudflare chậm/chặn thì hàm này treo. Nên thêm timeout (curl/Curl) và ghi log khi verify thất bại để biết là do token sai hay do không gọi được API.

---

## 4. `/lenhios` — nút "Lệnh Chat" trong menu bị 404

**Triệu chứng:** `GET https://nrokura.site/lenhios → 404`.

**Vị trí nút:** `views/layout/nav.php:177` và `views/layout/nav.php:222` đều có `<a href="/lenhios">Lệnh Chat</a>`.

**Nguyên nhân:** không có file `lenhios.php` và `.htaccess` cũng không có rewrite cho route này (chỉ có: `login`, `register`, `naptien`, `logout`, `change-password`, `napcard`, `huongdan`, `home`, `dua-top`, `top-*`, `chucnang`, `power`, `task`, `money`, `mocnap`).

**Cách sửa — chọn 1:**
- (a) Làm thật: tạo `C:\xampp\htdocs\nrokura\lenhios.php` (nội dung danh sách lệnh chat trong game) rồi thêm vào `.htaccess`:
  ```apache
  RewriteRule ^lenhios$ /lenhios.php [L]
  ```
- (b) Tạm: đổi 2 link trong `nav.php` sang trang đã có nội dung (ví dụ `/huongdan`), hoặc bỏ nút nếu chưa có nội dung.
- Sau khi sửa: xoá cache Cloudflare cho `/lenhios`, mở lại kiểm tra không còn 404.

---

## 5. Các điểm nhỏ

1. **2 link chết (href rỗng, bấm không có gì xảy ra):**
   - nút **"TikTok"** trong popup cảnh báo ở trang chủ
   - link **"điều khoản dịch vụ"** ở footer
   → điền URL thật hoặc xoá phần tử.
2. **`views/layout/head.php:21`** nạp `https://www.google.com/recaptcha/api.js` cho **mọi trang** nhưng web dùng Turnstile, không có phần tử `.g-recaptcha` nào (đã kiểm: 0 widget) → mỗi lượt tải trang tốn 1 request ngoài vô ích, nên xoá.
3. **`login.php:105`** đã comment thẻ captcha nhưng `login.php:110` vẫn nạp `turnstile/v0/api.js` → hoặc xoá script, hoặc mở lại widget sau khi sửa xong mục 3 để trang đăng nhập cũng được bảo vệ.
4. **`AnhTuan.php`**: `$soLuongMocNap = 5` nhưng có 10 mốc trong `$phanThuong`; thiếu `assets/images/char/qua5.png` (xem cuối mục 2).
5. `assets/css/tuanbinhcopy.css` là **file CSS có thật** (200) — đừng nhầm với `tuanbinh.php` đang thiếu ở mục 2.

---

## 6. Bằng chứng / cách tái hiện nhanh

```bash
# 1) Lỗi trang Mốc Nạp (fatal PHP)
C:\xampp\php\php.exe C:\xampp\htdocs\nrokura\cpanel\moc-nap.php
#   -> PHP Fatal error: Failed opening required '../tuanbinh.php' ... on line 6

# 2) Danh sách route đang có (để thấy thiếu lenhios)
type C:\xampp\htdocs\nrokura\.htaccess | findstr RewriteRule

# 3) Kiểm tra nhanh HTTP của các trang
#    (mở trong trình duyệt) /  /login /register /dua-top /chucnang /huongdan /naptien /mocnap /lenhios
#    -> /lenhios = 404, /mocnap = 200 nhưng body trắng
```

Trên trình duyệt: mở DevTools → Console ở `/register` sẽ thấy `TurnstileError ... 110200`; ở `/mocnap` sẽ thấy body chỉ còn header.
