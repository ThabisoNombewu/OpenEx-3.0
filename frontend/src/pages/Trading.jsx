import { useState } from 'react';
import { placeOrder } from '../api/client';

function Trading() {
  const [side, setSide] = useState('BUY');
  const [currencyPair, setCurrencyPair] = useState('BTC/USD');
  const [orderType, setOrderType] = useState('LIMIT');
  const [price, setPrice] = useState('');
  const [quantity, setQuantity] = useState('');
  const [status, setStatus] = useState(null);

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
    </div>
  );
}

export default Trading;