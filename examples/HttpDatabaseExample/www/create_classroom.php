<?php
	$response = array();

	if (isset($_POST['name'])) {
		$name = $_POST['name'];

		require_once __DIR__ . '/db_connect.php';
		$db = new DB_CONNECT();
		$result = mysql_query("INSERT INTO classrooms(name) VALUES('$name')");

		if ($result) {
			$response["success"] = 1;
			$response["message"] = "Classroom successfully created";

			echo json_encode($response);
		} else {
			$response["success"] = 0;
			$response["message"] = mysql_error();

			echo json_encode($response);
		}
	} else {
		$response["success"] = 0;
		$response["message"] = "Required field is missing";

		echo json_encode($response);
	}
?>
