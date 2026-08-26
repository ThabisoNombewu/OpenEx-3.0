import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { getBalances } from '../api/client';

function Dashboard() {
  const { username } = useAuth();
  const [loading, setLoading] = useState(true);

  const [balances, setBalances] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    getBalances()
      .then(setBalances)
      .catch(() => setError('Failed to load balances'))
      .finally(() => setLoading(false));
  }, []);

  return (
    <main className="dashboard-page">

      <section className="dashboard-header">
        <p className="eyebrow">
          OVERVIEW
        </p>

        <h1>
          Welcome back, {username}
        </h1>

        <p>
          Monitor your trading activity and access the live market.
        </p>
      </section>

      <section className="dashboard-grid">

        <div className="dashboard-card card">
          <div className="dashboard-card-label">
            Market
          </div>

          <div className="dashboard-card-value">
            BTC / USDT
          </div>
        </div>

        <div className="dashboard-card card">
          <div className="dashboard-card-label">
            Trading Status
          </div>

          <div
            className="dashboard-card-value"
            style={{ color: '#22c55e' }}
          >
            Online
          </div>
        </div>

        <div className="dashboard-card card">
          <div className="dashboard-card-label">
            Platform
          </div>

          <div className="dashboard-card-value">
            OpenEx 3.0
          </div>
        </div>

      </section>

      <section
        className="card"
        style={{
          marginTop: '20px',
          padding: '28px',
        }}
      >
        <p className="eyebrow">
          YOUR BALANCES
        </p>

        {loading && <p style={{ color: '#94a3b8' }}>Loading balances...</p>}
        {error && <p style={{ color: '#ef4444' }}>{error}</p>}

        {!loading && !error && balances && Object.keys(balances).length === 0 && (
          <p style={{ color: '#94a3b8' }}>No balances yet</p>
        )}

        {!loading && !error && balances && Object.keys(balances).length > 0 && (
          <div className="dashboard-grid" style={{ marginTop: '12px' }}>
            {Object.entries(balances).map(([currency, amount]) => (
              <div className="dashboard-card card" key={currency}>
                <div className="dashboard-card-label">{currency}</div>
                <div className="dashboard-card-value">{amount}</div>
              </div>
            ))}
          </div>
        )}
      </section>

      <section
        className="card"
        style={{
          marginTop: '20px',
          padding: '28px',
        }}
      >
        <p className="eyebrow">
          LIVE MARKETS
        </p>

        <h2 style={{ marginBottom: '8px' }}>
          Ready to trade?
        </h2>

        <p
          style={{
            color: '#94a3b8',
            marginBottom: '20px',
          }}
        >
          Open the trading terminal to view the live order book
          and place orders.
        </p>

        <Link
          to="/trading"
          style={{
            display: 'inline-block',
            padding: '11px 18px',
            borderRadius: '8px',
            background: '#6366f1',
            color: '#fff',
            textDecoration: 'none',
            fontWeight: 700,
            fontSize: '14px',
          }}
        >
          Open Trading Terminal →
        </Link>
      </section>

    </main>
  );
}

export default Dashboard;