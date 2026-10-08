import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Briefcase, User as UserIcon, LogOut, Shield, Building, FileText, Calendar, PlusCircle, Menu, X } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { NotificationDropdown } from './NotificationDropdown';

export const Navbar: React.FC = () => {
  const { user, isAuthenticated, isCandidate, isRecruiter, isAdmin, logout } = useAuth();
  const navigate = useNavigate();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="bg-white border-b border-slate-200 sticky top-0 z-40">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center gap-8">
            <Link to="/" className="flex items-center gap-2.5 group">
              <div className="w-9 h-9 rounded-xl bg-slate-950 flex items-center justify-center text-white shadow-xs ring-1 ring-slate-800/80 group-hover:bg-indigo-950 transition-colors">
                <span className="font-mono font-bold text-sm tracking-tight select-none">
                  S<span className="text-indigo-400">H</span>
                </span>
              </div>
              <span className="text-lg font-bold tracking-tight text-slate-900">
                Smart<span className="text-indigo-600">Hire</span>
              </span>
            </Link>

            <div className="hidden md:flex items-center space-x-1">
              <Link to="/jobs" className="px-3 py-2 text-sm font-medium text-slate-600 hover:text-indigo-600 rounded-md hover:bg-slate-50 transition-colors">
                Find Jobs
              </Link>

              {isCandidate && (
                <>
                  <Link to="/candidate/dashboard" className="px-3 py-2 text-sm font-medium text-slate-600 hover:text-indigo-600 rounded-md hover:bg-slate-50 transition-colors">
                    Dashboard
                  </Link>
                  <Link to="/candidate/applications" className="px-3 py-2 text-sm font-medium text-slate-600 hover:text-indigo-600 rounded-md hover:bg-slate-50 transition-colors">
                    My Applications
                  </Link>
                  <Link to="/candidate/interviews" className="px-3 py-2 text-sm font-medium text-slate-600 hover:text-indigo-600 rounded-md hover:bg-slate-50 transition-colors">
                    Interviews
                  </Link>
                  <Link to="/candidate/profile" className="px-3 py-2 text-sm font-medium text-slate-600 hover:text-indigo-600 rounded-md hover:bg-slate-50 transition-colors">
                    Profile & Resume
                  </Link>
                </>
              )}

              {isRecruiter && (
                <>
                  <Link to="/recruiter/dashboard" className="px-3 py-2 text-sm font-medium text-slate-600 hover:text-indigo-600 rounded-md hover:bg-slate-50 transition-colors">
                    Recruiter Hub
                  </Link>
                  <Link to="/recruiter/jobs" className="px-3 py-2 text-sm font-medium text-slate-600 hover:text-indigo-600 rounded-md hover:bg-slate-50 transition-colors">
                    Manage Jobs
                  </Link>
                  <Link to="/recruiter/applications" className="px-3 py-2 text-sm font-medium text-slate-600 hover:text-indigo-600 rounded-md hover:bg-slate-50 transition-colors">
                    Applications
                  </Link>
                  <Link to="/recruiter/company" className="px-3 py-2 text-sm font-medium text-slate-600 hover:text-indigo-600 rounded-md hover:bg-slate-50 transition-colors">
                    Company
                  </Link>
                </>
              )}

              {isAdmin && (
                <>
                  <Link to="/admin/dashboard" className="px-3 py-2 text-sm font-medium text-slate-600 hover:text-indigo-600 rounded-md hover:bg-slate-50 transition-colors flex items-center gap-1.5">
                    <Shield className="w-4 h-4 text-indigo-500" /> Admin Console
                  </Link>
                  <Link to="/admin/companies" className="px-3 py-2 text-sm font-medium text-slate-600 hover:text-indigo-600 rounded-md hover:bg-slate-50 transition-colors">
                    Verify Companies
                  </Link>
                  <Link to="/admin/users" className="px-3 py-2 text-sm font-medium text-slate-600 hover:text-indigo-600 rounded-md hover:bg-slate-50 transition-colors">
                    Users
                  </Link>
                </>
              )}
            </div>
          </div>

          <div className="hidden md:flex items-center space-x-3">
            {isAuthenticated && user ? (
              <>
                <NotificationDropdown />

                {isRecruiter && (
                  <Link
                    to="/recruiter/post-job"
                    className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold bg-indigo-600 text-white hover:bg-indigo-700 shadow-sm transition-colors"
                  >
                    <PlusCircle className="w-4 h-4" /> Post Vacancy
                  </Link>
                )}

                <div className="flex items-center gap-3 pl-3 border-l border-slate-200">
                  <div className="text-right">
                    <p className="text-sm font-medium text-slate-800 leading-tight">{user.fullName}</p>
                    <span className="text-[10px] font-semibold uppercase px-1.5 py-0.5 rounded bg-slate-100 text-slate-600 inline-block mt-0.5">
                      {user.role.replace('ROLE_', '')}
                    </span>
                  </div>

                  <button
                    onClick={handleLogout}
                    className="p-2 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-lg transition-colors cursor-pointer"
                    title="Log Out"
                  >
                    <LogOut className="w-5 h-5" />
                  </button>
                </div>
              </>
            ) : (
              <div className="flex items-center gap-2">
                <Link
                  to="/login"
                  className="px-4 py-2 text-sm font-medium text-slate-700 hover:text-indigo-600 rounded-lg transition-colors"
                >
                  Sign In
                </Link>
                <Link
                  to="/register"
                  className="px-4 py-2 text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 rounded-lg shadow-sm transition-colors"
                >
                  Get Started
                </Link>
              </div>
            )}
          </div>

          <div className="flex md:hidden items-center">
            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              className="p-2 rounded-lg text-slate-600 hover:bg-slate-100"
            >
              {mobileMenuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile Drawer */}
      {mobileMenuOpen && (
        <div className="md:hidden border-t border-slate-200 bg-white px-4 pt-2 pb-6 space-y-2">
          <Link to="/jobs" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50">
            Find Jobs
          </Link>

          {isCandidate && (
            <>
              <Link to="/candidate/dashboard" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50">
                Dashboard
              </Link>
              <Link to="/candidate/applications" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50">
                My Applications
              </Link>
              <Link to="/candidate/interviews" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50">
                Interviews
              </Link>
              <Link to="/candidate/profile" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50">
                Profile & Resume
              </Link>
            </>
          )}

          {isRecruiter && (
            <>
              <Link to="/recruiter/dashboard" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50">
                Recruiter Hub
              </Link>
              <Link to="/recruiter/jobs" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50">
                Manage Jobs
              </Link>
              <Link to="/recruiter/applications" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50">
                Applications
              </Link>
              <Link to="/recruiter/company" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50">
                Company Profile
              </Link>
            </>
          )}

          {isAdmin && (
            <>
              <Link to="/admin/dashboard" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50">
                Admin Console
              </Link>
              <Link to="/admin/companies" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50">
                Verify Companies
              </Link>
              <Link to="/admin/users" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-medium text-slate-700 hover:bg-slate-50">
                Manage Users
              </Link>
            </>
          )}

          <div className="pt-4 border-t border-slate-200">
            {isAuthenticated ? (
              <button
                onClick={() => {
                  handleLogout();
                  setMobileMenuOpen(false);
                }}
                className="w-full text-left px-3 py-2 rounded-md text-base font-medium text-rose-600 hover:bg-rose-50"
              >
                Log Out ({user?.fullName})
              </button>
            ) : (
              <div className="flex flex-col gap-2">
                <Link to="/login" onClick={() => setMobileMenuOpen(false)} className="text-center py-2 text-slate-700 font-medium">Sign In</Link>
                <Link to="/register" onClick={() => setMobileMenuOpen(false)} className="text-center py-2 bg-indigo-600 text-white rounded-lg font-medium">Get Started</Link>
              </div>
            )}
          </div>
        </div>
      )}
    </nav>
  );
};
