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

  // Stats para el codigo
    const stats = useMemo(() => {
    if (!clients.length) return { count: 0, latest: null };

    const sortedByDate = [...clients].sort(
      (a, b) => new Date(b.registrationDate) - new Date(a.registrationDate)
    );

    return {
      count: clients.length,
      latest: sortedByDate[0],
    };
  }, [clients]);
}
