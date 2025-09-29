import { useState } from "react";
import styles from "../styles/Clients.module.css";

export default function ClientForm({ initialData, onSave, onClose }) {
  const isEdit = Boolean(initialData?.id);

  // --- Form state ---
  const [form, setForm] = useState({
    fullName: initialData?.fullName ?? "",
    email: initialData?.email ?? "",
    password: "",
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
      userRole: "CLIENT",
      ...(form.password &&
        form.password.trim().length >= 8 && { password: form.password.trim() }),
    };

    try {
      const res = await fetch(
        isEdit
          ? `http://localhost:8080/api/clients/${initialData.id}` // PUT
          : "http://localhost:8080/api/clients", // POST
        {
          method: isEdit ? "PUT" : "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        }
      );
      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(`HTTP ${res.status}: ${errorText}`);
      }
      const data = await res.json();
      onSave({ ...payload, id: data.id ?? initialData?.id });
    } catch (err) {
      console.error("Failed to save:", err.message);
    }
  };

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modal}>
        <h3>{isEdit ? "Editar cliente" : "Crear cliente"}</h3>
        <form onSubmit={handleSubmit}>
          {/* Full nam */}
          <input
            name="fullName"
            value={form.fullName}
            onChange={handleChange}
            placeholder="Full Name"
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

          {/* Password */}
          {!isEdit && (
            <input
              name="password"
              type="password"
              value={form.password}
              onChange={handleChange}
              placeholder="Password"
              required
            />
          )}

          {/* Phone */}
          <input
            name="phoneNumber"
            value={form.phoneNumber}
            onChange={handleChange}
            placeholder="Phone Number"
            required
          />

          {/* Address */}
          <input
            name="address"
            value={form.address}
            onChange={handleChange}
            placeholder="Address"
          />

          {/* Photo (URL) */}
          <input
            name="profilePictureUrl"
            value={form.profilePictureUrl}
            onChange={handleChange}
            placeholder="Photo (URL)"
          />

          {/* Create/Save and Cancel buttons */}
          <div className={styles.modalActions}>
            <button type="submit">{isEdit ? "Save" : "Create"}</button>
            <button type="button" onClick={onClose}>
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
