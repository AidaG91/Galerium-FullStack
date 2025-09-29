import { useState } from "react";
import styles from "../styles/Clients.module.css";

export default function ClientForm({ initialData, onSave, onClose }) {
  const isEdit = Boolean(initialData?.id);

  // --- Form state ---
  const [form, setForm] = useState({
    fullName: initialData?.fullName ?? "",
    email: initialData?.email ?? "",
    phoneNumber: initialData?.phoneNumber ?? "",
    address: initialData?.address ?? "",
    profilePictureUrl: initialData?.profilePictureUrl ?? "",
  });

  // --- Handlers Form ---
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value }));
  };

  // --- CREATE / UPDATE ---
  const handleSubmit = async (e) => {
    e.preventDefault();
    // Validar formulario nativo
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
      const res = await fetch(
        isEdit
          ? `http://localhost:8080/api/clients/${initialData.id}` // PUT
          : "http://localhost:8080/api/clients",                 // POST
        {
          method: isEdit ? "PUT" : "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        }
      );
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const data = await res.json();
      // Devolver al padre el cliente nuevo o actualizado
      onSave({ ...payload, id: data.id ?? initialData?.id });
    } catch (err) {
      console.error("Error al guardar", err);
    }
  };

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modal}>
        <h3>{isEdit ? "Editar cliente" : "Crear cliente"}</h3>
        <form onSubmit={handleSubmit}>
          {/* Nombre completo */}
          <input
            name="fullName"
            value={form.fullName}
            onChange={handleChange}
            placeholder="Nombre completo"
            required
          />

          {/* Email */}
          <input
            name="email"
            value={form.email}
            onChange={handleChange}
            placeholder="Email"
            required
          />

          {/* Teléfono */}
          <input
            name="phoneNumber"
            value={form.phoneNumber}
            onChange={handleChange}
            placeholder="Teléfono"
          />

          {/* Dirección */}
          <input
            name="address"
            value={form.address}
            onChange={handleChange}
            placeholder="Dirección"
          />

          {/* Foto (URL) */}
          <input
            name="profilePictureUrl"
            value={form.profilePictureUrl}
            onChange={handleChange}
            placeholder="Foto (URL)"
          />

          {/* Botones Crear/Guardar y Cancelar */}
          <div className={styles.modalActions}>
            <button type="submit">{isEdit ? "Guardar" : "Crear"}</button>
            <button type="button" onClick={onClose}>
              Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
