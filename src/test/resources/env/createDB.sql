CREATE USER IF NOT EXISTS 'ssbd03admin'@'%' IDENTIFIED BY '9LUoYTSMH';
CREATE USER IF NOT EXISTS 'ssbd03auth'@'%' IDENTIFIED BY 'KHgXydJUv';
CREATE USER IF NOT EXISTS 'ssbd03mok'@'%' IDENTIFIED BY 'CHqZxv5R1';
CREATE USER IF NOT EXISTS 'ssbd03mow'@'%' IDENTIFIED BY 'obSjEBGaX';

CREATE DATABASE IF NOT EXISTS ssbd03;

drop table if exists account_confirmation_token;
drop table if exists admin;
drop table if exists annual_balance;
drop table if exists email_confirmation_token;
drop table if exists heat_distribution_centre_pay_off;
drop table if exists heating_place_and_communal_area_advance;
drop table if exists hot_water_advance;
drop table if exists advance;
drop table if exists hot_water_entry;
drop table if exists login_data;
drop table if exists manager;
drop table if exists month_pay_off;
drop table if exists past_quarter_hot_water_pay_off;
drop table if exists personal_data;
drop table if exists place;
drop table if exists building;
drop table if exists address;
drop table if exists heat_distribution_centre;
drop table if exists owner;
drop table if exists access_level_mapping;
drop table if exists reset_password_token;
drop table if exists account;

create table account
(
    id                          bigint auto_increment primary key,
    creation_date_time          datetime             not null,
    last_modification_date_time datetime,
    version                     bigint,
    email                       varchar(255)          not null,
    unique (email),
    is_active                   boolean default false not null,
    is_enable                   boolean default false not null,
    language_                   varchar(255) default 'PL' not null,
    password                    varchar(60)           not null,
    register_date               datetime             not null,
    username                    varchar(16)           not null,
    unique (username),
    created_by                  bigint,
    foreign key (created_by) references account (id),
    last_modified_by            bigint,
    foreign key (last_modified_by) references account (id)
);

create table access_level_mapping
(
    access_level                varchar(31)          not null,
    id                          bigint auto_increment primary key,
    creation_date_time          datetime            not null,
    last_modification_date_time datetime,
    version                     bigint,
    is_active                   boolean default true not null,
    created_by                  bigint,
    last_modified_by            bigint,
    foreign key (last_modified_by) references account (id),
    account_id                  bigint not null,
    foreign key (account_id) references account (id),
    foreign key (created_by) references account (id),
    unique (account_id, access_level)
);

create index access_level_mapping_account_id
    on access_level_mapping (account_id);

create table account_confirmation_token
(
    id                          bigint auto_increment primary key,
    creation_date_time          datetime             not null,
    last_modification_date_time datetime,
    version                     bigint,
    expiration_date             datetime             not null,
    is_reminder_sent            boolean default false not null,
    token_value                 varchar(10)           not null,
    unique (token_value),
    created_by                  bigint,
    foreign key (created_by) references account(id),
    last_modified_by            bigint,
    foreign key (last_modified_by) references account(id),
    account_id                  bigint,
    foreign key (account_id) references account(id)
);

create table address
(
    id                          bigint auto_increment primary key,
    creation_date_time          datetime    not null,
    last_modification_date_time datetime,
    version                     bigint,
    building_number             varchar(255) not null,
    city                        varchar(32)  not null,
    postal_code                 varchar(6)   not null,
    street                      varchar(32)  not null,
    created_by                  bigint,
    foreign key (created_by) references account(id),
    last_modified_by            bigint,
    foreign key (last_modified_by) references account(id)
);

create table admin
(
    id bigint not null primary key,
    foreign key (id) references access_level_mapping(id)
);


create table email_confirmation_token
(
    id                          bigint auto_increment primary key,
    creation_date_time          datetime    not null,
    last_modification_date_time datetime,
    version                     bigint,
    email                       varchar(255) not null,
    expiration_date             datetime    not null,
    token_value                 varchar(10)  not null,
    unique (token_value),
    created_by                  bigint,
    foreign key (created_by) references account(id),
    last_modified_by            bigint,
    foreign key (last_modified_by) references account(id),
    account_id                  bigint,
    foreign key (account_id) references account(id)
);

create table heat_distribution_centre
(
    id                          bigint auto_increment primary key,
    creation_date_time          datetime not null,
    last_modification_date_time datetime,
    version                     bigint,
    created_by                  bigint,
    foreign key (created_by) references account(id),
    last_modified_by            bigint,
    foreign key (last_modified_by) references account(id)
);

create table building
(
    id                          bigint auto_increment primary key,
    creation_date_time          datetime not null,
    last_modification_date_time datetime,
    version                     bigint,
    communal_area_aggregate     decimal(10, 2) not null,
    total_area                  decimal(10, 2) not null,
    created_by                  bigint,
    foreign key (created_by) references account(id),
    last_modified_by            bigint,
    foreign key (last_modified_by) references account(id),
    address_id                  bigint,
    foreign key (address_id) references address(id),
    heat_distribution_centre_id bigint,
    foreign key (heat_distribution_centre_id) references heat_distribution_centre(id)
);

create index building_address_id
    on building (address_id);

create index building_heat_distribution_centre_id
    on building (heat_distribution_centre_id);

create table login_data
(
    creation_date_time          datetime not null,
    last_modification_date_time datetime,
    version                     bigint,
    invalid_login_counter       int default 0,
    check ((invalid_login_counter <= 3) AND (invalid_login_counter >= 0)),
    last_invalid_logic_address  varchar(15),
    last_invalid_login_date     datetime,
    last_valid_logic_address    varchar(15),
    last_valid_login_date       datetime,
    id                          bigint not null primary key,
    created_by                  bigint,
    foreign key (created_by) references account(id),
    last_modified_by            bigint,
    foreign key (last_modified_by) references account(id)
);

create table manager
(
    license varchar(20) not null,
    unique (license),
    id      bigint      not null primary key,
    foreign key (id) references access_level_mapping(id)
);

create table heat_distribution_centre_pay_off
(
    id                          bigint auto_increment primary key,
    creation_date_time          datetime not null,
    last_modification_date_time datetime,
    version                     bigint,
    consumption                 decimal(10, 2) not null,
    consumption_cost            decimal(10, 2) not null,
    date_                       date not null,
    heating_area_factor         decimal(3, 2) not null,
    created_by                  bigint,
    foreign key (created_by) references account(id),
    last_modified_by            bigint,
    foreign key (last_modified_by) references account(id),
    heat_distribution_centre_id bigint,
    foreign key (heat_distribution_centre_id) references heat_distribution_centre(id),
    manager_id                  bigint,
    foreign key (manager_id) references manager(id)
);

create index heat_distribution_centre_pay_off_heat_distribution_centre_id
    on heat_distribution_centre_pay_off (heat_distribution_centre_id);

create index heat_distribution_centre_pay_off_manager_id
    on heat_distribution_centre_pay_off (manager_id);

create table owner
(
    phone_number varchar(9) not null,
    unique (phone_number),
    id           bigint     not null primary key,
    foreign key (id) references access_level_mapping(id)
);

create table personal_data
(
    creation_date_time          datetime not null,
    last_modification_date_time datetime,
    version                     bigint,
    first_name                  varchar(32) not null,
    surname                     varchar(32) not null,
    id                          bigint      not null primary key,
    created_by                  bigint,
    foreign key (id) references account(id),
    foreign key (created_by) references account(id),
    last_modified_by            bigint,
    foreign key (last_modified_by) references account(id)
);

create table place
(
    id                              bigint auto_increment primary key,
    creation_date_time              datetime not null,
    last_modification_date_time     datetime,
    version                         bigint,
    area                            decimal(10, 2) not null,
    central_heating_connection      boolean not null,
    hot_water_connection            boolean not null,
    place_number                    smallint not null,
    predicted_hot_water_consumption decimal(10, 2) not null,
    created_by                      bigint,
    foreign key (created_by) references account(id),
    last_modified_by                bigint,
    foreign key (last_modified_by) references account(id),
    building_id                     bigint,
    foreign key (building_id) references building(id),
    owner_id                        bigint,
    foreign key (owner_id) references owner(id),
    unique (place_number, building_id)
);

create table advance
(
    id                          bigint auto_increment primary key,
    creation_date_time          datetime not null,
    last_modification_date_time datetime,
    version                     bigint,
    date_                       date not null,
    created_by                  bigint,
    foreign key (created_by) references account(id),
    last_modified_by            bigint,
    foreign key (last_modified_by) references account(id),
    place_id                    bigint,
    foreign key (place_id) references place(id)
);

create index advance_place_id
    on advance (place_id);

create table annual_balance
(
    id                                  bigint auto_increment primary key,
    creation_date_time                  datetime not null,
    last_modification_date_time         datetime,
    version                             bigint,
    total_heating_communal_area_advance decimal(10, 2) not null,
    total_heating_communal_area_cost    decimal(10, 2) not null,
    total_heating_place_advance         decimal(10, 2) not null,
    total_heating_place_cost            decimal(10, 2) not null,
    total_hot_water_advance             decimal(10, 2) not null,
    total_hot_water_cost                decimal(10, 2) not null,
    year_                               smallint not null check (year_ >= 2021),
    created_by                          bigint,
    foreign key (created_by) references account(id),
    last_modified_by                    bigint,
    foreign key (last_modified_by) references account(id),
    place_id                            bigint,
    foreign key (place_id) references place(id)
);

create index annual_balance_place_id
    on annual_balance (place_id);

create table heating_place_and_communal_area_advance
(
    advance_change_factor               decimal(19, 2) not null check (advance_change_factor >= 0),
    heating_communal_area_advance_value decimal(10, 2) not null,
    heating_place_advance_value         decimal(10, 2) not null,
    id                                  bigint not null primary key,
    foreign key (id) references advance(id)
);

create table hot_water_advance
(
    hot_water_advance_value decimal(10, 2) not null,
    id                      bigint         not null primary key,
    foreign key (id) references advance(id)
);

create table hot_water_entry
(
    id                          bigint auto_increment primary key,
    creation_date_time          datetime not null,
    last_modification_date_time datetime,
    version                     bigint,
    date_                       date not null,
    entry_value                 decimal(10, 2) not null,
    created_by                  bigint,
    foreign key (created_by) references account(id),
    last_modified_by            bigint,
    foreign key (last_modified_by) references account(id),
    manager_id                  bigint,
    foreign key (manager_id) references manager(id),
    place_id                    bigint,
    foreign key (place_id) references place(id)
);

create index hot_water_entry_place_id
    on hot_water_entry (place_id);

create index hot_water_entry_manager_id
    on hot_water_entry (manager_id);

create table month_pay_off
(
    id                          bigint auto_increment primary key,
    creation_date_time          datetime not null,
    last_modification_date_time datetime,
    version                     bigint,
    central_heating_unit_cost   decimal(10, 2) not null,
    hot_water_consumption       decimal(10, 2) not null,
    payoff_date                 date not null,
    water_heating_unit_cost     decimal(10, 2) not null,
    created_by                  bigint,
    foreign key (created_by) references account(id),
    last_modified_by            bigint,
    foreign key (last_modified_by) references account(id),
    owner_id                    bigint,
    foreign key (owner_id) references owner(id),
    place_id                    bigint,
    foreign key (place_id) references place(id)
);

create index month_pay_off_place_id
    on month_pay_off (place_id);

create index month_pay_off_owner_id
    on month_pay_off (owner_id);

create table past_quarter_hot_water_pay_off
(
    creation_date_time          datetime not null,
    last_modification_date_time datetime,
    version                     bigint,
    average_consumption         decimal(10, 2) not null check (average_consumption >= 0),
    days_number_in_quarter      int not null check (days_number_in_quarter >= 90 and days_number_in_quarter <= 92),
    id                          bigint not null primary key,
    foreign key (id) references place(id),
    created_by                  bigint,
    foreign key (created_by) references account(id),
    last_modified_by            bigint,
    foreign key (last_modified_by) references account(id)
);

create index place_building_id
    on place (building_id);

create index place_owner_id
    on place (owner_id);

create table reset_password_token
(
    id                          bigint auto_increment primary key,
    creation_date_time          datetime not null,
    last_modification_date_time datetime,
    version                     bigint,
    expiration_date             datetime not null,
    token_value                 varchar(10) not null,
    unique (token_value),
    created_by                  bigint,
    foreign key (created_by) references account(id),
    last_modified_by            bigint,
    foreign key (last_modified_by) references account(id),
    account_id                  bigint,
    foreign key (account_id) references account(id)
);



CREATE OR REPLACE VIEW glassfish_auth_view AS
SELECT username, password, access_level
FROM account
         JOIN access_level_mapping ON account.id = access_level_mapping.account_id
WHERE account.is_active = true
  AND account.is_enable = true
  AND access_level_mapping.is_active = true;

GRANT SELECT ON TABLE glassfish_auth_view TO ssbd03auth;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE access_level_mapping TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE account TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE owner TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE admin TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE manager TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE personal_data TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE login_data TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE account_confirmation_token TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE email_confirmation_token TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE reset_password_token TO ssbd03mok;
GRANT SELECT ON TABLE access_level_mapping TO ssbd03mow;
GRANT SELECT ON TABLE account TO ssbd03mow;
GRANT SELECT ON TABLE owner TO ssbd03mow;
GRANT SELECT ON TABLE admin TO ssbd03mow;
GRANT SELECT ON TABLE manager TO ssbd03mow;
GRANT SELECT ON TABLE personal_data TO ssbd03mow;
GRANT SELECT ON TABLE login_data TO ssbd03mow;
GRANT SELECT, INSERT, UPDATE ON TABLE building TO ssbd03mow;
GRANT SELECT, INSERT ON TABLE address TO ssbd03mow;
GRANT SELECT, INSERT, UPDATE ON TABLE annual_balance TO ssbd03mow;
GRANT SELECT, INSERT, UPDATE ON TABLE heat_distribution_centre TO ssbd03mow;
GRANT SELECT, INSERT ON TABLE heat_distribution_centre_pay_off TO ssbd03mow;
GRANT SELECT, INSERT, UPDATE ON TABLE hot_water_entry TO ssbd03mow;
GRANT SELECT, INSERT ON TABLE month_pay_off TO ssbd03mow;
GRANT SELECT, INSERT, UPDATE ON TABLE past_quarter_hot_water_pay_off TO ssbd03mow;
GRANT SELECT, INSERT, UPDATE ON TABLE place TO ssbd03mow;
GRANT SELECT, INSERT, UPDATE ON TABLE advance TO ssbd03mow;
GRANT SELECT, INSERT, UPDATE ON TABLE hot_water_advance TO ssbd03mow;
GRANT SELECT, INSERT, UPDATE ON TABLE heating_place_and_communal_area_advance TO ssbd03mow;

INSERT INTO address (id, street, building_number, city, postal_code, version, creation_date_time,
                     last_modification_date_time)
VALUES (1, 'Main Street', 12, 'New York', '12-345', 1, NOW(), NOW()),
       (-1, 'Abbey Road', 7, 'London', '34-123', 1, NOW(), NOW()),
       (-2, 'Champs-Élysées', 15, 'Paris', '12-345', 1, NOW(), NOW()),
       (-3, 'Przykladowa 1', 'Warszawa', '00-001', 'Polska', 1, NOW(), NOW());
INSERT INTO account (id, email, username, password, is_enable, is_active, register_date, language_, version,
                     creation_date_time, last_modification_date_time)
VALUES (1, 'johndoe@example.com', 'johndoe', '$2a$10$JgIl/CXyYZtLFXMfYJ34Neh5JYlS/.Um5KWArokqP.rZHr085sAzq', TRUE, TRUE,
        '2023-03-13 21:01:30.766600', 'EN', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-1, 'janekowalski@example.com', 'janekowalski', '$2a$10$JgIl/CXyYZtLFXMfYJ34Neh5JYlS/.Um5KWArokqP.rZHr085sAzq',
        TRUE, TRUE, '2023-03-13 21:01:30.766600', 'PL', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-2, 'maria.silva@example.com', 'mariasilva', '$2a$10$JgIl/CXyYZtLFXMfYJ34Neh5JYlS/.Um5KWArokqP.rZHr085sAzq',
        TRUE, TRUE, '2023-03-13 21:01:30.766600', 'PL', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-3, 'owner@example.com', 'ownerTestowy', '$2a$10$JgIl/CXyYZtLFXMfYJ34Neh5JYlS/.Um5KWArokqP.rZHr085sAzq', TRUE,
        TRUE, '2023-03-13 21:01:30.766600', 'EN', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-4, 'emmajohnson@example.com', 'emmaJohnson', '$2a$10$JgIl/CXyYZtLFXMfYJ34Neh5JYlS/.Um5KWArokqP.rZHr085sAzq',
        TRUE, TRUE, '2023-03-13 21:01:30.766600', 'EN', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-5, 'SophiaBrown@example.com', 'SophiaBrown', '$2a$10$JgIl/CXyYZtLFXMfYJ34Neh5JYlS/.Um5KWArokqP.rZHr085sAzq',
        TRUE, TRUE, '2023-03-13 21:01:30.766600', 'EN', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-6, 'JacksonLee@example.com', 'JacksonLee', '$2a$10$JgIl/CXyYZtLFXMfYJ34Neh5JYlS/.Um5KWArokqP.rZHr085sAzq',
        TRUE, TRUE, '2023-03-13 21:01:30.766600', 'EN', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-7, 'KarolRadomski@example.com', 'KarolRadomski',
        '$2a$10$JgIl/CXyYZtLFXMfYJ34Neh5JYlS/.Um5KWArokqP.rZHr085sAzq', TRUE, TRUE, '2023-03-13 21:01:30.766600', 'EN',
        1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600');
INSERT INTO personal_data (id, first_name, surname, version, creation_date_time, last_modification_date_time)
VALUES (1, 'John', 'Doe', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-1, 'Jan', 'Kowalski', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-2, 'Maria', 'Silva', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-3, 'Owner', 'Smith', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-4, 'Emma', 'Johnson', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-5, 'Sophia', 'Brown', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-6, 'Jackson', 'Lee', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-7, 'Karol', 'Radomski', 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600');
INSERT INTO login_data (id, last_valid_login_date, last_valid_logic_address, last_invalid_login_date,
                        last_invalid_logic_address, invalid_login_counter, version, creation_date_time,
                        last_modification_date_time)
VALUES (1, '2023-03-13 21:01:30.766600', '192.168.0.1', '2023-03-13 21:01:30.766600', '192.168.0.2', 2, 1,
        '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-1, '2023-03-13 21:01:30.766600', '10.0.0.1', '2023-03-13 21:01:30.766600', '10.0.0.2', 2, 1,
        '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-2, '2023-03-13 21:01:30.766600', '192.168.1.1', '2023-03-13 21:01:30.766600', '192.168.1.2', 1, 1,
        '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-3, '2023-03-13 21:01:30.766600', '192.168.2.1', '2023-03-13 21:01:30.766600', '192.168.2.2', 1, 1,
        '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-4, '2023-03-13 21:01:30.766600', '192.168.0.1', '2023-03-13 21:01:30.766600', '192.168.0.2', 2, 1,
        '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-5, '2023-03-13 21:01:30.766600', '192.168.22.12', '2023-03-13 21:01:30.766600', '192.168.12.8', 1, 1,
        '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-6, '2023-03-13 21:01:30.766600', '192.168.1.18', '2023-03-13 21:01:30.766600', '192.168.2.10', 1, 1,
        '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-7, '2023-03-13 21:01:30.766600', '192.168.1.18', '2023-03-13 21:01:30.766600', '192.168.2.10', 1, 1,
        '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600');
INSERT INTO access_level_mapping (id, access_level, is_active, account_id, version, creation_date_time,
                                  last_modification_date_time)
VALUES (1, 'ADMIN', true, 1, 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-1, 'MANAGER', true, -1, 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-2, 'OWNER', true, -2, 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-3, 'OWNER', true, -3, 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-4, 'OWNER', true, -4, 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-5, 'MANAGER', true, -5, 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-6, 'MANAGER', true, -6, 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600'),
       (-7, 'ADMIN', true, -7, 1, '2023-03-13 21:01:30.766600', '2023-03-13 21:01:30.766600');
INSERT INTO admin (id)
VALUES (1),
       (-7);
INSERT INTO manager (id, license)
VALUES (-5, '16309276031841959218'),
       (-6, '50396147920982670543'),
       (-1, '45434335353535464653');
INSERT INTO owner (id, phone_number)
VALUES (-2, '123456789'),
       (-3, '579314682'),
       (-4, '639247815');
INSERT INTO heat_distribution_centre (id, version, creation_date_time, last_modification_date_time)
VALUES (1, 1, '2022-01-01 00:30:30.7666', '2021-01-01 00:30:30.7666');
INSERT INTO heat_distribution_centre_pay_off (id, date_, consumption, consumption_cost, heating_area_factor,
                                              heat_distribution_centre_id, manager_id, version, creation_date_time,
                                              last_modification_date_time)
VALUES (-1, '2022-01-01', 270.00, 6750, 0.78, 1, -1, 1, '2022-01-01 08:32:51.0000', NULL),
       (-2, '2022-02-01', 240.00, 6000, 0.76, 1, -1, 1, '2022-02-01 08:45:12.0000', NULL),
       (-3, '2022-03-01', 152.00, 3800, 0.63, 1, -5, 1, '2022-03-01 09:20:30.0000', NULL),
       (-4, '2022-04-01', 98.00, 2450, 0.42, 1, -1, 1, '2022-04-01 10:15:05.0000', NULL),
       (-5, '2022-05-01', 73.00, 1825, 0.25, 1, -5, 1, '2022-05-01 11:05:40.0000', NULL),
       (-6, '2022-06-01', 64.00, 1600, 0.14, 1, -6, 1, '2022-06-01 12:40:20.0000', NULL),
       (-7, '2022-07-01', 61.00, 1525, 0.06, 1, -5, 1, '2022-07-01 13:55:15.0000', NULL),
       (-8, '2022-08-01', 60.00, 1500, 0.05, 1, -6, 1, '2022-08-01 14:25:30.0000', NULL),
       (-9, '2022-09-01', 70.00, 1750, 0.15, 1, -1, 1, '2022-09-01 15:10:20.0000', NULL),
       (-10, '2022-10-01', 96.00, 2400, 0.4, 1, -5, 1, '2022-10-01 16:00:45.0000', NULL),
       (-11, '2022-11-01', 142.00, 3550, 0.6, 1, -6, 1, '2022-11-01 08:12:50.0000', NULL),
       (-12, '2022-12-01', 242.00, 6050, 0.75, 1, -5, 1, '2022-12-01 09:38:05.0000', NULL),
       (-13, '2023-01-01', 285.00, 7210.5, 0.8, 1, -1, 1, '2023-01-01 10:50:15.0000', NULL),
       (-14, '2023-02-01', 198.00, 5009.4, 0.69, 1, -6, 1, '2023-02-01 11:22:30.0000', NULL),
       (-15, '2023-03-01', 142.00, 3592.6, 0.57, 1, -1, 1, '2023-03-01 12:45:55.0000', NULL),
       (-16, '2023-04-01', 93.00, 2352.9, 0.36, 1, -6, 1, '2023-04-01 13:10:40.0000', NULL),
       (-17, '2023-05-01', 78.00, 1973.4, 0.25, 1, -1, 1, '2023-05-01 14:55:20.0000', NULL),
       (-18, '2023-06-01', 66.00, 1669.8, 0.14, 1, -1, 1, '2023-06-01 15:40:10.0000', NULL);
INSERT INTO building (id, total_area, communal_area_aggregate, address_id, heat_distribution_centre_id, version,
                      creation_date_time, last_modification_date_time)
VALUES (1, 400.00, 120.00, 1, 1, 1, '2022-01-02 00:30:30.7666', '2022-01-02 00:30:30.7666'),
       (-1, 820.00, 520.00, 1, 1, 1, '2023-01-02 00:30:30.7666', '2022-01-02 00:30:30.7666');
INSERT INTO place (id, place_number, area, hot_water_connection, central_heating_connection,
                   predicted_hot_water_consumption, building_id, owner_id, version, creation_date_time,
                   last_modification_date_time)
VALUES (1, 1, 115.25, TRUE, TRUE, 3.43, 1, -2, 1, '2022-01-02 00:31:30.7666', '2022-01-02 00:31:30.7666'),
       (-1, 2, 164.75, TRUE, TRUE, 3.78, 1, -3, 1, '2022-01-02 00:31:30.7666', '2022-01-02 00:31:30.7666'),
       (-2, 1, 78.35, TRUE, TRUE, 3.12, -1, -4, 1, '2022-01-02 00:31:30.7666', '2022-01-02 00:31:30.7666'),
       (-3, 2, 121.32, TRUE, TRUE, 3.99, -1, -2, 1, '2022-01-02 00:31:30.7666', '2022-01-02 00:31:30.7666'),
       (-4, 3, 100.33, TRUE, TRUE, 4.76, -1, -3, 1, '2022-01-02 00:31:30.7666', '2022-01-02 00:31:30.7666');
INSERT INTO advance (id, date_, place_id, version, creation_date_time, last_modification_date_time)
VALUES (1, '2022-01-01', 1, 1, '2022-01-02 01:31:30.7666', '2022-01-02 01:31:30.7666'),
       (-1, '2022-01-01', -4, 1, '2022-01-02 01:31:30.7666', '2022-01-02 01:31:30.7666'),
       (-2, '2022-01-01', -3, 1, '2022-01-02 01:31:30.7666', '2022-01-02 01:31:30.7666'),
       (-3, '2022-01-01', -2, 1, '2022-01-02 01:31:30.7666', '2022-01-02 01:31:30.7666'),
       (-4, '2022-01-01', -1, 1, '2022-01-02 01:31:30.7666', '2022-01-02 01:31:30.7666'),
       (-5, '2022-02-01', 1, 1, '2022-02-02 01:31:30.7666', '2022-02-02 01:31:30.7666'),
       (-6, '2022-02-01', -4, 1, '2022-02-02 01:31:30.7666', '2022-02-02 01:31:30.7666'),
       (-7, '2022-02-01', -3, 1, '2022-02-02 01:31:30.7666', '2022-02-02 01:31:30.7666'),
       (-8, '2022-02-01', -2, 1, '2022-02-02 01:31:30.7666', '2022-02-02 01:31:30.7666'),
       (-9, '2022-02-01', -1, 1, '2022-02-02 01:31:30.7666', '2022-02-02 01:31:30.7666'),
       (-10, '2022-03-01', 1, 1, '2022-03-02 01:31:30.7666', '2022-03-02 01:31:30.7666'),
       (-11, '2022-03-01', -4, 1, '2022-03-02 01:31:30.7666', '2022-03-02 01:31:30.7666'),
       (-12, '2022-03-01', -3, 1, '2022-03-02 01:31:30.7666', '2022-03-02 01:31:30.7666'),
       (-13, '2022-03-01', -2, 1, '2022-03-02 01:31:30.7666', '2022-03-02 01:31:30.7666'),
       (-14, '2022-03-01', -1, 1, '2022-03-02 01:31:30.7666', '2022-03-02 01:31:30.7666'),
       (-15, '2022-04-01', 1, 1, '2022-04-02 01:31:30.7666', '2022-04-02 01:31:30.7666'),
       (-16, '2022-04-01', -4, 1, '2022-04-02 01:31:30.7666', '2022-04-02 01:31:30.7666'),
       (-17, '2022-04-01', -3, 1, '2022-04-02 01:31:30.7666', '2022-04-02 01:31:30.7666'),
       (-18, '2022-04-01', -2, 1, '2022-04-02 01:31:30.7666', '2022-04-02 01:31:30.7666'),
       (-19, '2022-04-01', -1, 1, '2022-04-02 01:31:30.7666', '2022-04-02 01:31:30.7666'),
       (-20, '2022-05-01', 1, 1, '2022-05-02 01:31:30.7666', '2022-05-02 01:31:30.7666'),
       (-21, '2022-05-01', -4, 1, '2022-05-02 01:31:30.7666', '2022-05-02 01:31:30.7666'),
       (-22, '2022-05-01', -3, 1, '2022-05-02 01:31:30.7666', '2022-05-02 01:31:30.7666'),
       (-23, '2022-05-01', -2, 1, '2022-05-02 01:31:30.7666', '2022-05-02 01:31:30.7666'),
       (-24, '2022-05-01', -1, 1, '2022-05-02 01:31:30.7666', '2022-05-02 01:31:30.7666'),
       (-25, '2022-06-01', 1, 1, '2022-06-02 01:31:30.7666', '2022-06-02 01:31:30.7666'),
       (-26, '2022-06-01', -4, 1, '2022-06-02 01:31:30.7666', '2022-06-02 01:31:30.7666'),
       (-27, '2022-06-01', -3, 1, '2022-06-02 01:31:30.7666', '2022-06-02 01:31:30.7666'),
       (-28, '2022-06-01', -2, 1, '2022-06-02 01:31:30.7666', '2022-06-02 01:31:30.7666'),
       (-29, '2022-06-01', -1, 1, '2022-06-02 01:31:30.7666', '2022-06-02 01:31:30.7666'),
       (-30, '2022-07-01', 1, 1, '2022-07-02 01:31:30.7666', '2022-07-02 01:31:30.7666'),
       (-31, '2022-07-01', -4, 1, '2022-07-02 01:31:30.7666', '2022-07-02 01:31:30.7666'),
       (-32, '2022-07-01', -3, 1, '2022-07-02 01:31:30.7666', '2022-07-02 01:31:30.7666'),
       (-33, '2022-07-01', -2, 1, '2022-07-02 01:31:30.7666', '2022-07-02 01:31:30.7666'),
       (-34, '2022-07-01', -1, 1, '2022-07-02 01:31:30.7666', '2022-07-02 01:31:30.7666'),
       (-35, '2022-08-01', 1, 1, '2022-08-02 01:31:30.7666', '2022-08-02 01:31:30.7666'),
       (-36, '2022-08-01', -4, 1, '2022-08-02 01:31:30.7666', '2022-08-02 01:31:30.7666'),
       (-37, '2022-08-01', -3, 1, '2022-08-02 01:31:30.7666', '2022-08-02 01:31:30.7666'),
       (-38, '2022-08-01', -2, 1, '2022-08-02 01:31:30.7666', '2022-08-02 01:31:30.7666'),
       (-39, '2022-08-01', -1, 1, '2022-08-02 01:31:30.7666', '2022-08-02 01:31:30.7666'),
       (-40, '2022-09-01', 1, 1, '2022-09-02 01:31:30.7666', '2022-09-02 01:31:30.7666'),
       (-41, '2022-09-01', -4, 1, '2022-09-02 01:31:30.7666', '2022-09-02 01:31:30.7666'),
       (-42, '2022-09-01', -3, 1, '2022-09-02 01:31:30.7666', '2022-09-02 01:31:30.7666'),
       (-43, '2022-09-01', -2, 1, '2022-09-02 01:31:30.7666', '2022-09-02 01:31:30.7666'),
       (-44, '2022-09-01', -1, 1, '2022-09-02 01:31:30.7666', '2022-09-02 01:31:30.7666'),
       (-45, '2022-10-01', 1, 1, '2022-10-02 01:31:30.7666', '2022-10-02 01:31:30.7666'),
       (-46, '2022-10-01', -4, 1, '2022-10-02 01:31:30.7666', '2022-10-02 01:31:30.7666'),
       (-47, '2022-10-01', -3, 1, '2022-10-02 01:31:30.7666', '2022-10-02 01:31:30.7666'),
       (-48, '2022-10-01', -2, 1, '2022-10-02 01:31:30.7666', '2022-10-02 01:31:30.7666'),
       (-49, '2022-10-01', -1, 1, '2022-10-02 01:31:30.7666', '2022-10-02 01:31:30.7666'),
       (-50, '2022-11-01', 1, 1, '2022-11-02 01:31:30.7666', '2022-11-02 01:31:30.7666'),
       (-51, '2022-11-01', -4, 1, '2022-11-02 01:31:30.7666', '2022-11-02 01:31:30.7666'),
       (-52, '2022-11-01', -3, 1, '2022-11-02 01:31:30.7666', '2022-11-02 01:31:30.7666'),
       (-53, '2022-11-01', -2, 1, '2022-11-02 01:31:30.7666', '2022-11-02 01:31:30.7666'),
       (-54, '2022-11-01', -1, 1, '2022-11-02 01:31:30.7666', '2022-11-02 01:31:30.7666'),
       (-55, '2022-12-01', 1, 1, '2022-12-02 01:31:30.7666', '2022-12-02 01:31:30.7666'),
       (-56, '2022-12-01', -4, 1, '2022-12-02 01:31:30.7666', '2022-12-02 01:31:30.7666'),
       (-57, '2022-12-01', -3, 1, '2022-12-02 01:31:30.7666', '2022-12-02 01:31:30.7666'),
       (-58, '2022-12-01', -2, 1, '2022-12-02 01:31:30.7666', '2022-12-02 01:31:30.7666'),
       (-59, '2022-12-01', -1, 1, '2022-12-02 01:31:30.7666', '2022-12-02 01:31:30.7666'),
       (-60, '2023-01-01', 1, 1, '2023-01-02 01:31:30.7666', '2023-01-02 01:31:30.7666'),
       (-61, '2023-01-01', -4, 1, '2023-01-02 01:31:30.7666', '2023-01-02 01:31:30.7666'),
       (-62, '2023-01-01', -3, 1, '2023-01-02 01:31:30.7666', '2023-01-02 01:31:30.7666'),
       (-63, '2023-01-01', -2, 1, '2023-01-02 01:31:30.7666', '2023-01-02 01:31:30.7666'),
       (-64, '2023-01-01', -1, 1, '2023-01-02 01:31:30.7666', '2023-01-02 01:31:30.7666'),
       (-65, '2023-02-01', 1, 1, '2023-02-02 01:31:30.7666', '2023-02-02 01:31:30.7666'),
       (-66, '2023-02-01', -4, 1, '2023-02-02 01:31:30.7666', '2023-02-02 01:31:30.7666'),
       (-67, '2023-02-01', -3, 1, '2023-02-02 01:31:30.7666', '2023-02-02 01:31:30.7666'),
       (-68, '2023-02-01', -2, 1, '2023-02-02 01:31:30.7666', '2023-02-02 01:31:30.7666'),
       (-69, '2023-02-01', -1, 1, '2023-02-02 01:31:30.7666', '2023-02-02 01:31:30.7666'),
       (-70, '2023-03-01', 1, 1, '2023-03-02 01:31:30.7666', '2023-03-02 01:31:30.7666'),
       (-71, '2023-03-01', -4, 1, '2023-03-02 01:31:30.7666', '2023-03-02 01:31:30.7666'),
       (-72, '2023-03-01', -3, 1, '2023-03-02 01:31:30.7666', '2023-03-02 01:31:30.7666'),
       (-73, '2023-03-01', -2, 1, '2023-03-02 01:31:30.7666', '2023-03-02 01:31:30.7666'),
       (-74, '2023-03-01', -1, 1, '2023-03-02 01:31:30.7666', '2023-03-02 01:31:30.7666'),
       (-75, '2023-04-01', 1, 1, '2023-04-02 01:31:30.7666', '2023-04-02 01:31:30.7666'),
       (-76, '2023-04-01', -4, 1, '2023-04-02 01:31:30.7666', '2023-04-02 01:31:30.7666'),
       (-77, '2023-04-01', -3, 1, '2023-04-02 01:31:30.7666', '2023-04-02 01:31:30.7666'),
       (-78, '2023-04-01', -2, 1, '2023-04-02 01:31:30.7666', '2023-04-02 01:31:30.7666'),
       (-79, '2023-04-01', -1, 1, '2023-04-02 01:31:30.7666', '2023-04-02 01:31:30.7666'),
       (-80, '2023-05-01', 1, 1, '2023-05-02 01:31:30.7666', '2023-05-02 01:31:30.7666'),
       (-81, '2023-05-01', -4, 1, '2023-05-02 01:31:30.7666', '2023-05-02 01:31:30.7666'),
       (-82, '2023-05-01', -3, 1, '2023-05-02 01:31:30.7666', '2023-05-02 01:31:30.7666'),
       (-83, '2023-05-01', -2, 1, '2023-05-02 01:31:30.7666', '2023-05-02 01:31:30.7666'),
       (-84, '2023-05-01', -1, 1, '2023-05-02 01:31:30.7666', '2023-05-02 01:31:30.7666'),
       (-85, '2023-06-01', 1, 1, '2023-06-02 01:31:30.7666', '2023-06-02 01:31:30.7666'),
       (-86, '2023-06-01', -4, 1, '2023-06-02 01:31:30.7666', '2023-06-02 01:31:30.7666'),
       (-87, '2023-06-01', -3, 1, '2023-06-02 01:31:30.7666', '2023-06-02 01:31:30.7666'),
       (-88, '2023-06-01', -2, 1, '2023-06-02 01:31:30.7666', '2023-06-02 01:31:30.7666'),
       (-89, '2023-06-01', -1, 1, '2023-06-02 01:31:30.7666', '2023-06-02 01:31:30.7666');
INSERT INTO hot_water_advance (id, hot_water_advance_value)
VALUES (1, 295.53),
       (-1, 295.53),
       (-2, 295.53),
       (-3, 295.53),
       (-4, 295.53),
       (-5, 295.53),
       (-6, 295.53),
       (-7, 295.53),
       (-8, 295.53),
       (-9, 295.53),
       (-10, 295.53),
       (-11, 295.53),
       (-12, 295.53),
       (-13, 295.53),
       (-14, 295.53),
       (-15, 295.53),
       (-16, 295.53),
       (-17, 295.53),
       (-18, 295.53),
       (-19, 295.53),
       (-20, 295.53),
       (-21, 295.53),
       (-22, 295.53),
       (-23, 295.53),
       (-24, 295.53),
       (-25, 295.53),
       (-26, 295.53),
       (-27, 295.53),
       (-28, 295.53),
       (-29, 295.53),
       (-30, 301.44),
       (-31, 301.44),
       (-32, 301.44),
       (-33, 301.44),
       (-34, 301.44),
       (-35, 301.44),
       (-36, 301.44),
       (-37, 301.44),
       (-38, 301.44),
       (-39, 301.44),
       (-40, 301.44),
       (-41, 301.44),
       (-42, 301.44),
       (-43, 301.44),
       (-44, 301.44),
       (-45, 319.53),
       (-46, 319.53),
       (-47, 319.53),
       (-48, 319.53),
       (-49, 319.53),
       (-50, 319.53),
       (-51, 319.53),
       (-52, 319.53),
       (-53, 319.53),
       (-54, 319.53),
       (-55, 319.53),
       (-56, 319.53),
       (-57, 319.53),
       (-58, 319.53),
       (-59, 319.53),
       (-60, 329.11),
       (-61, 329.11),
       (-62, 329.11),
       (-63, 329.11),
       (-64, 329.11),
       (-65, 329.11),
       (-66, 329.11),
       (-67, 329.11),
       (-68, 329.11),
       (-69, 329.11),
       (-70, 329.11),
       (-71, 329.11),
       (-72, 329.11),
       (-73, 329.11),
       (-74, 329.11),
       (-75, 332.40),
       (-76, 332.40),
       (-77, 332.40),
       (-78, 332.40),
       (-79, 332.40),
       (-80, 332.40),
       (-81, 332.40),
       (-82, 332.40),
       (-83, 332.40),
       (-84, 332.40),
       (-85, 332.40),
       (-86, 332.40),
       (-87, 332.40),
       (-88, 332.40),
       (-89, 332.40);
INSERT INTO hot_water_entry (id, date_, entry_value, place_id, manager_id, version, creation_date_time,
                             last_modification_date_time)
VALUES (1, '2022-01-01', 126.10, 1, NULL, 1, '2022-01-01', NULL),
       (-1, '2022-01-01', 118.00, -4, NULL, 1, '2022-01-01', NULL),
       (-2, '2022-01-01', 111.00, -3, NULL, 1, '2022-01-01', NULL),
       (-3, '2022-01-01', 150.00, -2, NULL, 1, '2022-01-01', NULL),
       (-4, '2022-01-01', 110.00, -1, NULL, 1, '2022-01-01', NULL),
       (-5, '2022-02-01', 136.10, 1, NULL, 1, '2022-02-01', NULL),
       (-6, '2022-02-01', 128.00, -4, NULL, 1, '2022-02-01', NULL),
       (-7, '2022-02-01', 121.00, -3, NULL, 1, '2022-02-01', NULL),
       (-8, '2022-02-01', 160.00, -2, NULL, 1, '2022-02-01', NULL),
       (-9, '2022-02-01', 120.00, -1, NULL, 1, '2022-02-01', NULL),
       (-10, '2022-03-01', 146.10, 1, NULL, 1, '2022-03-01', NULL),
       (-11, '2022-03-01', 138.00, -4, NULL, 1, '2022-03-01', NULL),
       (-12, '2022-03-01', 131.00, -3, NULL, 1, '2022-03-01', NULL),
       (-13, '2022-03-01', 170.00, -2, NULL, 1, '2022-03-01', NULL),
       (-14, '2022-03-01', 130.00, -1, NULL, 1, '2022-03-01', NULL),
       (-15, '2022-04-01', 156.10, 1, NULL, 1, '2022-04-01', NULL),
       (-16, '2022-04-01', 148.00, -4, NULL, 1, '2022-04-01', NULL),
       (-17, '2022-04-01', 141.00, -3, NULL, 1, '2022-04-01', NULL),
       (-18, '2022-04-01', 180.00, -2, NULL, 1, '2022-04-01', NULL),
       (-19, '2022-04-01', 140.00, -1, NULL, 1, '2022-04-01', NULL),
       (-20, '2022-05-01', 166.10, 1, NULL, 1, '2022-05-01', NULL),
       (-21, '2022-05-01', 158.00, -4, NULL, 1, '2022-05-01', NULL),
       (-22, '2022-05-01', 151.00, -3, NULL, 1, '2022-05-01', NULL),
       (-23, '2022-05-01', 190.00, -2, NULL, 1, '2022-05-01', NULL),
       (-24, '2022-05-01', 150.00, -1, NULL, 1, '2022-05-01', NULL),
       (-25, '2022-06-01', 176.10, 1, NULL, 1, '2022-06-01', NULL),
       (-26, '2022-06-01', 168.00, -4, NULL, 1, '2022-06-01', NULL),
       (-27, '2022-06-01', 161.00, -3, NULL, 1, '2022-06-01', NULL),
       (-28, '2022-06-01', 200.00, -2, NULL, 1, '2022-06-01', NULL),
       (-29, '2022-06-01', 160.00, -1, NULL, 1, '2022-06-01', NULL),
       (-30, '2022-07-01', 186.10, 1, NULL, 1, '2022-07-01', NULL),
       (-31, '2022-07-01', 178.00, -4, NULL, 1, '2022-07-01', NULL),
       (-32, '2022-07-01', 171.00, -3, NULL, 1, '2022-07-01', NULL),
       (-33, '2022-07-01', 210.00, -2, NULL, 1, '2022-07-01', NULL),
       (-34, '2022-07-01', 170.00, -1, NULL, 1, '2022-07-01', NULL),
       (-35, '2022-08-01', 196.10, 1, NULL, 1, '2022-08-01', NULL),
       (-36, '2022-08-01', 188.00, -4, NULL, 1, '2022-08-01', NULL),
       (-37, '2022-08-01', 181.00, -3, NULL, 1, '2022-08-01', NULL),
       (-38, '2022-08-01', 220.00, -2, NULL, 1, '2022-08-01', NULL),
       (-39, '2022-08-01', 180.00, -1, NULL, 1, '2022-08-01', NULL),
       (-40, '2022-09-01', 206.10, 1, NULL, 1, '2022-09-01', NULL),
       (-41, '2022-09-01', 198.00, -4, NULL, 1, '2022-09-01', NULL),
       (-42, '2022-09-01', 191.00, -3, NULL, 1, '2022-09-01', NULL),
       (-43, '2022-09-01', 230.00, -2, NULL, 1, '2022-09-01', NULL),
       (-44, '2022-09-01', 190.00, -1, NULL, 1, '2022-09-01', NULL),
       (-45, '2022-10-01', 216.10, 1, NULL, 1, '2022-10-01', NULL),
       (-46, '2022-10-01', 208.00, -4, NULL, 1, '2022-10-01', NULL),
       (-47, '2022-10-01', 201.00, -3, NULL, 1, '2022-10-01', NULL),
       (-48, '2022-10-01', 240.00, -2, NULL, 1, '2022-10-01', NULL),
       (-49, '2022-10-01', 200.00, -1, NULL, 1, '2022-10-01', NULL),
       (-50, '2022-11-01', 226.10, 1, NULL, 1, '2022-11-01', NULL),
       (-51, '2022-11-01', 218.00, -4, NULL, 1, '2022-11-01', NULL),
       (-52, '2022-11-01', 211.00, -3, NULL, 1, '2022-11-01', NULL),
       (-53, '2022-11-01', 250.00, -2, NULL, 1, '2022-11-01', NULL),
       (-54, '2022-11-01', 210.00, -1, NULL, 1, '2022-11-01', NULL),
       (-55, '2022-12-01', 236.10, 1, NULL, 1, '2022-12-01', NULL),
       (-56, '2022-12-01', 228.00, -4, NULL, 1, '2022-12-01', NULL),
       (-57, '2022-12-01', 221.10, -3, NULL, 1, '2022-12-01', NULL),
       (-58, '2022-12-01', 260.00, -2, NULL, 1, '2022-12-01', NULL),
       (-59, '2022-12-01', 220.00, -1, NULL, 1, '2022-12-01', NULL),
       (-60, '2023-01-01', 246.10, 1, NULL, 1, '2023-01-01', NULL),
       (-61, '2023-01-01', 238.00, -4, NULL, 1, '2023-01-01', NULL),
       (-62, '2023-01-01', 231.00, -3, NULL, 1, '2023-01-01', NULL),
       (-63, '2023-01-01', 270.00, -2, NULL, 1, '2023-01-01', NULL),
       (-64, '2023-01-01', 230.00, -1, NULL, 1, '2023-01-01', NULL),
       (-65, '2023-02-01', 256.10, 1, NULL, 1, '2023-02-01', NULL),
       (-66, '2023-02-01', 248.00, -4, NULL, 1, '2023-02-01', NULL),
       (-67, '2023-02-01', 241.00, -3, NULL, 1, '2023-02-01', NULL),
       (-68, '2023-02-01', 280.00, -2, NULL, 1, '2023-02-01', NULL),
       (-69, '2023-02-01', 240.00, -1, NULL, 1, '2023-02-01', NULL),
       (-70, '2023-03-01', 266.10, 1, NULL, 1, '2023-03-01', NULL),
       (-71, '2023-03-01', 258.00, -4, NULL, 1, '2023-03-01', NULL),
       (-72, '2023-03-01', 251.00, -3, NULL, 1, '2023-03-01', NULL),
       (-73, '2023-03-01', 290.00, -2, NULL, 1, '2023-03-01', NULL),
       (-74, '2023-03-01', 250.00, -1, NULL, 1, '2023-03-01', NULL),
       (-75, '2023-04-01', 276.10, 1, NULL, 1, '2023-04-01', NULL),
       (-76, '2023-04-01', 268.00, -4, NULL, 1, '2023-04-01', NULL),
       (-77, '2023-04-01', 261.00, -3, NULL, 1, '2023-04-01', NULL),
       (-78, '2023-04-01', 300.00, -2, NULL, 1, '2023-04-01', NULL),
       (-79, '2023-04-01', 260.00, -1, NULL, 1, '2023-04-01', NULL),
       (-80, '2023-05-01', 286.10, 1, NULL, 1, '2023-05-01', NULL),
       (-81, '2023-05-01', 278.00, -4, NULL, 1, '2023-05-01', NULL),
       (-82, '2023-05-01', 271.00, -3, NULL, 1, '2023-05-01', NULL),
       (-83, '2023-05-01', 310.00, -2, NULL, 1, '2023-05-01', NULL),
       (-84, '2023-05-01', 270.00, -1, NULL, 1, '2023-05-01', NULL),
       (-85, '2023-06-01', 296.10, 1, NULL, 1, '2023-06-01', NULL),
       (-86, '2023-06-01', 288.00, -4, NULL, 1, '2023-06-01', NULL),
       (-87, '2023-06-01', 281.00, -3, NULL, 1, '2023-06-01', NULL),
       (-88, '2023-06-01', 320.00, -2, NULL, 1, '2023-06-01', NULL),
       (-89, '2023-06-01', 280.00, -1, NULL, 1, '2023-06-01', NULL);
INSERT INTO annual_balance (id, year_, total_hot_water_advance, total_heating_place_advance,
                            total_heating_communal_area_advance, total_hot_water_cost, total_heating_place_cost,
                            total_heating_communal_area_cost, place_id, version, creation_date_time,
                            last_modification_date_time)
VALUES (1, 2022, 3636.06, 438.65, 1141.83, 3443.05, 415.37, 1081.22, 1, 1, '2022-01-02 01:31:30.7666',
        '2023-01-02 00:00:30.7666'),
       (-1, 2022, 3636.06, 381.87, 3298.62, 3443.05, 361.60, 3123.52, -4, 1, '2022-01-02 01:31:30.7666',
        '2023-01-02 00:00:30.7666'),
       (-2, 2022, 3636.06, 461.76, 3298.62, 3443.05, 437.24, 3123.52, -3, 1, '2022-01-02 01:31:30.7666',
        '2023-01-02 00:00:30.7666'),
       (-3, 2022, 3636.06, 298.21, 3298.62, 3443.05, 282.38, 3123.52, -2, 1, '2022-01-02 01:31:30.7666',
        '2023-01-02 00:00:30.7666'),
       (-4, 2022, 3636.06, 627.05, 1141.83, 3443.05, 593.77, 1081.22, -1, 1, '2022-01-02 01:31:30.7666',
        '2023-01-02 00:00:30.7666'),
       (-5, 2023, 1984.54, 239.41, 623.20, 1792.35, 242.72, 631.81, 1, 1, '2023-01-02 01:31:30.7666',
        '2023-06-02 00:00:30.7666'),
       (-6, 2023, 1984.54, 208.42, 1800.36, 1792.35, 211.30, 1825.23, -4, 1, '2023-01-02 01:31:30.7666',
        '2023-06-02 00:00:30.7666'),
       (-7, 2023, 1984.54, 252.02, 1800.36, 1792.35, 255.50, 1825.23, -3, 1, '2023-01-02 01:31:30.7666',
        '2023-06-02 00:00:30.7666'),
       (-8, 2023, 1984.54, 162.76, 1800.36, 1792.35, 165.01, 1825.23, -2, 1, '2023-01-02 01:31:30.7666',
        '2023-06-02 00:00:30.7666'),
       (-9, 2023, 1984.54, 342.24, 623.20, 1792.35, 346.97, 631.81, -1, 1, '2023-01-02 01:31:30.7666',
        '2023-06-02 00:00:30.7666');
INSERT INTO month_pay_off (id, payoff_date, water_heating_unit_cost, central_heating_unit_cost, hot_water_consumption,
                           owner_id, place_id, version, creation_date_time, last_modification_date_time)
VALUES (1, '2022-01-01', 29.7, 4.32, 10, -2, 1, 1, '2022-01-01 01:31:30.7666', '2022-01-01 01:31:30.7666'),
       (-1, '2022-01-01', 29.7, 4.32, 10, -3, -4, 1, '2022-01-01 01:31:30.7666', '2022-01-01 01:31:30.7666'),
       (-2, '2022-01-01', 29.7, 4.32, 10, -2, -3, 1, '2022-01-01 01:31:30.7666', '2022-01-01 01:31:30.7666'),
       (-3, '2022-01-01', 29.7, 4.32, 10, -4, -3, 1, '2022-01-01 01:31:30.7666', '2022-01-01 01:31:30.7666'),
       (-4, '2022-01-01', 29.7, 4.32, 10, -3, -2, 1, '2022-01-01 01:31:30.7666', '2022-01-01 01:31:30.7666'),
       (-5, '2022-02-01', 28.80, 3.74, 10, -2, 1, 1, '2022-02-01 01:31:30.7666', '2022-02-01 01:31:30.7666'),
       (-6, '2022-02-01', 28.80, 3.74, 10, -3, -4, 1, '2022-02-01 01:31:30.7666', '2022-02-01 01:31:30.7666'),
       (-7, '2022-02-01', 28.80, 3.74, 10, -2, -3, 1, '2022-02-01 01:31:30.7666', '2022-02-01 01:31:30.7666'),
       (-8, '2022-02-01', 28.80, 3.74, 10, -4, -3, 1, '2022-02-01 01:31:30.7666', '2022-02-01 01:31:30.7666'),
       (-9, '2022-02-01', 28.80, 3.74, 10, -3, -2, 1, '2022-02-01 01:31:30.7666', '2022-02-01 01:31:30.7666'),
       (-10, '2022-03-01', 28.12, 1.96, 10, -2, 1, 1, '2022-03-01 01:31:30.7666', '2022-03-01 01:31:30.7666'),
       (-11, '2022-03-01', 28.12, 1.96, 10, -3, -4, 1, '2022-03-01 01:31:30.7666', '2022-03-01 01:31:30.7666'),
       (-12, '2022-03-01', 28.12, 1.96, 10, -2, -3, 1, '2022-03-01 01:31:30.7666', '2022-03-01 01:31:30.7666'),
       (-13, '2022-03-01', 28.12, 1.96, 10, -4, -3, 1, '2022-03-01 01:31:30.7666', '2022-03-01 01:31:30.7666'),
       (-14, '2022-03-01', 28.12, 1.96, 10, -3, -2, 1, '2022-03-01 01:31:30.7666', '2022-03-01 01:31:30.7666'),
       (-15, '2022-04-01', 28.42, 0.84, 10, -2, 1, 1, '2022-04-01 01:31:30.7666', '2022-04-01 01:31:30.7666'),
       (-16, '2022-04-01', 28.42, 0.84, 10, -3, -4, 1, '2022-04-01 01:31:30.7666', '2022-04-01 01:31:30.7666'),
       (-17, '2022-04-01', 28.42, 0.84, 10, -2, -3, 1, '2022-04-01 01:31:30.7666', '2022-04-01 01:31:30.7666'),
       (-18, '2022-04-01', 28.42, 0.84, 10, -4, -3, 1, '2022-04-01 01:31:30.7666', '2022-04-01 01:31:30.7666'),
       (-19, '2022-04-01', 28.42, 0.84, 10, -3, -2, 1, '2022-04-01 01:31:30.7666', '2022-04-01 01:31:30.7666'),
       (-20, '2022-05-01', 27.38, 0.37, 10, -2, 1, 1, '2022-05-01 01:31:30.7666', '2022-05-01 01:31:30.7666'),
       (-21, '2022-05-01', 27.38, 0.37, 10, -3, -4, 1, '2022-05-01 01:31:30.7666', '2022-05-01 01:31:30.7666'),
       (-22, '2022-05-01', 27.38, 0.37, 10, -2, -3, 1, '2022-05-01 01:31:30.7666', '2022-05-01 01:31:30.7666'),
       (-23, '2022-05-01', 27.38, 0.37, 10, -4, -3, 1, '2022-05-01 01:31:30.7666', '2022-05-01 01:31:30.7666'),
       (-24, '2022-05-01', 27.38, 0.37, 10, -3, -2, 1, '2022-05-01 01:31:30.7666', '2022-05-01 01:31:30.7666'),
       (-25, '2022-06-01', 27.52, 0.18, 10, -2, 1, 1, '2022-06-01 01:31:30.7666', '2022-06-01 01:31:30.7666'),
       (-26, '2022-06-01', 27.52, 0.18, 10, -3, -4, 1, '2022-06-01 01:31:30.7666', '2022-06-01 01:31:30.7666'),
       (-27, '2022-06-01', 27.52, 0.18, 10, -2, -3, 1, '2022-06-01 01:31:30.7666', '2022-06-01 01:31:30.7666'),
       (-28, '2022-06-01', 27.52, 0.18, 10, -4, -3, 1, '2022-06-01 01:31:30.7666', '2022-06-01 01:31:30.7666'),
       (-29, '2022-06-01', 27.52, 0.18, 10, -3, -2, 1, '2022-06-01 01:31:30.7666', '2022-06-01 01:31:30.7666'),
       (-30, '2022-07-01', 28.67, 0.08, 10, -2, 1, 1, '2022-07-01 01:31:30.7666', '2022-07-01 01:31:30.7666'),
       (-31, '2022-07-01', 28.67, 0.08, 10, -3, -4, 1, '2022-07-01 01:31:30.7666', '2022-07-01 01:31:30.7666'),
       (-32, '2022-07-01', 28.67, 0.08, 10, -2, -3, 1, '2022-07-01 01:31:30.7666', '2022-07-01 01:31:30.7666'),
       (-33, '2022-07-01', 28.67, 0.08, 10, -4, -3, 1, '2022-07-01 01:31:30.7666', '2022-07-01 01:31:30.7666'),
       (-34, '2022-07-01', 28.67, 0.08, 10, -3, -2, 1, '2022-07-01 01:31:30.7666', '2022-07-01 01:31:30.7666'),
       (-35, '2022-08-01', 28.50, 0.06, 10, -2, 1, 1, '2022-08-01 01:31:30.7666', '2022-08-01 01:31:30.7666'),
       (-36, '2022-08-01', 28.50, 0.06, 10, -3, -4, 1, '2022-08-01 01:31:30.7666', '2022-08-01 01:31:30.7666'),
       (-37, '2022-08-01', 28.50, 0.06, 10, -2, -3, 1, '2022-08-01 01:31:30.7666', '2022-08-01 01:31:30.7666'),
       (-38, '2022-08-01', 28.50, 0.06, 10, -4, -3, 1, '2022-08-01 01:31:30.7666', '2022-08-01 01:31:30.7666'),
       (-39, '2022-08-01', 28.50, 0.06, 10, -3, -2, 1, '2022-08-01 01:31:30.7666', '2022-08-01 01:31:30.7666'),
       (-40, '2022-09-01', 29.75, 0.22, 10, -2, 1, 1, '2022-09-01 01:31:30.7666', '2022-09-01 01:31:30.7666'),
       (-41, '2022-09-01', 29.75, 0.22, 10, -3, -4, 1, '2022-09-01 01:31:30.7666', '2022-09-01 01:31:30.7666'),
       (-42, '2022-09-01', 29.75, 0.22, 10, -2, -3, 1, '2022-09-01 01:31:30.7666', '2022-09-01 01:31:30.7666'),
       (-43, '2022-09-01', 29.75, 0.22, 10, -4, -3, 1, '2022-09-01 01:31:30.7666', '2022-09-01 01:31:30.7666'),
       (-44, '2022-09-01', 29.75, 0.22, 10, -3, -2, 1, '2022-09-01 01:31:30.7666', '2022-09-01 01:31:30.7666'),
       (-45, '2022-10-01', 28.80, 0.79, 10, -2, 1, 1, '2022-10-01 01:31:30.7666', '2022-10-01 01:31:30.7666'),
       (-46, '2022-10-01', 28.80, 0.79, 10, -3, -4, 1, '2022-10-01 01:31:30.7666', '2022-10-01 01:31:30.7666'),
       (-47, '2022-10-01', 28.80, 0.79, 10, -2, -3, 1, '2022-10-01 01:31:30.7666', '2022-10-01 01:31:30.7666'),
       (-48, '2022-10-01', 28.80, 0.79, 10, -4, -3, 1, '2022-10-01 01:31:30.7666', '2022-10-01 01:31:30.7666'),
       (-49, '2022-10-01', 28.80, 0.79, 10, -3, -2, 1, '2022-10-01 01:31:30.7666', '2022-10-01 01:31:30.7666'),
       (-50, '2022-11-01', 28.40, 1.75, 10, -2, 1, 1, '2022-11-01 01:31:30.7666', '2022-11-01 01:31:30.7666'),
       (-51, '2022-11-01', 28.40, 1.75, 10, -3, -4, 1, '2022-11-01 01:31:30.7666', '2022-11-01 01:31:30.7666'),
       (-52, '2022-11-01', 28.40, 1.75, 10, -2, -3, 1, '2022-11-01 01:31:30.7666', '2022-11-01 01:31:30.7666'),
       (-53, '2022-11-01', 28.40, 1.75, 10, -4, -3, 1, '2022-11-01 01:31:30.7666', '2022-11-01 01:31:30.7666'),
       (-54, '2022-11-01', 28.40, 1.75, 10, -3, -2, 1, '2022-11-01 01:31:30.7666', '2022-11-01 01:31:30.7666'),
       (-55, '2022-12-01', 30.25, 3.72, 10, -2, 1, 1, '2022-12-01 01:31:30.7666', '2022-12-01 01:31:30.7666'),
       (-56, '2022-12-01', 30.25, 3.72, 10, -3, -4, 1, '2022-12-01 01:31:30.7666', '2022-12-01 01:31:30.7666'),
       (-57, '2022-12-01', 30.25, 3.72, 10, -2, -3, 1, '2022-12-01 01:31:30.7666', '2022-12-01 01:31:30.7666'),
       (-58, '2022-12-01', 30.25, 3.72, 10, -4, -3, 1, '2022-12-01 01:31:30.7666', '2022-12-01 01:31:30.7666'),
       (-59, '2022-12-01', 30.25, 3.72, 10, -3, -2, 1, '2022-12-01 01:31:30.7666', '2022-12-01 01:31:30.7666'),
       (-60, '2023-01-01', 28.84, 4.73, 10, -2, 1, 1, '2023-01-01 01:31:30.7666', '2023-01-01 01:31:30.7666'),
       (-61, '2023-01-01', 28.84, 4.73, 10, -3, -4, 1, '2023-01-01 01:31:30.7666', '2023-01-01 01:31:30.7666'),
       (-62, '2023-01-01', 28.84, 4.73, 10, -2, -3, 1, '2023-01-01 01:31:30.7666', '2023-01-01 01:31:30.7666'),
       (-63, '2023-01-01', 28.84, 4.73, 10, -4, -3, 1, '2023-01-01 01:31:30.7666', '2023-01-01 01:31:30.7666'),
       (-64, '2023-01-01', 28.84, 4.73, 10, -3, -2, 1, '2023-01-01 01:31:30.7666', '2023-01-01 01:31:30.7666'),
       (-65, '2023-02-01', 31.06, 2.83, 10, -2, 1, 1, '2023-02-01 01:31:30.7666', '2023-02-01 01:31:30.7666'),
       (-66, '2023-02-01', 31.06, 2.83, 10, -3, -4, 1, '2023-02-01 01:31:30.7666', '2023-02-01 01:31:30.7666'),
       (-67, '2023-02-01', 31.06, 2.83, 10, -2, -3, 1, '2023-02-01 01:31:30.7666', '2023-02-01 01:31:30.7666'),
       (-68, '2023-02-01', 31.06, 2.83, 10, -4, -3, 1, '2023-02-01 01:31:30.7666', '2023-02-01 01:31:30.7666'),
       (-69, '2023-02-01', 31.06, 2.83, 10, -3, -2, 1, '2023-02-01 01:31:30.7666', '2023-02-01 01:31:30.7666'),
       (-70, '2023-03-01', 30.90, 1.68, 10, -2, 1, 1, '2023-03-01 01:31:30.7666', '2023-03-01 01:31:30.7666'),
       (-71, '2023-03-01', 30.90, 1.68, 10, -3, -4, 1, '2023-03-01 01:31:30.7666', '2023-03-01 01:31:30.7666'),
       (-72, '2023-03-01', 30.90, 1.68, 10, -2, -3, 1, '2023-03-01 01:31:30.7666', '2023-03-01 01:31:30.7666'),
       (-73, '2023-03-01', 30.90, 1.68, 10, -4, -3, 1, '2023-03-01 01:31:30.7666', '2023-03-01 01:31:30.7666'),
       (-74, '2023-03-01', 30.90, 1.68, 10, -3, -2, 1, '2023-03-01 01:31:30.7666', '2023-03-01 01:31:30.7666'),
       (-75, '2023-04-01', 30.12, 0.69, 10, -2, 1, 1, '2023-04-01 01:31:30.7666', '2023-04-01 01:31:30.7666'),
       (-76, '2023-04-01', 30.12, 0.69, 10, -3, -4, 1, '2023-04-01 01:31:30.7666', '2023-04-01 01:31:30.7666'),
       (-77, '2023-04-01', 30.12, 0.69, 10, -2, -3, 1, '2023-04-01 01:31:30.7666', '2023-04-01 01:31:30.7666'),
       (-78, '2023-04-01', 30.12, 0.69, 10, -4, -3, 1, '2023-04-01 01:31:30.7666', '2023-04-01 01:31:30.7666'),
       (-79, '2023-04-01', 30.12, 0.69, 10, -3, -2, 1, '2023-04-01 01:31:30.7666', '2023-04-01 01:31:30.7666'),
       (-80, '2023-05-01', 29.60, 0.40, 10, -2, 1, 1, '2023-05-01 01:31:30.7666', '2023-05-01 01:31:30.7666'),
       (-81, '2023-05-01', 29.60, 0.40, 10, -3, -4, 1, '2023-05-01 01:31:30.7666', '2023-05-01 01:31:30.7666'),
       (-82, '2023-05-01', 29.60, 0.40, 10, -2, -3, 1, '2023-05-01 01:31:30.7666', '2023-05-01 01:31:30.7666'),
       (-83, '2023-05-01', 29.60, 0.40, 10, -4, -3, 1, '2023-05-01 01:31:30.7666', '2023-05-01 01:31:30.7666'),
       (-84, '2023-05-01', 29.60, 0.40, 10, -3, -2, 1, '2023-05-01 01:31:30.7666', '2023-05-01 01:31:30.7666'),
       (-85, '2023-06-01', 28.72, 0.19, 10, -2, 1, 1, '2023-06-01 01:31:30.7666', '2023-06-01 01:31:30.7666'),
       (-86, '2023-06-01', 28.72, 0.19, 10, -3, -4, 1, '2023-06-01 01:31:30.7666', '2023-06-01 01:31:30.7666'),
       (-87, '2023-06-01', 28.72, 0.19, 10, -2, -3, 1, '2023-06-01 01:31:30.7666', '2023-06-01 01:31:30.7666'),
       (-88, '2023-06-01', 28.72, 0.19, 10, -4, -3, 1, '2023-06-01 01:31:30.7666', '2023-06-01 01:31:30.7666'),
       (-89, '2023-06-01', 28.72, 0.19, 10, -3, -2, 1, '2023-06-01 01:31:30.7666', '2023-06-01 01:31:30.7666');
INSERT INTO heating_place_and_communal_area_advance (id, heating_place_advance_value,
                                                     heating_communal_area_advance_value, advance_change_factor)
VALUES (1, 35.65, 92.80, 1.03),
       (-1, 50.97, 92.80, 1.03),
       (-2, 24.24, 268.10, 1.03),
       (-3, 37.53, 268.10, 1.03),
       (-4, 31.04, 268.10, 1.03),
       (-5, 35.65, 92.80, 1.03),
       (-6, 50.97, 92.80, 1.03),
       (-7, 24.24, 268.10, 1.03),
       (-8, 37.53, 268.10, 1.03),
       (-9, 31.04, 268.10, 1.03),
       (-10, 35.65, 92.80, 1.03),
       (-11, 50.97, 92.80, 1.03),
       (-12, 24.24, 268.10, 1.03),
       (-13, 37.53, 268.10, 1.03),
       (-14, 31.04, 268.10, 1.03),
       (-15, 35.65, 92.80, 1),
       (-16, 50.97, 92.80, 1),
       (-17, 24.24, 268.10, 1),
       (-18, 37.53, 268.10, 1),
       (-19, 31.04, 268.10, 1),
       (-20, 35.65, 92.80, 1),
       (-21, 50.97, 92.80, 1),
       (-22, 24.24, 268.10, 1),
       (-23, 37.53, 268.10, 1),
       (-24, 31.04, 268.10, 1),
       (-25, 35.65, 92.80, 1),
       (-26, 50.97, 92.80, 1),
       (-27, 24.24, 268.10, 1),
       (-28, 37.53, 268.10, 1),
       (-29, 31.04, 268.10, 1),
       (-30, 36.37, 94.66, 1.02),
       (-31, 51.98, 94.66, 1.02),
       (-32, 24.72, 273.46, 1.02),
       (-33, 38.28, 273.46, 1.02),
       (-34, 31.66, 273.46, 1.02),
       (-35, 36.37, 94.66, 1.02),
       (-36, 51.98, 94.66, 1.02),
       (-37, 24.72, 273.46, 1.02),
       (-38, 38.28, 273.46, 1.02),
       (-39, 31.66, 273.46, 1.02),
       (-40, 36.37, 94.66, 1.02),
       (-41, 51.98, 94.66, 1.02),
       (-42, 24.72, 273.46, 1.02),
       (-43, 38.28, 273.46, 1.02),
       (-44, 31.66, 273.46, 1.02),
       (-45, 38.55, 104.34, 1.06),
       (-46, 55.10, 100.34, 1.06),
       (-47, 26.21, 289.87, 1.06),
       (-48, 40.58, 289.87, 1.06),
       (-49, 33.56, 289.87, 1.06),
       (-50, 38.55, 104.34, 1.06),
       (-51, 55.10, 100.34, 1.06),
       (-52, 26.21, 289.87, 1.06),
       (-53, 40.58, 289.87, 1.06),
       (-54, 33.56, 289.87, 1.06),
       (-55, 38.55, 104.34, 1.06),
       (-56, 55.10, 100.34, 1.06),
       (-57, 26.21, 289.87, 1.06),
       (-58, 40.58, 289.87, 1.06),
       (-59, 33.56, 289.87, 1.06),
       (-60, 39.7, 103.35, 1.03),
       (-61, 56.76, 103.35, 1.03),
       (-62, 26.99, 298.57, 1.03),
       (-63, 41.79, 298.57, 1.03),
       (-64, 34.56, 298.57, 1.03),
       (-65, 39.7, 103.35, 1.03),
       (-66, 56.76, 103.35, 1.03),
       (-67, 26.99, 298.57, 1.03),
       (-68, 41.79, 298.57, 1.03),
       (-69, 34.56, 298.57, 1.03),
       (-70, 39.7, 103.35, 1.03),
       (-71, 56.76, 103.35, 1.03),
       (-72, 26.99, 298.57, 1.03),
       (-73, 41.79, 298.57, 1.03),
       (-74, 34.56, 298.57, 1.03),
       (-75, 57.32, 104.38, 1.01),
       (-76, 57.32, 104.38, 1.01),
       (-77, 27.26, 301.55, 1.01),
       (-78, 42.21, 301.55, 1.01),
       (-79, 34.91, 301.55, 1.01),
       (-80, 57.32, 104.38, 1.01),
       (-81, 57.32, 104.38, 1.01),
       (-82, 27.26, 301.55, 1.01),
       (-83, 42.21, 301.55, 1.01),
       (-84, 34.91, 301.55, 1.01),
       (-85, 57.32, 104.38, 1.01),
       (-86, 57.32, 104.38, 1.01),
       (-87, 27.26, 301.55, 1.01),
       (-88, 42.21, 301.55, 1.01),
       (-89, 34.91, 301.55, 1.01);