CREATE SCHEMA IF NOT EXISTS customer;

CREATE TABLE customer.customer (
  id IDENTITY PRIMARY KEY,
  first_name VARCHAR(20) NOT NULL,
  last_name VARCHAR(20) NOT NULL,
  password VARCHAR(20) NOT NULL,
  date_of_birth DATE,
  created_on TIMESTAMP WITH TIME ZONE default now(),
  updated_on TIMESTAMP WITH TIME ZONE default now()
);


INSERT INTO customer.customer(first_name, last_name,password, date_of_birth) VALUES('test', 'test', 'test','1992-01-01');
INSERT INTO customer.customer(first_name, last_name,password, date_of_birth) VALUES('tom', 'scott', 'secret','1991-09-05');
INSERT INTO customer.customer(first_name, last_name,password, date_of_birth) VALUES('martin', 'daniel', 'secret','1982-08-01');
INSERT INTO customer.customer(first_name, last_name,password, date_of_birth) VALUES('diana', 'lutz', 'secret','1979-06-22');
INSERT INTO customer.customer(first_name, last_name,password, date_of_birth) VALUES('bruce', 'lee', 'secret','1986-12-11');