import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Briefcase, ArrowLeft, AlertCircle, CheckCircle2, ShieldAlert } from 'lucide-react';
import type { EmploymentType, WorkMode, Company } from '../../types';
import api from '../../api/client';

export const PostJobPage: React.FC = () => {
  const navigate = useNavigate();
  const [company, setCompany] = useState<Company | null>(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Form fields
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [skillInput, setSkillInput] = useState('');
  const [requiredSkills, setRequiredSkills] = useState<string[]>(['Java', 'Spring Boot', 'PostgreSQL']);
  const [minExperience, setMinExperience] = useState<number>(1);
  const [maxExperience, setMaxExperience] = useState<number>(3);
  const [minSalary, setMinSalary] = useState<number>(600000);
  const [maxSalary, setMaxSalary] = useState<number>(1000000);
  const [currency, setCurrency] = useState('INR');
  const [location, setLocation] = useState('Hyderabad, India');
  const [employmentType, setEmploymentType] = useState<EmploymentType>('FULL_TIME');
  const [workMode, setWorkMode] = useState<WorkMode>('HYBRID');
  const [openings, setOpenings] = useState<number>(2);
  const [applicationDeadline, setApplicationDeadline] = useState('');

  useEffect(() => {
    // Set default deadline to 30 days from now
    const nextMonth = new Date();
    nextMonth.setDate(nextMonth.getDate() + 30);
    setApplicationDeadline(nextMonth.toISOString().split('T')[0]);

    api.get('/recruiter/company')
      .then((res) => {
        if (res.data.success) {
          setCompany(res.data.data);
        }
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  const handleAddSkill = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && skillInput.trim()) {
      e.preventDefault();
      if (!requiredSkills.includes(skillInput.trim())) {
        setRequiredSkills([...requiredSkills, skillInput.trim()]);
      }
      setSkillInput('');
    }
  };

  const handleRemoveSkill = (skillToRemove: string) => {
    setRequiredSkills(requiredSkills.filter((s) => s !== skillToRemove));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    setError(null);

    api.post('/recruiter/jobs', {
      title,
      description,
      requiredSkills,
      minExperience: Number(minExperience),
      maxExperience: Number(maxExperience),
      minSalary: Number(minSalary),
      maxSalary: Number(maxSalary),
      currency,
      location,
      employmentType,
      workMode,
      openings: Number(openings),
      applicationDeadline,
    })
      .then((res) => {
        if (res.data.success) {
          alert('Job vacancy posted successfully!');
          navigate('/recruiter/jobs');
        }
      })
      .catch((err) => {
        setError(err.response?.data?.message || 'Error posting job vacancy');
      })
      .finally(() => {
        setSubmitting(false);
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
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      <Link to="/recruiter/jobs" className="inline-flex items-center gap-1.5 text-xs font-semibold text-slate-500 hover:text-indigo-600">
        <ArrowLeft className="w-3.5 h-3.5" /> Back to job vacancies
      </Link>

      <div>
        <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Post New Job Vacancy</h1>
        <p className="text-xs text-slate-500 mt-0.5">
          Publish a detailed position requirement for candidates to discover and apply
        </p>
      </div>

      {company && !company.verified && (
        <div className="p-4 bg-amber-50 border border-amber-200 rounded-2xl flex items-start gap-3">
          <ShieldAlert className="w-5 h-5 text-amber-600 shrink-0 mt-0.5" />
          <div className="text-xs text-amber-900">
            <h4 className="font-bold">Pending Administrative Verification</h4>
            <p className="mt-0.5 leading-relaxed">
              Your company ({company.name}) must be verified by an administrator before job vacancies can be published.
              You can test company approval from the Admin Console.
            </p>
          </div>
        </div>
      )}

      {error && (
        <div className="p-3 bg-rose-50 border border-rose-200 text-rose-700 text-xs rounded-xl flex items-center gap-2">
          <AlertCircle className="w-4 h-4 shrink-0" />
          <span>{error}</span>
        </div>
      )}

      <form onSubmit={handleSubmit} className="bg-white p-6 sm:p-8 rounded-2xl border border-slate-200 shadow-xs space-y-6 text-xs">
        {/* Title & Location */}
        <div className="space-y-4">
          <div>
            <label className="block font-semibold text-slate-700 mb-1">Job Title *</label>
            <input
              type="text"
              required
              minLength={3}
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="e.g. Senior Java Backend Developer"
              className="w-full p-2.5 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-indigo-500/20"
            />
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <div>
              <label className="block font-semibold text-slate-700 mb-1">Location *</label>
              <input
                type="text"
                required
                value={location}
                onChange={(e) => setLocation(e.target.value)}
                placeholder="e.g. Hyderabad, India"
                className="w-full p-2.5 rounded-xl border border-slate-200"
              />
            </div>

            <div>
              <label className="block font-semibold text-slate-700 mb-1">Work Mode *</label>
              <select
                value={workMode}
                onChange={(e) => setWorkMode(e.target.value as WorkMode)}
                className="w-full p-2.5 rounded-xl border border-slate-200 bg-white"
              >
                <option value="HYBRID">Hybrid</option>
                <option value="REMOTE">Remote</option>
                <option value="ON_SITE">On-Site</option>
              </select>
            </div>

            <div>
              <label className="block font-semibold text-slate-700 mb-1">Employment Type *</label>
              <select
                value={employmentType}
                onChange={(e) => setEmploymentType(e.target.value as EmploymentType)}
                className="w-full p-2.5 rounded-xl border border-slate-200 bg-white"
              >
                <option value="FULL_TIME">Full Time</option>
                <option value="PART_TIME">Part Time</option>
                <option value="CONTRACT">Contract</option>
                <option value="INTERNSHIP">Internship</option>
              </select>
            </div>
          </div>
        </div>

        {/* Experience & Openings */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 pt-4 border-t border-slate-100">
          <div>
            <label className="block font-semibold text-slate-700 mb-1">Min Experience (Years)</label>
            <input
              type="number"
              min={0}
              value={minExperience}
              onChange={(e) => setMinExperience(Number(e.target.value))}
              className="w-full p-2.5 rounded-xl border border-slate-200"
            />
          </div>

          <div>
            <label className="block font-semibold text-slate-700 mb-1">Max Experience (Years)</label>
            <input
              type="number"
              min={0}
              value={maxExperience}
              onChange={(e) => setMaxExperience(Number(e.target.value))}
              className="w-full p-2.5 rounded-xl border border-slate-200"
            />
          </div>

          <div>
            <label className="block font-semibold text-slate-700 mb-1">Number of Openings *</label>
            <input
              type="number"
              min={1}
              required
              value={openings}
              onChange={(e) => setOpenings(Number(e.target.value))}
              className="w-full p-2.5 rounded-xl border border-slate-200"
            />
          </div>
        </div>

        {/* Compensation & Deadline */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 pt-4 border-t border-slate-100">
          <div>
            <label className="block font-semibold text-slate-700 mb-1">Min Annual Salary (₹ CTC)</label>
            <input
              type="number"
              step={50000}
              value={minSalary}
              onChange={(e) => setMinSalary(Number(e.target.value))}
              className="w-full p-2.5 rounded-xl border border-slate-200"
            />
          </div>

          <div>
            <label className="block font-semibold text-slate-700 mb-1">Max Annual Salary (₹ CTC)</label>
            <input
              type="number"
              step={50000}
              value={maxSalary}
              onChange={(e) => setMaxSalary(Number(e.target.value))}
              className="w-full p-2.5 rounded-xl border border-slate-200"
            />
          </div>

          <div>
            <label className="block font-semibold text-slate-700 mb-1">Application Deadline *</label>
            <input
              type="date"
              required
              min={new Date().toISOString().split('T')[0]}
              value={applicationDeadline}
              onChange={(e) => setApplicationDeadline(e.target.value)}
              className="w-full p-2.5 rounded-xl border border-slate-200"
            />
          </div>
        </div>

        {/* Skills */}
        <div className="pt-4 border-t border-slate-100">
          <label className="block font-semibold text-slate-700 mb-1.5">Required Skills & Keywords</label>
          <input
            type="text"
            value={skillInput}
            onChange={(e) => setSkillInput(e.target.value)}
            onKeyDown={handleAddSkill}
            placeholder="Type skill name and press Enter..."
            className="w-full p-2.5 rounded-xl border border-slate-200 mb-2"
          />

          <div className="flex flex-wrap gap-1.5">
            {requiredSkills.map((s, idx) => (
              <span key={idx} className="inline-flex items-center gap-1.5 px-2.5 py-1 bg-indigo-50 text-indigo-700 rounded-lg text-xs font-medium">
                {s}
                <button type="button" onClick={() => handleRemoveSkill(s)} className="hover:text-rose-600 cursor-pointer">
                  ×
                </button>
              </span>
            ))}
          </div>
        </div>

        {/* Description */}
        <div className="pt-4 border-t border-slate-100">
          <label className="block font-semibold text-slate-700 mb-1">Detailed Job Description (min 20 characters) *</label>
          <textarea
            rows={6}
            required
            minLength={20}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Describe the role, day-to-day responsibilities, ideal candidate background, tech stack, and benefits..."
            className="w-full p-3 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-indigo-500/20"
          />
        </div>

        <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
          <Link
            to="/recruiter/jobs"
            className="px-5 py-2.5 font-semibold text-slate-700 bg-slate-100 hover:bg-slate-200 rounded-xl"
          >
            Cancel
          </Link>
          <button
            type="submit"
            disabled={submitting}
            className="px-6 py-2.5 font-semibold text-white bg-indigo-600 hover:bg-indigo-700 rounded-xl shadow-xs transition-colors cursor-pointer disabled:opacity-50"
          >
            {submitting ? 'Publishing...' : 'Publish Vacancy'}
          </button>
        </div>
      </form>
    </div>
  );
};
