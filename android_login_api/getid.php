<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();
$response = array("error" => TRUE);

if (isset($_POST['setnum'])) {
	// receiving the post params
    $setnum = $_POST['setnum'];
	$uid = $db->getId($setnum);
	$size = sizeof($uid);
	$response["uid"] = $uid;
	/*
	for ($i = 0; $i < $size; $i++) {
		$response["uid"][strval($i)] = $uid[$i];
	}
*/
	if ($size != 0) {
		$response["error"] = FALSE;
		$response["size"] = $size;
		echo json_encode($response);
	} else {
	    // user is not found with the credentials
	    $response["error"] = TRUE;
	    $response["error_msg"] = "No such quiz set available!";
	    echo json_encode($response);
	}
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter quiz set number is missing!";
    echo json_encode($response);
}

?>