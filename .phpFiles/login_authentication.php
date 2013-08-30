<?php
 
/*
 * Following code will validate the user
 * A user is identified by Username
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
if (isset($_POST["username"])&& isset($_POST["password"])) {
    $username = $_POST['username'];
	$password = $_POST['password'];

    // get a user from super user table
    $result = mysql_query("SELECT * FROM super_user WHERE Username = '$username' and Password = '$password'");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

            // echoing JSON response
			$response["success"] = 1;
            $response["message"] = "Welcome to Howdy!";
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "Invalid Username/Password";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "Invalid Username or Password";
 
        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Username or Password is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>