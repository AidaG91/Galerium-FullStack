import { Routes, Route, Navigate } from "react-router-dom";
import Dashboard from "../pages/Dashboard";
import LandingPage from "../pages/LandingPage";
import Clients from "../pages/ClientsPage";
import SidebarLayout from "../layouts/SidebarLayout";
import ClientDetail from "../components/ClientDetail";

export default function AppRouter() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />

      <Route element={<SidebarLayout />}>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/clients" element={<Clients />} />
        <Route path="/clients/:id" element={<ClientDetail />} />
        {/* <Route path="/galleries" element={<h1>Galerías</h1>} />
        <Route path="/calendar" element={<h1>Calendario</h1>} />
        <Route path="/settings" element={<h1>Configuración</h1>} /> */}
      </Route>

      <Route path="*" element={<p>404 · Route not found</p>} />
    </Routes>
  );
}
