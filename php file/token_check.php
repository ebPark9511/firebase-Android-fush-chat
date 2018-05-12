<?php
        header("Content-Type: text/html; charset=UTF-8");
        extract($_POST);
        extract($_GET);
        extract($_SERVER);
        extract($_FILES);
        extract($_ENV);
        extract($_COOKIE);
        extract($_SESSION);

?>
<?php

   function parse_query_str(){
         foreach($_POST as $key => $value){
                         global${$key};
                        ${$key} = $value;

        }
          foreach($_GET as $key => $value){
                       global${$key};
                        ${$key} = $value;
          }

   }
    mysql_connect("127.0.0.1", "DB_USER_ID", "DB_USER_PW") or die (mysql_error());
    mysql_select_db("SELECT_DB_NAME");

    $token= $_POST[token];
    parse_query_str();

    $query_search
                        =" SELECT *
                        FROM SystemPark
                        where token= '$token'; ";

        $query_exec = mysql_query($query_search) or die(mysql_error());
        $rows = mysql_num_rows($query_exec);


	$xmlcode = "<?xml version = \"1.0\" encoding = \"utf-8\"?>\n";
	$xmlcode .= "<node>\n";

        if($rows==0){
		$xmlcode .= "<chk>User Not Found</chk>\n";
        }else{
                $xmlcode .= "<chk>User Found</chk>\n";
        }
	$xmlcode .= "</node>";

echo $xmlcode;

?>

