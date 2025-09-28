import { useEffect, useMemo, useState } from "react";
import { FaEye, FaEdit, FaTrash } from "react-icons/fa";
import styles from "../styles/Clients.module.css";

const API = "http://localhost:8080/api/clients";

const cap = (s) => (s ? s.charAt(0).toUpperCase() + s.slice(1) : s);

function toClient(c) {
  return {
    id: c.id,
    fullName: cap(c.fullName ?? "No name"),
    email: c.email,
    phoneNumber: c.phoneNumber,
    address: cap(c.address ?? "No address"),
    profilePictureUrl: c.profilePictureUrl,
    isEnabled: c.isEnabled,
    registrationDate: c.registrationDate,
  };
}

export default function ClientCRUD() {
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // --- Filters / order ---
  const [query, setQuery] = useState("");
  const [sortBy, setSortBy] = useState("fullName"); // fullName | email | phoneNumber
  const [sortDir, setSortDir] = useState("desc"); // asc | desc
  const [isEnabled, setIsEnabled] = useState(true);

  // --- Form (Create/Update) ---
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    phoneNumber: "",
    address: "",
    profilePictureUrl: "",
  });

  // --- Modal ---
  const [showModal, setShowModal] = useState(false);
  const openCreateModal = () => {
    startCreate();
    setShowModal(true);
  };

  // --- READ: First load ---
  useEffect(() => {
    let canceled = false;
    (async () => {
      try {
        const res = await fetch(`${API}?_limit=12`, { cache: "no-store" });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        if (canceled) return;
        setClients((Array.isArray(data) ? data : []).map(toClient));
        setError(null);
      } catch (e) {
        if (!canceled) setError(e.message || "Error loading the API");
      } finally {
        if (!canceled) setLoading(false);
      }
    })();
    return () => {
      canceled = true;
    };
  }, []);

  // --- Derived values ---
  const filteredSorted = useMemo(() => {
    const normalizedQuery = query.trim().toLowerCase();

    const filtered = clients.filter(
      (c) =>
        c.fullName.toLowerCase().includes(normalizedQuery) ||
        c.address.toLowerCase().includes(normalizedQuery) ||
        c.phoneNumber.toLowerCase().includes(normalizedQuery) ||
        c.email.toLowerCase().includes(normalizedQuery)
    );

    const sorted = [...filtered].sort((a, b) => {
      let comp = 0;
      if (sortBy === "fullName") comp = a.fullName.localeCompare(b.fullName);
      else if (sortBy === "email") comp = a.email.localeCompare(b.email);
      else if (sortBy === "phoneNumber")
        comp = a.phoneNumber.localeCompare(b.phoneNumber);
      else if (sortBy === "address") comp = a.address.localeCompare(b.address);
      return sortDir === "asc" ? comp : -comp;
    });

    return sorted;
  }, [clients, query, sortBy, sortDir]);

  // --- Handlers Form ---
  const startCreate = () => {
    setEditingId(null);
    setForm({
      fullName: "",
      email: "",
      phoneNumber: "",
      address: "",
      profilePictureUrl: "",
    });
  };

  const startEdit = (c) => {
    setEditingId(c.id);
    setForm({
      fullName: c.fullName ?? "",
      email: c.email ?? "",
      phoneNumber: c.phoneNumber ?? "",
      address: c.address ?? "",
      profilePictureUrl: c.profilePictureUrl ?? "",
    });
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value }));
  };

  // --- CREATE / UPDATE ---
  const handleSubmit = async (e) => {
    e.preventDefault();
    const formEl = e.currentTarget;
    if (!formEl.checkValidity()) {
      formEl.reportValidity();
      return;
    }

    const payload = {
      fullName: form.fullName.trim(),
      email: form.email.trim(),
      phoneNumber: form.phoneNumber.trim(),
      address: form.address.trim(),
      profilePictureUrl: form.profilePictureUrl.trim(),
    };

    try {
      if (editingId == null) {
        // CREATE
        const res = await fetch(API, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const created = await res.json();
        const newClient = toClient({
          ...payload,
          id: created.id ?? Date.now(),
        });
        setClients((prev) => [newClient, ...prev]);
      } else {
        // UPDATE
        const res = await fetch(`${API}/${editingId}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const updated = await res.json();
        setClients((prev) =>
          prev.map((c) =>
            c.id === editingId ? toClient({ ...c, ...updated }) : c
          )
        );
      }
      // reset form
      setEditingId(null);
      setForm({
        fullName: "",
        email: "",
        phoneNumber: "",
        address: "",
        profilePictureUrl: "",
      });
      setError(null);
    } catch (e2) {
      setError(e2.message || "Failed to send data");
    }
  };

  // --- DELETE ---
  const handleDelete = async (id) => {
    if (!confirm("Are you sure you want to delete this client?")) return;
    try {
      const res = await fetch(`${API}/${id}`, { method: "DELETE" });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      setClients((prev) => prev.filter((c) => c.id !== id));
    } catch (e2) {
      setError(e2.message || "Failed to delete");
    }
  };

  if (loading) return <p className={styles.status}>Loading...</p>;
  if (error) return <p className={styles.error}>Error: {error}</p>;

  // --- JSX Structure ---
  return (
    <section className={styles.wrapper}>
      {/* ---- Header ---- */}
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
                title="Ver ficha"
                onClick={() => console.log("Ver", c.id)}
              >
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

      {/* ---- Modal ---- */}
      {showModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <h3>{editingId == null ? "Crear cliente" : "Editar cliente"}</h3>
            <form onSubmit={handleSubmit}>
              <input
                name="fullName"
                value={form.fullName}
                onChange={handleChange}
                placeholder="Nombre completo"
                required
              />
              <input
                name="email"
                value={form.email}
                onChange={handleChange}
                placeholder="Email"
                required
              />
              <input
                name="phoneNumber"
                value={form.phoneNumber}
                onChange={handleChange}
                placeholder="Teléfono"
              />
              <input
                name="address"
                value={form.address}
                onChange={handleChange}
                placeholder="Dirección"
              />
              <input
                name="profilePictureUrl"
                value={form.profilePictureUrl}
                onChange={handleChange}
                placeholder="Foto (URL)"
              />
              <div className={styles.modalActions}>
                <button type="submit">
                  {editingId == null ? "Crear" : "Guardar"}
                </button>
                <button type="button" onClick={() => setShowModal(false)}>
                  Cancelar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </section>
  );
}
