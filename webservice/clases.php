<?php

    //token_Val --> valor alfanumérico del token
    //token_ID  --> identificador del token en la BdD
    class Token{
        public $tipo="";//Tipos de Token: ERROR,TOKEN,SESION,REGISTRO
        public $token_ID = "";
        public $token_Val="";
        public $sesion_ID="";
        public $sesion_Val = "";
        public $mensaje="";
    }

    class DatosToken{
        public $usuario_CI="";
        public $token_Val="";
        public $num_ID="";
        public $marcaTiempo="";
        public $estado="";
    }


    class DatosUsuario{
        public $usuario_CI = "";
        public $nombre = "";
        public $apellido = "";
        public $telefono = "";
        public $email = "";
        public $direccion = "";
        public $recibeSMS = "";
        public $recibeEmail = "";
    }
    
    class DatosLogin{
        public $usuario_CI = "";
        public $hash = "";
    }

     class SesionValida{
         public $valida = "";//Valores:  "true" / "false"
     }

    class Confirmacion{
        public $estado = "";//Estados: ERROR y OK
        public $mensaje = "";
    }

    class Receta{
        public $id = "";
    }

    class Medicamento{
        public $id="";
        public $nombre="";
    }

    class ListaMedicamento{
        public $medicamentos=array();
    }
?>