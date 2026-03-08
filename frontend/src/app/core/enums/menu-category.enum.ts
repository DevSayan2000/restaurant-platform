export enum MenuCategory {
  APPETIZER = 'APPETIZER',
  MAIN_COURSE = 'MAIN_COURSE',
  DESSERT = 'DESSERT',
}

export const MenuCategoryLabels: Record<MenuCategory, string> = {
  [MenuCategory.APPETIZER]: 'Appetizers',
  [MenuCategory.MAIN_COURSE]: 'Main Course',
  [MenuCategory.DESSERT]: 'Desserts',
};

