import { useState } from "react";
import { FaEye, FaEdit, FaTrash } from "react-icons/fa";
import styles from "../styles/Clients.module.css";
import ClientForm from "./ClientForm";
import { useNavigate } from "react-router-dom";

export default function ClientCRUD({
  clients,
  setClients,
  query,
  setQuery,
  page,
  setPage,
  totalPages,
  totalElements,
}) {
  // --- Modal & Form (Create/Update) ---
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState(null);
  const [showModal, setShowModal] = useState(false);

  // Open modal to create
  const openCreateModal = () => {
    setEditingId(null);
    setFormData(null);
    setShowModal(true);
  };

  // Open modal to editar
  const startEdit = (c) => {
    setEditingId(c.id);
    setFormData(c);
    window.scrollTo({ top: 0, behavior: "smooth" });
    setShowModal(true);
  };

  // Routes
  const navigate = useNavigate();

  // --- DELETE ---
  const handleDelete = async (id) => {
    if (!confirm("Delete client?")) return;
    try {
      const res = await fetch(`http://localhost:8080/api/clients/${id}`, {
        method: "DELETE",
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);

      setClients((prev) => prev.filter((c) => c.id !== id));
    } catch (err) {
      console.error("Failed to delete client", err);
    }
  };

  // --- JSX Structure ---
  return (
    <section className={styles.wrapper}>
      {/* ---- Header: search + ADD button ---- */}
      <header className={styles.header}>
        <h2>Clients</h2>
        <div className={styles.actions}>
          <input
            className={styles.input}
            placeholder="Search client…"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
          <button className={styles.addBtn} onClick={openCreateModal}>
            + ADD
          </button>
        </div>
      </header>

      {/* ---- Client list ---- */}
      <ul className={styles.list}>
        {clients.map((c) => (
          <li key={c.id} className={styles.card}>
            <div className={styles.info}>
              <strong>{c.fullName}</strong>
              <p>{c.email}</p>
              <p>{c.phoneNumber}</p>
            </div>
            <div className={styles.actions}>
              <button
                title="See client"
                onClick={() => navigate(`/clients/${c.id}`)}
              >
                <FaEye />
              </button>
              <button title="Edit" onClick={() => startEdit(c)}>
                <FaEdit />
              </button>
              <button title="Delete" onClick={() => handleDelete(c.id)}>
                <FaTrash />
              </button>
            </div>
          </li>
        ))}
      </ul>

      {/* ---- Modal: create / update ---- */}
      {showModal && (
        <ClientForm
          initialData={formData}
          onClose={() => setShowModal(false)}
          onSave={(client) => {
            if (editingId) {
              setClients((prev) =>
                prev.map((c) => (c.id === editingId ? client : c))
              );
            } else {
              setClients((prev) => [client, ...prev]);
            }
            setShowModal(false);
          }}
        />
      )}

      {/* Next / Previous Page buttons */}
      <div className={styles.pagination}>
        <p>
          Showing page <strong>{page + 1}</strong> of{" "}
          <strong>{totalPages}</strong> (<strong>{totalElements}</strong>{" "}
          clients)
        </p>
        <div className={styles.pageButtons}>
          <button disabled={page === 0} onClick={() => setPage((p) => p - 1)}>
            ← Previous
          </button>
          <button
            disabled={page >= totalPages - 1}
            onClick={() => setPage((p) => p + 1)}
          >
            Next →
          </button>
        </div>
      </div>
    </section>
  );
}
