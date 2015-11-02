insert into role_names(id, entry_date, name) values (null, CURRENT_TIMESTAMP(), 'USER');
insert into role_names(id, entry_date, name) values (null, CURRENT_TIMESTAMP(), 'ADMIN');
insert into role_names(id, entry_date, name) values (null, CURRENT_TIMESTAMP(), 'VIEW_ADVERTS');

insert into roles(id, entry_date, roleName_id) values (null, CURRENT_TIMESTAMP(), 1);

insert into users(id, entry_date, email, lat, lng, location, phone, name, password)
values (null, CURRENT_TIMESTAMP(), 'user@user.pl', 50.0411867, 21.999119599999972,
        'Rzeszow', '605854368', 'Robert Sikora', 'f3296af46c9ae0d8180456f04090ba73a6ce23de1ede2edb40caaeaff7c56d4280c79bdaaa7e5160');

insert into user_roles(id_user, id_role) values (1, 1);
insert into roles(id, entry_date, roleName_id) values (null, CURRENT_TIMESTAMP(), 2);

insert into users(id, entry_date, email, lat, lng, location, phone, name, password)
values (null, CURRENT_TIMESTAMP(), 'admin@admin.pl', 50.0411867, 21.999119599999972,
        'Rzeszow', '605854368', 'Robert Sikora', '7c0a0c68d2777c451de2d940567408f6920cf4a06c962684a14134173dcc2a5e55e106242b87f6b1');

insert into user_roles(id_user, id_role) values (2, 1);
insert into user_roles(id_user, id_role) values (2, 2);
