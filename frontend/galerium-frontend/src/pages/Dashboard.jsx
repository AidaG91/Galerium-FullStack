export default function Dashboard() {
  const probarConexion = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/health');
      const text = await res.text();
      alert(text);
    } catch (err) {
      alert('Error de conexión con el backend');
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Dashboard del Fotógrafo</h1>
      <button onClick={probarConexion}>Probar conexión</button>
    </div>
  );
}
