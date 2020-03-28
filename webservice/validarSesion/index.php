<?php
include_once "../conexionbd.php";
include_once "../funcionesVarias.php";
include_once "../clases.php";
    // required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// Cmienzo del proceso de validación
// 1 - Se chequea la existencia de datos POST. 
if($_POST){
    
    $objJSON = new SesionValida();
    //Se chequea que la ci_usuario este presente y no sea vacía
    if(isset($_POST["usuario_ci"]) && !(empty($_POST["usuario_ci"])) ){

        $usuario_ci = validarDatos($_POST["usuario_ci"]);
        
        //Se chequea que el token_val este presente y no sea vacío
        if( isset($_POST["token_val"]) && !( empty($_POST["token_val"]) ) ){
            
            $token_val = $_POST["token_val"];
                
            //Se chequea que el sesion_val este presente y no sea vacío
            if( isset($_POST["sesion_val"]) && !( empty($_POST["sesion_val"]) ) ){
                
                $sesion_val = validarDatos($_POST["sesion_val"]);
                $objJSON = validarSesion($usuario_ci,$token_val,$sesion_val);
                
                http_response_code(200);
                $JSON_string = TokenAJSON($objJSON);
                echo "$JSON_string";

            }
        }
    }
}



?>