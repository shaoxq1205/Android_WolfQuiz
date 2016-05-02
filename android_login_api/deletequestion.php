<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
//$question = "Ttr";
//if($question){
if (isset($_POST['question'])) {
 
    // receiving the post params
    $question = $_POST['question'];

    // check if question is already existed with the same question
    if ($db->isQuestionExisted($question)) {
        // create a new question
        $question = $db->deleteQuestion($question);
        if ($question) {
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in question creation!";
            echo json_encode($response);
        } else {
            // question failed to store
            $response["error"] = FALSE;
            $response["error_msg"] = "Delete Successfully!";
            echo json_encode($response);
        }
    } else {
		// question does not existed
        $response["error"] = TRUE;
        $response["error_msg"] = "Question does not exist ";
        echo json_encode($response);  
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters are missing!";
    echo json_encode($response);
}
?>