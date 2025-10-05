// src/components/ManageTags.jsx
import { useEffect, useState } from 'react';
import { FaTrash } from 'react-icons/fa';
import { toast } from 'react-hot-toast';
import { getAllTags, deleteTag } from '../api/clientService';
import styles from '../styles/ManageTags.module.css';
import DeleteModal from './DeleteModal';

export default function ManageTags() {
  const [tags, setTags] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [tagToDelete, setTagToDelete] = useState(null);

  // Cargar todos los tags al iniciar el componente
  useEffect(() => {
    setIsLoading(true);
    getAllTags()
      .then(setTags)
      .catch(() => toast.error('Could not fetch tags.'))
      .finally(() => setIsLoading(false));
  }, []);

  // Función para manejar el borrado
  const handleDelete = async () => {
    const tagName = tagToDelete;

    if (!tagName) return;

    setTagToDelete(null);

    /* Pedimos confirmación al usuario
    if (!window.confirm(`Are you sure you want to delete the tag "${tagName}"?`)) {
      return;
    } */

    try {
      await deleteTag(tagName);
      setTags((prevTags) => prevTags.filter((t) => t !== tagName));
      toast.success(`Tag "${tagName}" deleted successfully!`);
    } catch (error) {
      if (error.message.includes('409')) {
        toast.error(
          `Cannot delete "${tagName}" because it is currently in use.`
        );
      } else {
        toast.error('Failed to delete tag.');
      }
    }
  };

  return (
    <section className={styles.container}>
      <h2>Manage Tags</h2>
      <p>Here you can delete tags that are no longer in use.</p>

      {isLoading ? (
        <p>Loading tags...</p>
      ) : (
        <ul className={styles.tagList}>
          {tags.map((tag) => (
            <li key={tag} className={styles.tagItem}>
              <span>{tag}</span>
              <button
                className={styles.deleteButton}
                onClick={() => setTagToDelete(tag)}
                title={`Delete ${tag}`}
              >
                <FaTrash />
              </button>
            </li>
          ))}
        </ul>
      )}
      {tags.length === 0 && !isLoading && <p>No tags found.</p>}

      {tagToDelete && (
        <DeleteModal
          message={`Are you sure you want to delete the tag "${tagToDelete}"?`}
          onConfirm={handleDelete}
          onCancel={() => setTagToDelete(null)}
        />
      )}
    </section>
  );
}
