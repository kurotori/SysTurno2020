<?php
    include_once "../conexionbd.php";
    include_once "../funcionesVarias.php";
    include_once "../clases.php";

    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json; charset=UTF-8");

    date_default_timezone_set('America/Montevideo');
    $fecha = date('Y-m-d');
    
    //echo "$fecha";

    $hora_inicio = 8;
    $hora_fin = 19;

    $minutos = array();
    
    $cant_turnos_hora = 6;
    $minutos_por_turno = 60/$cant_turnos_hora;
    
    /*$fecha_string = "$fecha $hora_inicio:$hora_fin";
    echo $fecha_string;
    $turno = new Turno();
    $turno->fechaHora = $fecha_string;
    $t=crearTurno($turno);
echo $t->mensaje;*/
    
    for($i=0;$i<$cant_turnos_hora;$i++){
        $min = $minutos_por_turno*$i;
        array_push($minutos,$min);
    }
    
    //1 - Cerramos los turnos no usados del día anterior
    
    cerrarTurnos();
    
    $turnos = new Turnos();

    //2 - Creamos turnos para el día de hoy
    for($i=$hora_inicio;$i<$hora_fin;$i++){
        foreach($minutos as $minuto){
        
            $fecha_string = "$fecha $i:$minuto";
            $fecha_turno = new DateTime($fecha_string);
            $turno = new Turno();
            $turno->fechaHora = $fecha_turno->format('Y-m-d H:i:s');
            $t = crearTurno($turno);
            array_push($turnos->turnos,$t);
        }
    }
    
    http_response_code(200);
    $JSON_string = ObjAJSON($turnos);//ObjAJSON($objJSON);
    echo "$JSON_string";
?>