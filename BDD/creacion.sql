DROP SCHEMA IF EXISTS systurno;
create schema systurno;
use systurno;

drop user if exists "systurno"@"localhost";
create user "systurno"@"localhost" identified by "systurno";

grant 
insert,select,update,delete,
create routine, alter routine, execute on systurno.* 
to "systurno"@"localhost";

create table systurno.usuario(
 CI int(8) unsigned unique not null primary key,
 nombre_p varchar(30) not null,
 apellido_p varchar(30) not null,
 hash varchar(128) not null
 );

create table systurno.token(
 ID int(10) unsigned unique not null auto_increment primary key,
 valor varchar(60) not null,
 fechahora timestamp not null default current_timestamp
 );

create table systurno.solicita(
 usuario_CI int(8) unsigned not null,
 token_ID int(10) unsigned unique not null primary key
);

alter table solicita
add constraint fk_usuario_solicita
foreign key (usuario_CI)
references usuario(CI)
on update cascade
on delete cascade;

alter table solicita
add constraint fk_solicita_token
foreign key (token_ID)
references token(ID)
on update cascade
on delete cascade;


