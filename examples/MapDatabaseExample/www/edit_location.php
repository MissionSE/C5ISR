<?php
	$response = array();

	if (isset($_POST['id']) && isset($_POST['name']) && isset($_POST['latitude']) && isset($_POST['longitude'])) {
		$id = mysql_real_escape_string($_POST['id']);
		$name = mysql_real_escape_string($_POST['name']);
		$latitude = mysql_real_escape_string($_POST['latitude']);
		$longitude = mysql_real_escape_string($_POST['longitude']);

		require_once __DIR__ . '/db_connect.php';
		$db = new DB_CONNECT();
		$result = mysql_query("UPDATE locations SET name='$name', latitude='$latitude', longitude='$longitude', updated_at=NOW() WHERE id='$id'");

		if ($result) {
			$response["success"] = 1;
			$response["message"] = "Location successfully modified";

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
