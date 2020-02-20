<?php
include_once "datosbd.php";
//    
//    $conexion = mysqli_connect($servidor,$usuario,$contraseña,$bdd);
//
//    if($conexion===false){
//        echo "Error de Conexion: ".mysqli_connect_error();
//    }
//    else{
//        echo "Conexión Existosa";
//    }

    //function CrearConexion($servidor,$usuario,$contraseña,$bdd){
       // $conexion = new PDO("mysql:host=$servidor;dbname=$bdd", $usuario, $contraseña);
            //new mysqli($servidor, $usuario, $contraseña, $bdd);
        //mysqli_connect($servidor,$usuario,$contraseña,$bdd);
        //if($conexion->connect_error){
           // die("ERROR:  " . $conexion->connect_error);
            //echo "Error de Conexion: ".mysqli_connect_error();
        //}
        //else{
            //echo "Conexión Exitosa";
            //return $conexion;
        //}
       // return $conexion;
    //}


    function consultaDB($consulta) {
        // Connect to the database
        $conexion = GenerarConexion_MSYQLI();
        // Query the database
        $result = mysqli_query($conexion,$consulta);
        mysqli_close($conexion);
        return $result;
    }

    
    function validarDatos($datos){
        $datos = trim($datos);
        $datos = stripslashes($datos);
        $datos = htmlspecialchars($datos);
        return $datos;
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

    
    function iniciarSesion($id_usuario){
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
    }

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

    
    function registrarUsuario($id_usuario, 
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
    }

?>