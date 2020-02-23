
/*Registro de token*/
DELIMITER $$
CREATE DEFINER='systurno'@'localhost' 
PROCEDURE registrar_token( 
	IN ci_usuario int unsigned,
	IN token varchar(60)
)
BEGIN
	DECLARE ciU int unsigned;
	DECLARE val_tkn varchar(60);
	DECLARE id_tkn int unsigned;
	
	SET ciU = ci_usuario;
	SET val_tkn = token;
	
	INSERT into token(valor) VALUES (val_tkn);
	SET id_tkn = LAST_INSERT_ID();
	
	INSERT into systurno.solicita(usuario_CI,token_ID) values (ciU,id_tkn);
END$$
DELIMITER ;