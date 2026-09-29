import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../api/authApi';
import { useAuth } from '../context/AuthContext';
import '../styles/shared.css';

function LoginPage() {
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [loading, setLoading] = useState(false);

  const { setToken } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setErrorMessage('');

    try {
      const token = await login({ userName, password });
      setToken(token);
      navigate('/dashboard');
    } catch (error) {
      if (error.response) {
        setErrorMessage(error.response.data.message || 'Invalid email or password.');
      } else {
        setErrorMessage('Could not reach the server. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-container">
      <div className="form-card">
        <h1 className="form-title">Login</h1>
        <p className="form-subtitle">Sign in to access your banking account</p>

        <form onSubmit={handleSubmit} noValidate>
          <div className="form-field">
            <label htmlFor="userName">Email</label>
            <input id="userName" type="email" value={userName} onChange={(e) => setUserName(e.target.value)} />
          </div>
          <div className="form-field">
            <label htmlFor="password">Password</label>
            <input id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
          </div>
          <button type="submit" className="submit-button" disabled={loading}>
            {loading ? 'Signing in...' : 'Login'}
          </button>
          {errorMessage && <p className="general-error">{errorMessage}</p>}
        </form>
      </div>
    </div>
  );
}

export default LoginPage;
// iske baad jo h Customer Dashboard pe render ho ge
// dashboard se KYC verification then Account Page