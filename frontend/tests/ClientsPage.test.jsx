import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { BrowserRouter } from 'react-router-dom';
import ClientsPage from '../src/pages/ClientsPage';

vi.spyOn(window, 'fetch');

function createFetchResponse(data) {
  return { ok: true, json: () => new Promise((resolve) => resolve(data)) };
}

describe('ClientsPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should render clients when API returns data', async () => {
    const mockClients = {
      content: [
        { id: 1, fullName: 'Cliente Uno' },
        { id: 2, fullName: 'Cliente Dos' },
      ],
      totalPages: 1,
      totalElements: 2,
    };
    window.fetch.mockResolvedValue(createFetchResponse(mockClients));

    render(
      <BrowserRouter>
        <ClientsPage />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(screen.queryByText(/loading/i)).not.toBeInTheDocument();
    });

    expect(screen.getByText('Cliente Uno')).toBeInTheDocument();
    expect(screen.getByText('Cliente Dos')).toBeInTheDocument();
  });

  it('should render no clients found message when API returns empty list', async () => {
    const mockEmpty = {
      content: [],
      totalPages: 0,
      totalElements: 0,
    };
    window.fetch.mockResolvedValue(createFetchResponse(mockEmpty));

    render(
      <BrowserRouter>
        <ClientsPage />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(screen.queryByText(/loading/i)).not.toBeInTheDocument();
    });

    expect(screen.getByText(/no clients found/i)).toBeInTheDocument();
  });
});
