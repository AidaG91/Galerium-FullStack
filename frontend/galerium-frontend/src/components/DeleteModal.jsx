import styles from "../styles/DeleteModal.module.css";

export default function DeleteModal({ message, onConfirm, onCancel }) {
return (
    <div className={styles.modalOverlay}>
      <div className={styles.modal}>
        <p>{message}</p>
        <div className={styles.modalActions}>
          {/* BOTÓN DE CONFIRMAR ACTUALIZADO */}
          <button onClick={onConfirm} className="btn btn--danger">
            Yes, delete
          </button>
          {/* BOTÓN DE CANCELAR ACTUALIZADO */}
          <button onClick={onCancel} className="btn btn--secondary">
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
}
