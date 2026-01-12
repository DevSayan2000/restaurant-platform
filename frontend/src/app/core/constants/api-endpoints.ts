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
    userList: '/users/restaurants'
  }
};
