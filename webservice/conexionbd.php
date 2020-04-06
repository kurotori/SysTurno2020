<?php
include_once "datosbd.php";
include_once "clases.php";
include_once "funcionesVarias.php";

    function consultaDB($consulta) {
        // Connect to the database
        $conexion = GenerarConexion_MSYQLI();
        // Query the database
        $result = mysqli_query($conexion,$consulta);
        mysqli_close($conexion);
        return $result;
    }

    
    
    
    function chequearUsuario($ci_usuario){
        $resultado = false;
        $conexion = GenerarConexion();
        
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "SELECT count(*) FROM Usuario WHERE CI = :ci_usuario";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':ci_usuario', $ci_usuario);
            
            $sentencia->execute();
            $respuesta = $sentencia->fetchAll();
            $cantidad = $respuesta[0][0];
            
            if($cantidad > 0){
                $resultado = true;
            }
            
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $resultado;
    }


    //Recibe un token_Val y los datos del usuario y los registra en el sistema
    //asignando el token al usuario, devuelve el .
    function registrarToken($token_Val,$usuario_ci){
        $num_token = 0;
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL registrar_token(:usuarioCi,:tokenVal)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':usuarioCi', $usuario_ci);
            $sentencia->bindParam(':tokenVal', $token_Val);
            
            $sentencia->execute();
            
            $resultado = $sentencia->fetchAll();
           
            $num_token = $resultado[0][0];
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $num_token;
    }

    //Recibe los datos de sesión y los registra en el servidor, devolviendo
    //la ID numérica de la sesión en la base de datos 
    //(no confundir con el valor de la sesión, que es alfanumérico).
    function registrarSesion($usuario_ci,$obj_token,$sesion_val){
        $sesion_ID = 0;
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL registrar_sesion(:ciUsuario,:num_token,:sesionVal)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':ciUsuario', $usuario_ci);
            $sentencia->bindParam(':num_token', $obj_token->token_ID);
            $sentencia->bindParam(':sesionVal', $sesion_val);
            
            $sentencia->execute();
            
            $resultado = $sentencia->fetchAll();
           
            $sesion_ID = $resultado[0][0];
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $sesion_ID;
    }


    //Busca y obtiene los datos de un token (marca de tiempo, dueño, estado)
    //y los devuelve en un objeto para  su verificación
    function buscarDatosToken($num_identificador){
        $datosToken = new DatosToken();
        
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL buscar_datos_token(:num_token)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':num_token', $num_identificador);
            
            $sentencia->execute();
            
            $resultado = $sentencia->fetchAll();
           
            /*
            public $usuario_CI="";
            public $token_Val="";
            public $num_ID="";
            public $marcaTiempo="";
            public $estado="";
            */
            
            $datosToken->usuario_CI = $resultado[0][0];
            $datosToken->token_Val = $resultado[0][1];
            $datosToken->num_ID = $resultado[0][2];
            $datosToken->marcaTiempo = $resultado[0][3];
            //$datosToken->estado = $resultado[0][4];
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $datosToken;
    }

    //Permite ubicar los datos de un token en posesión de un usuario
    // y devuelve un objeto con esos datos
    //function buscarToken($token_id, $usuario_ci){
        //NOTA: función en evaluación
       // $obj_token = new Token();
        
        
        
       // return $obj_token;
   // }

    
//Busca los datos del usuario en la BdD y devuelve un objeto con esos datos
function buscarDatosLoginUsuario($usuario_CI){
    $datosLogin = new DatosLogin();
    
    $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "SELECT ci,hash FROM Usuario WHERE ci = :usuario_ci";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':usuario_ci', $usuario_CI);
            
            $sentencia->execute();
            
            $resultado = $sentencia->fetchAll();
           
            $datosLogin->usuario_CI = $resultado[0][0];
            $datosLogin->hash = $resultado[0][1];
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
    
    return $datosLogin;
}
    
    //Registra un usuario en la BdD a partir de un objeto con los datos
    function registrarUsuario(DatosUsuario $datosUsuario){
        $resultado = "";
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL registrar_usuario(:usuario_ci,:hash,:nombre,:apellido,:telefono,:direccion,:email)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':usuario_ci', $datosUsuario->usuario_CI);
            $sentencia->bindParam(':hash', $datosUsuario->hash);
            $sentencia->bindParam(':nombre', $datosUsuario->nombre);
            $sentencia->bindParam(':apellido', $datosUsuario->apellido);
            $sentencia->bindParam(':telefono', $datosUsuario->telefono);
            $sentencia->bindParam(':direccion', $datosUsuario->direccion);
            $sentencia->bindParam(':email', $datosUsuario->email);
            
            $sentencia->execute();
            $resultado = "Usuario ".$datosUsuario->usuario_CI." registrado con éxito";
        }
        catch(PDOException $e){
            $resultado = "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $resultado;
    
    }
    
    //Recibe los datos de sesión del usuario y los valida contra la BdD
    function validarSesion($usuario_ci, $token_val, $sesion_val){
        $sesionValida = new SesionValida();
        $sesionValida->valida = "false";
        
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL validar_sesion(:usuarioCi, :tokenVal, :sesionVal)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':usuarioCi', $usuario_ci);
            $sentencia->bindParam(':tokenVal', $token_val);
            $sentencia->bindParam(':sesionVal', $sesion_val);
            
            $sentencia->execute();
            
            $resultado = $sentencia->fetchAll();
           
            $sesiones = $resultado[0][0];
            $tokens = $resultado[0][1];
            $vinculos = $resultado[0][2];
           
            if($sesiones == 1){
                if($tokens == 1){
                    if($vinculos == 1){
                        $sesionValida->valida = "true";
                    }
                    else{
                        $sesionValida->valida = "false";
                    }
                }
                else{
                    $sesionValida->valida = "false";
                }
            }
            else{
                $sesionValida->valida = "false";
            }
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $sesionValida;
    }


    //Recibe los datos de sesión del usuario y los valida contra la BdD
    //Para uso interno en otras funciones. Devuelve 'true' si la sesión es válida
    function validarSesionInterno($usuario_ci, $token_val, $sesion_val){
        $sesionValida = "false";
        
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL validar_sesion(:usuarioCi, :tokenVal, :sesionVal)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':usuarioCi', $usuario_ci);
            $sentencia->bindParam(':tokenVal', $token_val);
            $sentencia->bindParam(':sesionVal', $sesion_val);
            
            $sentencia->execute();
            
            $resultado = $sentencia->fetchAll();
           
            $sesiones = $resultado[0][0];
            $tokens = $resultado[0][1];
            $vinculos = $resultado[0][2];
           
            if($sesiones == 1){
                if($tokens == 1){
                    if($vinculos == 1){
                        $sesionValida = "true";
                    }
                    else{
                        $sesionValida = "false";
                    }
                }
                else{
                    $sesionValida = "false";
                }
            }
            else{
                $sesionValida = "false";
            }
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $sesionValida;
    }



    //Cierra TODAS las sesiones abiertas del usuario en en el servidor
    function cerrarSesiones($usuario_ci){
        $resultado = false;
         $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL cerrar_sesiones(:usuarioCi)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':usuarioCi', $usuario_ci);
            
            $sentencia->execute();
            $resultado = true;
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $resultado;
        
    }

    //Obtiene los datos del perfil del usuario y los devuelve en un objeto
    function buscarDatosPerfilUsuario($usuario_ci){
            $datos_usuario = new DatosUsuario();

            $conexion = GenerarConexion();
            try{
                // set the PDO error mode to exception
                $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                $consulta = "SELECT id,nombre,apellido,telefono,direccion,email,recibeSMS,recibeEmail FROM Perfil where id = :usuario_ci";
                $sentencia = $conexion->prepare($consulta);
                $sentencia->bindParam(':usuario_ci', $usuario_ci);

                $sentencia->execute();
                $resultado = $sentencia->fetchAll();

               //print_r($resultado);

                //echo ":::===>> ".($resultado[0]['nombre_u']);
                $datos_usuario->usuario_CI = $resultado[0]['id'];
                $datos_usuario->nombre = $resultado[0]['nombre'];
                $datos_usuario->apellido = $resultado[0]['apellido'];
                $datos_usuario->telefono = $resultado[0]['telefono'];
                $datos_usuario->direccion = $resultado[0]['direccion'];
                $datos_usuario->email = $resultado[0]['email'];
                $datos_usuario->recibeSMS = $resultado[0]['recibeSMS'];
                $datos_usuario->recibeEmail = $resultado[0]['recibeEmail'];

            }
            catch(PDOException $e){
                $datos_usuario->nombre="Error: ". $e->getMessage();

            }

            $conexion=null;
            return $datos_usuario;
        }




?>