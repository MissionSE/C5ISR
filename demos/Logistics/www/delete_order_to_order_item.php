<?php
	require_once __DIR__ . '/db_access.php';
	$db = new DatabaseAccessor();
	$response = $db->delete_item('orders_to_order_items');

	echo json_encode($response);
?>
