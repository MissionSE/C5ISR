<?php
	$response = array();

	if (isset($_POST['first_name']) && isset($_POST['last_name'])) {
		$first_name = $_POST['first_name'];
		$last_name = $_POST['last_name'];

		require_once __DIR__ . '/db_connect.php';
		$db = new DB_CONNECT();
		$result = mysql_query("INSERT INTO students(first_name, last_name) VALUES('$first_name', '$last_name')");

		if ($result) {
			$response["success"] = 1;
			$response["message"] = "Student successfully created.";

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
