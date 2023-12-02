--As a recap, it will create:

--logical_led_part table to store LogicalLedPart instances.
--logical_led_part_led junction table to maintain many-to-many relationships between LogicalLedPart and Led.
--logical_led table for LogicalLed instances.
--logical_led_part_logical_led junction table to maintain many-to-many relationships between LogicalLed and LogicalLedPart.
--led_group table for LedGroup instances.
--led_group_logical_led junction table to maintain many-to-many relationships between LedGroup and LogicalLed.
--logical_representation table for LogicalRepresentation instances.
--logical_representation_led_group junction table to maintain many-to-many relationships between LogicalRepresentation and LedGroup.
--It assumes that the physical_representation and led tables have already been created.

-- Table: LogicalLedPart
CREATE TABLE IF NOT EXISTS logical_led_part (
                                                id SERIAL PRIMARY KEY,
                                                serial INT NOT NULL
);

-- Table: LogicalLedPartLed
CREATE TABLE IF NOT EXISTS logical_led_part_led (
                                                    logical_led_part_id INT NOT NULL,
                                                    led_id INT NOT NULL,
                                                    PRIMARY KEY (logical_led_part_id, led_id),
                                                    FOREIGN KEY (logical_led_part_id) REFERENCES logical_led_part(id),
                                                    FOREIGN KEY (led_id) REFERENCES led(id)
);

-- Table: LogicalLed
CREATE TABLE IF NOT EXISTS logical_led (
                                           id SERIAL PRIMARY KEY,
                                           serial INT NOT NULL,
                                           cardinal_point VARCHAR(255) NOT NULL
);

-- Table: LogicalLedPartLogicalLed
CREATE TABLE IF NOT EXISTS logical_led_part_logical_led (
                                                            logical_led_id INT NOT NULL,
                                                            logical_led_part_id INT NOT NULL,
                                                            PRIMARY KEY (logical_led_id, logical_led_part_id),
                                                            FOREIGN KEY (logical_led_id) REFERENCES logical_led(id),
                                                            FOREIGN KEY (logical_led_part_id) REFERENCES logical_led_part(id)
);

-- Table: LedGroup
CREATE TABLE IF NOT EXISTS led_group (
                                         id SERIAL PRIMARY KEY,
                                         group_name VARCHAR(255) NOT NULL
);

-- Table: LedGroupLogicalLed
CREATE TABLE IF NOT EXISTS led_group_logical_led (
                                                     led_group_id INT NOT NULL,
                                                     logical_led_id INT NOT NULL,
                                                     PRIMARY KEY (led_group_id, logical_led_id),
                                                     FOREIGN KEY (led_group_id) REFERENCES led_group(id),
                                                     FOREIGN KEY (logical_led_id) REFERENCES logical_led(id)
);
-- Table: LogicalRepresentation
CREATE TABLE IF NOT EXISTS logical_representation (
                                                      id SERIAL PRIMARY KEY,
                                                      serial INT NOT NULL,
                                                      parameters TEXT,
                                                      physical_representation_id INT,
                                                      FOREIGN KEY (physical_representation_id) REFERENCES physical_representation(id)
);

-- Table: LogicalRepresentationLedGroup
CREATE TABLE IF NOT EXISTS logical_representation_led_group (
                                                                logical_representation_id INT NOT NULL,
                                                                led_group_id INT NOT NULL,
                                                                PRIMARY KEY (logical_representation_id, led_group_id),
                                                                FOREIGN KEY (logical_representation_id) REFERENCES logical_representation(id),
                                                                FOREIGN KEY (led_group_id) REFERENCES led_group(id)
);

CREATE TABLE IF NOT EXISTS location (
                                        id SERIAL PRIMARY KEY,
                                        location_name VARCHAR(255) NOT NULL,
                                        parameters TEXT,
                                        physical_representation_id INT,
                                        logical_representation_id INT,
                                        FOREIGN KEY (physical_representation_id) REFERENCES physical_representation(id),
                                        FOREIGN KEY (logical_representation_id) REFERENCES logical_representation(id)
);

-- Table: Place
CREATE TABLE IF NOT EXISTS place (
                                     id SERIAL PRIMARY KEY,
                                     place_name VARCHAR(255) NOT NULL,
                                     parameters TEXT
);

-- Table: PlaceLocation
CREATE TABLE IF NOT EXISTS place_location (
                                              place_id INT NOT NULL,
                                              location_id INT NOT NULL,
                                              PRIMARY KEY (place_id, location_id),
                                              FOREIGN KEY (place_id) REFERENCES place(id),
                                              FOREIGN KEY (location_id) REFERENCES location(id)
);
