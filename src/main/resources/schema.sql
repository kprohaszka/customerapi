CREATE TABLE IF NOT EXISTS customers (
                                         id SERIAL PRIMARY KEY,
                                         first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    date_of_birth DATE NOT NULL,
    phone_number VARCHAR(15)
    );
CREATE TABLE IF NOT EXISTS db_initialization (
                                                 id SERIAL PRIMARY KEY,
                                                 initialized BOOLEAN NOT NULL,
                                                 initialization_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);