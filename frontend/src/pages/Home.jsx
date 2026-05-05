import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Home() {
  const { token, logout } = useAuth();
  const navigate = useNavigate();
  const [records, setRecords] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    companyName: "",
    esgScore: "",
    industry: "",
    country: "",
    environmentalScore: "",
    socialScore: "",
    governanceScore: "",
  });

  useEffect(() => {
    fetchRecords();
  }, [token]);

  const fetchRecords = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/esg", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (!response.ok) {
        console.error("Failed to fetch:", response.status);
        setRecords([]);
        return;
      }
      const data = await response.json();
      setRecords(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error("Error fetching records:", error);
      setRecords([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await fetch("http://localhost:8080/api/esg", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          ...formData,
          esgScore: parseFloat(formData.esgScore),
          environmentalScore: formData.environmentalScore ? parseFloat(formData.environmentalScore) : null,
          socialScore: formData.socialScore ? parseFloat(formData.socialScore) : null,
          governanceScore: formData.governanceScore ? parseFloat(formData.governanceScore) : null,
        }),
      });
      setShowForm(false);
      setFormData({
        companyName: "",
        esgScore: "",
        industry: "",
        country: "",
        environmentalScore: "",
        socialScore: "",
        governanceScore: "",
      });
      fetchRecords();
    } catch (error) {
      console.error("Error creating record:", error);
    }
  };

  const handleAnalyze = async (record) => {
    try {
      const response = await fetch("http://localhost:8080/api/ai/analyze-esg", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          companyName: record.companyName,
          esgScore: record.esgScore,
          industry: record.industry,
        }),
      });
      const data = await response.json();
      alert(JSON.stringify(data, null, 2));
    } catch (error) {
      console.error("Error analyzing:", error);
    }
  };

  if (loading) return <div className="p-8">Loading...</div>;

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold">ESG Benchmark Comparator</h1>
        <div className="flex gap-4">
          <button
            onClick={() => setShowForm(!showForm)}
            className="bg-blue-500 text-white px-4 py-2 rounded"
          >
            {showForm ? "Cancel" : "Add New Record"}
          </button>
          <button
            onClick={() => { logout(); navigate("/"); }}
            className="bg-red-500 text-white px-4 py-2 rounded"
          >
            Logout
          </button>
        </div>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow mb-6">
          <div className="grid grid-cols-2 gap-4">
            <input
              placeholder="Company Name"
              value={formData.companyName}
              onChange={(e) => setFormData({ ...formData, companyName: e.target.value })}
              className="border p-2 rounded"
              required
            />
            <input
              placeholder="ESG Score"
              type="number"
              step="0.1"
              value={formData.esgScore}
              onChange={(e) => setFormData({ ...formData, esgScore: e.target.value })}
              className="border p-2 rounded"
              required
            />
            <input
              placeholder="Industry"
              value={formData.industry}
              onChange={(e) => setFormData({ ...formData, industry: e.target.value })}
              className="border p-2 rounded"
            />
            <input
              placeholder="Country"
              value={formData.country}
              onChange={(e) => setFormData({ ...formData, country: e.target.value })}
              className="border p-2 rounded"
            />
            <input
              placeholder="Environmental Score"
              type="number"
              step="0.1"
              value={formData.environmentalScore}
              onChange={(e) => setFormData({ ...formData, environmentalScore: e.target.value })}
              className="border p-2 rounded"
            />
            <input
              placeholder="Social Score"
              type="number"
              step="0.1"
              value={formData.socialScore}
              onChange={(e) => setFormData({ ...formData, socialScore: e.target.value })}
              className="border p-2 rounded"
            />
            <input
              placeholder="Governance Score"
              type="number"
              step="0.1"
              value={formData.governanceScore}
              onChange={(e) => setFormData({ ...formData, governanceScore: e.target.value })}
              className="border p-2 rounded"
            />
          </div>
          <button
            type="submit"
            className="mt-4 bg-green-500 text-white px-6 py-2 rounded"
          >
            Create Record
          </button>
        </form>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {records.map((record) => (
          <div key={record.id} className="bg-white p-6 rounded-lg shadow">
            <h3 className="text-xl font-bold">{record.companyName}</h3>
            <p className="text-2xl font-semibold text-blue-600">
              ESG Score: {record.esgScore}
            </p>
            <p className="text-gray-600">Industry: {record.industry}</p>
            <p className="text-gray-600">Country: {record.country}</p>
            {record.environmentalScore && (
              <p className="text-green-600">Environmental: {record.environmentalScore}</p>
            )}
            {record.socialScore && (
              <p className="text-yellow-600">Social: {record.socialScore}</p>
            )}
            {record.governanceScore && (
              <p className="text-purple-600">Governance: {record.governanceScore}</p>
            )}
            <button
              onClick={() => handleAnalyze(record)}
              className="mt-4 bg-purple-500 text-white px-4 py-2 rounded w-full"
            >
              AI Analysis
            </button>
          </div>
        ))}
      </div>

      {records.length === 0 && (
        <div className="text-center text-gray-500 mt-8">
          No ESG records found. Add your first record!
        </div>
      )}
    </div>
  );
}