import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
  const [leaves, setLeaves] = useState([]);
  const [salaries, setSalaries] = useState([]);
  const [error, setError] = useState("");
  const token = localStorage.getItem("token");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const headers = { Authorization: `Bearer ${token}` };
        const leaveRes = await axios.get("http://localhost:8080/api/users/my-leaves", { headers });
        const salaryRes = await axios.get("http://localhost:8080/api/users/my-salaries", { headers });
        setLeaves(leaveRes.data);
        setSalaries(salaryRes.data);
      } catch (err) {
        setError("Veriler getirilirken bir hata oluştu.");
        console.error(err);
      }
    };
    fetchData();
  }, [token]);

  const handleIzinlerimClick = () => {
    navigate("/izinlerim");
  };

  const handleMaaslarimClick = () => {
    navigate("/maaslarim");
  };

  return (
    <div className="container" style={styles.container}>
      <h2 style={styles.title}>Kullanıcı Paneli</h2>

      {error && <p style={styles.error}>{error}</p>}

      <div style={styles.buttonContainer}>
        <button onClick={handleIzinlerimClick} style={styles.button}>
          İzinlerim
        </button>
        <button onClick={handleMaaslarimClick} style={styles.button}>
          Maaşlarım
        </button>
      </div>

      <div style={styles.section}>
        <h3>Son İzin Kayıtları</h3>
        <ul>
          {leaves.map((leave, i) => (
            <li key={i}>
              {leave.leaveDate} - {leave.reason}
            </li>
          ))}
        </ul>
      </div>

      <div style={styles.section}>
        <h3>Son Maaş Bilgileri</h3>
        <ul>
          {salaries.map((salary, i) => (
            <li key={i}>
              {salary.date} - {salary.amount} {salary.currency}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

const styles = {
  container: {
    maxWidth: "800px",
    margin: "auto",
    padding: "20px",
    backgroundColor: "#f5f5f5",
    borderRadius: "10px",
  },
  title: {
    textAlign: "center",
    marginBottom: "30px",
  },
  buttonContainer: {
    display: "flex",
    justifyContent: "center",
    gap: "20px",
    marginBottom: "20px",
  },
  button: {
    padding: "10px 20px",
    fontSize: "16px",
    borderRadius: "8px",
    backgroundColor: "#007bff",
    color: "#fff",
    border: "none",
    cursor: "pointer",
  },
  section: {
    marginTop: "30px",
  },
  error: {
    color: "red",
    textAlign: "center",
  },
};

export default Dashboard;
