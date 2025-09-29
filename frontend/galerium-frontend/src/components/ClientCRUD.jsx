import { useState } from "react";
import { FaEye, FaEdit, FaTrash } from "react-icons/fa";
import styles from "../styles/Clients.module.css";
import ClientForm from "./ClientForm";

export default function ClientCRUD({ clients, setClients, query, setQuery }) {
  // --- Modal & Form (Create/Update) ---
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState(null);
  const [showModal, setShowModal] = useState(false);

  // Abrir modal para crear
  const openCreateModal = () => {
    setEditingId(null);
    setFormData(null);
    setShowModal(true);
  };

  // Abrir modal para editar
  const startEdit = (c) => {
    setEditingId(c.id);
    setFormData(c);
    window.scrollTo({ top: 0, behavior: "smooth" });
    setShowModal(true);
  };

  // --- DELETE ---
  const handleDelete = async (id) => {
    if (!confirm("¿Eliminar cliente?")) return;
    try {
      const res = await fetch(`http://localhost:8080/api/clients/${id}`, {
        method: "DELETE",
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      // Actualizar estado local tras borrado
      setClients((prev) => prev.filter((c) => c.id !== id));
    } catch (err) {
      console.error("Error al eliminar", err);
    }
  };

  // --- JSX Structure ---
  return (
    <section className={styles.wrapper}>
      {/* ---- Header: buscador + botón ADD ---- */}
      <header className={styles.header}>
        <h2>Clientes</h2>
        <div className={styles.actions}>
          <input
            className={styles.input}
            placeholder="Buscar cliente…"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
          <button className={styles.addBtn} onClick={openCreateModal}>
            + ADD
          </button>
        </div>
      </header>

      {/* ---- Client list: tarjetas de cliente ---- */}
      <ul className={styles.list}>
        {clients.map((c) => (
          <li key={c.id} className={styles.card}>
            <div className={styles.info}>
              <strong>{c.fullName}</strong>
              <p>{c.email}</p>
              <p>{c.phoneNumber}</p>
            </div>
            <div className={styles.actions}>
              <button title="Ver ficha" onClick={() => console.log("Ver", c.id)}>
                <FaEye />
              </button>
              <button title="Editar" onClick={() => startEdit(c)}>
                <FaEdit />
              </button>
              <button title="Eliminar" onClick={() => handleDelete(c.id)}>
                <FaTrash />
              </button>
            </div>
          </li>
        ))}
      </ul>

      {/* ---- Modal: formulario de creación/edición ---- */}
      {showModal && (
        <ClientForm
          initialData={formData}
          onClose={() => setShowModal(false)}
          onSave={(client) => {
            if (editingId) {
              // UPDATE local
              setClients((prev) =>
                prev.map((c) => (c.id === editingId ? client : c))
              );
            } else {
              // CREATE local
              setClients((prev) => [client, ...prev]);
            }
            setShowModal(false);
          }}
        />
      )}
    </section>
  );
}
