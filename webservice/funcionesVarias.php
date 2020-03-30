<?php
    include_once "conexionbd.php";
    include_once "clases.php";
    
    //Valida y limpia los datos que provienen del POST para
    // evitar inyecciones SQL
    function validarDatos($datos){
        $datos = trim($datos);
        $datos = stripslashes($datos);
        $datos = htmlspecialchars($datos);
        return $datos;
    }

    //Recibe una contraseña y devuelve un hash de la misma
    function crearHash($contrasenia){
        $hashC = password_hash($contrasenia,PASSWORD_DEFAULT);
        return $hashC;
    }
    //Pasa un objeto Token a JSON
    function TokenAJSON(Token $objToken){
        $jsonDatos = new stdClass;
        $jsonDatos=array("Token"=>$objToken);
        
        $objJSON = json_encode($jsonDatos);

        return $objJSON;
    }

    //Pasa un objeto a JSON
    function ObjAJSON($objeto){
        $jsonDatos = new stdClass;
        $nombreClase = get_class($objeto);
        $jsonDatos=array("$nombreClase"=>$objeto);
        $objJSON = json_encode($jsonDatos);
        return $objJSON;
    }

    //Pasa un objeto SesionValida a JSON
    function SesionValidaAJSON(SesionValida $objSV){
        $jsonDatos = new stdClass;
        $jsonDatos=array("SesionValida"=>$objSV);
        
        $objJSON = json_encode($jsonDatos);

        return $objJSON;
    }

    //Comprueba la identidad del usuario y si es válido, 
    //registra y entrega objeto conteniendo un token de acceso al sistema
    function generarToken($usuario_ci){
        $chequeo = chequearUsuario($usuario_ci);
        
        $tiempoUnix = time();

        $obj_token = new Token();
        
        if( $chequeo ){
            $dato_string = "$usuario_ci.$chequeo.$tiempoUnix";
            $obj_token->token_Val= password_hash($dato_string, PASSWORD_BCRYPT);
            
            $id = registrarToken($obj_token->token_Val,$usuario_ci);
            $obj_token->tipo = "TOKEN";
            $obj_token->mensaje = "OK";
            $obj_token->token_ID = $id;
        }
        else{
            $obj_token=generarError(1);
        }
        
        //$objJSON = TokenAJSON($obj_token);

        return $obj_token;
    }

    //Esta función es solo con fines de testeo
    function escribirEnArchivo($dato){
            $archivo = fopen("entrada.txt", "w") or die("Unable to open file!");
            fwrite($archivo,$dato);
            fclose($archivo);
    }

    //Crea una sesión en el servidor de acuerdo a los datos provistos
    //y devuelve el token con la idSesion y su identificador correspondientes, o un error.
    function iniciarSesion($usuario_ci, $contrasenia, $obj_token){
        $nuevo_token = new Token();
        
        $tiempoUnix = time();
        
        //Se valida el token, si es válido, se deben bajar los datos del usuario
        // para autenticar la sesión
        if( validarToken($obj_token,$usuario_ci) ){
            $datosLogin = buscarDatosLoginUsuario($usuario_ci);
            
            //Se verifica la contraseña y si es válida, se genera el token de sesion
            //y se lo registra en el servidor
            if( password_verify($contrasenia,$datosLogin->hash) ){
                $sesion_string = "$usuario_ci.$obj_token->token_Val.$tiempoUnix";
                $sesion_val = password_hash($sesion_string, PASSWORD_BCRYPT);
                
                $sesion_ID = registrarSesion($usuario_ci,$obj_token,$sesion_val);
                
                $nuevo_token->tipo = "SESION";
                $nuevo_token->token_ID = $obj_token->token_ID;
                $nuevo_token->token_Val = $obj_token->token_Val;
                $nuevo_token->sesion_ID = $sesion_ID;
                $nuevo_token->sesion_Val = $sesion_val;
                $nuevo_token->mensaje = "OK";
            }
            else{
                //Error de contraseña
                $nuevo_token = generarError(3);
            }
        }
        else{
            //Error de validez de Token
            $nuevo_token = generarError(4);
        }
        
        return $nuevo_token;
    }


    //Confirma la validez de un token ante los registros del servidor.
    //Un token es válido si: 
    // 1 - esta registrado al usuario
    // 2 - no ha expirado
    // 3 - no ha sido invalidado por un cierre de sesión
    // 4 - su valor alfanumérico es el mismo que el almacenado en el servidor
    //Provisoriamente se establece que un token es válido durante 1 minuto.    
    function validarToken($obj_token,$usuario_CI){
        //$minutosValidez = 10;
        $resultado = false;
        $token_ID = $obj_token->token_ID;
        
        $datosToken = new DatosToken();
        $datosToken = buscarDatosToken($token_ID);
        //echo $datosToken->token_Val;
        //1 - Chequeo del estado
        //if ($datosToken->estado == 'activo'){
            //echo "token activo ok|";
            //2 - Chequeo de registro del token
            if( strcmp($datosToken->usuario_CI,$usuario_CI) == 0 ){
                //echo "usuario ok|";
                //3 - Chequeo de expiración
                //$marcaTiempo = new DateTime($datosToken->marcaTiempo);
                    //date( 'd m Y H i',strtotime($datosToken->marcaTiempo) );
                //$ahora = new DateTime('now');
                //$diferencia = $ahora->diff($marcaTiempo)->format('%i');
                    //date_diff($marcaTiempo,$ahora)->format('%i');
                
                //if( ($minutosValidez-$diferencia)>0 ){
                    //echo "tiempo ok|";
                    //Chequeo de valor alfanumérico
                    if(strcmp($obj_token->token_Val,$datosToken->token_Val)==0){
                        //echo "val ok";
                        $resultado = true;
                    }
               // }
            }
        //}
        return $resultado;
    }


    //Confirma la validez de una sesión
   // function validarSesion($obj_sesion,$obj_token,$usuario_CI){
        
    //}
        
        
        
    //Permite generar objetos Token con mensajes de error
    //de acuerdo a códigos de error predefinidos
    //
    function generarError($codigo){
        $obj_token = new Token();
        $obj_token->tipo = "ERROR";
        $obj_token->token_Val = "0";
        $obj_token->token_ID = "0";
        $obj_token->sesion_ID = "0";
        $obj_token->sesion_Val = "0";
        
        switch($codigo){
            case 1:
                $obj_token->mensaje = "Usuario desconocido";
                break;
            case 2:
                $obj_token->mensaje = "Debe ingresar una contraseña";
                break;
            case 3:
                $obj_token->mensaje = "Contraseña Incorrecta";
                break;
            case 4:
                $obj_token->mensaje = "Token Inválido";
                break;
            case 5:
                $obj_token->mensaje = "Ya existe otra sesión activa";
                break;
        }
        
        return $obj_token;
    }
?>