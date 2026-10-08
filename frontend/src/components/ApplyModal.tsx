import React, { useState, useEffect } from 'react';
import { X, Upload, FileText, CheckCircle2, AlertCircle } from 'lucide-react';
import type { Job, Resume } from '../types';
import api from '../api/client';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

interface ApplyModalProps {
  job: Job | null;
  isOpen: boolean;
  onClose: () => void;
  onSuccess: (jobId: number) => void;
}

export const ApplyModal: React.FC<ApplyModalProps> = ({ job, isOpen, onClose, onSuccess }) => {
  const { isAuthenticated, isCandidate } = useAuth();
  const navigate = useNavigate();

  const [resumes, setResumes] = useState<Resume[]>([]);
  const [selectedResumeId, setSelectedResumeId] = useState<number | null>(null);
  const [coverLetter, setCoverLetter] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [uploadingResume, setUploadingResume] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (isOpen && isAuthenticated && isCandidate) {
      api.get('/resumes')
        .then((res) => {
          if (res.data.success) {
            setResumes(res.data.data);
            const primary = res.data.data.find((r: Resume) => r.isPrimary);
            if (primary) setSelectedResumeId(primary.id);
            else if (res.data.data.length > 0) setSelectedResumeId(res.data.data[0].id);
          }
        })
        .catch(() => {});
    }
  }, [isOpen, isAuthenticated, isCandidate]);

  if (!isOpen || !job) return null;

  if (!isAuthenticated) {
    return (
      <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/50 p-4 backdrop-blur-xs">
        <div className="bg-white rounded-2xl max-w-md w-full p-6 text-center shadow-xl">
          <AlertCircle className="w-12 h-12 text-indigo-600 mx-auto mb-3" />
          <h3 className="text-lg font-bold text-slate-900">Sign in to Apply</h3>
          <p className="text-sm text-slate-600 mt-2">
            You need a candidate account on SmartHire to apply for <span className="font-semibold">{job.title}</span>.
          </p>
          <div className="flex gap-3 mt-6">
            <button
              onClick={onClose}
              className="flex-1 px-4 py-2 text-sm font-medium text-slate-700 bg-slate-100 hover:bg-slate-200 rounded-lg cursor-pointer"
            >
              Cancel
            </button>
            <button
              onClick={() => {
                onClose();
                navigate('/login');
              }}
              className="flex-1 px-4 py-2 text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 rounded-lg shadow-sm cursor-pointer"
            >
              Sign In
            </button>
          </div>
        </div>
      </div>
    );
  }

  const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);
    setUploadingResume(true);
    setError(null);

    api.post('/resumes/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
      .then((res) => {
        if (res.data.success) {
          const uploaded = res.data.data;
          setResumes((prev) => [uploaded, ...prev]);
          setSelectedResumeId(uploaded.id);
        }
      })
      .catch((err) => {
        setError(err.response?.data?.message || 'Failed to upload resume. PDF/DOCX max 10MB.');
      })
      .finally(() => {
        setUploadingResume(false);
      });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    setError(null);

    api.post(`/jobs/${job.id}/apply`, {
      resumeId: selectedResumeId,
      coverLetter,
    })
      .then((res) => {
        if (res.data.success) {
          onSuccess(job.id);
          onClose();
        }
      })
      .catch((err) => {
        setError(err.response?.data?.message || 'Error submitting application.');
      })
      .finally(() => {
        setSubmitting(false);
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

        <h3 className="text-lg font-bold text-slate-900">Apply for Position</h3>
        <p className="text-xs text-slate-500 mt-1">
          {job.title} • {job.companyName}
        </p>

        {error && (
          <div className="mt-4 p-3 bg-rose-50 border border-rose-200 text-rose-700 text-xs rounded-lg flex items-center gap-2">
            <AlertCircle className="w-4 h-4 shrink-0" />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="mt-5 space-y-4">
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1.5">
              Select Resume to Submit *
            </label>

            {resumes.length === 0 ? (
              <div className="border-2 border-dashed border-slate-200 rounded-xl p-4 text-center">
                <FileText className="w-8 h-8 text-slate-400 mx-auto mb-2" />
                <p className="text-xs text-slate-600 font-medium">No resumes uploaded yet</p>
                <p className="text-[11px] text-slate-400 mt-0.5">Upload your PDF or DOCX file</p>
                <label className="inline-flex items-center gap-1.5 mt-3 px-3 py-1.5 text-xs font-semibold text-indigo-600 bg-indigo-50 hover:bg-indigo-100 rounded-lg cursor-pointer">
                  <Upload className="w-3.5 h-3.5" />
                  <span>{uploadingResume ? 'Uploading...' : 'Upload Resume'}</span>
                  <input type="file" accept=".pdf,.doc,.docx" onChange={handleFileUpload} className="hidden" disabled={uploadingResume} />
                </label>
              </div>
            ) : (
              <div className="space-y-2 max-h-40 overflow-y-auto pr-1">
                {resumes.map((r) => (
                  <label
                    key={r.id}
                    className={`flex items-center justify-between p-3 rounded-lg border text-xs cursor-pointer transition-all ${
                      selectedResumeId === r.id
                        ? 'border-indigo-600 bg-indigo-50/40 text-indigo-900'
                        : 'border-slate-200 hover:bg-slate-50 text-slate-700'
                    }`}
                  >
                    <div className="flex items-center gap-2.5">
                      <input
                        type="radio"
                        name="resume"
                        checked={selectedResumeId === r.id}
                        onChange={() => setSelectedResumeId(r.id)}
                        className="text-indigo-600"
                      />
                      <div>
                        <p className="font-semibold">{r.originalFileName}</p>
                        <p className="text-[10px] text-slate-400">
                          v{r.version} • {(r.fileSize / 1024).toFixed(0)} KB {r.isPrimary && '• Primary'}
                        </p>
                      </div>
                    </div>
                    {selectedResumeId === r.id && <CheckCircle2 className="w-4 h-4 text-indigo-600" />}
                  </label>
                ))}

                <div className="pt-2">
                  <label className="text-xs text-indigo-600 hover:underline flex items-center gap-1 cursor-pointer">
                    <Upload className="w-3 h-3" />
                    <span>{uploadingResume ? 'Uploading...' : 'Upload different resume'}</span>
                    <input type="file" accept=".pdf,.doc,.docx" onChange={handleFileUpload} className="hidden" disabled={uploadingResume} />
                  </label>
                </div>
              </div>
            )}
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1.5">
              Cover Letter / Introduction Note (Optional)
            </label>
            <textarea
              rows={4}
              value={coverLetter}
              onChange={(e) => setCoverLetter(e.target.value)}
              placeholder="Highlight your relevant experience, key skills, and why you are excited about this position..."
              className="w-full text-xs p-3 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500"
            />
          </div>

          <div className="flex gap-3 pt-3">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 px-4 py-2 text-xs font-semibold text-slate-700 bg-slate-100 hover:bg-slate-200 rounded-lg cursor-pointer"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={submitting || !selectedResumeId}
              className="flex-1 px-4 py-2 text-xs font-semibold text-white bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 rounded-lg shadow-sm cursor-pointer"
            >
              {submitting ? 'Submitting...' : 'Submit Application'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
