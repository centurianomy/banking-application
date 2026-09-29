import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { decodeToken } from '../utils/jwtHelper';
import { getCurrentCustomer } from '../api/customerApi';
import { getAccountsByCustomer } from '../api/accountApi';
import '../styles/DashboardPage.css';

const QUICK_ACTIONS = [
  { key: 'deposit', label: 'Deposit', desc: 'Add funds to an account', icon: '↓', to: '/accounts/deposit' },
  { key: 'withdraw', label: 'Withdraw', desc: 'Take funds from an account', icon: '↑', to: '/accounts/withdraw' },
  { key: 'transfer', label: 'Transfer', desc: 'Move funds between accounts', icon: '⇄', to: '/accounts/transfer' },
];

function getInitials(name) {
  if (!name) return '';
  const parts = name.trim().split(' ');
  return parts.length === 1
    ? parts[0].slice(0, 2).toUpperCase()
    : (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
}

function DashboardPage() {
  const { token, logout } = useAuth();
  const decoded = decodeToken(token);

  const [currentCustomer, setCurrentCustomer] = useState(null);
  const [loadingCustomer, setLoadingCustomer] = useState(true);
  const [customerError, setCustomerError] = useState('');

  const [accounts, setAccounts] = useState([]);
  const [loadingAccounts, setLoadingAccounts] = useState(true);
  const [accountsError, setAccountsError] = useState('');

  useEffect(() => {
    const fetchCurrentCustomer = async () => {
      try {
        const data = await getCurrentCustomer();
        setCurrentCustomer(data);

        const accountsData = await getAccountsByCustomer(data.customerId);
        setAccounts(accountsData);
      } catch (error) {
        setCustomerError('Could not load your profile.');
        setAccountsError('Could not load your accounts.');
      } finally {
        setLoadingCustomer(false);
        setLoadingAccounts(false);
      }
    };
    fetchCurrentCustomer();
  }, []);

  return (
    <div className="dash-shell">
      <aside className="dash-sidebar">
        <div className="dash-brand">
          <span className="dash-brand-mark">B</span>
          <span className="dash-brand-name">BankApp</span>
        </div>

        <nav className="dash-nav">
          <span className="dash-nav-item active">Dashboard</span>
          {!loadingCustomer && currentCustomer && (
            <Link to={`/customers/${currentCustomer.customerId}/accounts/new`} className="dash-nav-item">
              Open Account
            </Link>
          )}
          {QUICK_ACTIONS.map((a) => (
            <Link key={a.key} to={a.to} className="dash-nav-item">
              {a.label}
            </Link>
          ))}
        </nav>

        <button className="dash-logout" onClick={logout}>Logout</button>
      </aside>

      <main className="dash-main">
        <header className="dash-topbar">
          <h1 className="dash-welcome">
            {loadingCustomer ? 'Welcome back' : `Welcome back, ${currentCustomer?.name?.split(' ')[0] || ''}`}
          </h1>
          {currentCustomer && (
            <div className="dash-avatar">{getInitials(currentCustomer.name)}</div>
          )}
        </header>

        <section className="dash-grid">
          <div className="dash-card">
            <h2 className="dash-card-title">Your Profile</h2>
            {loadingCustomer ? (
              <p className="dash-muted">Loading...</p>
            ) : customerError ? (
              <p className="general-error">{customerError}</p>
            ) : (
              <>
                <div className="dash-row">
                  <span className="dash-label">Customer ID</span>
                  <span className="dash-value">{currentCustomer.customerId}</span>
                </div>
                <div className="dash-row">
                  <span className="dash-label">Name</span>
                  <span className="dash-value">{currentCustomer.name}</span>
                </div>
                <div className="dash-row">
                  <span className="dash-label">Email</span>
                  <span className="dash-value">{currentCustomer.email}</span>
                </div>
                <Link
                  to={`/customers/${currentCustomer.customerId}/accounts/new`}
                  className="submit-button dash-card-action"
                >

                  Open an Account
                </Link>
              </>
            )}
          </div>

          <div className="dash-card">
            <h2 className="dash-card-title">Quick Actions</h2>
            <div className="dash-action-list">
              {QUICK_ACTIONS.map((a) => (
                <Link key={a.key} to={a.to} className={`dash-action-btn dash-action-${a.key}`}>
                  <span className="dash-action-icon">{a.icon}</span>
                  <span>
                    <span className="dash-action-label">{a.label}</span>
                    <span className="dash-action-desc">{a.desc}</span>
                  </span>
                </Link>
              ))}
            </div>
          </div>

          <div className="dash-card dash-card-wide">
            <h2 className="dash-card-title">Your Active Accounts</h2>
            {loadingAccounts ? (
              <p className="dash-muted">Loading accounts...</p>
            ) : accountsError ? (
              <p className="general-error">{accountsError}</p>
            ) : accounts.length === 0 ? (
              <p className="dash-muted">No accounts yet.</p>
            ) : (
              <div className="dash-account-list">
                {accounts.map((acc) => (
                  <div key={acc.accountId} className="dash-account-item">
                    <div>
                      <span className="dash-account-id">ACCOUNT ID: {acc.accountId} </span>
                      <span className="dash-account-no">{acc.accountNo}</span>
                      <span className="dash-account-meta">{acc.branch}, {acc.city} · {acc.accountStatus}</span>
                    </div>
                    <span className="dash-account-balance">
                      ₹{Number(acc.balance).toLocaleString('en-IN')}
                    </span>
                  </div>
                ))}
              </div>
            )}
          </div>
        </section>
      </main>
    </div>
  );
}

export default DashboardPage;