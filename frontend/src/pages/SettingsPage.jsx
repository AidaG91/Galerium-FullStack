import ManageTags from '../components/ManageTags';
import styles from '../styles/SettingsPage.module.css';

export default function SettingsPage() {
  return (
    <div className={styles.wrapper}>
      <header className={styles.header}>
        <h1>Settings</h1>
      </header>
      <main>
        {/* Aquí pondremos nuestro nuevo componente */}
        <ManageTags />
        {/* En el futuro, podrías añadir más componentes de ajustes aquí */}
      </main>
    </div>
  );
}