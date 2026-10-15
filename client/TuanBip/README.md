# TuanBip — Unity client

Project client game Unity nằm trong thư mục này, tách riêng với server Java/PHP ở repo gốc.

## Phiên bản và cấu trúc

- **Unity Editor:** `2022.3.62f3` (LTS). Nên cài đúng phiên bản này trong Unity Hub để tránh Unity tự nâng cấp project.
- **Tên app:** NroKuRa; version hiện tại `2.3.7`.
- **Scene build:** `Assets/NROL.unity`, `Assets/NRO2.unity`, `Assets/NRO3.unity` đang được bật trong `ProjectSettings/EditorBuildSettings.asset`. `Assets/Scenes/SampleScene.unity` đang tắt.
- **Package:** danh sách package nằm trong `Packages/manifest.json`; phiên bản resolved nằm trong `Packages/packages-lock.json`.
- **Mã nguồn:** `Assets/Scripts/Game1`, `Game2`, `Game3`, cùng `Assets/Resources`, `Assets/Plugins` và `Assets/OPS`.

## Chuẩn bị môi trường

1. Cài **Unity Hub** và Unity Editor phiên bản **2022.3.62f3**.
2. Khi cài Editor, chọn module theo nền tảng cần build:
   - Android: **Android Build Support**, gồm **Android SDK & NDK Tools** và **OpenJDK**.
   - Windows: **Windows Build Support (IL2CPP)** nếu cần build Windows bằng IL2CPP; project hiện lưu backend Android/Standalone là IL2CPP.
3. Clone nhánh chứa client:

   ```bash
   git clone --branch client/tuanbip-unity --single-branch https://github.com/Theace-vip/Dragonballsaga.git
   ```

   Mở Unity Hub → **Add project from disk** → chọn thư mục `client/TuanBip`.
4. Chờ Unity import assets và resolve package lần đầu. Không chép `Library`, `Temp`, `Logs`, `Obj` từ máy khác; Unity tự tạo lại.
5. Nếu Unity báo package không resolve, kiểm tra Internet/Unity Package Manager rồi mở lại project; không tự nâng phiên bản package hoặc Editor nếu chưa kiểm thử.

## Build Android APK/AAB

1. Mở project bằng Unity `2022.3.62f3`.
2. Vào **File → Build Settings** (hoặc **Build Profiles**, tùy giao diện Editor) → chọn **Android** → **Switch Platform** nếu cần.
3. Kiểm tra danh sách scene gồm `NROL`, `NRO2`, `NRO3` và các scene cần phát hành đều được bật.
4. Trong **Player Settings → Android**, kiểm tra package identifier, version, min SDK và kiến trúc. Giá trị hiện tại gồm:
   - Application ID: `com.TheGioiNroo.TheGioiNroo`
   - Version: `2.3.7`
   - Minimum SDK: API 23
   - ARMv7 và ARM64 đang được chọn.
5. Tạo APK bằng **Build**; bật **Build App Bundle** nếu mục tiêu là AAB. Lưu file đầu ra ngoài source, ví dụ `Builds/Android/`.
6. Muốn phát hành có ký số: cấu hình **keystore/alias** trong Player Settings hoặc Publishing Settings theo quy trình bảo mật của đội. Repo không kèm keystore; không commit file keystore, mật khẩu hay alias secret. `ProjectSettings.asset` hiện để trống tên keystore/alias.
7. Cài APK lên thiết bị Android thật và thử mở game, kết nối server, tải resource, đăng nhập trước khi phân phối.

## Build Windows

Trong **File → Build Settings/Build Profiles**, chọn **Windows**, xác nhận scene và kiến trúc, rồi Build ra thư mục ngoài source, ví dụ `Builds/Windows/`. Kiểm tra backend/target module đã cài trong Unity Hub. Repo chưa có build Windows được cam kết sẵn.

## Đổi địa chỉ server trước khi build client mới

Client có nhiều biến thể `Game1`, `Game2`, `Game3`. Trước khi phát hành, kiểm tra/cập nhật đồng bộ:

- `Assets/Scripts/Game1/GameMidlet.cs`, `Game2/GameMidlet.cs`, `Game3/GameMidlet.cs`: IP dự phòng và port.
- `Assets/Scripts/Game1/ServerListScreen.cs`, `Game2/ServerListScreen.cs`, `Game3/ServerListScreen.cs`: server list mặc định (`smartPhoneVN`/`javaVN`) và endpoint `linkGetHost` nếu endpoint còn được sử dụng.

Port game hiện cấu hình là **14445**. Đổi cả ba biến thể để tránh build tab/server khác nhau còn trỏ về host cũ. Địa chỉ hardcode đang là thông tin môi trường hiện có, không đảm bảo còn đúng sau khi chuyển VPS. Không đưa IP mới vào trước khi được xác nhận; ưu tiên dùng hostname ổn định nếu cấu hình DNS/game server hỗ trợ.

Sau khi đổi, tìm lại toàn bộ project để chắc không còn IP/domain cũ trong code và cấu hình liên quan; build rồi kiểm tra kết nối từ mạng ngoài. `linkGetHost` là URL HTTP trong code cũ: kiểm tra endpoint tồn tại và nội dung nó trả về trước khi phát hành; chỉ thay hostname không đảm bảo endpoint vẫn chạy.

## Obfuscation và lưu ý build

- Project có cấu hình obfuscation trong `Assets/Editor/Beebyte/Obfuscator/ObfuscatorOptions.asset` và `Assets/OPS/Obfuscator/Settings/Obfuscator_Settings.json`.
- Các hook obfuscation có thể chạy trong quá trình build. Hãy kiểm thử build Development trước; nếu lỗi, xem Console/Editor log và chẩn đoán plugin, không tắt obfuscation cho bản release tùy tiện.
- Chưa có Unity Editor CLI/build pipeline được cấu hình trong repo; hướng dẫn này dùng Unity Editor GUI. Máy chạy tác vụ này không có Unity Editor trong PATH, vì vậy chưa xác nhận build APK/Windows.

## Git và file không đưa lên repo

Theo dõi các thư mục Unity cần thiết: `Assets/`, `Packages/`, `ProjectSettings/`, cùng README này. Không commit cache, log, IDE state, output build, file tạm/response dùng để debug, hoặc signing keys. Nếu cần thay đổi asset, giữ file `.meta` cạnh asset để GUID và reference trong scene không bị mất.
