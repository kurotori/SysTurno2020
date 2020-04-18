<?php

include_once "../conexionbd.php";
include_once "../funcionesVarias.php";
include_once "../clases.php";

// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

//Requerimientos para actualizar datos:
// - usuario_ci
// - token_val
// - sesion_val
// - tipo de actualización
// - datos extra
if($_POST){
       //echo "ok POST|";
    //Se chequean sistemáticamente los datos de sesión para validar el proceso
    if(isset($_POST["usuario_ci"]) && !( empty($_POST["usuario_ci"]) )){
        //echo "ok CI|";
        $usuario_ci = validarDatos($_POST["usuario_ci"]);
        
        if( isset($_POST["token_val"]) && isset($_POST["sesion_val"]) ){
                
            // 3.1 - Si no esta vacío se continúa
            if( !( empty($_POST["token_val"]) ) && !( empty($_POST["sesion_val"]) )){
                //echo "TOKEN y Sesion OK|";

                $token_val = validarDatos($_POST["token_val"]);
                $sesion_val = validarDatos($_POST["sesion_val"]);
                
                $sesion_valida = new SesionValida();
                
                //Chequeo de validez de sesión
                $sesion_valida = validarSesion($usuario_ci,$token_val,$sesion_val);
                
                //Validez de la sesion. Si es válida, se continúa
                if(strcmp($sesion_valida->valida,"true")==0){
                    //echo "sesion valida OK|";
                    //se prepara el resultado
                    $resultado=new stdClass();
                    
                    if(isset($_POST["tipo"]) && !(empty( $_POST["tipo"] ))){
                        //echo "tipo OK|";
                        $tipo = validarDatos($_POST["tipo"]);
                        //Se raliza la búsqueda según el tipo
                        switch($tipo){
                            case "10":
                                $resultado = actDatosPerfil($usuario_ci);
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

function actDatosPerfil($usuario_ci){
    $nombre = validarDatos($_POST["nombre"]);
    $apellido = validarDatos($_POST["apellido"]);
    $telefono = validarDatos($_POST["telefono"]);
    $direccion = validarDatos($_POST["direccion"]);
    $email = validarDatos($_POST["email"]);
    $recibeEmail = validarDatos($_POST["recibeMail"]);
    $recibeSMS = validarDatos($_POST["recibeSMS"]);

    $datosUsuario = new DatosUsuario();
    $datosUsuario->usuario_CI = $usuario_ci;
    $datosUsuario->nombre = $nombre;
    $datosUsuario->apellido = $apellido;
    $datosUsuario->telefono = $telefono;
    $datosUsuario->email = $email;
    $datosUsuario->recibeEmail = $recibeEmail;
    $datosUsuario->recibeSMS = $recibeSMS;
    
    $resultado = actualizarDatosPerfilUsuario($usuario_ci,$datosUsuario);    
    return $resultado;
}



?>