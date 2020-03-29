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
        
        if ( cerrarSesiones($usuario_ci) ){
            $objJSON->estado = "OK";
            $objJSON->mensaje = "Sesión cerrada con éxito";
        }
        else{
            $objJSON->estado = "ERROR";
            $objJSON->mensaje = "Ha ocurrido un error. Intente de nuevo más tarde";
        }
    }
    else{
        $objJSON->estado = "ERROR";
        $objJSON->mensaje = "No_usuario_ci";
    }
        
    
}

?>