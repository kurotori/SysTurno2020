<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");


$jsonDatos = new stdClass;
$dato = "RECIBIDO";

$jsonDatos->dato = $dato;

$objJSON = json_encode($jsonDatos);

http_response_code(200);
echo $objJSON;


?>