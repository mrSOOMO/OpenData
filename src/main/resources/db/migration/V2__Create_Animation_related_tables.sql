-- Table: AnimationDeveloper
CREATE TABLE IF NOT EXISTS animation_developer (
                                                   id SERIAL PRIMARY KEY,
                                                   name VARCHAR(255) DEFAULT '',
                                                   phone VARCHAR(255) DEFAULT '',
                                                   info TEXT DEFAULT ''
);

-- Table: Animation
CREATE TABLE IF NOT EXISTS animation (
                                         id SERIAL PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
                                         author_id INT NOT NULL REFERENCES animation_developer(id),
                                         info TEXT DEFAULT ''
);

-- Table: AnimationVersion
CREATE TABLE IF NOT EXISTS animation_version (
                                                 id SERIAL PRIMARY KEY,
                                                 animation_id INT NOT NULL,
                                                 version VARCHAR(255) NOT NULL,
                                                 parameter TEXT DEFAULT '',
                                                 FOREIGN KEY (animation_id) REFERENCES animation(id)
);

-- Table: AnimationInstance
CREATE TABLE IF NOT EXISTS animation_instance (
                                                  id SERIAL PRIMARY KEY,
                                                  animation_version_id INT NOT NULL,
                                                  FOREIGN KEY (animation_version_id) REFERENCES animation_version(id)
);