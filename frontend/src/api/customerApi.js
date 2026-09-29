import axiosInstance from './axiosInstance';

export const createCustomer = async (customerData) => {
  const response = await axiosInstance.post('/customers', customerData);
  return response.data;
};

export const getCustomerById = async (customerId) => {
  const response = await axiosInstance.get(`/customers/${customerId}`);
  return response.data;
};

export const getCurrentCustomer = async () => {
  const response = await axiosInstance.get('/customers/me');
  return response.data;
};