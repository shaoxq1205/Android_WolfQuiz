<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
$response = array("error" => FALSE);

if (isset($_POST['question']) && isset($_POST['optiona']) && isset($_POST['optionb']) && isset($_POST['optionc']) && isset($_POST['optiond']) && isset($_POST['setnum']) && isset($_POST['type'])) {
 
    // receiving the post params
    $question = $_POST['question'];
    $optiona = $_POST['optiona'];
    $optionb = $_POST['optionb'];
    $optionc = $_POST['optionc'];
    $optiond = $_POST['optiond'];
    $setnum = $_POST['setnum'];
    $type = $_POST['type'];
 
    // check if question is already existed with the same question
    if ($db->isQuestionExisted($question)) {
        // question already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "Question already existed with " . $question;
        echo json_encode($response);
    } else {
        // create a new question
        $question = $db->storeQuestion($question, $optiona, $optionb, $optionc, $optiond, $setnum, $type);
        if ($question) {
            // question stored successfully
            $response["error"] = FALSE;
            $response["uid"] = $question["unique_id"];
            $response["question"]["question"] = $question["question"];
            $response["question"]["optiona"] = $question["optiona"];
            $response["question"]["optionb"] = $question["optionb"];
            $response["question"]["optionc"] = $question["optionc"];
            $response["question"]["optiond"] = $question["optiond"];
            $response["question"]["created_at"] = $question["created_at"];
            $response["question"]["updated_at"] = $question["updated_at"];
            $response["question"]["setnum"] = $question["setnum"];
            $response["question"]["type"] = $question["type"];
            echo json_encode($response);
        } else {
            // question failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in question creation!". $question;
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters are missing SSBB!";
    echo json_encode($response);
}
?>