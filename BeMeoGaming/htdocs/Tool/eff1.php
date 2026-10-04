<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Image Selection with Pixel Grid</title>
    <style>
        body {
            display: flex;
        }

        #imageContainer {
            flex: 1;
            margin-right: 20px;
        }

        #infoContainer {
            flex: 1;
        }

        canvas {
            border: 1px solid #000;
            max-width: 100%;
        }

        #info {
            margin-top: 10px;
        }

        #frameList {
            margin-top: 10px;
        }

        #frameList ul {
            list-style-type: none;
            padding: 0;
            margin: 0;
        }

        #frameList li {
            margin-bottom: 5px;
        }
    </style>
</head>

<body>
    <h1>Image Selection with Pixel Grid</h1>

    <div id="imageContainer">
        <!-- Form để chọn và hiển thị ảnh ngay lập tức -->
        <form id="uploadForm" enctype="multipart/form-data">
            <input type="file" id="imageInput" accept="image/*" required>
        </form>

        <div id="imagePreview">
            <canvas id="imageCanvas"></canvas>
        </div>
    </div>

    <div id="infoContainer">
        <div id="info"></div>
        <div id="frameList">
            <h2>Frame List</h2>
            <ul id="frameUl"></ul>
            <button onclick="addFrame()">Add Frame</button>
        </div>
        <div>
            <label for="zoomInput">Zoom:</label>
            <input type="range" id="zoomInput" min="0.1" max="3" step="0.1" value="1" onchange="updateZoom()">
        </div>
    </div>

    <script>
        var frameList = [];
        var startX, startY, endX, endY;
        var updatePixelThreshold = 5; // Giảm số lượng pixel để cập nhật
        var updateCounter = 0;
        var zoomLevel = 1;

        document.getElementById('imageInput').addEventListener('change', function (event) {
            var input = event.target;

            if (input.files && input.files[0]) {
                var reader = new FileReader();

                reader.onload = function (e) {
                    var image = new Image();
                    image.src = e.target.result;

                    image.onload = function () {
                        var imageCanvas = document.getElementById('imageCanvas');
                        var ctx = imageCanvas.getContext('2d');
                        var hiddenCanvas = document.createElement('canvas');
                        hiddenCanvas.width = image.width;
                        hiddenCanvas.height = image.height;
                        var hiddenCtx = hiddenCanvas.getContext('2d');

                        // Hiển thị ảnh trong hidden canvas
                        hiddenCtx.drawImage(image, 0, 0);

                        // Thiết lập kích thước canvas bằng kích thước ảnh
                        imageCanvas.width = image.width * zoomLevel;
                        imageCanvas.height = image.height * zoomLevel;

                        // Vẽ ảnh từ hidden canvas lên image canvas
                        ctx.drawImage(hiddenCanvas, 0, 0, imageCanvas.width, imageCanvas.height);

                        // Vẽ lưới pixel
                        drawPixelGrid(ctx, imageCanvas.width, imageCanvas.height);

                        // Hiển thị danh sách khung hình đã lưu
                        showSavedFrames();

                        // Hiển thị khung vùng chọn khi di chuyển chuột
                        var isDrawing = false;

                        // Bắt đầu vẽ khi chuột được nhấn
                        imageCanvas.addEventListener('mousedown', function (e) {
                            isDrawing = true;
                            var rect = imageCanvas.getBoundingClientRect();
                            startX = (e.clientX - rect.left) / zoomLevel;
                            startY = (e.clientY - rect.top) / zoomLevel;
                        });

                        // Kết thúc vẽ khi chuột được nhả
                        imageCanvas.addEventListener('mouseup', function (e) {
                            isDrawing = false;
                            var rect = imageCanvas.getBoundingClientRect();
                            endX = (e.clientX - rect.left) / zoomLevel;
                            endY = (e.clientY - rect.top) / zoomLevel;

                            // Vẽ hình chữ nhật
                            ctx.strokeStyle = '#000';
                            ctx.setLineDash([1, 2]); // Thiết lập mẫu đường đứt nhỏ
                            ctx.lineWidth = 0.5;      // Đặt độ rộng đường về 0.5 để có đường đậm hơn
                            ctx.strokeRect(startX, startY, endX - startX, endY - startY);

                            // Hiển thị thông tin
                            showInfo(startX, startY, endX, endY);
                        });

                        // Vẽ khi di chuyển chuột
                        imageCanvas.addEventListener('mousemove', function (e) {
                            if (!isDrawing) return;

                            var rect = imageCanvas.getBoundingClientRect();
                            endX = (e.clientX - rect.left) / zoomLevel;
                            endY = (e.clientY - rect.top) / zoomLevel;

                            updateCounter++;

                            if (updateCounter >= updatePixelThreshold) {
                                // Xóa khung vùng chọn trước đó
                                ctx.clearRect(0, 0, imageCanvas.width, imageCanvas.height);

                                // Vẽ lại ảnh từ hidden canvas
                                ctx.drawImage(hiddenCanvas, 0, 0, imageCanvas.width, imageCanvas.height);

                                // Vẽ lưới pixel
                                drawPixelGrid(ctx, imageCanvas.width, imageCanvas.height);

                                // Hiển thị các khung hình đã lưu
                                showSavedFrames();

                                // Vẽ khung vùng chọn mới
                                ctx.strokeStyle = '#000';
                                ctx.setLineDash([1, 2]); // Thiết lập mẫu đường đứt nhỏ
                                ctx.lineWidth = 0.5;      // Đặt độ rộng đường về 0.5 để có đường đậm hơn
                                ctx.strokeRect(startX, startY, endX - startX, endY - startY);

                                // Hiển thị thông tin
                                showInfo(startX, startY, endX, endY);

                                updateCounter = 0; // Đặt lại counter
                            }
                        });
                    };
                };

                // Đọc dữ liệu ảnh
                reader.readAsDataURL(input.files[0]);
            }
        });

        function drawPixelGrid(ctx, width, height) {
            ctx.strokeStyle = '#ccc';
            ctx.lineWidth = 0.5;
            
            // Vẽ lưới pixel
            for (var x = 0; x < width; x += 10) {
                ctx.beginPath();
                ctx.moveTo(x, 0);
                ctx.lineTo(x, height);
                ctx.stroke();
            }

            for (var y = 0; y < height; y += 10) {
                ctx.beginPath();
                ctx.moveTo(0, y);
                ctx.lineTo(width, y);
                ctx.stroke();
            }
        }

        function showInfo(startX, startY, endX, endY) {
            // Làm tròn giá trị x, y, w và h
            var roundedX = Math.round(startX);
            var roundedY = Math.round(startY);
            var roundedWidth = Math.round(Math.abs(endX - startX));
            var roundedHeight = Math.round(Math.abs(endY - startY));

            // Tạo đối tượng JSON chứa thông tin
            var coordinates = {
                x: roundedX,
                y: roundedY,
                w: roundedWidth,
                h: roundedHeight
            };

            // Chuyển đối tượng JSON thành chuỗi
            var coordinatesString = JSON.stringify(coordinates);

            // Hiển thị thông tin toạ độ và kích thước
            var infoDiv = document.getElementById('info');
            infoDiv.innerHTML = coordinatesString;
        }

        function showSavedFrames() {
            // Hiển thị danh sách các frame đã lưu
            var frameUl = document.getElementById('frameUl');
            frameUl.innerHTML = '';

            frameList.forEach(function (frame) {
                var frameLi = document.createElement('li');
                frameLi.textContent = JSON.stringify(frame);
                frameUl.appendChild(frameLi);

                var imageCanvas = document.getElementById('imageCanvas');
                var ctx = imageCanvas.getContext('2d');
                ctx.strokeStyle = '#000';
                ctx.setLineDash([1, 2]); // Thiết lập mẫu đường đứt nhỏ
                ctx.lineWidth = 0.5;      // Đặt độ rộng đường về 0.5 để có đường đậm hơn
                ctx.strokeRect(frame.x * zoomLevel, frame.y * zoomLevel, frame.w * zoomLevel, frame.h * zoomLevel);
            });
        }

        function addFrame() {
            // Lưu frame mới vào danh sách
            frameList.push({
                x: Math.round(startX),
                y: Math.round(startY),
                w: Math.round(Math.abs(endX - startX)),
                h: Math.round(Math.abs(endY - startY)),
                id: frameList.length + 1 // Đặt ID là số thứ tự
            });

            // Hiển thị lại danh sách frame
            showSavedFrames();
        }

        function updateZoom() {
            var zoomInput = document.getElementById('zoomInput');
            zoomLevel = parseFloat(zoomInput.value);

            // Cập nhật kích thước canvas và vẽ lại ảnh
            var imageCanvas = document.getElementById('imageCanvas');
            var ctx = imageCanvas.getContext('2d');
            imageCanvas.width = imageCanvas.width * zoomLevel;
            imageCanvas.height = imageCanvas.height * zoomLevel;
            ctx.drawImage(hiddenCanvas, 0, 0, imageCanvas.width, imageCanvas.height);

            // Vẽ lại lưới pixel
            drawPixelGrid(ctx, imageCanvas.width, imageCanvas.height);

            // Vẽ lại các khung hình đã lưu
            showSavedFrames();
        }
    </script>
</body>

</html>
