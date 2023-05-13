CREATE OR REPLACE VIEW public.glassfish_auth_view AS SELECT username, password, access_level FROM account JOIN access_level_mapping ON account.id = access_level_mapping.account_id WHERE account.is_active = true AND account.is_enable = true AND access_level_mapping.is_active = true;

GRANT SELECT ON TABLE glassfish_auth_view TO ssbd03auth;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE access_level_mapping TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE account TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE owner TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE admin TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE manager TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE personal_data TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE login_data TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE account_confirmation_token TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE email_confirmation_token TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE reset_password_token TO ssbd03mok;

GRANT SELECT ON TABLE access_level_mapping TO ssbd03mow;
GRANT SELECT ON TABLE account TO ssbd03mow;
GRANT SELECT ON TABLE owner TO ssbd03mow;
GRANT SELECT ON TABLE admin TO ssbd03mow;
GRANT SELECT ON TABLE manager TO ssbd03mow;
GRANT SELECT ON TABLE personal_data TO ssbd03mow;
GRANT SELECT ON TABLE login_data TO ssbd03mow;
GRANT SELECT,INSERT,UPDATE ON TABLE building TO ssbd03mow;
GRANT SELECT,INSERT ON TABLE address TO ssbd03mow;
GRANT SELECT,INSERT,UPDATE ON TABLE annual_balance TO ssbd03mow;
GRANT SELECT,INSERT,UPDATE ON TABLE heat_distribution_centre TO ssbd03mow;
GRANT SELECT,INSERT ON TABLE heat_distribution_centre_pay_off TO ssbd03mow;
GRANT SELECT,INSERT,UPDATE ON TABLE hot_water_entry TO ssbd03mow;
GRANT SELECT,INSERT ON TABLE month_pay_off TO ssbd03mow;
GRANT SELECT,INSERT,UPDATE ON TABLE past_quarter_hot_water_pay_off TO ssbd03mow;
GRANT SELECT,INSERT,UPDATE ON TABLE place TO ssbd03mow;
GRANT SELECT,INSERT,UPDATE ON TABLE advance TO ssbd03mow;
GRANT SELECT,INSERT,UPDATE ON TABLE hot_water_advance TO ssbd03mow;
GRANT SELECT,INSERT,UPDATE ON TABLE heating_place_and_communal_area_advance TO ssbd03mow;

GRANT SELECT,UPDATE ON SEQUENCE access_level_mapping_id_seq TO ssbd03mok;
GRANT SELECT,UPDATE ON SEQUENCE account_id_seq TO ssbd03mok;
GRANT SELECT,UPDATE ON SEQUENCE reset_password_token_id_seq TO ssbd03mok;
GRANT SELECT,UPDATE ON SEQUENCE account_confirmation_token_id_seq TO ssbd03mok;
GRANT SELECT,UPDATE ON SEQUENCE email_confirmation_token_id_seq TO ssbd03mok;

GRANT SELECT,UPDATE ON SEQUENCE address_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE advance_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE annual_balance_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE building_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE heat_distribution_centre_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE heat_distribution_centre_pay_off_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE hot_water_entry_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE month_pay_off_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE place_id_seq TO ssbd03mow;

INSERT INTO address (id, street, building_number, city, postal_code, version, creation_date_time, last_modification_date_time) VALUES (0, 'Main Street', 12, 'New York', '12-345', 1, NOW(), NOW()), (-1, 'Abbey Road', 7, 'London', '34-123', 1, NOW(), NOW()), (-2, 'Champs-Élysées', 15, 'Paris', '12-345', 1, NOW(), NOW());

INSERT INTO account (id, email, username, password, is_enable, is_active, register_date, language_, version, creation_date_time, last_modification_date_time) VALUES (0, 'johndoe@example.com', 'johndoe', '$2a$10$JgIl/CXyYZtLFXMfYJ34Neh5JYlS/.Um5KWArokqP.rZHr085sAzq', TRUE, TRUE, NOW(), 'EN', 1, NOW(), NOW()), (-1, 'janekowalski@example.com', 'janekowalski', '$2a$10$JgIl/CXyYZtLFXMfYJ34Neh5JYlS/.Um5KWArokqP.rZHr085sAzq', TRUE, TRUE, NOW(), 'PL', 1, NOW(), NOW()), (-2, 'maria.silva@example.com', 'mariasilva', '$2a$10$JgIl/CXyYZtLFXMfYJ34Neh5JYlS/.Um5KWArokqP.rZHr085sAzq', TRUE, TRUE, NOW(), 'PL', 1, NOW(), NOW());

INSERT INTO personal_data (id, first_name, surname, version, creation_date_time, last_modification_date_time) VALUES (0, 'John', 'Doe', 1, NOW(), NOW()), (-1, 'Jan', 'Kowalski', 1, NOW(), NOW()), (-2, 'Maria', 'Silva', 1, NOW(), NOW());

INSERT INTO login_data (id, last_valid_login_date, last_valid_logic_address, last_invalid_login_date, last_invalid_logic_address, invalid_login_counter, version, creation_date_time, last_modification_date_time) VALUES (0, NOW() - INTERVAL '1 day', '192.168.0.1', NOW() - INTERVAL '3 days', '192.168.0.2', 2, 1, NOW(), NOW()), (-1, NOW() - INTERVAL '5 days', '10.0.0.1', NOW() - INTERVAL '8 days', '10.0.0.2', 2, 1, NOW(), NOW()), (-2, NOW() - INTERVAL '2 days', '192.168.1.1', NOW() - INTERVAL '4 days', '192.168.1.2', 0, 1, NOW(), NOW());

INSERT INTO access_level_mapping (id, access_level, is_active, account_id, version, creation_date_time, last_modification_date_time) VALUES (0, 'ADMIN', true, 0, 1, NOW(), NOW()), (-1, 'MANAGER', true, -1, 1, NOW(), NOW()), (-2, 'OWNER', true, -2, 1, NOW(), NOW());

INSERT INTO admin (id) VALUES (0);

INSERT INTO manager (id, license) VALUES (-1, '12345678912345678912');

INSERT INTO owner (id, phone_number) VALUES (-2, '123456789');

INSERT INTO heat_distribution_centre (id, version, creation_date_time, last_modification_date_time) VALUES (0, 1, NOW(), NOW());

INSERT INTO heat_distribution_centre_pay_off (id, date_, consumption, consumption_cost, heating_area_factor, heat_distribution_centre_id, manager_id, version, creation_date_time, last_modification_date_time) VALUES (0, '2022-01-01', 500.00, 2500.00, 0.75, 0, -1, 1, NOW(), NOW());

INSERT INTO building (id, total_area, communal_area_aggregate, address_id, heat_distribution_centre_id, version, creation_date_time, last_modification_date_time) VALUES (0, 200.00, 50.00, 0, 0, 1, NOW(), NOW());

INSERT INTO place (id, area, hot_water_connection, central_heating_connection, predicted_hot_water_consumption, building_id, owner_id, version, creation_date_time, last_modification_date_time) VALUES (0, 50.25, TRUE, TRUE, 15.55, 0, -2, 1, NOW(), NOW());

INSERT INTO past_quarter_hot_water_pay_off (id, average_consumption, days_number_in_quarter, version, creation_date_time, last_modification_date_time) VALUES (0, 12.34, 90, 1, NOW(), NOW());

INSERT INTO advance (id, date_, place_id, version, creation_date_time, last_modification_date_time) VALUES (0, '2023-01-01', 0, 1, NOW(), NOW()), (-1, '2021-01-01', 0, 1, NOW(), NOW()),(-2, '2022-01-01', 0, 1, NOW(), NOW());

INSERT INTO hot_water_advance (id, hot_water_advance_value) VALUES (0, 1000.00), (-1, 800.00), (-2, 1500.00);

INSERT INTO hot_water_entry (id, date_, entry_value, place_id, manager_id, version, creation_date_time, last_modification_date_time) VALUES (0, '2022-01-01', 150.00, 0, -1, 1, NOW(), NOW());

INSERT INTO annual_balance (id, year_, total_hot_water_advance, total_heating_place_advance, total_heating_communal_area_advance, total_hot_water_cost, total_heating_place_cost, total_heating_communal_area_cost, place_id, version, creation_date_time, last_modification_date_time) VALUES (0, 2023, 3000.00, 2500.00, 1500.00, 2800.00, 1800.00, 1100.00, 0, 1, NOW(), NOW());

INSERT INTO month_pay_off (id, payoff_date, water_heating_unit_cost, central_heating_unit_cost, hot_water_consumption, owner_id, place_id, version, creation_date_time, last_modification_date_time)VALUES (0, '2022-03-01', 10.50, 30.00, 12.30, -2, 0, 1, NOW(), NOW()),(-1, '2021-03-01', 12.20, 28.50, 18.20, -2, 0, 1, NOW(), NOW()),(-2, '2023-03-01', 9.80, 31.20, 15.60, -2, 0, 1, NOW(), NOW());

INSERT INTO heating_place_and_communal_area_advance (id, heating_place_advance_value, heating_communal_area_advance_value, advance_change_factor) VALUES (0, 1200.00, 800.00, 0.9), (-1, 900.00, 600.00, 0.8), (-2, 1800.00, 1200.00, 0.85);