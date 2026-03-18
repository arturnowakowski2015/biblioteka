import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'; // Import routera
import './index.css'
import App from './App.tsx'


createRoot(document.getElementById('root')!).render(
  <StrictMode>
    {/* BrowserRouter musi być rodzicem dla wszystkiego, co używa Routes/Route */}
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </StrictMode>
);

