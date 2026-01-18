export const API_ENDPOINTS = {
  auth: {
    login: '/auth/login',
    profile: '/auth/me',
  },
  users: {
    list: '/users',
    detail: (id: string) => `/users/${id}`,
    delete: (id: number) => `/users/${id}`,
    reviewedRestaurants: '/users/reviewedRestaurants',
    userReviews: '/users/reviews'
  },
  restaurants: {
    list: '/restaurants',
    userList: '/users/restaurants',
    delete: (id: number) => `/restaurants/${id}`,
    details: (id: string) => `/restaurants/${id}`,
    reviews: (id: string) => `/restaurants/${id}/ratings/reviews`,
    addReview: (id: string) => `/restaurants/${id}/ratings`,
    deleteReview: (id: string) => `/restaurants/ratings/${id}`
  },
  analytics: {
    analytics: '/analytics',
    recentReviews: '/analytics/recentReviews',
    popularRestaurants: '/analytics/popularRestaurants',
  }
};
