INSERT INTO springify.address (id,"name",reg_date,mod_date)
	VALUES (1,'Address 1','2018-09-07 14:55:11.000','2019-09-07 14:55:12.798');

INSERT INTO springify.address (id,"name",reg_date,mod_date)
	VALUES (2,'Test','2000-10-07 14:55:11.000','2019-01-07 14:55:12.798');

INSERT INTO springify.users (id,"name",lastname,address_id,state,reg_date,mod_date)
	VALUES (1,'Test','User Test',1,'ACTIVE','2018-09-07 14:55:11.000','2019-09-07 14:56:42.051');

INSERT INTO springify.users (id,"name",lastname,address_id,state,reg_date,mod_date)
	VALUES (2,'Test','User Test (INACTIVE)',1,'INACTIVE','2018-09-07 14:55:11.000','2019-09-07 14:56:42.000');

INSERT INTO springify.profiles (id,"name",alias,reg_date,mod_date)
	VALUES (1,'Anonymous','ANON','2014-01-11 13:39:11.000','2019-09-11 18:40:00.099');
INSERT INTO springify.profiles (id,"name",alias,reg_date,mod_date)
	VALUES (2,'Admin','ADM','0204-02-14 13:31:11.000','2018-09-04 01:45:13.000');

INSERT INTO springify.user_profiles_map (id_user,id_profile)
	VALUES (1,1);
INSERT INTO springify.user_profiles_map (id_user,id_profile)
	VALUES (2,1);
INSERT INTO springify.user_profiles_map (id_user,id_profile)
	VALUES (2,2);

INSERT INTO springify.roles (id,"name",alias,reg_date,mod_date)
	VALUES (1,'Create','CREATE','2019-09-11 18:53:09.505','2019-09-11 18:53:10.902');
INSERT INTO springify.roles (id,"name",alias,reg_date,mod_date)
	VALUES (2,'Read','READ','2019-09-11 18:53:47.818','2019-09-11 18:53:49.224');
INSERT INTO springify.roles (id,"name",alias,reg_date,mod_date)
	VALUES (3,'Update','UPDATE','2019-09-11 18:54:05.879','2019-09-11 18:54:07.209');
INSERT INTO springify.roles (id,"name",alias,reg_date,mod_date)
	VALUES (4,'Delete','DELETE','2019-09-11 18:54:38.887','2019-09-11 18:54:40.351');

INSERT INTO springify.profiles_roles_map (id_profile,id_role)
	VALUES (1,2);
INSERT INTO springify.profiles_roles_map (id_profile,id_role)
	VALUES (2,1);
INSERT INTO springify.profiles_roles_map (id_profile,id_role)
	VALUES (2,2);
INSERT INTO springify.profiles_roles_map (id_profile,id_role)
	VALUES (2,3);
INSERT INTO springify.profiles_roles_map (id_profile,id_role)
	VALUES (2,4);
