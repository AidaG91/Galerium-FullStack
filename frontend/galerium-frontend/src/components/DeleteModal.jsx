import styles from "../styles/ConfirmModal.module.css";

export default function ConfirmModal({ message, onConfirm, onCancel }) {
  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modal}>
        <p>{message}</p>
        <div className={styles.modalActions}>
          <button onClick={onConfirm} className={styles.dangerBtn}>
            Yes, delete
          </button>
          <button onClick={onCancel}>Cancel</button>
        </div>
      </div>
    </div>
  );
}
