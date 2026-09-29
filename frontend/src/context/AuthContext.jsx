import { createContext, useState, useContext } from 'react';
import { setAuthToken } from '../api/axiosInstance';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {

    // const [token, setTokenState] = useState();---> react memory

  // for localstorage
  const [token, setTokenState] = useState(() => {
    const storedToken = localStorage.getItem('authToken');
    if(storedToken){
      setAuthToken(storedToken);
    }
    return storedToken;
  });

  // const setToken = (newToken) => {
  //   setTokenState(newToken);
  //   setAuthToken(newToken);
  // };--> react memory

   const setToken = (newToken) => {
    setTokenState(newToken);
    setAuthToken(newToken);
    if(newToken){
      localStorage.setItem('authToken', newToken);
    } else {
      localStorage.removeItem('authToken');
    }
  };

  const isAuthenticated = Boolean(token);

  const logout = () => {
    setToken(null);
  };

  return (
    <AuthContext.Provider value={{ token, setToken, isAuthenticated, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}