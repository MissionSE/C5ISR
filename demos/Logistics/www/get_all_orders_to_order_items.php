<?php
	$tablename = "orders_to_order_items";
	$columns = array("id", "order_id", "item_id");

	require_once __DIR__ . '/db_access.php';
	$db = new DatabaseAccessor();
	$response = $db->fetch_table($tablename, $columns);

	echo json_encode($response);
?>