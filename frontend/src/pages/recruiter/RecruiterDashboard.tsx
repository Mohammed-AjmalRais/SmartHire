import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Briefcase, Users, CheckCircle, Clock, Calendar, AlertTriangle, PlusCircle, ArrowRight, XCircle } from 'lucide-react';
import type { RecruiterStats, Company } from '../../types';
import api from '../../api/client';
import { useAuth } from '../../context/AuthContext';

export const RecruiterDashboard: React.FC = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState<RecruiterStats | null>(null);
  const [company, setCompany] = useState<Company | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      api.get('/recruiter/stats'),
      api.get('/recruiter/company'),
    ])
      .then(([statsRes, compRes]) => {
        if (statsRes.data.success) setStats(statsRes.data.data);
        if (compRes.data.success) setCompany(compRes.data.data);
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
      {/* Welcome Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <span className="text-xs uppercase font-semibold tracking-wider text-indigo-600">Recruiter Portal</span>
          <h1 className="text-2xl sm:text-3xl font-extrabold text-slate-900 mt-0.5">
            {company?.name ? `${company.name} Talent Hub` : 'Recruiter Hub'}
          </h1>
          <p className="text-xs text-slate-500 mt-1">
            Logged in as {user?.fullName} • {user?.email}
          </p>
        </div>

        <div className="flex items-center gap-3">
          <Link
            to="/recruiter/post-job"
            className="inline-flex items-center gap-1.5 px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold text-xs rounded-xl shadow-xs transition-colors"
          >
            <PlusCircle className="w-4 h-4" /> Post New Job
          </Link>
          <Link
            to="/recruiter/company"
            className="px-4 py-2 bg-white hover:bg-slate-50 text-slate-700 font-semibold text-xs rounded-xl border border-slate-200 shadow-xs transition-colors"
          >
            Company Profile
          </Link>
        </div>
      </div>

      {/* Company Verification Banner */}
      {company && !company.verified && (
        <div className="bg-amber-50 border border-amber-200 rounded-2xl p-4 flex items-start gap-3">
          <AlertTriangle className="w-5 h-5 text-amber-600 shrink-0 mt-0.5" />
          <div className="text-xs text-amber-900">
            <h4 className="font-bold">Company Pending Admin Verification</h4>
            <p className="mt-0.5 leading-relaxed">
              <strong>{company.name}</strong> is currently pending verification by platform administrators.
              As a security measure to protect job seekers from unverified job postings, you will be able to publish vacancies once an administrator approves your company.
            </p>
          </div>
        </div>
      )}

      {/* Recruitment Pipeline Metrics */}
      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-4">
        <div className="bg-white p-4 rounded-2xl border border-slate-200">
          <span className="text-2xl font-black text-slate-900">{stats?.activeJobs ?? 0}</span>
          <p className="text-xs text-slate-500 mt-0.5 font-medium">Active Vacancies</p>
        </div>

        <div className="bg-white p-4 rounded-2xl border border-slate-200">
          <span className="text-2xl font-black text-blue-600">{stats?.totalApplications ?? 0}</span>
          <p className="text-xs text-slate-500 mt-0.5 font-medium">Total Applicants</p>
        </div>

        <div className="bg-white p-4 rounded-2xl border border-slate-200">
          <span className="text-2xl font-black text-amber-600">{stats?.underReviewApplications ?? 0}</span>
          <p className="text-xs text-slate-500 mt-0.5 font-medium">Under Review</p>
        </div>

        <div className="bg-white p-4 rounded-2xl border border-slate-200">
          <span className="text-2xl font-black text-purple-600">{stats?.shortlistedApplications ?? 0}</span>
          <p className="text-xs text-slate-500 mt-0.5 font-medium">Shortlisted</p>
        </div>

        <div className="bg-white p-4 rounded-2xl border border-slate-200">
          <span className="text-2xl font-black text-indigo-600">{stats?.scheduledInterviews ?? 0}</span>
          <p className="text-xs text-slate-500 mt-0.5 font-medium">Interviews</p>
        </div>

        <div className="bg-white p-4 rounded-2xl border border-slate-200">
          <span className="text-2xl font-black text-emerald-600">{stats?.selectedCandidates ?? 0}</span>
          <p className="text-xs text-slate-500 mt-0.5 font-medium">Selected / Offers</p>
        </div>
      </div>

      {/* Quick Action Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Link
          to="/recruiter/applications"
          className="bg-white p-6 rounded-2xl border border-slate-200 hover:border-indigo-300 hover:shadow-md transition-all group"
        >
          <div className="w-10 h-10 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center mb-4">
            <Users className="w-5 h-5" />
          </div>
          <h3 className="text-base font-bold text-slate-900 group-hover:text-indigo-600 transition-colors">
            Candidate Pipeline
          </h3>
          <p className="text-xs text-slate-500 mt-1 leading-relaxed">
            Review candidate resumes, advance status through recruitment stages, provide feedback, and schedule interviews.
          </p>
          <div className="mt-4 flex items-center gap-1 text-xs font-semibold text-indigo-600">
            View All Applications <ArrowRight className="w-3.5 h-3.5" />
          </div>
        </Link>

        <Link
          to="/recruiter/jobs"
          className="bg-white p-6 rounded-2xl border border-slate-200 hover:border-indigo-300 hover:shadow-md transition-all group"
        >
          <div className="w-10 h-10 rounded-xl bg-purple-50 text-purple-600 flex items-center justify-center mb-4">
            <Briefcase className="w-5 h-5" />
          </div>
          <h3 className="text-base font-bold text-slate-900 group-hover:text-indigo-600 transition-colors">
            Manage Vacancies
          </h3>
          <p className="text-xs text-slate-500 mt-1 leading-relaxed">
            Publish new job descriptions, update requirements and salaries, or close filled vacancies.
          </p>
          <div className="mt-4 flex items-center gap-1 text-xs font-semibold text-indigo-600">
            Manage Jobs <ArrowRight className="w-3.5 h-3.5" />
          </div>
        </Link>

        <Link
          to="/recruiter/company"
          className="bg-white p-6 rounded-2xl border border-slate-200 hover:border-indigo-300 hover:shadow-md transition-all group"
        >
          <div className="w-10 h-10 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center mb-4">
            <CheckCircle className="w-5 h-5" />
          </div>
          <h3 className="text-base font-bold text-slate-900 group-hover:text-indigo-600 transition-colors">
            Company Branding
          </h3>
          <p className="text-xs text-slate-500 mt-1 leading-relaxed">
            Configure company overview, website, industry, office location, and brand logo.
          </p>
          <div className="mt-4 flex items-center gap-1 text-xs font-semibold text-indigo-600">
            Edit Company <ArrowRight className="w-3.5 h-3.5" />
          </div>
        </Link>
      </div>
    </div>
  );
};
