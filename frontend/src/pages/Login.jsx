import { useState } from "react";

function Login({ onLogin }) {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e) => {

    e.preventDefault();

    if (username === "admin" && password === "admin") {
      onLogin();
    } else {
      alert("Invalid Username or Password");
    }
  };

  return (

    <div style={{
      height: "100vh",
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      background: "none"
    }}>

      <div style={{
        width: "380px",
        background: "white",
        padding: "40px",
        borderRadius: "15px",
        boxShadow: "0 8px 20px rgba(0,0,0,0.2)"
      }}>

        <div style={{ textAlign: "center" }}>

          <h1 style={{
            marginBottom: "10px",
            color: "#0f172a"
          }}>
            ESG Platform
          </h1>

          <p style={{
            color: "gray",
            marginBottom: "30px"
          }}>
            Login to access dashboard analytics
          </p>

        </div>

        <form onSubmit={handleSubmit}>

          <div style={{ marginBottom: "20px" }}>

            <label style={{
              display: "block",
              marginBottom: "8px",
              fontWeight: "bold"
            }}>
              Username
            </label>

            <input
              type="text"
              placeholder="Enter username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              style={{
                width: "100%",
                padding: "12px",
                borderRadius: "8px",
                border: "1px solid #ccc",
                fontSize: "14px"
              }}
            />

          </div>

          <div style={{ marginBottom: "25px" }}>

            <label style={{
              display: "block",
              marginBottom: "8px",
              fontWeight: "bold"
            }}>
              Password
            </label>

            <input
              type="password"
              placeholder="Enter password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={{
                width: "100%",
                padding: "12px",
                borderRadius: "8px",
                border: "1px solid #ccc",
                fontSize: "14px"
              }}
            />

          </div>

          <button
            type="submit"
            style={{
              width: "100%",
              padding: "12px",
              background: "#2563eb",
              color: "white",
              border: "none",
              borderRadius: "8px",
              fontSize: "16px",
              fontWeight: "bold",
              cursor: "pointer"
            }}
          >
            Login
          </button>

        </form>

        <div style={{
          marginTop: "20px",
          textAlign: "center",
          color: "gray",
          fontSize: "14px"
        }}>
          Demo Credentials: admin / admin
        </div>

      </div>

    </div>
  );
}

export default Login;