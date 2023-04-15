CREATE
OR REPLACE VIEW public.glassfish_auth_view AS
SELECT username, password, access_level
FROM account
         JOIN access_level_mapping
              ON account.id = access_level_mapping.account_id
WHERE account.is_active = true && account.is_enable = true;