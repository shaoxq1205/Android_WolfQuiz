<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();
$response = array();

$num = $db->getMaxSetnum();
$response["maxquiz"] = $num;
echo json_encode($response);

?>