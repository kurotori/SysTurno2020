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
    
    $objJSON = new validarSesion();
    
    if(isset($_POST["usuario_ci"])){

        $usuario_ci = validarDatos($_POST["usuario_ci"]);
        if( isset($_POST["token_val"]) && isset($_POST["token_id"]) ){
            
            if( !( empty($_POST["token_val"]) ) && !( empty($_POST["token_id"]) )){
                $token_val = $_POST["token_val"];
                $token_id = $_POST["token_id"];
                
                
                if( isset($_POST["sesion_val"]) && isset($_POST["sesion_id"]) ){
                    
                    if( !( empty($_POST["sesion_val"]) ) && !( empty($_POST["sesion_id"]) )){
                        
                    }
                }
            }
        }
    }
    
}



?>