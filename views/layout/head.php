<!DOCTYPE html>
<html lang="vi">

<head>
    <!--CODE BY VMN-->
    <title><?php echo htmlspecialchars($defaultTitle); ?></title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta property="og:type" content="website" />
    <meta property="og:title" content="<?php echo htmlspecialchars($defaultTitle); ?>" />
    <meta property="og:description" content="<?php echo htmlspecialchars($description); ?>" />
    <meta name="description" content="<?php echo htmlspecialchars($description); ?>">
    <meta name="keywords" content="<?php echo htmlspecialchars($keywords); ?>">
    <link rel="shortcut icon" href="<?php echo htmlspecialchars($favicon); ?>">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="../assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="../assets/css/tuanbinhcopy.css">
    <link rel="stylesheet" href="../assets/css/style.css">
    <link rel="stylesheet" href="../assets/css/summernote.min.css">
    <!-- THEME TU TIEN: HUYET NGUYET MA DAO (nen tang cuoi cung de override) -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet"
        href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@400;500;600;700&family=Noto+Serif+Display:wght@600;700;900&display=swap">
    <?php
    // Cache-busting theo thoi diem sua file: sua xong la client/Cloudflare lay ban moi ngay.
    $bm_v = function ($f) {
        $p = __DIR__ . '/../../assets/' . $f;
        return @filemtime($p) ?: '1';
    };
    ?>
    <link rel="stylesheet" href="../assets/css/tutien.css?v=<?php echo $bm_v('css/tutien.css'); ?>">
    <script defer src="../assets/js/tutien.js?v=<?php echo $bm_v('js/tutien.js'); ?>"></script>
    <script type="module" src="../assets/js/tutien3d.js?v=<?php echo $bm_v('js/tutien3d.js'); ?>"></script>
    <script src="../assets/js/sweetalert2@11.js"></script>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        /* ==== Logo chu NROKuRA dong (co hieu ung) ==== */
        .brand-logo {
            display: inline-block;
            font-family: 'Arial Black', Arial, sans-serif;
            font-size: 46px;
            font-weight: 900;
            letter-spacing: 4px;
            line-height: 1.05;
            padding: 2px 6px 4px;
            background: linear-gradient(100deg, #ff4e50 0%, #ff8a3d 18%, #ffe259 34%, #fff6b0 42%, #ffe259 50%, #ff8a3d 68%, #ff4e50 100%);
            background-size: 300% 100%;
            -webkit-background-clip: text;
            background-clip: text;
            -webkit-text-fill-color: transparent;
            color: transparent;
            filter: drop-shadow(0 3px 5px rgba(0, 0, 0, .45));
            animation: brandShine 5s linear infinite, brandIn .9s ease-out 1;
            text-decoration: none;
        }

        .brand-logo:hover {
            animation-play-state: paused;
        }

        .brand-logo.sm {
            font-size: 26px;
            letter-spacing: 2px;
        }

        .brand-slogan {
            font-size: 12px;
            letter-spacing: 3px;
            font-weight: bold;
            text-transform: uppercase;
            color: #0b3d36;
            animation: sloganIn 1.2s ease-out 1;
        }

        @keyframes brandShine {
            0% {
                background-position: 0% 50%;
            }

            100% {
                background-position: 300% 50%;
            }
        }

        @keyframes brandIn {
            0% {
                transform: scale(.86) translateY(8px);
                opacity: 0;
            }

            60% {
                transform: scale(1.04) translateY(0);
                opacity: 1;
            }

            100% {
                transform: scale(1);
                opacity: 1;
            }
        }

        /* Modal thong bao: giua man hinh + logo canh giữa */
        #Noti_Home .modal-header {
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 26px 16px;
        }

        #Noti_Home .modal-dialog {
            width: calc(100% - 24px);
            max-width: 540px;
            margin: 1rem auto;
        }

        #Noti_Home .modal-body {
            text-align: center;
            padding: 14px 16px 22px;
        }

        @media (max-width: 520px) {
            .brand-logo {
                font-size: 34px;
                letter-spacing: 2px;
            }

            .brand-logo.sm {
                font-size: 22px;
            }

            .brand-slogan {
                font-size: 10px;
                letter-spacing: 2px;
            }
        }

        @keyframes sloganIn {
            0% {
                opacity: 0;
                letter-spacing: 14px;
            }

            100% {
                opacity: 1;
                letter-spacing: 3px;
            }
        }

        .form-auth {
            width: 100%;
            max-width: 330px;
            padding: 15px;
            margin: 0 auto;
        }

        .form-auth .checkbox {
            font-weight: 400;
        }

        .form-auth .form-control {
            position: relative;
            box-sizing: border-box;
            height: auto;
            padding: 10px;
            font-size: 16px;
            margin-bottom: 5px;
            border-radius: 15px;
        }

        .form-auth .form-control:focus {
            z-index: 2;
        }

        .form-auth input[type="text"] {
            margin-bottom: 5px;
            border-radius: 15px;
        }

        .form-auth input[type="password"] {
            margin-bottom: 5px;
            border-radius: 15px;
        }
    </style>
</head>