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
                $("#tabla_receta").html(
                    "<thead><tr><th colspan='2'>Receta:"+receta.Receta.id+"</th></tr></thead>"
                );
                $("#tabla_receta").html( $("#tabla_receta").html()+"<tr><td colspan='3'><button id='btn_nuevoMed' onclick='nuevoMedicamento()'>Nuevo Medicamento</button></td></tr>"
                );
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
                
                medicamentos.ListaMedicamento.medicamentos.forEach(
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