import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import App from './App';

describe('App Component Unit Tests', () => {
  beforeEach(() => {
    jest.restoreAllMocks();
  });

  test('Hiển thị link Learn React và trạng thái Loading ban đầu', () => {
    // Giả lập fetch đang pending
    jest.spyOn(global, 'fetch').mockImplementation(() => new Promise(() => {}));

    render(<App />);

    expect(screen.getByText(/learn react/i)).toBeInTheDocument();
    expect(screen.getByText(/loading\.\.\./i)).toBeInTheDocument();
  });

  test('Hiển thị thông tin chào mừng khi API fetch thành công', async () => {
    // Giả lập API /api trả về { id: 1, name: "Docker Compose" }
    jest.spyOn(global, 'fetch').mockResolvedValueOnce({
      json: async () => ({ id: 1, name: 'Docker Compose' }),
    } as Response);

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText('Hello from Docker Compose')).toBeInTheDocument();
    });
  });

  test('Vẫn giữ trạng thái Loading khi gọi API bị lỗi', async () => {
    // Ẩn log console.error khi giả lập lỗi mạng
    const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});
    jest.spyOn(global, 'fetch').mockRejectedValueOnce(new Error('Network error'));

    render(<App />);

    expect(screen.getByText(/loading\.\.\./i)).toBeInTheDocument();
    consoleSpy.mockRestore();
  });
});
