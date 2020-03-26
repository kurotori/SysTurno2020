
/*Registro de token y devolución de ID del mismo*/
DELIMITER $$
CREATE DEFINER='systurno_mobile'@'localhost' 
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
	
	/* Invalidar todo otro token del usuario 
	UPDATE systurno_mobile.Token inner join systurno_mobile.Solicita
		set estado = 'inactivo'
		WHERE 
		systurno_mobile.Token.id = systurno_mobile.Solicita.token_id AND
		systurno_mobile.Token.estado = 'activo' AND 
		systurno_mobile.Solicita.usuario_ci = usr_ci AND
		systurno_mobile.Token.id != tkn_id;*/
	
	INSERT into systurno_mobile.Solicita(usuario_ci,token_id) values (usr_ci,tkn_id);
	SELECT tkn_id;
END$$
DELIMITER ;


/*Registro de Sesion y devolución de ID*/
DELIMITER $$
CREATE DEFINER='systurno_mobile'@'localhost' 
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
	
	INSERT into systurno_mobile.Sesion(valor) values (ssn_val);
	set ssn_id = LAST_INSERT_ID();
	
	INSERT into systurno_mobile.Inicia(usuario_ci,sesion_id) values (usr_ci,ssn_id);
	INSERT into systurno_mobile.Requiere(sesion_id,token_id) values (ssn_id,tkn_id);
	
	/* Cerrar toda otra sesión abierta por el usuario */
	UPDATE systurno_mobile.Sesion inner join systurno_mobile.Inicia
		set estado = 'cerrada'
		WHERE 
		systurno_mobile.Sesion.id = systurno_mobile.Inicia.sesion_id AND
		systurno_mobile.Sesion.estado = 'abierta' AND 
		systurno_mobile.Inicia.usuario_ci = usr_ci AND
		systurno_mobile.Sesion.id != ssn_id;
	
	select ssn_id;
	
END$$
DELIMITER ;


/* Buscar datos del Token en la BdD */
DELIMITER $$
CREATE DEFINER='systurno_mobile'@'localhost' 
PROCEDURE buscar_datos_token( 
	IN token_id int unsigned
)
BEGIN
	DECLARE tkn_id int unsigned;
	SET tkn_id = token_id;
	
	SELECT
		systurno_mobile.solicita.usuario_ci as 'CI_Usuario',
		(SELECT systurno_mobile.token.valor 
		FROM systurno_mobile.token 
		WHERE systurno_mobile.token.id = systurno_mobile.solicita.token_id) as 'token_ID',
		(SELECT systurno_mobile.token.id 
		FROM systurno_mobile.token 
		WHERE systurno_mobile.token.id = systurno_mobile.solicita.token_id) as 'num_identificador',
		(SELECT systurno_mobile.token.marcaTiempo 
		FROM systurno_mobile.token 
		WHERE systurno_mobile.token.id = systurno_mobile.solicita.token_id) as 'marcaTiempo'
		/*(SELECT systurno_mobile.token.estado 
		FROM systurno_mobile.token 
		WHERE systurno_mobile.token.id = systurno_mobile.solicita.token_id) as 'estado'*/
	FROM systurno_mobile.solicita
	WHERE systurno_mobile.solicita.token_id = tkn_id;
	
END$$
DELIMITER ;

/* Registrar un usuario a la BdD */
DELIMITER $$
CREATE DEFINER='systurno_mobile'@'localhost' 
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
	
	INSERT INTO systurno_mobile.usuario(ci,hash) values(u_ci,hsh);
	INSERT INTO systurno_mobile.perfil(id,nombre,apellido,telefono,direccion,email)
		VALUES(u_ci,u_nom,u_ape,u_tel,u_dir,u_em);
	INSERT INTO systurno_mobile.Tiene(usuario_ci,perfil_id) VALUES(u_ci,u_ci);
	
END$$
DELIMITER ;

