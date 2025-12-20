CREATE TABLE rating (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    review TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_rating_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurant(id),
    CONSTRAINT fk_rating_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT unique_user_restaurant_rating
        UNIQUE (restaurant_id, user_id)
);