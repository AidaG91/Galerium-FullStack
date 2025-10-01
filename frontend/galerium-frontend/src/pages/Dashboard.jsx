import { useState } from 'react';
import { FaCheckCircle, FaExclamationCircle } from 'react-icons/fa';
import styles from '../styles/Dashboard.module.css';

export default function Dashboard() {
  const [connectionStatus, setConnectionStatus] = useState(null);
  const [isTesting, setIsTesting] = useState(false);

  const probarConexion = async () => {
    setIsTesting(true);
    setConnectionStatus(null);
    try {
      const res = await fetch('http://localhost:8080/api/health');
      if (res.ok) {
        setConnectionStatus('success');
      } else {
        setConnectionStatus('error');
      }
    } catch (err) {
      console.error(err);
      setConnectionStatus('error');
    } finally {
      setIsTesting(false);
    }
  };

  return (
    <div className={styles.wrapper}>
      <header className={styles.header}>
        <h1>Dashboard</h1>
      </header>

      {/* ... stats will go here ... */}

      <section className={styles.connectionTest}>
        <h3>System Status</h3>
        <button
          onClick={probarConexion}
          className="btn btn--secondary"
          disabled={isTesting}
        >
          {isTesting ? 'Testing...' : 'Test Connection'}
        </button>

        {/* Status message */}
        {connectionStatus === 'success' && (
          <p className={`${styles.statusMessage} ${styles.success}`}>
            <FaCheckCircle /> Connection to the database is successful.
          </p>
        )}
        {connectionStatus === 'error' && (
          <p className={`${styles.statusMessage} ${styles.error}`}>
            <FaExclamationCircle /> Could not connect to the database.
          </p>
        )}
      </section>
    </div>
  );
}
