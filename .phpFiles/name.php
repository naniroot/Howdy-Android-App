<?php
/*
 * Following code will list presence and buddy list
 */
// array for JSON response
$response["namearray"] = array();

// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// check for post data
if (isset($_POST["username"])) {

    $username = $_POST['username'];
	
	$result = mysql_query("SELECT Name FROM super_user WHERE Username ='$username'") or die(mysql_error()); 
	
// check for empty result
	if(mysql_num_rows($result) > 0){
	    while ($row = mysql_fetch_array($result)) {
		
			// temp user array
			$buddy = array();
			$buddy["myname"] = $row["Name"];
		
			//push
			array_push($response["namearray"],$buddy);
		}
		// success
		$response["success"] = 1;
		// echoing JSON response
		echo json_encode($response);
	}
	else{
	// no products found
    $response["success"] = 0;
    $response["message"] = "No name found";
	
	echo json_encode($response);
	}	
}
?>