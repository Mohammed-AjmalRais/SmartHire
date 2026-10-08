import React, { useState, useEffect } from 'react';
import { Calendar, Video, Clock, MapPin, CheckCircle, XCircle } from 'lucide-react';
import type { Interview } from '../../types';
import api from '../../api/client';

export const MyInterviewsPage: React.FC = () => {
  const [interviews, setInterviews] = useState<Interview[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/candidate/interviews')
      .then((res) => {
        if (res.data.success) {
          setInterviews(res.data.data);
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
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Interview Schedule</h1>
        <p className="text-xs text-slate-500 mt-0.5">
          Upcoming and past interviews with recruiters and technical panel members
        </p>
      </div>

      {interviews.length === 0 ? (
        <div className="bg-white p-12 rounded-2xl border border-slate-200 text-center">
          <Calendar className="w-12 h-12 text-slate-300 mx-auto mb-3" />
          <h3 className="text-base font-bold text-slate-800">No interviews scheduled yet</h3>
          <p className="text-xs text-slate-500 mt-1 max-w-sm mx-auto">
            When recruiters shortlist your applications and schedule interview rounds, details and meeting links will appear here.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {interviews.map((iv) => (
            <div key={iv.id} className="bg-white rounded-2xl border border-slate-200 p-5 shadow-xs flex flex-col justify-between space-y-4">
              <div>
                <div className="flex items-start justify-between gap-2">
                  <div>
                    <span className="text-[11px] font-bold text-indigo-600 uppercase tracking-wider">
                      {iv.interviewType} Interview
                    </span>
                    <h3 className="text-base font-bold text-slate-900 mt-0.5">{iv.jobTitle}</h3>
                    <p className="text-xs text-slate-600">{iv.companyName}</p>
                  </div>
                  <span className={`text-[10px] font-bold px-2 py-0.5 rounded-full uppercase ${
                    iv.status === 'SCHEDULED' ? 'bg-indigo-50 text-indigo-700' :
                    iv.status === 'COMPLETED' ? 'bg-emerald-50 text-emerald-700' : 'bg-slate-100 text-slate-600'
                  }`}>
                    {iv.status}
                  </span>
                </div>

                <div className="space-y-2 mt-4 text-xs text-slate-600">
                  <div className="flex items-center gap-2">
                    <Calendar className="w-4 h-4 text-slate-400" />
                    <span>
                      {new Date(iv.interviewDate).toLocaleDateString([], {
                        weekday: 'short',
                        month: 'short',
                        day: 'numeric',
                        year: 'numeric',
                      })}
                    </span>
                  </div>

                  <div className="flex items-center gap-2">
                    <Clock className="w-4 h-4 text-slate-400" />
                    <span>{iv.startTime.substring(0, 5)} – {iv.endTime.substring(0, 5)}</span>
                  </div>

                  <div className="flex items-center gap-2">
                    <MapPin className="w-4 h-4 text-slate-400" />
                    <span>{iv.location || 'Online / Remote'}</span>
                  </div>

                  <div className="pt-2 text-slate-500 text-[11px]">
                    Interviewer: <strong className="text-slate-700">{iv.interviewerName}</strong>
                  </div>

                  {iv.notes && (
                    <div className="p-2.5 bg-slate-50 rounded-lg text-[11px] text-slate-600 italic">
                      "{iv.notes}"
                    </div>
                  )}
                </div>
              </div>

              {iv.meetingLink && iv.status === 'SCHEDULED' && (
                <div className="pt-3 border-t border-slate-100">
                  <a
                    href={iv.meetingLink}
                    target="_blank"
                    rel="noreferrer"
                    className="w-full py-2 bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-semibold rounded-xl flex items-center justify-center gap-1.5 shadow-xs transition-colors"
                  >
                    <Video className="w-4 h-4" /> Join Online Meeting
                  </a>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
