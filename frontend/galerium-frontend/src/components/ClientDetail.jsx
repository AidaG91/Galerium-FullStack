import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { FaEdit, FaTrash } from "react-icons/fa";
import styles from "../styles/ClientDetail.module.css";
import ClientForm from "./ClientForm";

export default function ClientDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [client, setClient] = useState(null);

  const [showForm, setShowForm] = useState(false);
  const [editData, setEditData] = useState(null);

  const startEdit = (client) => {
    setEditData(client);
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this client?")) return;
    try {
      await fetch(`http://localhost:8080/api/clients/${id}`, {
        method: "DELETE",
      });
      navigate("/clients");
    } catch (err) {
      console.error("Error deleting client", err);
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
        console.error("Error fetching client", err);
      }
    })();
  }, [id]);

  if (!client) return <p>Loading client...</p>;

  return (
    <div className={styles.page}>
      {/* Contenido principal */}
      <main className={styles.content}>
        <header className={styles.header}>
          <h2>Clients</h2>
          <button
            onClick={() => navigate("/clients")}
            className={styles.backBtn}
          >
            ← Back
          </button>
        </header>

        <section className={styles.topSection}>
          <div className={styles.photo}>
            {client.profilePictureUrl ? (
              <img src={client.profilePictureUrl} alt={client.fullName} />
            ) : (
              <div className={styles.placeholder}>Photo</div>
            )}
            <div className={styles.actions}>
              <button title="Edit" onClick={() => startEdit(client)}>
                <FaEdit />
              </button>
              <button title="Delete" onClick={() => handleDelete(client.id)}>
                <FaTrash />
              </button>
            </div>
          </div>

          <div className={styles.details}>
            <h3>{client.fullName}</h3>
            <p>
              <strong>Email:</strong> {client.email}
            </p>
            <p>
              <strong>Phone:</strong> {client.phoneNumber}
            </p>
            <p>
              <strong>Address:</strong> {client.address}
            </p>
            <p>
              <strong>Notes:</strong> {client.internalNotes ?? "—"}
            </p>
          </div>
        </section>

        <section className={styles.bottomSection}>
          <div className={styles.calendar}>CALENDAR (coming soon)</div>
          <div className={styles.galleries}>GALERIAS (coming soon)</div>
        </section>

        {showForm && (
          <ClientForm
            initialData={editData}
            onSave={(updated) => {
              setClient(updated);
              setShowForm(false);
            }}
            onClose={() => setShowForm(false)}
          />
        )}
      </main>
    </div>
  );
}
