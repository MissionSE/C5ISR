<?php
	$response = array();

	require_once __DIR__ . '/db_connect.php';

	$db = new DB_CONNECT();
	$result = mysql_query("DELETE FROM students WHERE 1");

	if (mysql_affected_rows() > 0) {
		$response["success"] = 1;
		$response["message"] = "Students successfully deleted";

		echo json_encode($response);
	} else {
		$response["success"] = 0;
		$response["message"] = "No students found";

		echo json_encode($response);
	}
?>
