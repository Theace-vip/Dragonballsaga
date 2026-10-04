<?php

$ip_sv = "127.0.0.1";
$port = "3306";
$user_sv = "root";
$pass_sv = "";

$dbname_sv = "hondaodragon";
// 	$dbname_sv1 = "nrosv1";
// $dbname_sv2 = "nrosv2";
// $dbname_sv3 = "nrosv3";

// GMT +7
date_default_timezone_set('Asia/Ho_Chi_Minh');
// $pdo = new PDO("mysql:host=$ip_sv;port=$port;dbname=$dbname_sv", $user_sv, $pass_sv);
try {
    // Create connection
    $dsn = "mysql:host=$ip_sv;port=$port;dbname=$dbname_sv;charset=utf8mb4";
    $conn = new PDO($dsn, $user_sv, $pass_sv);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    // $dsn1 = "mysql:host=$ip_sv;port=$port;dbname=$dbname_sv1;charset=utf8mb4";
    // $conn1 = new PDO($dsn1, $user_sv, $pass_sv);
    // $conn1->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    // $dsn2 = "mysql:host=$ip_sv;port=$port;dbname=$dbname_sv2;charset=utf8mb4";
    // $conn2 = new PDO($dsn2, $user_sv, $pass_sv);
    // $conn2->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    //  $dsn3 = "mysql:host=$ip_sv;port=$port;dbname=$dbname_sv3;charset=utf8mb4";
    // $conn3 = new PDO($dsn3, $user_sv, $pass_sv);
    // $conn3->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
} catch (PDOException $e) {
    die("Connection failed 1!");
}
$Giftcode = $conn->query("SELECT * FROM giftcode")->fetchAll(PDO::FETCH_ASSOC);
?>
