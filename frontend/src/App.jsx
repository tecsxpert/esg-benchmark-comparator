import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Analytics from "./pages/Analytics";

function App() {

  const isLoggedIn = localStorage.getItem("loggedIn");

  const handleLogin = () => {
    localStorage.setItem("loggedIn", "true");
    window.location.href = "/dashboard";
  };

  const handleLogout = () => {
    localStorage.removeItem("loggedIn");
    window.location.href = "/";
  };

  return (

    <BrowserRouter>

      <Routes>

        {/* LOGIN PAGE */}
        <Route
          path="/"
          element={
            isLoggedIn
              ? <Navigate to="/dashboard" />
              : <Login onLogin={handleLogin} />
          }
        />

        {/* DASHBOARD PAGE */}
        <Route
          path="/dashboard"
          element={
            isLoggedIn
              ? <Dashboard onLogout={handleLogout} />
              : <Navigate to="/" />
          }
        />

        {/* ANALYTICS PAGE */}
        <Route
          path="/analytics"
          element={
            isLoggedIn
              ? <Analytics />
              : <Navigate to="/" />
          }
        />

      </Routes>

    </BrowserRouter>
  );
}

export default App;