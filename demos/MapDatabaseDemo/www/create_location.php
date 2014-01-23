<?php
	$tablename = "locations";
	$columns = array("name", "latitude", "longitude");
	
	require_once __DIR__ . '/db_access.php';
	$db = new DatabaseAccessor();
	$response = $db->insert_item($tablename, $columns);
	
	echo json_encode($response);
?>
