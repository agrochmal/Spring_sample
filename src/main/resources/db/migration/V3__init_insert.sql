insert into role_names(id, entry_date, name) values (null, CURRENT_TIMESTAMP(), 'USER');
insert into role_names(id, entry_date, name) values (null, CURRENT_TIMESTAMP(), 'ADMIN');
insert into role_names(id, entry_date, name) values (null, CURRENT_TIMESTAMP(), 'VIEW_ADVERTS');

insert into roles(id, entry_date, roleName_id) values (null, CURRENT_TIMESTAMP(), 1);

insert into users(id, entry_date, email, lat, lng, location, phone, name, password)
values (null, CURRENT_TIMESTAMP(), 'user@user.pl', 50.0411867, 21.999119599999972,
        'Rzeszow', '605854368', 'Robert Sikora', '$2a$10$CAw31P3luMH2K0u97S4l0OCpiowiw8hHSKVc5OwsOMzuXATYLSYgW');

insert into user_roles(id_user, id_role) values (1, 1);
insert into roles(id, entry_date, roleName_id) values (null, CURRENT_TIMESTAMP(), 2);

insert into users(id, entry_date, email, lat, lng, location, phone, name, password)
values (null, CURRENT_TIMESTAMP(), 'admin@admin.pl', 50.0411867, 21.999119599999972,
        'Rzeszow', '605854368', 'Robert Sikora', '$2a$10$ce33e4Bn93pDThjtyGk4pu9.OGQJ5Lin64gf.eebjSVcHGmC4oCTy');

insert into user_roles(id_user, id_role) values (2, 1);
insert into user_roles(id_user, id_role) values (2, 2);
