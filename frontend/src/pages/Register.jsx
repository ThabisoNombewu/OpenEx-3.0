import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { register } from '../api/client';

function Register() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    setError('');
    setSuccess('');

    try {
      await register(username, password);

      setSuccess('Account created successfully. Redirecting to login...');

      setTimeout(() => {
        navigate('/login');
      }, 1000);
    } catch (err) {
      setError(
        err?.response?.data?.message ||
        'Unable to create account'
      );
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
          <h2>Create account</h2>

          <p className="auth-description">
            Create your account to start trading.
          </p>

          {error && (
            <div className="auth-error">
              {error}
            </div>
          )}

          {success && (
            <div className="order-status success">
              {success}
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
                placeholder="Choose a username"
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
                placeholder="Choose a password"
                required
              />
            </div>

            <button
              type="submit"
              className="auth-submit"
            >
              Create Account
            </button>
          </form>

          <div className="auth-footer">
            Already have an account?{' '}
            <Link to="/login">
              Sign in
            </Link>
          </div>
        </section>

      </div>
    </main>
  );
}

export default Register;