import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function Dashboard({ onLogout }) {

  const [data, setData] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [search, setSearch] = useState("");

  const [question, setQuestion] = useState("");
  const [answer, setAnswer] = useState("");

  const navigate = useNavigate();

  useEffect(() => {

    axios.get("http://localhost:8080/api/all")
      .then((res) => {
        setData(res.data);
        setFiltered(res.data);
      })
      .catch((err) => {
        console.log(err);
      });

  }, []);

  const handleSearch = (value) => {

    setSearch(value);

    const result = data.filter((item) =>
      (item.name || item.companyName)
        .toLowerCase()
        .includes(value.toLowerCase())
    );

    setFiltered(result);
  };

  const handleAI = () => {

    if (!question) {
      return;
    }

    setAnswer(
      "AI Suggestion: Improve ESG score by focusing on sustainability, governance, and renewable initiatives."
    );
  };

  return (

    <div style={{
      padding: "20px",
      background: "#f4f6f9",
      minHeight: "100vh"
    }}>

      {/* HEADER */}
      <div style={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center"
      }}>

        <h2>ESG Dashboard</h2>

        <button
          onClick={onLogout}
          style={{
            background: "red",
            color: "white",
            border: "none",
            padding: "10px 15px",
            borderRadius: "5px",
            cursor: "pointer"
          }}
        >
          Logout
        </button>

      </div>

      {/* SEARCH */}
      <div style={{ marginTop: "20px" }}>

        <input
          type="text"
          placeholder="Search company..."
          value={search}
          onChange={(e) => handleSearch(e.target.value)}
          style={{
            padding: "10px",
            width: "300px",
            borderRadius: "5px",
            border: "1px solid #ccc"
          }}
        />

      </div>

      {/* DATA CARDS */}
      <div style={{ marginTop: "20px" }}>

        {filtered.map((item) => (

          <div
            key={item.id}
            style={{
              background: "white",
              padding: "15px",
              marginBottom: "15px",
              borderRadius: "8px",
              boxShadow: "0 2px 8px rgba(0,0,0,0.1)"
            }}
          >

            <h3>{item.name || item.companyName}</h3>

            <p>
              Score: {item.score || item.esgScore}
            </p>

          </div>

        ))}

      </div>

      {/* AI PANEL */}
      <div style={{
        marginTop: "40px",
        background: "white",
        padding: "20px",
        borderRadius: "10px",
        boxShadow: "0 2px 8px rgba(0,0,0,0.1)"
      }}>

        <h3>AI Panel</h3>

        <input
          type="text"
          placeholder="Ask AI..."
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          style={{
            padding: "10px",
            width: "300px",
            borderRadius: "5px",
            border: "1px solid #ccc"
          }}
        />

        <button
          onClick={handleAI}
          style={{
            marginLeft: "10px",
            padding: "10px 15px",
            background: "green",
            color: "white",
            border: "none",
            borderRadius: "5px",
            cursor: "pointer"
          }}
        >
          Ask AI
        </button>

        <p style={{ marginTop: "15px" }}>
          {answer}
        </p>

      </div>

      {/* ANALYTICS BUTTON */}
      <div style={{ marginTop: "30px" }}>

        <button
          onClick={() => navigate("/analytics")}
          style={{
            padding: "12px 20px",
            background: "#1976d2",
            color: "white",
            border: "none",
            borderRadius: "5px",
            cursor: "pointer"
          }}
        >
          View Analytics
        </button>

      </div>

    </div>
  );
}

export default Dashboard;