import React from 'react';
import Map from './components/Map.js'
import './App.css';

function App() {
  return (
    <div className="App">
      <div style={{
        display: 'flex'
      }}>
        <Map></Map>
      </div>
    </div>
  );
}

export default App;
