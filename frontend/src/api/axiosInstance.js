import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,   // set the BE address
  headers: {    //header used to send extra information
    'Content-Type': 'application/json', //data is on json formate
  },
});

let authToken = null;

export const setAuthToken = (token) => {
  authToken = token;
};

axiosInstance.interceptors.request.use((config) => {
  if (authToken) {
    config.headers.Authorization = `Bearer ${authToken}`;
  }
  return config;
});

export default axiosInstance;

/*
esse use hoga ye
import axiosInstance from "./axiosInstance";

axiosInstance.get("/customers");

Internally Axios khud bana dega:

http://localhost:8080/customers

Kyuki usko base address pehle se yaad hai.
*/