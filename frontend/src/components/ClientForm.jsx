import { useState, useEffect } from 'react';
import { FaTimes } from 'react-icons/fa';
import styles from '../styles/ClientForm.module.css';
import { createClient, updateClient } from '../api/clientService';

const isEmailValid = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

export default function ClientForm({
  initialData,
  onSave,
  onClose,
  allTags = [],
}) {
  const isEdit = Boolean(initialData?.id);

  const [form, setForm] = useState({
    fullName: '',
    email: '',
    password: '',
    phoneNumber: '',
    address: '',
    profilePictureUrl: '',
    tags: [],
    internalNotes: '',
  });

  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState(null);
  const [imageError, setImageError] = useState(false);
  const [currentTag, setCurrentTag] = useState('');
  const [tagSuggestions, setTagSuggestions] = useState([]);
  const [highlightedIndex, setHighlightedIndex] = useState(-1);

  useEffect(() => {
    if (initialData) {
      setForm({
        fullName: initialData.fullName ?? '',
        email: initialData.email ?? '',
        password: '',
        phoneNumber: initialData.phoneNumber ?? '',
        address: initialData.address ?? '',
        profilePictureUrl: initialData.profilePictureUrl ?? '',
        tags: initialData.tags ?? [],
        internalNotes: initialData.internalNotes ?? '',
      });
    }
  }, [initialData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value }));
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: null }));
    }
  };

  const handleTagInputChange = (e) => {
    const value = e.target.value;
    setCurrentTag(value);

    if (value.trim() === '') {
      setTagSuggestions([]);
      return;
    }

    const suggestions = allTags
      .filter((tag) => tag.toLowerCase().includes(value.toLowerCase()))
      .filter((tag) => !form.tags.includes(tag));

    setTagSuggestions(suggestions);
  };

  const handleSelectTag = (tag) => {
    if (!form.tags.includes(tag)) {
      setForm((f) => ({ ...f, tags: [...f.tags, tag] }));
    }
    setCurrentTag('');
    setTagSuggestions([]);
  };

  const handleAddTag = (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();

      if (highlightedIndex >= 0 && tagSuggestions[highlightedIndex]) {
        handleSelectTag(tagSuggestions[highlightedIndex]);
      } else if (currentTag.trim() !== '') {
        const newTag = currentTag.trim();
        if (!form.tags.includes(newTag)) {
          setForm((f) => ({ ...f, tags: [...f.tags, newTag] }));
        }
        setCurrentTag('');
        setTagSuggestions([]);
      }
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'ArrowDown') {
      e.preventDefault();
      setHighlightedIndex((prev) =>
        prev < tagSuggestions.length - 1 ? prev + 1 : 0
      );
    } else if (e.key === 'ArrowUp') {
      e.preventDefault();
      setHighlightedIndex((prev) =>
        prev > 0 ? prev - 1 : tagSuggestions.length - 1
      );
    } else {
      handleAddTag(e);
    }
  };

  const handleRemoveTag = (tagToRemove) => {
    setForm((f) => ({
      ...f,
      tags: f.tags.filter((tag) => tag !== tagToRemove),
    }));
  };

  const handleBlur = (e) => {
    const { name, value } = e.target;
    if (name === 'email' && value.trim() !== '' && !isEmailValid(value)) {
      setErrors((prev) => ({
        ...prev,
        email: 'Please enter a valid email format.',
      }));
    }
    if (
      !isEdit &&
      name === 'password' &&
      value.trim() !== '' &&
      value.trim().length < 8
    ) {
      setErrors((prev) => ({
        ...prev,
        password: 'Password must be at least 8 characters',
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const payload = {
      fullName: form.fullName.trim(),
      email: form.email.trim(),
      phoneNumber: form.phoneNumber.trim(),
      address: form.address.trim(),
      profilePictureUrl: form.profilePictureUrl.trim(),
      userRole: 'CLIENT',
      tags: form.tags,
      internalNotes: form.internalNotes.trim(),
      ...(form.password &&
        form.password.trim().length >= 8 && { password: form.password.trim() }),
    };

    const newErrors = {};
    if (!form.fullName.trim()) newErrors.fullName = 'Name is required';
    if (!form.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!isEmailValid(form.email)) {
      newErrors.email = 'Please enter a valid email format.';
    }
    if (!isEdit && (!form.password || form.password.trim().length < 8))
      newErrors.password = 'Password must be at least 8 characters';
    if (!form.phoneNumber.trim()) newErrors.phoneNumber = 'Phone is required';

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    setErrors({});
    setIsSubmitting(true);
    setSubmitError(null);

    try {
      const saveAction = isEdit
        ? updateClient(initialData.id, payload)
        : createClient(payload);

      const savedClient = await saveAction;

      onSave(savedClient);
    } catch (err) {
      console.error('Failed to save:', err.message);
      setSubmitError('Failed to save client. Please try again later.');
    } finally {
      if (!success) {
        setIsSubmitting(false);
      }
    }
  };

  return (
    <div className={styles.formWrapper}>
      <h3>{isEdit ? 'Edit client' : 'Create client'}</h3>

      <form onSubmit={handleSubmit} className={styles.form} noValidate>
        {/* Full name */}
        <input
          name="fullName"
          value={form.fullName}
          onChange={handleChange}
          placeholder="Full Name"
          className={errors.fullName ? styles.inputError : ''}
        />
        {errors.fullName && (
          <p className={styles.errorText}>{errors.fullName}</p>
        )}

        {/* Email */}
        <input
          name="email"
          value={form.email}
          onChange={handleChange}
          onBlur={handleBlur}
          placeholder="Email"
          className={errors.email ? styles.inputError : ''}
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
              onBlur={handleBlur}
              placeholder="Password"
              className={errors.password ? styles.inputError : ''}
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
          className={errors.phoneNumber ? styles.inputError : ''}
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

        {/* Internal Notes */}
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
              onKeyDown={handleKeyDown}
              placeholder="Type to add or search tags..."
              autoComplete="off"
              disabled={isSubmitting}
            />

            {tagSuggestions.length > 0 && (
              <ul className={styles.suggestionsList}>
                {tagSuggestions.map((suggestion, index) => {
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
                      className={
                        index === highlightedIndex ? styles.highlighted : ''
                      }
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

        {submitError && <p className={styles.submitErrorText}>{submitError}</p>}

        {/* Create/Save and Cancel buttons */}
        <div className={styles.formActions}>
          <button
            type="submit"
            className="btn btn--primary"
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Saving...' : isEdit ? 'Save' : 'Create'}
          </button>
          <button
            type="button"
            className="btn btn--secondary"
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
