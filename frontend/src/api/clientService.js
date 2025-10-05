const API_BASE_URL =
  import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const handleResponse = async (response) => {
  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(
      `HTTP ${response.status}: ${errorText || 'An error occurred'}`
    );
  }
  if (response.status === 204) {
    return;
  }
  return response.json();
};

export const getClients = (params) => {
  let path;
  const tags = params.tag || [];

  if (tags.length > 0) {
    path = '/clients/by-tag';
  } else if (params.q) {
    path = '/clients/search/paged';
  } else {
    path = '/clients/paged';
  }

  const url = new URL(`${API_BASE_URL}${path}`);

  Object.keys(params).forEach((key) => {
    if (key === 'tag' && Array.isArray(params[key])) {
      params[key].forEach((tagValue) => url.searchParams.append(key, tagValue));
    } else {
      url.searchParams.append(key, params[key]);
    }
  });

  return fetch(url).then(handleResponse);
};

export const getClientById = (id) => {
  return fetch(`${API_BASE_URL}/clients/${id}`).then(handleResponse);
};

export const createClient = (clientData) => {
  return fetch(`${API_BASE_URL}/clients`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(clientData),
  }).then(handleResponse);
};

export const updateClient = (id, clientData) => {
  return fetch(`${API_BASE_URL}/clients/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(clientData),
  }).then(handleResponse);
};

export const deleteClient = (id) => {
  return fetch(`${API_BASE_URL}/clients/${id}`, {
    method: 'DELETE',
  }).then(handleResponse);
};

export const getAllTags = () => {
  return fetch(`${API_BASE_URL}/tags`).then(handleResponse);
};
