-- A brief review:
--The led table represents individual LEDs and references the led_strip table which represents an array of LEDs.
--The output table seems to represent an output of an LED controller and it references the led_controller table.
--The output_led_strip is a many-to-many bridge table that links the output table and led_strip table.
--The location_controller table corresponds to your LocationController case class.
--The led_controller table represents the LED controllers and has a foreign key linking to the location_controller table.
--The physical_representation table seems to correspond well with the PhysicalRepresentation case class and references the location_controller table.

-- Table: LocationController
CREATE TABLE IF NOT EXISTS location_controller (
                                                   id SERIAL PRIMARY KEY,
                                                   serial VARCHAR(255) NOT NULL,
                                                   hardware VARCHAR(255),
                                                   parameter TEXT
);


-- Table: LedController
CREATE TABLE IF NOT EXISTS led_controller (
                                              id SERIAL PRIMARY KEY,
                                              position INT NOT NULL,
                                              parameters TEXT,
                                              location_controller_id INT NOT NULL,
                                              FOREIGN KEY (location_controller_id) REFERENCES location_controller(id)
);
-- Table: LedStrip
CREATE TABLE IF NOT EXISTS led_strip (
                                         id SERIAL PRIMARY KEY,
                                         position INT NOT NULL,
                                         parameters TEXT
);
-- Table: PhysicalRepresentation
CREATE TABLE IF NOT EXISTS physical_representation (
                                                       id SERIAL PRIMARY KEY,
                                                       parameters TEXT,
                                                       location_controller_id INT NOT NULL,
                                                       FOREIGN KEY (location_controller_id) REFERENCES location_controller(id)
);
-- Table: Led
CREATE TABLE IF NOT EXISTS led (
                                   id SERIAL PRIMARY KEY,
                                   color VARCHAR(255) NOT NULL,
                                   is_on BOOLEAN NOT NULL,
                                   brightness INT NOT NULL,
                                   position INT NOT NULL,
                                   strip_id INT NOT NULL,
                                   FOREIGN KEY (strip_id) REFERENCES led_strip(id)
);
-- Table: Output
CREATE TABLE IF NOT EXISTS output (
                                      id SERIAL PRIMARY KEY,
                                      position INT NOT NULL,
                                      controller_id INT NOT NULL,
                                      FOREIGN KEY (controller_id) REFERENCES led_controller(id)
);



-- Table: OutputLedStrip
CREATE TABLE IF NOT EXISTS output_led_strip (
                                                output_id INT NOT NULL,
                                                led_strip_id INT NOT NULL,
                                                PRIMARY KEY (output_id, led_strip_id),
                                                FOREIGN KEY (output_id) REFERENCES output(id),
                                                FOREIGN KEY (led_strip_id) REFERENCES led_strip(id)
);







