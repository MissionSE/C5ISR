<?php
	$tablename = "inventory_items";
	$columns = array("name_id", "quantity", "maximum");

	require_once __DIR__ . '/db_access.php';
	$db = new DatabaseAccessor();
	$response = $db->insert_item($tablename, $columns);

	echo json_encode($response);
?>
