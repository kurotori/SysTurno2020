<?php
include_once "conexionbd.php";
include_once "herramientas/CIValidator.php";

use Leewayweb\CiValidator\CiValidator;

$civ = new CiValidator();

//echo " --> ".($civ->validate_ci('38915368') ? 'true' : 'false').PHP_EOL;
$prueba = ($civ->validate_ci('11111110') ? true : false);

$prueba2 = buscarUsuarioExiste('38915369');

echo $prueba;
if($prueba){
    echo "sirve";
}
else{
    echo "tambien sirve";
}

echo "<p>---> $prueba2 </p>";

?>