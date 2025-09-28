import { useState, useEffect } from "react";
import ClientCRUD from "../components/ClientCRUD";

export default function Clients() {
  const [query, setQuery] = useState("");
  const [clients, setClients] = useState([]);

  useEffect(() => {
    const delayDebounce = setTimeout(() => {
      if (query.trim().length > 0) {
        fetch(`http://localhost:8080/api/clients/name/${query}`)
          .then((res) => res.json())
          .then((data) => setClients(data))
          .catch((err) => {
            console.error("Error when searching for clients", err);
            setClients([]);
          });
      } else {
        fetch("http://localhost:8080/api/clients")
          .then((res) => res.json())
          .then((data) => setClients(data))
          .catch((err) => {
            console.error("Error loading clients", err);
            setClients([]);
          });
      }
    }, 500);

    return () => clearTimeout(delayDebounce);
  }, [query]);

  return (
    <ClientCRUD
      clients={clients}
      query={query}
      setQuery={setQuery}
      setClients={setClients}
    />
  );
}
