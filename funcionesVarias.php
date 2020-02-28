<?php
    include_once "conexionbd.php";
    include_once "clases.php";
    
    //Comprueba la identidad del usuario y si es válido, 
    //registra y entrega objeto conteniendo un token de acceso al sistema
    function generarToken($usuario){
        $chequeo = chequearUsuario($usuario);

        $tiempoUnix = time();
        $jsonDatos = new stdClass;
        //$token = "";
        //$tipo = "";
        $obj_token = new Token();
        
        if($chequeo){
            $dato_string = "$usuario.$chequeo.$tiempoUnix";
            
            $obj_token->token = password_hash($dato_string, PASSWORD_BCRYPT);
            $obj_token->tipo = "TOKEN";
            $obj_token->mensaje = "OK";

            registrarToken($obj_token->token,$usuario);
        }
        else{
            $obj_token->token = "01";
            $obj_token->tipo = "ERROR";
            $obj_token->mensaje = "Usuario desconocido";
        }
        
        
        $jsonDatos=array("Token"=>$obj_token);
        
        $objJSON = json_encode($jsonDatos);

        return $objJSON;
    }
?>