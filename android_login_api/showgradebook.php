<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();
// json response array
$response = array("error" => FALSE);
//$status = "Administrator";
//$email = "2525fdaa";
//if($email){
	    $studinfo = $db->getGradeTeacher();	
		if ($studinfo != false) {
		$SetsCount  = count($studinfo[0])-6;
		$StudentsCount  = count($studinfo);
        $response["error"] = FALSE;
		$response["SetsCount"] = "$SetsCount";
		$response["StudentsCount"] = "$StudentsCount";
		for($j = 1; $j<= $StudentsCount; $j++){
			$Student = "Student"."$j";
			$response[$Student]["qid"] = $studinfo[$j-1]["unique_id"];
            $response[$Student]["email"] = $studinfo[$j-1]["email"];
            $response[$Student]["name"] = $studinfo[$j-1]["name"];
	        for($i = 1; $i<= $SetsCount; $i++){
		       $quizname = "Quiz"."$i";
               $response[$Student][$quizname] = $studinfo[$j-1][$quizname];
	        }
            $response[$Student]["created_at"] = $studinfo[$j-1]["created_at"];
            $response[$Student]["updated_at"] = $studinfo[$j-1]["updated_at"];
		}  
    echo json_encode($response);
}


?>