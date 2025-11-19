CREATE TABLE IF NOT EXISTS locations
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(128) NOT NULL,
    user_id   BIGINT       NOT NULL,
    latitude  NUMERIC(10, 8),
    longitude NUMERIC(11, 8),

    FOREIGN KEY (user_id) REFERENCES users (id)
);

COMMENT ON TABLE locations IS 'Таблица для хранения географических локаций пользователей';
COMMENT ON COLUMN locations.id IS 'Уникальный идентификатор локации';
COMMENT ON COLUMN locations.name IS 'Название локации';
COMMENT ON COLUMN locations.user_id IS 'Внешний ключ на пользователя';
COMMENT ON COLUMN locations.latitude IS 'Географическая широта';
COMMENT ON COLUMN locations.longitude IS 'Географическая долгота';