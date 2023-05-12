drop table IF EXISTS personal_data;
drop table IF EXISTS login_data;
drop table IF EXISTS annual_balance;
drop table IF EXISTS hot_water_advance;
drop table IF EXISTS heating_place_and_communal_area_advance;
drop table IF EXISTS hot_water_entry;
drop table IF EXISTS advance;
drop table IF EXISTS month_pay_off;
drop table IF EXISTS place;
drop table IF EXISTS owner;
drop table IF EXISTS admin;
drop table IF EXISTS heat_distribution_centre_pay_off;
drop table IF EXISTS manager;
drop table IF EXISTS access_level_mapping;
drop table IF EXISTS account;
drop table IF EXISTS past_quarter_hot_water_pay_off;
drop table IF EXISTS building;
drop table IF EXISTS address;
drop table IF EXISTS heat_distribution_centre;

create table address (
    id BIGINT PRIMARY KEY,
    street VARCHAR(32) NOT NULL,
    building_number SMALLINT NOT NULL,
    city VARCHAR(32) NOT NULL,
    postal_code VARCHAR(6) NOT NULL CHECK (postal_code ~* '^\d{2}-\d{3}$'),
    version BIGINT NOT NULL
);

create table past_quarter_hot_water_pay_off (
    id BIGINT PRIMARY KEY,
    average_consumption DECIMAL(10,2) NOT NULL CHECK (average_consumption >= 0),
    days_number_in_quarter SMALLINT NOT NULL CHECK (days_number_in_quarter >= 90 AND days_number_in_quarter <= 92),
    version BIGINT NOT NULL
);

create table account (
    id BIGINT PRIMARY KEY,
    email VARCHAR(254) NOT NULL CHECK (email ~* '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{1,10}$'),
    constraint unique_email UNIQUE(email),
    username VARCHAR(16) NOT NULL CHECK (username ~* '^[a-zA-Z0-9_]{6,}$'),
    constraint unique_username UNIQUE(username),
    password VARCHAR(60) NOT NULL,
    is_enable BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT FALSE,
    register_date TIMESTAMP NOT NULL,
    language_ VARCHAR NOT NULL DEFAULT 'PL',
    version BIGINT NOT NULL
);

create table personal_data (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES account(id),
    first_name VARCHAR(32) NOT NULL,
    surname VARCHAR(32) NOT NULL,
    version BIGINT NOT NULL
);

create table login_data (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES account(id),
    last_valid_login_date TIMESTAMP,
    last_valid_logic_address VARCHAR(15) CHECK (last_valid_logic_address ~* '^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$'),
    last_invalid_login_date TIMESTAMP,
    last_invalid_logic_address VARCHAR(15) CHECK (last_invalid_logic_address ~* '^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$'),
    invalid_login_counter SMALLINT CHECK (invalid_login_counter >= 0 AND invalid_login_counter <=3),
    version BIGINT NOT NULL
);

create table access_level_mapping (
    id BIGINT PRIMARY KEY,
    access_level VARCHAR(7) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    account_id BIGINT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account(id),
    version BIGINT NOT NULL
);

CREATE INDEX access_level_mapping_account_id ON access_level_mapping (account_id);

create table admin (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES access_level_mapping(id),
    version BIGINT NOT NULL
);

create table manager (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES access_level_mapping(id),
    license VARCHAR(20) NOT NULL,
    constraint unique_license UNIQUE(license),
    version BIGINT NOT NULL
);

create table owner (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES access_level_mapping(id),
    phone_number VARCHAR(9) NOT NULL CHECK(phone_number ~ '^\d{9}$'),
    constraint unique_phone_number UNIQUE(phone_number),
    version BIGINT NOT NULL
);

create table heat_distribution_centre (
    id BIGINT PRIMARY KEY,
    version BIGINT NOT NULL
);

CREATE TABLE building (
    id BIGINT PRIMARY KEY,
    total_area DECIMAL(10,2) NOT NULL CHECK (total_area >= 0),
    communal_area_aggregate DECIMAL(10,2) NOT NULL CHECK (communal_area_aggregate >= 0),
    address_id BIGINT NOT NULL,
    FOREIGN KEY (address_id) REFERENCES address(id),
    heat_distribution_centre_id BIGINT NOT NULL,
    FOREIGN KEY (heat_distribution_centre_id) REFERENCES heat_distribution_centre(id),
    version BIGINT NOT NULL
);

CREATE INDEX building_address_id ON building (address_id);

CREATE INDEX building_heat_distribution_centre_id ON building (heat_distribution_centre_id);

CREATE TABLE place (
    id BIGINT PRIMARY KEY,
    area DECIMAL(10,2) NOT NULL CHECK (area >= 0),
    hot_water_connection BOOLEAN NOT NULL,
    central_heating_connection BOOLEAN NOT NULL,
    predicted_hot_water_consumption DECIMAL(10, 2) CHECK (predicted_hot_water_consumption >= 0),
    building_id BIGINT NOT NULL,
    FOREIGN KEY (building_id) REFERENCES building(id),
    past_quarter_hot_water_payoff_id BIGINT NOT NULL,
    FOREIGN KEY (past_quarter_hot_water_payoff_id) REFERENCES past_quarter_hot_water_pay_off(id),
    owner_id BIGINT NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES owner(id),
    version BIGINT NOT NULL
);

CREATE INDEX place_building_id ON place (building_id);

CREATE INDEX place_owner_id ON place (owner_id);

create table annual_balance (
    id BIGINT PRIMARY KEY,
    year_ SMALLINT NOT NULL CHECK (year_ >= 2021),
    total_hot_water_advance DECIMAL(10,2) NOT NULL CHECK (total_hot_water_advance >= 0),
    total_heating_place_advance DECIMAL(10,2) NOT NULL CHECK (total_heating_place_advance >= 0),
    total_heating_communal_area_advance DECIMAL(10,2) NOT NULL CHECK (total_heating_communal_area_advance >= 0),
    total_hot_water_cost DECIMAL(10,2) NOT NULL CHECK (total_hot_water_cost >= 0),
    total_heating_place_cost DECIMAL(10,2) NOT NULL CHECK (total_heating_place_cost >= 0),
    total_heating_communal_area_cost DECIMAL(10,2) NOT NULL CHECK (total_heating_communal_area_cost >= 0),
    place_id BIGINT NOT NULL,
    FOREIGN KEY (place_id) REFERENCES place(id),
    version BIGINT NOT NULL
);

CREATE INDEX annual_balance_place_id ON annual_balance (place_id);

create table month_pay_off (
    id BIGINT PRIMARY KEY,
    payoff_date DATE NOT NULL,
    water_heating_unit_cost DECIMAL(10,2) NOT NULL CHECK (water_heating_unit_cost >= 0),
    central_heating_unit_cost DECIMAL(10,2) NOT NULL CHECK (central_heating_unit_cost >= 0),
    hot_water_consumption DECIMAL(10,2) NOT NULL CHECK (hot_water_consumption >= 0),
    owner_id BIGINT NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES owner(id),
    place_id BIGINT NOT NULL,
    FOREIGN KEY (place_id) REFERENCES place(id),
    version BIGINT NOT NULL
);

CREATE INDEX month_pay_off_place_id ON month_pay_off (place_id);

CREATE INDEX month_pay_off_owner_id ON month_pay_off (owner_id);

create table advance (
    id BIGINT PRIMARY KEY,
    date_ DATE NOT NULL,
    place_id BIGINT NOT NULL,
    FOREIGN KEY (place_id) REFERENCES place(id),
    version BIGINT NOT NULL
);

CREATE INDEX advance_place_id ON advance (place_id);

create table hot_water_advance (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES advance(id),
    hot_water_advance_value DECIMAL(10,2) NOT NULL CHECK (hot_water_advance_value >= 0),
    version BIGINT NOT NULL
);

create table heating_place_and_communal_area_advance (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES advance(id),
    heating_place_advance_value DECIMAL(10,2) NOT NULL CHECK (heating_place_advance_value >= 0),
    heating_communal_area_advance_value DECIMAL(10,2) NOT NULL CHECK (heating_communal_area_advance_value >= 0),
    advance_change_factor DECIMAL(3,2) NOT NULL CHECK (advance_change_factor >= 0 AND advance_change_factor <= 9),
    version BIGINT NOT NULL
);

create table heat_distribution_centre_pay_off (
    id BIGINT PRIMARY KEY,
    date_ DATE NOT NULL,
    consumption DECIMAL(10,2) NOT NULL CHECK (consumption >= 0),
    consumption_cost DECIMAL(10,2) NOT NULL CHECK (consumption_cost >= 0),
    heating_area_factor DECIMAL(3, 2) NOT NULL CHECK (heating_area_factor > 0 AND heating_area_factor < 1),
    heat_distribution_centre_id BIGINT NOT NULL,
    FOREIGN KEY (heat_distribution_centre_id) REFERENCES heat_distribution_centre(id),
    manager_id BIGINT NOT NULL,
    FOREIGN KEY (manager_id) REFERENCES manager(id),
    version BIGINT NOT NULL
);

CREATE INDEX heat_distribution_centre_pay_off_heat_distribution_centre_id ON heat_distribution_centre_pay_off (heat_distribution_centre_id);

CREATE INDEX heat_distribution_centre_pay_off_manager_id ON heat_distribution_centre_pay_off (manager_id);

create table hot_water_entry (
    id BIGINT PRIMARY KEY,
    date_ DATE NOT NULL ,
    entry_value DECIMAL(10,2) NOT NULL CHECK (entry_value >= 0),
    place_id BIGINT NOT NULL,
    FOREIGN KEY (place_id) REFERENCES place(id),
    manager_id BIGINT,
    FOREIGN KEY (manager_id) REFERENCES manager(id),
    version BIGINT NOT NULL
);

CREATE INDEX hot_water_entry_place_id ON hot_water_entry (place_id);

CREATE INDEX hot_water_entry_manager_id ON hot_water_entry (manager_id);

