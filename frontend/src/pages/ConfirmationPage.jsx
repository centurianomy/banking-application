
import { useLocation, useNavigate } from 'react-router-dom';
import RegistrationSummary from '../components/RegistrationSummary';
import AccountSummary from '../components/AccountSummary';
import TransactionSummary from '../components/TransactionSummary';
import '../styles/shared.css';


import '../styles/ConfirmationPage.css';


const CONFIRMATION_CONFIG = {
  registration: {
    title: 'Registration Successful',
    subtitle: "You're all set. Please log in to continue.",
    Summary: RegistrationSummary,
  },
  account: {
    title: 'Account Opened Successfully',
    subtitle: 'The new account has been created',
    Summary: AccountSummary,
  },
  transaction: {
    title: 'Transaction Successful',
    subtitle: 'Your transaction has been completed',
    Summary: TransactionSummary,
  },
};

function ConfirmationPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const data = location.state;

  if (!data) {
    return (
      <div className="page-container">
        <div className="form-card">
          <p>No confirmation data found.</p>
          <button className="submit-button" onClick={() => navigate('/dashboard')}>
            Go to Dashboard
          </button>
        </div>
      </div>
    );
  }

  const config = CONFIRMATION_CONFIG[data.flowType] || CONFIRMATION_CONFIG.account;
  const { title, subtitle, Summary } = config;

  return (
    <div className="page-container">
      <div className="confirmation-card">
        <div className="success-icon">✓</div>
        <h1 className="confirmation-title">{data.actionLabel ? `${data.actionLabel} Successful` : title}</h1>
        <p className="confirmation-subtitle">{subtitle}</p>

        <Summary data={data} />

        <div className="confirmation-actions">
          <button className="submit-button" onClick={() => window.print()}>
            Print
          </button>
          {data.flowType === 'registration' ? (
            <button className="secondary-button" onClick={() => navigate('/login')}>
              Continue to Login
            </button>
          ) : (
            <button className="secondary-button" onClick={() => navigate('/dashboard')}>
              Go to Dashboard
            </button>
          )}
        </div>
      </div>
    </div>
  );
}

export default ConfirmationPage;
// Render to create user credentials (userName & password) SIGN IN -> login