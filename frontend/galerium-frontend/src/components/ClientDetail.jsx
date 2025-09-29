import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { FaEdit, FaTrash } from "react-icons/fa";
import styles from "../styles/ClientDetail.module.css";
import DeleteModal from "./DeleteModal";

export default function ClientDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [client, setClient] = useState(null);
  const [showConfirm, setShowConfirm] = useState(false);

  const handleDelete = async (id) => {
    try {
      await fetch(`http://localhost:8080/api/clients/${id}`, {
        method: "DELETE",
      });
      navigate("/clients");
    } catch (err) {
      console.error("Error deleting client", err);
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
              <button
                title="Edit"
                onClick={() => navigate(`/clients/${client.id}/edit`)}
              >
                <FaEdit />
              </button>
              <button title="Delete" onClick={() => setShowConfirm(true)}>
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

        {showConfirm && (
          <DeleteModal
            message="Are you sure you want to delete this client?"
            onConfirm={() => handleDelete(client.id)}
            onCancel={() => setShowConfirm(false)}
          />
        )}
      </main>
    </div>
  );
}
