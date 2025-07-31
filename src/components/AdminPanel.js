import React, { useEffect, useState } from "react";
import axios from "axios";

const AdminPanel = () => {
  const [users, setUsers] = useState([]);
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchUsers = async () => {
      const res = await axios.get("http://localhost:8080/api/users", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setUsers(res.data);
    };
    fetchUsers();
  }, [token]);

  return (
    <div className="container">
      <h2>Admin Paneli</h2>
      <h3>Tüm Çalışanlar</h3>
      <ul>
        {users.map((user) => (
          <li key={user.id}>{user.fullName} - {user.email} - {user.role}</li>
        ))}
      </ul>
    </div>
  );
};

export default AdminPanel;
