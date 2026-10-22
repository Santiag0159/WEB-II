alter table listas add column usuario_id bigint;

alter table listas add constraint fk_listas_usuarios
foreign key (usuario_id) references usuarios(id);
