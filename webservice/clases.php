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
        public $fecha = "";
        public $especialista = "";
        //public $medicamentosRecetados = array();
    }
    
    /**
    *  Clase para el listado de recetas de un usuario
    */
    class Recetas{
        public $recetas=array();
    }
    
    class ListadoMedRecetados{
        public $medicamentos = array();
    }

    class MedicamentoRecetado{
        public $receta_id = "";
        public $fecha = "";
        public $especialista = "";
        public $med_id="";
        public $med_nombre="";
        public $cantidad="";
        public $disponibilidad="";
        public $entregado="";
    }

    class Medicamento{
        public $id="";
        public $nombre="";
    }

    class ListadoMedicamentos{
        public $medicamentos=array();
    }

    class Turno{
        public $id="0";
        public $fechaHora="";
        public $estado=""; // 'confirmado','cancelado','usado','abierto' para la BDD '---' para evaluación
    }

    class Turnos{
        public $turnos=array();
    }

?>