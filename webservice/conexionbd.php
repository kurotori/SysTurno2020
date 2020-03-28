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
            $consulta = "SELECT count(*) FROM usuario WHERE CI = :ci_usuario";
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
            $consulta = "SELECT ci,hash FROM usuario WHERE ci = :usuario_ci";
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
            echo "OK";
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
    
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
                }
            }
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $sesionValida;
    }










    function crearPartida($conexion,$nombre,$tamanio,$usuario){
        $num_consulta=0;
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL nueva_partida(:tamanio,:nombre,:usuario)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':nombre', $nombre);
            $sentencia->bindParam(':tamanio', $tamanio);
            $sentencia->bindParam(':usuario', $usuario);
            
            $sentencia->execute();
            $resultado = $sentencia->fetchAll();
            //echo $resultado[0][0];
            //print_r ($resultado);
            $num_consulta = $resultado[0][0];
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $num_consulta;
    }

    function crearBarco($conexion,$tipo){
        $id_barco=0;
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL nuevo_barco(:tipo)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':tipo', $tipo);
            
            $sentencia->execute();
            $resultado = $sentencia->fetchAll();
            //echo $resultado[0][0];
            //print_r ($resultado);
            $id_barco = $resultado[0][0];
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $id_barco;
    }


    function crearYasignarBarco($conexion,$barco,$idUsuario,$idPartida){
        $id_barco=0;
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL crear_y_asignar_barco(:barco,:idUsuario,:idPartida)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':barco', $barco);
            $sentencia->bindParam(':idUsuario', $idUsuario);
            $sentencia->bindParam(':idPartida', $idPartida);
            
            $sentencia->execute();
            $resultado = $sentencia->fetchAll();
            $id_barco = $resultado[0][0];
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $id_barco;
    }

    function crearYasignarCeldaABarco($conexion,$idBarco,$celda){
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL crear_y_asignar_celda_a_barco(:idBarco,:celda)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':idBarco', $idBarco);
            $sentencia->bindParam(':celda', $celda);
            
            $sentencia->execute();
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
    }

    
/*    function iniciarSesion($id_usuario){
        $sesion = new \stdClass();
        $sesion->id = 0;
        $sesion->mensaje = "";
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL iniciar_sesion(:idUsuario)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':idUsuario', $id_usuario);
            
            $sentencia->execute();
            $resultado = $sentencia->fetchAll();
            $sesion->id = $resultado[0][0];
            $sesion->mensaje = "OK";
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $sesion;
    }*/

    function cerrarSesion($id_sesion){
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "UPDATE sesion SET estado = 'cerrada' WHERE ID = :id_sesion";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':id_sesion', $id_sesion);
            
            $sentencia->execute();
        }
        catch(PDOException $e){
            echo "Error: " . $e->getMessage();
        }

        $conexion=null;
    }

    function nombreUsuarioExiste($nombre_usuario){
        $existe = false;
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "SELECT count(*) FROM usuario where nombre_u=:nombre_u";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':nombre_u', $nombre_usuario);
            
            $sentencia->execute();
            $resultado = $sentencia->fetchAll();
            if($resultado[0][0] > 0){
                $existe = true;
            }
        }
        catch(PDOException $e){
            //echo "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $existe;
    }

    function idUsuarioExiste($id_usuario){
        $existe = false;
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "SELECT count(*) FROM usuario where ID=:id_u";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':id_u', $id_usuario);
            
            $sentencia->execute();
            $resultado = $sentencia->fetchAll();
            if($resultado[0][0] > 0){
                $existe = true;
            }
        }
        catch(PDOException $e){
            //echo "Error: " . $e->getMessage();
        }

        $conexion=null;
        return $existe;
    }

    function datosDelUsuario($nombre_usuario){
        $datos_usuario = new \stdClass();
        $datos_usuario->id_usuario=0;
        $datos_usuario->nombre_u="";
        $datos_usuario->nombre_p="";
        $datos_usuario->apellido_p="";
        $datos_usuario->fecha_reg="";
        $datos_usuario->fecha_nac="";
        $datos_usuario->hash="";
        $datos_usuario->error="";
        
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "SELECT id,nombre_u,nombre_p,apellido_p,fecha_reg,fecha_nac,hash FROM usuario where nombre_u = :nombre_u";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':nombre_u', $nombre_usuario);
            
            $sentencia->execute();
            $resultado = $sentencia->fetchAll();
            
           //print_r($resultado);
            
            //echo ":::===>> ".($resultado[0]['nombre_u']);
            $datos_usuario->id_usuario=$resultado[0]['id'];
            $datos_usuario->nombre_u=$resultado[0]['nombre_u'];
            $datos_usuario->nombre_p=$resultado[0]['nombre_p'];
            $datos_usuario->apellido_p=$resultado[0]['apellido_p'];
            $datos_usuario->fecha_reg=$resultado[0]['fecha_reg'];
            $datos_usuario->fecha_nac=$resultado[0]['fecha_nac'];
            $datos_usuario->hash=$resultado[0]['hash'];
            $datos_usuario->error="OK";
        }
        catch(PDOException $e){
            $datos_usuario->error="Error: ". $e->getMessage();
            
        }

        $conexion=null;
        return $datos_usuario;
    }

    
    /*function registrarUsuario($id_usuario, 
                              $nombre_u, 
                              $nombre_p, 
                              $apellido_p,
                              $fecha_nac,
                              $hash
                             ){
        
        $resultado = new \stdClass();
        $resultado->error=0;
        $resultado->mensaje="";
        
        if(idUsuarioExiste($id_usuario)){
            $resultado->error=14;
            $resultado->mensaje="CI de usuario ya registrado";
        }
        elseif(nombreUsuarioExiste($nombre_u)){
            $resultado->error=13;
            $resultado->mensaje="Nombre de usuario ya registrado";
        }
        else{
            
            $conexion = GenerarConexion();
            try{
                // set the PDO error mode to exception
                $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                $consulta = "INSERT INTO usuario(ID, nombre_u, nombre_p, apellido_p, fecha_nac, hash) values (:id, :nombre_u, :nombre_p, :apellido_p, :fecha_nac, :hash)";
                $sentencia = $conexion->prepare($consulta);
                $sentencia->bindParam(':id', $id_usuario);
                $sentencia->bindParam(':nombre_u', $nombre_u);
                $sentencia->bindParam(':nombre_p', $nombre_p);
                $sentencia->bindParam(':apellido_p', $apellido_p);
                $sentencia->bindParam(':fecha_nac', $fecha_nac);
                $sentencia->bindParam(':hash', $hash);

                $sentencia->execute();
                $resultado->error=0;
                $resultado->mensaje="OK, Usuario ".$nombre_u."(".$id_usuario.") Registrado";
            }
            catch(PDOException $e){
                $resultado->error=1;
                $resultado->mensaje="Error: ". $e->getMessage();
            }

            $conexion=null;
        }
        
        return $resultado;
    }*/

?>