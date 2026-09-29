import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { deposit, withdraw } from '../api/accountApi';
import '../styles/shared.css';

function TransactionPage({ mode }) {
  const navigate = useNavigate();
  const isDeposit = mode === 'deposit';

  const [accountId, setAccountId] = useState('');
  const [amount, setAmount] = useState('');
  const [loading, setLoading] = useState(false);
  const [fieldErrors, setFieldErrors] = useState({});
  const [generalError, setGeneralError] = useState('');
  const [isConflict, setIsConflict] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setFieldErrors({});
    setGeneralError('');

    try {
      const action = isDeposit ? deposit : withdraw;
      const response = await action(accountId, parseFloat(amount));
      navigate('/customers/confirmation', {
        state: { ...response, flowType: 'account', actionLabel: isDeposit ? 'Deposit' : 'Withdrawal' },
      });
    } catch (error) {
      if (error.response?.data?.fieldErrors) {
        setFieldErrors(error.response.data.fieldErrors);
      }else if (error.response?.status === 409) {
  setGeneralError('This account was just updated by another transaction. Please review your balance and try again.');
} else if (error.response) {
  setGeneralError(error.response.data.message || 'Failed to process request.');
  setIsConflict(error.response.status === 409);
}
       else {
        setGeneralError('Could not reach the server. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-container">
      <div className="form-card">
        <h1 className="form-title">{isDeposit ? 'Deposit Funds' : 'Withdraw Funds'}</h1>
        <p className="form-subtitle">
          {isDeposit ? 'Add money to an account' : 'Take money out of an account'}
        </p>

        <form onSubmit={handleSubmit} noValidate>
          <div className="form-field">
            <label htmlFor="accountId">Account ID</label>
            <input id="accountId" value={accountId} onChange={(e) => setAccountId(e.target.value)} />
            {fieldErrors.accountId && <span className="field-error">{fieldErrors.accountId}</span>}
          </div>

          <div className="form-field">
            <label htmlFor="amount">Amount (₹)</label>
            <input
              id="amount"
              type="number"
              step="0.01"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
            />
            {fieldErrors.amount && <span className="field-error">{fieldErrors.amount}</span>}
          </div>

          <button type="submit" className="submit-button" disabled={loading}>
            {loading ? 'Processing...' : isDeposit ? 'Deposit' : 'Withdraw'}
          </button>

          {generalError && (
  <div className="general-error">
    <p>{generalError}</p>
    {isConflict && (
      <button type="button" className="secondary-button" onClick={handleSubmit}>
        Retry
      </button>
    )}
  </div>
)}
        </form>
      </div>
    </div>
  );
}

export default TransactionPage;