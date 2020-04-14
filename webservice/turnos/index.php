<?php

include_once "../conexionbd.php";
include_once "../funcionesVarias.php";
include_once "../clases.php";

// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");


//Requerimientos para una solicitud de turnos:
// - usuario_ci
// - token_val
// - sesion_val
// - tipo de solicitud
// - datos extra

//Tipos de solicitud:
// 100 - Asignar turno a usuario
// 
// 
if($_POST){
    //echo "ok POST|";
    //Se chequean sistemáticamente los datos de sesión para validar el proceso
    if(isset($_POST["usuario_ci"]) && !( empty($_POST["usuario_ci"]) )){

        $usuario_ci = validarDatos($_POST["usuario_ci"]);
        
        if( isset($_POST["token_val"]) && isset($_POST["sesion_val"]) ){
                
            // 3.1 - Si no esta vacío se continúa
            if( !( empty($_POST["token_val"]) ) && !( empty($_POST["sesion_val"]) )){
                //echo "TOKEN OK";

                $token_val = validarDatos($_POST["token_val"]);
                $sesion_val = validarDatos($_POST["sesion_val"]);
                
                $sesion_valida = new SesionValida();
                
                //Chequeo de validez de sesión
                $sesion_valida = validarSesion($usuario_ci,$token_val,$sesion_val);
                
                //Validez de la sesion. Si es válida, se continúa
                if(strcmp($sesion_valida->valida,"true")==0){
                    
                    //se prepara el resultado
                    $resultado=new stdClass();
                    //Obtención de tipo de búsqueda
                    if(isset($_POST["tipo"]) && !(empty( $_POST["tipo"] ))){
                        $tipo = validarDatos($_POST["tipo"]);
                        //Se raliza la búsqueda según el tipo
                        switch($tipo){
                            case "100":
                                $resultado = solicitarUnTurno($usuario_ci);
                                break;
                        }
                        
                        http_response_code(200);
                        $JSON_string = ObjAJSON($resultado);//ObjAJSON($objJSON);
                        echo "$JSON_string";
                    }
                }
            }
        }
    }
}

function solicitarUnTurno($usuario_ci){
    $confirmacion = new Confirmacion();
    $turno_id=validarDatos($_POST["turno_id"]);
    $confirmacion = solicitarTurno($usuario_ci,$turno_id);
    return $confirmacion;
}

?>