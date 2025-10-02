import { useState, useEffect } from 'react';
import { useDebounce } from 'use-debounce';
import ClientCRUD from '../components/ClientCRUD';
import { getClients, getAllTags } from '../api/clientService';

export default function ClientsPage() {
  const [clients, setClients] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const [query, setQuery] = useState('');
  const [debouncedQuery] = useDebounce(query, 500);
  const [selectedTags, setSelectedTags] = useState([]);

  const [allTags, setAllTags] = useState([]);

  const [page, setPage] = useState(0);
  const [size, _setSize] = useState(10);
  const [sortBy, _setSortBy] = useState('full_name');
  const [sortDir, _setSortDir] = useState('asc');

  useEffect(() => {
    getAllTags()
      .then(setAllTags)
      .catch((err) => console.error('Failed to fetch tags', err));
  }, []);

  useEffect(() => {
    setIsLoading(true);

    const params = {
      page,
      size,
      sort: `${sortBy},${sortDir}`,
    };

    if (selectedTags.length > 0) {
      params.tag = selectedTags;
    } else if (debouncedQuery.trim().length > 0) {
      params.q = debouncedQuery;
    }

    getClients(params)
      .then((data) => {
        setClients(data.content ?? []);
        setTotalPages(data.totalPages ?? 0);
        setTotalElements(data.totalElements ?? 0);
      })
      .catch((err) => {
        console.error('Error fetching clients', err);
      })
      .finally(() => {
        setIsLoading(false);
      });
  }, [debouncedQuery, selectedTags, page, size, sortBy, sortDir]);

  const handleSelectTag = (tag) => {
    setPage(0);
    setQuery('');
    setSelectedTags((prevTags) =>
      prevTags.includes(tag)
        ? prevTags.filter((t) => t !== tag)
        : [...prevTags, tag]
    );
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
      selectedTags={selectedTags}
      onSelectTag={handleSelectTag}
      onClearTagFilter={() => setSelectedTags([])}
    />
  );
}
