<?php
	$tablename = "orders";
	$columns = array("ordered_at", "severity_id", "status_id");

	require_once __DIR__ . '/db_access.php';
	$db = new DatabaseAccessor();
	$response = $db->insert_item($tablename, $columns);

	echo json_encode($response);
?>
