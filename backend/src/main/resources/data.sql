INSERT INTO roles (id, role_type) VALUES
                                      (1, 'USER'),
                                      (2, 'ADMIN')
ON DUPLICATE KEY UPDATE id = id; -- No actual update, just to avoid errors on duplicate key

INSERT INTO users (id, expire_date, password, username, first_name, middle_name, second_name) VALUES
    (1, '2050-03-22', 'admin', 'admin', 'admin', 'admin', 'admin')
ON DUPLICATE KEY UPDATE id = id; -- No actual update, just to avoid errors on duplicate key

INSERT INTO user_roles (user_id, role_id) VALUES
    (1, 2)
ON DUPLICATE KEY UPDATE user_id = user_id; -- No actual update, just to avoid errors on duplicate key
