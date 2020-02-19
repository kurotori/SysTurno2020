<?php

$usuario = "fulano";
$chequeo = true;
$tiempoUnix = time();
$jsonDatos = new stdClass;

//echo "$usuario - $chequeo - $tiempoUnix <br/>";

$dato_string = "$usuario.$chequeo.$tiempoUnix";

$token = password_hash($dato_string, PASSWORD_BCRYPT);

$jsonDatos->chequeo = $chequeo;
$jsonDatos->token = $token;

$objJSON = json_encode($jsonDatos);

echo $objJSON;



?>