insert into listas (nombre, descripcion, fecha_creacion)
SELECT 'Sin clasificar', 'Lista generada automáticamente para migrar favoritos huérfanos', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM listas WHERE nombre = 'Sin clasificar');

UPDATE favoritos
SET lista_id = (SELECT id FROM listas WHERE nombre = 'Sin clasificar' LIMIT 1)
WHERE lista_id IS NULL;

ALTER TABLE favoritos
    ALTER COLUMN lista_id SET NOT NULL;