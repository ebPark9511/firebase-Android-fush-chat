<?php

header("Content-Type: text/html; charset=UTF-8");
$connect = mysql_connect("127.0.0.1", "DB_USER_ID", "DB_USER_PW");
mysql_selectdb("DB_SELECT_NAME");
mysql_query("SET NAMES utf8");
$token = $_REQUEST[token];
$location_X = $_REQUEST[location_X];
$location_Y = $_REQUEST[location_Y];


$qry = "SELECT nick, location_X, location_Y , ( 6371 * ACOS( COS( RADIANS( $location_X ) ) * COS( RADIANS( location_X ) ) * COS( RADIANS( location_Y ) - RADIANS( $location_Y ) ) + SIN( RADIANS( $location_X ) ) * SIN( RADIANS( location_X ) ) ) ) AS distance
FROM SystemPark
WHERE token != '$token'
HAVING distance <=0.5
ORDER BY distance;";
$query_exec = mysql_query($qry);


$xmlcode = "<?xml version = \"1.0\" encoding = \"utf-8\"?>\n";
$xmlcode .= "<node>\n";

while($result=mysql_fetch_array($query_exec)){
        $nick = $result[nick];
        $location_X = $result[location_X];
        $location_Y = $result[location_Y];
	$xmlcode .= "<nick>$nick</nick>\n";
	$xmlcode .= "<location_X>$location_X</location_X>\n";
	$xmlcode .= "<location_Y>$location_Y</location_Y>\n";


}

$xmlcode .= "</node>";

echo $xmlcode;





?>
