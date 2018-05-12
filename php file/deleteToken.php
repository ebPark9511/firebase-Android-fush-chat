<?php
$connect = mysql_connect("127.0.0.1", "DB_USER_ID", "DB_USER_PW");
mysql_selectdb("DB_SELECT_NAME");
mysql_query("set names utf8");
$token = $_REQUEST[token];



$qry = "DELETE
        FROM SystemPark
        where token = '$token';";


$result = mysql_query($qry);




?>

