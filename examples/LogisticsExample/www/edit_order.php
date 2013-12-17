<?php
	$tablename = "orders";
	$columns = array("ordered_at", "severity", "status");

	require_once __DIR__ . '/db_access.php';
	$db = new DatabaseAccessor();
	$response = $db->update_item($tablename, $columns);

	echo json_encode($response);
?>
