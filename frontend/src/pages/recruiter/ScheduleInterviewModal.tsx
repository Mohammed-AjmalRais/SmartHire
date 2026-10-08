import React, { useState } from 'react';
import { X, Calendar, Clock, Video, AlertCircle, CheckCircle } from 'lucide-react';
import type { Application, InterviewType } from '../../types';
import api from '../../api/client';

interface ScheduleInterviewModalProps {
  application: Application | null;
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void;
}

export const ScheduleInterviewModal: React.FC<ScheduleInterviewModalProps> = ({
  application,
  isOpen,
  onClose,
  onSuccess,
}) => {
  const [interviewType, setInterviewType] = useState<InterviewType>('TECHNICAL');
  const [interviewDate, setInterviewDate] = useState('');
  const [startTime, setStartTime] = useState('10:00');
  const [endTime, setEndTime] = useState('11:00');
  const [meetingLink, setMeetingLink] = useState('https://meet.google.com/');
  const [location, setLocation] = useState('Google Meet');
  const [notes, setNotes] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  if (!isOpen || !application) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    // Format times into HH:mm:ss for backend LocalTime
    const formattedStartTime = startTime.length === 5 ? `${startTime}:00` : startTime;
    const formattedEndTime = endTime.length === 5 ? `${endTime}:00` : endTime;

    api.post('/interviews', {
      applicationId: application.id,
      interviewType,
      interviewDate,
      startTime: formattedStartTime,
      endTime: formattedEndTime,
      meetingLink,
      location,
      notes,
    })
      .then((res) => {
        if (res.data.success) {
          alert('Interview scheduled successfully! An invitation alert has been sent to the candidate.');
          onSuccess();
          onClose();
        }
      })
      .catch((err) => {
        setError(err.response?.data?.message || 'Error scheduling interview. Check for overlapping conflicts.');
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/50 p-4 backdrop-blur-xs">
      <div className="bg-white rounded-2xl max-w-lg w-full p-6 shadow-2xl relative max-h-[90vh] overflow-y-auto">
        <button
          onClick={onClose}
          className="absolute top-4 right-4 p-1 text-slate-400 hover:text-slate-600 rounded-lg cursor-pointer"
        >
          <X className="w-5 h-5" />
        </button>

        <h3 className="text-lg font-bold text-slate-900">Schedule Interview Round</h3>
        <p className="text-xs text-slate-500 mt-1">
          Candidate: <span className="font-semibold text-slate-700">{application.candidateName}</span> for{' '}
          <span className="font-semibold text-slate-700">{application.jobTitle}</span>
        </p>

        {error && (
          <div className="mt-4 p-3 bg-rose-50 border border-rose-200 text-rose-700 text-xs rounded-xl flex items-start gap-2">
            <AlertCircle className="w-4 h-4 text-rose-600 shrink-0 mt-0.5" />
            <div>
              <p className="font-semibold">Scheduling Error</p>
              <p className="mt-0.5">{error}</p>
            </div>
          </div>
        )}

        <form onSubmit={handleSubmit} className="mt-5 space-y-4 text-xs">
          <div>
            <label className="block font-semibold text-slate-700 mb-1">Interview Round Type *</label>
            <select
              value={interviewType}
              onChange={(e) => setInterviewType(e.target.value as InterviewType)}
              className="w-full p-2.5 rounded-xl border border-slate-200 bg-white"
            >
              <option value="TECHNICAL">Technical Interview</option>
              <option value="HR">HR Interview</option>
              <option value="MANAGERIAL">Managerial Interview</option>
            </select>
          </div>

          <div>
            <label className="block font-semibold text-slate-700 mb-1">Interview Date *</label>
            <input
              type="date"
              required
              min={new Date().toISOString().split('T')[0]}
              value={interviewDate}
              onChange={(e) => setInterviewDate(e.target.value)}
              className="w-full p-2.5 rounded-xl border border-slate-200"
            />
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block font-semibold text-slate-700 mb-1">Start Time *</label>
              <input
                type="time"
                required
                value={startTime}
                onChange={(e) => setStartTime(e.target.value)}
                className="w-full p-2.5 rounded-xl border border-slate-200"
              />
            </div>
            <div>
              <label className="block font-semibold text-slate-700 mb-1">End Time *</label>
              <input
                type="time"
                required
                value={endTime}
                onChange={(e) => setEndTime(e.target.value)}
                className="w-full p-2.5 rounded-xl border border-slate-200"
              />
            </div>
          </div>

          <div>
            <label className="block font-semibold text-slate-700 mb-1">Meeting Link / Video URL</label>
            <input
              type="url"
              value={meetingLink}
              onChange={(e) => setMeetingLink(e.target.value)}
              placeholder="https://meet.google.com/xyz-abc"
              className="w-full p-2.5 rounded-xl border border-slate-200"
            />
          </div>

          <div>
            <label className="block font-semibold text-slate-700 mb-1">Location or Platform Note</label>
            <input
              type="text"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              placeholder="e.g. Google Meet, Zoom, or Office Room 4B"
              className="w-full p-2.5 rounded-xl border border-slate-200"
            />
          </div>

          <div>
            <label className="block font-semibold text-slate-700 mb-1">Preparation Notes / Focus Areas</label>
            <textarea
              rows={2}
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              placeholder="e.g. Discussion on Spring Boot microservices, system design, and database queries."
              className="w-full p-2.5 rounded-xl border border-slate-200"
            />
          </div>

          <div className="flex gap-3 pt-3">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 py-2 font-semibold text-slate-700 bg-slate-100 hover:bg-slate-200 rounded-xl cursor-pointer"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={loading}
              className="flex-1 py-2 font-semibold text-white bg-indigo-600 hover:bg-indigo-700 rounded-xl shadow-xs cursor-pointer disabled:opacity-50"
            >
              {loading ? 'Validating Conflicts...' : 'Confirm Schedule'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
