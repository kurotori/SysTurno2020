DROP SCHEMA IF EXISTS systurno_mobile;
create schema systurno_mobile;
use systurno_mobile;

drop user if exists "systurno_mobile"@"localhost";
create user "systurno_mobile"@"localhost" identified by "systurno_mobile";

grant 
insert,select,update,delete,
create routine, alter routine, execute on systurno_mobile.* 
to "systurno_mobile"@"localhost";


/*  Csp9db]q2-F;Qfgc  */
/* ----------------------*/

CREATE TABLE Doctor ( 
  ci int(8) unsigned NOT NULL unique primary key, 
  nombre varchar(30) NOT NULL, 
  apellido varchar(30) NOT NULL, 
  telefono varchar(12) NOT NULL, 
  email varchar(45) NOT NULL, 
  sal varchar(65) NOT NULL, 
  hash varchar(32) NOT NULL, 
  especialidad varchar(40) NOT NULL 
);

CREATE TABLE Medicamento ( 
  id int(11) unsigned NOT NULL unique auto_increment primary key, 
  nombre varchar(30) NOT NULL, 
  laboratorio varchar(30) NOT NULL, 
  principio varchar(50) NOT NULL, 
  presentacion varchar(30) NOT NULL, 
  controlado tinyint(1) NOT NULL, 
  stock int(4) NOT NULL 
); 

CREATE TABLE Receta ( 
  id int(11) unsigned NOT NULL unique auto_increment primary key, 
  fecha date NOT NULL DEFAULT CURRENT_TIMESTAMP
); 

CREATE TABLE Usuario ( 
  ci int(8) unsigned NOT NULL unique primary key, 
  hash varchar(80) NOT NULL
);

/*NOTA: Se separan los datos del usuario de la tabla usuario
	para mejorar la eficiencia de la misma durante el login.
*/
CREATE TABLE Perfil(
	id int(8) unsigned NOT NULL unique primary key,
	nombre varchar(30) NOT NULL, 
	apellido varchar(30) NOT NULL, 
	telefono varchar(12) NOT NULL, 
	direccion varchar(60) NOT NULL,
	email varchar(45) NOT NULL,
	recibeSMS enum('si','no') NOT NULL default 'si',
	recibeEmail enum('si','no') NOT NULL default 'si'
);

CREATE TABLE Turno ( 
  id int(11) unsigned NOT NULL unique auto_increment primary key, 
  fechahora datetime NOT NULL, 
  estado enum('confirmado','cancelado','usado','abierto') NOT NULL DEFAULT 'abierto' 
);

create table Token(
 id int(11) unsigned unique not null auto_increment primary key,
 valor varchar(60) not null,
 marcaTiempo timestamp not null default current_timestamp/*,
 estado enum('activo','inactivo') default 'activo'*/
 );

CREATE TABLE Sesion( 
  id int(11) unsigned NOT NULL unique auto_increment primary key,
  valor varchar(60) not null,
  fechahora timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, 
  estado enum('abierta','cerrada') NOT NULL default 'abierta'
);

/*----------------*/

/* 1 usuario tiene 1 perfil */
CREATE TABLE Tiene(
	usuario_ci int(8) unsigned unique not null primary key,
	perfil_id int(8) unsigned unique not null
);

/* n turno asociado con n receta */
CREATE TABLE Asociado (
  id int(11) unsigned NOT NULL unique auto_increment primary key,
  turno_id int(11) unsigned NOT NULL, 
  receta_id int(11) unsigned NOT NULL
);

/* n receta contiene n medicamento */
CREATE TABLE Contiene (
  id int(11) unsigned NOT NULL unique auto_increment primary key,
  receta_id int(11) unsigned NOT NULL,
  medicamento_id int(11) unsigned NOT NULL, 
  cantidad int(3) unsigned DEFAULT NULL, 
  entregado enum('si','no') default 'no'
);

/* 1 doctor entrega n receta */
CREATE TABLE Entrega ( 
  doctor_ci int(8) unsigned NOT NULL, 
  receta_id int(11) unsigned NOT NULL unique primary key
); 

/* 1 usuario genera n turno */
CREATE TABLE Genera ( 
  usuario_ci int(8) unsigned NOT NULL, 
  turno_id int(11) unsigned NOT NULL unique primary key
) ;

/* 1 usuario inicia n sesion */
CREATE TABLE Inicia ( 
  usuario_ci int(8) unsigned NOT NULL, 
  sesion_id int(11) unsigned NOT NULL unique primary key
);

/* 1 usuario recibe n receta */
CREATE TABLE Recibe ( 
  usuario_ci int(8) unsigned NOT NULL, 
  receta_id int(11) unsigned NOT NULL unique primary key
); 

/* 1 sesion requiere 1 token */
CREATE TABLE Requiere(
	token_id int(11) unsigned NOT NULL unique,
	sesion_id int(11) unsigned NOT NULL UNIQUE primary key
);

/* 1 usuario solicita n token */
create table Solicita(
   usuario_ci int(8) unsigned not null,
   token_id int(11) unsigned unique not null primary key
);

/* ---------------------------- */

alter table Solicita
add constraint fk_usuario_solicita
foreign key (usuario_ci)
references Usuario(ci)
on update cascade
on delete cascade;

alter table Solicita
add constraint fk_solicita_token
foreign key (token_id)
references Token(id)
on update cascade
on delete cascade;

ALTER TABLE Requiere
add constraint fk_sesion_requiere
foreign key (sesion_id)
references Sesion(id)
on update cascade
on delete cascade;

ALTER TABLE Requiere
add constraint fk_requiere_token
foreign key (token_id)
references Token(id)
on update cascade
on delete cascade;

ALTER TABLE Entrega
add constraint fk_doctor_entrega
foreign key (doctor_ci)
references Doctor(ci)
on update cascade
on delete cascade;

ALTER TABLE Entrega
add constraint fk_entrega_receta
foreign key (receta_id)
references Receta(id)
on update cascade
on delete cascade;

ALTER TABLE Genera
add constraint fk_usuario_genera
foreign key (usuario_ci)
references Usuario(ci)
on update cascade
on delete cascade;

ALTER TABLE Genera
add constraint fk_genera_turno
foreign key (turno_id)
references Turno(id)
on update cascade
on delete cascade;

ALTER TABLE Inicia
add constraint fk_usuario_inicia
foreign key (usuario_ci)
references Usuario(ci)
on update cascade
on delete cascade;

ALTER TABLE Inicia
add constraint fk_inicia_sesion
foreign key (sesion_id)
references Sesion(id)
on update cascade
on delete cascade;

ALTER TABLE Recibe
add constraint fk_usuario_recibe
foreign key (usuario_ci)
references Usuario(ci)
on update cascade
on delete cascade;

ALTER TABLE Recibe
add constraint fk_recibe_receta
foreign key (receta_id)
references Receta(id)
on update cascade
on delete cascade;

ALTER TABLE Contiene
add constraint fk_receta_contiene
foreign key (receta_id)
references Receta(id)
on update cascade
on delete cascade;

ALTER TABLE Contiene
add constraint fk_contiene_medicamento
foreign key (medicamento_id)
references Medicamento(id)
on update cascade
on delete cascade;

ALTER TABLE Asociado
add constraint fk_turno_asociado
foreign key (turno_id)
references Turno(id)
on update cascade
on delete cascade;

ALTER TABLE Asociado
add constraint fk_asociado_receta
foreign key (receta_id)
references Receta(id)
on update cascade
on delete cascade;

ALTER TABLE Asociado
add constraint fk_asociado_receta
foreign key (receta_id)
references Receta(id)
on update cascade
on delete cascade;

ALTER TABLE Tiene
add constraint fk_usuario_tiene
foreign key (usuario_ci)
references Usuario(ci)
on update cascade
on delete cascade;

ALTER TABLE Tiene
add constraint fk_tiene_perfil
foreign key (perfil_id)
references Perfil(id)
on update cascade
on delete cascade;