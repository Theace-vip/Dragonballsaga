const swal = (...props) => Swal.fire(...props);

$('.form-auth').ajaxForm({
    url: '../ajax.php',
    method: 'POST',
    dataType: 'json',
    success: (res) => {
        !res.success ? swal('Thông Báo', res.error, 'error') && grecaptcha.reset() : swal('Thông Báo', res.success, 'success') && setTimeout(() => window.location.reload(), 1500);
    }
})

$('.cancel-password2').click(function (e) {
    swal({
        title: 'Xác nhận hủy mật khẩu cấp 2',
        input: 'text',
        inputPlaceholder: 'Mật khẩu cấp 2 hiện tại',
        inputAttributes: {
            autocapitalize: 'off'
        },
        showCancelButton: true,
        confirmButtonText: 'Hủy ngay',
        confirmButtonColor: '#d73814',
        showLoaderOnConfirm: true,
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '../ajax.php',
                method: 'POST',
                dataType: 'json',
                data: {
                    action: 'cancel-password2',
                    password2: result.value
                },
                success: (res) => {
                    !res.success ? swal('Thông Báo', res.error, 'error') : swal('Thông Báo', res.success, 'success') && setTimeout(() => window.location.reload(), 1500);
                }
            })
        }
    })
})

$('form[name="reCharge"]').ajaxForm({
    url: '../ajax.php',
    method: 'POST',
    dataType: 'json',
    data: {
        action: 'reCharge'
    },
    success: (res) => {
        !res.success ? swal('Thông Báo', res.error, 'error') && grecaptcha.reset() : swal('Thông Báo', res.success, 'success') && setTimeout(() => window.location.reload(), 1500);
    }
})

$('.ajaxForm').ajaxForm({
    url: '../ajax.php',
    method: 'POST',
    dataType: 'json',
    success: (res) => {
        !res.success ? swal('Thông Báo', res.error, 'error') : swal('Thông Báo', res.success, 'success') && setTimeout(() => window.location.reload(), 1500);
    }
})