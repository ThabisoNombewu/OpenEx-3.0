import { useEffect, useState } from 'react';
import { getBalances } from '../api/client';
import { useAuth } from '../context/AuthContext';

function Dashboard() {
  const [balances, setBalances] = useState(null);
  const [error, setError] = useState('');
  const { username } = useAuth();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getBalances()
      .then(setBalances)
      .catch(() => setError('Failed to load balances'))
      .finally(() => setLoading(false));
}, []);

// in render:
{loading && <p>Loading balances...</p>}
{!loading && balances && Object.keys(balances).length === 0 && <p>No balances yet</p>}
{!loading && balances && Object.keys(balances).length > 0 && (
  <ul>{Object.entries(balances).map(([c, a]) => <li key={c}>{c}: {a}</li>)}</ul>
)}

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