import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Briefcase, Download, Calendar, Video, AlertCircle, Ban, CheckCircle2 } from 'lucide-react';
import type { Application } from '../../types';
import api from '../../api/client';
import { StatusBadge } from '../../components/StatusBadge';

export const MyApplicationsPage: React.FC = () => {
  const [applications, setApplications] = useState<Application[]>([]);
  const [loading, setLoading] = useState(true);
  const [withdrawingId, setWithdrawingId] = useState<number | null>(null);

  const fetchApplications = () => {
    setLoading(true);
    api.get('/candidate/applications')
      .then((res) => {
        if (res.data.success) {
          setApplications(res.data.data);
        }
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchApplications();
  }, []);

  const handleWithdraw = (id: number) => {
    if (!confirm('Are you sure you want to withdraw this application? This action cannot be reversed.')) return;
    setWithdrawingId(id);
    api.put(`/candidate/applications/${id}/withdraw`)
      .then(() => {
        fetchApplications();
      })
      .catch((err) => {
        alert(err.response?.data?.message || 'Error withdrawing application');
      })
      .finally(() => setWithdrawingId(null));
  };

  if (loading) {
    return (
      <div className="min-h-[50vh] flex items-center justify-center">
        <div className="w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin"></div>
      </div>
    );
  }

  const getStageIndex = (status: string) => {
    switch (status) {
      case 'APPLIED': return 1;
      case 'UNDER_REVIEW': return 2;
      case 'SHORTLISTED': return 3;
      case 'INTERVIEW_SCHEDULED': return 4;
      case 'SELECTED': return 5;
      case 'REJECTED': return -1;
      case 'WITHDRAWN': return -2;
      default: return 0;
    }
  };

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-900 tracking-tight">My Job Applications</h1>
        <p className="text-xs text-slate-500 mt-0.5">
          Track the lifecycle stages of your submissions, interview invitations, and outcomes.
        </p>
      </div>

      {applications.length === 0 ? (
        <div className="bg-white p-12 rounded-2xl border border-slate-200 text-center">
          <Briefcase className="w-12 h-12 text-slate-300 mx-auto mb-3" />
          <h3 className="text-base font-bold text-slate-800">No applications found</h3>
          <p className="text-xs text-slate-500 mt-1 max-w-sm mx-auto">
            You haven't applied to any job vacancies yet. Find matching jobs and submit your profile.
          </p>
          <Link
            to="/jobs"
            className="mt-4 inline-block px-4 py-2 bg-indigo-600 text-white text-xs font-semibold rounded-xl hover:bg-indigo-700 transition-colors"
          >
            Explore Available Positions
          </Link>
        </div>
      ) : (
        <div className="space-y-4">
          {applications.map((app) => {
            const stage = getStageIndex(app.status);
            const canWithdraw = app.status !== 'SELECTED' && app.status !== 'REJECTED' && app.status !== 'WITHDRAWN';

            return (
              <div key={app.id} className="bg-white rounded-2xl border border-slate-200 p-6 shadow-xs space-y-5">
                <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                  <div>
                    <div className="flex items-center gap-2">
                      <Link to={`/jobs/${app.jobId}`} className="text-base font-bold text-slate-900 hover:text-indigo-600 transition-colors">
                        {app.jobTitle}
                      </Link>
                      <StatusBadge status={app.status} />
                    </div>
                    <p className="text-xs font-semibold text-slate-600 mt-0.5">{app.companyName}</p>
                    <span className="text-[11px] text-slate-400 block mt-1">
                      Submitted on {new Date(app.appliedAt).toLocaleDateString([], { month: 'long', day: 'numeric', year: 'numeric' })}
                    </span>
                  </div>

                  <div className="flex items-center gap-2 shrink-0">
                    <a
                      href={`/api/reports/application/${app.id}/pdf`}
                      download
                      className="px-3 py-1.5 text-xs font-semibold text-slate-700 bg-slate-50 hover:bg-slate-100 border border-slate-200 rounded-lg flex items-center gap-1.5 transition-colors"
                    >
                      <Download className="w-3.5 h-3.5" /> PDF Summary
                    </a>
                    {canWithdraw && (
                      <button
                        onClick={() => handleWithdraw(app.id)}
                        disabled={withdrawingId === app.id}
                        className="px-3 py-1.5 text-xs font-medium text-rose-600 hover:bg-rose-50 border border-rose-200 rounded-lg transition-colors cursor-pointer"
                      >
                        {withdrawingId === app.id ? 'Withdrawing...' : 'Withdraw'}
                      </button>
                    )}
                  </div>
                </div>

                {/* Lifecycle Pipeline Progress Tracker */}
                <div className="pt-2">
                  <div className="grid grid-cols-5 gap-2 text-center">
                    {[
                      { step: 1, label: 'Applied' },
                      { step: 2, label: 'Under Review' },
                      { step: 3, label: 'Shortlisted' },
                      { step: 4, label: 'Interview' },
                      { step: 5, label: 'Offer / Selected' },
                    ].map((s) => {
                      const isPast = stage >= s.step;
                      const isCurrent = stage === s.step;
                      const isFailed = stage < 0;

                      return (
                        <div key={s.step} className="space-y-1">
                          <div
                            className={`h-1.5 rounded-full transition-all ${
                              isPast
                                ? 'bg-indigo-600'
                                : isFailed
                                ? 'bg-slate-200'
                                : 'bg-slate-100'
                            }`}
                          />
                          <span
                            className={`text-[10px] block font-semibold ${
                              isCurrent
                                ? 'text-indigo-600'
                                : isPast
                                ? 'text-slate-800'
                                : 'text-slate-400'
                            }`}
                          >
                            {s.label}
                          </span>
                        </div>
                      );
                    })}
                  </div>
                </div>

                {/* Additional Notices */}
                {app.rejectionReason && (
                  <div className="p-3 bg-rose-50 border border-rose-200 text-rose-800 text-xs rounded-xl flex items-start gap-2">
                    <AlertCircle className="w-4 h-4 text-rose-600 shrink-0 mt-0.5" />
                    <div>
                      <p className="font-semibold">Rejection Feedback:</p>
                      <p className="mt-0.5">{app.rejectionReason}</p>
                    </div>
                  </div>
                )}

                {/* Scheduled Interviews attached */}
                {app.interviews && app.interviews.length > 0 && (
                  <div className="p-3.5 bg-indigo-50/70 border border-indigo-100 rounded-xl space-y-2">
                    <span className="text-[11px] font-bold text-indigo-900 uppercase tracking-wider block">
                      Scheduled Interview Rounds
                    </span>
                    {app.interviews.map((iv) => (
                      <div key={iv.id} className="flex flex-col sm:flex-row sm:items-center justify-between gap-2 text-xs">
                        <div className="flex items-center gap-2">
                          <Calendar className="w-4 h-4 text-indigo-600" />
                          <span className="font-semibold text-slate-800">
                            {iv.interviewType} Interview
                          </span>
                          <span className="text-slate-500">
                            on {new Date(iv.interviewDate).toLocaleDateString()} at {iv.startTime.substring(0, 5)} - {iv.endTime.substring(0, 5)}
                          </span>
                        </div>
                        {iv.meetingLink && (
                          <a
                            href={iv.meetingLink}
                            target="_blank"
                            rel="noreferrer"
                            className="inline-flex items-center gap-1 text-xs font-semibold text-indigo-600 hover:underline"
                          >
                            <Video className="w-3.5 h-3.5" /> Meeting Link
                          </a>
                        )}
                      </div>
                    ))}
                  </div>
                )}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};
