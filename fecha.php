<?php
include_once "conexionbd.php";
include_once "clases.php";
include_once "funcionesVarias.php";

/*
$Tiempo = new DateTime();

$marcaTiempo = new DateTime("2020-03-24 14:48:05");

$dato = 6;

echo $Tiempo->diff($marcaTiempo)->format("%i");
echo "<br>";
echo ($Tiempo->diff($marcaTiempo)->format("%i") - $dato);
*/
$datosToken = new DatosToken();
$datosToken = buscarDatosToken(8);
echo "-->".($datosToken->token_Val);
?>