import React, { useEffect, useState } from "react";
import axios from "axios";

const Maaslarim = () => {
  const [salaries, setSalaries] = useState([]);
  const [error, setError] = useState("");
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchSalaries = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/users/my-salaries", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setSalaries(response.data);
      } catch (err) {
        setError("Maaş bilgileri getirilirken hata oluştu.");
      }
    };

    fetchSalaries();
  }, [token]);

  return (
    <div style={{ padding: "20px" }}>
      <h2>Maaşlarım</h2>

      {error && <p style={{ color: "red" }}>{error}</p>}

      <table border="1" cellPadding="10" style={{ borderCollapse: "collapse", marginTop: "10px" }}>
        <thead>
          <tr>
            <th>Tarih</th>
            <th>Miktar</th>
            <th>Para Birimi</th>
          </tr>
        </thead>
        <tbody>
          {salaries.map((salary, i) => (
            <tr key={i}>
              <td>{salary.date}</td>
              <td>{salary.amount}</td>
              <td>{salary.currency}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Maaslarim;
