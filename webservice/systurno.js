var receta="";
var medicamentos="";
var cantMedicamentos=0;
//Genera una nueva receta tomando los datos del formulario, enviándolos al servidor
//y recuperando la ID de receta resultante
function nuevaReceta(){
    var usuario_ci=$("#usuario_ci").val();
    var doctor_ci=$("#doctor_ci").val();
    var fecha=$("#fecha").val();
    console.log(usuario_ci+" - "+doctor_ci+" - "+fecha);
    
    var resultado = $.ajax(
        {
            url:"nueva_receta.php",
            method:"POST",
            data:{
                usuario_ci:usuario_ci,
                doctor_ci:doctor_ci,
                fecha:fecha
            },
            dataType: "json",
            success:function(data){
                receta = data;
                console.log(receta.Receta.id);
                
                var tabla = document.getElementById("tabla_receta");
                var cabecera=tabla.createTHead();
                var cuerpo=tabla.createTBody();
                cabecera.innerHTML = "<tr><td colspan=3>Receta:"+receta.Receta.id+"</td></tr>";
                var numFilas = cuerpo.rows.length;
                var fila = cuerpo.insertRow(numFilas);
                var celda1 = fila.insertCell(0);
                celda1.innerHTML="<button id='btn_nuevoMed' onclick='nuevoMedicamento()'>Nuevo Medicamento</button>";
                document.getElementById("btn_nuevaReceta").disabled = true;
                document.getElementById("btn_guardarReceta").disabled = false;
                
            },
            error:function(errorThrown){
                console.log(errorThrown.responseText);
            }
        }
    );
    
}



function nuevoMedicamento(){
    var listado = "";
    
    var nombreSelect = "med"+cantMedicamentos;
    cantMedicamentos++;
    var resultado = $.ajax(
        {
            url:"listar_medicamentos.php",
            method:"POST",
            data:{
            },
            dataType: "json",
            success:function(data){
                medicamentos = data;
                //console.log(medicamentos);
                var datos_tabla = $("#tabla_receta").html();
                
                medicamentos.ListadoMedicamentos.medicamentos.forEach(
                    medicamento => listado = listado + "<option value='"+medicamento.id+"'>"+medicamento.nombre+"</option>");
                
                var tabla = document.getElementById("tabla_receta");
                var filas = tabla.rows.length;
                var fila = tabla.insertRow(filas);
                var celda1 = fila.insertCell(0);
                var celda2 = fila.insertCell(1);
                //var celda3 = fila.insertCell(2);
                
                celda1.innerHTML = "<select class='medicamento' id='med"+cantMedicamentos+"'onchange='itemSeleccionado($(this))'>"+listado+"</select>";
                celda2.innerHTML = "<input class='cantidad' type=number value='1' id='cant"+cantMedicamentos+"'>";
                
            },
            error:function(errorThrown){
                console.log(errorThrown.responseText);
            }
        }
    );
}


function itemSeleccionado(obj){
    console.log(obj+"-"+ obj.val());
}


function agregarMedicamento(receta_id,medicamento_id,cantidad){
    var rec_id = receta_id;
    
    var resultado = $.ajax(
        {
            url:"agregar_medicamento.php",
            method:"POST",
            data:{
                receta_id:receta_id,
                medicamento_id:medicamento_id,
                cantidad:cantidad
            },
            dataType: "json",
            success:function(data){
                var respuesta = data;
                console.log(respuesta.Confirmacion.estado+"-"+respuesta.Confirmacion.mensaje);
            },
            error:function(errorThrown){
                console.log(errorThrown.responseText);
            }
        }
    );    
}

function guardarReceta(){
    var receta_id = receta.Receta.id;
    var medicamentos = document.getElementsByClassName('medicamento');
    var cantidades = document.getElementsByClassName('cantidad');
    
    
    for(i=0; i<medicamentos.length;i++){
        agregarMedicamento(receta_id,medicamentos[i].value,cantidades[i].value);
        console.log(receta_id+" - "+ medicamentos[i].value+" - "+cantidades[i].value);
    }
    
    var tabla = document.getElementById("tabla_receta");
    tabla.removeChild(tabla.getElementsByTagName('tbody')[0])
    var cabecera=tabla.createTHead();
    cabecera.innerHTML = "Receta Guardada";
    document.getElementById("btn_nuevaReceta").disabled = false;
    document.getElementById("btn_guardarReceta").disabled = true;
}












$(document).ready(
    function(){
        
        
        $("#btn_nuevaReceta").click(
            function(){
                nuevaReceta();
            }
        );
        
        $(".medicina").change(
            function(){
                console.log($(this).val());
            }
        );
        
    }
);