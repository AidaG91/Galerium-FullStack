import { render, screen, waitFor } from "@testing-library/react";
import { describe, it, expect, vi } from "vitest";
import { BrowserRouter } from "react-router-dom";
import ClientsPage from "./ClientsPage";

global.fetch = vi.fn();

function createFetchResponse(data) {
  return { ok: true, json: () => new Promise((resolve) => resolve(data)) };
}

describe("ClientsPage", () => {
  beforeEach(() => {
    fetch.mockClear();
  });

  it("should render clients when API returns data", async () => {
    const mockClients = {
      content: [
        {
          id: 1,
          fullName: "Cliente Uno",
          email: "uno@test.com",
          phoneNumber: "111",
        },
        {
          id: 2,
          fullName: "Cliente Dos",
          email: "dos@test.com",
          phoneNumber: "222",
        },
      ],
      totalPages: 1,
      totalElements: 2,
    };
    fetch.mockResolvedValue(createFetchResponse(mockClients));

    render(
      <BrowserRouter>
        <ClientsPage />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(screen.queryByText(/loading/i)).not.toBeInTheDocument();
    });

    expect(screen.getByText("Cliente Uno")).toBeInTheDocument();
    expect(screen.getByText("Cliente Dos")).toBeInTheDocument();
  });

  it("should render no clients found message when API returns empty list", async () => {
    const mockEmpty = {
      content: [],
      totalPages: 0,
      totalElements: 0,
    };
    fetch.mockResolvedValue(createFetchResponse(mockEmpty));

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
