<?php
$connect = mysql_connect("127.0.0.1", "DB_USER_ID", "DB_USER_PW");
mysql_selectdb("DB_SELECT_NAME");
mysql_query("set names utf8");
$token = $_REQUEST[token];
$location_X = $_REQUEST[location_X];
$location_Y = $_REQUEST[location_Y];




$qry = "UPDATE SystemPark
        SET location_X = $location_X
             where token= '$token'";
$result = mysql_query($qry);

$qry = "UPDATE SystemPark
        SET location_Y = $location_Y
             where token= '$token'";
$result = mysql_query($qry);

?>


