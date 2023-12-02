-- Insert into Account
INSERT INTO account (id, login, hashed_password, info, access_level)
VALUES (1, 'user1', '$2a$10$ERXk8vZYBW5wALquO1MJ..I29b6zbRCJJ0mO8Lkaj/ginC1LCP1o.', 'User One Info: User', 1),
       (2, 'user2', '$2a$10$Zu/hpZ6L70/4cFcTXgnlsexwPEWoyII85dBx37rMqUHrF6L27Sx3i', 'User Two Info: AnimationDeveloper', 2),
       (3, 'user3', '$2a$10$73DBNWRx/e5.6uoGrbTi8exdLLsdj3uWM9VlmA2KrJzh13FzSVpLe', 'User Three Info: Engineer', 3),
       (4, 'user4', '$2a$10$t7dMi5gl7UbyLW./IrL4b.7s9F9u/uiAJMbf7sV847U5TnrlI7USK', 'User Four  Info: Manager', 4),
       (5, 'user5', '$2a$10$OpE.NITw0uZe/SAFesk1COoGuZTPzcuxxuf55GhF6e91h/Hf2uq7O', 'User Five Info: Admin', 5)
ON CONFLICT (id) DO UPDATE
    SET login = EXCLUDED.login, hashed_password = EXCLUDED.hashed_password, info = EXCLUDED.info, access_level = EXCLUDED.access_level;

-- Similar approach for other tables
-- Assuming 'name' as the unique key in the following tables

-- Insert into AnimationDeveloper
INSERT INTO animation_developer (id, name, phone, info)
VALUES (1, 'dev1', '111-111-1111', 'Developer One Info'),
       (2, 'dev2', '222-222-2222', 'Developer Two Info'),
       (3, 'dev3', '333-333-3333', 'Developer Three Info')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, phone = EXCLUDED.phone, info = EXCLUDED.info;


-- Insert into Animation
INSERT INTO animation (id, name, author_id, info)
VALUES (1, 'animation1', 1, 'Animation One Info'),
       (2, 'animation2', 2, 'Animation Two Info'),
       (3, 'animation3', 3, 'Animation Three Info')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, author_id = EXCLUDED.author_id, info = EXCLUDED.info;


-- Insert into AnimationVersion
INSERT INTO animation_version (id, animation_id, version, parameter)
VALUES (1, 1, 'v1.0', 'Parameter One'),
       (2, 2, 'v1.1', 'Parameter Two'),
       (3, 3, 'v1.2', 'Parameter Three')
ON CONFLICT (id) DO UPDATE
    SET animation_id = EXCLUDED.animation_id, version = EXCLUDED.version, parameter = EXCLUDED.parameter;


-- Insert into AnimationInstance
INSERT INTO animation_instance (id, animation_version_id)
VALUES (1, 1),
       (2, 2),
       (3, 3)
ON CONFLICT (id) DO UPDATE
    SET animation_version_id = EXCLUDED.animation_version_id;  -- As there's no other fields to update, we just DO NOTHING on conflict

