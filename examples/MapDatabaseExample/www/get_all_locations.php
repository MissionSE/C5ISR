<?php
	$response = array();

	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	$result = mysql_query("SELECT * FROM locations") or die(mysql_error());

	if (mysql_num_rows($result) > 0) {
		$response["locations"] = array();

		while ($row = mysql_fetch_array($result)) {
			$location = array();
			$location["id"] = $row["id"];
			$location["name"] = $row["name"];
			$location["latitude"] = $row["latitude"];
			$location["longitude"] = $row["longitude"];

			array_push($response["locations"], $location);
		}

		$response["success"] = 1;
		echo json_encode($response);
	} else {
		$response["success"] = 0;
		$response["message"] = "No locations found";
		echo json_encode($response);
	}
?>
