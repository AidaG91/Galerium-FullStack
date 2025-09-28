import { Routes, Route, Navigate, NavLink, Outlet } from "react-router-dom";
import Dashboard from "../pages/Dashboard";
import LandingPage from "../pages/LandingPage";
import Clients from "../pages/Clients";

function Layout() {
  return (
    <>
      <header style={{ padding: "12px 16px", borderBottom: "1px solid #eee" }}>
        <nav style={{ display: "flex", gap: 12, justifyContent: "center" }}>
          <NavLink
            to="/home"
            style={({ isActive }) => ({
              textDecoration: "none",
              color: isActive ? "#ef8a17" : "#111827",
              fontWeight: isActive ? 700 : 500,
            })}
          >
            Home
          </NavLink>

          <NavLink
            to="/dashboard"
            style={({ isActive }) => ({
              textDecoration: "none",
              color: isActive ? "#ef8a17" : "#111827",
              fontWeight: isActive ? 700 : 500,
            })}
          >
            Dashboard
          </NavLink>
          
          <NavLink
            to="/clients"
            style={({ isActive }) => ({
              textDecoration: "none",
              color: isActive ? "#ef8a17" : "#111827",
              fontWeight: isActive ? 700 : 500,
            })}
          >
            Clients
          </NavLink>
        </nav>
      </header>
      <main style={{ padding: 16 }}>
        <Outlet />
      </main>
    </>
  );
}

function NotFound() {
  return <p>404 · Route not found</p>;
}

export default function AppRouter() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<Navigate to="/home" replace />} />
        <Route path="/home" element={<LandingPage />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/clients" element={<Clients />} />

        <Route path="*" element={<NotFound />} />
      </Route>
    </Routes>
  );
}
