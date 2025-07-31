import React, { useEffect, useState } from "react";
import axios from "axios";

const Dashboard = () => {
  const [leaves, setLeaves] = useState([]);
  const [salaries, setSalaries] = useState([]);
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchData = async () => {
      const headers = { Authorization: `Bearer ${token}` };
      const leaveRes = await axios.get("http://localhost:8080/api/users/my-leaves", { headers });
      const salaryRes = await axios.get("http://localhost:8080/api/users/my-salaries", { headers });
      setLeaves(leaveRes.data);
      setSalaries(salaryRes.data);
    };
    fetchData();
  }, [token]);

  return (
    <div className="container">
      <h2>Kullanıcı Paneli</h2>

      <h3>İzinlerim</h3>
      <ul>
        {leaves.map((leave, i) => (
          <li key={i}>{leave.leaveDate} - {leave.reason}</li>
        ))}
      </ul>

      <h3>Maaşlarım</h3>
      <ul>
        {salaries.map((salary, i) => (
          <li key={i}>{salary.amount} {salary.currency}</li>
        ))}
      </ul>
    </div>
  );
};

export default Dashboard;
