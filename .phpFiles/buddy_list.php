<?php
/*
 * Following code will list presence and buddy list
 */
// array for JSON response
$response["buddies"] = array();

// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// check for post data
if (isset($_POST["username"])) {

    $username = $_POST['username'];
	
	$result = mysql_query("SELECT a.Name as Name1, b.Location as Location1 FROM super_user a, user_location_mapping b WHERE a.Username!='$username' AND a.Username=b.Username") or die(mysql_error()); 
	
// check for empty result
	if(mysql_num_rows($result) > 0){
	    while ($row = mysql_fetch_array($result)) {
		
			// temp user array
			$buddy = array();
			$buddy["name"] = $row["Name1"];
			$buddy["location"] = $row["Location1"];
		
			//push
			array_push($response["buddies"],$buddy);
		}
		// success
		$response["success"] = 1;
		// echoing JSON response
		echo json_encode($response);
	}
	else{
	// no products found
    $response["success"] = 0;
    $response["message"] = "No buddies found";
	
	echo json_encode($response);
	}	
}
?>