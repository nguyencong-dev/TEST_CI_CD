import React from "react";
import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import App from "./App";

const greetings = [
  { id: 1, name: "Docker" },
  { id: 2, name: "Spring Boot" },
  { id: 3, name: "MariaDB" },
];
const summary = { service: "Greeting API", database: "MariaDB", totalGreetings: 3 };

beforeEach(() => jest.restoreAllMocks());

test("shows loading and requests both database endpoints", async () => {
  const fetchMock = jest.spyOn(global, "fetch")
    .mockResolvedValueOnce({ ok: true, json: async () => greetings } as Response)
    .mockResolvedValueOnce({ ok: true, json: async () => summary } as Response);

  render(<App />);
  expect(screen.getByText("Đang tải dữ liệu từ API...")).toBeInTheDocument();
  await screen.findByText("API hoạt động");
  expect(fetchMock).toHaveBeenCalledWith("/api/greetings");
  expect(fetchMock).toHaveBeenCalledWith("/api/summary");
  expect(screen.getByText("3 mục")).toBeInTheDocument();
});

test("selects a different database record", async () => {
  jest.spyOn(global, "fetch")
    .mockResolvedValueOnce({ ok: true, json: async () => greetings } as Response)
    .mockResolvedValueOnce({ ok: true, json: async () => summary } as Response);

  render(<App />);
  await screen.findByText("API hoạt động");
  fireEvent.click(screen.getByRole("button", { name: /02 Spring Boot/ }));
  expect(screen.getByText("/greetings/2")).toBeInTheDocument();
  expect(screen.getByText(/ID 2 được đọc từ MariaDB/)).toBeInTheDocument();
});

test("shows a useful error when API returns failure", async () => {
  jest.spyOn(global, "fetch").mockResolvedValue({ ok: false, status: 503 } as Response);
  render(<App />);
  await waitFor(() => expect(screen.getByRole("alert")).toHaveTextContent("HTTP 503"));
  expect(screen.getByText("API lỗi")).toBeInTheDocument();
});
