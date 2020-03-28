
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
	
	INSERT into Solicita(usuario_ci,token_id) values (usr_ci,tkn_id);
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
CREATE DEFINER='systurno_mobile'@'localhost' 
PROCEDURE buscar_datos_token( 
	IN token_id int unsigned
)
BEGIN
	DECLARE tkn_id int unsigned;
	SET tkn_id = token_id;
	
	SELECT
		solicita.usuario_ci as 'CI_Usuario',
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
	
	INSERT INTO Usuario(ci,hash) values(u_ci,hsh);
	INSERT INTO Perfil(id,nombre,apellido,telefono,direccion,email)
		VALUES(u_ci,u_nom,u_ape,u_tel,u_dir,u_em);
	INSERT INTO Tiene(usuario_ci,perfil_id) VALUES(u_ci,u_ci);
	
END$$
DELIMITER ;

/* Buscar datos de sesión para validarla */
DELIMITER $$
CREATE DEFINER='systurno_mobile'@'localhost' 
PROCEDURE validar_sesion( 
	IN usuario_ci int(8) unsigned,
	IN token_val varchar(60),
	IN seson_val varchar(60)
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
		SELECT count(*) FROM Inicia 
		WHERE sesion_id = ssn_id AND
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


