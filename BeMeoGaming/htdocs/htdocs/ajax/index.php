<?php
require_once './DHKD/Header.php';
#Duong Huynh Khanh Dang
?>
<div class="layer">
    <div class="loading-container">
        <div class="tank-gif"></div>
    </div>
</div>


<div id="wrapper" class="en wrapper scaleDesktop scaleMobile">
    <section id="block1" class="section block1 scrollFrame" data-block-id="block1">
        <div class="section__background">
            <img src="../Assets/ThangHoa/bg.png" alt="" width="100%" height="100%" class="lazyload pc">
            <style>
            .vdcc {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 20%;
    overflow: hidden;
    z-index: -1; /* Đảm bảo video nằm phía sau nội dung khác */
}

.vdcc video {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover; /* Đảm bảo video bao phủ toàn bộ phần tử mà không bị méo */
}
            </style>
            <div class="vdcc">
            <img src="../Assets/ThangHoa/bg.png" alt="" width="100%" height="100%" class="lazyload mb is-loaded" style="object-fit: cover;">
            </div>
            <!-- <span id="block1-scrollwatch-pin" class="scrollwatch-pin"></span> -->
        </div>
        <div class="section__content">
            <div class="rating">
                <img src="../../img.game/products/metalslug/vng-18.jpg" alt="" class="pc">
                <img src="../../img.game/products/metalslug/m-nph-18.jpg" alt="" class="mb">
            </div>
            <div class="title">
                <img src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block1/images/locate/vn/1.8.png"
                    alt="" class="pc">
                <img src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block1/images/locate/vn/1.8-mb.png"
                    alt="" class="mb">
                <div class="tooltip"></div>
            </div>
            <div class="store-bg pc">
                <img src="../Assets/ThangHoa/game-icon.png" class="game-icon">
                <div class="download-container"><a href="<?= $_Iphone ?>"
                        onclick="dataLayer.push({'event':'DownloadIosVN'})" target="_blank" class="link link-appstore"
                        rel="noopener">APPSTORE</a> <a href="<?= $_Windows ?>"
                        onclick="dataLayer.push({'event':'DownloadGGPlayVN'})" target="_blank"
                        class="link link-googleplay" rel="noopener">GOOGLEPLAY</a> <a href="<?= $_Android ?>"
                        onclick="dataLayer.push({'event':'DownloadApkVN'})" target="_blank" class="link link-apk"
                        rel="noopener">APK</a></div>
                <a href="/nap-the" onclick="dataLayer.push({'event':'TopupVN'})" target="_blank" class="link link-topup"
                    rel="noopener">TOPUP</a> <a href="redeem.html" target="_blank" class="link link-code"
                    rel="noopener">CODE</a>
            </div>
			
			<style>
.popup-overlay {
    display: none; /* Ẩn popup theo mặc định */
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5); /* Nền tối cho lớp phủ */
    justify-content: center;
    align-items: center;
}

.popup-content {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    width: 300px;
    text-align: center;
    position: relative;
	color: #a77f25;
    font-weight: bold; /* Làm cho chữ đậm hơn */
}

.close-btn {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 24px;
    cursor: pointer;
}

.btn-download-popup {
    background-color: #ffcc00; /* Màu nền của nút trong popup */
    border: none;
    padding: 10px 20px;
    color: #fff;
    font-size: 16px;
    text-decoration: none;
    display: inline-block;
    border-radius: 5px;
}
			</style>
			
    <a id="downloadBtn" class="btn-download mb"><span class="arrow-icon"></span>TẢI GAME</a>

    
        </div>
    </section>
	
    <section id="block2" class="section block2 scrollFrame" data-block-id="block2">
        <div class="section__background">
            <img data-src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block2/images/bg.jpg"
                alt="" class="lazyload pc">
            <img data-src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block2/images/bg-mb.jpg"
                alt="" class="lazyload mb">
            <div class="diag-top"></div>
            <div class="pc diag-bot"></div>
            <div class="man"></div>
            <span id="block2-scrollwatch-pin" class="scrollwatch-pin"></span>
        </div>
        <style>
        /* General Styles */
        body {
            background-color: #2d2d2d;
            font-family: 'Roboto', sans-serif;
            color: #e4d6b6;
        }

        .current-top {
            position: absolute;
            bottom: 860px;
            right: 630px;
            background-color: rgba(0, 0, 0, 0.8);
            color: white;
            padding: 10px;
            border-radius: 5px;
            font-size: 16px;
        }

        .table-responsive {
            position: relative;
            margin-bottom: 60px;
        }


        .rank_table {
            width: 100%;
            max-width: 1200px;
            border-collapse: separate;
            border-spacing: 0;
            margin: 20px auto;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
            border-radius: 10px;
            overflow: hidden;
        }

        .rank_table thead {
            background-color: #444;
            color: beige;
            font-family: 'utm-talling', sans-serif;
            font-size: 35px;
            font-weight: 700;
            text-align: left;
        }

        .rank_table thead th {
            padding: 12px 15px;
            border-bottom: 2px solid #e4d6b6;
        }

        .rank_table tbody {
            background-color: #333;
        }

        .rank_table tbody tr {
            border-bottom: 1px solid #444;
            transition: background-color 0.3s ease;
        }

        .rank_table tbody tr:last-child {
            border-bottom: none;
        }

        .rank_table tbody tr:hover {
            background-color: #555;
        }

        .rank_table tbody td {
            padding: 12px 15px;
            font-family: 'Roboto', sans-serif;
            font-size: 20px;
            color: #e4d6b6;
            font-weight: 400;
            line-height: 24px;
        }

        .rank_table tbody td:nth-child(1) {
            text-align: center;
        }

        /* Button Styles */
        .btn-play {
            display: inline-block;
            padding: 10px 20px;
            background-color: #e4d6b6;
            color: #444;
            font-size: 18px;
            font-weight: bold;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        .btn-play:hover {
            background-color: #ccc;
        }

        /* Responsive Design */
        @media screen and (max-width: 768px) {
            .rank_table thead {
                font-size: 20px;
            }

            .rank_table tbody td {
                font-size: 16px;
            }
        }

        #block2 .section__content .top-nav-container {
            display: flex;
            gap: 5px;
            left: 850px;
            position: relative;
            top: 180px;
            z-index: 100;
        }

        #block2 .section__content .top-nav-container button {
            background-image: url(../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block4/sprite.png);
            background-position: 0 -659px;
            color: #eb7d24;
            flex-shrink: 0;
            font-family: utm-talling;
            font-size: 40px;
            font-weight: 700;
            height: 74px;
            padding-bottom: 4px;
            transition: filter .2s ease-in-out;
            width: 282px;
        }

        #block2 .section__content .top-nav-container button:hover {
            filter: brightness(110%);
        }

        #block2 .section__content .top-nav-container button.active {
            background-image: url(../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block4/sprite.png);
            background-position: -266px -580px;
            color: #fff;
            height: 74px;
            width: 282px;
        }

        @media(orientation:portrait)and (max-width:998px) {
            #block2 .section__content .top-nav-container button {
                background-image: url(../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block4/sprite.png);
                background-position: -779px 0;
                font-size: 35.83px;
                height: 57px;
                width: 243px;
            }

            #block2 .section__content .top-nav-container button.active {
                background-image: url(../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block4/sprite.png);
                background-position: -779px -57px;
                height: 57px;
                width: 243px;
            }
        }
        </style>

<div class="section__content">
            <div class="decor-line pc"></div>
            <img data-src="/Assets/DangVip/video-img.jpg" alt="" class="lazyload video-img" />
            <div class="arcade">
                <img class="lazyload title pc" alt=""
                    data-src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block2/images/locate/vn/title.png" />
                <img class="lazyload title mb" alt=""
                    data-src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block2/images/locate/vn/title-mb.png" />
                <a href="https://youtu.be/yKf4SrbyjmM" class="btn-play" data-fancybox="">Play</a>
            </div>
            <div class="top-nav-container">
                <button class="btn-character-tab active" onclick="switchTab('character')">Dame Boss</button>
                <button class="btn-weapon-tab" onclick="switchTab('weapon')">Điểm SB</button>
                <button class="btn-tienbang-tab" onclick="switchTab('tienbang')">VIP Ingame</button>
                <!-- <button class="btn-nhapma-tab" onclick="switchTab('nhapma')">Nhập Ma</button> -->
            </div>
            <div class="text-container pc">
                <div class="heading" style="font-family: utm-talling;font-size: 80px;font-weight: 700;color:beige;">Bảng
                    Xếp Hạng</div>
                <div class="content character active">
                    <div id="rankingsm"></div>
                </div>

                <div class="content weapon">
                    <div id="rankingtv"></div>
                </div>

                <div class="content tienbang">
                    <div id="rankingtb"></div>
                </div>
                <!-- <div class="content nhapma">
                    <div id="nhapma"></div>
                </div> -->
            </div>
            <div class="current-top pc">
                <p class="text-right"><strong>Thông Tin Top Tháng <span id="currentMonth"></strong></p>
            </div>
        </div>

        <script>
        function switchTab(tabName) {
            // Remove active class from all buttons
            document.querySelectorAll('.top-nav-container button').forEach(button => {
                button.classList.remove('active');
            });

            // Add active class to the clicked button
            document.querySelector(`.btn-${tabName}-tab`).classList.add('active');

            // Hide all tab content
            document.querySelectorAll('.content').forEach(content => {
                content.classList.remove('active');
            });

            // Show the selected tab content
            document.querySelector(`.${tabName}`).classList.add('active');
        }

        // Get the current month number (1 to 12)
        const currentMonth = new Date().getMonth() + 1;

        // Update the HTML element with the current month number
        document.getElementById('currentMonth').textContent = currentMonth;

        // Fetch data for SỨC MẠNH
        fetch('/API/BXH/getRankingSucManh.php')
                .then(response => response.json())
                .then(data => {
                    let html = '<table class="rank_table">';
                    html += '<thead><tr><th>HẠNG</th><th>NHÂN VẬT</th><th>Điểm</th></tr></thead>';
                    html += '<tbody>';

                    for (let i = 0; i < 10; i++) {
                        const row = data[i];
                        if (!row) break;

                        html += '<tr>';
                        if (i < 3) {
                            html += `<td><img src="/Assets/images/top/top-${i + 1}.png" alt="Top ${i + 1}" /></td>`;
                        } else {
                            html += `<td>${i + 1}</td>`;
                        }

                        html += `<td>${row.name}</td>`;
                        html += `<td>${formatNumber(row.dameBoss)}</td>`;
                        // html += `<td>${getPlanetName(row.class)}</td>`;
                        html += '</tr>';
                    }

                    html += '</tbody></table>';
                    document.getElementById('rankingsm').innerHTML = html;
                })
                .catch(error => {
                    console.error('Lỗi khi lấy dữ liệu từ API:', error);
                    document.getElementById('rankingsm').innerHTML = 'Không thể tải dữ liệu từ API.';
                });

        // Fetch data for THỎI VÀNG
        fetch('/API/BXH/getRankingThoiVang.php')
            .then(response => response.json())
            .then(data => {
                let html = '<table class="rank_table">';
                html += '<thead><tr><th>HẠNG</th><th>NHÂN VẬT</th><th>Điểm</th></tr></thead>';
                html += '<tbody>';

                for (let i = 0; i < 10; i++) {
                    const row = data[i];
                    if (!row) break;

                    html += '<tr>';
                    if (i < 3) {
                        html += `<td><img src="/Assets/images/top/top-${i + 1}.png" alt="Top ${i + 1}" /></td>`;
                    } else {
                        html += `<td>${i + 1}</td>`;
                    }
                    html += `<td>${row.name}</td>`;
                    html += `<td>${row.point_sb}</td>`;
                    html += '</tr>';
                }

                html += '</tbody></table>';
                document.getElementById('rankingtv').innerHTML = html;
            })
            .catch(error => {
                console.error('Lỗi khi lấy dữ liệu từ API:', error);
                document.getElementById('rankingtv').innerHTML = 'Không thể tải dữ liệu từ API.';
            });

        // Fetch data for VIP
        fetch('/API/BXH/getRankingNap.php')
            .then(response => response.json())
            .then(data => {
                let html = '<table class="rank_table">';
                html += '<thead><tr><th>HẠNG</th><th>NHÂN VẬT</th><th>Điểm TrainFarm</th></tr></thead>';
                html += '<tbody>';

                for (let i = 0; i < 10; i++) {
                    const row = data[i];
                    if (!row) break;

                    html += '<tr>';
                    if (i < 3) {
                        html += `<td><img src="/Assets/images/top/top-${i + 1}.png" alt="Top ${i + 1}" /></td>`;
                    } else {
                        html += `<td>${i + 1}</td>`;
                    }

                    html += `<td>${row.name}</td>`;
                    html += `<td>${row.Diemfam}</td>`;
                    html += '</tr>';
                }

                html += '</tbody></table>';
                document.getElementById('rankingtb').innerHTML = html;
            })
            .catch(error => {
                console.error('Lỗi khi lấy dữ liệu từ API:', error);
                document.getElementById('rankingtb').innerHTML = 'Không thể tải dữ liệu từ API.';
            });
// Fetch data for NHẬP MA
// fetch('/API/BXH/getRankingNhapMa.php')
//             .then(response => response.json())
//             .then(data => {
//                 let html = '<table class="rank_table">';
//                 html += '<thead><tr><th>HẠNG</th><th>NHÂN VẬT</th><th>Nhập Ma</th></tr></thead>';
//                 html += '<tbody>';

//                 for (let i = 0; i < 10; i++) {
//                     const row = data[i];
//                     if (!row) break;

//                     html += '<tr>';
//                     if (i < 3) {
//                         html += `<td><img src="/Assets/images/top/top-${i + 1}.png" alt="Top ${i + 1}" /></td>`;
//                     } else {
//                         html += `<td>${i + 1}</td>`;
//                     }
//                     html += `<td>${row.name}</td>`;
//                     html += `<td>${row.LbTamkjll}</td>`;
//                     html += '</tr>';
//                 }

//                 html += '</tbody></table>';
//                 document.getElementById('nhapma').innerHTML = html;
//             })
//             .catch(error => {
//                 console.error('Lỗi khi lấy dữ liệu từ API:', error);
//                 document.getElementById('nhapma').innerHTML = 'Không thể tải dữ liệu từ API.';
//             });
        // Format number
        function formatNumber(number) {
            if (number > 1000000000) {
                return (number / 1000000000).toFixed(1) + ' tỷ điểm';
            } else if (number > 1000000) {
                return (number / 1000000).toFixed(1) + ' triệu điểm';
            } else if (number >= 1000) {
                return (number / 1000).toFixed(1) + ' nghìn điểm';
            } else {
                return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            }
        }

        function formatMoney(number) {
            if (isNaN(number) || number === null) {
                return '0 cấp';
            }

            let suffix = '';
            if (number >= 1000000000000) {
                number /= 1000000000000;
                suffix = ' cấp';
            } else if (number >= 1000000000) {
                number /= 1000000000;
                suffix = ' cấp';
            } else if (number >= 1000000) {
                number /= 1000000;
                suffix = ' cấp';
            } else if (number >= 1000) {
                number /= 1000;
                suffix = ' cấp';
            }

            return number.toLocaleString() + suffix;
        }


        // Get planet name
        function getPlanetName(gender) {
            return ["Gôku", "Cađíc", "Gôhan", "Pôcôlô", "Fide", "Cell"][gender];
        }
        </script>
        <div class="floatright"></div>
    </section>
    <section id="block3" class="section block3 scrollFrame" data-block-id="block3">
        <div class="section__background">
            <img data-src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block3/images/bg-v2.jpg"
                alt="" class="lazyload pc">
            <img data-src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block3/images/bg-v2-mb.jpg"
                alt="" class="lazyload mb">
            <span id="block3-scrollwatch-pin" class="scrollwatch-pin"></span>
        </div>
        <div class="section__content">
            <img class="title pc" alt=""
                src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block3/images/locate/vn/title.png" />
            <img class="title mb" alt=""
                src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block3/images/locate/vn/title-mb.png" />
            <div class="people"></div>
            <div class="news-bg" id="news-block">
                <div class="banner-swiper-container">
                    <div class="banner">&lt;<?= $_ServerName ?>&gt; Chính thức ra mắt</div>
                    <div class="banner">Danh Sách Giftcode </div>
                    <div class="banner">Mở Thành Viên </div>
                    <div class="banner">Nạp Thẻ</div>
                    <div class="swiper">
                        <div class="swiper-wrapper">
                            <div class="swiper-slide">
                                <a href="#" target="_blank" title="&lt;<?= $_ServerName ?>&gt; Chính thức ra mắt">
                                    <img src="../../Assets/ThangHoa/Banner/1.jpg"
                                        alt="&lt;<?= $_ServerName ?>&gt; Chính thức ra mắt">
                                </a>
                            </div>
                            <div class="swiper-slide">
                                <a href="/nap-the" target="_blank" title="Nạp Thẻ">
                                    <img src="../../Assets/ThangHoa/Banner/4.jpg" alt="Nạp Thẻ">
                                </a>
                            </div>
                        </div>
                    </div>

                    <button class="swiper-button swiper-button-prev"></button>
                    <button class="swiper-button swiper-button-next"></button>
                    <div class="swiper-pagination"></div>
                </div>
                <!-- Thêm thư viện Swiper.js -->
                <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css">
                <script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>

                <div class="top-menu">
                    <nav class="nav-container">
                        <button href="#" class="btn-event active" data-params="?cate=event" data-block-name="home-news"
                            data-shorturl="//metalslugawk.vnggames.com/tin-tuc-ajax"
                            data-viewall="http://metalslugawk.vnggames.com/news/event.1.html">SỰ KIỆN</button>
                        <button href="#" class="btn-news" data-params="?cate=news" data-block-name="home-news"
                            data-shorturl="//metalslugawk.vnggames.com/tin-tuc-ajax"
                            data-viewall="http://metalslugawk.vnggames.com/news/news.1.html">TIN TỨC</button>
                    </nav>
                    <form class="search-form" id="search__form" method="get"
                        action="http://metalslugawk.vnggames.com/search.html">
                        <input class="search__field" type="text" name="q" placeholder="Tìm kiếm thông tin">
                        <button class="search__button btn-search" type="submit"><span
                                class="search-icon"></span></button>
                    </form>
                </div>
                <div class="news-container">
                    <div class="news-list">
                        <?php
                        ?>
                    </div>
                    <a href="#" class="viewAll btn-more">XEM THÊM</a>
                </div>

            </div>
        </div>
    </section>

    <section id="block4" class="section block4 scrollFrame" data-block-id="block4">
        <div class="section__background">
            <img data-src="/img.game/products/metalslug/2023-global-mainsite/dist/assets/block4/images/bg-v2.jpg"
                alt="" class="lazyload pc is-loaded"
                src="/img.game/products/metalslug/2023-global-mainsite/dist/assets/block4/images/bg-v2.jpg"
                data-loaded="true">
            <img data-src="/img.game/products/metalslug/2023-global-mainsite/dist/assets/block4/images/bg-v2-mb.jpg"
                alt="" class="lazyload mb">
            <span id="block4-scrollwatch-pin" class="scrollwatch-pin scroll-watch-in-view"></span>
        </div>
        <div class="section__content">
            <img class="title" alt=""
                src="/img.game/products/metalslug/2023-global-mainsite/dist/assets/block4/images/locate/vn/title.png">
            <div class="top-nav-container">
                <button class="btn-character-tab active">NHÂN VẬT</button>
                <button class="btn-weapon-tab">VŨ KHÍ</button>
                <button class="btn-vehicle-tab">PHƯƠNG TIỆN</button>
            </div>
            <div class="block4-swiper-container">
                <div id="character-swiper-container" class="swiper-container active">
                    <div id="character-swiper"
                        class="swiper swiper-initialized swiper-horizontal swiper-pointer-events">
                        <ul class="swiper-wrapper" style="transform: translate3d(0px, 0px, 0px);">
                            <li class="swiper-slide" style="width: 1473px;">
                                <div class="video-container">
                                    <div class="video-bg">
                                        <a href="#" class="btn-play" data-fancybox=""></a>
                                    </div>
                                    <img data-src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/lilithgame.png"
                                        alt="" class="lazyload">
                                </div>
                                <img data-src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/bulma.png"
                                    alt="" class="character-img lazyload">
                                <div class="character-description">
                                    <div class="lightning"></div>
                                    <div class="content">
                                        <div class="character-name">BULMA</div>
                                        <div class="character-text">
                                            <p>Bulma là đại tiểu thư của tập đoàn tài phiệt Capsule. Cô thừa hưởng bộ
                                                não thiên tài từ bố (Kĩ sư Brief),
                                                ngoại hình xinh đẹp từ mẹ (Bà Brief).
                                                Mặc dù xuất thân trong một tập đoàn cơ khí nổi tiếng, Bulma vẫn hòa đồng
                                                với bạn bè và đam mê chế tạo máy.</p>
                                            <p>Là một trong những NPC nổi tiếng và gắn liền với cốt truyện của Dragon
                                                Ball. Thông qua các NPC đặc biệt như Thượng Đế,
                                                Thần Mèo, Thần Vũ Trụ, bạn có khả năng tăng sức mạnh và tiềm năng của
                                                nhân vật.</p>
                                        </div>
                                    </div>
                                </div>
                            </li>
                            <li class="swiper-slide" style="width: 1473px;">
                                <div class="video-container">
                                    <div class="video-bg">
                                        <a href="https://youtu.be/KjCpA5sa968" class="btn-play" data-fancybox=""></a>
                                    </div>
                                    <img data-src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/joeingame.png"
                                        alt="" class="lazyload">
                                </div>
                                <img data-src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/quylao.png"
                                    alt="" class="character-img lazyload">
                                <div class="character-description">
                                    <div class="lightning"></div>
                                    <div class="content">
                                        <div class="character-name">QUY LÃO TIÊN SINH</div>
                                        <div class="character-text">
                                            <p>Một ngày nọ, Quy Lão Tiên Sinh quyết định thử sức với công nghệ hiện đại.
                                                Ông mua một chiếc điện thoại thông minh và bắt đầu học cách sử dụng nó.
                                            </p>
                                            <p>Sau vài giờ loay hoay, ông cuối cùng cũng biết cách chụp ảnh tự sướng.
                                                Ông rất tự hào và quyết định gửi bức ảnh đầu tiên cho bạn bè.</p>
                                            <p>Nhưng thay vì gửi ảnh, ông lại vô tình gửi một tin nhắn thoại dài 5 phút,
                                                trong đó ông chỉ toàn nói về cách ông đã học cách chụp ảnh tự sướng như
                                                thế nào. Bạn bè của ông cười nghiêng ngả và gọi ông là "Quy Lão Công
                                                Nghệ".</p>
                                        </div>

                                    </div>
                                </div>
                            </li>

                            <li class="swiper-slide" style="width: 1473px;">
                                <div class="video-container">
                                    <div class="video-bg">
                                        <a href="https://youtu.be/IpOxyK0GiwE" class="btn-play" data-fancybox=""></a>
                                    </div>
                                    <img data-src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/alessioingame.png"
                                        alt="" class="lazyload">
                                </div>
                                <img data-src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/launch.png"
                                    alt="" class="character-img lazyload">
                                <div class="character-description">
                                    <div class="lightning"></div>
                                    <div class="content">
                                        <div class="character-name">LAUNCH</div>
                                        <div class="character-text">
                                            <p>Launch là một nhân vật đặc biệt với hai tính cách hoàn toàn trái ngược
                                                nhau. Khi cô hắt hơi, cô sẽ chuyển từ một cô gái hiền lành, dễ thương
                                                thành một chiến binh mạnh mẽ và hung dữ.</p>
                                            <p>Trong trạng thái hiền lành, Launch rất tốt bụng và luôn sẵn sàng giúp đỡ
                                                người khác. Nhưng khi cô hắt hơi và biến thành trạng thái hung dữ, cô
                                                trở nên vô cùng nguy hiểm và không ngần ngại sử dụng vũ lực để đạt được
                                                mục đích của mình.</p>
                                            <p>Dù ở trạng thái nào, Launch luôn là một người bạn đáng tin cậy và sẵn
                                                sàng chiến đấu vì những người cô yêu thương.</p>
                                        </div>
                                    </div>
                                </div>
                            </li>

                            <li class="swiper-slide" style="width: 1473px;">
                                <div class="video-container">
                                    <div class="video-bg">
                                        <a href="https://youtu.be/gy8b4mbVgKI" class="btn-play" data-fancybox=""></a>
                                    </div>
                                    <img data-src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/kyoingame.png"
                                        alt="" class="lazyload">
                                </div>
                                <img data-src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/goku.png"
                                    alt="" class="character-img lazyload">
                                <div class="character-description">
                                    <div class="lightning"></div>
                                    <div class="content">
                                        <div class="character-name">GOKU</div>
                                        <div class="character-text">
                                            <p>Goku là một chiến binh Saiyan mạnh mẽ, người đã bảo vệ Trái Đất và vũ trụ
                                                khỏi nhiều mối đe dọa. Anh ta nổi tiếng với sức mạnh phi thường và lòng
                                                dũng cảm.</p>
                                            <p>Goku luôn tìm kiếm những đối thủ mạnh mẽ để thử thách bản thân và không
                                                ngừng rèn luyện để trở nên mạnh mẽ hơn. Anh ta có khả năng biến hình
                                                thành nhiều trạng thái khác nhau, mỗi trạng thái đều tăng cường sức mạnh
                                                của anh ta.</p>
                                            <p>Với tinh thần chiến đấu không ngừng nghỉ và lòng nhân ái, Goku đã trở
                                                thành một biểu tượng của sự kiên cường và lòng dũng cảm trong thế giới
                                                Dragon Ball.</p>
                                        </div>
                                    </div>
                                </div>
                            </li>

                            <li class="swiper-slide" style="width: 1473px;">
                                <div class="video-container">
                                    <div class="video-bg">
                                        <a href="https://youtu.be/Ef3kwxeAbZQ" class="btn-play" data-fancybox=""></a>
                                    </div>
                                    <img data-src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/sayaingame.png"
                                        alt="" class="lazyload">
                                </div>
                                <img data-src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/piccolo.png"
                                    alt="" class="character-img lazyload">
                                <div class="character-description">
                                    <div class="lightning"></div>
                                    <div class="content">
                                        <div class="character-name">PICCOLO</div>
                                        <div class="character-text">
                                            <p>Piccolo là một chiến binh Namekian mạnh mẽ và là một trong những nhân vật
                                                quan trọng trong thế giới Dragon Ball. Anh ta là con trai và là sự tái
                                                sinh của Đại Ma Vương Piccolo.</p>
                                            <p>Ban đầu, Piccolo là kẻ thù của Goku, nhưng sau đó anh ta đã trở thành một
                                                đồng minh quan trọng và là người thầy của Gohan. Piccolo nổi tiếng với
                                                khả năng chiến đấu và trí tuệ sắc bén.</p>
                                            <p>Với lòng dũng cảm và sự quyết tâm, Piccolo đã nhiều lần bảo vệ Trái Đất
                                                khỏi các mối đe dọa và trở thành một trong những chiến binh mạnh mẽ nhất
                                                trong vũ trụ.</p>
                                        </div>
                                    </div>
                                </div>
                            </li>
                            <li class="swiper-slide" style="width: 1473px;">
                                <div class="video-container">
                                    <div class="video-bg">
                                        <a href="https://youtu.be/Ef3kwxeAbZQ" class="btn-play" data-fancybox=""></a>
                                    </div>
                                    <img data-src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/kukikgame.png"
                                        alt="" class="lazyload">
                                </div>
                                <img data-src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/videl.png"
                                    alt="" class="character-img lazyload">
                                <div class="character-description">
                                    <div class="lightning"></div>
                                    <div class="content">
                                        <div class="character-name">VIDEL</div>
                                        <div class="character-text">
                                            <p>Videl là con gái của Mr. Satan và là vợ của Gohan. Cô là một nữ chiến
                                                binh mạnh mẽ và dũng cảm, luôn sẵn sàng chiến đấu để bảo vệ những người
                                                cô yêu thương.</p>
                                            <p>Videl đã học được nhiều kỹ năng chiến đấu từ Gohan và trở thành một chiến
                                                binh tài năng. Cô cũng là mẹ của Pan, một trong những nhân vật quan
                                                trọng trong thế giới Dragon Ball.</p>
                                            <p>Với lòng dũng cảm và sự quyết tâm, Videl đã nhiều lần chứng tỏ mình là
                                                một chiến binh đáng gờm và là một người mẹ tuyệt vời.</p>
                                        </div>

                                    </div>
                                </div>
                            </li>
                            <li class="swiper-slide swiper-slide-active" style="width: 1473px;">
                                <div class="video-container">
                                    <div class="video-bg">
                                        <a href="https://youtu.be/OVBdUFxjKpI" class="btn-play" data-fancybox=""></a>
                                    </div>
                                    <img data-src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/huanlonggame.png"
                                        alt="" class="lazyload is-loaded"
                                        src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/huanlonggame.png"
                                        data-loaded="true">
                                </div>
                                <img data-src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/and21.png"
                                    alt="" class="character-img lazyload is-loaded"
                                    src="./global-mainsite.mto.zing.vn/products/metalslug/2023-global-mainsite/dist/assets/block4/images/and21.png"
                                    data-loaded="true">
                                <div class="character-description">
                                    <div class="lightning"></div>
                                    <div class="content">
                                        <div class="character-name">ANDROID 21</div>
                                        <div class="character-text">
                                            <p>Android 21 là một nhân vật đặc biệt trong thế giới Dragon Ball, được tạo
                                                ra bởi Dr. Gero. Cô là một nhà khoa học tài năng với trí tuệ vượt trội,
                                                và có khả năng biến hình thành nhiều trạng thái khác nhau.</p>
                                            <p>Trong hình dạng ban đầu, Android 21 có mái tóc dài màu nâu và đeo kính.
                                                Khi biến hình thành trạng thái Majin, da của cô trở nên màu hồng, tóc
                                                chuyển sang màu trắng và cô có thêm một cái đuôi.</p>
                                            <p>Android 21 có hai mặt tính cách: một mặt tốt và một mặt xấu. Mặt tốt của
                                                cô luôn cố gắng bảo vệ mọi người, trong khi mặt xấu lại muốn tiêu diệt
                                                tất cả. Sự đấu tranh giữa hai mặt tính cách này tạo nên một nhân vật
                                                phức tạp và thú vị.</p>
                                        </div>

                                    </div>
                                </div>
                            </li>
                        </ul>
                        <div id="character-pagination"
                            class="swiper-initialized swiper-horizontal swiper-pointer-events swiper-watch-progress">
                            <div class="swiper-wrapper">
                                <div class="swiper-slide swiper-slide-visible swiper-slide-active"
                                    style="width: 201.857px; margin-right: 10px;">
                                    <button class="nav-button active">BULMA</button>
                                </div>
                                <div class="swiper-slide swiper-slide-visible swiper-slide-next"
                                    style="width: 201.857px; margin-right: 10px;">
                                    <button class="nav-button">QUY LÃO</button>
                                </div>
                                <div class="swiper-slide swiper-slide-visible"
                                    style="width: 201.857px; margin-right: 10px;">
                                    <button class="nav-button">LAUNCH</button>
                                </div>
                                <div class="swiper-slide swiper-slide-visible"
                                    style="width: 201.857px; margin-right: 10px;">
                                    <button class="nav-button">GOKU</button>
                                </div>
                                <div class="swiper-slide swiper-slide-visible"
                                    style="width: 201.857px; margin-right: 10px;">
                                    <button class="nav-button">PICCOLO</button>
                                </div>
                                <div class="swiper-slide swiper-slide-visible"
                                    style="width: 201.857px; margin-right: 10px;">
                                    <button class="nav-button">VIDEL</button>
                                </div>
                                <div class="swiper-slide swiper-slide-visible"
                                    style="width: 201.857px; margin-right: 10px;">
                                    <button class="nav-button">ANDROID 21</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <button class="swiper-button swiper-button-prev swiper-button-disabled" disabled=""></button>
                    <button class="swiper-button swiper-button-next"></button>
                </div>

                <div id="weapon-swiper-container" class="swiper-container">
                    <div id="weapon-swiper"
                        class="swiper swiper-initialized swiper-horizontal swiper-pointer-events swiper-backface-hidden">
                        <div class="swiper-wrapper">
                            <div class="swiper-slide swiper-slide-active" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/weapon/Ragewing.png"
                                    class="weapon-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/weapon/Ragewing.png"
                                    data-loaded="true">
                                <div class="weapon-content">
                                    <div class="weapon-name">RAGEWING</div>
                                    <div class="lightning"></div>
                                    <div class="table-title">Thông tin (Lv1)</div>
                                    <div class="row-container">
                                        <div class="row">
                                            <div class="left"><span class="icon icon-gun"></span><span class="text">Tấn
                                                    Công</span></div>
                                            <div class="right"><span class="value">700</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-heart"></span><span
                                                    class="text">Hộp đạn</span></div>
                                            <div class="right"><span class="value">112</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-bullet"></span><span
                                                    class="text">DPS</span></div>
                                            <div class="right"><span class="value">259.3%</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-target"></span><span
                                                    class="text">Phạm vi</span></div>
                                            <div class="right"><span class="value">tầm trung</span></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="swiper-slide swiper-slide-next" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/weapon/Sky-Ripper.png"
                                    class="weapon-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/weapon/Sky-Ripper.png"
                                    data-loaded="true">
                                <div class="weapon-content">
                                    <div class="weapon-name">SKY RIPPER</div>
                                    <div class="lightning"></div>
                                    <div class="table-title">Thông tin (Lv1)</div>
                                    <div class="row-container">
                                        <div class="row">
                                            <div class="left"><span class="icon icon-gun"></span><span class="text">Tấn
                                                    Công</span></div>
                                            <div class="right"><span class="value">700</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-heart"></span><span
                                                    class="text">Hộp đạn</span></div>
                                            <div class="right"><span class="value">156</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-bullet"></span><span
                                                    class="text">DPS</span></div>
                                            <div class="right"><span class="value">275.0%</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-target"></span><span
                                                    class="text">Phạm vi</span></div>
                                            <div class="right"><span class="value">tầm xa</span></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="swiper-slide" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/weapon/Moltor.png"
                                    class="weapon-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/weapon/Moltor.png"
                                    data-loaded="true">
                                <div class="weapon-content">
                                    <div class="weapon-name">MOLTOR</div>
                                    <div class="lightning"></div>
                                    <div class="table-title">Thông tin (Lv1)</div>
                                    <div class="row-container">
                                        <div class="row">
                                            <div class="left"><span class="icon icon-gun"></span><span class="text">Tấn
                                                    Công</span></div>
                                            <div class="right"><span class="value">700</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-heart"></span><span
                                                    class="text">Hộp đạn</span></div>
                                            <div class="right"><span class="value">18</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-bullet"></span><span
                                                    class="text">DPS</span></div>
                                            <div class="right"><span class="value">255.9%</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-target"></span><span
                                                    class="text">Phạm vi</span></div>
                                            <div class="right"><span class="value">tầm trung</span></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="swiper-slide" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/weapon/Star-Slayer.png"
                                    class="weapon-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/weapon/Star-Slayer.png"
                                    data-loaded="true">
                                <div class="weapon-content">
                                    <div class="weapon-name">STAR SLAYER</div>
                                    <div class="lightning"></div>
                                    <div class="table-title">Thông tin (Lv1)</div>
                                    <div class="row-container">
                                        <div class="row">
                                            <div class="left"><span class="icon icon-gun"></span><span class="text">Tấn
                                                    Công</span></div>
                                            <div class="right"><span class="value">1.160</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-heart"></span><span
                                                    class="text">Hộp đạn</span></div>
                                            <div class="right"><span class="value">42</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-bullet"></span><span
                                                    class="text">DPS</span></div>
                                            <div class="right"><span class="value">322.0%</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-target"></span><span
                                                    class="text">Phạm vi</span></div>
                                            <div class="right"><span class="value">tầm trung</span></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="swiper-slide" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/weapon/ECIPSE.png"
                                    class="weapon-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/weapon/ECIPSE.png"
                                    data-loaded="true">
                                <div class="weapon-content">
                                    <div class="weapon-name">ECIPSE</div>
                                    <div class="lightning"></div>
                                    <div class="table-title">Thông tin (Lv1)</div>
                                    <div class="row-container">
                                        <div class="row">
                                            <div class="left"><span class="icon icon-gun"></span><span class="text">Tấn
                                                    Công</span></div>
                                            <div class="right"><span class="value">700</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-heart"></span><span
                                                    class="text">Hộp đạn</span></div>
                                            <div class="right"><span class="value">20</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-bullet"></span><span
                                                    class="text">DPS</span></div>
                                            <div class="right"><span class="value">245.1%</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-target"></span><span
                                                    class="text">Phạm vi</span></div>
                                            <div class="right"><span class="value">tầm xa</span></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="swiper-slide" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/weapon/Icebound-Deity.png"
                                    class="weapon-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/weapon/Icebound-Deity.png"
                                    data-loaded="true">
                                <div class="weapon-content">
                                    <div class="weapon-name">ICEBOUND DEITY</div>
                                    <div class="lightning"></div>
                                    <div class="table-title">Thông tin (Lv1)</div>
                                    <div class="row-container">
                                        <div class="row">
                                            <div class="left"><span class="icon icon-gun"></span><span class="text">Tấn
                                                    Công</span></div>
                                            <div class="right"><span class="value">700</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-heart"></span><span
                                                    class="text">Hộp đạn</span></div>
                                            <div class="right"><span class="value">86</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-bullet"></span><span
                                                    class="text">DPS</span></div>
                                            <div class="right"><span class="value">247.1%</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-target"></span><span
                                                    class="text">Phạm vi</span></div>
                                            <div class="right"><span class="value">tầm gần</span></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="swiper-slide" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/weapon/Howler.png"
                                    class="weapon-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/weapon/Howler.png"
                                    data-loaded="true">
                                <div class="weapon-content">
                                    <div class="weapon-name">HOWLER</div>
                                    <div class="lightning"></div>
                                    <div class="table-title">Thông tin (Lv1)</div>
                                    <div class="row-container">
                                        <div class="row">
                                            <div class="left"><span class="icon icon-gun"></span><span class="text">Tấn
                                                    Công</span></div>
                                            <div class="right"><span class="value">700</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-heart"></span><span
                                                    class="text">Hộp đạn</span></div>
                                            <div class="right"><span class="value">16</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-bullet"></span><span
                                                    class="text">DPS</span></div>
                                            <div class="right"><span class="value">264.3%</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-target"></span><span
                                                    class="text">Phạm vi</span></div>
                                            <div class="right"><span class="value">tầm gần</span></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="swiper-slide" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/weapon/Peace-Slammer.png"
                                    class="weapon-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/weapon/Peace-Slammer.png"
                                    data-loaded="true">
                                <div class="weapon-content">
                                    <div class="weapon-name">PEACE SLAMMER</div>
                                    <div class="lightning"></div>
                                    <div class="table-title">Thông tin (Lv1)</div>
                                    <div class="row-container">
                                        <div class="row">
                                            <div class="left"><span class="icon icon-gun"></span><span class="text">Tấn
                                                    Công</span></div>
                                            <div class="right"><span class="value">700</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-heart"></span><span
                                                    class="text">Hộp đạn</span></div>
                                            <div class="right"><span class="value">18</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-bullet"></span><span
                                                    class="text">DPS</span></div>
                                            <div class="right"><span class="value">273.2%</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-target"></span><span
                                                    class="text">Phạm vi</span></div>
                                            <div class="right"><span class="value">tầm gần</span></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="swiper-slide" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/weapon/blade-saw-launcher.png"
                                    class="weapon-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/weapon/blade-saw-launcher.png"
                                    data-loaded="true">
                                <div class="weapon-content">
                                    <div class="weapon-name">BLADE SAW LAUNCHER</div>
                                    <div class="lightning"></div>
                                    <div class="table-title">Thông tin (Lv1)</div>
                                    <div class="row-container">
                                        <div class="row">
                                            <div class="left"><span class="icon icon-gun"></span><span class="text">Tấn
                                                    Công</span></div>
                                            <div class="right"><span class="value">360</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-heart"></span><span
                                                    class="text">Hộp đạn</span></div>
                                            <div class="right"><span class="value">16</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-bullet"></span><span
                                                    class="text">DPS</span></div>
                                            <div class="right"><span class="value">222.0%</span></div>
                                        </div>
                                        <div class="row">
                                            <div class="left"><span class="icon icon-target"></span><span
                                                    class="text">Phạm vi</span></div>
                                            <div class="right"><span class="value">tầm trung</span></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="weapon-pagination"
                            class="swiper-initialized swiper-horizontal swiper-pointer-events swiper-backface-hidden">
                            <div class="swiper-wrapper" style="transition-duration: 0ms;">
                                <div class="swiper-slide" style="width: 288.2px; margin-right: 16px;"><button
                                        class="nav-button active"> <img class="gun-inner gun-1" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/weapon/Ragewing.png">
                                    </button></div>
                                <div class="swiper-slide swiper-slide-prev" style="width: 288.2px; margin-right: 16px;">
                                    <button class="nav-button"> <img class="gun-inner gun-2" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/weapon/Sky-Ripper.png">
                                    </button>
                                </div>
                                <div class="swiper-slide swiper-slide-active"
                                    style="width: 288.2px; margin-right: 16px;"><button class="nav-button"> <img
                                            class="gun-inner gun-3" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/weapon/Moltor.png">
                                    </button></div>
                                <div class="swiper-slide swiper-slide-next" style="width: 288.2px; margin-right: 16px;">
                                    <button class="nav-button"> <img class="gun-inner gun-4" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/weapon/Star-Slayer.png">
                                    </button>
                                </div>
                                <div class="swiper-slide" style="width: 288.2px; margin-right: 16px;"><button
                                        class="nav-button"> <img class="gun-inner gun-5" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/weapon/ECIPSE.png">
                                    </button></div>
                                <div class="swiper-slide" style="width: 288.2px; margin-right: 16px;"><button
                                        class="nav-button"> <img class="gun-inner gun-5" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/weapon/Icebound-Deity.png">
                                    </button></div>
                                <div class="swiper-slide" style="width: 288.2px; margin-right: 16px;"><button
                                        class="nav-button"> <img class="gun-inner gun-5" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/weapon/Howler.png">
                                    </button></div>
                                <div class="swiper-slide" style="width: 288.2px; margin-right: 16px;"><button
                                        class="nav-button"> <img class="gun-inner gun-5" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/weapon/Peace-Slammer.png">
                                    </button></div>
                                <div class="swiper-slide" style="width: 288.2px; margin-right: 16px;"><button
                                        class="nav-button"> <img class="gun-inner gun-5" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/weapon/blade-saw-launcher.png">
                                    </button></div>
                            </div>
                        </div>
                    </div>
                    <button class="swiper-button swiper-button-prev swiper-button-disabled" disabled=""></button>
                    <button class="swiper-button swiper-button-next"></button>
                </div>
                <div id="vehicle-swiper-container" class="swiper-container">
                    <div id="vehicle-swiper"
                        class="swiper swiper-initialized swiper-horizontal swiper-pointer-events swiper-backface-hidden">
                        <div class="swiper-wrapper">
                            <div class="swiper-slide swiper-slide-active" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/vehicle/1.png"
                                    class="vehicle-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/vehicle/1.png"
                                    data-loaded="true">
                                <div class="vehicle-description">
                                    <div class="lightning"></div>
                                    <div class="content">
                                        <div class="vehicle-name">Metal Slug</div>
                                        <div class="vehicle-text">
                                            <p>"Phương Tiện Siêu Cấp 001" Xe Tăng Chiến Đấu.</p>
                                            <p>Xe tăng tiên tiến biệt hiệu Metal Slug, được quân chính quy và chiến sĩ
                                                kháng chiến sử dụng rộng rãi; Trang bị 1 nòng pháo chính sát thương cao
                                                và một súng máy bắn siêu tốc, hỏa lực mạnh!</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="swiper-slide swiper-slide-next" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/vehicle/5.png"
                                    class="vehicle-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/vehicle/5.png"
                                    data-loaded="true">
                                <div class="vehicle-description">
                                    <div class="lightning"></div>
                                    <div class="content">
                                        <div class="vehicle-name">Drill Slug</div>
                                        <div class="vehicle-text">
                                            <p>Máy Khoan Điên Cuồng không thể ngăn cản.</p>
                                            <p>Cỗ máy đào bới làm từ kimm loại cường độ cao, mũi khoan tốc độ cao khổng
                                                lồ xuyên thấu mọi vật thể cản đường</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="swiper-slide" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/vehicle/3.png"
                                    class="vehicle-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/vehicle/3.png"
                                    data-loaded="true">
                                <div class="vehicle-description">
                                    <div class="lightning"></div>
                                    <div class="content">
                                        <div class="vehicle-name">Heavy Mecha</div>
                                        <div class="vehicle-text">
                                            <p>Máy Chiến Đấu Kềm Lớn Công Nghiệp.</p>
                                            <p>Cỗ máy công nghiệp được cải tiến từ "Giáo binh cơ khí" đã nghỉ hưu, lắp
                                                ráp cặp kìm công nghiệp khổng lồ hạng nặng, ngoài việc chuyển hàng, còn
                                                có thể đập nát những thứ không thuận mắt</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="swiper-slide" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/vehicle/4.png"
                                    class="vehicle-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/vehicle/4.png"
                                    data-loaded="true">
                                <div class="vehicle-description">
                                    <div class="lightning"></div>
                                    <div class="content">
                                        <div class="vehicle-name">Sonic Alpaca</div>
                                        <div class="vehicle-text">
                                            <p>Siêu âm làm tan vỡ trái tim bạn</p>
                                            <p>Súng máy kết hợp với đạn nổ! Dê Kính Râm xuất hiện, ta sẽ dùng tiếng ồn
                                                oanh tạc hai tai và giáp của kẻ địch</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="swiper-slide" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/vehicle/2.png"
                                    class="vehicle-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/vehicle/2.png"
                                    data-loaded="true">
                                <div class="vehicle-description">
                                    <div class="lightning"></div>
                                    <div class="content">
                                        <div class="vehicle-name">Metal Express</div>
                                        <div class="vehicle-text">
                                            <p>Xe cải tạo trượt bánh mạnh</p>
                                            <p>Quân Chính Quy cải tạo xe dân dụng Tia 660 thành phương tiện chiến đấu,
                                                sở hữu máy bắn tên lửa và súng máy. Thiết bị phun nitro được cải tạo, xe
                                                trượt bánh nhanh hơn làm kẻ địch đầu hàng!</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="swiper-slide" style="width: 1505px;"><img
                                    data-src="/img.game/upload/metalslug/source/Mainsite/vehicle/6.png"
                                    class="vehicle-img lazyload is-loaded" alt=""
                                    src="/img.game/upload/metalslug/source/Mainsite/vehicle/6.png"
                                    data-loaded="true">
                                <div class="vehicle-description">
                                    <div class="lightning"></div>
                                    <div class="content">
                                        <div class="vehicle-name">SV-Camel</div>
                                        <div class="vehicle-text">
                                            <p>Lạc đà chiến đấu huyền thoại</p>
                                            <p>Nhìn thì là lạc đà nhu thuận, nhưng lại là Súng Máy với sức mạnh và tốc
                                                độ bắn kinh người, còn có thể tấn công bằng sét trong phạm vi toàn màn
                                                hình!</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="vehicle-pagination"
                            class="swiper-initialized swiper-horizontal swiper-pointer-events swiper-backface-hidden">
                            <div class="swiper-wrapper" style="transition-duration: 0ms;">
                                <div class="swiper-slide" style="width: 288.2px; margin-right: 16px;"><button
                                        class="nav-button active"><img class="vehicle-inner vehicle-1" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/vehicle/1_1.png"></button>
                                </div>
                                <div class="swiper-slide swiper-slide-prev" style="width: 288.2px; margin-right: 16px;">
                                    <button class="nav-button"><img class="vehicle-inner vehicle-2" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/vehicle/5_5.png"></button>
                                </div>
                                <div class="swiper-slide swiper-slide-active"
                                    style="width: 288.2px; margin-right: 16px;"><button class="nav-button"><img
                                            class="vehicle-inner vehicle-3" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/vehicle/3_3.png"></button>
                                </div>
                                <div class="swiper-slide swiper-slide-next" style="width: 288.2px; margin-right: 16px;">
                                    <button class="nav-button"><img class="vehicle-inner vehicle-4" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/vehicle/4_4.png"></button>
                                </div>
                                <div class="swiper-slide" style="width: 288.2px; margin-right: 16px;"><button
                                        class="nav-button"><img class="vehicle-inner vehicle-5" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/vehicle/2_2.png"></button>
                                </div>
                                <div class="swiper-slide" style="width: 288.2px; margin-right: 16px;"><button
                                        class="nav-button"><img class="vehicle-inner vehicle-5" alt=""
                                            src="/img.game/upload/metalslug/source/Mainsite/vehicle/6_6.png"></button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <button class="swiper-button swiper-button-prev swiper-button-disabled" disabled=""></button>
                    <button class="swiper-button swiper-button-next"></button>
                </div>
            </div>
        </div>
    </section>


    <section id="block5" class="section block5 scrollFrame" data-block-id="block5">
        <div class="section__background">
            <img data-src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block5/images/bg-v2.jpg"
                alt="" class="lazyload pc">
            <img data-src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block5/images/bg-v2-mb.jpg"
                alt="" class="lazyload mb">
            <span id="block5-scrollwatch-pin" class="scrollwatch-pin"></span>
        </div>
        <div class="section__content">
            <img class="title" alt=""
                src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/block5/images/locate/vn/title.png" />
            <div class="block5-swiper-container">
                <div id="hot-feature-swiper" class="swiper">
                    <div class="swiper-wrapper">
                        <div class="swiper-slide">
                            <div class="img-bg">
                                <div class="img-container">
                                    <img src="https://placehold.co/400x236" target="_blank" alt="1">
                                    <a href="#" class="btn-play" data-fancybox>Play</a>
                                </div>
                            </div>
                        </div>
                        <div class="swiper-slide">
                            <div class="img-bg">
                                <div class="img-container">
                                    <img src="https://dichvuhades.com/Content/images/9k.gif" target="_blank" alt="2">
									<?php if ($_Login === null) {?>
									<a  class="btn-play">Play</a>
									<?php } else { ?>
                                    <a  class="btn-play">Play</a>
									<?php } ?>
                                </div>
                            </div>
                        </div>
                        <div class="swiper-slide">
                            <div class="img-bg">
                                <div class="img-container">
                                    <img src="https://placehold.co/400x236" target="_blank" alt="3">
                                    <a href="#" class="btn-play" data-fancybox>Play</a>
                                </div>
                            </div>
                        </div>

                    </div>
                    <div class="swiper-pagination"></div>
                </div>
                <button class="swiper-button swiper-button-prev"></button>
                <button class="swiper-button swiper-button-next"></button>
            </div>
        </div>
    </section>


    <?php include "./DHKD/Footer.php" ?>
</div>
<div class="floating floating-left" style="z-index: 9999;">
    <div id="float_top" class="float-scale float_top scaleMobile scaleDesktop" data-block-id="float_top">
        <div class="float__content pc">
            <img src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/float_top/images/bg.png"
                alt="" class="bg-img">
            <a href="/" class="metalslug-icon">ICON<img src="<?= $_Logo ?>"
                    style="display: block; margin-left: auto; margin-right: auto; max-width: 200px;"></a>
            <nav class="navigation">


                <ul class="list-container">
                    <li class="list-item">
                        <a href="#block1" class="menu-item scrollFrameControl" title="TRANG CHỦ">
                            TRANG CHỦ
                        </a>
                    </li>
                    <li class="list-item">
                        <a href="#block2" class="menu-item scrollFrameControl" title="BẢNG XẾP HẠNG">
                            BẢNG XẾP HẠNG
                        </a>
                    </li>
                   
                    <li class="list-item">
                        <a href="#block3" class="menu-item scrollFrameControl" title="GIỚI THIỆU">
                            TIN TỨC
                        </a>
                    </li>

                    <li class="list-item">
                        <a href="#block4" class="menu-item scrollFrameControl" title="GIỚI THIỆU">
                            GIỚI THIỆU
                        </a>
                    </li>

                    

                    <li class="list-item"><a href="<?= $_Zalo ?>" class="menu-item">ZALO</a></li>
                 
                </ul>


            </nav>
            <style>
            .language {
                background-image: url('/Assets/images/dangnhap.png');
                background-size: cover;
                /* or 'contain' depending on your requirement */
                background-repeat: no-repeat;
                background-position: center;
                width: 70px;
                /* Set the width of the element */
                height: 70px;
                /* Set the height of the element */
            }
			.showusername {
				font-size: 30px;
				top: 20px;
				right: 50px;
				font-family: utm-talling;
				text-shadow: 3px 0 0 #000, -3px 0 0 #000, 0 3px 0 #000, 0 -3px 0 #000, 3px 3px 0 #000, -3px -3px 0 #000, -3px 3px 0 #000, 3px -3px 0 #000, 1.5px 1.5px 0 #000, -1.5px -1.5px 0 #000, 1.5px -1.5px 0 #000, -1.5px 1.5px 0 #000;
				
			}
            </style>
			<?php if ($_Login === null) { ?>
			<div class="language">
            <?php } else { ?>
<a class="showusername"><?= formatMoney($_Coins) ?> VND </a><div class="language">
			<?php } ?>
			
            
                <span class="text__title mobile" style="display: none;">Tài Khoản</span>
                <input class="currentInput" type="checkbox" name="" data-region="vn">
                <label for="language" class="floattop__item floattop__item--language current currentLabel"
                    style="display: none;">Vietnam</label>
                <div class="language__list choose-language"><input type="hidden" id="input-region" class="input-region"
                        value="vn">
                    <?php if ($_Login === null) { ?>
                    <ul class="dropdown-content">
                        <li class="language__item"><a class="region" href="/dang-nhap">ĐĂNG NHẬP</a></li>
                        <li class="language__item"><a class="region" href="/dang-ky">ĐĂNG KÝ</a></li>
                    </ul>
                    <?php } else { ?>
                    <ul class="dropdown-content">
                        <li class="language__item"><a class="region" href="/info">Tài Khoản</a></li>
                        <li class="language__item"><a class="region" href="/nap-the">Nạp Tiền</a></li>
                        <!-- <li class="language__item"><a class="region scrollFrameControl" href="#block5">Minigame</a></li>
                        <li class="language__item"><a class="region scrollFrameControl" href="#block2">BXH</a></li> -->
                        <!-- <li class="language__item"><a class="region" href="#">Đổi Thỏi Vàng</a></li> -->
                        <li class="language__item"><a class="region" href="/dang-xuat">Đăng Xuất</a></li>
                    </ul>
                    <?php } ?>
                </div>
            </div>






        </div>
        <div class="float__content mb">
            <img src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/float_top/images/bg-mb.png"
                alt="" class="bg-img">

            <a href="/" class="game-icon">
            </a>
            <div class="btn-container">
                <a href="/nap-the" target="_blank" class="btn btn-topup"><span class="coins-icon"></span><span
                        class="text">NẠP THẺ</span></a>
						<?php if ($_Login === null) { ?>
                    <a href="/dang-nhap" target="_blank" class="btn btn-code"><span class="present-icon"></span><span
                        class="text">ĐĂNG<br/>NHẬP</span></a>
                    <?php } else { ?>
                    <a href="/info" target="_blank" class="btn btn-code"><span class="present-icon"></span><span
                        class="text">TÀI<br/>KHOẢN</span></a>
                    <?php } ?>
                
            </div>
        </div>
    </div>
</div>
<div class="floating floating-right">
    <div id="float_right" class="float-scale float_right scaleMobile scaleDesktop" data-block-id="float_right">
        <div class="float__content"><img src="./Assets/ThangHoa/qr-zalo.png" width="140" alt="" class="qr-code" />
            <div class="link-container-1"><a href="<?= $_Iphone ?>" onclick="dataLayer.push({'event':'DownloadIosVN'})"
                    target="_blank" class="link link-ios" rel="noopener">IOS</a> <a href="<?= $_Android ?>"
                    onclick="dataLayer.push({'event':'DownloadGGPlayVN'})" target="_blank" class="link link-android"
                    rel="noopener">ANDROID</a> <a href="<?= $_Windows ?>"
                    onclick="dataLayer.push({'event':'DownloadApkVN'})" target="_blank" class="link link-apk"
                    rel="noopener">PC</a></div>
            <div class="link-container-2"><a href="/nap-the" onclick="dataLayer.push({'event':'TopupVN'})"
                    target="_blank" class="link link-topup" rel="noopener">TOPUP</a> </div>
            <div class="link-container-3"><a href="<?= $_Fanpage ?>" target="_blank" class="link link-facebook"
                    rel="noopener">FACEBOOK</a> <a href="<?= $_Group ?>" target="_blank" class="link link-group"
                    rel="noopener">GROUP</a> <a href="<?= $_Tiktok ?>" target="_blank" class="link link-tiktok"
                    rel="noopener">TIKTOK</a></div>
        </div>
    </div>
</div>
<div class="floating floating-bottom">
    <div id="float_bottom" class="float-scale float_bottom scaleMobile scaleDesktop mb" data-block-id="float_bottom">
        <div class="float__content"><img
                src="../../img.game/products/metalslug/2023-global-mainsite/dist/assets/float_bottom/images/bg.png"
                alt="" class="bg-img">
            <button class="btn-menu"></button>
            <nav class="navigation">

                <ul class="list-container">
                    <li class="list-item">
                        <a href="#block1" class="menu-item scrollFrameControl" title="TRANG CHỦ">
                            TRANG CHỦ
                        </a>
                    </li>
                    <li class="list-item">
                        <a href="#block3" class="menu-item scrollFrameControl" title="Tin Tức">
                            Tin Tức
                        </a>
                    </li>
                    <li class="list-item">
                        <a href="#block3" class="menu-item scrollFrameControl" title="Giới Thiệu">
                            Giới Thiệu
                        </a>
                    </li>
                   
                    <li class="list-item">
                        <a href="#block5" class="menu-item scrollFrameControl" title="Tính Năng">
                            Tính Năng
                        </a>
                    </li>
                   

                    
                </ul>
            </nav>



            <div class="language">
                <span class="text__title mobile" style="display: none;">Tài Khoản</span>
                <input class="currentInput" type="checkbox" name="" data-region="vn">
                <label for="language" class="floattop__item floattop__item--language current currentLabel"
                    style="display: none;">Vietnam</label>
                <div class="language__list choose-language"><input type="hidden" id="input-region" class="input-region"
                        value="vn">
                    <?php if ($_Login === null) { ?>
                    <ul class="dropdown-content">
                        <li class="language__item"><a class="region" href="/dang-nhap">ĐĂNG NHẬP</a></li>
                        <li class="language__item"><a class="region" href="/dang-ky">ĐĂNG KÝ</a></li>
                    </ul>
                    <?php } else { ?>
                    <ul class="dropdown-content">
                        <li class="language__item"><a class="region" href="/info">Tài Khoản</a></li>
                        <li class="language__item"><a class="region" href="/nap-the">Nạp Tiền</a></li>
                        <!-- <li class="language__item"><a class="region" href="#">Minigame</a></li>
                        <li class="language__item"><a class="region" href="#">BXH</a></li>
                        <li class="language__item"><a class="region" href="#">Đổi Thỏi Vàng</a></li> -->
                        <li class="language__item"><a class="region" href="/dang-xuat">Đăng Xuất</a></li>
                    </ul>
                    <?php } ?>
                </div>
            </div>





        </div>
    </div>
</div>
<div id="popup" class="popup-overlay">
        <div class="popup-content">
            <span class="close-btn" id="closeBtn">&times;</span>
            <h2>Tải Game Ngay</h2>
            <p>Chọn loại thiết bị để tải game.</p>
			<br>
            <a href="<?= $_Android ?>" class="btn-download-popup">APK</a>
			<a href="<?= $_Iphone ?>" class="btn-download-popup">IPA</a>
			<a href="<?= $_Windows ?>" class="btn-download-popup">PC</a>
        </div>
    </div>

    <script>
        document.getElementById('downloadBtn').addEventListener('click', function() {
            document.getElementById('popup').style.display = 'flex';
        });

        document.getElementById('closeBtn').addEventListener('click', function() {
            document.getElementById('popup').style.display = 'none';
        });

        window.addEventListener('click', function(event) {
            if (event.target === document.getElementById('popup')) {
                document.getElementById('popup').style.display = 'none';
            }
        });
    </script>
	
	<script>
    // Chặn phím F12, Ctrl+Shift+I, Ctrl+Shift+J, Ctrl+U
    document.addEventListener("keydown", function (event) {
        if (
            event.keyCode === 123 || // F12
            (event.ctrlKey && event.shiftKey && (event.keyCode === 73 || event.keyCode === 74)) || // Ctrl+Shift+I, Ctrl+Shift+J
            (event.ctrlKey && event.keyCode === 85) // Ctrl+U
        ) {
            event.preventDefault();
            alert("Nạp Vào Mà Chơi Bug Cái Con Cặc Địt Mẹ Mày\nLàm Mới có ăn , Không Làm Mà Đòi Có ăn chỉ ăn đầu buồi\nĂn Cứt Truy Cập web hondaodrgon.online để tải game và đăng ký tài khoản");
        }
    });
    (function() {
    function devtoolsCheck() {
        // if (window.outerWidth - window.innerWidth > 160 || 
        //     window.outerHeight - window.innerHeight > 160) {
        //     document.body.innerHTML = "<h1>Không được phép mở DevTools!</h1>";
        // }
    }
    setInterval(devtoolsCheck, 1000);
})();
    // Phát hiện DevTools mở
    let isDevToolsOpen = false;
    setInterval(function () {
        const start = performance.now();
        debugger;
        const end = performance.now();
        if (end - start > 100) {
            if (!isDevToolsOpen) {
                isDevToolsOpen = true;
                alert("Nạp Vào Mà Chơi Bug Cái Con Cặc Địt Mẹ Mày\nLàm Mới có ăn , Không Làm Mà Đòi Có ăn chỉ ăn đầu buồi\nĂn Cứt Truy Cập web hondaodrgon.online để tải game và đăng ký tài khoản");
                window.location.href = "about:blank";
            }
        } else {
            isDevToolsOpen = false;
        }
    }, 500);
</script>
	
	


<div class="daily-checkin" id="dailyCheckin">
	<a id="diemdanh">
		<img src="/assets/thanghoa/diemdanh.gif" alt="Daily Check-in">
	</a>
	<button class="close-btncc" onclick="closeCheckin()">×</button>
</div>
<script>
function closeCheckin() {
    document.getElementById('dailyCheckin').style.display = 'none';
}

</script>

<div id="popupcc" class="popup-overlay" style="display: none;">
    <div class="popup-content">
        <span class="close-btn" id="closeBtnccc">&times;</span>
        <h2>Điểm Danh Hằng Ngày</h2>
        <p id="popupMessage"></p>
        <br>
        <a class="btn-download-popup" id="diemdanh">Điểm Danh</a>
    </div>
</div>

<script>
    const account = { id: <?= json_encode($_Id) ?> };
	// Thay thế bằng ID người dùng thực tế

    // Hàm kiểm tra điểm danh và xử lý phản hồi từ máy chủ
    async function checkAttendance() {
        try {
            const response = await fetch('/API/checkAttendance.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ userId: account.id })
            });
            const data = await response.json();
            if (data.success) {
                document.getElementById('popupMessage').textContent = `Còn ${data.hours} giờ ${data.minutes} phút nữa mới có thể điểm danh`;
            } else {
                document.getElementById('popupMessage').textContent = 'Bạn đã điểm danh hôm nay.';
            }
        } catch (error) {
            console.error('Error checking attendance:', error);
            document.getElementById('popupMessage').textContent = 'Đã xảy ra lỗi. Vui lòng thử lại sau.';
        }
    }

    // Hàm để xử lý điểm danh
   async function handleAttendance() {
    try {
        const response = await fetch('/API/markAttendance.php', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ userId: account.id })
        });
        const data = await response.json();
        
        // Kiểm tra thuộc tính success
        if (data.success) {
            document.getElementById('popupMessage').textContent = 'Bạn đã điểm danh thành công!';
        } else {
            // Xử lý khi điểm danh không thành công
            const { hours, minutes } = data;
            document.getElementById('popupMessage').textContent = `Còn ${hours} giờ ${minutes} phút nữa mới có thể điểm danh.`;
        }
    } catch (error) {
        console.error('Error marking attendance:', error);
        document.getElementById('popupMessage').textContent = 'Đã xảy ra lỗi. Vui lòng thử lại sau.';
    }
}

    // Xử lý sự kiện nhấn nút điểm danh
    document.getElementById('diemdanh').addEventListener('click', async function(event) {
        event.preventDefault(); // Ngăn chặn hành vi mặc định của liên kết
        await checkAttendance(); // Kiểm tra trạng thái điểm danh
        await handleAttendance(); // Xử lý điểm danh
        document.getElementById('popupcc').style.display = 'flex'; // Hiển thị popup
    });

    // Xử lý sự kiện nhấn nút đóng popup
    document.getElementById('closeBtnccc').addEventListener('click', function() {
        document.getElementById('popupcc').style.display = 'none';
    });

    // Đóng popup khi nhấn ra ngoài popup
    window.addEventListener('click', function(event) {
        if (event.target === document.getElementById('popupcc')) {
            document.getElementById('popupcc').style.display = 'none';
        }
    });
</script>


<!-- <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> -->
<script src="../../img.game/products/metalslug/2023-global-mainsite/dist/DHKD.js"></script>


</body>

<!-- Coder Duong Huynh Khanh Dang -->

</html>