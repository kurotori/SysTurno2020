<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");


$jsonDatos = new stdClass;

class Pruebas{
    public $dato="";
    public $estado="";
}

$pruebas = new Pruebas();
$pruebas->dato="RECIBIDO";
$pruebas->estado="OK";

$jsonDatos=array(
    "Pruebas"=>$pruebas
);
    

$objJSON = json_encode($jsonDatos);

http_response_code(200);
echo $objJSON;


?>