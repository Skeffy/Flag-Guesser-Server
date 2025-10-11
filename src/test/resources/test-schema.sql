START TRANSACTION;

DROP TABLE IF EXISTS country;

CREATE TABLE country (
	country_id SERIAL,
	name varchar(50) NOT NULL,
	code varchar(2) NOT NULL,
	capital varchar(20) NOT NULL,
	continent varchar(15) NOT NULL,
	flag_image varchar(200),
	CONSTRAINT PK_country PRIMARY KEY (country_id)
);

COMMIT;