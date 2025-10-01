import { useParams, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import ClientForm from '../components/ClientForm';
import styles from '../styles/ClientFormPage.module.css';
import { getClientById, getAllTags } from '../api/clientService';

export default function ClientFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [initialData, setInitialData] = useState(null);
  const [allTags, setAllTags] = useState([]);

  useEffect(() => {
    getAllTags().then(setAllTags).catch(console.error);
  }, []);

  useEffect(() => {
    if (!id) return;
    getClientById(id).then(setInitialData).catch(console.error);
  }, [id]);

  return (
    <section className={styles.wrapper}>
      <header className={styles.header}>
        <h2>{id ? 'Edit Client' : 'Create Client'}</h2>
        <button
          className="btn btn--secondary"
          onClick={() => navigate('/clients')}
        >
          ← Back to list
        </button>
      </header>

      <ClientForm
        initialData={initialData}
        allTags={allTags}
        onClose={() => navigate(id ? `/clients/${id}` : '/clients')}
        onSave={(client) => {
          const target = client?.id ? `/clients/${client.id}` : '/clients';
          navigate(target);
        }}
      />
    </section>
  );
}
