import { useEffect, useState } from 'react';
import { getBalances } from '../api/client';
import { useAuth } from '../context/AuthContext';

function Dashboard() {
  const [balances, setBalances] = useState(null);
  const [error, setError] = useState('');
  const { username } = useAuth();

  useEffect(() => {
    getBalances()
      .then(setBalances)
      .catch(() => setError('Failed to load balances'));
  }, []);

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Dashboard</h1>
      <p>Welcome, {username}</p>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {balances && (
        <ul>
          {Object.entries(balances).map(([currency, amount]) => (
            <li key={currency}>{currency}: {amount}</li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default Dashboard;