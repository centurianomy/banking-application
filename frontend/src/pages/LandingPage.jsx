import { Link } from 'react-router-dom';
import '../styles/LandingPage.css';

const FEATURES = [
  {
    icon: '🔒',
    title: 'Secure by Design',
    desc: 'JWT-based authentication and protected sessions keep your account safe.',
  },
  {
    icon: '⇄',
    title: 'Instant Transfers',
    desc: 'Move money between accounts with concurrency-safe, reliable processing.',
  },
  {
    icon: '📋',
    title: 'Simple Onboarding',
    desc: 'Open a new account in minutes with a guided registration flow.',
  },
  {
    icon: '✓',
    title: 'Built for Accuracy',
    desc: 'Every transaction is validated end-to-end, with clear confirmations.',
  },
];

function LandingPage() {
  return (
    <div className="landing">
      <nav className="landing-nav">
        <div className="landing-brand">
          <span className="landing-brand-mark">B</span>
          <span className="landing-brand-name">BankApp</span>
        </div>
        <div className="landing-nav-actions">
          <Link to="/login" className="landing-nav-login">Login</Link>
          <Link to="/customers" className="landing-nav-register">Register</Link>
        </div>
      </nav>

      <section className="landing-hero">
        <h1 className="landing-headline">
          Banking that's simple,<br />secure, and built to last.
        </h1>
        <p className="landing-subtext">
          Open an account, manage your money, and move funds with confidence —
          all backed by secure, concurrency-safe transaction processing.
        </p>
        <div className="landing-cta">
          <Link to="/customers" className="landing-btn-primary">Open an Account</Link>
          <Link to="/login" className="landing-btn-secondary">Login</Link>
        </div>
      </section>

      <section className="landing-features">
        {FEATURES.map((f) => (
          <div key={f.title} className="landing-feature-card">
            <span className="landing-feature-icon">{f.icon}</span>
            <h3 className="landing-feature-title">{f.title}</h3>
            <p className="landing-feature-desc">{f.desc}</p>
          </div>
        ))}
      </section>

      <footer className="landing-footer">
        <p>Banking Management System — built as a final-year academic project.</p>
      </footer>
    </div>
  );
}

export default LandingPage;