<?php
	$tablename = "inventory_items";
	$columns = array("id", "name_id", "quantity", "maximum");

	require_once __DIR__ . '/db_access.php';
	$db = new DatabaseAccessor();
	$response = $db->fetch_table($tablename, $columns);

	echo json_encode($response);
?>
