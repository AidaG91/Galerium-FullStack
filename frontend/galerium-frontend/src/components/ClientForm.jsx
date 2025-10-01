import { useState, useEffect } from "react";
import { FaTimes } from "react-icons/fa";
import styles from "../styles/ClientForm.module.css";

export default function ClientForm({
  initialData,
  onSave,
  onClose,
  allTags = [],
}) {
  const isEdit = Boolean(initialData?.id);

  // --- Form state ---
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    password: "",
    phoneNumber: "",
    address: "",
    profilePictureUrl: "",
    tags: [],
    internalNotes: "",
  });

  const [errors, setErrors] = useState({});
  const [success, setSuccess] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState(null);
  const [imageError, setImageError] = useState(false);
  const [currentTag, setCurrentTag] = useState("");
  const [tagSuggestions, setTagSuggestions] = useState([]);

  useEffect(() => {
    if (initialData) {
      setForm({
        fullName: initialData.fullName ?? "",
        email: initialData.email ?? "",
        password: "",
        phoneNumber: initialData.phoneNumber ?? "",
        address: initialData.address ?? "",
        profilePictureUrl: initialData.profilePictureUrl ?? "",
        tags: initialData.tags ?? [],
        internalNotes: initialData.internalNotes ?? "",
      });
    }
  }, [initialData]);

  // --- Handlers Form ---
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value }));
  };

  // --- Tag Handling Logic ---
  const handleTagInputChange = (e) => {
    const value = e.target.value;
    setCurrentTag(value);

    if (value.trim() === "") {
      setTagSuggestions([]);
      return;
    }

    // Filtra las sugerencias
    const suggestions = allTags
      .filter((tag) => tag.toLowerCase().includes(value.toLowerCase()))
      .filter((tag) => !form.tags.includes(tag));

    setTagSuggestions(suggestions);
  };

  // Función para cuando se selecciona una sugerencia
  const handleSelectTag = (tag) => {
    if (!form.tags.includes(tag)) {
      setForm((f) => ({ ...f, tags: [...f.tags, tag] }));
    }
    setCurrentTag("");
    setTagSuggestions([]);
  };

  const handleAddTag = (e) => {
    if (e.key === "Enter" && currentTag.trim() !== "") {
      e.preventDefault();
      const newTag = currentTag.trim();
      if (!form.tags.includes(newTag)) {
        setForm((f) => ({ ...f, tags: [...f.tags, newTag] }));
      }
      setCurrentTag("");
      setTagSuggestions([]);
    }
  };

  const handleRemoveTag = (tagToRemove) => {
    setForm((f) => ({
      ...f,
      tags: f.tags.filter((tag) => tag !== tagToRemove),
    }));
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
      tags: form.tags,
      internalNotes: form.internalNotes.trim(),
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
    setIsSubmitting(true);
    setSubmitError(null);

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
      setSubmitError("Failed to save client. Please try again later.");
    } finally {
      if (!success) {
        setIsSubmitting(false);
      }
    }
  };

  return (
    <div className={styles.formWrapper}>
      <h3>{isEdit ? "Edit client" : "Create client"}</h3>

      <form onSubmit={handleSubmit} className={styles.form} noValidate>
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
          disabled={isSubmitting}
        />

        {form.profilePictureUrl && (
          <div className={styles.imagePreviewWrapper}>
            {imageError ? (
              <div className={styles.imageErrorPlaceholder}>
                <span>Image not available</span>
              </div>
            ) : (
              <img
                src={form.profilePictureUrl}
                alt="Client Preview"
                className={styles.previewImage}
                onError={() => setImageError(true)}
                onLoad={() => setImageError(false)}
              />
            )}
          </div>
        )}

        <textarea
          name="internalNotes"
          value={form.internalNotes}
          onChange={handleChange}
          placeholder="Internal notes about the client..."
          className={styles.textarea}
          rows="4"
          disabled={isSubmitting}
        />

        <div className={styles.tagInputWrapper}>
          <label htmlFor="tags">Tags</label>
          <div className={styles.tagsContainer}>
            {form.tags.map((tag) => (
              <div key={tag} className={styles.tag}>
                {tag}
                <button type="button" onClick={() => handleRemoveTag(tag)}>
                  <FaTimes />
                </button>
              </div>
            ))}
          </div>

          <div className={styles.autocompleteWrapper}>
            <input
              id="tags"
              type="text"
              value={currentTag}
              onChange={handleTagInputChange}
              onKeyDown={handleAddTag}
              placeholder="Type to add or search tags..."
              autoComplete="off"
              disabled={isSubmitting}
            />

            {tagSuggestions.length > 0 && (
              <ul className={styles.suggestionsList}>
                {tagSuggestions.map((suggestion) => {
                  const matchStart = suggestion
                    .toLowerCase()
                    .indexOf(currentTag.toLowerCase());
                  const matchEnd = matchStart + currentTag.length;
                  const beforeMatch = suggestion.slice(0, matchStart);
                  const matchText = suggestion.slice(matchStart, matchEnd);
                  const afterMatch = suggestion.slice(matchEnd);

                  return (
                    <li
                      key={suggestion}
                      onClick={() => handleSelectTag(suggestion)}
                    >
                      {beforeMatch}
                      <strong>{matchText}</strong>
                      {afterMatch}
                    </li>
                  );
                })}
              </ul>
            )}
          </div>
        </div>

        {/* Muestra el error de envío si existe */}
        {submitError && <p className={styles.submitErrorText}>{submitError}</p>}

        {/* Create/Save and Cancel buttons */}
        <div className={styles.formActions}>
          <button
            type="submit"
            className="btn btn--primary" // <-- Clase global
            disabled={isSubmitting}
          >
            {isSubmitting ? "Saving..." : isEdit ? "Save" : "Create"}
          </button>
          <button
            type="button"
            className="btn btn--secondary" // <-- Clase global
            onClick={onClose}
            disabled={isSubmitting}
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
