import { useState, useEffect } from 'react';

export default function Clients() {
  const [query, setQuery] = useState('');
  const [clients, setClients] = useState([]);

  useEffect(() => {
    const delayDebounce = setTimeout(() => {
      if (query.trim().length > 0) {
        fetch(`http://localhost:8080/api/clients/name/${query}`)
          .then(res => res.json())
          .then(data => setClients(data))
          .catch(err => {
            console.error('Error when searching for clients', err);
            setClients([]); 
          });
      } else {
        setClients([]);
      }
    }, 500);

    return () => clearTimeout(delayDebounce);
  }, [query]);

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Clients</h1>
      <input
        type="text"
        placeholder="Search by name..."
        value={query}
        onChange={e => setQuery(e.target.value)}
      />
      <ul>
        {Array.isArray(clients) && clients.map(client => (
          <li key={client.id}>
            {client.fullName} – {client.email}
          </li>
        ))}
      </ul>
    </div>
  );
}
