create schema if not exists "PUBLIC";

create table adverts (
        id bigint generated by default as identity,
        entry_date timestamp not null,
        entry_user VARCHAR(100),

        active boolean not null,
        email varchar(100) not null,
        lat double not null,
        lng double not null,
        location varchar(80) not null,
        phone varchar(25) not null,
        creationDate timestamp not null,
        description TEXT NOT NULL,
        endDate timestamp,
        owner_name varchar(80),
        rate float,
        thumbUrl varchar(255),
        title varchar(80) not null,
        user_id bigint,
        primary key (id)
);

create table role_names (
        id bigint generated by default as identity,
        entry_date timestamp not null,
        entry_user VARCHAR(100),

        name varchar(80),
        primary key (id)
);

create table roles (
        id bigint generated by default as identity,
        entry_date timestamp not null,
        entry_user VARCHAR(100),

        roleName_id bigint,
        primary key (id)
);

create table user_roles (
        id_user bigint not null,
        id_role bigint not null,
        primary key (id_user, id_role)
);

create table users (
        id bigint generated by default as identity,
        entry_date timestamp not null,
        entry_user VARCHAR(100),

        email varchar(100) not null,
        lat double not null,
        lng double not null,
        location varchar(80) not null,
        phone varchar(25) not null,
        name varchar(255) not null,
        password varchar(80) not null,
        primary key (id)
);

alter table role_names add constraint name_uk  unique (name);
alter table users add constraint email_uk unique (email);
alter table adverts add constraint adverts_users_fk foreign key (user_id) references users;
alter table roles add constraint roles_rolesNames_fk foreign key (roleName_id) references role_names;
alter table user_roles add constraint user_rolesRoles_fk foreign key (id_role) references roles;
alter table user_roles add constraint user_rolesUsers_fk foreign key (id_user) references users;