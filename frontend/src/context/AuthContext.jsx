import { createContext, useContext, useState } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem('jwt_token'));
  const [username, setUsername] = useState(localStorage.getItem('username'));

  const setAuth = (newToken, newUsername) => {
    localStorage.setItem('jwt_token', newToken);
    localStorage.setItem('username', newUsername);
    setToken(newToken);
    setUsername(newUsername);
  };

  const logout = () => {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('username');
    setToken(null);
    setUsername(null);
  };

  return (
    <AuthContext.Provider value={{ token, username, setAuth, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

// The hook is kept here to preserve the existing public API; exclude it from
// the Fast Refresh component-export check.
// eslint-disable-next-line react-refresh/only-export-components
export function useAuth() {
  return useContext(AuthContext);
}