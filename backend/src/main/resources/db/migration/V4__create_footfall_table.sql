CREATE TABLE footfall (
    id BIGINT PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    visit_date DATE NOT NULL,
    footfall_count INT CHECK (footfall_count >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_footfall_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurant(id),
    CONSTRAINT fk_footfall_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);