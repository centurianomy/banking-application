import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getCustomerById } from '../api/customerApi';
import { createAccount } from '../api/accountApi';
import '../styles/shared.css';

const initialFormState = {
  city: '',
  branch: '',
  balance: '',
};

function AccountPage() {
  const { customerId } = useParams();
  const navigate = useNavigate();

  const [customer, setCustomer] = useState(null);
  const [loadingCustomer, setLoadingCustomer] = useState(true);
  const [loadError, setLoadError] = useState('');

  const [formData, setFormData] = useState(initialFormState);
  const [submitting, setSubmitting] = useState(false);
  const [fieldErrors, setFieldErrors] = useState({});
  const [generalError, setGeneralError] = useState('');

  useEffect(() => {
    const fetchCustomer = async () => {
      try {
        const data = await getCustomerById(customerId);
        setCustomer(data);
      } catch (error) {
        setLoadError('Could not load customer details.');
      } finally {
        setLoadingCustomer(false);
      }
    };
    fetchCustomer();
  }, [customerId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setFieldErrors({});
    setGeneralError('');

    try {
      const balanceValue = formData.balance ? parseFloat(formData.balance) : 0;
  const payload = { ...formData, balance: balanceValue };

      const response = await createAccount(customerId, payload);
      navigate('/customers/confirmation', { state: response });
    } catch (error) {
      if (error.response?.data?.fieldErrors) {
        setFieldErrors(error.response.data.fieldErrors);
      } else if (error.response) {
        setGeneralError(error.response.data.message || 'Failed to open account.');
      } else {
        setGeneralError('Could not reach the server. Please try again.');
      }
    } finally {
      setSubmitting(false);
    }
  };

  if (loadingCustomer) {
    return <div className="page-container"><p>Loading customer details...</p></div>;
  }

  if (loadError) {
    return <div className="page-container"><p className="general-error">{loadError}</p></div>;
  }

  return (
    <div className="page-container">
      <div className="form-card">
        <h1 className="form-title">Open New Account</h1>
        <p className="form-subtitle">for {customer.name} (Customer ID: {customer.customerId})</p>

        <form onSubmit={handleSubmit} noValidate>
          <div className="form-field">
            <label htmlFor="city">City</label>
            <input id="city" name="city" value={formData.city} onChange={handleChange} />
            {fieldErrors.city && <span className="field-error">{fieldErrors.city}</span>}
          </div>

          <div className="form-field">
            <label htmlFor="branch">Branch</label>
            <input id="branch" name="branch" value={formData.branch} onChange={handleChange} />
            {fieldErrors.branch && <span className="field-error">{fieldErrors.branch}</span>}
          </div>

          <div className="form-field">
            <label htmlFor="balance">Opening Deposit (₹)</label>
            <input id="balance" name="balance" type="number" step="0.01" value={formData.balance} onChange={handleChange} placeholder="Minimum ₹1000" />
            {fieldErrors.balance && <span className="field-error">{fieldErrors.balance}</span>}
          </div>

          <button type="submit" className="submit-button" disabled={submitting}>
            {submitting ? 'Opening Account...' : 'Open Account'}
          </button>

          {generalError && <p className="general-error">{generalError}</p>}
        </form>
      </div>
    </div>
 );
}

export default AccountPage;