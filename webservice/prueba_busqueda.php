<?php
    include_once "conexionbd.php";
    include_once "funcionesVarias.php";
    include_once "clases.php";

//required headers
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json; charset=UTF-8");

/*
    $listado = new ListadoMedRecetados();
    $listado = buscarMedicamentosRecetadosNoEnt("38915360");

*/
    //$listado = new TurnoSolicitado();
    $listado = cancelarTurnoSolicitado('38915360');
    
    http_response_code(200);
    $JSON_string = ObjAJSON($listado);//ObjAJSON($objJSON);
    echo "$JSON_string";
  

//probarSolicitarTurno('38915360','331')

    
?>