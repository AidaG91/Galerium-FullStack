import { useState, useEffect } from "react";
import { useDebounce } from "use-debounce";
import ClientCRUD from "../components/ClientCRUD";
import styles from "../styles/ClientCRUD.module.css";

export default function ClientsPage() {
  const [query, setQuery] = useState("");
  const [clients, setClients] = useState([]);
  const [debouncedQuery] = useDebounce(query, 500);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [sortBy, setSortBy] = useState("fullName");
  const [sortDir, setSortDir] = useState("asc");

  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const controller = new AbortController();
    const signal = controller.signal;

    (async () => {
      setIsLoading(true);
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
        setTotalPages(data.totalPages ?? 0);
        setTotalElements(data.totalElements ?? 0);
      } catch (err) {
        if (err.name !== "AbortError") {
          console.error("Error fetching clients", err);
          setClients([]);
        }
      } finally {
        if (signal.aborted === false) {
          setIsLoading(false);
        }
      }
    })();

    return () => controller.abort();
  }, [debouncedQuery, page, size, sortBy, sortDir]);

  return (
    <ClientCRUD
      isLoading={isLoading}
      clients={clients}
      setClients={setClients}
      query={query}
      setQuery={setQuery}
      page={page}
      setPage={setPage}
      totalPages={totalPages}
      totalElements={totalElements}
    />
  );
}
