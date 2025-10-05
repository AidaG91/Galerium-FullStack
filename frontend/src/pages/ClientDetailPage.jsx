import { useNavigate, useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { FaUserCircle } from 'react-icons/fa';
import { FaEdit, FaTrash } from 'react-icons/fa';
import styles from '../styles/ClientDetail.module.css';
import DeleteModal from '../components/DeleteModal';
import { toast } from 'react-hot-toast';
import { getClientById, deleteClient } from '../api/clientService';

export default function ClientDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [client, setClient] = useState(null);
  const [showConfirm, setShowConfirm] = useState(false);

  const handleDelete = async (id) => {
  try {
    await deleteClient(id); // <-- Usar la función del servicio
    toast.success('Client deleted successfully!');
    navigate('/clients');
  } catch (err) {
    console.error('Error deleting client', err);
    toast.error('Failed to delete the client.');
  } finally {
    setShowConfirm(false);
  }
};

useEffect(() => {
    (async () => {
      try {
        const res = await fetch(`http://localhost:8080/api/clients/${id}`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        setClient(data);
      } catch (err) {
        console.error('Error fetching client', err);
      }
    })();
  }, [id]);

  if (!client) return <p>Loading client...</p>;

  return (
    <div className={styles.wrapper}>
      <header className={styles.header}>
        <div className={styles.headerTitle}>
          <button
            className="btn btn--secondary"
            onClick={() => navigate('/clients')}
          >
            ← Back to Clients
          </button>
          <h1>Client Details</h1>
        </div>
        <div className={styles.headerActions}>
          <button
            className="btn btn--secondary"
            onClick={() => navigate(`/clients/${client.id}/edit`)}
          >
            <FaEdit /> Edit
          </button>
          <button
            className="btn btn--danger"
            onClick={() => setShowConfirm(true)}
          >
            <FaTrash /> Delete
          </button>
        </div>
      </header>

      <main>
        <section className={styles.profileHeader}>
          <div className={styles.profilePicture}>
            {client.profilePictureUrl ? (
              <img src={client.profilePictureUrl} alt={client.fullName} />
            ) : (
              <FaUserCircle className={styles.placeholderIcon} />
            )}
          </div>
          <div className={styles.profileInfo}>
            <h2>{client.fullName}</h2>
            <p>{client.email}</p>
            {client.tags && client.tags.length > 0 && (
              <div className={styles.tagsContainer}>
                {client.tags.map((tag) => (
                  <span key={tag} className={styles.tag}>
                    {tag}
                  </span>
                ))}
              </div>
            )}
          </div>
        </section>

        <section className={styles.detailsGrid}>
          <div className={styles.detailItem}>
            <span className={styles.label}>Phone</span>
            <span className={styles.value}>{client.phoneNumber || '—'}</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.label}>Address</span>
            <span className={styles.value}>{client.address || '—'}</span>
          </div>
          <div className={`${styles.detailItem} ${styles.fullWidth}`}>
            <span className={styles.label}>Notes</span>
            <span className={styles.value}>{client.internalNotes || '—'}</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.label}>Client Since</span>
            <span className={styles.value}>
              {new Date(client.registrationDate).toLocaleDateString('en-En', {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
              })}
            </span>
          </div>
        </section>

        <section className={styles.futureSections}>
          <div className={styles.futureCard}>
            <h3>Galleries</h3>
            <p>(Coming Soon)</p>
          </div>
          <div className={styles.futureCard}>
            <h3>Appointments</h3>
            <p>(Coming Soon)</p>
          </div>
        </section>
      </main>

      {showConfirm && (
        <DeleteModal
          message="Are you sure you want to delete this client?"
          onConfirm={() => handleDelete(client.id)}
          onCancel={() => setShowConfirm(false)}
        />
      )}
    </div>
  );
}
