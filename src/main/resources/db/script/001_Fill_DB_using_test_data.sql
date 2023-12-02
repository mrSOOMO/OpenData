-- Insert into Account
--password: 'password1' 'password2' 'password3' 'password4' 'password5'
INSERT INTO account (login, hashed_password, info, access_level)
VALUES ('user1', '$2a$10$ERXk8vZYBW5wALquO1MJ..I29b6zbRCJJ0mO8Lkaj/ginC1LCP1o.', 'User One Info: User', 1),
       ('user2', '$2a$10$Zu/hpZ6L70/4cFcTXgnlsexwPEWoyII85dBx37rMqUHrF6L27Sx3i', 'User Two Info: AnimationDeveloper', 2),
       ('user3', '$2a$10$73DBNWRx/e5.6uoGrbTi8exdLLsdj3uWM9VlmA2KrJzh13FzSVpLe', 'User Three Info: Engineer', 3),
       ('user4', '$2a$10$t7dMi5gl7UbyLW./IrL4b.7s9F9u/uiAJMbf7sV847U5TnrlI7USK', 'User Four  Info: Manager', 4),
       ('user5', '$2a$10$OpE.NITw0uZe/SAFesk1COoGuZTPzcuxxuf55GhF6e91h/Hf2uq7O', 'User Five Info: Admin', 5);

-- Insert into AnimationDeveloper
INSERT INTO animation_developer (name, phone, info)
VALUES ('dev1', '111-111-1111', 'Developer One Info'),
       ('dev2', '222-222-2222', 'Developer Two Info'),
       ('dev3', '333-333-3333', 'Developer Three Info');

-- You might need to modify these inserts based on the actual IDs of the developers
-- Insert into Animation
INSERT INTO animation (name, author_id, info)
VALUES ('animation1', 1, 'Animation One Info'),
       ('animation2', 2, 'Animation Two Info'),
       ('animation3', 3, 'Animation Three Info');

-- You might need to modify these inserts based on the actual IDs of the animations
-- Insert into AnimationVersion
INSERT INTO animation_version (animation_id, version, parameter)
VALUES (1, 'v1.0', 'Parameter One'),
       (2, 'v1.1', 'Parameter Two'),
       (3, 'v1.2', 'Parameter Three');

-- You might need to modify these inserts based on the actual IDs of the animation versions
-- Insert into AnimationInstance
INSERT INTO animation_instance (animation_version_id)
VALUES (1),
       (2),
       (3);
