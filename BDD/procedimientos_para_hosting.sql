
/*Registro de token y devolución de ID del mismo*/
DELIMITER $$
CREATE
PROCEDURE registrar_token( 
	IN usuario_ci int(8) unsigned,
	IN token_val varchar(60)
)
BEGIN
	DECLARE usr_ci int(8) unsigned;
	DECLARE tkn_val varchar(60);
	DECLARE tkn_id int unsigned;
	
	SET usr_ci = usuario_ci;
	SET tkn_val = token_val;
	
	INSERT into Token(valor) VALUES (tkn_val);
	SET tkn_id = LAST_INSERT_ID();
	
	INSERT into Solicita(usuario_ci,token_id) values (usr_ci,tkn_id);
	SELECT tkn_id;
END$$
DELIMITER ;


/*Registro de Sesion y devolución de ID*/
DELIMITER $$
CREATE 
PROCEDURE registrar_sesion( 
	IN usuario_ci int(8) unsigned,
	IN token_id int unsigned,
	IN sesion_val varchar(60)
)
BEGIN
	DECLARE usr_ci int(8) unsigned;
	DECLARE tkn_id int unsigned;
	DECLARE ssn_val varchar(60);
	DECLARE ssn_id int unsigned;
	
	/* Manejo automático de errores de clave duplicada para evitar sesiones 'huérfanas' */

	DECLARE EXIT HANDLER FOR 1062
	BEGIN
		ROLLBACK;
	END;
	
	SET usr_ci = usuario_ci;
	SET tkn_id = token_id;
	SET ssn_val = sesion_val;
	
	INSERT into Sesion(valor) values (ssn_val);
	set ssn_id = LAST_INSERT_ID();
	
	INSERT into Inicia(usuario_ci,sesion_id) values (usr_ci,ssn_id);
	INSERT into Requiere(sesion_id,token_id) values (ssn_id,tkn_id);
	
	/* Cerrar toda otra sesión abierta por el usuario */
	UPDATE Sesion inner join Inicia
		set estado = 'cerrada'
		WHERE 
		Sesion.id = Inicia.sesion_id AND
		Sesion.estado = 'abierta' AND 
		Inicia.usuario_ci = usr_ci AND
		Sesion.id != ssn_id;
	
	select ssn_id;
	
END$$
DELIMITER ;


/* Buscar datos del Token en la BdD */
DELIMITER $$
CREATE 
PROCEDURE buscar_datos_token( 
	IN token_id int unsigned
)
BEGIN
	DECLARE tkn_id int unsigned;
	SET tkn_id = token_id;
	
	SELECT
		Solicita.usuario_ci as 'CI_Usuario',
		(SELECT Token.valor 
		FROM Token 
		WHERE Token.id = Solicita.token_id) as 'token_ID',
		(SELECT Token.id 
		FROM Token 
		WHERE Token.id = Solicita.token_id) as 'num_identificador',
		(SELECT Token.marcaTiempo 
		FROM Token 
		WHERE Token.id = Solicita.token_id) as 'marcaTiempo'
		/*(SELECT systurno_mobile.token.estado 
		FROM systurno_mobile.token 
		WHERE systurno_mobile.token.id = systurno_mobile.solicita.token_id) as 'estado'*/
	FROM Solicita
	WHERE Solicita.token_id = tkn_id;
	
END$$
DELIMITER ;

/* Registrar un usuario a la BdD */
DELIMITER $$
CREATE 
PROCEDURE registrar_usuario( 
	IN usuario_ci int(8) unsigned,
	IN hash varchar(80),
	IN nombre varchar(30),
	IN apellido varchar(30),
	IN telefono varchar(12),
	IN direccion varchar(40),
	IN email varchar(45)
)
BEGIN
	DECLARE u_ci int(8) unsigned;
	DECLARE hsh varchar(80);
	DECLARE u_nom varchar(30);
	DECLARE u_ape varchar(30);
	DECLARE u_tel varchar(12);
	DECLARE u_dir varchar(40);
	DECLARE u_em varchar(45);
	
	SET u_ci = usuario_ci;
	SET hsh = hash;
	SET u_nom = nombre;
	SET u_ape = apellido;
	SET u_dir = direccion;
	SET u_tel = telefono;
	SET u_em = email;
	
	INSERT INTO Usuario(ci,hash) values(u_ci,hsh);
	INSERT INTO Perfil(id,nombre,apellido,telefono,direccion,email)
		VALUES(u_ci,u_nom,u_ape,u_tel,u_dir,u_em);
	INSERT INTO Tiene(usuario_ci,perfil_id) VALUES(u_ci,u_ci);
	
END$$
DELIMITER ;

/* Buscar datos de sesión para validarla */
DELIMITER $$
CREATE 
PROCEDURE validar_sesion( 
	IN usuario_ci int(8) unsigned,
	IN token_val varchar(60),
	IN sesion_val varchar(60)
)
BEGIN
	DECLARE usr_ci int(8) unsigned;
	DECLARE tkn_id int unsigned;
	DECLARE tkn_val varchar(60);
	DECLARE ssn_id int unsigned;
	DECLARE ssn_val varchar(60);
	DECLARE sesiones int;
	DECLARE tokens int;
	DECLARE vinculos int;
	
	SET usr_ci = usuario_ci;
	SET tkn_val = token_val;
	SET ssn_val = sesion_val;
	
	SET ssn_id = (SELECT id FROM Sesion WHERE valor = ssn_val);
	SET tkn_id = (SELECT id FROM Token WHERE valor = tkn_val);
	
	SET sesiones=(
		SELECT count(*) FROM Inicia INNER JOIN Sesion
		WHERE Sesion.id = Inicia.sesion_id AND
		sesion_id = ssn_id AND
		Sesion.estado = 'abierta' AND
		usuario_ci = usr_ci);
	
	SET tokens=(
		SELECT count(*) FROM Solicita 
		WHERE token_id = tkn_id AND
		usuario_ci = usr_ci);
	
	SET vinculos=(
		SELECT count(*) FROM Requiere 
		WHERE token_id = tkn_id AND
		sesion_id = ssn_id);
	
	SELECT sesiones,tokens,vinculos;
END$$
DELIMITER ;

/* Cierra todas las sesiones abiertas de un usuario */
DELIMITER $$
CREATE 
PROCEDURE cerrar_sesiones( 
	IN usuario_ci int(8) unsigned
)
BEGIN
	DECLARE usr_ci int(8) unsigned;
	
	SET usr_ci = usuario_ci;

	UPDATE Sesion inner join Inicia
		set estado = 'cerrada'
		WHERE 
		Sesion.id = Inicia.sesion_id AND
		Sesion.estado = 'abierta' AND 
		Inicia.usuario_ci = usr_ci;

END$$
DELIMITER ;



/* Recupera los datos del perfil del usuario */
DELIMITER $$
CREATE 
PROCEDURE buscar_datos_usuario( 
	IN usuario_ci int(8) unsigned
)
BEGIN
	DECLARE usr_ci int(8) unsigned;
	
	SET usr_ci = usuario_ci;
	
	SELECT nombre,apellido,telefono,direccion,email,recibeSMS,recibeEmail
	FROM Perfil where id = usr_ci;
END$$
DELIMITER ;


/* Crea una nueva receta, la relaciona con un doctor y un usuario y devuelve la ID */

DELIMITER $$
CREATE 
PROCEDURE nueva_receta( 
	IN doctor_ci int(8) unsigned,
	IN usuario_ci int(8) unsigned,
	IN receta_fecha date
)
BEGIN
	DECLARE usr_ci int(8) unsigned;
	DECLARE doc_ci int(8) unsigned;
	DECLARE rct_fch date;
	DECLARE rct_id int(11) unsigned;
	
	SET usr_ci = usuario_ci;
	SET doc_ci = doctor_ci;
	SET rct_fch = receta_fecha;
	
	INSERT INTO Receta(fecha) VALUES(rct_fch);
	SET rct_id = LAST_INSERT_ID();
	INSERT INTO Entrega(doctor_ci,receta_id) VALUES(doc_ci,rct_id);
	INSERT INTO Recibe(usuario_ci,receta_id) VALUES(usr_ci,rct_id);
	SELECT rct_id;
	
END$$
DELIMITER ;



/* Añade un medicamento a una receta */

DELIMITER $$
CREATE 
PROCEDURE agregar_medicamento( 
	IN receta_id int(11) unsigned,
	IN medicamento_id int(11) unsigned,
	IN cantidad int(3) unsigned
)
BEGIN
	DECLARE rct_id int(11) unsigned;
	DECLARE med_id int(11) unsigned;
	DECLARE cant int(3) unsigned;
	
	SET rct_id = receta_id;
	SET med_id = medicamento_id;
	SET cant = cantidad;
	
	INSERT INTO Contiene(receta_id,medicamento_id,cantidad) VALUES (rct_id,med_id,cant);
	
END$$
DELIMITER ;

/* Genera un listado de todos los medicamentos recetados y no entregados a un usuario */
DELIMITER $$
CREATE 
PROCEDURE medicamentos_recetados_no_ent( 
	IN usuario_ci int(8) unsigned
	)
BEGIN
	DECLARE usr_ci int(8) unsigned;
	
	SET usr_ci = usuario_ci;
	
	SELECT 
		Receta.id as receta_id,
		Receta.fecha as fecha,
		Contiene.medicamento_id as med_id, 
		Contiene.entregado as "estado",
		Contiene.cantidad as "cantidad",
		(SELECT Medicamento.nombre FROM Medicamento 
			WHERE Medicamento.id = Contiene.medicamento_id) 
			as "med_nombre",
		(SELECT CONCAT_WS("-",CONCAT_WS(" ",Doctor.nombre,Doctor.apellido),Doctor.especialidad)
		From Doctor
			WHERE Doctor.ci = 
			(SELECT Entrega.doctor_ci FROM Entrega
			WHERE Entrega.receta_id = Receta.id))
			as "especialista",
		(SELECT Medicamento.stock FROM Medicamento 
			WHERE Medicamento.id = Contiene.medicamento_id)
			as "stock"
	FROM Contiene INNER JOIN Receta INNER JOIN Recibe
	WHERE Receta.id=Contiene.receta_id AND
	Receta.id = Recibe.receta_id AND
	Contiene.entregado = "no" AND
	Recibe.usuario_ci = usr_ci
	ORDER BY receta_id;
END$$
DELIMITER ;


/* Genera un listado de todas las recetas de un usuario con medicamentos no entregados */
DELIMITER $$
CREATE 
PROCEDURE recetas_de_usuario( 
	IN usuario_ci int(8) unsigned
	)
BEGIN
	DECLARE usr_ci int(8) unsigned;
	
	SET usr_ci = usuario_ci;
	
	SELECT 
		Receta.id as receta_id,
		Receta.fecha as fecha,
		(SELECT CONCAT_WS("-",CONCAT_WS(" ",Doctor.nombre,Doctor.apellido),Doctor.especialidad)
		From Doctor
		WHERE Doctor.ci = 
			(SELECT Entrega.doctor_ci FROM Entrega
			WHERE Entrega.receta_id = Receta.id))
			as "especialista"
	FROM Contiene INNER JOIN Receta INNER JOIN Recibe
	WHERE Receta.id=Contiene.receta_id AND
	Receta.id = Recibe.receta_id AND
	Contiene.entregado = "no" AND
	Recibe.usuario_ci = usr_ci
	GROUP BY receta_id
	ORDER BY receta_id;
END$$
DELIMITER ;


/* Asigna un turno a un usuario y lo marca como confirmado */
DELIMITER $$
CREATE 
PROCEDURE asignar_turno( 
	IN usuario_ci int(8) unsigned,
	IN turno_id int(11) unsigned
	)
BEGIN
	DECLARE usr_ci int(8) unsigned;
	DECLARE trn_id int(11) unsigned;
	
	SET usr_ci = usuario_ci;
	SET trn_id = turno_id;
	
	INSERT INTO Genera(usuario_ci,turno_id) VALUES (usr_ci,trn_id);
	
	UPDATE Turno SET Turno.estado='confirmado' WHERE Turno.id=trn_id;
	
END$$
DELIMITER ;
