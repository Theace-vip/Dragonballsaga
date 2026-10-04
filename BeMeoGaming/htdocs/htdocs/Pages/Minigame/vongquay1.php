<?php
#Duong Huynh Khanh Dang
include '../../DHKD/header.php';

$_ID_vongquay = "20"; // ID của vòng quay
$query = "SELECT * FROM `vongquay` WHERE `id` = :id AND `status` = 'true'";
$stmt = $conn->prepare($query);
$stmt->bindParam(':id', $_ID_vongquay, PDO::PARAM_INT);
$stmt->execute();
$row = $stmt->fetch(PDO::FETCH_ASSOC);

if (!$row) {
    die("<center><h1>Không Tìm Thấy Vòng Quay!</h1></center>");
}
?>

<head>
    <link rel="shortcut icon" href="./Assets/favicon.png" />
    <link rel="stylesheet" type="text/css" href="/Assets/v2/css/lib/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="/Assets/v2/css/select2.min.css" />
    <link rel="stylesheet" type="text/css" href="/Assets/v2/css/style.css?v=1721052870" />
    <link rel="stylesheet" type="text/css" href="https://mihoyo.gamota.com/Asset/v2/css/custom.css?v=1721052870" />
    <script type="text/javascript" src="/Assets/v2/js/jquery-3.4.1.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" crossorigin="anonymous"></script>
    <script type="text/javascript" src="/Assets/v2/js/bootstrap.min.js"></script>




    <header>
        <nav class="navbar navbar-expand-lg">
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
            <div class="navbar-logo"></div>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item"><a class="nav-link system-tooltip" href="/"><span>Trang Chủ</span></a></li>
                    <li class="nav-item"><a class="nav-link system-tooltip" href="#"><span>Hướng dẫn</span></a></li>
                </ul>
            </div>
        </nav>
    </header>
    <div class="main-content">
        <style type="text/css">
            .bg-gray {
                background: #fff;
            }

            
        </style>
        <div class="game-header">
            <div class="container">
                <div class="app-name dropdown">
                    <a role="button">
                        <img src="/assets/ThangHoa/game-icon.png" class="thumb app-pic">
                        <span>Ngọc Rồng Thăng Hoa</span>
                    </a>

                </div>
                <a class="float-right" style="padding-top:5px;text-decoration:none;" href="/lich-su">
                    <img src="/Assets/images/dangnhap.png">
                </a>
            </div>
        </div>


    </div>




    <!-- CSS Links -->
    <link href="https://fonts.googleapis.com/css?family=Pacifico|Work+Sans:400,300,600,400italic,700|Amatic+SC:400,700&subset=latin-ext" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Roboto|Roboto+Condensed" rel="stylesheet">
    <link rel="stylesheet" href="/assets/frontend/css/vongquay.css">
    <link rel="stylesheet" href="/assets/frontend/css/style.css">
    <link rel="stylesheet" href="/assets/frontend/css/util.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="/assets/frontend/theme/assets/global/plugins/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="/assets/frontend/theme/assets/global/plugins/simple-line-icons/simple-line-icons.min.css">
    <link rel="stylesheet" href="/assets/frontend/theme/assets/plugins/animate/animate.min.css">
    <link rel="stylesheet" href="/assets/frontend/theme/assets/plugins/magnific/magnific.css">
    <link rel="stylesheet" href="/assets/frontend/theme/assets/plugins/cubeportfolio/css/cubeportfolio.min.css">
    <link rel="stylesheet" href="/assets/frontend/theme/assets/plugins/fancybox/jquery.fancybox.css">
    <link rel="stylesheet" href="/assets/frontend/theme/assets/plugins/socicon/socicon.css">
    <link rel="stylesheet" href="/assets/frontend/theme/assets/plugins/bootstrap-social/bootstrap-social.css">
    <link rel="stylesheet" href="/assets/Scripts/sweetalert/sweetalert.css">
    <link rel="stylesheet" href="/assets/Scripts/loader/css.css">

    <!-- JavaScript Links -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="/assets/frontend/plugins/bootstrap/js/bootstrap.min.js"></script>
    <script src="/assets/frontend/plugins/owl-carousel/slider.js"></script>
    <script src="/assets/Scripts/bootstrap-filestyle.min.js"></script>
    <script src="/assets/Scripts/loader/pace.js"></script>
    <script src="/assets/Scripts/sweetalert/sweetalert.min.js"></script>
    <script src="/assets/Scripts/loadingoverlay/loadingoverlay.min.js"></script>
    <script src="/assets/Scripts/loadingoverlay/loadingoverlay_progress.min.js"></script>
    <script src="/assets/frontend/theme/assets/plugins/jquery-migrate.min.js"></script>
    <script src="/assets/frontend/theme/assets/plugins/jquery.easing.min.js"></script>
    <script src="/assets/frontend/theme/assets/plugins/reveal-animate/wow.js"></script>
    <script src="/assets/frontend/theme/assets/demos/default/js/scripts/reveal-animate/reveal-animate.js"></script>
    <script src="/assets/Scripts/client_0x2165x1.js"></script>
</head>
<style>
    #history-list {
        width: 100%;
        border-collapse: collapse;
    }

    #history-list th,
    #history-list td {
        border: 1px solid #ddd;
        padding: 8px;
    }

    #history-list th {
        background-color: #f2f2f2;
    }
</style>
<style>
    #rotate {
        border: 5px solid #ddd;
        border-radius: 50%;
        margin: 50px auto;
        transition: transform 11s cubic-bezier(0.19, 1, 0.22, 1);
    }

    #rank-list {
        width: 80%;
        margin: 20px auto;
        border-collapse: collapse;
    }

    #rank-list td,
    #rank-list th {
        border: 1px solid #ddd;
        padding: 8px;
    }

    #rank-list tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    #rank-list tr:hover {
        background-color: #ddd;
    }

    #rank-list th {
        padding-top: 12px;
        padding-bottom: 12px;
        text-align: left;
        background-color: #4CAF50;
        color: white;
    }
</style>
<header>
    <nav class="navbar navbar-expand-lg">
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
        <div class="navbar-logo"></div>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item"><a class="nav-link system-tooltip" href="/"><span>Trang Chủ</span></a></li>
                <li class="nav-item"><a class="nav-link system-tooltip" href="#"><span>Hướng dẫn</span></a></li>
            </ul>
        </div>
    </nav>
</header>
<input type="hidden" id="document" value="<?php echo htmlspecialchars($_ID_vongquay, ENT_QUOTES, 'UTF-8'); ?>">
<input type="hidden" id="csrf" value="<?php echo htmlspecialchars($csrfToken, ENT_QUOTES, 'UTF-8'); ?>">
<form id="form"></form>

<div class="c-content-title-1 pd50" style="margin-top: 50px;">
    <center>
        <h3 class="c-center c-font-uppercase c-font-bold"><?php echo htmlspecialchars($row['name'], ENT_QUOTES, 'UTF-8'); ?></h3>
        <b>
            <font color="red">Chú ý : <?php echo htmlspecialchars(compact_number($row['giatien']), ENT_QUOTES, 'UTF-8'); ?>/1 lần quay </font>
        </b>
    </center>
    <div class="c-line-center c-theme-bg"></div>
</div>

<div class="col-lg-6 col-md-12" style="margin: 0px;margin-bottom: 100px;">
    <div class="item item-left">
        <section class="rotation">
            <div class="play-spin">
                <a class="ani-zoom" id="start"><img src="/assets/images/minigame/btn-quay.png" alt="Play Center"></a>
                <div class="rotate" id="rotate">
                    <img style="width: 100%;max-width: 100%;opacity: 1;" src="<?php echo htmlspecialchars(vongquay_image($_ID_vongquay, 'image'), ENT_QUOTES, 'UTF-8'); ?>" alt="Play">
                </div>
            </div>
            <div class="text-center"></div>
        </section>
    </div>
</div>

<style type="text/css">
    .list-roll-inner {
        width: 85%;
        margin-top: 30px;
        max-height: 400px;
        overflow-y: scroll;
        margin: 0 auto;
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
</style>
<div class="col-lg-6 col-md-12 list-roll">
    <div class="btn-top">
        <a href="#" class="thele btn btn-success m-btn m-btn--custom m-btn--icon m-btn--pill">
            <span>
                <i class="la la-cloud-upload"></i>
                <span>Thể Lệ</span>
            </span>
        </a>
        <a href="/login" class="btn btn-success m-btn m-btn--custom m-btn--icon m-btn--pill">
            <span>
                <i class="la la-cloud-upload"></i>
                <span>Rút quà</span>
            </span>
        </a>
        <a href="/rubywheel/logacc/1281" class="btn btn-success m-btn m-btn--custom m-btn--icon m-btn--pill">
            <span>
                <i class="la la-cloud-upload"></i>
                <span>Lịch sử quay</span>
            </span>
        </a>
    </div>

    <div class="modal fade" id="theleModal" role="dialog" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" style="font-weight: bold;text-transform: uppercase;color: #FF0000;text-align: center">Thể Lệ</h4>
                </div>
                <div class="modal-body" style="font-family: helvetica, arial, sans-serif;">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn c-theme-btn c-btn-border-2x c-btn-square c-btn-bold c-btn-uppercase" data-dismiss="modal">Đóng</button>
                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        $(document).ready(function() {
            $(".thele").on("click", function() {
                $("#theleModal").modal('show');
            })
            $(".uytin").on("click", function() {
                $("#uytinModal").modal('show');
            })
        });
    </script>
    <div class="modal fade" id="uytinModal" role="dialog" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" style="font-weight: bold;text-transform: uppercase;color: #FF0000;text-align: center">Uy Tín</h4>
                </div>
                <div class="modal-body" style="font-family: helvetica, arial, sans-serif;">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn c-theme-btn c-btn-border-2x c-btn-square c-btn-bold c-btn-uppercase" data-dismiss="modal">Đóng</button>
                </div>
            </div>
        </div>
    </div>

    <style type="text/css">
        .list-roll-inner {
            width: 85%;
            margin-top: 30px;
            max-height: 400px;
            overflow-y: scroll;
            margin: 0 auto;
        }
    </style>

    <div class="c-content-title-1" style="margin: 21 auto">
        <h3 class="c-center c-font-uppercase c-font-bold">LƯỢT QUAY GẦN ĐÂY</h3>
    </div>

    <div id="rankingsm"></div>


</div>

<script>
    var bRotate = false;


    function rotateFn(angles, txt, type) {
        console.log('rotateFn called with angles:', angles); // Debugging
        if (bRotate) return;

        bRotate = true;
        $('#rotate').css({
            'transition': 'transform 11s cubic-bezier(0.19, 1, 0.22, 1)',
            'transform': 'rotate(' + (angles + 1800) + 'deg)'
        });

        setTimeout(function() {
            swal("Thành công!", txt, type);
            window.location.reload(); // Nếu quay thành công thì tải lại
            bRotate = false; // Cho phép quay lại
        }, 11000); // Thời gian quay phải trùng khớp với thời gian quay
    }

    $('#start').click(function() {
        console.log('Start button clicked'); // Debugging
        if (bRotate) return;

        $.ajax({
            type: 'post',
            dataType: "JSON",
            url: '/API/vongquay',
            data: {
                csrf: $('#csrf').val()
            },
            success: function(json) {
                console.log('AJAX response:', json); // Debugging

                if (json.status == 3) {
                    swal("Lỗi!", "Vui lòng đăng nhập để quay!", "error");
                } else if (json.status == 4) {
                    swal("Lỗi!", "Bạn Không Đủ Tiền Trong Tài Khoản Vui Lòng Nạp Thêm!", "error");
                } else if (json.status == 1) {
                    if (json.item > 0 && json.item < 9) {
                        rotateFn(json.location, json.msg, "success");
                    } else {
                        swal("Lỗi!", "Lỗi hệ thống!", "error");
                    }
                } else {
                    swal("Lỗi!", "Lỗi hệ thống!", "error");
                }
            },
            error: function(xhr, status, error) {
                console.log('AJAX error:', xhr.responseText); // Debugging
                swal("Lỗi!", "Có lỗi xảy ra, vui lòng thử lại!", "error");
            }
        });
    });



    fetch('/API/getlswheel.php')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            if (!Array.isArray(data)) {
                throw new Error('Data is not an array');
            }

            console.log(data); // Xem dữ liệu trong console

            let html = '<table class="rank_table">';
            html += '<thead><tr><th>Tài Khoản</th><th>Giải Thưởng</th><th>Thời Gian</th></tr></thead>';
            html += '<tbody>';

            data.forEach(item => {
                // Chuyển đổi thời gian từ epoch sang định dạng ngày giờ
                const time = new Date(item.time * 1000).toLocaleString();

                html += '<tr>';
                html += `<td>${item.username}</td>`;
                html += `<td>${item.mota}</td>`;
                html += `<td>${time}</td>`;
                html += '</tr>';
            });

            html += '</tbody></table>';
            document.getElementById('rankingsm').innerHTML = html;
        })
        .catch(error => {
            console.error('Lỗi khi lấy dữ liệu từ API:', error);
            document.getElementById('rankingsm').innerHTML = 'Không thể tải dữ liệu từ API. ' + error.message;
        });
</script>