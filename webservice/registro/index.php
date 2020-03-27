<?php
include_once "../conexionbd.php";
include_once "../funcionesVarias.php";
include_once "../clases.php";

if($_POST){
    if(isset($_POST["usuario_ci"])){
        $usuario_ci = validarDatos($_POST["usuario_ci"]);
        
        if(isset($_POST["contrasenia"])){
            $contrasenia = validarDatos($_POST["contrasenia"]);
            $hash = crearHash($contrasenia);
            
            if(isset($_POST["nombre"])){
                $nombre = validarDatos($_POST["nombre"]);
                
                if(isset($_POST["apellido"])){
                    $apellido = validarDatos($_POST["apellido"]);
                    
                    if(isset($_POST["telefono"])){
                        $telefono = validarDatos($_POST["telefono"]);
                        
                        if(isset($_POST["direccion"])){
                            $direccion = validarDatos($_POST["direccion"]);
                            
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
                                
                                registrarUsuario($datosUsuario);
                            }
                            else{
                                echo "Falta EMAIL";
                            }
                        }
                        else{
                            echo "Falta DIRECCION";
                        }
                    }
                    else{
                        echo "Falta TELÉFONO";
                    }
                }
                else{
                    echo "Falta APELLIDO";
                }
            }
            else{
                echo "Falta NOMBRE";
            }
        }
        else{
            echo "Falta CONTRASEÑA";
        }
    }
    else{
        echo "Falta CI";
    }
}
                     




?>