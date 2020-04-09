<?php
    include_once "../conexionbd.php";
    include_once "../funcionesVarias.php";
    include_once "../clases.php";
?>
<html>
    <head>
        <meta http-equiv='Content-type' content='text/html;charset=UTF-8'>
        <script type='text/javascript' src='../jquery.js'></script>
        <script type="text/javascript" src="../jquery-ui/jquery-ui.js"></script>
        <script type='text/javascript' src='../systurno.js'></script>
        <link rel="stylesheet" href="index.css">
    </head>
    <body>
            <label for="fecha">Fecha:</label>
            <input id="fecha" name="fecha" type="date" /><br />
            
            <label for="usuario_ci">Usuario</label><br /> 
            <select id="usuario_ci" name="usuario_ci">
                <?php
                    generarListaUsuariosParaSelect();
                    
                ?>                
            </select><br/>

            <input id="doctor_ci" name="doctor_ci" value="22446688" type="hidden" />
            
            <table id="tabla_receta">
                <tr>
                    <th>
                        
                    </th>
                    <th>
                    </th>
                    <th>
                    </th>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
            <button id="btn_nuevaReceta">Nueva Receta</button>
            <button id="btn_guardarReceta" onclick="guardarReceta()" disabled="true">Guardar Receta</button>
        
    </body>
</html>