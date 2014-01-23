<?php
	$tablename = "sites_to_orders";
	$columns = array("site_id", "order_id");

	require_once __DIR__ . '/db_access.php';
	$db = new DatabaseAccessor();
	$response = $db->insert_item($tablename, $columns);

	echo json_encode($response);
?>
