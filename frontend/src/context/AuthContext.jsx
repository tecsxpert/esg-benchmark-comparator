import { createContext, useContext, useMemo, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(
    () => localStorage.getItem("token")
  );

  const value = useMemo(
    () => ({
      token,
      isLoggedIn: !!token,
      async login(username, password) {
        try {
          const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password }),
          });
          if (!response.ok) return false;
          const data = await response.json();
          setToken(data.token);
          localStorage.setItem("token", data.token);
          return true;
        } catch (err) {
          console.error("Login error:", err);
          return false;
        }
      },
      logout() {
        localStorage.removeItem("token");
        setToken(null);
      },
    }),
    [token]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used inside AuthProvider");
  }
  return context;
}
