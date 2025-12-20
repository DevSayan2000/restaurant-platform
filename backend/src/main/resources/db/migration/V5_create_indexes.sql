CREATE INDEX idx_restaurant_city ON restaurant(city);
CREATE INDEX idx_rating_restaurant ON rating(restaurant_id);
CREATE INDEX idx_footfall_restaurant ON footfall(restaurant_id);
CREATE INDEX idx_footfall_date ON footfall(visit_date);