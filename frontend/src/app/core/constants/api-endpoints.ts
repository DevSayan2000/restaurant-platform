export const API_ENDPOINTS = {
  auth: {
    login: '/auth/login',
    logout: '/auth/me',
  },
  users: {
    list: '/api/users',
    detail: (id: string) => `/users/${id}`,
  },
};
