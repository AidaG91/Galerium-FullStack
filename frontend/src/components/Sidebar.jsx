import { NavLink, Link } from 'react-router-dom';
import styles from '../styles/Sidebar.module.css';
import {
  FaTachometerAlt,
  FaUsers,
  FaImages,
  FaCalendarAlt,
  FaCog,
  FaUserCircle,
  FaTimes,
} from 'react-icons/fa';

export default function Sidebar({ isOpen, onClose }) {
  return (
    <nav className={`${styles.sidebar} ${isOpen ? styles.open : ''}`}>
      <button className={styles.closeButton} onClick={onClose}>
        <FaTimes />
      </button>{' '}
      {/* --- Upper Section --- */}
      <div className={styles.sidebarTop}>
        <Link to="/" className={styles.logo}>
          <img src="/galerium-logo-text.png" alt="Galerium Logo" />
        </Link>
        <ul className={styles.navList}>
          <li>
            <NavLink
              to="/dashboard"
              className={({ isActive }) =>
                isActive ? `${styles.navLink} ${styles.active}` : styles.navLink
              }
            >
              <FaTachometerAlt />
              <span>Dashboard</span>
            </NavLink>
          </li>
          <li>
            <NavLink
              to="/clients"
              className={({ isActive }) =>
                isActive ? `${styles.navLink} ${styles.active}` : styles.navLink
              }
            >
              <FaUsers />
              <span>Clients</span>
            </NavLink>
          </li>
          <li>
            <NavLink
              to="/galleries"
              className={({ isActive }) =>
                isActive ? `${styles.navLink} ${styles.active}` : styles.navLink
              }
            >
              <FaImages />
              <span>Galleries</span>
            </NavLink>
          </li>
          <li>
            <NavLink
              to="/calendar"
              className={({ isActive }) =>
                isActive ? `${styles.navLink} ${styles.active}` : styles.navLink
              }
            >
              <FaCalendarAlt />
              <span>Calendar</span>
            </NavLink>
          </li>
        </ul>
      </div>
      {/* --- Lower Section --- */}
      <div className={styles.sidebarBottom}>
        <div className={styles.userProfile}>
          <FaUserCircle className={styles.avatar} />
          <div className={styles.userName}>Photographer</div>
        </div>
        <NavLink to="/settings" className={styles.settingsLink}>
          <FaCog />
        </NavLink>
      </div>
    </nav>
  );
}
