<?php
	$tablename = "orders";
	$columns = array("id", "ordered_at", "severity", "status");

	require_once __DIR__ . '/db_access.php';
	$db = new DatabaseAccessor();
	$response = $db->fetch_table($tablename, $columns);

	echo json_encode($response);
?>