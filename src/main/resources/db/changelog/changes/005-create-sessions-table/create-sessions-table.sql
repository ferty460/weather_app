CREATE TABLE IF NOT EXISTS sessions
(
    id         VARCHAR(255) PRIMARY KEY,
    user_id    BIGINT    NOT NULL,
    expires_at TIMESTAMP NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (id)
);

COMMENT ON TABLE sessions IS 'Таблица для хранения информации о сессиях';
COMMENT ON COLUMN sessions.id IS 'Уникальный идентификатор сессии';
COMMENT ON COLUMN sessions.expires_at IS 'Время истечения сессии. Равно времени создания сессии плюс N часов';