import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <div>
      <h1>Hoşgeldiniz!</h1>
      <p>İnsan Kaynakları Uygulamasına giriş yapınız.</p>
      <Link to="/login">Giriş Yap</Link> | <Link to="/register">Kayıt Ol</Link>
    </div>
  );
};

export default Home;
