-- Table: ExecutionPattern
CREATE TABLE IF NOT EXISTS execution_pattern (
                                                 id SERIAL PRIMARY KEY,
                                                 animation_instance_id INT NOT NULL,
                                                 FOREIGN KEY (animation_instance_id) REFERENCES animation_instance(id)
);

-- Table: Parameter
CREATE TABLE IF NOT EXISTS parameter (
                                         id SERIAL PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
                                         value TEXT NOT NULL,
                                         execution_pattern_id INT NOT NULL,
                                         FOREIGN KEY (execution_pattern_id) REFERENCES execution_pattern(id)
);

-- Table: Trigger
CREATE TABLE IF NOT EXISTS trigger (
                                       id SERIAL PRIMARY KEY,
                                       type VARCHAR(255) NOT NULL,
                                       time VARCHAR(255),
                                       smart_home_system VARCHAR(255),
                                       command VARCHAR(255)
);

-- Table: Frame
CREATE TABLE IF NOT EXISTS frame (
                                     id SERIAL PRIMARY KEY,
                                     colors BYTEA NOT NULL
);

-- Table: FrameSeq
CREATE TABLE IF NOT EXISTS frame_seq (
                                         id SERIAL PRIMARY KEY,
                                         output_no INT NOT NULL,
                                         start_with_led_idx INT NOT NULL
);

-- Table: FrameSeqFrame
CREATE TABLE IF NOT EXISTS frame_seq_frame (
                                               frame_seq_id INT NOT NULL,
                                               frame_id INT NOT NULL,
                                               PRIMARY KEY (frame_seq_id, frame_id),
                                               FOREIGN KEY (frame_seq_id) REFERENCES frame_seq(id),
                                               FOREIGN KEY (frame_id) REFERENCES frame(id)
);
