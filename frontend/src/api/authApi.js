import axiosInstance from './axiosInstance';

export const login = async (credentials) => {
  const response = await axiosInstance.post('/auth/login', credentials);
  return response.data; // this IS the raw token string, confirmed earlier
};

export const registerCredentials = async (customerId, password) => {
  const response = await axiosInstance.post(`/customers/${customerId}/register`, { password });
  return response.data;
};
