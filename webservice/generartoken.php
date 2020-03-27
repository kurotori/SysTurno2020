<?php
include_once "conexionbd.php";

$usuario = "12345678";
$chequeo = chequearUsuario($usuario);

$tiempoUnix = time();
$jsonDatos = new stdClass;
$token = "";
$tipo = "";


//echo "$usuario - $chequeo - $tiempoUnix <br/>";
if($chequeo){
    $dato_string = "$usuario.$chequeo.$tiempoUnix";
    $token = password_hash($dato_string, PASSWORD_BCRYPT);
    $tipo = "TOKEN";
    
    registrarToken($token,$usuario);
}
else{
    $token = "01";
    $tipo = "ERROR";
}
$jsonDatos->tipo = $tipo;
$jsonDatos->token = $token;

$objJSON = json_encode($jsonDatos);

echo $objJSON;

?>