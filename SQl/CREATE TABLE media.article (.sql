CREATE TABLE sample.employees (
   id INT(11) auto_increment,
   name VARCHAR(50) NOT NULL DEFAULT “-”,
   addr VARCHAR(50) NOT NULL,
   age INT(3) NOT NULL DEFAULT 0,
   PRIMARY KEY (id)
);