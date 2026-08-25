import { generateUUID } from '../utils/uuid';

const BASE_URL = import.meta.env.VITE_API_URL;

async function request(path, options = {}) {
  const token = localStorage.getItem('jwt_token');
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...options.headers,
  };

  let response;
  try {
    response = await fetch(`${BASE_URL}${path}`, { ...options, headers });
  } catch (err) {
    throw new Error('Unable to reach the server. Check your connection and try again.');
  }

  if (!response.ok) {
    if (response.status === 401) {
      localStorage.removeItem('jwt_token');
      localStorage.removeItem('username');
      window.location.href = '/login';
      throw new Error('Session expired. Please log in again.');
    }
    const errorText = await response.text().catch(() => '');
    throw new Error(errorText || `Request failed (${response.status})`);
  }

  return response.json();
}

export const login = (username, password) =>
  request('/auth/login', { method: 'POST', body: JSON.stringify({ username, password }) });

export const register = (username, password, firstName, lastName) =>
  request('/auth/register', {
    method: 'POST',
    body: JSON.stringify({ username, password, firstName, lastName }),
  });

export const getBalances = () => request('/wallets/balances', { method: 'GET' });

export const getOrderHistory = () => request('/orders', { method: 'GET' });

export const placeOrder = (order) =>
  request('/orders', {
    method: 'POST',
    headers: { 'Idempotency-Key': generateUUID() },
    body: JSON.stringify(order),
  });