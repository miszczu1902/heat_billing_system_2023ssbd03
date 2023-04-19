CREATE OR REPLACE VIEW public.glassfish_auth_view AS SELECT username, password, access_level FROM account JOIN access_level_mapping ON account.id = access_level_mapping.account_id WHERE account.is_active = true AND account.is_enable = true;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE glassfish_auth_view TO ssbd03auth;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE access_level_mapping TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE account TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE owner TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE admin TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE manager TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE personal_data TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE login_data TO ssbd03mok;

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

GRANT SELECT,UPDATE ON SEQUENCE address_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE advance_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE annual_balance_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE building_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE heat_distribution_centre_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE heat_distribution_centre_pay_off_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE hot_water_entry_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE month_pay_off_id_seq TO ssbd03mow;
GRANT SELECT,UPDATE ON SEQUENCE place_id_seq TO ssbd03mow;