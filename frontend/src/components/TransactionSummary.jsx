function TransactionSummary({ data }) {
  return (
    <>
      <div className="detail-row">
        <span className="detail-label">Account Number</span>
        <span className="detail-value">{data.accountNo}</span>
      </div>
      <div className="detail-row">
        <span className="detail-label">New Balance</span>
        <span className="detail-value">₹{Number(data.balance).toLocaleString('en-IN')}</span>
      </div>
    </>
  );
}

export default TransactionSummary;