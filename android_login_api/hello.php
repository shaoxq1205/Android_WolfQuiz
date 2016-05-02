<?php

	include 'include/DB_Connect.php';
	$db = new Db_Connect();
    $conn = $db->connect();
    $stmt = $conn->prepare("SELECT * from questions WHERE id = 1");
    $stmt->execute();
    $ques = $stmt->get_result()->fetch_assoc();
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
    }
    
    echo $ques["question"]."<br />";
    echo $ques["optiona"]."<br />";
    echo $ques["optionb"]."<br />";
    echo $ques["optionc"]."<br />";
    echo $ques["optiond"]."<br />";

    echo $ques_random[0]."<br />";
    echo $ques_random[1]."<br />";
    echo $ques_random[2]."<br />";
    echo $ques_random[3]."<br />";
    
    
    
    
    /*
    $id = $stmt->get_result()->fetch_assoc();
    $s = implode($id);
    $i = intval($s);
    $i = $i + 1;
    $stmt = $conn->prepare("DELETE FROM multiplechoice WHERE question = 'aa'");
    $stmt->execute();
  
    while($row=mysqli_fetch_array(mysqli_query($conn, "SELECT * FROM multiplechoice WHERE id = $i"), MYSQLI_ASSOC)) { 
        $stmt = $conn->prepare("UPDATE multiplechoice SET id = id - 1 WHERE id = $i");
        $stmt->execute();
        $i = $i + 1;
    }
    /*
    $stmt = $conn->prepare("SELECT * FROM multiplechoice WHERE id = $i+1");
    $stmt->execute();
    $result = $stmt->get_result()->fetch_assoc();
    echo $result['question'];
    */


?>