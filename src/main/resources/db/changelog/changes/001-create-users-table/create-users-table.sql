CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    login    VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(64)  NOT NULL
);

COMMENT ON TABLE users IS 'Таблица для хранения информации о пользователях';
COMMENT ON COLUMN users.id IS 'Уникальный идентификатор пользователя';
COMMENT ON COLUMN users.login IS 'Логин пользователя, username или email';
COMMENT ON COLUMN users.password IS 'Пароль пользователя';