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
          <button className={styles.addBtn} onClick={() => navigate("/clients/new")}>
            Add Client
          </button>
        </div>
      </header>

      {/* ---- Client list ---- */}
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
                  <button className={styles.iconButton} 
                    title="View client"
                    onClick={() => navigate(`/clients/${c.id}`)}
                  >
                    <FaEye />
                  </button>
                  <button className={styles.iconButton}  title="Edit client" onClick={() => navigate(`/clients/${c.id}/edit`)}>
                    <FaEdit />
                  </button>
                  <button className={styles.iconButton} 
                    title="Delete client"
                    onClick={() => handleDelete(c.id)}
                  >
                    <FaTrash />
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

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
