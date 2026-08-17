import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function NavBar() {
  const location = useLocation();
  const navigate = useNavigate();
  const { token, username, logout } = useAuth();

  const linkStyle = (path) => ({
    marginRight: '1.5rem',
    textDecoration: 'none',
    color: location.pathname === path ? '#4f46e5' : '#333',
    fontWeight: location.pathname === path ? 600 : 400,
  });

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav style={{ padding: '1rem 2rem', borderBottom: '1px solid #e5e5e5', display: 'flex', justifyContent: 'space-between' }}>
      <div>
        <Link to="/dashboard" style={linkStyle('/dashboard')}>Dashboard</Link>
        <Link to="/trading" style={linkStyle('/trading')}>Trading</Link>
        {!token && <Link to="/login" style={linkStyle('/login')}>Login</Link>}
      </div>
      {token && (
        <div>
          <span style={{ marginRight: '1rem' }}>{username}</span>
          <button onClick={handleLogout}>Logout</button>
        </div>
      )}
    </nav>
  );
}

export default NavBar;