<?php
	$tablename = "sites";
	$columns = array("name", "latitude", "longitude", "parent_id");

	require_once __DIR__ . '/db_access.php';
	$db = new DatabaseAccessor();
	$response = $db->update_item($tablename, $columns);

	echo json_encode($response);
?>
