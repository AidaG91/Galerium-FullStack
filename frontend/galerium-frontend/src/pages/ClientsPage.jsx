import { useState, useEffect } from "react";
import { useDebounce } from "use-debounce";
import ClientCRUD from "../components/ClientCRUD";

export default function ClientsPage() {
  const [query, setQuery] = useState("");
  const [clients, setClients] = useState([]);
  const [debouncedQuery] = useDebounce(query, 500);

  // --- Paginación y orden ---
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [sortBy, setSortBy] = useState("fullName");
  const [sortDir, setSortDir] = useState("asc");

  useEffect(() => {
    const controller = new AbortController();
    const signal = controller.signal;

    (async () => {
      try {
        const url =
          debouncedQuery.trim().length > 0
            ? `http://localhost:8080/api/clients/search/paged?q=${debouncedQuery}&page=${page}&size=${size}&sort=${sortBy},${sortDir}`
            : `http://localhost:8080/api/clients/paged?page=${page}&size=${size}&sort=${sortBy},${sortDir}`;

        console.log("Fetching:", url);
        const res = await fetch(url, { signal });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        setClients(data.content ?? []);
      } catch (err) {
        if (err.name !== "AbortError") {
          console.error("Error fetching clients", err);
          setClients([]);
        }
      }
    })();

    return () => controller.abort();
  }, [debouncedQuery, page, size, sortBy, sortDir]);

  return (
    <ClientCRUD
      clients={clients}
      setClients={setClients}
      query={query}
      setQuery={setQuery}
    />
  );
}
