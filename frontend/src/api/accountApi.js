import axiosInstance from './axiosInstance';

export const createAccount = async (customerId, accountData) => {
  const response = await axiosInstance.post(`/customers/${customerId}/accounts`, accountData);
  return response.data;
};

export const getAccountById = async (accountId) => {
  const response = await axiosInstance.get(`/accounts/${accountId}`);
  return response.data;
};

export const deposit = async (accountId, amount) => {
  const response = await axiosInstance.post(`/accounts/${accountId}/deposit`, { amount });
  return response.data;
};

export const withdraw = async (accountId, amount) => {
  const response = await axiosInstance.post(`/accounts/${accountId}/withdraw`, { amount });
  return response.data;
};

export const transfer = async (accountId, toAccountId, amount) => {
  const response = await axiosInstance.post(`/accounts/${accountId}/transfer`, { toAccountId, amount });
  return response.data;
};

export const getAccountsByCustomer = async (customerId) => {
  const response = await axiosInstance.get(`/customers/${customerId}/accounts`);
  return response.data;
};