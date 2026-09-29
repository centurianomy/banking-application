import { Routes, Route } from 'react-router-dom';
import CustomerPage from './pages/CustomerPage';
import AccountPage from './pages/AccountPage';
import ConfirmationPage from './pages/ConfirmationPage';
import LoginPage from './pages/LoginPage';
import ProtectedRoute from './components/ProtectRoute';
import DashboardPage from './pages/DashboardPage';
import SetPasswordPage from './pages/SetPasswordPage';
import TransactionPage from './pages/TransactionPage';
import TransferPage from './pages/TransferPage';
import LandingPage from './pages/LandingPage';

function App() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/login" element={<LoginPage />} />

      <Route path="/customers" element={
        <ProtectedRoute><CustomerPage /></ProtectedRoute>
      } />
      <Route path="/customers/confirmation" element={
        <ProtectedRoute><ConfirmationPage /></ProtectedRoute>
      } />
      <Route path="/accounts" element={
  <ProtectedRoute><AccountPage /></ProtectedRoute>
} />
      <Route path="/customers/:customerId/accounts/new" element={
        <ProtectedRoute><AccountPage /></ProtectedRoute>
      } />
      <Route path="/dashboard" element={
  <ProtectedRoute><DashboardPage /></ProtectedRoute>
} />
<Route path="/set-password" element={<SetPasswordPage />} />
    <Route path="/accounts/deposit" element={
  <ProtectedRoute><TransactionPage mode="deposit" /></ProtectedRoute>
} />
<Route path="/accounts/withdraw" element={
  <ProtectedRoute><TransactionPage mode="withdraw" /></ProtectedRoute>
} />

<Route path="/accounts/transfer" element={
  <ProtectedRoute><TransferPage /></ProtectedRoute>
} />




    </Routes>

  );
}

export default App;