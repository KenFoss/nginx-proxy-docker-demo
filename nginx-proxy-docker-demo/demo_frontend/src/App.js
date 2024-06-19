import logo from './logo.svg';
import './App.css';
import Dash from './pages/Dash.js';
import Login from './pages/Login.js';

import React from 'react';
import {Route, Routes, Link, BrowserRouter} from 'react-router-dom';


function App() {


  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login/>}/>
        <Route path="/login-google" element={<Login/>} />
        <Route path="/dashboard" element={<Dash/>}/>
      </Routes>
    </BrowserRouter>
  );
}

export default App;