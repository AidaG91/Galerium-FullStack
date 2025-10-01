import { useState, useEffect } from 'react';
import { useDebounce } from 'use-debounce';
import ClientCRUD from '../components/ClientCRUD';
import { getClients, getAllTags } from '../api/clientService';

export default function ClientsPage() {
  const [clients, setClients] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // Estados para la búsqueda y filtrado
  const [query, setQuery] = useState('');
  const [debouncedQuery] = useDebounce(query, 500);
  const [selectedTag, setSelectedTag] = useState(null);

  // Estado para la lista de todos los tags disponibles
  const [allTags, setAllTags] = useState([]);

  // Paginación
  const [page, setPage] = useState(0);
  const [size, _setSize] = useState(10);
  const [sortBy, _setSortBy] = useState('fullName');
  const [sortDir, _setSortDir] = useState('asc');

  useEffect(() => {
    getAllTags()
      .then(setAllTags)
      .catch((err) => console.error('Failed to fetch tags', err));
  }, []);

  useEffect(() => {
    setIsLoading(true);

    // --> 2. Prepara los parámetros para la API
    const params = {
      page,
      size,
      sort: `${sortBy},${sortDir}`,
    };

    if (selectedTag) {
      params.tag = selectedTag;
    } else if (debouncedQuery.trim().length > 0) {
      params.q = debouncedQuery;
    }

    // --> 3. Llama a la función centralizada
    getClients(params)
      .then((data) => {
        setClients(data.content ?? []);
        setTotalPages(data.totalPages ?? 0);
        setTotalElements(data.totalElements ?? 0);
      })
      .catch((err) => {
        console.error('Error fetching clients', err);
        // Aquí podrías añadir un estado de error para mostrarlo en la UI
      })
      .finally(() => {
        setIsLoading(false);
      });
  }, [debouncedQuery, selectedTag, page, size, sortBy, sortDir]);

  const handleSelectTag = (tag) => {
    setPage(0);
    setQuery('');
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
