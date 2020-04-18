<html>
    <head>
        <meta http-equiv='Content-type' content='text/html;charset=UTF-8'>
    </head>
    <body>
        <form id="previo" action="actualizar/" method="post">
            
            <label for="usuario_ci">CI</label><br /> 
            <input id="usuario_ci" name="usuario_ci" type="text" /> <br />
            
            <label for="nombre">Nombre</label><br /> 
            <input id="nombre" name="nombre" type="text" /> <br />
            
            <label for="apellido">Apellido</label><br /> 
            <input id="apellido" name="apellido" type="text" /> <br />
            
            <label for="telefono">Teléfono</label><br /> 
            <input id="telefono" name="telefono" type="tel" /> <br />
            
            <label for="direccion">Dirección</label><br /> 
            <input id="direccion" name="direccion" type="text" /> <br />
            
            <label for="email">E-Mail</label><br /> 
            <input id="email" name="email" type="email" /> <br />
            
            <label for="recibeMail">Recibe Mail</label><br /> 
            <select id="recibeMail" name="recibeMail">
                <option value="si">SI</option>
                <option value="no">NO</option>
            </select>    
                <br />
            <label for="recibeSMS">Recibe SMS</label><br /> 
            <select id="recibeSMS" name="recibeSMS">
                <option value="si">SI</option>
                <option value="no">NO</option>
            </select>    
                <br />
            
            <label for="token_val">Token Val</label><br /> 
            <input id="token_val" name="token_val" type="text" /> <br />
            
            <label for="sesion_val">Sesion Val</label><br /> 
            <input id="sesion_val" name="sesion_val" type="text" /> <br />
           
            <label for="tipo">Tipo</label><br /> 
            <input id="tipo" name="tipo" type="text" /> <br />
            
            <input type="submit" value="Enviar">
        </form>
        
        <script>
            
        </script>
        
    </body>
</html>