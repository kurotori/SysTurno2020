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
    
    //'objJSON' será el objeto que contendrá el token resultante
    $objJSON= new Token();
    // 2 - Si existe un ci_usuario en el POST, se valida esa entrada de datos
    // y se la asigna a la variable local
    if(isset($_POST["usuario_ci"])){

        $usuario_ci = validarDatos($_POST["usuario_ci"]);
        

        //echo "CI $usuario_ci OK";
        // 2.1 - Si el usuario indicado es 'registro', se trata de una solicitud
        // de token para registro. 
        if(strcmp($usuario_ci,"registro")==0){
            
            //"REGISTRO OK";
        }
        else{
            
            //echo "NO REGISTRO";
            // 3 - Si existen un token y un token_id en el POST, 
            // se lo valida y se lo asigna a la variable local
            if( isset($_POST["token_val"]) && isset($_POST["token_id"]) ){                
                
                // 3.1 - Si no esta vacío se continúa
                if( !( empty($_POST["token_val"]) ) && !( empty($_POST["token_id"]) )){
                    //echo "TOKEN OK";
                    
                    $token_val = validarDatos($_POST["token_val"]);
                    $token_id = validarDatos($_POST["token_id"]);

                    // 4 - Se chequea la existencia de una contraseña en los datos del POST
                    if( isset($_POST["contrasenia"]) ){
                        //echo "PASS OK";
                        // 5 - Si se proveyó una contraseña, se la asigna a la variable y se la valida
                        $contrasenia = validarDatos($_POST["contrasenia"]);

                        // 6 - Se procede al inicio de sesión con los datos validados
                        //y se obtiene el nuevo token con los datos completos de inicio de sesion
                        $obj_token = new Token();
                        $obj_token->token_ID = $token_id;
                        $obj_token->token_Val = $token_val;
                    
                        $objJSON = iniciarSesion($usuario_ci,$contrasenia,$obj_token);

                    }
                    // 4.1 - Si no hay una contraseña, se devuelve un error de Contraseña
                    else{
                        $objJSON = generarError(2);
                    }
                }
                // 3.1 - Si no hay token, se deriva la solicitud a la generación de token
                else{
                    $objJSON = generarToken($usuario_ci);
                    
                }
            }
            else{
                $objJSON = generarToken($usuario_ci);
            }
        }
    }
    
    // 7 - Se prepara la respuesta del servidor
    http_response_code(200);
    $JSON_string = TokenAJSON($objJSON);
    echo "$JSON_string";
    //$cosa = array_keys($_POST);
    
    //echo $objJSON;
}
else{
    // 1.1 - El servidor no responde ante solicitudes vacías
}
?>