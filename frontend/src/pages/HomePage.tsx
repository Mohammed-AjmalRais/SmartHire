import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Search, MapPin, Briefcase, Users, CheckCircle, ArrowRight, ShieldCheck, Calendar, Sparkles } from 'lucide-react';
import type { Job } from '../types';
import api from '../api/client';
import { JobCard } from '../components/JobCard';
import { ApplyModal } from '../components/ApplyModal';

export const HomePage: React.FC = () => {
  const navigate = useNavigate();
  const [keyword, setKeyword] = useState('');
  const [location, setLocation] = useState('');
  const [recentJobs, setRecentJobs] = useState<Job[]>([]);
  const [selectedJobForApply, setSelectedJobForApply] = useState<Job | null>(null);

  useEffect(() => {
    api.get('/jobs?size=4')
      .then((res) => {
        if (res.data.success && res.data.data.content) {
          setRecentJobs(res.data.data.content);
        }
      })
      .catch(() => {});
  }, []);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    const query = new URLSearchParams();
    if (keyword) query.set('keyword', keyword);
    if (location) query.set('location', location);
    navigate(`/jobs?${query.toString()}`);
  };

  return (
    <div className="space-y-16 pb-16">
      {/* Hero Section */}
      <section className="relative overflow-hidden bg-gradient-to-b from-indigo-50/60 via-white to-white pt-16 pb-12 border-b border-slate-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-indigo-50 border border-indigo-100 text-indigo-700 text-xs font-semibold mb-6">
            <Sparkles className="w-3.5 h-3.5" /> Modern Enterprise Recruitment Platform
          </div>

          <h1 className="text-4xl sm:text-5xl lg:text-6xl font-extrabold text-slate-900 tracking-tight leading-tight max-w-4xl mx-auto">
            Where Top Tech Talent Meets <span className="text-indigo-600">Great Companies</span>
          </h1>

          <p className="mt-4 text-base sm:text-lg text-slate-600 max-w-2xl mx-auto leading-relaxed">
            SmartHire digitizes the end-to-end hiring journey. From discovery and resume screening to interview scheduling and offer rollout.
          </p>

          {/* Search Bar */}
          <form onSubmit={handleSearch} className="mt-8 max-w-3xl mx-auto bg-white p-2.5 rounded-2xl shadow-lg border border-slate-200 flex flex-col sm:flex-row gap-2">
            <div className="flex-1 flex items-center px-3 py-2 border-b sm:border-b-0 sm:border-r border-slate-100 gap-2.5">
              <Search className="w-4 h-4 text-slate-400 shrink-0" />
              <input
                type="text"
                placeholder="Job title, skills (e.g. Java, Spring Boot, React)"
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                className="w-full text-sm text-slate-800 focus:outline-none placeholder:text-slate-400"
              />
            </div>

            <div className="flex-1 flex items-center px-3 py-2 gap-2.5">
              <MapPin className="w-4 h-4 text-slate-400 shrink-0" />
              <input
                type="text"
                placeholder="Location (e.g. Hyderabad, Bengaluru, Remote)"
                value={location}
                onChange={(e) => setLocation(e.target.value)}
                className="w-full text-sm text-slate-800 focus:outline-none placeholder:text-slate-400"
              />
            </div>

            <button
              type="submit"
              className="px-6 py-3 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold text-sm rounded-xl shadow-sm transition-colors cursor-pointer flex items-center justify-center gap-2"
            >
              <Search className="w-4 h-4" /> Search Jobs
            </button>
          </form>

          {/* Popular Tags */}
          <div className="mt-4 flex flex-wrap items-center justify-center gap-2 text-xs text-slate-500">
            <span className="font-semibold text-slate-600">Popular:</span>
            {['Java Developer', 'Spring Boot', 'React', 'Full-Stack', 'PostgreSQL', 'Hybrid'].map((tag) => (
              <button
                key={tag}
                type="button"
                onClick={() => {
                  setKeyword(tag);
                  navigate(`/jobs?keyword=${encodeURIComponent(tag)}`);
                }}
                className="px-2.5 py-1 bg-white hover:bg-slate-100 rounded-full border border-slate-200 text-slate-600 transition-colors cursor-pointer"
              >
                {tag}
              </button>
            ))}
          </div>
        </div>
      </section>

      {/* Featured Jobs Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-end justify-between mb-8">
          <div>
            <h2 className="text-2xl font-bold text-slate-900">Featured Opportunities</h2>
            <p className="text-sm text-slate-500 mt-1">Discover latest verified openings with transparent salary and requirements</p>
          </div>
          <Link to="/jobs" className="text-sm font-semibold text-indigo-600 hover:text-indigo-800 flex items-center gap-1">
            Browse all jobs <ArrowRight className="w-4 h-4" />
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
          {recentJobs.map((job) => (
            <JobCard
              key={job.id}
              job={job}
              onApplyClick={(j) => setSelectedJobForApply(j)}
            />
          ))}
        </div>
      </section>

      {/* Complete Recruitment Workflow */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="bg-slate-900 rounded-3xl p-8 sm:p-12 text-white relative overflow-hidden">
          <div className="text-center max-w-2xl mx-auto mb-12">
            <h2 className="text-3xl font-bold tracking-tight">Structured Recruitment Lifecycle</h2>
            <p className="mt-3 text-slate-400 text-sm">
              SmartHire moves applications through verified states, keeping candidates informed and recruiters organized.
            </p>
          </div>

          <div className="grid grid-cols-2 md:grid-cols-6 gap-3 text-center">
            {[
              { step: '1', title: 'Applied', desc: 'Application received & logged' },
              { step: '2', title: 'Under Review', desc: 'Recruiter resume evaluation' },
              { step: '3', title: 'Shortlisted', desc: 'Qualified for interviewing' },
              { step: '4', title: 'Interview', desc: 'Conflict-checked scheduling' },
              { step: '5', title: 'Selected', desc: 'Job offer extended' },
              { step: '6', title: 'Admin Verified', desc: 'Ensures genuine companies' }
            ].map((st, i) => (
              <div key={i} className="bg-slate-800/80 border border-slate-700/60 rounded-2xl p-4 flex flex-col justify-between">
                <div>
                  <span className="w-7 h-7 rounded-full bg-indigo-600/30 text-indigo-400 font-bold text-xs inline-flex items-center justify-center border border-indigo-500/30 mb-2">
                    {st.step}
                  </span>
                  <h4 className="text-sm font-semibold text-white">{st.title}</h4>
                </div>
                <p className="text-[11px] text-slate-400 mt-2">{st.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Why SmartHire Features */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div className="p-6 bg-white rounded-2xl border border-slate-200">
            <div className="w-10 h-10 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center mb-4">
              <Users className="w-5 h-5" />
            </div>
            <h3 className="text-base font-bold text-slate-900">For Candidates</h3>
            <p className="text-xs text-slate-600 mt-2 leading-relaxed">
              Create a comprehensive profile once, maintain multiple resume versions, get transparent live status updates, and download PDF summaries.
            </p>
          </div>

          <div className="p-6 bg-white rounded-2xl border border-slate-200">
            <div className="w-10 h-10 rounded-xl bg-purple-50 text-purple-600 flex items-center justify-center mb-4">
              <Briefcase className="w-5 h-5" />
            </div>
            <h3 className="text-base font-bold text-slate-900">For Recruiters</h3>
            <p className="text-xs text-slate-600 mt-2 leading-relaxed">
              Post detailed vacancies, review resumes with strict company authorization, advance candidate stages, and schedule interviews with collision detection.
            </p>
          </div>

          <div className="p-6 bg-white rounded-2xl border border-slate-200">
            <div className="w-10 h-10 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center mb-4">
              <ShieldCheck className="w-5 h-5" />
            </div>
            <h3 className="text-base font-bold text-slate-900">For Administrators</h3>
            <p className="text-xs text-slate-600 mt-2 leading-relaxed">
              Prevent fake job postings through mandatory company verification, monitor platform statistics, and activate/deactivate system user accounts.
            </p>
          </div>
        </div>
      </section>

      <ApplyModal
        job={selectedJobForApply}
        isOpen={!!selectedJobForApply}
        onClose={() => setSelectedJobForApply(null)}
        onSuccess={() => {
          alert('Application successfully submitted!');
        }}
      />
    </div>
  );
};
