<?php
    $servidorST="localhost";
    $usuarioST="systurno_mobile";
    $contraseñaST="systurno_mobile";
    $bddST="systurno_mobile";
    

function GenerarConexion(){
    global $servidorST, $usuarioST, $contraseñaST, $bddST;
    
    $conexion = new PDO("mysql:host=$servidorST;dbname=$bddST", $usuarioST, $contraseñaST);
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
    return $conexion;
}

function GenerarConexion_MSYQLI(){
    $servidorBN="localhost";
    $usuarioBN="batallanaval";
    $contraseñaBN="batallanaval";
    $bddBN="batallanaval";

    $conexion = new mysqli($servidorBN, $usuarioBN, $contraseñaBN, $bddBN);
        //mysqli_connect($servidor,$usuario,$contraseña,$bdd);
        if($conexion->connect_error){
            die("ERROR:  " . $conexion->connect_error);
            echo "Error de Conexion: ".mysqli_connect_error();
        }
        else{
            //echo "Conexión Exitosa";
            return $conexion;
        }
    return $conexion;
}

?>