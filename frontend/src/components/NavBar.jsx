import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function NavBar() {
  const location = useLocation();
  const navigate = useNavigate();
  const { token, username, logout } = useAuth();

  const linkStyle = (path) => ({
    textDecoration: 'none',
    color:
      location.pathname === path
        ? '#818cf8'
        : '#94a3b8',
    fontWeight:
      location.pathname === path
        ? 600
        : 400,
  });

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  // Don't show the application navbar on authentication pages
  if (!token) {
    return null;
  }

  return (
    <nav className="navbar">
      <Link to="/dashboard" className="navbar-brand">
        OpenEx
      </Link>

      <div className="navbar-links">
        <Link
          to="/dashboard"
          style={linkStyle('/dashboard')}
        >
          Dashboard
        </Link>

        <Link
          to="/trading"
          style={linkStyle('/trading')}
        >
          Trading
        </Link>
      </div>

      <div className="navbar-user">
        <span>{username}</span>

        <button
          type="button"
          className="logout-button"
          onClick={handleLogout}
        >
          Logout
        </button>
      </div>
    </nav>
  );
}

export default NavBar;