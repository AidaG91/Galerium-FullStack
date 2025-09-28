import Sidebar from "../components/Sidebar";
import { Outlet } from "react-router-dom";

export default function SidebarLayout() {
  return (
    <div style={{ display: "flex" }}>
      <Sidebar />
      <main style={{ flex: 1, padding: "2rem" }}>
        <Outlet />
      </main>
    </div>
  );
}
