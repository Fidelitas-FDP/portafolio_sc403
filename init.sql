-- este script no se corre en lo absoluto para aiven
-- Sección de administración (ejecutar una vez en un entorno de desarrollo)
drop database if exists techshop;
drop user if exists usuario_prueba;
drop user if exists usuario_reportes;

-- Creación del esquema
CREATE database techshop
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- Creación de usuarios con contraseñas seguras (idealmente asignadas fuera del script)
create user 'usuario_prueba'@'%' identified by 'Usuar1o_Clave.';
create user 'usuario_reportes'@'%' identified by 'Usuar1o_Reportes.';

-- Asignación de permisos
-- Se otorgan permisos específicos en lugar de todos los permisos a todas las tablas futuras
grant all privileges on techshop.* to 'usuario_prueba'@'%';
grant select on techshop.* to 'usuario_reportes'@'%';
flush privileges;
