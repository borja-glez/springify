INSERT INTO springify.address (id,"name",reg_date,mod_date)
	VALUES (1,'Address 1','2018-09-07 14:55:11.000','2019-09-07 14:55:12.798');

INSERT INTO springify.address (id,"name",reg_date,mod_date)
	VALUES (2,'Test','2000-10-07 14:55:11.000','2019-01-07 14:55:12.798');

INSERT INTO springify.users (id,"name",lastname,address_id,state,reg_date,mod_date)
	VALUES (1,'Test','User Test',1,'ACTIVE','2018-09-07 14:55:11.000','2019-09-07 14:56:42.051');

INSERT INTO springify.users (id,"name",lastname,address_id,state,reg_date,mod_date)
	VALUES (2,'Test','User Test (INACTIVE)',1,'INACTIVE','2018-09-07 14:55:11.000','2019-09-07 14:56:42.000');
