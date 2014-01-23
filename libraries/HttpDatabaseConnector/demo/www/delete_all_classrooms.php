<?php
	$response = array();

	require_once __DIR__ . '/db_connect.php';

	$db = new DB_CONNECT();
	$result = mysql_query("DELETE FROM classrooms WHERE 1");

	if (mysql_affected_rows() > 0) {
		$response["success"] = 1;
		$response["message"] = "Classrooms successfully deleted";

		echo json_encode($response);
	} else {
		$response["success"] = 0;
		$response["message"] = "No classrooms found";

		echo json_encode($response);
	}
?>
