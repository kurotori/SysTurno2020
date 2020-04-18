<?php
    include_once "../conexionbd.php";
    include_once "../funcionesVarias.php";
    include_once "../clases.php";

    // required headers
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json; charset=UTF-8");

    $receta_id = validarDatos($_POST["receta_id"]);
    $medicamento_id = validarDatos($_POST["medicamento_id"]);
    $cantidad = validarDatos($_POST["cantidad"]);

    $confirmacion = new Confirmacion();
    $confirmacion = agregarMedicamento($receta_id,$medicamento_id,$cantidad);

    http_response_code(200);
    $JSON_string = ObjAJSON($confirmacion);
    echo "$JSON_string";

?>