<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['uid'])) {
	// receiving the post params
    $uid = $_POST['uid'];
	//get the question
	$ques = $db->getQuestions($uid);
	if ($ques != false) {
	    // question exists
	    $response["error"] = FALSE;
	    $response["qid"] = $ques["unique_id"];
	    $response["id"] = $ques["id"];
	    $response["ques"]["question"] = $ques["question"];
	    $response["ques"]["created_at"] = $ques["created_at"];
	    $response["ques"]["updated_at"] = $ques["updated_at"];
	    $response["ques"]["type"] = $ques["type"];
	    $response["ques"]["ans"] = $ques["optiona"];
	    if (!strcmp($ques["type"], "Multiple Choice")) {
	    	$ques_random = array();
	    	$ques_random[0] = $ques["optiona"];
	    	$ques_random[1] = $ques["optionb"];
	    	$ques_random[2] = $ques["optionc"];
	    	$ques_random[3] = $ques["optiond"];
	    	shuffle($ques_random);
	    	$response["ques"]["optiona"] = $ques_random[0];
	    	$response["ques"]["optionb"] = $ques_random[1];
	    	$response["ques"]["optionc"] = $ques_random[2];
	    	$response["ques"]["optiond"] = $ques_random[3];
	    } else {
	    	$response["ques"]["optiona"] = $ques["optiona"];
	    	$response["ques"]["optionb"] = $ques["optionb"];
	    }
	    
	    echo json_encode($response);
	} else {
	    // user is not found with the credentials
	    $response["error"] = TRUE;
	    $response["error_msg"] = "No question in the database!";
	    echo json_encode($response);
	}
} else {
	// required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Question id is null!";
    echo json_encode($response);
}


?>