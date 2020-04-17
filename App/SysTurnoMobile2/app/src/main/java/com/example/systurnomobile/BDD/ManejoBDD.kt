package com.example.systurnomobile.BDD

import android.content.Context
import android.widget.TextView
import com.example.systurnomobile.Herramientas.Respuestas.RespDatosUsuario
import com.example.systurnomobile.Herramientas.Respuestas.RespTokenYSesion
import kotlin.concurrent.thread


class ManejoBDD {

    private var bdd: BaseDeDatos? = null
    private var sesionDAO: SesionDAO? = null
    private var usuarioDAO: UsuarioDAO? = null
    private var medicamentoRecetadoDAO: MedicamentoRecetadoDAO?=null
    private var turnoDAO: TurnoDAO?=null
    private var misTurnosDAO: MisTurnosDAO? = null
    private var misRecetasDAO:MisRecetasDAO? = null

    /**
     * Funcion de pruebas para guardar datos recibidos del servidor y leerlos
     * y corroborar así el correcto funcionamiento de la base local
     * NO USAR
     */
    public fun guardarLeer(ctx:Context,respuesta: RespTokenYSesion):String{
        var datos= ""

        val thread:Thread = thread(start = true){
            //Guardado de datos en la BDD
            bdd = BaseDeDatos.obtenerBDD(ctx)
            sesionDAO = bdd?.sesionDao()

            var sesion = Sesion(
                null,
                respuesta.tokenVal(),
                respuesta.tokenId(),
                respuesta.sesionVal(),
                respuesta.sesionId(),
                respuesta.mensaje()
            )

            with(sesionDAO){
                this?.nuevaSesion(sesion)
            }
            //Lectura de datos de la BdD
            var cosas = bdd?.sesionDao()?.ultimaSesion()
            cosas?.forEach {
                datos="tokenVal: "+it.tokenVal
                datos=datos + "\nsesionVal: "+it.sesionVal
                datos=datos + "\nmensaje: "+it.mensaje
                datos = datos + "\nid: "+it.id
            }

        }
        thread.join()
        //println(datos)
        return datos
    }

    /**
     * Crea un usuario en a base local, pero solo almacena su CI
     */
    fun guardarCiUsuario(ctx:Context,ciUsuario:Int){
        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            usuarioDAO = bdd?.usuarioDao()

            var usuario = Usuario(
                ciUsuario
            )
            with(usuarioDAO){
                this?.nuevoUsuario(usuario)
            }
        }
        thread.join()
    }

    /**
     * Guarda los datos de un usuario en la base local.
     * Elimina todos los datos anteriores para evitar tener múltiples versiones de los mismos datos.
     * Todos los datos obtenidos del servidor
     */
    fun guardarDatosUsuario(ctx: Context,datosUsuario: RespDatosUsuario){
        //borrarUsuarios(ctx)
        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            usuarioDAO = bdd?.usuarioDao()

            var usuario = Usuario(
                ci = datosUsuario.usuario_ci().toInt(),
                nombre = datosUsuario.nombre(),
                apellido = datosUsuario.apellido(),
                direccion = datosUsuario.direccion(),
                email = datosUsuario.email(),
                telefono = datosUsuario.telefono(),
                recibe_email = datosUsuario.recibeEmail(),
                recibe_sms = datosUsuario.recibeSMS()
            )

            with(usuarioDAO){
                this?.nuevoUsuario(usuario)
            }
        }
        thread.join()
    }

    fun actualizarDatosUsuario(ctx:Context,usuario:Usuario){
        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            usuarioDAO = bdd?.usuarioDao()

            with(usuarioDAO){
                this?.actualizarDatosUsuario(usuario)
            }
        }
        thread.join()
    }

    /**
     * Guarda los datos de un sesión en la base local
     */
    fun guardarSesion(ctx:Context,respuesta: RespTokenYSesion){
        val thread:Thread = thread(start = true){
            //Guardado de datos en la BDD
            bdd = BaseDeDatos.obtenerBDD(ctx)
            sesionDAO = bdd?.sesionDao()

            var sesion = Sesion(
                null,
                respuesta.tokenVal(),
                respuesta.tokenId(),
                respuesta.sesionVal(),
                respuesta.sesionId(),
                respuesta.mensaje()
            )

            with(sesionDAO){
                this?.nuevaSesion(sesion)
            }
        }
        thread.join()
    }

    /**
     * Lee la última sesión almacenada en la base de datos local
     */
    fun leerSesion(ctx: Context): Sesion? {
        var sesion:Sesion? = null

        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            sesionDAO = bdd?.sesionDao()

            var sesiones = bdd?.sesionDao()?.ultimaSesion()

            sesiones?.forEach {
                var ses:Sesion = Sesion(
                    id = it.id,
                    tokenId = it.tokenId,
                    tokenVal = it.tokenVal,
                    sesionId = it.sesionId,
                    sesionVal = it.sesionVal,
                    mensaje = it.mensaje
                )
                println("G:token ID:"+it.tokenId)
                println("G:token Val:"+it.tokenVal)
                println("G:sesion ID:"+it.sesionId)
                println("G:sesion Val:"+it.sesionVal)
                sesion = ses
            }
        }
        //La función join del thread es importante ya que fuerza al sistema a esperar
        // al proceso del thread a que termine
        thread.join()

        println("SG:>"+sesion?.sesionVal)
        return sesion
    }

    /**
     * Obtiene los datos del Usuario de la base de datos local,
     * por ahora solamente el dato de su CI
     */
    fun leerCiUsuario(ctx: Context): Usuario? {
        var usuario:Usuario? = null

        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            usuarioDAO = bdd?.usuarioDao()

            var usuarios = bdd?.usuarioDao()?.verDatosUsuario()

            usuarios?.forEach {
                var usr: Usuario = Usuario(
                    ci = it.ci
                )
                println("ci Usuario:"+it.ci)
                usuario = usr
            }
        }
        //La función join del thread es importante ya que fuerza al sistema a esperar
        // al proceso del thread a que termine
        thread.join()
        return usuario
    }

    /**
     * Obtiene los datos del Usuario de la base de datos local,
     * Todos los datos
     */
    fun leerDatosUsuario(ctx: Context): Usuario? {
        var usuario:Usuario? = null

        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            usuarioDAO = bdd?.usuarioDao()

            var usuarios = bdd?.usuarioDao()?.verDatosUsuario()

            usuarios?.forEach {
                var usr = Usuario(
                    ci = it.ci,
                    nombre = it.nombre,
                    apellido = it.apellido,
                    direccion = it.direccion,
                    telefono = it.telefono,
                    email = it.email,
                    recibe_sms = it.recibe_sms,
                    recibe_email = it.recibe_email
                )
                println("recibe :"+it.nombre)
                usuario = usr
            }
        }
        //La función join del thread es importante ya que fuerza al sistema a esperar
        // al proceso del thread a que termine
        thread.join()
        return usuario
    }

    /**
     * Borra todos los datos de Usuario de la base local
     */
    fun borrarUsuarios(ctx: Context){
        val thread:Thread = thread(start = true) {
            bdd = BaseDeDatos.obtenerBDD(ctx)
            usuarioDAO = bdd?.usuarioDao()

            usuarioDAO?.borrarTodosLosUsuarios()
        }
        thread.join()
    }

    /**
     * Borra todos los datos de Sesion de la base local
     */
    fun borrarSesiones(ctx: Context){
        val thread:Thread = thread(start = true) {
            bdd = BaseDeDatos.obtenerBDD(ctx)
            sesionDAO = bdd?.sesionDao()

            sesionDAO?.borrarTodasLasSesiones()
        }
        thread.join()
    }

    fun guardarMedicamentosRecetados(ctx:Context,medicamentoRecetado: MedicamentoRecetado){
        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            medicamentoRecetadoDAO = bdd?.medicamentoRecetadoDao()
            /*
            var medicamento = MedicamentoRecetado(
                receta_id = medicamentoRecetado.receta_id,
                fecha = medicamentoRecetado.fecha,
                med_id = medicamentoRecetado.med_id,
                med_nombre = medicamentoRecetado.med_nombre,
                cantidad = medicamentoRecetado.cantidad,
                especialista = medicamentoRecetado.especialista,
                entregado = medicamentoRecetado.entregado,
                disponibilidad = medicamentoRecetado.disponibilidad
            )*/
            medicamentoRecetadoDAO?.nuevoMedicamentoRecetado(medicamentoRecetado)

        }
        thread.join()
    }

    /**
     * Obtiene el listado de medicamentos recetados
     */
    fun leerMedicamentosRecetados(ctx:Context):List<MedicamentoRecetado>?{
        var medicamentosRecetados:MutableList<MedicamentoRecetado>?=ArrayList<MedicamentoRecetado>()
        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            medicamentoRecetadoDAO = bdd?.medicamentoRecetadoDao()

            var medRec=medicamentoRecetadoDAO?.todosLosMedicamentosRecetados()
            medRec?.forEach {
                println("Leido de la BDD local: "+it.med_nombre)
                medicamentosRecetados?.add(it)
            }
        }
        thread.join()
        return medicamentosRecetados
    }

    /**
     * Borra todos los datos de Medicamentos Recetados de la base local
     */
    fun borrarMedicamentos(ctx: Context){
        val thread:Thread = thread(start = true) {
            bdd = BaseDeDatos.obtenerBDD(ctx)
            medicamentoRecetadoDAO = bdd?.medicamentoRecetadoDao()

            medicamentoRecetadoDAO?.borrarTodosLosMedicamentos()
        }
        thread.join()
    }

    /**
     * Ingresa un nuevo turno en la Base de Datos local
     */
    fun nuevoTurno(ctx:Context, turno:Turno){
        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            turnoDAO = bdd?.turnoDao()
            turnoDAO?.nuevoTurno(turno)
        }
        thread.join()
    }

    /**
     * Lee el primer turno de la lista de la BdD local
     */
    fun leerPrimerTurno(ctx:Context):Turno?{
        var turno:Turno? = null

        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            turnoDAO = bdd?.turnoDao()

            var turnos = turnoDAO?.primerTurno()
            turnos?.forEach {
                var t = Turno(
                    id = it.id,
                    fechahora = it.fechahora,
                    estado = it.estado
                )
                turno = t
            }
        }
        thread.join()
        return turno
    }


    /**
     * Borra los turnos almacenados en la BdD local
     */
    fun borrarTurnos(ctx:Context){
        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            turnoDAO = bdd?.turnoDao()
            turnoDAO?.borrarTurnos()
        }
        thread.join()
    }

    /**
     * Busca el turno siguiente al turno entregado
     */
    fun leerTurnoSig(ctx:Context, turno: Turno):Turno?{
        var turno_id = turno.id
        var turno_sig:Turno? = null

        val thread:Thread = thread(start = true) {
            bdd = BaseDeDatos.obtenerBDD(ctx)
            turnoDAO = bdd?.turnoDao()
            var turnos = turnoDAO?.buscarTurnoSig(turno_id)

            if (turnos.isNullOrEmpty()){
                turno_sig = turno
            }
            else{
                turnos?.forEach {
                    var t = Turno(
                        id = it.id,
                        fechahora = it.fechahora,
                        estado = it.estado
                    )
                    turno_sig = t
                }
            }
        }
        thread.join()
        return turno_sig
    }


    /**
     * Busca el turno anterior al turno entregado
     */
    fun leerTurnoAnt(ctx:Context, turno: Turno):Turno?{
        var turno_id = turno.id
        var turno_ant:Turno? = null

        val thread:Thread = thread(start = true) {
            bdd = BaseDeDatos.obtenerBDD(ctx)
            turnoDAO = bdd?.turnoDao()
            var turnos = turnoDAO?.buscarTurnoAnt(turno_id)

            //Si la lista es vacía, eso significa que no hay turnos anteriores y se devuelve el
            // turno actual
            if (turnos.isNullOrEmpty()){
                turno_ant = turno
            }
            else{
                turnos?.forEach {
                    var t = Turno(
                        id = it.id,
                        fechahora = it.fechahora,
                        estado = it.estado
                    )
                    turno_ant = t
                }
            }
        }
        thread.join()
        return turno_ant
    }

    fun convertirTurno(turno:Turno):MisTurnos{
        var mt = MisTurnos(
            id = turno.id,
            fechahora = turno.fechahora,
            estado = turno.estado
        )
        return mt
    }

    /**
     * Agrega los datos de un turno solicitado a la base local
     */
    fun nuevoTurnoSolicitado(ctx:Context, turno: MisTurnos){
        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            misTurnosDAO = bdd?.misTurnosDao()

            misTurnosDAO?.nuevoTurno(turno)
        }
        thread.join()
    }

    /**
     * Borra los datos de todos los turnos solicitados7
     */

    fun borrarMisTurnos(ctx:Context){
        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            misTurnosDAO = bdd?.misTurnosDao()
            misTurnosDAO?.borrarMisTurnos()
        }
        thread.join()
    }

    /**
     * Lee los datos de turnos solicitados de la base de datos local
     */
    fun leerMisTurnos(ctx:Context):MutableList<MisTurnos>{
        var misTurnos:MutableList<MisTurnos> = ArrayList<MisTurnos>()
        var thread = thread (start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            misTurnosDAO = bdd?.misTurnosDao()

            var mt = misTurnosDAO?.todosMisTurnos()
            mt?.forEach {
                misTurnos?.add(it)
            }
        }
        thread.join()
        return misTurnos
    }

    /**
     * Ingresar una nueva receta a la bdd local
     */
    fun nuevaMiReceta(ctx: Context,receta:MisRecetas){
        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            misRecetasDAO = bdd?.misRecetasDao()

            misRecetasDAO?.nuevaReceta(receta)
        }
        thread.join()
    }

    /**
     * Borrar todas la recetas almacenadas en la bdd local
     */
    fun borrarMisRecetas(ctx: Context){
        val thread:Thread = thread(start = true){
            bdd = BaseDeDatos.obtenerBDD(ctx)
            misRecetasDAO = bdd?.misRecetasDao()

            misRecetasDAO?.borrarTodasMisRecetas()
        }
        thread.join()
    }

    /**
     * Buscar las recetas de un turno específico
     */
    fun buscarRecetasDeTurno(turno_id: Int, ctx: Context):List<MisRecetas>{
        var lista:MutableList<MisRecetas> = ArrayList()
        val thread:Thread = thread(start = true) {
            bdd = BaseDeDatos.obtenerBDD(ctx)
            misRecetasDAO = bdd?.misRecetasDao()
            var lr = misRecetasDAO?.buscarRecetasDelTurno(turno_id)
            lr?.forEach {
               lista.add(it)
            }
        }
        thread.join()
        return lista
    }
}