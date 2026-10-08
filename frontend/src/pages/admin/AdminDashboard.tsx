import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Shield, Users, Building2, Briefcase, FileText, Calendar, CheckCircle2, AlertTriangle, ArrowRight } from 'lucide-react';
import type { AdminStats } from '../../types';
import api from '../../api/client';

export const AdminDashboard: React.FC = () => {
  const [stats, setStats] = useState<AdminStats | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/admin/stats')
      .then((res) => {
        if (res.data.success) {
          setStats(res.data.data);
        }
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <div className="min-h-[50vh] flex items-center justify-center">
        <div className="w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin"></div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <span className="text-xs uppercase font-semibold tracking-wider text-indigo-600 flex items-center gap-1.5">
            <Shield className="w-3.5 h-3.5" /> Platform Governance
          </span>
          <h1 className="text-2xl sm:text-3xl font-extrabold text-slate-900 mt-0.5">
            Administrator Console
          </h1>
          <p className="text-xs text-slate-500 mt-1">
            System overview, company verification queue, and user account management
          </p>
        </div>

        <div className="flex items-center gap-3">
          <Link
            to="/admin/companies"
            className="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold text-xs rounded-xl shadow-xs transition-colors"
          >
            Verify Companies ({stats?.pendingCompanies ?? 0} Pending)
          </Link>
          <Link
            to="/admin/users"
            className="px-4 py-2 bg-white hover:bg-slate-50 text-slate-700 font-semibold text-xs rounded-xl border border-slate-200 shadow-xs transition-colors"
          >
            Manage Users
          </Link>
        </div>
      </div>

      {/* Pending verification alert if pending companies exist */}
      {(stats?.pendingCompanies ?? 0) > 0 && (
        <div className="bg-amber-50 border border-amber-200 rounded-2xl p-4 flex items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <AlertTriangle className="w-5 h-5 text-amber-600 shrink-0" />
            <div className="text-xs text-amber-900">
              <span className="font-bold">
                {stats?.pendingCompanies} company registration{stats?.pendingCompanies! > 1 ? 's' : ''} waiting for approval
              </span>
              <p className="mt-0.5 text-amber-800">
                Recruiters from these companies cannot publish jobs until an administrator verifies them.
              </p>
            </div>
          </div>
          <Link
            to="/admin/companies"
            className="px-3 py-1.5 text-xs font-semibold bg-amber-600 hover:bg-amber-700 text-white rounded-lg transition-colors shrink-0"
          >
            Review Now
          </Link>
        </div>
      )}

      {/* Core Platform Analytics */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
        <div className="bg-white p-5 rounded-2xl border border-slate-200">
          <div className="w-8 h-8 rounded-lg bg-blue-50 text-blue-600 flex items-center justify-center mb-3">
            <Users className="w-4 h-4" />
          </div>
          <span className="text-2xl font-black text-slate-900">{stats?.totalUsers ?? 0}</span>
          <p className="text-xs text-slate-500 mt-0.5 font-medium">Total Registered Users</p>
          <div className="flex gap-2 text-[11px] text-slate-400 mt-2 pt-2 border-t border-slate-100">
            <span>{stats?.totalCandidates} Candidates</span>
            <span>•</span>
            <span>{stats?.totalRecruiters} Recruiters</span>
          </div>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200">
          <div className="w-8 h-8 rounded-lg bg-indigo-50 text-indigo-600 flex items-center justify-center mb-3">
            <Building2 className="w-4 h-4" />
          </div>
          <span className="text-2xl font-black text-slate-900">{stats?.totalCompanies ?? 0}</span>
          <p className="text-xs text-slate-500 mt-0.5 font-medium">Total Companies</p>
          <div className="flex gap-2 text-[11px] text-slate-400 mt-2 pt-2 border-t border-slate-100">
            <span className="text-emerald-600 font-semibold">{stats?.verifiedCompanies} Verified</span>
            <span>•</span>
            <span className="text-amber-600 font-semibold">{stats?.pendingCompanies} Pending</span>
          </div>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200">
          <div className="w-8 h-8 rounded-lg bg-purple-50 text-purple-600 flex items-center justify-center mb-3">
            <Briefcase className="w-4 h-4" />
          </div>
          <span className="text-2xl font-black text-slate-900">{stats?.activeJobs ?? 0}</span>
          <p className="text-xs text-slate-500 mt-0.5 font-medium">Active Vacancies</p>
          <div className="text-[11px] text-slate-400 mt-2 pt-2 border-t border-slate-100">
            From verified organizations
          </div>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200">
          <div className="w-8 h-8 rounded-lg bg-emerald-50 text-emerald-600 flex items-center justify-center mb-3">
            <CheckCircle2 className="w-4 h-4" />
          </div>
          <span className="text-2xl font-black text-emerald-600">{stats?.selectedCandidates ?? 0}</span>
          <p className="text-xs text-slate-500 mt-0.5 font-medium">Successful Selections</p>
          <div className="text-[11px] text-slate-400 mt-2 pt-2 border-t border-slate-100">
            {stats?.totalApplications} total applications submitted
          </div>
        </div>
      </div>

      {/* Secondary Pipeline Stats */}
      <div className="bg-white p-6 rounded-2xl border border-slate-200">
        <h3 className="text-sm font-bold text-slate-900 mb-4">Recruitment Pipeline Statistics</h3>
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 text-center">
          <div className="p-4 bg-slate-50 rounded-xl">
            <span className="text-xl font-bold text-slate-800">{stats?.totalApplications ?? 0}</span>
            <p className="text-xs text-slate-500 mt-1">Total Applications</p>
          </div>
          <div className="p-4 bg-purple-50 rounded-xl">
            <span className="text-xl font-bold text-purple-700">{stats?.shortlistedApplications ?? 0}</span>
            <p className="text-xs text-purple-600 mt-1">Shortlisted</p>
          </div>
          <div className="p-4 bg-indigo-50 rounded-xl">
            <span className="text-xl font-bold text-indigo-700">{stats?.scheduledInterviews ?? 0}</span>
            <p className="text-xs text-indigo-600 mt-1">Scheduled Interviews</p>
          </div>
          <div className="p-4 bg-emerald-50 rounded-xl">
            <span className="text-xl font-bold text-emerald-700">{stats?.selectedCandidates ?? 0}</span>
            <p className="text-xs text-emerald-600 mt-1">Candidates Selected</p>
          </div>
        </div>
      </div>
    </div>
  );
};
