<?php
	$tablename = "sites_to_inventory_items";
	$columns = array("site_id", "item_id");

	require_once __DIR__ . '/db_access.php';
	$db = new DatabaseAccessor();
	$response = $db->insert_item($tablename, $columns);

	echo json_encode($response);
?>
