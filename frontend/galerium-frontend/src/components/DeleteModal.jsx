import styles from '../styles/DeleteModal.module.css';
import { FaExclamationTriangle } from 'react-icons/fa';

export default function DeleteModal({ message, onConfirm, onCancel }) {
  const handleOverlayClick = (e) => {
    if (e.target === e.currentTarget) {
      onCancel();
    }
  };

  return (
    <div className={styles.modalOverlay} onClick={handleOverlayClick}>
      <div className={styles.modal}>
        <div className={styles.iconWrapper}>
          <FaExclamationTriangle />
        </div>

        <p>{message}</p>
        <div className={styles.modalActions}>
          <button onClick={onConfirm} className="btn btn--danger">
            Yes, delete
          </button>
          <button onClick={onCancel} className="btn btn--secondary">
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
}
