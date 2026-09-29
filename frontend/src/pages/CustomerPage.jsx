import { useState } from 'react';
import { createCustomer } from '../api/customerApi';
import { useNavigate } from 'react-router-dom';
import '../styles/shared.css';

const initialFormState = {
  name: '',
  fatherName: '',
  address: '',
  email: '',
  adharId: '',
  phoneNo: '',
  dob: '',
  gender: '',
};

function CustomerPage() {
  const [formData, setFormData] = useState(initialFormState);
  const [loading, setLoading] = useState(false);
  const [fieldErrors, setFieldErrors] = useState({});
  const [generalError, setGeneralError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setFieldErrors({});
    setGeneralError('');
    setSuccessMessage('');

    try {
      const response = await createCustomer(formData);
      navigate('/set-password', { state: response });
      setFormData(initialFormState);
    } catch (error) {
      if (error.response?.data?.fieldErrors) {
        setFieldErrors(error.response.data.fieldErrors);
      } else if (error.response) {
        setGeneralError(error.response.data.message || 'Failed to register customer.');
      } else {
        setGeneralError('Could not reach the server. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-container">
      <div className="form-card">
        {generalError && <p className="general-error">{generalError}</p>}
        {successMessage && <p className="success-message">{successMessage}</p>}

        <h1 className="form-title">Customer Registration</h1>
        <p className="form-subtitle">Enter customer details to open a new profile</p>

        <form onSubmit={handleSubmit} noValidate>
          <div className="form-field">
            <label htmlFor="name">Full Name</label>
            <input id="name" name="name" value={formData.name} onChange={handleChange} />
            {fieldErrors.name && <span className="field-error">{fieldErrors.name}</span>}
          </div>

          <div className="form-field">
            <label htmlFor="fatherName">Father's Name</label>
            <input id="fatherName" name="fatherName" value={formData.fatherName} onChange={handleChange} />
            {fieldErrors.fatherName && <span className="field-error">{fieldErrors.fatherName}</span>}
          </div>

          <div className="form-field">
            <label htmlFor="address">Address</label>
            <input id="address" name="address" value={formData.address} onChange={handleChange} />
            {fieldErrors.address && <span className="field-error">{fieldErrors.address}</span>}
          </div>

          <div className="form-field">
            <label htmlFor="email">Email</label>
            <input id="email" name="email" type="email" value={formData.email} onChange={handleChange} />
            {fieldErrors.email && <span className="field-error">{fieldErrors.email}</span>}
          </div>

          <div className="form-field">
            <label htmlFor="adharId">Aadhar ID</label>
            <input id="adharId" name="adharId" value={formData.adharId} onChange={handleChange} placeholder="12-digit number" />
            {fieldErrors.adharId && <span className="field-error">{fieldErrors.adharId}</span>}
          </div>

          <div className="form-field">
            <label htmlFor="phoneNo">Phone Number</label>
            <input id="phoneNo" name="phoneNo" value={formData.phoneNo} onChange={handleChange} placeholder="10-digit mobile number" />
            {fieldErrors.phoneNo && <span className="field-error">{fieldErrors.phoneNo}</span>}
          </div>

          <div className="form-field">
            <label htmlFor="dob">Date of Birth</label>
            <input id="dob" name="dob" type="date" value={formData.dob} onChange={handleChange} />
            {fieldErrors.dob && <span className="field-error">{fieldErrors.dob}</span>}
          </div>

          <div className="form-field">
            <label htmlFor="gender">Gender</label>
            <select id="gender" name="gender" value={formData.gender} onChange={handleChange}>
              <option value="">Select gender</option>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
              <option value="OTHER">Other</option>
            </select>
            {fieldErrors.gender && <span className="field-error">{fieldErrors.gender}</span>}
          </div>

          <button type="submit" className="submit-button" disabled={loading}>
            {loading ? 'Submitting...' : 'Register Customer'}
          </button>


        </form>
      </div>
    </div>
  );
}

export default CustomerPage;

// Render to Confirmation Page