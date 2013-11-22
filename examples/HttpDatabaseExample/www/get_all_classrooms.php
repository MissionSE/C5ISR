<?php
	$response = array();

	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	$result = mysql_query("SELECT * FROM classrooms") or die(mysql_error());

	if (mysql_num_rows($result) > 0) {
		$response["classrooms"] = array();

		while ($row = mysql_fetch_array($result)) {
			$classroom = array();
			$classroom["pid"] = $row["pid"];
			$classroom["name"] = $row["name"];

			array_push($response["classrooms"], $classroom);
		}

		$response["success"] = 1;
		echo json_encode($response);
	} else {
		$response["success"] = 0;
		$response["message"] = "No classrooms found";
		echo json_encode($response);
	}
?>
