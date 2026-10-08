import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Users, FileText, Download, Calendar, Check, X, MessageSquare, AlertCircle, Eye, ArrowRight } from 'lucide-react';
import type { Application, ApplicationStatus, Job } from '../../types';
import api from '../../api/client';
import { StatusBadge } from '../../components/StatusBadge';
import { ScheduleInterviewModal } from './ScheduleInterviewModal';

export const RecruiterApplicationsPage: React.FC = () => {
  const [searchParams] = useSearchParams();
  const initialJobId = searchParams.get('jobId');

  const [applications, setApplications] = useState<Application[]>([]);
  const [jobs, setJobs] = useState<Job[]>([]);
  const [selectedJobId, setSelectedJobId] = useState<string>(initialJobId || '');
  const [selectedStatus, setSelectedStatus] = useState<string>('');
  const [loading, setLoading] = useState(true);

  // Modals / Dialog state
  const [activeInterviewApp, setActiveInterviewApp] = useState<Application | null>(null);
  const [rejectingApp, setRejectingApp] = useState<Application | null>(null);
  const [rejectionReason, setRejectionReason] = useState('');
  const [notesApp, setNotesApp] = useState<Application | null>(null);
  const [recruiterNotes, setRecruiterNotes] = useState('');

  const fetchJobs = () => {
    api.get('/recruiter/jobs')
      .then((res) => {
        if (res.data.success) {
          setJobs(res.data.data);
        }
      })
      .catch(() => {});
  };

  const fetchApplications = () => {
    setLoading(true);
    const endpoint = selectedJobId
      ? `/recruiter/jobs/${selectedJobId}/applications`
      : '/recruiter/applications';

    api.get(endpoint)
      .then((res) => {
        if (res.data.success) {
          setApplications(res.data.data);
        }
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchJobs();
  }, []);

  useEffect(() => {
    fetchApplications();
  }, [selectedJobId]);

  const handleUpdateStatus = (appId: number, status: ApplicationStatus, reason?: string, notes?: string) => {
    api.put(`/recruiter/applications/${appId}/status`, {
      status,
      rejectionReason: reason,
      recruiterNotes: notes,
    })
      .then((res) => {
        if (res.data.success) {
          setApplications((prev) =>
            prev.map((a) => (a.id === appId ? res.data.data : a))
          );
        }
      })
      .catch((err) => {
        alert(err.response?.data?.message || 'Error updating application status');
      });
  };

  const submitRejection = () => {
    if (!rejectingApp) return;
    handleUpdateStatus(rejectingApp.id, 'REJECTED', rejectionReason);
    setRejectingApp(null);
    setRejectionReason('');
  };

  const submitNotes = () => {
    if (!notesApp) return;
    handleUpdateStatus(notesApp.id, notesApp.status, undefined, recruiterNotes);
    setNotesApp(null);
    setRecruiterNotes('');
  };

  const filteredApplications = applications.filter((a) => {
    if (selectedStatus && a.status !== selectedStatus) return false;
    return true;
  });

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Recruitment Applicants</h1>
          <p className="text-xs text-slate-500 mt-0.5">
            Review applicant profiles, inspect resumes, transition recruitment stages, and schedule interviews
          </p>
        </div>

        {/* Filter controls */}
        <div className="flex flex-wrap items-center gap-3">
          <select
            value={selectedJobId}
            onChange={(e) => setSelectedJobId(e.target.value)}
            className="text-xs p-2.5 rounded-xl border border-slate-200 bg-white"
          >
            <option value="">All Job Vacancies</option>
            {jobs.map((j) => (
              <option key={j.id} value={j.id}>
                {j.title}
              </option>
            ))}
          </select>

          <select
            value={selectedStatus}
            onChange={(e) => setSelectedStatus(e.target.value)}
            className="text-xs p-2.5 rounded-xl border border-slate-200 bg-white"
          >
            <option value="">All Stages</option>
            <option value="APPLIED">Applied</option>
            <option value="UNDER_REVIEW">Under Review</option>
            <option value="SHORTLISTED">Shortlisted</option>
            <option value="INTERVIEW_SCHEDULED">Interview Scheduled</option>
            <option value="SELECTED">Selected</option>
            <option value="REJECTED">Rejected</option>
            <option value="WITHDRAWN">Withdrawn</option>
          </select>
        </div>
      </div>

      {loading ? (
        <div className="bg-white p-12 rounded-2xl border border-slate-200 text-center">
          <div className="w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin mx-auto mb-3"></div>
          <p className="text-xs text-slate-500">Loading applicants...</p>
        </div>
      ) : filteredApplications.length === 0 ? (
        <div className="bg-white p-12 rounded-2xl border border-slate-200 text-center">
          <Users className="w-12 h-12 text-slate-300 mx-auto mb-3" />
          <h3 className="text-base font-bold text-slate-800">No applicants found</h3>
          <p className="text-xs text-slate-500 mt-1 max-w-sm mx-auto">
            {selectedStatus || selectedJobId
              ? 'No applicants match the selected job vacancy and status filters.'
              : 'Candidates who apply to your vacancies will appear in this pipeline.'}
          </p>
        </div>
      ) : (
        <div className="space-y-4">
          {filteredApplications.map((app) => (
            <div key={app.id} className="bg-white rounded-2xl border border-slate-200 p-6 shadow-xs space-y-4">
              <div className="flex flex-col md:flex-row md:items-start justify-between gap-4">
                <div>
                  <div className="flex items-center gap-2.5">
                    <h3 className="text-base font-bold text-slate-900">{app.candidateName}</h3>
                    <StatusBadge status={app.status} />
                  </div>
                  <div className="flex flex-wrap items-center gap-x-3 gap-y-1 text-xs text-slate-500 mt-1">
                    <span>{app.candidateEmail}</span>
                    {app.candidatePhone && <span>• {app.candidatePhone}</span>}
                    <span>• Applied for <strong className="text-slate-700">{app.jobTitle}</strong></span>
                    <span>• {new Date(app.appliedAt).toLocaleDateString([], { month: 'short', day: 'numeric', year: 'numeric' })}</span>
                  </div>
                </div>

                {/* Candidate Attachments and Reports */}
                <div className="flex flex-wrap items-center gap-2">
                  {app.resumeId ? (
                    <a
                      href={`/api/resumes/${app.resumeId}/download`}
                      download
                      className="px-3 py-1.5 text-xs font-semibold text-indigo-700 bg-indigo-50 hover:bg-indigo-100 border border-indigo-200 rounded-lg flex items-center gap-1.5 transition-colors"
                      title="Download Candidate Resume (PDF/DOCX)"
                    >
                      <Download className="w-3.5 h-3.5" /> Download Resume
                    </a>
                  ) : (
                    <span className="text-xs text-slate-400 italic">No resume attached</span>
                  )}

                  <a
                    href={`/api/reports/candidate/${app.candidateId}/pdf`}
                    download
                    className="px-3 py-1.5 text-xs font-semibold text-slate-700 bg-slate-50 hover:bg-slate-100 border border-slate-200 rounded-lg flex items-center gap-1.5 transition-colors"
                    title="Download Profile Summary PDF"
                  >
                    <FileText className="w-3.5 h-3.5" /> Profile PDF
                  </a>

                  <a
                    href={`/api/reports/application/${app.id}/pdf`}
                    download
                    className="px-3 py-1.5 text-xs font-semibold text-slate-700 bg-slate-50 hover:bg-slate-100 border border-slate-200 rounded-lg flex items-center gap-1.5 transition-colors"
                    title="Download Application Summary PDF"
                  >
                    <Download className="w-3.5 h-3.5" /> Summary
                  </a>
                </div>
              </div>

              {/* Cover Letter if any */}
              {app.coverLetter && (
                <div className="p-3 bg-slate-50 rounded-xl text-xs text-slate-600 border border-slate-100">
                  <span className="font-semibold text-slate-800 block mb-0.5">Candidate Note:</span>
                  <p className="whitespace-pre-line leading-relaxed">{app.coverLetter}</p>
                </div>
              )}

              {/* Notes & Feedback preview */}
              <div className="flex flex-wrap items-center gap-4 text-xs text-slate-500">
                {app.recruiterNotes ? (
                  <div className="flex items-center gap-1.5 bg-amber-50/70 border border-amber-200/60 px-3 py-1 rounded-lg text-amber-900">
                    <MessageSquare className="w-3.5 h-3.5 text-amber-600 shrink-0" />
                    <span>Internal Note: <strong>{app.recruiterNotes}</strong></span>
                  </div>
                ) : null}

                {app.rejectionReason && (
                  <div className="flex items-center gap-1.5 bg-rose-50 border border-rose-200 px-3 py-1 rounded-lg text-rose-800">
                    <AlertCircle className="w-3.5 h-3.5 text-rose-600 shrink-0" />
                    <span>Rejection Reason: {app.rejectionReason}</span>
                  </div>
                )}
              </div>

              {/* Recruitment Stage Transition Buttons */}
              <div className="flex flex-wrap items-center justify-between gap-3 pt-3 border-t border-slate-100">
                <div className="flex flex-wrap items-center gap-2">
                  {/* Step 1: APPLIED -> UNDER_REVIEW */}
                  {app.status === 'APPLIED' && (
                    <button
                      onClick={() => handleUpdateStatus(app.id, 'UNDER_REVIEW')}
                      className="px-3 py-1.5 text-xs font-semibold text-amber-700 bg-amber-50 hover:bg-amber-100 border border-amber-200 rounded-lg cursor-pointer transition-colors"
                    >
                      Move to Under Review
                    </button>
                  )}

                  {/* Step 2: UNDER_REVIEW -> SHORTLISTED */}
                  {(app.status === 'APPLIED' || app.status === 'UNDER_REVIEW') && (
                    <button
                      onClick={() => handleUpdateStatus(app.id, 'SHORTLISTED')}
                      className="px-3 py-1.5 text-xs font-semibold text-purple-700 bg-purple-50 hover:bg-purple-100 border border-purple-200 rounded-lg cursor-pointer transition-colors"
                    >
                      Shortlist Candidate
                    </button>
                  )}

                  {/* Step 3: SHORTLISTED / ANY -> SCHEDULE INTERVIEW */}
                  {(app.status === 'SHORTLISTED' || app.status === 'INTERVIEW_SCHEDULED') && (
                    <button
                      onClick={() => setActiveInterviewApp(app)}
                      className="px-3 py-1.5 text-xs font-semibold text-indigo-700 bg-indigo-50 hover:bg-indigo-100 border border-indigo-200 rounded-lg flex items-center gap-1.5 cursor-pointer transition-colors"
                    >
                      <Calendar className="w-3.5 h-3.5" /> Schedule Interview Round
                    </button>
                  )}

                  {/* Step 4: SHORTLISTED / INTERVIEW_SCHEDULED -> SELECTED */}
                  {(app.status === 'SHORTLISTED' || app.status === 'INTERVIEW_SCHEDULED') && (
                    <button
                      onClick={() => handleUpdateStatus(app.id, 'SELECTED')}
                      className="px-3 py-1.5 text-xs font-semibold text-emerald-700 bg-emerald-50 hover:bg-emerald-100 border border-emerald-200 rounded-lg flex items-center gap-1 cursor-pointer transition-colors"
                    >
                      <Check className="w-3.5 h-3.5" /> Select Candidate / Extend Offer
                    </button>
                  )}

                  {/* Step 5: Reject option */}
                  {app.status !== 'REJECTED' && app.status !== 'WITHDRAWN' && app.status !== 'SELECTED' && (
                    <button
                      onClick={() => {
                        setRejectingApp(app);
                        setRejectionReason('');
                      }}
                      className="px-3 py-1.5 text-xs font-semibold text-rose-700 bg-rose-50 hover:bg-rose-100 border border-rose-200 rounded-lg flex items-center gap-1 cursor-pointer transition-colors"
                    >
                      <X className="w-3.5 h-3.5" /> Reject
                    </button>
                  )}
                </div>

                <div>
                  <button
                    onClick={() => {
                      setNotesApp(app);
                      setRecruiterNotes(app.recruiterNotes || '');
                    }}
                    className="text-xs text-slate-500 hover:text-indigo-600 flex items-center gap-1 cursor-pointer"
                  >
                    <MessageSquare className="w-3.5 h-3.5" />
                    <span>{app.recruiterNotes ? 'Edit Notes' : 'Add Internal Note'}</span>
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Schedule Interview Modal */}
      <ScheduleInterviewModal
        application={activeInterviewApp}
        isOpen={!!activeInterviewApp}
        onClose={() => setActiveInterviewApp(null)}
        onSuccess={() => {
          fetchApplications();
        }}
      />

      {/* Rejection Reason Modal */}
      {rejectingApp && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/50 p-4 backdrop-blur-xs">
          <div className="bg-white rounded-2xl max-w-md w-full p-6 shadow-xl space-y-4">
            <h3 className="text-base font-bold text-slate-900">Reject Application</h3>
            <p className="text-xs text-slate-600">
              Provide feedback or rejection reason for {rejectingApp.candidateName}.
            </p>

            <textarea
              rows={3}
              value={rejectionReason}
              onChange={(e) => setRejectionReason(e.target.value)}
              placeholder="e.g. Profile does not meet minimum hands-on Spring Boot experience requirement..."
              className="w-full text-xs p-3 rounded-xl border border-slate-200 focus:outline-none"
            />

            <div className="flex gap-2 justify-end">
              <button
                onClick={() => setRejectingApp(null)}
                className="px-4 py-2 text-xs font-semibold text-slate-700 bg-slate-100 hover:bg-slate-200 rounded-xl cursor-pointer"
              >
                Cancel
              </button>
              <button
                onClick={submitRejection}
                className="px-4 py-2 text-xs font-semibold text-white bg-rose-600 hover:bg-rose-700 rounded-xl cursor-pointer"
              >
                Confirm Rejection
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Internal Notes Modal */}
      {notesApp && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/50 p-4 backdrop-blur-xs">
          <div className="bg-white rounded-2xl max-w-md w-full p-6 shadow-xl space-y-4">
            <h3 className="text-base font-bold text-slate-900">Internal Recruiter Notes</h3>
            <p className="text-xs text-slate-600">
              Notes visible only to members of your organization for {notesApp.candidateName}.
            </p>

            <textarea
              rows={3}
              value={recruiterNotes}
              onChange={(e) => setRecruiterNotes(e.target.value)}
              placeholder="e.g. Excellent communication skills, expected CTC 9 LPA, available in 15 days."
              className="w-full text-xs p-3 rounded-xl border border-slate-200 focus:outline-none"
            />

            <div className="flex gap-2 justify-end">
              <button
                onClick={() => setNotesApp(null)}
                className="px-4 py-2 text-xs font-semibold text-slate-700 bg-slate-100 hover:bg-slate-200 rounded-xl cursor-pointer"
              >
                Cancel
              </button>
              <button
                onClick={submitNotes}
                className="px-4 py-2 text-xs font-semibold text-white bg-indigo-600 hover:bg-indigo-700 rounded-xl cursor-pointer"
              >
                Save Notes
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
