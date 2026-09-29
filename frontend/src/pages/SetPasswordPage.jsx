import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { registerCredentials } from '../api/authApi';
import '../styles/shared.css';

function SetPasswordPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const customer = location.state;

  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  if (!customer) {
    return (
      <div className="page-container">
        <div className="form-card">
          <p>No registration data found.</p>
          <button className="submit-button" onClick={() => navigate('/customers')}>
            Go to Registration
          </button>
        </div>
      </div>
    );
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (password.length < 8) {
      setError('Password must be at least 8 characters long.');
      return;
    }
    if (password !== confirmPassword) {
      setError('Passwords do not match.');
      return;
    }

    setLoading(true);
    try {
      await registerCredentials(customer.customerId, password);
      navigate('/customers/confirmation', { state: { ...customer, flowType: 'registration' } });
    } catch (err) {
      if (err.response) {
        setError(err.response.data.message || 'Failed to set password.');
      } else {
        setError('Could not reach the server. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-container">
      <div className="form-card">
        <h1 className="form-title">Set Your Password</h1>
        <p className="form-subtitle">for {customer.name} ({customer.email})</p>

        <form onSubmit={handleSubmit} noValidate>
          <div className="form-field">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="At least 8 characters"
            />
          </div>

          <div className="form-field">
            <label htmlFor="confirmPassword">Confirm Password</label>
            <input
              id="confirmPassword"
              type="password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
          </div>

          <button type="submit" className="submit-button" disabled={loading}>
            {loading ? 'Setting Password...' : 'Set Password & Continue'}
          </button>

          {error && <p className="general-error">{error}</p>}
        </form>
      </div>
    </div>
  );
}

export default SetPasswordPage;