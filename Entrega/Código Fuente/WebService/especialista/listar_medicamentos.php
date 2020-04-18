<?php
include_once "../conexionbd.php";
include_once "../funcionesVarias.php";
include_once "../clases.php";

    // required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

$medicamentos = new ListadoMedicamentos();

$medicamentos = generarListaMedicamentos();

http_response_code(200);
$JSON_string = ObjAJSON($medicamentos);//ObjAJSON($objJSON);
echo "$JSON_string";




?>