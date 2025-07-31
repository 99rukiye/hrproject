import React, { useEffect, useState } from "react";
import axios from "axios";

const Izinlerim = () => {
  const [leaves, setLeaves] = useState([]);
  const [error, setError] = useState("");

  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchLeaves = async () => {
      try {
        const headers = { Authorization: `Bearer ${token}` };
        const res = await axios.get("http://localhost:8080/api/users/my-leaves", { headers });
        setLeaves(res.data);
      } catch (err) {
        setError("İzin bilgileri alınamadı.");
      }
    };

    fetchLeaves();
  }, [token]);

  return (
    <div style={{ padding: "20px" }}>
      <h2>İzinlerim</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <ul>
        {leaves.map((leave, i) => (
          <li key={i}>
            {leave.leaveDate} - {leave.reason}
          </li>
        ))}
      </ul>
      <button style={{ marginTop: "10px" }}>Yeni İzin Talebi</button>
    </div>
  );
};

export default Izinlerim;
