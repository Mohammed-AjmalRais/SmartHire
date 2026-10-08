import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Briefcase, PlusCircle, Users, Clock, MapPin, Eye, XCircle } from 'lucide-react';
import type { Job } from '../../types';
import api from '../../api/client';

export const ManageJobsPage: React.FC = () => {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchJobs = () => {
    setLoading(true);
    api.get('/recruiter/jobs')
      .then((res) => {
        if (res.data.success) {
          setJobs(res.data.data);
        }
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchJobs();
  }, []);

  const handleCloseJob = (id: number) => {
    if (!confirm('Are you sure you want to close this job vacancy? Candidates will no longer be able to submit applications.')) return;
    api.put(`/recruiter/jobs/${id}/close`)
      .then(() => {
        fetchJobs();
      })
      .catch((err) => {
        alert(err.response?.data?.message || 'Error closing job');
      });
  };

  if (loading) {
    return (
      <div className="min-h-[50vh] flex items-center justify-center">
        <div className="w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin"></div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Manage Job Vacancies</h1>
          <p className="text-xs text-slate-500 mt-0.5">
            Overview of company job postings, applicant counts, and status control
          </p>
        </div>

        <Link
          to="/recruiter/post-job"
          className="inline-flex items-center gap-1.5 px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold text-xs rounded-xl shadow-xs transition-colors shrink-0"
        >
          <PlusCircle className="w-4 h-4" /> Post New Vacancy
        </Link>
      </div>

      {jobs.length === 0 ? (
        <div className="bg-white p-12 rounded-2xl border border-slate-200 text-center">
          <Briefcase className="w-12 h-12 text-slate-300 mx-auto mb-3" />
          <h3 className="text-base font-bold text-slate-800">No vacancies created yet</h3>
          <p className="text-xs text-slate-500 mt-1 max-w-sm mx-auto">
            Get started by posting your company's first job opening.
          </p>
          <Link
            to="/recruiter/post-job"
            className="mt-4 inline-block px-4 py-2 bg-indigo-600 text-white text-xs font-semibold rounded-xl hover:bg-indigo-700"
          >
            Create Job Post
          </Link>
        </div>
      ) : (
        <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden shadow-xs">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead className="bg-slate-50 border-b border-slate-200 text-slate-600 uppercase font-semibold">
                <tr>
                  <th className="px-5 py-3.5">Job Title & Location</th>
                  <th className="px-5 py-3.5">Work Mode / Type</th>
                  <th className="px-5 py-3.5">Applicants</th>
                  <th className="px-5 py-3.5">Status</th>
                  <th className="px-5 py-3.5">Deadline</th>
                  <th className="px-5 py-3.5 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-slate-700">
                {jobs.map((job) => (
                  <tr key={job.id} className="hover:bg-slate-50/50 transition-colors">
                    <td className="px-5 py-4">
                      <Link to={`/jobs/${job.id}`} className="font-bold text-slate-900 hover:text-indigo-600 block text-sm">
                        {job.title}
                      </Link>
                      <div className="flex items-center gap-1.5 text-slate-400 text-[11px] mt-0.5">
                        <MapPin className="w-3 h-3" />
                        <span>{job.location}</span>
                      </div>
                    </td>

                    <td className="px-5 py-4">
                      <span className="font-semibold text-slate-800">{job.workMode.replace('_', ' ')}</span>
                      <span className="text-slate-400 block text-[11px]">{job.employmentType.replace('_', ' ')}</span>
                    </td>

                    <td className="px-5 py-4">
                      <Link
                        to={`/recruiter/applications?jobId=${job.id}`}
                        className="inline-flex items-center gap-1.5 px-2.5 py-1 bg-indigo-50 hover:bg-indigo-100 text-indigo-700 font-bold rounded-lg transition-colors"
                      >
                        <Users className="w-3.5 h-3.5" />
                        <span>{job.totalApplicationsCount} applicants</span>
                      </Link>
                    </td>

                    <td className="px-5 py-4">
                      <span className={`px-2.5 py-0.5 rounded-full font-bold text-[10px] uppercase ${
                        job.status === 'PUBLISHED'
                          ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                          : 'bg-slate-100 text-slate-600 border border-slate-200'
                      }`}>
                        {job.status}
                      </span>
                    </td>

                    <td className="px-5 py-4 text-slate-500">
                      {new Date(job.applicationDeadline).toLocaleDateString([], {
                        month: 'short',
                        day: 'numeric',
                        year: 'numeric',
                      })}
                    </td>

                    <td className="px-5 py-4 text-right">
                      <div className="flex items-center justify-end gap-2">
                        <Link
                          to={`/jobs/${job.id}`}
                          className="p-1.5 text-slate-400 hover:text-indigo-600 hover:bg-slate-100 rounded-lg transition-colors"
                          title="View Vacancy Public Details"
                        >
                          <Eye className="w-4 h-4" />
                        </Link>
                        {job.status === 'PUBLISHED' && (
                          <button
                            onClick={() => handleCloseJob(job.id)}
                            className="p-1.5 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-lg transition-colors cursor-pointer"
                            title="Close Vacancy"
                          >
                            <XCircle className="w-4 h-4" />
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
};
