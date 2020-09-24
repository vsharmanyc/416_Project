import React from 'react';
import HomeScreen from './components/HomeScreen.js'
import Navbar from './components/Navbar.js'
import './App.css';

function App() {
  return (
    <div className="App">
      <div>
        <Navbar
          sections={['Reports','Graphs', 'Sources']}
        ></Navbar>
        <HomeScreen></HomeScreen>
      </div>
    </div>
  );
}

export default App;
