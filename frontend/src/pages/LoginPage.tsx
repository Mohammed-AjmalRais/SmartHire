import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { Briefcase, AlertCircle, KeyRound, Mail, ArrowRight } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import api from '../api/client';

export const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuth();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const from = (location.state as any)?.from?.pathname || '/';

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    api.post('/auth/login', { email, password })
      .then((res) => {
        if (res.data.success) {
          const { token, user } = res.data.data;
          login(token, user);
          if (user.role === 'ROLE_ADMIN') navigate('/admin/dashboard');
          else if (user.role === 'ROLE_RECRUITER') navigate('/recruiter/dashboard');
          else navigate('/candidate/dashboard');
        }
      })
      .catch((err) => {
        setError(err.response?.data?.message || 'Invalid email or password.');
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const handleDemoLogin = (demoEmail: string, demoPass: string) => {
    setEmail(demoEmail);
    setPassword(demoPass);
    setLoading(true);
    setError(null);

    api.post('/auth/login', { email: demoEmail, password: demoPass })
      .then((res) => {
        if (res.data.success) {
          const { token, user } = res.data.data;
          login(token, user);
          if (user.role === 'ROLE_ADMIN') navigate('/admin/dashboard');
          else if (user.role === 'ROLE_RECRUITER') navigate('/recruiter/dashboard');
          else navigate('/candidate/dashboard');
        }
      })
      .catch((err) => {
        setError(err.response?.data?.message || 'Login failed.');
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <div className="min-h-[80vh] flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8 bg-white p-8 rounded-3xl border border-slate-200 shadow-xl">
        <div className="text-center">
          <div className="w-12 h-12 rounded-2xl bg-indigo-600 flex items-center justify-center text-white mx-auto shadow-sm">
            <Briefcase className="w-6 h-6" />
          </div>
          <h2 className="mt-4 text-2xl font-extrabold text-slate-900 tracking-tight">Sign in to SmartHire</h2>
          <p className="mt-1 text-xs text-slate-500">
            Welcome back! Access your recruitment portal.
          </p>
        </div>

        {error && (
          <div className="p-3 bg-rose-50 border border-rose-200 text-rose-700 text-xs rounded-xl flex items-center gap-2">
            <AlertCircle className="w-4 h-4 shrink-0" />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1">Email Address</label>
            <div className="flex items-center px-3 py-2.5 rounded-xl border border-slate-200 bg-slate-50/50 gap-2">
              <Mail className="w-4 h-4 text-slate-400" />
              <input
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="name@company.com"
                className="w-full text-xs bg-transparent focus:outline-none"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1">Password</label>
            <div className="flex items-center px-3 py-2.5 rounded-xl border border-slate-200 bg-slate-50/50 gap-2">
              <KeyRound className="w-4 h-4 text-slate-400" />
              <input
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                className="w-full text-xs bg-transparent focus:outline-none"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold text-xs sm:text-sm rounded-xl shadow-sm transition-colors cursor-pointer flex items-center justify-center gap-2 disabled:opacity-50"
          >
            {loading ? 'Authenticating...' : 'Sign In'} <ArrowRight className="w-4 h-4" />
          </button>
        </form>

        {/* Demo Fast Login shortcuts */}
        <div className="pt-4 border-t border-slate-100">
          <p className="text-[11px] font-semibold text-slate-400 text-center uppercase tracking-wider mb-2.5">
            Instant Demo Sign In
          </p>
          <div className="grid grid-cols-3 gap-2">
            <button
              type="button"
              onClick={() => handleDemoLogin('john.doe@example.com', 'Candidate@123')}
              className="py-1.5 px-2 bg-slate-50 hover:bg-indigo-50 hover:text-indigo-600 text-slate-700 border border-slate-200 rounded-lg text-[11px] font-medium transition-colors cursor-pointer text-center"
            >
              Candidate
            </button>
            <button
              type="button"
              onClick={() => handleDemoLogin('recruiter@technova.com', 'Recruiter@123')}
              className="py-1.5 px-2 bg-slate-50 hover:bg-indigo-50 hover:text-indigo-600 text-slate-700 border border-slate-200 rounded-lg text-[11px] font-medium transition-colors cursor-pointer text-center"
            >
              Recruiter
            </button>
            <button
              type="button"
              onClick={() => handleDemoLogin('admin@smarthire.com', 'Admin@123')}
              className="py-1.5 px-2 bg-slate-50 hover:bg-indigo-50 hover:text-indigo-600 text-slate-700 border border-slate-200 rounded-lg text-[11px] font-medium transition-colors cursor-pointer text-center"
            >
              Admin
            </button>
          </div>
        </div>

        <p className="text-center text-xs text-slate-500">
          Don't have an account?{' '}
          <Link to="/register" className="font-semibold text-indigo-600 hover:underline">
            Register now
          </Link>
        </p>
      </div>
    </div>
  );
};
