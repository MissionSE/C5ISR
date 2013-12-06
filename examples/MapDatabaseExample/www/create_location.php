<?php
	$response = array();

	if (isset($_POST['name']) && isset($_POST['latitude']) && isset($_POST['longitude'])) {
		$name = mysql_real_escape_string($_POST['name']);
		$latitude = mysql_real_escape_string($_POST['latitude']);
		$longitude = mysql_real_escape_string($_POST['longitude']);

		require_once __DIR__ . '/db_connect.php';
		$db = new DB_CONNECT();
		$result = mysql_query("INSERT INTO locations(name, latitude, longitude) VALUES('$name', '$latitude', '$longitude')");

		if ($result) {
			$response["success"] = 1;
			$response["message"] = "Location successfully created";

			echo json_encode($response);
		} else {
			$response["success"] = 0;
			$response["message"] = mysql_error();

			echo json_encode($response);
		}
	} else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";

		echo json_encode($response);
	}
?>
