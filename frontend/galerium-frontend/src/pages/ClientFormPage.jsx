import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import ClientForm from "../components/ClientForm";
import styles from "../styles/ClientFormPage.module.css";

export default function ClientFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [initialData, setInitialData] = useState(null);

  useEffect(() => {
    if (!id) return;
    (async () => {
      try {
        const res = await fetch(`http://localhost:8080/api/clients/${id}`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        setInitialData(data);
      } catch (err) {
        console.error("Error loading client", err);
      }
    })();
  }, [id]);

  return (
    <section className={styles.wrapper}>
      <header className={styles.header}>
        <h2>{id ? "Edit Client" : "Create Client"}</h2>
        <button className={styles.backBtn} onClick={() => navigate("/clients")}>
          ← Back to list
        </button>
      </header>

      <ClientForm
        initialData={initialData}
        onClose={() => navigate(id ? `/clients/${id}` : "/clients")}
        onSave={(client) => {
          const target = client?.id ? `/clients/${client.id}` : "/clients";
          navigate(target);
        }}
      />
    </section>
  );
}
