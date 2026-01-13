export const API_ENDPOINTS = {
  auth: {
    login: '/auth/login',
    profile: '/auth/me',
  },
  users: {
    list: '/users',
    detail: (id: string) => `/users/${id}`,
  },
  restaurants: {
    list: '/restaurants',
    userList: '/users/restaurants',
    delete: (id: number) => `/restaurants/${id}`,
    details: (id: string) => `/restaurants/${id}`,
    reviews: (id: string) => `/restaurants/${id}/ratings/reviews`,
    addReview: (id: string) => `/restaurants/${id}/ratings`,
  }
};
