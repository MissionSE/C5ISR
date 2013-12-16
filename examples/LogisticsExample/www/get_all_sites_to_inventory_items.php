<?php
	$tablename = "sites_to_inventory_items";
	$columns = array("id", "site_id", "item_id");

	require_once __DIR__ . '/db_access.php';
	$db = new DatabaseAccessor();
	$response = $db->fetch_table($tablename, $columns);

	echo json_encode($response);
?>