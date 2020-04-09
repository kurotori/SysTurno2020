<?php
    include_once "../conexionbd.php";
    include_once "../funcionesVarias.php";
    include_once "../clases.php";

    // required headers
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json; charset=UTF-8");

    $usuario_ci = validarDatos($_POST["usuario_ci"]);
    $doctor_ci = validarDatos($_POST["doctor_ci"]);
    $fecha = validarDatos($_POST["fecha"]);

    $receta_id = nuevaReceta($doctor_ci,$usuario_ci,$fecha);

    $receta = new Receta();
    $receta->id = $receta_id;

    http_response_code(200);
    $JSON_string = ObjAJSON($receta);
    echo "$JSON_string";

?>