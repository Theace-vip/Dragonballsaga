<?php
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Connections.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Session.php';
include $_SERVER['DOCUMENT_ROOT'] . '/DHKD/Configs.php';


if (isset($_GET['status']) && isset($_GET['request_id'])) {
    $tranid = $_GET['request_id'];
    $status = $_GET['status'];
    $am_real = $_GET['amount'];

    $stmt = $conn->prepare("SELECT * FROM `cvh_recharge` WHERE `tranid` = :tranid");
    $stmt->bindValue(":tranid", $tranid);
    $stmt->execute();
    $result = $stmt->fetch();

    if ($result) {
        $account_id = $result['account_id'];
        $monney = $result['amount'];

        if ($status == 1) {
            $update = $conn->prepare("UPDATE `account` SET `temp_vnd` = `temp_vnd` + :monney, `tongnap` = `tongnap` + :monney WHERE `id` = :account_id");
            $update->bindValue(":monney", $monney);
            $update->bindValue(":account_id", $account_id);
            $update->execute();

            $update2 = $conn->prepare("UPDATE `cvh_recharge` SET `status` = '1', `amount_real` = :am_real WHERE `tranid` = :tranid");
            $update2->bindValue(":am_real", $am_real);
            $update2->bindValue(":tranid", $tranid);
            $update2->execute();
        } else {
            $update2 = $conn->prepare("UPDATE `cvh_recharge` SET `status` = '2', `amount_real` = 0 WHERE `tranid` = :tranid");
            $update2->bindValue(":tranid", $tranid);
            $update2->execute();
        }
    }
}
?>