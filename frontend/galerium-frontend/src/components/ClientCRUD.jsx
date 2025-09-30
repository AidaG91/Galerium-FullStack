import { useState } from "react";
import { FaEye, FaEdit, FaTrash } from "react-icons/fa";
import styles from "../styles/ClientCRUD.module.css";
import { useNavigate } from "react-router-dom";
import DeleteModal from "./DeleteModal";

export default function ClientCRUD({
  isLoading,
  clients,
  setClients,
  query,
  setQuery,
  page,
  setPage,
  totalPages,
  totalElements,
}) {
  const navigate = useNavigate();
  const [confirmId, setConfirmId] = useState(null);

  const handleDelete = async (id) => {
    try {
      const res = await fetch(`http://localhost:8080/api/clients/${id}`, {
        method: "DELETE",
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      setClients((prev) => prev.filter((c) => c.id !== id));
    } catch (err) {
      console.error("Failed to delete client", err);
    } finally {
      setConfirmId(null);
    }
  };

  return (
    <section className={styles.wrapper}>
      <header className={styles.header}>
        <h2>Clients</h2>
        <div className={styles.actions}>
          <input
            className={styles.searchInput}
            placeholder="Search client…"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
          <button
            className="btn btn--primary"
            onClick={() => navigate("/clients/new")}
          >
            Add Client
          </button>
        </div>
      </header>

      <div className={styles.tableWrapper}>
        {isLoading && (
          <div className={styles.loadingOverlay}>
            <div className={styles.spinner}></div>
          </div>
        )}

      <table className={styles.table}>
        <thead>
          <tr>
            <th>Name</th>
            <th>Phone</th>
            <th>Email</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {clients.map((c) => (
            <tr key={c.id}>
              <td>{c.fullName}</td>
              <td className={styles.phone}>{c.phoneNumber}</td>
              <td>{c.email}</td>
              <td className={styles.actionsCell}>
                <div className={styles.actions}>
                  <button
                    className={styles.iconButton}
                    title="View client"
                    onClick={() => navigate(`/clients/${c.id}`)}
                  >
                    <FaEye />
                  </button>
                  <button
                    className={styles.iconButton}
                    title="Edit client"
                    onClick={() => navigate(`/clients/${c.id}/edit`)}
                  >
                    <FaEdit />
                  </button>
                  
                  <button
                    className={`${styles.iconButton} ${styles.delete}`}
                    title="Delete client"
                    onClick={() => setConfirmId(c.id)}
                  >
                    <FaTrash />
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
</div>

      {!isLoading && clients.length === 0 && (
        <p className={styles.noResults}>
          No clients found. Try a different search or add a new one.
        </p>
      )}

      <div className={styles.pagination}>
        <p>
          Showing page <strong>{page + 1}</strong> of{" "}
          <strong>{totalPages}</strong> ({totalElements} clients)
        </p>
        <div className={styles.pageButtons}>
          <button
            className="btn btn--secondary"
            disabled={page === 0}
            onClick={() => setPage((p) => p - 1)}
          >
            ← Previous
          </button>
          <button
            className="btn btn--secondary"
            disabled={page >= totalPages - 1}
            onClick={() => setPage((p) => p + 1)}
          >
            Next →
          </button>
        </div>
      </div>

      {confirmId && (
        <DeleteModal
          message="Are you sure you want to delete this client?"
          onConfirm={() => handleDelete(confirmId)}
          onCancel={() => setConfirmId(null)}
        />
      )}
    </section>
  );
}