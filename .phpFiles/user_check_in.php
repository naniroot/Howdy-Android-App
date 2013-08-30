<?php
/*
 * Following code will update user presence
 */
// array for JSON response
$response = array();

// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// check for post data
if (isset($_POST["username"])&& isset($_POST["latitude"])&&isset($_POST["longitude"])&&isset($_POST["location"])) {
    $username = $_POST['username'];
	$latitude = $_POST['latitude'];
	$longitude= $_POST['longitude'];
	$location = $_POST['location'];
	
// get user location detail from user_location_mapping
$result_1 = mysql_query("SELECT * FROM user_location_mapping WHERE Username='$username'") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result_1) > 0) {
    // looping through all results
    // products node
    $result_2 =mysql_query("SELECT * FROM presence_gps_mapping WHERE Username='$username' AND Latitude='$latitude' AND Longitude='$longitude'") or die(mysql_error());
	if(mysql_num_rows($result_2)>0){
		$result_3 = mysql_query("UPDATE presence_gps_mapping SET Location = '$location' WHERE Username = '$username' AND Latitude = '$latitude' AND Longitude ='$longitude'");
		$result_4 = mysql_query("UPDATE user_location_mapping SET Location = '$location' WHERE Username = '$username'");
	    if ($result_3 == TRUE and $result_4== TRUE) {
			// successfully updated
			$response["success"] = 1;
			$response["message"] = "Location successfully updated.";
			// echoing JSON response
			echo json_encode($response);
		}
		else{
			$response["success"] = 0;
			$response["message"] = "Oops! An error occurred.";
			// echoing JSON response
			echo json_encode($response);
		}
	}
	else {
		$result_3 = mysql_query("INSERT INTO presence_gps_mapping VALUES ('$username', '$latitude', '$longitude', '$location')");
		$result_4 = mysql_query("UPDATE user_location_mapping SET Location = '$location' WHERE Username = '$username'");
	    if ($result_3 == TRUE and $result_4== TRUE) {
			// successfully updated
			$response["success"] = 1;
			$response["message"] = "Location successfully updated.";
			// echoing JSON response
			echo json_encode($response);
		}
		else{
			$response["success"] = 0;
			$response["message"] = "Oops! An error occurred.";
			// echoing JSON response
			echo json_encode($response);
		}
	}
}	
else {
	$result_3 = mysql_query("INSERT INTO presence_gps_mapping VALUES ('$username', '$latitude', '$longitude', '$location')");
	$result_4 = mysql_query("INSERT INTO user_location_mapping VALUES ('$username', '$location')");	
	if ($result_3 == TRUE and $result_4== TRUE) {
			// successfully updated
			$response["success"] = 1;
			$response["message"] = "Location successfully updated.";
			// echoing JSON response
			echo json_encode($response);
		}
		else{
			$response["success"] = 0;
			$response["message"] = "Oops! An error occurred.";
			// echoing JSON response
			echo json_encode($response);
		}
}
}
?>