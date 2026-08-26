import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { register } from '../api/client';
import { useAuth } from '../context/AuthContext';
import AnimatedBackground from '../components/AnimatedBackground';

function Register() {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { setAuth } = useAuth();
  const navigate = useNavigate();

  const inputStyle = {
  display: 'block',
  width: '100%',
  padding: '10px 12px',
  borderRadius: '8px',
  border: '1px solid #334155',
  background: '#1e293b',
  color: '#fff',
  fontSize: '14px',
};

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const data = await register(username, password, firstName, lastName);
      setAuth(data.token, data.username);
      navigate('/dashboard');
    } catch (err) {
      console.error(err);
      setError('Registration failed — username may already be taken');
    }
  };

    return (
    <div
      style={{
        position: 'relative',
        minHeight: '100vh',
        background: '#0f172a',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        padding: '2rem',
      }}
    >
      <AnimatedBackground />

      <div
        className="card"
        style={{
          position: 'relative',
          zIndex: 1,
          width: '100%',
          maxWidth: '380px',
          padding: '32px',
          borderRadius: '12px',
        }}
      >
        <p className="eyebrow" style={{ marginBottom: '4px' }}>
          GET STARTED
        </p>
        <h1 style={{ marginBottom: '24px', color: '#fff' }}>Register</h1>

        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '16px' }}>
            <label style={{ display: 'block', marginBottom: '6px', color: '#94a3b8', fontSize: '14px' }}>
              First Name
            </label>
            <input
              type="text"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              style={inputStyle}
              required
            />
          </div>

          <div style={{ marginBottom: '16px' }}>
            <label style={{ display: 'block', marginBottom: '6px', color: '#94a3b8', fontSize: '14px' }}>
              Last Name
            </label>
            <input
              type="text"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              style={inputStyle}
              required
            />
          </div>

          <div style={{ marginBottom: '16px' }}>
            <label style={{ display: 'block', marginBottom: '6px', color: '#94a3b8', fontSize: '14px' }}>
              Username
            </label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              style={inputStyle}
              required
            />
          </div>

          <div style={{ marginBottom: '20px' }}>
            <label style={{ display: 'block', marginBottom: '6px', color: '#94a3b8', fontSize: '14px' }}>
              Password
            </label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={inputStyle}
              required
            />
          </div>

          {error && (
            <p style={{ color: '#ef4444', marginBottom: '16px', fontSize: '14px' }}>{error}</p>
          )}

          <button
            type="submit"
            style={{
              width: '100%',
              padding: '11px 18px',
              borderRadius: '8px',
              background: '#6366f1',
              color: '#fff',
              border: 'none',
              fontWeight: 700,
              fontSize: '14px',
              cursor: 'pointer',
            }}
          >
            Register
          </button>
        </form>
      </div>
    </div>
  );
}

export default Register;