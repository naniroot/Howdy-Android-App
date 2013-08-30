<?php
/*
 * Following code will list presence and buddy list
 */
// array for JSON response
$response["presence"] = array();

// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// check for post data
if (isset($_POST["username"]) && isset($_POST["latitude"]) && isset($_POST["longitude"]) &&isset($_POST["flag"])) 
{

    $username = $_POST['username'];
	$latitude = $_POST['latitude'];
	$longitude = $_POST['longitude'];
	$flag = $_POST['flag'];
	$TEMP=0.0001;
	
	if ($flag!=1)
	{
		$result = mysql_query("SELECT a.Name as Name1, b.Location as Location1 FROM super_user a, presence_gps_mapping b WHERE a.Username='$username' AND a.Username=b.Username AND b.Latitude BETWEEN ($latitude-$TEMP) AND ($latitude+$TEMP) AND b.Longitude BETWEEN ($longitude-$TEMP) AND ($longitude+$TEMP)") or die(mysql_error()); 
		
		// check for empty result
		if(mysql_num_rows($result) > 0)
		{
			while ($row = mysql_fetch_array($result)) 
			{
				$location=$row["Location1"];
				$result_2 = mysql_query("UPDATE user_location_mapping SET Location = '$location' WHERE Username = '$username'");
				// temp user array
				$buddy = array();
				$buddy["myname"] = $row["Name1"];
				$buddy["mylocation"] = $location;
				
				//push
				array_push($response["presence"],$buddy);
			}
			// success
			$response["success"] = 1;
			// echoing JSON response
			echo json_encode($response);
		}
		else{
		// no products found
			$result_1 = mysql_query("SELECT Name FROM super_user WHERE Username='$username'") or die(mysql_error());
			$result_2 = mysql_query("UPDATE user_location_mapping SET Location = 'Presence Unavailable' WHERE Username = '$username'");
			$row = mysql_fetch_array($result_1);
			$buddy = array();
			$buddy["myname"] = $row["Name"];
			$buddy["mylocation"] = "Presence Unavailable";
			//push
			array_push($response["presence"],$buddy);
			
			$response["success"] = 1;
			// echoing JSON response
			echo json_encode($response);
		}	
	} 	
	else
	{
		$result_1 = mysql_query("UPDATE user_location_mapping SET Location = 'Driving' WHERE Username = '$username'");
		$result = mysql_query("SELECT a.Name, b.Location FROM super_user a, user_location_mapping b WHERE a.Username='$username' AND a.Username=b.Username") or die(mysql_error());
		if (mysql_num_rows($result) > 0) 
		{
			// looping through all results
			// products node
			$response["presence"] = array();
			$row = mysql_fetch_array($result);
			// temp user array
			$buddy = array();
			$buddy["myname"] = $row["Name"];
			$buddy["mylocation"] = $row["Location"];
			array_push($response["presence"],$buddy);
		    // success
			$response["success"] = 1;
			// echoing JSON response
			echo json_encode($response);	
		}
	}
}

?>