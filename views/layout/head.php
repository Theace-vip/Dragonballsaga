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
    <link rel="shortcut icon" href="<?php echo htmlspecialchars($favicon); ?>" type="image/x-icon">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="../assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="../assets/css/tuanbinhcopy.css">
    <link rel="stylesheet" href="../assets/css/style.css">
    <link rel="stylesheet" href="../assets/css/summernote.min.css">
    <script src="../assets/js/sweetalert2@11.js"></script>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
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