import { NavLink } from "react-router-dom";
import styles from "../styles/Sidebar.module.css";

export default function Sidebar() {
  return (
    <aside className={styles.container}>
      <NavLink
        to="/dashboard"
        className={({ isActive }) =>
          isActive ? `${styles.link} ${styles.active}` : styles.link
        }
      >
        Dashboard
      </NavLink>

      <NavLink
        to="/clients"
        className={({ isActive }) =>
          isActive ? `${styles.link} ${styles.active}` : styles.link
        }
      >
        Clients
      </NavLink>

      <NavLink
        to="/galleries"
        className={({ isActive }) =>
          isActive ? `${styles.link} ${styles.active}` : styles.link
        }
      >
        Galleries
      </NavLink>

      <NavLink
        to="/calendar"
        className={({ isActive }) =>
          isActive ? `${styles.link} ${styles.active}` : styles.link
        }
      >
        Calendar
      </NavLink>

      <NavLink
        to="/settings"
        className={({ isActive }) =>
          isActive ? `${styles.link} ${styles.active}` : styles.link
        }
      >
        Settings
      </NavLink>
    </aside>
  );
}
