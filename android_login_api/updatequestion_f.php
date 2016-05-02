<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
//$id = "6";
//$question = "2525fdaa";
//$optiona = "dff";
//$optionb = "aa";
//$optionc = "d";
//$optiond = "d";
// if ($id) {
if (isset($_POST['question']) && isset($_POST['optiona']) && isset($_POST['id'])) {
 
    // receiving the post params
    $question = $_POST['question'];
    $optiona = $_POST['optiona'];
	$id = $_POST['id'];
 
    // check if question is already existed with the same question
    if ($db->isIDExisted($id)) {
        // update an existing question
        $ids = $db->updateQuestion_f($question, $optiona, $id);
        if ($ids) {
            // question updated successfully
            $response["error"] = FALSE;
            $response["uid"] = $ids["unique_id"];
            $response["ids"]["question"] = $ids["question"];
            $response["ids"]["optiona"] = $ids["optiona"];
            $response["ids"]["created_at"] = $ids["created_at"];
            $response["ids"]["updated_at"] = $ids["updated_at"];
            echo json_encode($response);
        } else {
            // question failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in question creation!";
            echo json_encode($response);
        }
    } else {
		// question not existed
        $response["error"] = TRUE;
        $response["error_msg"] = "Question not existed with " . $id;
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters are missing SSBB!";
    echo json_encode($response);
}
?>