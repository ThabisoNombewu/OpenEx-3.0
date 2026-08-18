import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

import { login } from '../api/client';
import { useAuth } from '../context/AuthContext';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const { setAuth } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const data = await login(username, password);

      setAuth(data.token, data.username);

      navigate('/dashboard');
    } catch (err) {
      setError('Invalid username or password');
    }
  };

  return (
    <main className="auth-page">
      <div className="auth-container">

        <div className="auth-brand">
          <h1>OpenEx</h1>
          <p>Decentralized Trading Terminal</p>
        </div>

        <section className="auth-card card">
          <h2>Welcome back</h2>

          <p className="auth-description">
            Sign in to access your trading dashboard.
          </p>

          {error && (
            <div className="auth-error">
              {error}
            </div>
          )}

          <form
            onSubmit={handleSubmit}
            className="auth-form"
          >
            <div className="form-group">
              <label htmlFor="username">
                Username
              </label>

              <input
                id="username"
                type="text"
                value={username}
                onChange={(e) =>
                  setUsername(e.target.value)
                }
                placeholder="Enter your username"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="password">
                Password
              </label>

              <input
                id="password"
                type="password"
                value={password}
                onChange={(e) =>
                  setPassword(e.target.value)
                }
                placeholder="Enter your password"
                required
              />
            </div>

            <button
              type="submit"
              className="auth-submit"
            >
              Sign In
            </button>
          </form>

          <div className="auth-footer">
            Don't have an account?{' '}
            <Link to="/register">
              Create one
            </Link>
          </div>
        </section>

      </div>
    </main>
  );
}

export default Login;