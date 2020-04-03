<?php
include_once "../conexionbd.php";
include_once "../funcionesVarias.php";
include_once "../clases.php";

    // required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");


if($_POST){
    $confirmacion = new Confirmacion();
    
    if(isset($_POST["usuario_ci"]) && !(empty( $_POST["usuario_ci"] ))){
        $usuario_ci = validarDatos($_POST["usuario_ci"]);
        
        //Si hay un valor de token en los datos enviados
        // se procede con el registro, comprobando cada dato
        if(isset($_POST["token_val"]) && !( empty($_POST["token_val"]) )){
            $token_val = $_POST["token_val"];
            
            //Si hay un token_id
            if(isset($_POST["token_id"]) && !( empty($_POST["token_id"]) )){
                $token_id = $_POST["token_id"];
                $token = new Token();
                $token->token_Val = $token_val;
                $token->token_ID = $token_id;
            
                //Se valida el token
                $chequeo_token = validarToken($token,"11110007");
                if($chequeo_token){
                    
                    //Si hay una contraseña, de genera un hash
                    if(isset($_POST["contrasenia"]) && !(empty( $_POST["contrasenia"] ))){
                        $contrasenia = validarDatos($_POST["contrasenia"]);
                        $hash = crearHash($contrasenia);

                        //Si hay un nombre, se valida y se añade a la variable correspondiente
                        if(isset($_POST["nombre"]) && !(empty( $_POST["nombre"] )) ){
                            $nombre = validarDatos($_POST["nombre"]);

                            //Si hay un apellido, se valida y se lo añade a la variable correspondiente
                            if(isset($_POST["apellido"]) && !(empty( $_POST["apellido"] ))){
                                $apellido = validarDatos($_POST["apellido"]);

                                 //Si hay un teléfono, se valida y se lo añade a la variable correspondiente
                                if(isset($_POST["telefono"]) && !(empty( $_POST["telefono"] )) ){
                                    $telefono = validarDatos($_POST["telefono"]);

                                    //Si hay una dirección
                                    if(isset($_POST["direccion"]) && !(empty($_POST["direccion"])) ){
                                        $direccion = validarDatos($_POST["direccion"]);

                                        //si hay un email, se completa el procedimiento
                                        if(isset($_POST["email"])){
                                            $email = validarDatos($_POST["email"]);

                                            $datosUsuario =  new DatosUsuario();
                                            $datosUsuario->usuario_CI = $usuario_ci;
                                            $datosUsuario->hash = $hash;
                                            $datosUsuario->nombre = $nombre;
                                            $datosUsuario->apellido = $apellido;
                                            $datosUsuario->telefono = $telefono;
                                            $datosUsuario->direccion = $direccion;
                                            $datosUsuario->email = $email;

                                            $mensaje = registrarUsuario($datosUsuario);

                                            $confirmacion->estado = "OK";
                                            $confirmacion->mensaje = $mensaje;
                                        }
                                        else{
                                            $confirmacion->estado = "ERROR";
                                            $confirmacion->mensaje = "Debe ingresar un email";
                                        }
                                    }
                                    //Si no hay dirección
                                    else{
                                        $confirmacion->estado = "ERROR";
                                        $confirmacion->mensaje="Debe ingresar una dirección";
                                    }
                                }
                                //Si no hay teléfono
                                else{
                                    $confirmacion->estado = "ERROR";
                                    $confirmacion->mensaje="Debe ingresar un teléfono";
                                }
                            }
                            //Si no hay apellido se genera un error
                            else{
                                $confirmacion->estado = "ERROR";
                                $confirmacion->mensaje="Debe ingresar un apellido";
                            }
                        }
                        //Si no hay un nombre, se añade un error a la confirmación
                        else{
                            $confirmacion->estado = "ERROR";
                            $confirmacion->mensaje="Debe ingresar un nombre";
                        }
                    }
                    //Si no hay contraseña, se añade un error a la confirmación
                    else{
                        $confirmacion->estado = "ERROR";
                        $confirmacion->mensaje = "Debe ingresar una contraseña";
                    }
                }
                //Si el token no es válido
                else{
                    $confirmacion->estado = "ERROR";
                    $confirmacion->mensaje = "Token Inválido";
                }
            }
            //Si no hay token_id    
            else{
                $confirmacion->estado = "ERROR";
                $confirmacion->mensaje = "No hay token_ID";
            }
            
            //Se procesa el objeto 'confirmacion' a JSON y se envía la respuesta
            http_response_code(200);
            $JSON_string=ObjAJSON($confirmacion);
            echo "$JSON_string";

        }
        //Si hay un dato de usuario_ci, pero no hay token, es una solicitud
        // de token para registro
        else{
            $token = new Token();
            $usuario_reg = "11110007";
            $token = generarToken($usuario_reg);

            http_response_code(200);
            $JSON_string = TokenAJSON($token);
            echo "$JSON_string";
        }
    }
}
?>