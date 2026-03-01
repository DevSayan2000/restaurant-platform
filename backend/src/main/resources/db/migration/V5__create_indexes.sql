-- Indexes for performance optimization

-- Users table indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_active ON users(active);

-- Restaurant table indexes
CREATE INDEX idx_restaurant_city ON restaurant(city);
CREATE INDEX idx_restaurant_food_type ON restaurant(food_type);

-- Rating table indexes
CREATE INDEX idx_rating_restaurant ON rating(restaurant_id);
CREATE INDEX idx_rating_user ON rating(user_id);
CREATE INDEX idx_rating_created_at ON rating(created_at);


