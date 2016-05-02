<?php
 
class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password, $identity) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
 
        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, identity, encrypted_password, salt, created_at) VALUES(?, ?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("ssssss", $uuid, $name, $email, $identity, $encrypted_password, $salt);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }

    /**
     * Storing new question
     * returns question details
     */
    public function storeQuestion($question, $optiona, $optionb, $optionc, $optiond, $setnum, $type) {
        $uuid = uniqid('', true);
        $stmt = $this->conn->prepare("ALTER TABLE questions AUTO_INCREMENT = 1");
        $stmt->execute();
        $stmt = $this->conn->prepare("INSERT INTO questions(unique_id, question, optiona, optionb, optionc, optiond, created_at, setnum, type) VALUES(?, ?, ?, ?, ?, ?, NOW(), ?, ?)");
        $stmt->bind_param("ssssssss", $uuid, $question, $optiona, $optionb, $optionc, $optiond, $setnum, $type);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM questions WHERE question = ?");
            $stmt->bind_param("s", $question);
            $stmt->execute();
            $question = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $question;
        } else {
            return false;
        }
    }
	
	public function deleteQuestion($question){
        $uuid = uniqid('', true);
        $stmt = $this->conn->prepare("SELECT id FROM questions WHERE question = ?");
        $stmt->bind_param("s", $question);
        $stmt->execute();
        $id = $stmt->get_result()->fetch_assoc();
        $s = implode($id);
        $i = intval($s);
        $i = $i + 1;
        $stmt = $this->conn->prepare("DELETE FROM questions WHERE question = ?");
        $stmt->bind_param("s", $question);
        $result = $stmt->execute();
        while($row=mysqli_fetch_array(mysqli_query($this->conn, "SELECT * FROM questions WHERE id = $i"), MYSQLI_ASSOC)) { 
            $stmt = $this->conn->prepare("UPDATE questions SET id = id - 1 WHERE id = $i");
            $stmt->execute();
            $i = $i + 1;
        }
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM questions WHERE question = ?");
            $stmt->bind_param("s", $question);
            $stmt->execute();
            $question = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $question;
        } else {
            return true;
        }
    }

     public function updateQuestion($question, $optiona, $optionb, $optionc, $optiond, $id){
        $uuid = uniqid('', true);
        $stmt = $this->conn->prepare("UPDATE questions SET question = ?, optiona = ?, optionb = ?, optionc = ?, optiond = ?  WHERE unique_id = ?");
        $stmt->bind_param("ssssss", $question, $optiona, $optionb, $optionc, $optiond, $id);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful update
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM questions WHERE unique_id = ?");
            $stmt->bind_param("s", $id);
            $stmt->execute();
            $ids = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $ids;
        } else {
            return true;
        }
    }
 
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
 
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }

    public function getMaxSetnum() {
        $i = 1;
        $s = "Quiz";
        $setnum = $s.$i;
        while (1) {
            $stmt = $this->conn->prepare("SELECT id from questions WHERE setnum = ?");
            $stmt->bind_param("s", $setnum);
            $stmt->execute();
            $result = $stmt->get_result();
            if(!$string = $result->fetch_array())
                break;
            else {
                $i++;
                $setnum = $s.$i;
            }
        }
        $i--;
        return $i;

    }

    /**
    * Get all questions
    */
    public function getQuestions($uid) {

        $stmt = $this->conn->prepare("SELECT * FROM questions WHERE unique_id = ?");
        $stmt->bind_param("s", $uid);
        if ($stmt->execute()) {
            $question = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $question;
            
        } else {
            return NULL;
        }
    }

    public function getId($setnum) {
        $stmt = $this->conn->prepare("SELECT unique_id from questions WHERE setnum = ?");
        $stmt->bind_param("s", $setnum);
        $stmt->execute();
        $result = $stmt->get_result();
        $i = 0;
        $uid = array();
        while ($string = $result->fetch_array(MYSQLI_NUM)) {
            $uid[$i] = $string[0];
            $i++;
        }
        return $uid;
    }
 

  public function updateQuestion_m($question, $optiona, $optionb, $optionc, $optiond, $id){
        $uuid = uniqid('', true);
        $stmt = $this->conn->prepare("UPDATE questions SET question = ?, optiona = ?, optionb = ?, optionc = ?, optiond = ?  WHERE unique_id = ?");
        $stmt->bind_param("ssssss", $question, $optiona, $optionb, $optionc, $optiond, $id);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful update
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM questions WHERE unique_id = ?");
            $stmt->bind_param("s", $id);
            $stmt->execute();
            $ids = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $ids;
        } else {
            return true;
        }
    }

    public function updateQuestion_t($question, $optiona, $optionb, $id){
        $uuid = uniqid('', true);
        $stmt = $this->conn->prepare("UPDATE questions SET question = ?, optiona = ?, optionb = ? WHERE unique_id = ?");
        $stmt->bind_param("ssss", $question, $optiona, $optionb, $id);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful update
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM questions WHERE unique_id = ?");
            $stmt->bind_param("s", $id);
            $stmt->execute();
            $ids = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $ids;
        } else {
            return true;
        }
    }

    public function updateQuestion_f($question, $optiona, $id){
        $uuid = uniqid('', true);
        $stmt = $this->conn->prepare("UPDATE questions SET question = ?, optiona = ? WHERE unique_id = ?");
        $stmt->bind_param("sss", $question, $optiona, $id);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful update
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM questions WHERE unique_id = ?");
            $stmt->bind_param("s", $id);
            $stmt->execute();
            $ids = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $ids;
        } else {
            return true;
        }
    }
 
    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
 
     public function isQuestionExisted($question) {
        $stmt = $this->conn->prepare("SELECT question from questions WHERE question = ?");
 
        $stmt->bind_param("s", $question);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
     
	 public function isIDExisted($uid) {
        $stmt = $this->conn->prepare("SELECT question from questions WHERE unique_id = ?");
 
        $stmt->bind_param("s", $uid);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 

    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }

    public function updateGradebook($email, $name, $setname, $grade){
        $stmt = $this->conn->prepare("SELECT email from gradebook WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        if ($stmt->num_rows > 0) {
            // user existed then update grade
			$stmt = $this->conn->prepare("UPDATE gradebook SET $setname = ?  WHERE email = ?");
            $stmt->bind_param("ss", $grade, $email);
            $result = $stmt->execute();
            $stmt->close();
            return $result;
        } else {
		    $uuid = uniqid('', true);
            $stmt = $this->conn->prepare("INSERT INTO gradebook(unique_id, name, email, $setname, created_at) VALUES(?, ?, ?, ?, NOW())");
            $stmt->bind_param("ssss", $uuid, $name, $email, $grade);
            $result = $stmt->execute();
            $stmt->close();
			return $result;
        }
    }

    public function insertColumn($newsetname, $aftercolumn, $table){
         $column = $newsetname;
		 $stmt = $this->conn->prepare("ALTER TABLE $table ADD $column VARCHAR( 255 ) NULL AFTER $aftercolumn");
         $add = $stmt->execute();
         $stmt->close();
		 if($add){
			 $result = $this->isColumnExisted($newsetname, $table);
			 return $result;
		 }
         	else {
            return false;
        }  
    }


	public function getGradeTeacher() {
        $i=0;
        $studinfo = array();
        $stmt = $this->conn->prepare("SELECT * FROM gradebook");
        if($stmt->execute()){
			$result = $stmt->get_result();
        while($studentinfo = $result->fetch_array(MYSQLI_ASSOC)){
			$studinfo[$i] = $studentinfo;
			$i++;
		}
            $stmt->close();
            return $studinfo;
		}else{
			return false;
		}
		
    }

    public function isColumnExisted($columnname, $table) {
        $stmt = $this->conn->prepare("SHOW COLUMNS FROM $table WHERE FIELD = ?");
        $stmt->bind_param("s", $columnname);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
 
}
 
?>