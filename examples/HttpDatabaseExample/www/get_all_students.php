<?php
	$response = array();

	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	$result = mysql_query("SELECT * FROM students") or die(mysql_error());

	if (mysql_num_rows($result) > 0) {
		$response["students"] = array();

		while ($row = mysql_fetch_array($result)) {
			$student = array();
			$student["pid"] = $row["pid"];
			$student["first_name"] = $row["first_name"];
			$student["last_name"] = $row["last_name"];

			array_push($response["students"], $student);
		}

		$response["success"] = 1;
		echo json_encode($response);
	} else {
		$response["success"] = 0;
		$response["message"] = "No students found";
		echo json_encode($response);
	}
?>
