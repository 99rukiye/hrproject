import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setErrorMsg('');
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', { email, password });

      const { token, role } = response.data;

      localStorage.setItem('token', token);

      if (role === 'ADMIN') {
        navigate('/admin');
      } else {
        navigate('/dashboard');
      }
    } catch (error) {
      if (error.response && error.response.status === 403) {
        setErrorMsg('Hesabınız bloke olmuştur. Lütfen yöneticinizle iletişime geçin.');
      } else {
        setErrorMsg('Giriş başarısız. Bilgilerinizi kontrol edin.');
      }
    }
  };

  const goToRegister = () => {
    navigate('/register');
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.title}>Giriş Yap</h2>
      <form onSubmit={handleLogin} style={styles.form}>
        <input
          type="email"
          placeholder="Email"
          value={email}
          required
          onChange={(e) => setEmail(e.target.value)}
          style={styles.input}
        />
        <input
          type="password"
          placeholder="Şifre"
          value={password}
          required
          onChange={(e) => setPassword(e.target.value)}
          style={styles.input}
        />
        <button type="submit" style={styles.button}>Giriş Yap</button>
      </form>
      {errorMsg && <p style={styles.error}>{errorMsg}</p>}
      <p style={styles.registerText}>
        Üye değil misiniz?{' '}
        <button onClick={goToRegister} style={styles.registerButton}>Kayıt Ol</button>
      </p>
    </div>
  );
};

const styles = {
  container: {
    maxWidth: '400px',
    margin: '50px auto',
    padding: '30px',
    border: '1px solid #ccc',
    borderRadius: '8px',
    textAlign: 'center',
    backgroundColor: '#f9f9f9'
  },
  title: {
    marginBottom: '20px'
  },
  form: {
    display: 'flex',
    flexDirection: 'column'
  },
  input: {
    padding: '10px',
    margin: '10px 0',
    borderRadius: '5px',
    border: '1px solid #ccc'
  },
  button: {
    padding: '10px',
    borderRadius: '5px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    cursor: 'pointer'
  },
  error: {
    color: 'red',
    marginTop: '10px'
  },
  registerText: {
    marginTop: '15px',
    fontSize: '14px'
  },
  registerButton: {
    background: 'none',
    border: 'none',
    color: '#007bff',
    textDecoration: 'underline',
    cursor: 'pointer',
    padding: 0
  }
};

export default Login;
