<?php
include_once "conexionbd.php";
include_once "clases.php";
include_once "funcionesVarias.php";

cerrarTurnos();
/*
$tiempo1 = new DateTime();
$tiempo2_str = $tiempo1->sub(new DateInterval('P1D'));//format("Y-m-").($tiempo1->format("j")-1);//date("Y-m-").(date("d")-1);
//$tiempo2 =  //mktime(0, 0, 0, date("m")  , date("d")-1, date("Y"));

echo "tiempo1: ".$tiempo1->format('Y-m-d');
echo "<br/>";
//echo $tiempo2->format('Y-m-d');
echo "tiempo2: ".$tiempo2_str->format('Y-m-d');  
echo "tiempo2: ".$tiempo2_str->format('Y');
echo "tiempo2: ".$tiempo2_str->format('m');
echo "tiempo2: ".$tiempo2_str->format('d');

/*
$datosToken = new DatosToken();
$datosToken = buscarDatosToken(8);
echo "-->".($datosToken->token_Val);
*/
?>