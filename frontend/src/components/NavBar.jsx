import { Link, useLocation } from 'react-router-dom';

function NavBar() {
  const location = useLocation();

  const linkStyle = (path) => ({
    marginRight: '1.5rem',
    textDecoration: 'none',
    color: location.pathname === path ? '#4f46e5' : '#333',
    fontWeight: location.pathname === path ? 600 : 400,
  });

  return (
    <nav style={{ padding: '1rem 2rem', borderBottom: '1px solid #e5e5e5' }}>
      <Link to="/dashboard" style={linkStyle('/dashboard')}>Dashboard</Link>
      <Link to="/trading" style={linkStyle('/trading')}>Trading</Link>
      <Link to="/login" style={linkStyle('/login')}>Login</Link>
    </nav>
  );
}

export default NavBar;