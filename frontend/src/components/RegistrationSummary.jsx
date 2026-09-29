function RegistrationSummary({ data }) {
  return (
    <>
      <div className="detail-row">
        <span className="detail-label">Customer ID</span>
        <span className="detail-value">{data.customerId}</span>
      </div>
      <div className="detail-row">
        <span className="detail-label">Name</span>
        <span className="detail-value">{data.name}</span>
      </div>
      <div className="detail-row">
        <span className="detail-label">Email</span>
        <span className="detail-value">{data.email}</span>
      </div>
    </>
  );
}

export default RegistrationSummary;