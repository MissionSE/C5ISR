<?php
	$response = array();

	if (isset($_POST['id'])) {
		$id = mysql_real_escape_string($_POST['id']);

		require_once __DIR__ . '/db_connect.php';	
		$db = new DatabaseConnector();
		$result = mysql_query("DELETE FROM locations WHERE id='$id'");
	
		if (mysql_affected_rows() > 0) {
			$response["success"] = 1;
			$response["message"] = "Location successfully deleted";
	
			echo json_encode($response);
		} else {
			$response["success"] = 0;
			$response["message"] = "Location not found";
	
			echo json_encode($response);
		}
	}
?>
