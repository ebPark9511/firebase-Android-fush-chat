<?php
$connect = mysql_connect("127.0.0.1", "DB_USER_ID", "DB_USER_PW");
mysql_selectdb("DB_SELECT_NAME");
mysql_query("set names utf8");
$token = $_REQUEST[token];
$nick = $_REQUEST[nick];
$location_X = $_REQUEST[location_X];
$location_Y = $_REQUEST[location_Y];




$qry =  "insert into SystemPark(token, nick , location_X, location_Y)
                         values('$token' ,'$nick', $location_X, $location_Y);";
$result = mysql_query($qry);


?>

