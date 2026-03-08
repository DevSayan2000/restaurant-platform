-- Create menu_item table for restaurant menus
CREATE TABLE menu_item (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL REFERENCES restaurant(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(20) NOT NULL,
    vegetarian BOOLEAN NOT NULL DEFAULT false,
    available BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_menu_item_restaurant_name UNIQUE (restaurant_id, name)
);

-- Index for fast lookups by restaurant
CREATE INDEX idx_menu_item_restaurant_id ON menu_item(restaurant_id);

-- Index for filtering by category
CREATE INDEX idx_menu_item_category ON menu_item(restaurant_id, category);

