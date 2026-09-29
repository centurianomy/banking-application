import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { transfer } from '../api/accountApi';
import '../styles/shared.css';

function TransferPage() {
  const navigate = useNavigate();

  const [fromAccountId, setFromAccountId] = useState('');
  const [toAccountId, setToAccountId] = useState('');
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
      const response = await transfer(fromAccountId, toAccountId, parseFloat(amount));
      navigate('/customers/confirmation', {
  state: { ...response.fromAccount, flowType: 'transaction', actionLabel: 'Transfer' },
});
    } catch (error) {
      if (error.response?.data?.fieldErrors) {
        setFieldErrors(error.response.data.fieldErrors);
      }  else if (error.response?.status === 409) {
  setGeneralError('This account was just updated by another transaction. Please review your balance and try again.');
} else if (error.response) {
  setGeneralError(error.response.data.message || 'Failed to process request.');
  setIsConflict(error.response.status === 409);
}else {
        setGeneralError('Could not reach the server. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-container">
      <div className="form-card">
        <h1 className="form-title">Transfer Funds</h1>
        <p className="form-subtitle">Move money between two accounts</p>

        <form onSubmit={handleSubmit} noValidate>
          <div className="form-field">
            <label htmlFor="fromAccountId">From Account ID</label>
            <input id="fromAccountId" value={fromAccountId} onChange={(e) => setFromAccountId(e.target.value)} />
          </div>

          <div className="form-field">
            <label htmlFor="toAccountId">To Account ID</label>
            <input id="toAccountId" value={toAccountId} onChange={(e) => setToAccountId(e.target.value)} />
            {fieldErrors.toAccountId && <span className="field-error">{fieldErrors.toAccountId}</span>}
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
            {loading ? 'Transferring...' : 'Transfer'}
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

export default TransferPage;