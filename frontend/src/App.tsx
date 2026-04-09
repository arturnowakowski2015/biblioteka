import Main from './pages/Main'
import { LibraryProvider } from "./ctx/Authctx.tsx";
import { Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

import { Books } from './pages/Books.tsx';
import { Admin } from './pages/Admin.tsx';
import './pages/Main.css'
import { Auth } from './pages/Auth.tsx';
import { Order } from './pages/Order.tsx';

function App() {
  const queryClient = new QueryClient();
  return (


    //compound komponents
    <QueryClientProvider client={queryClient}>
      <LibraryProvider>
        <Routes>
          <Route path="auth" element={<Auth />} />
          <Route path="/" element={<Main />}>
            <Route path="order" element={<Order />} />
            <Route path="admin" element={<Admin />} />
            <Route path="books" element={<Books />} />
          </Route>
        </Routes>
      </LibraryProvider>
    </QueryClientProvider>
  );
}

export default App;