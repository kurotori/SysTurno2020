<?php
include_once "../conexionbd.php";
include_once "../funcionesVarias.php";
include_once "../clases.php";
    // required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");


if($_POST){
    $ci_usuario = validarDatos($_POST["ci_usuario"]);
    $objJSON = generarToken($ci_usuario);
        
    http_response_code(200);
    echo $objJSON;
}
else{
    
}
?>