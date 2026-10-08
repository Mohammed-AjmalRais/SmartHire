import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Briefcase, Calendar, CheckCircle2, FileText, ArrowRight, Clock, Video } from 'lucide-react';
import type { Application, Interview, CandidateProfile } from '../../types';
import api from '../../api/client';
import { StatusBadge } from '../../components/StatusBadge';

export const CandidateDashboard: React.FC = () => {
  const [applications, setApplications] = useState<Application[]>([]);
  const [interviews, setInterviews] = useState<Interview[]>([]);
  const [profile, setProfile] = useState<CandidateProfile | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      api.get('/candidate/applications'),
      api.get('/candidate/interviews'),
      api.get('/candidate/profile'),
    ])
      .then(([appRes, intRes, profRes]) => {
        if (appRes.data.success) setApplications(appRes.data.data);
        if (intRes.data.success) setInterviews(intRes.data.data);
        if (profRes.data.success) setProfile(profRes.data.data);
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

  const upcomingInterview = interviews.find((i) => i.status === 'SCHEDULED');
  const shortlistedCount = applications.filter((a) => a.status === 'SHORTLISTED' || a.status === 'INTERVIEW_SCHEDULED').length;
  const selectedCount = applications.filter((a) => a.status === 'SELECTED').length;

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">
      {/* Welcome Banner */}
      <div className="bg-gradient-to-r from-indigo-900 via-indigo-800 to-indigo-950 rounded-3xl p-8 text-white shadow-sm flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
        <div>
          <span className="text-xs uppercase font-semibold tracking-wider text-indigo-300">Candidate Dashboard</span>
          <h1 className="text-2xl sm:text-3xl font-extrabold mt-1">Hello, {profile?.fullName || 'Job Seeker'}!</h1>
          <p className="text-xs sm:text-sm text-indigo-200 mt-1 max-w-xl">
            Track all your job submissions, schedule updates, and professional profile from one unified dashboard.
          </p>
        </div>

        <div className="flex gap-3">
          <Link
            to="/jobs"
            className="px-4 py-2 bg-white text-indigo-900 font-semibold text-xs rounded-xl hover:bg-indigo-50 shadow-sm transition-colors"
          >
            Explore Jobs
          </Link>
          <Link
            to="/candidate/profile"
            className="px-4 py-2 bg-indigo-700/80 hover:bg-indigo-700 text-white font-semibold text-xs rounded-xl border border-indigo-500/50 transition-colors"
          >
            Update Profile
          </Link>
        </div>
      </div>

      {/* Metrics Grid */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <div className="bg-white p-5 rounded-2xl border border-slate-200">
          <div className="w-8 h-8 rounded-lg bg-blue-50 text-blue-600 flex items-center justify-center mb-3">
            <Briefcase className="w-4 h-4" />
          </div>
          <span className="text-2xl font-black text-slate-900">{applications.length}</span>
          <p className="text-xs text-slate-500 mt-0.5 font-medium">Total Applied</p>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200">
          <div className="w-8 h-8 rounded-lg bg-purple-50 text-purple-600 flex items-center justify-center mb-3">
            <FileText className="w-4 h-4" />
          </div>
          <span className="text-2xl font-black text-slate-900">{shortlistedCount}</span>
          <p className="text-xs text-slate-500 mt-0.5 font-medium">Shortlisted</p>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200">
          <div className="w-8 h-8 rounded-lg bg-indigo-50 text-indigo-600 flex items-center justify-center mb-3">
            <Calendar className="w-4 h-4" />
          </div>
          <span className="text-2xl font-black text-slate-900">{interviews.length}</span>
          <p className="text-xs text-slate-500 mt-0.5 font-medium">Scheduled Interviews</p>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200">
          <div className="w-8 h-8 rounded-lg bg-emerald-50 text-emerald-600 flex items-center justify-center mb-3">
            <CheckCircle2 className="w-4 h-4" />
          </div>
          <span className="text-2xl font-black text-slate-900">{selectedCount}</span>
          <p className="text-xs text-slate-500 mt-0.5 font-medium">Offers / Selected</p>
        </div>
      </div>

      {/* Upcoming Interview alert if present */}
      {upcomingInterview && (
        <div className="bg-indigo-50 border border-indigo-200 rounded-2xl p-5 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
          <div className="flex items-center gap-3.5">
            <div className="w-10 h-10 rounded-xl bg-indigo-600 text-white flex items-center justify-center shrink-0">
              <Video className="w-5 h-5" />
            </div>
            <div>
              <span className="text-[11px] font-bold text-indigo-700 uppercase tracking-wider">
                Upcoming {upcomingInterview.interviewType} Interview
              </span>
              <h4 className="text-sm font-bold text-slate-900">
                {upcomingInterview.jobTitle} • {upcomingInterview.companyName}
              </h4>
              <p className="text-xs text-slate-600 mt-0.5">
                {new Date(upcomingInterview.interviewDate).toLocaleDateString([], {
                  month: 'short',
                  day: 'numeric',
                  year: 'numeric',
                })}{' '}
                at {upcomingInterview.startTime.substring(0, 5)} - {upcomingInterview.endTime.substring(0, 5)}
              </p>
            </div>
          </div>

          {upcomingInterview.meetingLink && (
            <a
              href={upcomingInterview.meetingLink}
              target="_blank"
              rel="noreferrer"
              className="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-semibold rounded-xl shadow-xs transition-colors shrink-0 flex items-center gap-1.5"
            >
              <Video className="w-3.5 h-3.5" /> Join Meeting
            </a>
          )}
        </div>
      )}

      {/* Recent Applications list */}
      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <div className="p-5 border-b border-slate-100 flex items-center justify-between">
          <div>
            <h3 className="text-base font-bold text-slate-900">Recent Applications</h3>
            <p className="text-xs text-slate-500 mt-0.5">Track real-time recruitment progression</p>
          </div>
          <Link to="/candidate/applications" className="text-xs font-semibold text-indigo-600 hover:underline flex items-center gap-1">
            View all <ArrowRight className="w-3.5 h-3.5" />
          </Link>
        </div>

        {applications.length === 0 ? (
          <div className="p-12 text-center">
            <Briefcase className="w-10 h-10 text-slate-300 mx-auto mb-2" />
            <p className="text-xs text-slate-500">You haven't submitted any job applications yet.</p>
            <Link to="/jobs" className="mt-3 inline-block px-4 py-1.5 text-xs font-semibold bg-indigo-600 text-white rounded-lg">
              Browse Available Jobs
            </Link>
          </div>
        ) : (
          <div className="divide-y divide-slate-100">
            {applications.slice(0, 5).map((app) => (
              <div key={app.id} className="p-4 sm:p-5 flex flex-col sm:flex-row sm:items-center justify-between gap-4 hover:bg-slate-50/50 transition-colors">
                <div>
                  <Link to={`/jobs/${app.jobId}`} className="text-sm font-bold text-slate-900 hover:text-indigo-600">
                    {app.jobTitle}
                  </Link>
                  <p className="text-xs text-slate-500 mt-0.5">{app.companyName}</p>
                  <span className="text-[11px] text-slate-400 mt-1 block">
                    Applied on {new Date(app.appliedAt).toLocaleDateString([], { month: 'short', day: 'numeric', year: 'numeric' })}
                  </span>
                </div>

                <div className="flex items-center gap-3">
                  <StatusBadge status={app.status} />
                  <Link
                    to="/candidate/applications"
                    className="p-1.5 text-slate-400 hover:text-indigo-600 hover:bg-slate-100 rounded-lg"
                    title="View Timeline"
                  >
                    <ArrowRight className="w-4 h-4" />
                  </Link>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
