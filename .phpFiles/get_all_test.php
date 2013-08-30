<?php

$response = array();

//require_once __DIR__ . '/db_connect.php';



$response["buddies"] = array();
//$response["mylocation"]="home";
//$response["myname"]="nani";

$buddy = array();
$buddy["name"]='mayuresh';
$buddy["location"]='work';
array_push($response["buddies"], $buddy);


$buddy["name"]='kaushal';
$buddy["location"]='university';
array_push($response["buddies"], $buddy);


$response["success"] = 1;
echo json_encode($response);

?>
