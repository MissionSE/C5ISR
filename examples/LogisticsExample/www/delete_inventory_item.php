<?php
	require_once __DIR__ . '/db_access.php';
	$db = new DatabaseAccessor();
	$response = $db->delete_item('inventory_items');

	echo json_encode($response);
?>
