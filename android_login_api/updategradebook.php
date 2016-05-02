<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);

//$email = "dddX";
//$name = "Bxxxx";
//$setname = "Quiz5";
//$grade = "222";

// if ($email) {
if (isset($_POST['email']) && isset($_POST['name']) && isset($_POST['setname']) && isset($_POST['grade'])) {
    $email = $_POST['email'];
    $name = $_POST['name'];
    $setname = $_POST['setname'];
    $grade = $_POST['grade'];
    $table = "gradebook";
    // check if question is already existed with the same question
    if ($db->isColumnExisted($setname,$table)) {
        // update an existing question
        $ups = $db->updateGradebook($email, $name, $setname, $grade);
        if ($ups) {
            // question updated successfully
            $response["error"] = FALSE;
            echo json_encode($response);
        } else {
            // question failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in grade storing!";
            echo json_encode($response);
        }
    } else {
		$newsetname = $setname;
		$aftercolumn = "name";
		$ids = $db->insertColumn($newsetname, $aftercolumn, $table);
        if ($ids) {
            $ups = $db->updateGradebook($email, $name, $setname, $grade);
        if ($ups) {
            // question updated successfully
            $response["error"] = FALSE;
            echo json_encode($response);
        } else {
            // question failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in grade storing!";
            echo json_encode($response);
        }
        }else{
            // question failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in column creation!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters are missing SSBB!";
    echo json_encode($response);
}
?>