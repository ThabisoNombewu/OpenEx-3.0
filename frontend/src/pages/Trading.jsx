import { useState, useEffect } from 'react';
import { placeOrder, getOrderHistory } from '../api/client';

function Trading() {
  const [side, setSide] = useState('BUY');
  const [currencyPair, setCurrencyPair] = useState('BTC/USD');
  const [orderType, setOrderType] = useState('LIMIT');
  const [price, setPrice] = useState('');
  const [quantity, setQuantity] = useState('');
  const [status, setStatus] = useState(null);
  const [orderHistory, setOrderHistory] = useState([]);
  const [historyLoading, setHistoryLoading] = useState(true);
  const [historyError, setHistoryError] = useState('');

  const fetchOrderHistory = () => {
    setHistoryLoading(true);
    getOrderHistory()
      .then(setOrderHistory)
      .catch(() => setHistoryError('Failed to load order history'))
      .finally(() => setHistoryLoading(false));
};

useEffect(() => {
  fetchOrderHistory();
}, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setStatus(null);

    const order = {
      currencyPair,
      side,
      orderType,
      quantity: parseFloat(quantity),
      ...(orderType === 'LIMIT' ? { price: parseFloat(price) } : {}),
    };

    try {
      const result = await placeOrder(order);
      setStatus({ type: 'success', message: `Order placed: ${result.id ?? 'submitted'}` });
      setPrice('');
      setQuantity('');
      fetchOrderHistory();
    } catch (err) {
      setStatus({ type: 'error', message: 'Failed to place order' });
    }
  };

  return (
    <div style={{ padding: '2rem', maxWidth: '360px' }}>
      <h1>Trading</h1>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: '1rem' }}>
          <label>Currency Pair</label>
          <select value={currencyPair} onChange={(e) => setCurrencyPair(e.target.value)} style={{ display: 'block', width: '100%', padding: '0.5rem' }}>
            <option value="BTC/USD">BTC/USD</option>
            <option value="ETH/USD">ETH/USD</option>
            <option value="SOL/USD">SOL/USD</option>
            <option value="BNB/USD">BNB/USD</option>
            <option value="XRP/USD">XRP/USD</option>
            <option value="ADA/USD">ADA/USD</option>
            <option value="DOGE/USD">DOGE/USD</option>
            <option value="DOT/USD">DOT/USD</option>
            <option value="LINK/USD">LINK/USD</option>
            <option value="MATIC/USD">MATIC/USD</option>
          </select>
        </div>

        <div style={{ marginBottom: '1rem' }}>
          <label>Side</label>
          <select value={side} onChange={(e) => setSide(e.target.value)} style={{ display: 'block', width: '100%', padding: '0.5rem' }}>
            <option value="BUY">Buy</option>
            <option value="SELL">Sell</option>
          </select>
        </div>

        <div style={{ marginBottom: '1rem' }}>
          <label>Order Type</label>
          <select value={orderType} onChange={(e) => setOrderType(e.target.value)} style={{ display: 'block', width: '100%', padding: '0.5rem' }}>
            <option value="LIMIT">Limit</option>
            <option value="MARKET">Market</option>
          </select>
        </div>

        {orderType === 'LIMIT' && (
          <div style={{ marginBottom: '1rem' }}>
            <label>Price</label>
            <input
              type="number"
              step="0.00000001"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              style={{ display: 'block', width: '100%', padding: '0.5rem' }}
              required
            />
          </div>
        )}

        <div style={{ marginBottom: '1rem' }}>
          <label>Quantity</label>
          <input
            type="number"
            step="0.00000001"
            value={quantity}
            onChange={(e) => setQuantity(e.target.value)}
            style={{ display: 'block', width: '100%', padding: '0.5rem' }}
            required
          />
        </div>

        {status && (
          <p style={{ color: status.type === 'error' ? 'red' : 'green' }}>{status.message}</p>
        )}

        <button type="submit" style={{ padding: '0.5rem 1rem' }}>
          Place {side} Order
        </button>
      </form>
      <div style={{ marginTop: '2rem' }}>
  <h2>Order History</h2>
  {historyLoading && <p>Loading order history...</p>}
  {historyError && <p style={{ color: 'red' }}>{historyError}</p>}
  {!historyLoading && !historyError && orderHistory.length === 0 && (
    <p>No orders yet</p>
  )}
  {!historyLoading && !historyError && orderHistory.length > 0 && (
    <table style={{ width: '100%', borderCollapse: 'collapse' }}>
      <thead>
        <tr>
          <th style={{ textAlign: 'left', borderBottom: '1px solid #ddd', padding: '0.5rem' }}>Side</th>
          <th style={{ textAlign: 'left', borderBottom: '1px solid #ddd', padding: '0.5rem' }}>Type</th>
          <th style={{ textAlign: 'left', borderBottom: '1px solid #ddd', padding: '0.5rem' }}>Price</th>
          <th style={{ textAlign: 'left', borderBottom: '1px solid #ddd', padding: '0.5rem' }}>Quantity</th>
          <th style={{ textAlign: 'left', borderBottom: '1px solid #ddd', padding: '0.5rem' }}>Status</th>
        </tr>
      </thead>
      <tbody>
        {orderHistory.map((order) => (
          <tr key={order.id}>
            <td style={{ padding: '0.5rem' }}>{order.side}</td>
            <td style={{ padding: '0.5rem' }}>{order.orderType}</td>
            <td style={{ padding: '0.5rem' }}>{order.price ?? '—'}</td>
            <td style={{ padding: '0.5rem' }}>{order.quantity}</td>
            <td style={{ padding: '0.5rem' }}>{order.status}</td>
          </tr>
        ))}
      </tbody>
    </table>
  )}
</div>
    </div>
  );
}

export default Trading;