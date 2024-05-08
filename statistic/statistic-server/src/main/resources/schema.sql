CREATE TABLE IF NOT EXISTS apps (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT PK_APP PRIMARY KEY (id),
    CONSTRAINT UQ_APP_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS uris (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT PK_URI PRIMARY KEY (id),
    CONSTRAINT UQ_URI_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS ipis (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    address VARCHAR(255) NOT NULL,
    CONSTRAINT PK_IP PRIMARY KEY (id),
    CONSTRAINT UQ_IP_ADDRESS UNIQUE (address)
);

CREATE TABLE IF NOT EXISTS statistics (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app_id INTEGER REFERENCES apps (id) ON DELETE CASCADE NOT NULL,
    uri_id INTEGER REFERENCES uris (id) ON DELETE CASCADE NOT NULL,
    ip_id INTEGER REFERENCES ipis (id) ON DELETE CASCADE NOT NULL,
    created TIMESTAMP NOT NULL,
    CONSTRAINT PK_STATISTIC PRIMARY KEY (id)
);
