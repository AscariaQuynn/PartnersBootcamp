
CREATE TABLE files (
    id SERIAL PRIMARY KEY,
    content BYTEA NOT NULL,
    uploaded_file_flag SMALLINT NOT NULL,
    upload_status SMALLINT NOT NULL
);
