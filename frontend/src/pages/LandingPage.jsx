import { Link } from 'react-router-dom';
import { FaSignInAlt } from 'react-icons/fa';
import styles from '../styles/LandingPage.module.css';

export default function LandingPage() {
  return (
    <div className={styles.landingPage}>
      <header className={styles.header}>
        <nav className={styles.navbar}>
          <Link to="/" className={styles.logo}>
            <img src="/galerium-logo-text.png" alt="Galerium Logo" />
          </Link>
          <ul className={styles.navLinks}>
            <li>
              <Link to="/">Home</Link>
            </li>
            <li>
              <Link to="/about">About Us</Link>
            </li>
            <li>
              <Link to="/contact">Contact</Link>
            </li>
          </ul>
          <Link to="/dashboard" className="btn btn--primary">
            <FaSignInAlt />
            <span>Log In</span>
          </Link>
        </nav>
      </header>

      <main className={styles.hero}>
        <div className={styles.heroText}>
          <h1>Your Clients, Your Photos, All in One Place.</h1>
          <p>
            Galerium is the essential tool for professional photographers to
            manage clients, organize sessions, and deliver beautiful galleries
            effortlessly.
          </p>
          <Link to="/dashboard" className="btn btn--primary">
            Get Started
          </Link>
        </div>
        <div className={styles.heroImage}>
          <img
            src="https://images.unsplash.com/photo-1488926756850-a13b25e2f415?q=80&w=1167&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            alt="Photographer's desk with camera and laptop"
          />
        </div>
      </main>
    </div>
  );
}
