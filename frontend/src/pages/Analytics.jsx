import { useEffect, useState } from "react";
import axios from "axios";

import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  ResponsiveContainer
} from "recharts";

function Analytics() {

  const [data, setData] = useState([]);

  useEffect(() => {

    axios.get("http://localhost:8080/api/all")
      .then((res) => {

        const formattedData = res.data.map(item => ({
          name: item.companyName || item.name,
          score: item.esgScore || item.score
        }));

        setData(formattedData);

      })
      .catch((err) => {
        console.log(err);
      });

  }, []);

  return (

    <div style={{
      width: "100%",
      minHeight: "100vh",
      padding: "20px",
      background: "#f4f6f9"
    }}>

      <h2 style={{
        textAlign: "center",
        marginBottom: "30px"
      }}>
        ESG Analytics Dashboard
      </h2>

      <div style={{
        width: "100%",
        height: "500px",
        background: "white",
        borderRadius: "10px",
        padding: "20px",
        boxShadow: "0 2px 10px rgba(0,0,0,0.1)"
      }}>

        <ResponsiveContainer width="100%" height="100%">

          <BarChart data={data}>

            <CartesianGrid strokeDasharray="3 3" />

            <XAxis dataKey="name" />

            <YAxis />

            <Tooltip />

            <Bar dataKey="score" fill="#1976d2" />

          </BarChart>

        </ResponsiveContainer>

      </div>

    </div>
  );
}

export default Analytics;