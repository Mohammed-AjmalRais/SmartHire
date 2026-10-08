import React from 'react';
import { Link } from 'react-router-dom';
import { Building2, MapPin, Briefcase, IndianRupee, Clock, CheckCircle } from 'lucide-react';
import type { Job } from '../types';
import { CompanyLogo } from './CompanyLogo';

interface JobCardProps {
  job: Job;
  onApplyClick?: (job: Job) => void;
  hasApplied?: boolean;
}

export const JobCard: React.FC<JobCardProps> = ({ job, onApplyClick, hasApplied = false }) => {
  const formatSalary = () => {
    if (!job.minSalary && !job.maxSalary) return 'Competitive';
    if (job.minSalary && job.maxSalary) {
      return `₹${(job.minSalary / 100000).toFixed(1)}L - ₹${(job.maxSalary / 100000).toFixed(1)}L PA`;
    }
    if (job.minSalary) return `From ₹${(job.minSalary / 100000).toFixed(1)}L PA`;
    return `Up to ₹${(job.maxSalary! / 100000).toFixed(1)}L PA`;
  };

  return (
    <div className="bg-white rounded-xl border border-slate-200 p-5 hover:border-indigo-300 hover:shadow-md transition-all duration-200 flex flex-col justify-between">
      <div>
        <div className="flex items-start justify-between gap-3">
          <div className="flex gap-3.5">
            <CompanyLogo name={job.companyName} logoUrl={job.companyLogoUrl} size="md" />

            <div>
              <Link to={`/jobs/${job.id}`} className="text-base font-semibold text-slate-900 hover:text-indigo-600 transition-colors line-clamp-1">
                {job.title}
              </Link>
              <div className="flex items-center gap-1.5 mt-0.5">
                <span className="text-xs font-medium text-slate-600">{job.companyName}</span>
                {job.companyVerified && (
                  <span title="Verified Company" className="text-emerald-600">
                    <CheckCircle className="w-3.5 h-3.5" />
                  </span>
                )}
              </div>
            </div>
          </div>

          <span className="text-[11px] font-semibold px-2 py-0.5 rounded-full bg-slate-100 text-slate-600 uppercase tracking-wider shrink-0">
            {job.workMode.replace('_', ' ')}
          </span>
        </div>

        <div className="grid grid-cols-2 sm:grid-cols-3 gap-2 mt-4 text-xs text-slate-600">
          <div className="flex items-center gap-1.5">
            <MapPin className="w-3.5 h-3.5 text-slate-400 shrink-0" />
            <span className="truncate">{job.location}</span>
          </div>
          <div className="flex items-center gap-1.5">
            <Briefcase className="w-3.5 h-3.5 text-slate-400 shrink-0" />
            <span>{job.minExperience != null ? `${job.minExperience}–${job.maxExperience ?? '+'} Yrs` : 'Fresher / Any'}</span>
          </div>
          <div className="flex items-center gap-1.5 col-span-2 sm:col-span-1">
            <IndianRupee className="w-3.5 h-3.5 text-slate-400 shrink-0" />
            <span className="truncate">{formatSalary()}</span>
          </div>
        </div>

        <p className="text-xs text-slate-500 mt-3 line-clamp-2 leading-relaxed">
          {job.description}
        </p>

        {job.requiredSkills && job.requiredSkills.length > 0 && (
          <div className="flex flex-wrap gap-1.5 mt-3">
            {job.requiredSkills.slice(0, 4).map((skill, idx) => (
              <span key={idx} className="text-[11px] bg-slate-100 text-slate-700 px-2 py-0.5 rounded font-medium">
                {skill}
              </span>
            ))}
            {job.requiredSkills.length > 4 && (
              <span className="text-[11px] text-slate-400 py-0.5 font-medium">
                +{job.requiredSkills.length - 4} more
              </span>
            )}
          </div>
        )}
      </div>

      <div className="flex items-center justify-between pt-4 mt-4 border-t border-slate-100">
        <div className="flex items-center gap-1 text-[11px] text-slate-400">
          <Clock className="w-3 h-3" />
          <span>Deadline: {new Date(job.applicationDeadline).toLocaleDateString([], { month: 'short', day: 'numeric' })}</span>
        </div>

        <div className="flex items-center gap-2">
          <Link
            to={`/jobs/${job.id}`}
            className="px-3 py-1.5 text-xs font-medium text-slate-600 hover:text-indigo-600 hover:bg-slate-50 rounded-lg transition-colors"
          >
            Details
          </Link>
          {onApplyClick && (
            <button
              onClick={() => onApplyClick(job)}
              disabled={hasApplied}
              className={`px-3 py-1.5 text-xs font-semibold rounded-lg shadow-sm transition-colors cursor-pointer ${
                hasApplied
                  ? 'bg-emerald-50 text-emerald-700 border border-emerald-200 cursor-default'
                  : 'bg-indigo-600 text-white hover:bg-indigo-700'
              }`}
            >
              {hasApplied ? 'Applied' : 'Apply Now'}
            </button>
          )}
        </div>
      </div>
    </div>
  );
};
