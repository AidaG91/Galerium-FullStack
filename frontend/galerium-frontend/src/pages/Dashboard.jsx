export default function Dashboard() {
  const probarConexion = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/health');
      const text = await res.text();
      alert(text);
    } catch (err) {
      alert('Connection error with the backend');
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Dashboard</h1>
      <button onClick={probarConexion}>Try connection</button>
    </div>
  );
}
