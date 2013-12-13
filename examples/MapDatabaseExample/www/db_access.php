<?php
	class DatabaseAccessor {
		function fetch_table($tablename, $columns) {
			require_once __DIR__ . '/db_connect.php';
			$db = new DatabaseConnector();
			$result = mysql_query("SELECT * FROM $tablename") or die(mysql_error());
			
			$response = array();
			if (mysql_num_rows($result) > 0) {
				$response["$tablename"] = array();
					
				while ($row = mysql_fetch_array($result)) {
					$data = array();
					foreach ($columns as $column) {
						$data["$column"] = $row["$column"];
					}
					array_push($response["$tablename"], $data);
				}
					
				$response["success"] = 1;
				return $response;
			} else {
				$response["success"] = 0;
				$response["message"] = "Table $tablename is empty.";
				return $response;
			}
		}
	}
?>
