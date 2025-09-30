import { useState, useEffect } from "react";
import { useDebounce } from "use-debounce";
import ClientCRUD from "../components/ClientCRUD";

export default function ClientsPage() {
  const [clients, setClients] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // Estados para la búsqueda y filtrado
  const [query, setQuery] = useState("");
  const [debouncedQuery] = useDebounce(query, 500);
  const [selectedTag, setSelectedTag] = useState(null);

  // Estado para la lista de todos los tags disponibles
  const [allTags, setAllTags] = useState([]);

  // Paginación
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [sortBy, setSortBy] = useState("fullName");
  const [sortDir, setSortDir] = useState("asc");

  useEffect(() => {
    (async () => {
      try {
        const res = await fetch(`http://localhost:8080/api/tags`);
        const data = await res.json();
        setAllTags(data);
      } catch (err) {
        console.error("Failed to fetch tags", err);
      }
    })();
  }, []);

  useEffect(() => {
    const controller = new AbortController();
    const signal = controller.signal;

    (async () => {
      setIsLoading(true);
      try {
        let url;
        const params = `page=${page}&size=${size}&sort=${sortBy},${sortDir}`;

        if (selectedTag) {
          // Si hay un tag seleccionado, filtramos por tag
          url = `http://localhost:8080/api/clients/by-tag?tag=${selectedTag}&${params}`;
        } else if (debouncedQuery.trim().length > 0) {
          // Si hay una búsqueda de texto, usamos el endpoint de búsqueda
          url = `http://localhost:8080/api/clients/search/paged?q=${debouncedQuery}&${params}`;
        } else {
          // Si no, la lista paginada normal
          url = `http://localhost:8080/api/clients/paged?${params}`;
        }

        const res = await fetch(url, { signal });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        setClients(data.content ?? []);
        setTotalPages(data.totalPages ?? 0);
        setTotalElements(data.totalElements ?? 0);
      } catch (err) {
        if (err.name !== "AbortError") console.error("Error fetching clients", err);
      } finally {
        if (!signal.aborted) setIsLoading(false);
      }
    })();

    return () => controller.abort();
  }, [debouncedQuery, selectedTag, page, size, sortBy, sortDir]);

  const handleSelectTag = (tag) => {
    setPage(0);
    setQuery("");
    setSelectedTag(tag);
  };
  
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
      allTags={allTags}
      selectedTag={selectedTag}
      onSelectTag={handleSelectTag}
      onClearTagFilter={() => setSelectedTag(null)}
    />
  );
}
