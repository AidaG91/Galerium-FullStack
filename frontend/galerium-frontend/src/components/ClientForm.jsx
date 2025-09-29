import { useState, useEffect } from "react";
import styles from "../styles/Clients.module.css";

export default function ClientForm({ initialData, onSave, onClose }) {
  const isEdit = Boolean(initialData?.id);

  // --- Form state ---
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    password: "",
    phoneNumber: "",
    address: "",
    profilePictureUrl: "",
  });

  useEffect(() => {
    if (initialData) {
      setForm({
        fullName: initialData.fullName ?? "",
        email: initialData.email ?? "",
        password: "",
        phoneNumber: initialData.phoneNumber ?? "",
        address: initialData.address ?? "",
        profilePictureUrl: initialData.profilePictureUrl ?? "",
      });
    }
  }, [initialData]);

  const [errors, setErrors] = useState({});
  const [success, setSuccess] = useState(false);

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

    const newErrors = {};
    if (!form.fullName.trim()) newErrors.fullName = "Name is required";
    if (!form.email.trim()) newErrors.email = "Email is required";
    if (!isEdit && form.password.trim().length < 8)
      newErrors.password = "Password must be at least 8 characters";
    if (!form.phoneNumber.trim()) newErrors.phoneNumber = "Phone is required";

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    setErrors({});

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

      const newId = data.id ?? initialData?.id;
      setSuccess(true);
      setTimeout(() => {
        onSave({ ...payload, id: newId });
      }, 1000);
    } catch (err) {
      console.error("Failed to save:", err.message);
    }
  };

  return (
    <div className={styles.formWrapper}>
      <h3>{isEdit ? "Edit client" : "Create client"}</h3>

      <form onSubmit={handleSubmit} className={styles.form}>
        {success && (
          <p className={styles.successText}>✅ Client saved successfully!</p>
        )}
        {/* Full name */}
        <input
          name="fullName"
          value={form.fullName}
          onChange={handleChange}
          placeholder="Full Name"
          className={errors.fullName ? styles.inputError : ""}
        />
        {errors.fullName && (
          <p className={styles.errorText}>{errors.fullName}</p>
        )}

        {/* Email */}
        <input
          name="email"
          value={form.email}
          onChange={handleChange}
          placeholder="Email"
        />
        {errors.email && <p className={styles.errorText}>{errors.email}</p>}

        {/* Password */}
        {!isEdit && (
          <>
            <input
              name="password"
              type="password"
              value={form.password}
              onChange={handleChange}
              placeholder="Password"
              className={errors.password ? styles.inputError : ""}
            />
            {errors.password && (
              <p className={styles.errorText}>{errors.password}</p>
            )}
          </>
        )}

        {/* Phone */}
        <input
          name="phoneNumber"
          value={form.phoneNumber}
          onChange={handleChange}
          placeholder="Phone Number"
          className={errors.phoneNumber ? styles.inputError : ""}
        />
        {errors.phoneNumber && (
          <p className={styles.errorText}>{errors.phoneNumber}</p>
        )}

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
  );
}
