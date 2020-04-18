<?php
include_once "../conexionbd.php";
include_once "../funcionesVarias.php";
include_once "../clases.php";
    // required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");



// Comienzo del proceso de login
// 1 - Se chequea la existencia de datos POST. 
if($_POST){
    
    $objJSON = new Confirmacion();
    
    
    if(isset($_POST["usuario_ci"]) && !( empty($_POST["usuario_ci"]) ) ){
        
        $usuario_ci = validarDatos($_POST["usuario_ci"]);
        
        //Se chequea que el token_val este presente y no sea vacío
        if( isset($_POST["token_val"]) && !( empty($_POST["token_val"]) ) ){
            
            $token_val = $_POST["token_val"];
                
            //Se chequea que el sesion_val este presente y no sea vacío
            if( isset($_POST["sesion_val"]) && !( empty($_POST["sesion_val"]) ) ){
                
                $sesion_val = validarDatos($_POST["sesion_val"]);
                
                $validarSesion = validarSesionInterno($usuario_ci,$token_val,$sesion_val);
                
                if($validarSesion){
                    if ( cerrarSesiones($usuario_ci) ){
                        $objJSON->estado = "OK";
                        $objJSON->mensaje = "Sesión cerrada con éxito";
                    }
                    else{
                        $objJSON->estado = "ERROR";
                        $objJSON->mensaje = "Ha ocurrido un error. Intente de nuevo más tarde";
                    }
                }
            }
            else{
                $objJSON->estado = "ERROR";
                $objJSON->mensaje = "No_sesion_val";
            }
        }
        else{
            $objJSON->estado = "ERROR";
            $objJSON->mensaje = "No_token_val";
        }
        
    }
    else{
        $objJSON->estado = "ERROR";
        $objJSON->mensaje = "No_usuario_ci";
    }
    
    http_response_code(200);
    $JSON_string = ObjAJSON($objJSON);//ObjAJSON($objJSON);
    echo "$JSON_string";
    
}

?>