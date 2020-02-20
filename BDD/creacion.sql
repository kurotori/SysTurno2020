DROP SCHEMA IF EXISTS systurno;
create schema systurno;
use systurno;

drop user if exists "systurno"@"localhost";
create user "systurno"@"localhost" identified by "systurno";

grant 
insert,select,update,delete,
create routine, alter routine, execute on systurno.* 
to "systurno"@"localhost";

create table usuario(

