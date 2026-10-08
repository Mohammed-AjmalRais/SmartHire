import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { Building2, MapPin, Briefcase, IndianRupee, Calendar, Users, ArrowLeft, CheckCircle, ExternalLink } from 'lucide-react';
import type { Job } from '../types';
import api from '../api/client';
import { ApplyModal } from '../components/ApplyModal';
import { CompanyLogo } from '../components/CompanyLogo';

export const JobDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [job, setJob] = useState<Job | null>(null);
  const [loading, setLoading] = useState(true);
  const [showApplyModal, setShowApplyModal] = useState(false);
  const [hasApplied, setHasApplied] = useState(false);

  useEffect(() => {
    if (!id) return;
    setLoading(true);
    api.get(`/jobs/${id}`)
      .then((res) => {
        if (res.data.success) {
          setJob(res.data.data);
        }
      })
      .catch(() => {})
      .finally(() => {
        setLoading(false);
      });
  }, [id]);

  if (loading) {
    return (
      <div className="min-h-[50vh] flex items-center justify-center">
        <div className="w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin"></div>
      </div>
    );
  }

  if (!job) {
    return (
      <div className="max-w-3xl mx-auto px-4 py-16 text-center">
        <h2 className="text-xl font-bold text-slate-800">Job Not Found</h2>
        <p className="text-sm text-slate-500 mt-2">This vacancy may have been removed or expired.</p>
        <Link to="/jobs" className="mt-4 inline-flex items-center gap-1.5 text-xs font-semibold text-indigo-600 hover:underline">
          <ArrowLeft className="w-4 h-4" /> Back to all jobs
        </Link>
      </div>
    );
  }

  const formatSalary = () => {
    if (!job.minSalary && !job.maxSalary) return 'Competitive Salary';
    if (job.minSalary && job.maxSalary) {
      return `₹${(job.minSalary / 100000).toFixed(1)}L - ₹${(job.maxSalary / 100000).toFixed(1)}L Per Annum`;
    }
    if (job.minSalary) return `From ₹${(job.minSalary / 100000).toFixed(1)}L Per Annum`;
    return `Up to ₹${(job.maxSalary! / 100000).toFixed(1)}L Per Annum`;
  };

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      <Link to="/jobs" className="inline-flex items-center gap-1 text-xs font-semibold text-slate-500 hover:text-indigo-600 transition-colors">
        <ArrowLeft className="w-3.5 h-3.5" /> Back to job search
      </Link>

      {/* Header Card */}
      <div className="bg-white rounded-2xl border border-slate-200 p-6 sm:p-8 shadow-xs">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-6">
          <div className="flex gap-4 items-start">
            <CompanyLogo name={job.companyName} logoUrl={job.companyLogoUrl} size="lg" />

            <div>
              <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 tracking-tight">{job.title}</h1>
              <div className="flex items-center gap-2 mt-1">
                <span className="text-sm font-semibold text-slate-700">{job.companyName}</span>
                {job.companyVerified && (
                  <span className="inline-flex items-center gap-0.5 text-xs text-emerald-600 bg-emerald-50 px-2 py-0.5 rounded-full font-medium">
                    <CheckCircle className="w-3 h-3" /> Verified Company
                  </span>
                )}
                {job.companyIndustry && (
                  <span className="text-xs text-slate-400 hidden sm:inline">• {job.companyIndustry}</span>
                )}
              </div>
            </div>
          </div>

          <div className="shrink-0 flex sm:flex-col items-center sm:items-end justify-between gap-3 pt-4 sm:pt-0 border-t sm:border-t-0 border-slate-100">
            <button
              onClick={() => setShowApplyModal(true)}
              disabled={hasApplied}
              className={`w-full sm:w-auto px-6 py-2.5 text-sm font-semibold rounded-xl shadow-sm transition-colors cursor-pointer ${
                hasApplied
                  ? 'bg-emerald-50 text-emerald-700 border border-emerald-200 cursor-default'
                  : 'bg-indigo-600 text-white hover:bg-indigo-700'
              }`}
            >
              {hasApplied ? 'Application Submitted' : 'Apply for this Job'}
            </button>
            <span className="text-[11px] text-slate-400">
              Deadline: {new Date(job.applicationDeadline).toLocaleDateString([], { month: 'long', day: 'numeric', year: 'numeric' })}
            </span>
          </div>
        </div>

        {/* Quick specs */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 mt-8 pt-6 border-t border-slate-100 text-xs">
          <div>
            <span className="text-slate-400 block font-medium">Location & Mode</span>
            <span className="text-slate-800 font-semibold mt-0.5 block">
              {job.location} ({job.workMode.replace('_', ' ')})
            </span>
          </div>
          <div>
            <span className="text-slate-400 block font-medium">Experience Level</span>
            <span className="text-slate-800 font-semibold mt-0.5 block">
              {job.minExperience != null ? `${job.minExperience}–${job.maxExperience ?? '+'} Years` : 'Any'}
            </span>
          </div>
          <div>
            <span className="text-slate-400 block font-medium">Offered CTC / Salary</span>
            <span className="text-slate-800 font-semibold mt-0.5 block">{formatSalary()}</span>
          </div>
          <div>
            <span className="text-slate-400 block font-medium">Employment Type</span>
            <span className="text-slate-800 font-semibold mt-0.5 block">{job.employmentType.replace('_', ' ')}</span>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Main Details */}
        <div className="lg:col-span-2 space-y-6">
          <div className="bg-white rounded-2xl border border-slate-200 p-6 space-y-4">
            <h3 className="text-base font-bold text-slate-900">Job Description</h3>
            <div className="text-xs sm:text-sm text-slate-600 whitespace-pre-line leading-relaxed">
              {job.description}
            </div>

            {job.requiredSkills && job.requiredSkills.length > 0 && (
              <div className="pt-4 border-t border-slate-100">
                <h4 className="text-xs font-bold text-slate-800 mb-2.5">Required Skills & Technologies</h4>
                <div className="flex flex-wrap gap-2">
                  {job.requiredSkills.map((skill, idx) => (
                    <span key={idx} className="text-xs px-3 py-1 bg-indigo-50/70 text-indigo-700 font-medium rounded-lg border border-indigo-100">
                      {skill}
                    </span>
                  ))}
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Sidebar details */}
        <div className="space-y-6">
          <div className="bg-white rounded-2xl border border-slate-200 p-5 space-y-4">
            <h3 className="text-sm font-bold text-slate-900">Hiring Organization</h3>
            <p className="text-xs text-slate-600 leading-relaxed">
              {job.companyName} is hiring for {job.openings} open position{job.openings > 1 ? 's' : ''}.
            </p>
            <div className="text-xs space-y-2 pt-2 border-t border-slate-100">
              <div className="flex justify-between text-slate-500">
                <span>Total Applications:</span>
                <span className="font-semibold text-slate-800">{job.totalApplicationsCount}</span>
              </div>
              <div className="flex justify-between text-slate-500">
                <span>Posted by:</span>
                <span className="font-semibold text-slate-800">{job.recruiterName}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <ApplyModal
        job={job}
        isOpen={showApplyModal}
        onClose={() => setShowApplyModal(false)}
        onSuccess={() => {
          setHasApplied(true);
          alert('Congratulations! Your application has been submitted successfully.');
        }}
      />
    </div>
  );
};
