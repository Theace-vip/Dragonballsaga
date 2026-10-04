<?php
#Duong Huynh Khanh Dang
include '../DHKD/Connections.php';
include '../DHKD/Session.php';
include '../DHKD/Configs.php';
if (isset($_FixWeb) && $_FixWeb == 1) {
    echo "Máy chủ đang bảo trì website. Vui lòng chờ nhé!";
    exit;
}

if (!isset($_Login) || $_Login === null) {
    // Nếu chưa đăng nhập, chuyển hướng về trang chủ
    echo "<script>window.location.href = '/dang-nhap';</script>";
    exit;
}
$thongbao = isset($thongbao) ? $thongbao : '';
?>
<!DOCTYPE html>
<html>
<style>
    .alert.success {
        color: green;
    }

    .alert.error {
        color: tomato;
    }
</style>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="robots" content="index,follow" />
    <meta name="revisit-after" content="1days" />
    <title><?= htmlspecialchars($_Title) ?></title>
    <meta name="description" content="<?= htmlspecialchars($_Description) ?>" />
    <meta property="og:title" content="<?= htmlspecialchars($_Title) ?>" />
    <meta name="robot" content="index,follow" />
    <meta name="googlebot" content="index,follow" />
    <meta property="og:image" content="../../img.game/products/metalslug/share-mainsite/thumb.jpg" />
    <meta property="og:description" content="<?= htmlspecialchars($_Description) ?>" />
    <meta property="og:type" content="website" />
    <meta name="keywords" content="<?= htmlspecialchars($_Servername) ?>, nro, lậu, Dragon ball, game dragon ball, songoku, vegeta, quy lão tiên sinh, game dragon ball mobile, Chú Bé Rồng Online,ngoc rong mobile, game ngoc rong, game 7 vien ngoc rong, game bay vien ngoc rong">

    <link rel="shortcut icon" href="./Assets/favicon.png" />
    <link rel="stylesheet" type="text/css" href="/Assets/v2/css/lib/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="/Assets/v2/css/select2.min.css" />
    <link rel="stylesheet" type="text/css" href="/Assets/v2/css/style.css?v=1721052870" />
    <script type="text/javascript" src="/Assets/v2/js/jquery-3.4.1.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" crossorigin="anonymous"></script>
    <script type="text/javascript" src="/Assets/v2/js/bootstrap.min.js"></script>
</head>

<body class="bg-gray">
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
                        <span><?= htmlspecialchars($_ServerName) ?></span>
                    </a>
                    <!-- List other games -->
                    <a class="float-right" style="padding-top:5px; text-decoration:none;" href="/lich-su">Lịch sử giao dịch</a>
                </div>
                <div class="modal" id="phone-popup" aria-modal="true">
                    <div class="modal-dialog modal-sm">
                        <div class="modal-content">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                            <div class="modal-body">
                                <div class="container" style="margin-top:25px;">
                                    <div class="row">
                                        <div class="col-12">
                                            <input type="tel" class="form-control" name="phone" id="phone" placeholder="Số điện thoại" />
                                            <div class="error d-none" style="color: red;"></div>
                                        </div>
                                        <div class="col-12" style="margin-top: 15px;">
                                            <button type="button" class="btn btn-primary btn-block" data-href="/site/history/verify" onclick="checkPhone(this)">Tìm kiếm</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <input type="hidden" value="66952ec6993bdf0f0a49cd5a" id="PAYGATE_ID" />
        <div class="payment-form">
            <div class="container">
                <form id="msform">
                    <ul id="progressbar">
                        <li class="active" id="tab_payment_role"><span class="step">1</span><strong>Thông tin nhân vật</strong></li>
                        <li id="tab_payment_package"><span class="step">2</span><strong>Chọn gói nạp</strong></li>
                        <li id="tab_payment_method"><span class="step">3</span><strong>Phương thức thanh toán</strong></li>
                        <li id="tab_payment_pay"><span class="step">4</span><strong>Thanh toán</strong></li>
                    </ul>                    
                    <fieldset class="role">
                    <?php echo $thongbao; ?>
                        <div class="form-card" id="__game_role" data-href="/API/NapUser">
                            <div class="form-group">
                                <label>Chọn máy chủ</label>
                                <select id="serverID" name="serverID" class="form-control">
                                    <option><?= $_ServerName ?></option>
                                </select>
                            <!-- </div>
                            <div class="form-group">
                                <label>Nhập Tên Game&nbsp;</label>
                                <input type="text" id="role" name="role" class="form-control" onkeypress="return event.keyCode != 13;" onchange="refresh_form(this)" />
                            </div> -->
                        </div>
                        <!-- New div for displaying the message -->
                        <div id="role-message" class="alert alert-info d-none"></div>
                        <div class="form-action">
                            <button type="button" class="btn btn-primary action-button w100" id="find-role" data-game="zenlesszonezero" onclick="search_role(this);">Tìm kiếm</button>
                        </div>
                    </fieldset>
                    <fieldset class="package" id="list-package"></fieldset>
                    <fieldset class="method" id="list-method"></fieldset>
                    <fieldset class="payment" id="payment-form"></fieldset>
                </form>

            </div>
        </div>
        <!-- VOUCHER -->
        <div id="show_voucher" class="modal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">Chọn mã voucher muốn sử dụng</h4>
                    </div>
                    <div class="modal-body"></div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-voucher btn-block" disabled onclick="used_voucher(this)" data-api="/game/checkVoucher/zenlesszonezero">Sử dụng voucher</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="loading"><img src="/img.game/assets/ic-loading.gif"></div>
    <script type="text/javascript">
        
        function goto_ads(_ads) {
            var url = $(_ads).attr('data-href');
            if (url !== '') {
                window.location.href = url;
            }
        }



        function findMethod(_item, option = 'normal') {
            $('.loading').toggleClass('show');
            var find_method_url = $('#find-method').attr('data-href');
            if (option === 'next_pay') {
                var role_id = $('#__game_role').find('#role_id option:selected').val();
            } else {
                var role_id = $(_item).val();
            }
            if (role_id === '') {
                $('#__game_role').find('#role_id-error').html('Bạn chưa chọn nhân vật');
                $('#__game_role').find('#role_id-error').removeClass('d-none');
                $('.loading').toggleClass('show');
            } else {
                var role_name = $('#__game_role').find('#role_id option:selected').text();
                var paygateID = $('#PAYGATE_ID').val();
                var dataPost = {
                    paygate_id: paygateID,
                    role_id: role_id,
                    role_name: role_name
                };
                $.post(find_method_url, dataPost, function(res) {
                    $('.loading').toggleClass('show');
                    if (res.status === false) {
                        window.location.reload();
                    } else {
                        $('#list-method').html(res);
                        $('#msform').find('fieldset').hide();
                        $('fieldset#list-method').show().css({
                            'opacity': 1
                        });
                        $('#progressbar li').eq(2).addClass('active');
                        $('html,body').scrollTop($('#msform').offset().top - 90);
                    }
                });
            }
        }

        function findPackage(_item, option = 'normal') {
            $('.loading').toggleClass('show');
            var find_package_url = $('#find-package').attr('data-href');
            if (option === 'next_pay') {
                var role_id = $('#__game_role').find('#role_id option:selected').val();
            } else {
                var role_id = $(_item).val();
            }
            if (role_id === '') {
                $('#__game_role').find('#role_id-error').html('Bạn chưa chọn nhân vật');
                $('#__game_role').find('#role_id-error').removeClass('d-none');
                $('.loading').toggleClass('show');
            } else {
                var role_name = $('#__game_role').find('#role_id option:selected').text();
                var paygateID = $('#PAYGATE_ID').val();
                var dataPost = {
                    paygate_id: paygateID,
                    role_id: role_id,
                    role_name: role_name
                };
                $.post(find_package_url, dataPost, function(res) {
                    $('.loading').toggleClass('show');
                    if (res.status === false) {
                        /* window.location.reload(); */
                        $('#__game_role').find('#role_id-error').html(res.message);
                        $('#__game_role').find('#role_id-error').removeClass('d-none');
                    } else {
                        $('#__game_role').find('#role_id-error').addClass('d-none');
                        $('#list-package').html(res);
                        $('#msform').find('fieldset').hide();
                        $('fieldset#list-package').show().css({
                            'opacity': 1
                        });
                        $('#progressbar li').eq(1).addClass('active');
                    }
                });
            }
        }

        function show_voucher() {
            if ($('#show_voucher .modal-body').text().length == 0) {
                var clone = $('.voucher_list').html();
                $('#show_voucher .modal-body').html(clone);
                $('.voucher_block').remove();
            }
            $('#show_voucher').modal('show');
            setTimeout(function() {
                $('#show_voucher input[type=radio]').on("change", function() {
                    if ($(this).is(":checked")) {
                        $('.btn-voucher').prop('disabled', false);
                    }
                });
            }, 300);
        }

        function used_voucher(_item) {
            $('#voucher_info').addClass('d-none'); /* var api = $(_item).attr('data-api');var voucher_code = $('#show_voucher').find("input[name='voucher_usage']:checked").val();if(typeof voucher_code === 'undefined' || voucher_code === '') {$('#show_voucher').modal('hide');} else {$.post(api, {code:voucher_code}, function(res) {if(res.status === false) {console.log(res);} else {$('#show_voucher').modal('hide');$('#voucher_info').find('.message').html(res.message);$('#voucher_info').removeClass('d-none');}});} */
        }

        function refresh_form(_item) {
            $('#find-package').remove();
            $('.voucher_block').remove();
            $('#find-role').attr('onclick', 'search_role(this)');
            $('#find-role').text('Tìm kiếm');
        }

        function select_package(_item, is_popup = false) {
            $('#__game_package').find('.error').addClass('d-none');
            var url = $('#__game_package').attr('data-href');
            var package_id = $(_item).attr('data-packageid');
            var paygateID = $('#PAYGATE_ID').val();
            $('.loading').toggleClass('show');

            $.post(url, {
                paygate_id: paygateID,
                source_package_id: package_id
            }, function(res) {
                $('.loading').toggleClass('show');

                if (typeof res === 'string') {
                    res = JSON.parse(res);
                }

                if (typeof res.status !== 'undefined' && !res.status) {
                    if (typeof res.key !== 'undefined') {
                        $('#' + res.key).html(res.message);
                        $('#' + res.key).removeClass('d-none');
                    }
                    if (typeof res.reload !== 'undefined' && res.reload === true) {
                        window.location.reload();
                    }
                } else {
                    $('#list-method').html(res.html);
                    if (is_popup === 'is_popup') {
                        $('#payment-confirm').attr('disabled', 'true');
                        show_popup('next', package_id);
                    } else {
                        $('#msform').find('fieldset').hide();
                        $('fieldset#list-method').show().css({
                            'opacity': 1
                        });
                        $('#progressbar li').eq(2).addClass('active');
                    }
                }
            });
        }


        function show_popup(option, packageId = '') {
            $('.loading').toggleClass('show');
            var html = $('#popup-info_' + packageId).html();
            $('#package_detail').find('.modal-body').html(html);
            var aLink = '<button type="button" class="btn btn-primary next action-button w100" onclick="nextShowForm()">Xác nhận</button>';
            $('#package_detail').find('.modal-footer').html(aLink);
            $('#package_detail').modal('toggle');
            $('.loading').toggleClass('show');
        }

        function nextShowForm() {
            $('.loading').toggleClass('show');
            $('#package_detail').modal('hide');
            $('#msform').find('fieldset').hide();
            $('fieldset#list-method').show().css({
                'opacity': 1
            });
            $('#progressbar li').eq(2).addClass('active');
            $('.loading').toggleClass('show');
            $('html,body').scrollTop($('#msform').offset().top - 90);
        }

        function select_method(_item, is_popup = false) {
            $('#__method_group').find('.error').addClass('d-none');
            var url = $('#__game_method').attr('data-href');
            var source_package_id = $(_item).attr('data-source-package');
            var method = $(_item).attr('data-method');
            var package_id = $(_item).attr('data-packageid');
            var paygateID = $('#PAYGATE_ID').val();
            var dataForm = {
                paygate_id: paygateID,
                source_package_id: source_package_id,
                method: method,
                package_id: package_id
            };
            $('.loading').toggleClass('show');
            $.post(url, dataForm, function(res) {
                if (typeof res.status !== 'undefined' && !res.status) {
                    if (typeof res.reload !== 'undefined' && res.reload === true) {
                        window.location.reload();
                    } else {
                        $('.loading').toggleClass('show');
                        $('#__method_group').find('.error').html(res.message);
                        $('#__method_group').find('.error').removeClass('d-none');
                    }
                } else {
                    if (typeof res.url !== 'undefined') {
                        window.location.href = res.url;
                    } else {
                        $('.loading').toggleClass('show');
                        $('#payment-form').html(res);
                        $('#msform').find('fieldset').hide();
                        $('fieldset#payment-form').show().css({
                            'opacity': 1
                        });
                        $('#progressbar li').eq(3).addClass('active');
                    }
                }
            });
        }
        $(document).ready(function() {
            $('[data-toggle=popover]').popover({
                html: true
            });
            $('.popover-dismiss').popover({
                trigger: 'focus'
            });
            $('.btn-cancel').click(function() {
                var api = $(this).attr('data-api');
                $.post(api, {
                    uncode: true
                }, function(res) {
                    if (res.status === false) {
                        console.log(res);
                    } else {
                        $('#voucher_info').removeClass('d-none');
                        $('#voucher_info').find('.message').html('');
                        $('#show_voucher input[type=radio]').prop('checked', false);
                        $('#show_voucher .btn-voucher').prop('disabled', true);
                    }
                });
            });
        });
    </script>
    <script type="text/javascript">
        function checkPhone(button) {
            const phone = $("#phone").val().trim();
            if (!phone) {
                alert("Vui lòng nhập số điện thoại của bạn!");
                return;
            }
            const href = $(button).data("href");
            window.location.href = `${href}?phone=${phone}`;
        }

        function search_role(_item) {
            $('.loading').toggleClass('show');
            $('#__game_role').find('.error').addClass('d-none');
            $('#__game_role').find('#role-message').addClass('d-none').removeClass('error'); // Reset the role message

            var api = $('#__game_role').attr('data-href');
            var role_id = $('#role').val();

            if (role_id === '') {
                $('#role-message').html("Bạn cần nhập tên tài khoản!").removeClass('d-none').addClass('error');
                $('.loading').toggleClass('show');
            } else {
                var game = $(_item).attr('data-game');
                var serverID = $('#serverID').val();
                var paygateID = $('#PAYGATE_ID').val();
                var dataForm = {
                    paygate_id: paygateID,
                    serverID: serverID,
                    role: role_id
                };

                if (game === 'conduongtolua' || game === 'copubphathien' || game === 'grailtale') {
                    var platform = $('#platform').val();
                    dataForm.platform = platform;
                }

                $.post(api, dataForm, function(res) {
                    $('.loading').toggleClass('show'); // Ẩn hiện loading nếu có

                    try {
                        // Kiểm tra xem phản hồi có phải là JSON hợp lệ hay không
                        var response = JSON.parse(res);

                        // Kiểm tra dữ liệu trong response và xử lý
                        if (response.status === false) {
                            $('#role-message')
                                .html(response.message) // Hiển thị thông báo "Tên nhân vật không tồn tại"
                                .removeClass('d-none')
                                .addClass('error');
                        } else {
                            // Nếu đúng, cập nhật giao diện với HTML từ server
                            $('#__game_role').find('#role-error').addClass('d-none');
                            $('#__game_role').append(response.html);

                            // Cập nhật thuộc tính và văn bản của nút
                            $('#find-role').attr('onclick', 'findPackage(this, "next_pay")');
                            $('#find-role').text('Tiếp tục');
                        }
                    } catch (e) {
                        // Nếu JSON không hợp lệ, in ra phản hồi và lỗi
                        console.log("Response is not valid JSON:", res);
                        console.error(e);
                    }
                });



            }
        }
    </script>
    <script type="text/javascript" src="/assets/v2/js/select2.min.js"></script>
    <script type="text/javascript" src="/assets/v2/js/lib-dist.js?v=1721186835"></script>
    
</body>

</html>