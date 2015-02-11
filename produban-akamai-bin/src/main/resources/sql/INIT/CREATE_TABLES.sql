#CREATE DATABASE akamai;

CREATE TABLE IF NOT EXISTS configuration (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  kei varchar(32) NOT NULL,
  value varchar(32) NOT NULL, 
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS rules (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  regex varchar(200) NOT NULL,
  numberTimes int NOT NULL,
  window int NOT NULL,
  slideWindow int NOT NULL,
  message varchar(128) NOT NULL,
  PRIMARY KEY (id)
);