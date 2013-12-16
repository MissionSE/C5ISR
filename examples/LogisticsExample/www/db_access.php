<?php
	class DatabaseAccessor {
		public function __construct() {
			static $db;

			require_once __DIR__ . '/db_connect.php';
			$db = new DatabaseConnector();
		}

		public function query_database($query) {
			$result = mysql_query($query) or die(mysql_error());
			
			return $result;
		}

		public function fetch_table($tablename, $columns) {
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

		private function get_post_data($keys) {
			$post_data = array();
			foreach ($keys as $key) {
				if (isset($_POST[$key])) {
					$post_data[$key] = mysql_real_escape_string($_POST[$key]);
				}
			}

			return $post_data;
		}

		private function build_insert_query($tablename, $insert_data) {
			$columns_string = "";
			$values_string = "";
			foreach ($insert_data as $column => $value) {
				if ($columns_string != "" && $values_string != "") {
					$columns_string = "$columns_string, ";
					$values_string = "$values_string, ";
				}
				$columns_string = "$columns_string$column";
				$values_string = "$values_string'$value'";
			}

			return "INSERT INTO $tablename($columns_string) VALUES($values_string)";
		}

		public function insert_item($tablename, $columns) {
			$response = array();
			$success = 0;

			$post_data = $this->get_post_data($columns);
			if (count($columns) == count($post_data)) {
				$query = $this->build_insert_query($tablename, $post_data);
				$result = $this->query_database($query);
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
			} else {
				$response["message"] = "Required field(s) missing.";
			}

			$response["success"] = $success;
			return $response;
		}
	}
?>
