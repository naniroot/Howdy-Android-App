<?php
 
/*
 * Following code will create a new user row
 * All user details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['name']) && isset($_POST['username']) && isset($_POST['password'])) {
 
    $name = $_POST['name'];
    $username = $_POST['username'];
    $password = $_POST['password'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO super_user(Name, Username, Password) VALUES('$name', '$username', '$password')");
 
    // check if row inserted or not
    if (!empty($result)) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Registration Successful.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Please retry again! Username already exists.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>