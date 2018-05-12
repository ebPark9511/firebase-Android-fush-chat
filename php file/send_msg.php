<?php

        function send_notification ($tokens, $myMessage, $nick)
        {
                $url = 'https://fcm.googleapis.com/fcm/send';


		$msg = array(
			"message" => $myMessage,
			"nick" => $nick
		);

		echo $nick;
		echo $myMessage;
	        $fields = array(
                   'registration_ids' =>  $tokens,
                   'data' => $msg
                 );


	
                $headers = array(
                        'Authorization:key =' . FIREBASE_API_KEY,
                        'Content-Type: application/json'
                        );

           $ch = curl_init();
       curl_setopt($ch, CURLOPT_URL, $url);
       curl_setopt($ch, CURLOPT_POST, true);
       curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
       curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
       curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);
       curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
       curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
       $result = curl_exec($ch);
       if ($result === FALSE) {
           die('Curl failed: ' . curl_error($ch));
       }
       curl_close($ch);
       return $result;
        }



        include_once 'config.php';
        $connect = mysql_connect("127.0.0.1", "DB_USER_ID", "DB_USER_PW");
        mysql_selectdb("SELECT_DB_NAME");
        mysql_query("set names utf8");

	$token = $_REQUEST[token];
        $location_X = $_REQUEST[location_X];
        $location_Y = $_REQUEST[location_Y];
	$nick = $_REQUEST[nick];
        $msg = $_REQUEST[msg];




	$qry = "SELECT token , ( 6371 * ACOS( COS( RADIANS( $location_X ) ) * COS( RADIANS( location_X ) ) * COS( RADIANS( location_Y ) - RADIANS( $location_Y ) ) + SIN( RADIANS( $location_X ) ) * SIN( RADIANS( location_X ) ) ) ) AS distance
FROM SystemPark
WHERE token != '$token'
HAVING distance <=0.5
ORDER BY distance;";
	$query_exec = mysql_query($qry);


	$tokens = array();
	while($result=mysql_fetch_array($query_exec)){
        	$tokens[] = $result[token];
	}			

        $message = array("message" => $nickmsg);
        $message_status = send_notification($tokens, $msg, $nick);


        echo $message_status;
 ?>


