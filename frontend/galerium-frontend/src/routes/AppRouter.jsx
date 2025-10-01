import { Routes, Route } from 'react-router-dom';
import Dashboard from '../pages/Dashboard';
import LandingPage from '../pages/LandingPage';
import Clients from '../pages/ClientsPage';
import SidebarLayout from '../layouts/SidebarLayout';
import ClientDetail from '../pages/ClientDetailPage';
import ClientFormPage from '../pages/ClientFormPage';

export default function AppRouter() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />

      <Route element={<SidebarLayout />}>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/clients" element={<Clients />} />
        <Route path="/clients/:id" element={<ClientDetail />} />
        <Route path="/clients/new" element={<ClientFormPage />} />
        <Route path="/clients/:id/edit" element={<ClientFormPage />} />
        <Route path="/galleries" element={<h1>Galleries</h1>} />
        <Route path="/calendar" element={<h1>Calendar</h1>} />
        <Route path="/settings" element={<h1>Settings</h1>} />
      </Route>

      <Route path="*" element={<p>404 · Route not found</p>} />
    </Routes>
  );
}
