import Main from './pages/Main'
import { LibraryProvider } from "./ctx/Authctx.tsx";
import { Routes, Route } from 'react-router-dom';

import { Books } from './pages/Books.tsx';
import { Admin } from './pages/Admin.tsx';
import './pages/Main.css'
import { Auth } from './pages/Auth.tsx';
import { Order } from './pages/Order.tsx';

function App() {
  return (
    <LibraryProvider>
      <Routes>
        {/* Rodzic: Ścieżka /dashboard */}
        <Route path="auth" element={<Auth />} />
        <Route path="/" element={<Main />}>
          {/* Dzieci: Renderowane wewnątrz <Dashboard /> w miejscu <Outlet /> */}
          <Route path="order" element={<Order />} />
          <Route path="admin" element={<Admin />} />
          <Route path="books" element={<Books />} />
        </Route>
      </Routes>
    </LibraryProvider>
  );
}

export default App;