<?php
	class DatabaseAccessor {
		function __construct() {
			static $db;

			require_once __DIR__ . '/db_connect.php';
			$db = new DatabaseConnector();
		}

		function query_database($query) {
			$result = mysql_query($query) or die(mysql_error());
			
			return $result;
		}

		function fetch_table($tablename, $columns) {
			$result = $this->query_database("SELECT * FROM $tablename");

			$response = array();
			$success = 0;
			if (mysql_num_rows($result) > 0) {
				$success = 1;
				$response["$tablename"] = array();

				while ($row = mysql_fetch_array($result)) {
					$data = array();
					foreach ($columns as $column) {
						$data["$column"] = $row["$column"];
					}
					array_push($response["$tablename"], $data);
				}
			}

			$response["success"] = $success;
			if (!$success) {
				$response["message"] = "Table $tablename is empty.";
			}
			return $response;
		}

		function insert_item($tablename, $columns) {
			$response = array();
			$success = 0;

			$columns_posted = true;
			$columns_string = "";
			$values_string = "";
			foreach ($columns as $column) {
				if (isset($_POST[$column])) {
					$value = mysql_real_escape_string($_POST["$column"]);
					if ($columns_string != "" && $values_string != "") {
						$columns_string = "$columns_string, ";
						$values_string = "$values_string, ";
					}
					$columns_string = "$columns_string$column";
					$values_string = "$values_string'$value'";
				} else {
					$columns_posted = false;
					$response["message"] = "Required field(s) missing.";
					break;
				}
			}

			if ($columns_posted) {
				$result = $this->query_database("INSERT INTO $tablename($columns_string) VALUES($values_string)");
				if ($result) {
					$id = mysql_insert_id();
					if ($id) {
						$response["id"] = $id;
						$success = 1;
					} else {
						$response["message"] = "Unable to get last insert id.";
					}
				} else {
					$response["message"] = mysql_error();
				}
			}

			$response["success"] = $success;
			return $response;
		}
	}
?>
