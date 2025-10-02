import { useState } from 'react';
import { Outlet } from 'react-router-dom';
import { FaBars } from 'react-icons/fa';
import Sidebar from '../components/Sidebar';
import styles from '../styles/SidebarLayout.module.css';

export default function SidebarLayout() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  return (
    <div className={styles.layout}>
      <Sidebar isOpen={isSidebarOpen} onClose={toggleSidebar} />

      <div className={styles.mainContent}>
        <button className={styles.hamburgerButton} onClick={toggleSidebar}>
          <FaBars />
        </button>
        <main className={styles.content}>
          <Outlet />
        </main>
      </div>

      {isSidebarOpen && (
        <div className={styles.overlay} onClick={toggleSidebar}></div>
      )}
    </div>
  );
}
