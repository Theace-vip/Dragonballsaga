<?php
#Duong Huynh Khanh Dang
include $_SERVER['DOCUMENT_ROOT'].'/DHKD/Connections.php';
include $_SERVER['DOCUMENT_ROOT'].'/DHKD/Session.php';
include $_SERVER['DOCUMENT_ROOT'].'/DHKD/Configs.php';
include $_SERVER['DOCUMENT_ROOT'].'/DHKD/Core.php';
$kun = new System;
$kundeptrai = "";
$location = "";
$id = "20";

$vongquay = mysqli_fetch_assoc(mysqli_query($kun->connect_db(), "SELECT * FROM `vongquay` WHERE `id`='".$id."'"));
if(!$vongquay) die(json_encode(array('status' => false,'item' => '','location' => '', 'msg' => 'Lỗi hệ thống!')));


// 1 - Thành Công
// 3 - Chưa đăng nhập
// 4 - ko đủ tiền


if($_Login === null){
	$status = 3; // false
	$msg = 'Ops! Bạn Chưa Đăng Nhập!';
	
}else if($_Coins < $vongquay['giatien']){
	$status = 4; // false
	$msg = 'Bạn không đủ tiền trong tài khoản, vui lòng nạp thêm để quay!';
}else{

	require $_SERVER['DOCUMENT_ROOT'].'/Assets/lib/BiasedRandom/Element.php';
	require $_SERVER['DOCUMENT_ROOT'].'/Assets/lib/BiasedRandom/Randomizer.php';

	  $randomizer = new Randomizer();
  
	  $randomizer          ->add( new Element('1', $kun->vongquay_gift($id, 1, 'tyle'))) 
	                       ->add( new Element('2', $kun->vongquay_gift($id, 2, 'tyle'))) 
	                       ->add( new Element('3', $kun->vongquay_gift($id, 3, 'tyle')))
	                       ->add( new Element('4', $kun->vongquay_gift($id, 4, 'tyle'))) 
	                       ->add( new Element('5', $kun->vongquay_gift($id, 5, 'tyle')))
	                       ->add( new Element('6', $kun->vongquay_gift($id, 6, 'tyle'))) 
	                       ->add( new Element('7', $kun->vongquay_gift($id, 7, 'tyle'))) 
	                       ->add( new Element('8', $kun->vongquay_gift($id, 8, 'tyle'))) 
	                          ;
      	$kundeptrai = $randomizer->get();       
 	
		switch ($kundeptrai) {
		    case '1':
		    $location = 360;
		        break;
		    case '2':
		    $location = 320;        
		        break;
		    case '3':
		    $location = 270;        
		        break;
		    case '4':
		    $location = 230;        
		        break;
		    case '5':
		    $location = 180;       
		        break;
		    case '6':
		    $location = 130;        
		        break;
		    case '7':
		    $location = 85;        
		        break;
		    case '8':
		    $location = 44;        
		        break;
		    default:
		    $location = "";   
		        break;
		}

	$status = 1; // true

    $msg = $kun->vongquay_gift($id, $kundeptrai, 'text');
    	//UPDATE Kimcuong vào acc
    mysqli_query($kun->connect_db(),"UPDATE `account` SET `thoi_vang` = `thoi_vang` + '".$kun->vongquay_gift($id, $kundeptrai, 'kimcuong')."' WHERE `username` = '".$_Users."'");
    	// Update Lần sử dụng vòng quay
    mysqli_query($kun->connect_db(), "UPDATE `vongquay` SET `sudung` = `sudung` + 1 WHERE `id` = '".$id."'");
    	// Update vào lịch sử user
    mysqli_query($kun->connect_db(), "INSERT INTO `user_history_system` (`username`, `action`, `action_name`, `sotien`, `mota`, `time`) VALUES ('".$_Users."', 'Vòng Quay FreeFire', '".$vongquay['name']."', '-".number_format($vongquay['giatien'])."đ', 'Nhận được ".$kun->vongquay_gift($id, $kundeptrai, 'kimcuong')." Kim Cương', '".time()."')");
    	// Update Tiền User
    mysqli_query($kun->connect_db(),"UPDATE `account` SET `temp_vnd` = `temp_vnd` - '".$vongquay['giatien']."' WHERE `username` = '".$_Users."'"); 


}


echo json_encode(array('status' => $status, 'item' => $kundeptrai,'location' => $location, 'msg' =>$msg));