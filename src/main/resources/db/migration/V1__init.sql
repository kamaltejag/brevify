-- Needs to be run only once
-- Needed for creating UUID automatically
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

---- Users Table
--CREATE TABLE users (
--    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
--    username VARCHAR(100) NOT NULL,
--    email VARCHAR(100) UNIQUE NOT NULL,
--    password VARCHAR(100) NOT NULL,
--    created_at TIMESTAMP DEFAULT NOW(),
--    updated_at TIMESTAMP DEFAULT NOW()
--);

CREATE TABLE code_url_mapping (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    long_url TEXT NOT NULL,
    short_code VARCHAR(20) UNIQUE NOT NULL,
--    userId UUID NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
--    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE url_clicks (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    code_id UUID NOT NULL,
    visited_at TIMESTAMP DEFAULT NOW(),
    ip_address VARCHAR(100),
    user_agent VARCHAR(100),
    referrer VARCHAR(100),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (code_id) REFERENCES code_url_mapping(id) ON DELETE CASCADE
);