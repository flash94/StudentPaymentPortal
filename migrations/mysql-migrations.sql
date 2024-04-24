SET GLOBAL general_log_file='/var/log/mysql/postgres.log';
SET GLOBAL general_log = 1;

CREATE DATABASE student;
use student;


CREATE USER 'student-spring-user'@'%' IDENTIFIED BY 'student-secret';
GRANT ALL PRIVILEGES on student.* to `student-spring-user`;
FLUSH PRIVILEGES;