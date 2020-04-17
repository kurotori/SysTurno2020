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

    /**
    * Obtiene los datos del perfil del usuario y los devuelve en un objeto
    */
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


    /**
    * Permite actualizar los datos del perfil de un usuario 
    */
    function actualizarDatosPerfilUsuario($usuario_ci, DatosUsuario $datosUsuario){
            $confirmacion = new Confirmacion();

            $conexion = GenerarConexion();
            try{
                // set the PDO error mode to exception
                $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                $consulta = "UPDATE Perfil SET nombre=:nombre, apellido=:apellido, telefono=:telefono, direccion=:direccion, email=:email, recibeSMS=:recibeSMS, recibeEmail=:recibeMail WHERE id = :usuario_ci";
                $sentencia = $conexion->prepare($consulta);
                $sentencia->bindParam(':usuario_ci', $usuario_ci);
                $sentencia->bindParam(':nombre', $datosUsuario->nombre);
                $sentencia->bindParam(':apellido', $datosUsuario->apellido);
                $sentencia->bindParam(':telefono', $datosUsuario->telefono);
                $sentencia->bindParam(':direccion', $datosUsuario->direccion);
                $sentencia->bindParam(':email', $datosUsuario->email);
                $sentencia->bindParam(':recibeMail', $datosUsuario->recibeEmail);
                $sentencia->bindParam(':recibeSMS', $datosUsuario->recibeSMS);

                $sentencia->execute();
                $confirmacion->estado="OK";
                $confirmacion->mensaje = "Datos o Preferencias actualizados con éxito.";

            }
            catch(PDOException $e){
                $confirmacion->estado =  "Error";
                $confirmacion->mensaje = $e->getMessage();

            }

            $conexion=null;
            return $confirmacion;
        }

/*FUNCIONES PARA GENERAR RECETAS*/

    /**Obtiene la lista de usuarios para un campo desplegable en la etiqueta SELECT 
    *de un formulario HTML.
    */
    function generarListaUsuariosParaSelect(){
         $conexion = GenerarConexion();
            try{
                // set the PDO error mode to exception
                $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                $consulta = "SELECT Usuario.ci, concat_ws(' ',Perfil.nombre,Perfil.apellido) FROM Usuario INNER JOIN Perfil WHERE Usuario.ci = Perfil.id";
                $sentencia = $conexion->prepare($consulta);
                $sentencia->execute();
                $resultado = $sentencia->fetchAll();
                echo "hasta aca OK";
                foreach($resultado as $dato){
                    echo "<option value='".$dato['ci']."'>".$dato[1]."</option>";
                }
    
            }
        
            catch(PDOException $e){
                echo "Error ".$e->getMessage();
            }

            $conexion=null;
    }
    
    /**
    *  Obtiene la lista de doctores para un campo desplegable en la etiqueta SELECT 
    *  
    */

    function generarListaDoctoresParaSelect(){
         $conexion = GenerarConexion();
            try{
                // set the PDO error mode to exception
                $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                $consulta = "SELECT Doctor.ci, concat_ws(' ',Doctor.nombre, Doctor.apellido), Doctor.especialidad FROM Doctor";
                $sentencia = $conexion->prepare($consulta);
                $sentencia->execute();
                $resultado = $sentencia->fetchAll();
                echo "hasta aca OK";
                foreach($resultado as $dato){
                    echo "<option value='".$dato['ci']."'>".$dato[1]."-".$dato[2]."</option>";
                }
            }
        
            catch(PDOException $e){
                echo "Error ".$e->getMessage();
            }

            $conexion=null;
    }

    /**Obtiene la lista de medicamentos para un campo desplegable en la etiqueta SELECT 
    *de un formulario HTML.
    */
    function generarListaMedicamentos(){
        $medicamentos = new ListadoMedicamentos();
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "SELECT id,nombre FROM Medicamento";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->execute();
            $resultado = $sentencia->fetchAll();

            foreach($resultado as $dato){
                $med = new Medicamento();
                $med->id = $dato['id'];
                $med->nombre = $dato['nombre'];
                array_push($medicamentos->medicamentos,$med);
                //echo "<option value='".$dato['ci']."'>".$dato[1]."</option>";

            }

        }

        catch(PDOException $e){
            $confirmacion->estado =  "Error";
            $confirmacion->mensaje = $e->getMessage();

        }

        $conexion=null;
        return $medicamentos;
    }

    /**
    * Genera una nueva receta VACÍA en el servidor
    */
    function nuevaReceta($doctor_ci,$usuario_ci,$fecha){
        $receta_id = 0;
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL nueva_receta(:doctor_ci,:usuario_ci,:fecha)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':doctor_ci', $doctor_ci);
            $sentencia->bindParam(':usuario_ci', $usuario_ci);
            $sentencia->bindParam(':fecha', $fecha);
            $sentencia->execute();
            
            $resultado = $sentencia->fetchAll();
            
            $receta_id = $resultado[0][0];
        }
        catch(PDOException $e){
            $receta_id = $e->getMessage();
        }

        $conexion=null;
        return $receta_id;
    }

    /**
    * Agrega un medicamento a una receta existente
    */
    function agregarMedicamento($receta_id,$medicamento_id,$cantidad){
        $confirmacion = new Confirmacion();
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL agregar_medicamento(:receta_id,:medicamento_id,:cantidad)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':receta_id', $receta_id);
            $sentencia->bindParam(':medicamento_id', $medicamento_id);
            $sentencia->bindParam(':cantidad', $cantidad);
            $sentencia->execute();
            
            $confirmacion->estado="OK";
            $confirmacion->mensaje="Datos agregados con éxito";
        }
        catch(PDOException $e){
            $confirmacion->mensaje = "Error: ".$e->getMessage();
            $confirmacion->estado = "ERROR";
        }

        $conexion=null;
        return $confirmacion;
    }

    /**
    * Busca las recetas de un usuario y las devuelve en un array
    */
    function buscarRecetasDeUsuario($usuario_ci){
        $recetas = new Recetas();
        
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL recetas_de_usuario(:usuario_ci)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':usuario_ci', $usuario_ci);
            $sentencia->execute();
            $resultado = $sentencia->fetchAll();
            
            foreach($resultado as $dato){
                $receta = new Receta();
                $receta->especialista = $dato['especialista'];
                $receta->fecha = $dato['fecha'];
                $receta->id = $dato['receta_id'];
                array_push($recetas->recetas,$receta);
            }
        }
        catch(PDOException $e){
            $confirmacion = new Confirmacion();
            $confirmacion->mensaje = "Error: ".$e->getMessage();
            $confirmacion->estado = "ERROR";
            array_push($recetas->recetas,$confirmacion);
        }
        
        $conexion=null;
        return $recetas;
        
    }


    /**
    * Genera un listado de los medicamentos recetados no entregados
    * El listado esta contenido en un array para su manejo.
    */
    function buscarMedicamentosRecetadosNoEnt($usuario_ci){
        $listado = new ListadoMedRecetados();
        
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "CALL medicamentos_recetados_no_ent(:usuario_ci)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':usuario_ci', $usuario_ci);
            $sentencia->execute();
            $resultado = $sentencia->fetchAll();
            
            //Preprocesado de los datos
            //Por cada dato encontrado en el resultado... 
            foreach($resultado as $dato){
                //Procesamos el medicamento
                $medicamento = new MedicamentoRecetado();
                $medicamento->receta_id = $dato['receta_id'];
                $medicamento->fecha = $dato['fecha'];
                $medicamento->especialista = $dato['especialista'];
                $medicamento->med_id = $dato['med_id'];
                $medicamento->med_nombre = $dato['med_nombre'];
                $medicamento->cantidad = (int)$dato['cantidad'];
                $stock = (int)$dato['stock'];
                //Procesamos la disponibilidad del medicamento
                if($stock<$medicamento->cantidad){
                    $medicamento->disponibilidad="NO DISPONIBLE";
                }
                else{
                    $medicamento->disponibilidad="DISPONIBLE";
                }
                $medicamento->entregado = $dato['estado'];
                //Agregamos el medicamento al array.
                array_push($listado->medicamentos,$medicamento);
            }
        }
        catch(PDOException $e){
            $confirmacion = new Confirmacion();
            $confirmacion->mensaje = "Error: ".$e->getMessage();
            $confirmacion->estado = "ERROR";
            array_push($listado->medicamentos,$confirmacion);
        }

        $conexion=null;
        return $listado;
    }

    

    /**
    * Permite ingresar un nuevo turno libre al sistema
    */
    function crearTurno(Turno $turno){
        $resultado = new Confirmacion();
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "INSERT INTO Turno(fechahora) VALUES (:fechahora)";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':fechahora', $turno->fechaHora);
            $sentencia->execute();
            
            $resultado->estado="OK";
            $resultado->mensaje="Turno en la fecha:'$turno->fechaHora' creado con éxito";
        }
        catch(PDOException $e){
            
            $resultado->mensaje = "Error: ".$e->getMessage();
            $resultado->estado = "ERROR";
        }

        $conexion=null;
        return $resultado;
    }


/**
    * Permite cerrar los turnos no usados en el sistema del día anterior
    */
    function cerrarTurnos(){
        $fechaHoy=new DateTime();
        $fechaAyer=$fechaHoy->sub(new DateInterval('P1D'));
        
        $anio=$fechaAyer->format("Y");
        $mes=$fechaAyer->format("m");
        $dia=$fechaAyer->format("d");
        
        $resultado = new Confirmacion();
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "UPDATE Turno SET estado='cancelado' WHERE estado='abierto' OR estado='confirmado' AND YEAR(fechahora)=:anio AND MONTH(fechahora)=:mes AND DAY(fechahora)=:dia";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':anio', $anio);
            $sentencia->bindParam(':mes', $mes);
            $sentencia->bindParam(':dia', $dia);
                        
            $sentencia->execute();
            
            $resultado->estado="OK";
            $resultado->mensaje="Turnos cerrados con éxito";
        }
        catch(PDOException $e){
            
            $resultado->mensaje = "Error: ".$e->getMessage();
            $resultado->estado = "ERROR";
        }

        $conexion=null;
        return $resultado;
    }



    /**
    * Permite buscar el primer turno libre en el sistema
    */
    function buscarTurno(){
        $turno = new Turno();
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "SELECT id, fechahora, estado FROM Turno WHERE estado='abierto' ORDER BY fechahora LIMIT 1";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->execute();
            $resultado = $sentencia->fetchAll();
            
            
            $turno->id = $resultado[0]['id'];
            $turno->estado = $resultado[0]['estado'];
            $turno->fechaHora = $resultado[0]['fechahora'];
        }
        catch(PDOException $e){
            
            $turno->id = "0";
            $turno->estado = "ERROR";
            $turno->fechaHora = "Error: ".$e->getMessage();
        }

        $conexion=null;
        return $turno;
    }


 /**
    * Permite buscar los datos de un turno específico
    */
    function buscarDatosTurno($turno_id){
        $turno = new Turno();
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "SELECT id, fechahora, estado FROM Turno WHERE id = :turno_id";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':turno_id', $turno_id);
            
            $sentencia->execute();
            $resultado = $sentencia->fetchAll();
            
            
            $turno->id = $resultado[0]['id'];
            $turno->estado = $resultado[0]['estado'];
            $turno->fechaHora = $resultado[0]['fechahora'];
        }
        catch(PDOException $e){
            
            $turno->id = "0";
            $turno->estado = "ERROR";
            $turno->fechaHora = "Error: ".$e->getMessage();
        }

        $conexion=null;
        return $turno;
    }


/**
    * Genera un listado de los turnos disponibles en el servidor
    * El listado esta contenido en un array para su manejo.
    */
    function buscarTurnos(){
        $listado = new Turnos();
        
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "SELECT id, fechahora, estado FROM Turno WHERE estado='abierto' ORDER BY fechahora";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->execute();
            $resultado = $sentencia->fetchAll();
            
            //Preprocesado de los datos
            //Por cada dato encontrado en el resultado... 
            foreach($resultado as $dato){
                //Procesamos el medicamento
                $turno = new Turno();
                $turno->id = $dato['id'];
                $turno->estado = $dato['estado'];
                $turno->fechaHora=$dato['fechahora'];
                //Agregamos el medicamento al array.
                array_push($listado->turnos,$turno);
            }
        }
        catch(PDOException $e){
            $confirmacion = new Confirmacion();
            $confirmacion->mensaje = "Error: ".$e->getMessage();
            $confirmacion->estado = "ERROR";
            array_push($listado->turnos,$confirmacion);
        }

        $conexion=null;
        return $listado;
    }

    /**
    * Busca todos los turnos reservados por el usuario sin importar su estado.
    * Devuelve un array
    */
    function buscarTurnosDelUsuario($usuario_ci){
        $turnos =  new Turnos();
        
        //Datos de turno predefinidos por si no hay turnos
        $turno = new Turno();
        $turno->estado="---";
        $turno->fechaHora="";
        $turno->id="0";
        
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "SELECT Genera.turno_id as 'turno_id', Turno.fechahora as 'fechahora', Turno.estado as 'estado' FROM Turno INNER JOIN Genera WHERE Turno.id = Genera.turno_id AND Genera.usuario_ci = :usuario_ci";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':usuario_ci', $usuario_ci);
            $sentencia->execute();
            
            if($sentencia->rowcount()>0){
                $resultado = $sentencia->fetchAll();
                foreach($resultado as $dato){
                    $t = new Turno();
                    $t->estado = $dato['estado'];
                    $t->fechaHora = $dato['fechahora'];
                    $t->id = $dato['turno_id'];
                    array_push($turnos->turnos,$t);
                }
            }
            else{
                array_push($turnos->turnos,$turno);
            }
        }
        catch(PDOException $e){
            $turno->estado = "ERROR";
            $turno->fechaHora = "Error: ".$e->getMessage();
            array_push($turnos->turnos,$turno);
        }
        
        $conexion=null;
        return $turnos;
    }

    /**
    * Busca turnos reservados por el usuario que se encuentren en estado 'confirmado'
    * Solo debería existir un turno por vez.
    */
    function buscarTurnoSolicitado($usuario_ci){
        $turno =  new Turno();
        //Datos de turno predefinidos por si no hay turnos
        $turno->estado="---";
        $turno->fechaHora="";
        $turno->id="0";
        
        $conexion = GenerarConexion();
        try{
            // set the PDO error mode to exception
            $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $consulta = "SELECT Genera.turno_id as 'turno_id', Turno.fechahora as 'fechahora', Turno.estado as 'estado' FROM Turno INNER JOIN Genera WHERE Turno.id = Genera.turno_id AND Genera.usuario_ci = :usuario_ci AND Turno.estado='confirmado'";
            $sentencia = $conexion->prepare($consulta);
            $sentencia->bindParam(':usuario_ci', $usuario_ci);
            $sentencia->execute();
            if($sentencia->rowcount()>0){
                $resultado = $sentencia->fetchAll();
                $turno->estado = $resultado[0]['estado'];
                $turno->fechaHora = $resultado[0]['fechahora'];
                $turno->id = $resultado[0]['turno_id'];
            }
        }
        catch(PDOException $e){
            $turno->estado = "ERROR";
            $turno->fechaHora = "Error: ".$e->getMessage();
        }
        
        $conexion=null;
        return $turno;
    }



/**
* Asigna una receta a un turno
*/
function asignarRecetaAturno($receta_id,$turno_id){
    $confirmacion = new Confirmacion();
    
    $conexion = GenerarConexion();
    try{
        // set the PDO error mode to exception
        $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $consulta = "INSERT INTO Asociado(receta_id,turno_id) VALUES (:receta_id,:turno_id)";
        $sentencia = $conexion->prepare($consulta);
        $sentencia->bindParam(':receta_id', $receta_id);
        $sentencia->bindParam(':turno_id', $turno_id);
        $sentencia->execute();
    }
    catch(PDOException $e){
        $confirmacion = new Confirmacion();
        $confirmacion->mensaje = "Error: ".$e->getMessage();
        $confirmacion->estado = "ERROR";
    }
    $conexion=null;
    return $confirmacion;
    
}



/**
* Asigna un todas las recetas de un usuario a un turno y lo asigna al usuario. 
* Devuelve una confirmación
*/
 function solicitarTurno($usuario_ci, $turno_id){
     $confirmacion = new Confirmacion();
     
     $turno_reservado= new Turno();
     $turno = new Turno();
     
     $turno_reservado = buscarTurnoSolicitado($usuario_ci);
     $turno = buscarDatosTurno($turno_id);
     
     //Si la id del turno_reservado es 0, entonces el usuario no tiene ningún turno reservado
     if(strcmp($turno_reservado->id,"0")==0){
         //Si el estado del turno seleccionado es abierto, se puede continuar
         if(strcmp($turno->estado,"abierto")==0){
             
        //Se deben conseguir todas las recetas del usuario
            $recetas = new Recetas();
            $recetas = buscarRecetasDeUsuario($usuario_ci);
            
             //Asigno las recetas al turno
             foreach($recetas->recetas as $receta){
                 asignarRecetaAturno($receta->id,$turno_id);
             }
            
            //Asigno el turno al usuario 
             
            $conexion = GenerarConexion();
            try{
                // set the PDO error mode to exception
                $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                $consulta = "CALL asignar_turno(:usuario_ci,:turno_id)";
                $sentencia = $conexion->prepare($consulta);
                $sentencia->bindParam(':usuario_ci', $usuario_ci);
                $sentencia->bindParam(':turno_id', $turno_id);
                $sentencia->execute();
                
                $confirmacion->estado="OK";
                $confirmacion->mensaje = "Se le ha asignado el turno $turno_id";
            }
            catch(PDOException $e){
                $confirmacion->mensaje = "Error: ".$e->getMessage();
                $confirmacion->estado = "ERROR";
            }
             $conexion = null;
         }
         else{
             $confirmacion->estado="ERROR";
             $confirmacion->mensaje="El turno ".$turno_id." ya no se encuentra disponible. Por favor actualice la lista de turnos y pruebe nuevamente.";
         }
     }
     else{
         $confirmacion->estado="ERROR";
         $confirmacion->mensaje="Usted ya tiene un turno asignado en el sistema. Utilice o cancele ese turno para seleccionar un turno nuevo.";
     }
     
     return $confirmacion;
 }


/**
* Asigna un todas las recetas de un usuario a un turno y lo asigna al usuario. 
* Devuelve una confirmación
*/
 function probarSolicitarTurno($usuario_ci, $turno_id){
     $confirmacion = new Confirmacion();
     
     $turno_reservado= new Turno();
     $turno = new Turno();
     
     $turno_reservado = buscarTurnoDelUsuario($usuario_ci);
     $turno = buscarDatosTurno($turno_id);
     
     //Si la id del turno_reservado es 0, entonces el usuario no tiene ningún turno reservado
     if(strcmp($turno_reservado->id,"0")==0){
         //Si el estado del turno seleccionado es abierto, se puede continuar
         if(strcmp($turno->estado,"abierto")==0){
             
        //Se deben conseguir todas las recetas del usuario
            $recetas = new Recetas();
            $recetas = buscarRecetasDeUsuario($usuario_ci);
            
             //Asigno las recetas al turno
             foreach($recetas->recetas as $receta){
                 echo $receta->id;
                // asignarRecetaAturno($receta->id,$turno_id);
             }
            
            //Asigno el turno al usuario 
         }
         else{
             $confirmacion->estado="ERROR";
             $confirmacion->mensaje="El turno ".$turno_id." ya no se encuentra disponible. Por favor actualice la lista de turnos y pruebe nuevamente.";
         }
     }
     else{
         $confirmacion->estado="ERROR";
            $confirmacion->mensaje="Usted ya tiene un turno asignado en el sistema. Utilice o cancele ese turno para seleccionar un turno nuevo.";
     }
     
     return $confirmacion;
 }



/**
* Buscar las recetas asociadas a un turno
*/
function buscarRecetasAsociadas($turno_id){
    $recetas = new Recetas();
    
    $conexion = GenerarConexion();
    try{
        // set the PDO error mode to exception
        $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $consulta = "SELECT Receta.id as 'receta_id',Receta.fecha as 'receta_fecha',
(SELECT CONCAT_WS('-',CONCAT_WS(' ',Doctor.nombre,Doctor.apellido),Doctor.especialidad)
FROM Doctor	WHERE Doctor.ci = (SELECT Entrega.doctor_ci FROM Entrega WHERE Entrega.receta_id = Receta.id)) as 'especialista' FROM Receta INNER JOIN Asociado WHERE Receta.id = Asociado.receta_id AND Asociado.turno_id = :turno_id";
        $sentencia = $conexion->prepare($consulta);
        $sentencia->bindParam(':turno_id', $turno_id);
        $sentencia->execute();

        if($sentencia->rowcount()>0){
            $resultado = $sentencia->fetchAll();
            foreach($resultado as $dato){
                $r = new Receta();
                $r->especialista = $dato['especialista'];
                $r->fecha = $dato['receta_fecha'];
                $r->id = $dato['receta_id'];
                array_push($recetas->recetas,$r);
            }
        }
    }
    catch(PDOException $e){
        $r = new Receta();
        $r->especialista = "ERROR";
        $r->fecha = "Error: ".$e->getMessage();
        array_push($recetas->recetas,$r);
    }
    $conexion = null;
    return $recetas;
        
}


/**
* Buscar todos los medicamentos de una receta
*/
function buscarMedicamentosDeReceta($receta_id){
    $medicamentos_r = array();
    
    $conexion = GenerarConexion();
    try{
        // set the PDO error mode to exception
        $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $consulta = "SELECT Medicamento.nombre as 'med_nombre',
Medicamento.id as 'med_id',Contiene.cantidad as 'cantidad', Contiene.entregado as 'entregado', Medicamento.stock as 'stock' FROM Medicamento INNER JOIN Contiene WHERE Medicamento.id = Contiene.medicamento_id AND Contiene.receta_id = :receta_id";
        $sentencia = $conexion->prepare($consulta);
        $sentencia->bindParam(':receta_id', $receta_id);
        $sentencia->execute();

        if($sentencia->rowcount()>0){
            $resultado = $sentencia->fetchAll();
            foreach($resultado as $dato){
                $m = new MedicamentoDeLaReceta();
                $m->cantidad = $dato['cantidad'];
                $m->entregado = $dato['entregado'];
                $m->med_nombre = $dato['med_nombre'];
                $m->med_id = $dato['med_id'];
                $stock = (int)$dato['stock'];
                //Procesamos la disponibilidad del medicamento
                if($stock<$m->cantidad){
                    $m->disponibilidad="NO DISPONIBLE";
                }
                else{
                    $m->disponibilidad="DISPONIBLE";
                }
                array_push($medicamentos_r,$m);
            }
        }
    }
    catch(PDOException $e){
        $m = new MedicamentoDeLaReceta();
        $m->med_nombre = "ERROR";
        $m->entregado = "Error: ".$e->getMessage();
        array_push($medicamentos_r,$m);
    }
    $conexion = null;
    return $medicamentos_r;
}

/**
* Buscar todos los datos de un turno solicitado
*/
function buscarDatosDeTurnoSolicitado($usuario_ci){
    //1 - Buscamos el turno solicitado por el usuario
    $turno_s = new TurnoSolicitado();
    $turno = new Turno();
    $turno = buscarTurnoSolicitado($usuario_ci);
    
    $turno_s->id = $turno->id;
    $turno_s->estado = $turno->estado;
    $turno_s->fechaHora = $turno->fechaHora;
    
    //2 - Buscamos todas las recetas asociadas a ese turno
    $recetas = new Recetas();
    $recetas = buscarRecetasAsociadas($turno->id);
    
    //3 - Asociamos los datos de cada receta a un nuevo objeto 
    // y buscamos todos los medicamentos de cada receta y los asociamos a ese objeto
    foreach($recetas->recetas as $receta){
        $r = new RecetaDelTurno();
        $r->especialista = $receta->especialista;
        $r->fecha = $receta->fecha;
        $r->id = $receta->id;
        $r->medicamentos = buscarMedicamentosDeReceta($receta->id);
        array_push($turno_s->recetasDelTurno,$r);
    }
    return $turno_s;
}

/**
* Cancelar el turno solicitado por un usuario
*/
function cancelarTurnoSolicitado($usuario_ci){
    //1 - Buscamos el turno solicitado por el usuario
    $confirmacion = new Confirmacion();
    $turno_s = new TurnoSolicitado();
    $turno_s = buscarDatosDeTurnoSolicitado($usuario_ci);
    
    $turno_id = $turno_s->id;
    $conexion = GenerarConexion();
    try{
        // set the PDO error mode to exception
        $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $consulta ="UPDATE Turno SET Turno.estado='cancelado' WHERE Turno.id=:turno_id";
        $sentencia = $conexion->prepare($consulta);
        $sentencia->bindParam(':turno_id', $turno_id);
        $sentencia->execute();
        
        $confirmacion->estado="OK";
        $confirmacion->mensaje="El turno $turno_id se ha cancelado";
    }
    catch(PDOException $e){
        $confirmacion->mensaje = "Error: ".$e->getMessage();
        $confirmacion->estado = "ERROR";
    }
    $conexion = null;
    return $confirmacion;
}
?>