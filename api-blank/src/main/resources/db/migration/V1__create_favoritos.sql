create table favoritos(
    id bigint generated always as identity primary key,
    producto_id bigint not null,
    nota varchar(500) not null,
    fecha_alta timestamp not null 
);