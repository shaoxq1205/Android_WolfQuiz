<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
$aftercolumn = 'name';
$newsetname = "quiz1";
 if ($aftercolumn) {
//if (isset($_POST['aftercolumn']) && isset($_POST['newsetname']))
 
    // receiving the post params
    //$aftercolumn = $_POST['aftercolumn'];
    //$newsetname = $_POST['newsetname'];
    $table  = 'gradebook';
    // check if question is already existed with the same question
    if (!$db->isColumnExisted($newsetname, $table)) {
        $ids = $db->insertColumn($newsetname, $aftercolumn, $table);
        if ($ids) {
            // column inserted successfully
            $response["error"] = FALSE;
			
            echo json_encode($response);
        } else {
            // question failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in column creation!";
            echo json_encode($response);
        }
    } else {
		//  column existed
        $response["error"] = TRUE;
        $response["error_msg"] = "Column already exists with" . $newsetname;
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters are missing!";
    echo json_encode($response);
}
?>