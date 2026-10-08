import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Search, MapPin, Filter, RotateCcw, ChevronLeft, ChevronRight, Briefcase } from 'lucide-react';
import type { Job, EmploymentType, WorkMode } from '../types';
import api from '../api/client';
import { JobCard } from '../components/JobCard';
import { ApplyModal } from '../components/ApplyModal';

export const JobsPage: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const [jobs, setJobs] = useState<Job[]>([]);
  const [loading, setLoading] = useState(true);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  // Filters state
  const [keyword, setKeyword] = useState(searchParams.get('keyword') || '');
  const [location, setLocation] = useState(searchParams.get('location') || '');
  const [experience, setExperience] = useState<string>('');
  const [employmentType, setEmploymentType] = useState<string>('');
  const [workMode, setWorkMode] = useState<string>('');
  const [minSalary, setMinSalary] = useState<string>('');
  const [skill, setSkill] = useState<string>('');

  const [selectedJobForApply, setSelectedJobForApply] = useState<Job | null>(null);

  const fetchJobs = (page = 0) => {
    setLoading(true);
    const params = new URLSearchParams();
    params.set('page', page.toString());
    params.set('size', '8');

    if (keyword) params.set('keyword', keyword);
    if (location) params.set('location', location);
    if (experience) params.set('experience', experience);
    if (employmentType) params.set('employmentType', employmentType);
    if (workMode) params.set('workMode', workMode);
    if (minSalary) params.set('minSalary', (Number(minSalary) * 100000).toString());
    if (skill) params.set('skill', skill);

    api.get(`/jobs?${params.toString()}`)
      .then((res) => {
        if (res.data.success && res.data.data) {
          setJobs(res.data.data.content);
          setTotalPages(res.data.data.totalPages);
          setTotalElements(res.data.data.totalElements);
          setCurrentPage(res.data.data.pageNumber);
        }
      })
      .catch(() => {})
      .finally(() => {
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchJobs(0);
  }, [searchParams]);

  const handleFilterSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    fetchJobs(0);
  };

  const handleReset = () => {
    setKeyword('');
    setLocation('');
    setExperience('');
    setEmploymentType('');
    setWorkMode('');
    setMinSalary('');
    setSkill('');
    setSearchParams({});
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      {/* Top Search bar */}
      <div className="bg-white p-4 rounded-2xl border border-slate-200 shadow-xs">
        <form onSubmit={handleFilterSubmit} className="flex flex-col md:flex-row gap-3">
          <div className="flex-1 flex items-center px-3 py-2 bg-slate-50 rounded-xl border border-slate-200 gap-2">
            <Search className="w-4 h-4 text-slate-400" />
            <input
              type="text"
              placeholder="Search keyword (e.g. Java, Backend, TechNova)..."
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              className="w-full text-xs sm:text-sm bg-transparent focus:outline-none"
            />
          </div>

          <div className="flex-1 flex items-center px-3 py-2 bg-slate-50 rounded-xl border border-slate-200 gap-2">
            <MapPin className="w-4 h-4 text-slate-400" />
            <input
              type="text"
              placeholder="Location (e.g. Hyderabad, Remote)..."
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              className="w-full text-xs sm:text-sm bg-transparent focus:outline-none"
            />
          </div>

          <button
            type="submit"
            className="px-6 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold text-xs sm:text-sm rounded-xl shadow-sm transition-colors cursor-pointer"
          >
            Search
          </button>
        </form>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        {/* Sidebar Filters */}
        <div className="bg-white p-5 rounded-2xl border border-slate-200 h-fit space-y-5">
          <div className="flex items-center justify-between pb-3 border-b border-slate-100">
            <div className="flex items-center gap-2 text-sm font-bold text-slate-800">
              <Filter className="w-4 h-4 text-indigo-600" />
              <span>Filter Vacancies</span>
            </div>
            <button
              onClick={handleReset}
              className="text-xs text-slate-400 hover:text-indigo-600 flex items-center gap-1 cursor-pointer"
            >
              <RotateCcw className="w-3 h-3" /> Reset
            </button>
          </div>

          {/* Work Mode */}
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-2">Work Mode</label>
            <div className="space-y-1.5">
              {['REMOTE', 'HYBRID', 'ON_SITE'].map((mode) => (
                <label key={mode} className="flex items-center gap-2 text-xs text-slate-600 cursor-pointer">
                  <input
                    type="radio"
                    name="workMode"
                    value={mode}
                    checked={workMode === mode}
                    onChange={(e) => setWorkMode(e.target.value)}
                    className="text-indigo-600"
                  />
                  <span>{mode.replace('_', ' ')}</span>
                </label>
              ))}
            </div>
          </div>

          {/* Employment Type */}
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-2">Employment Type</label>
            <div className="space-y-1.5">
              {['FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERNSHIP'].map((type) => (
                <label key={type} className="flex items-center gap-2 text-xs text-slate-600 cursor-pointer">
                  <input
                    type="radio"
                    name="empType"
                    value={type}
                    checked={employmentType === type}
                    onChange={(e) => setEmploymentType(e.target.value)}
                    className="text-indigo-600"
                  />
                  <span>{type.replace('_', ' ')}</span>
                </label>
              ))}
            </div>
          </div>

          {/* Experience level */}
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-2">Experience (Years)</label>
            <select
              value={experience}
              onChange={(e) => setExperience(e.target.value)}
              className="w-full text-xs p-2.5 rounded-lg border border-slate-200 bg-white"
            >
              <option value="">Any Experience</option>
              <option value="0">0 (Fresher)</option>
              <option value="1">1 Year</option>
              <option value="2">2 Years</option>
              <option value="3">3 Years</option>
              <option value="5">5+ Years</option>
            </select>
          </div>

          {/* Min Salary */}
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-2">Minimum Salary (Lakhs PA)</label>
            <select
              value={minSalary}
              onChange={(e) => setMinSalary(e.target.value)}
              className="w-full text-xs p-2.5 rounded-lg border border-slate-200 bg-white"
            >
              <option value="">Any Salary</option>
              <option value="3">₹3 LPA +</option>
              <option value="6">₹6 LPA +</option>
              <option value="10">₹10 LPA +</option>
              <option value="15">₹15 LPA +</option>
              <option value="25">₹25 LPA +</option>
            </select>
          </div>

          {/* Specific Skill */}
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-2">Specific Skill</label>
            <input
              type="text"
              placeholder="e.g. Java, Spring Boot, React"
              value={skill}
              onChange={(e) => setSkill(e.target.value)}
              className="w-full text-xs p-2.5 rounded-lg border border-slate-200"
            />
          </div>

          <button
            onClick={() => fetchJobs(0)}
            className="w-full py-2 bg-slate-900 hover:bg-slate-800 text-white text-xs font-semibold rounded-lg cursor-pointer"
          >
            Apply Filters
          </button>
        </div>

        {/* Results listing */}
        <div className="lg:col-span-3 space-y-4">
          <div className="flex items-center justify-between text-xs text-slate-500">
            <span>
              Showing <strong className="text-slate-800">{jobs.length}</strong> of{' '}
              <strong className="text-slate-800">{totalElements}</strong> jobs available
            </span>
          </div>

          {loading ? (
            <div className="bg-white p-12 rounded-2xl border border-slate-200 text-center">
              <div className="w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin mx-auto mb-3"></div>
              <p className="text-xs text-slate-500">Loading vacancies...</p>
            </div>
          ) : jobs.length === 0 ? (
            <div className="bg-white p-12 rounded-2xl border border-slate-200 text-center">
              <Briefcase className="w-12 h-12 text-slate-300 mx-auto mb-3" />
              <h3 className="text-base font-bold text-slate-800">No jobs match your criteria</h3>
              <p className="text-xs text-slate-500 mt-1 max-w-sm mx-auto">
                Try loosening your filters, removing keywords, or searching across all locations.
              </p>
              <button
                onClick={handleReset}
                className="mt-4 px-4 py-2 bg-indigo-50 text-indigo-600 text-xs font-semibold rounded-lg hover:bg-indigo-100 cursor-pointer"
              >
                Reset All Filters
              </button>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {jobs.map((job) => (
                <JobCard
                  key={job.id}
                  job={job}
                  onApplyClick={(j) => setSelectedJobForApply(j)}
                />
              ))}
            </div>
          )}

          {/* Pagination */}
          {totalPages > 1 && (
            <div className="flex items-center justify-center gap-2 pt-6">
              <button
                disabled={currentPage === 0}
                onClick={() => fetchJobs(currentPage - 1)}
                className="p-2 border border-slate-200 rounded-lg hover:bg-slate-50 disabled:opacity-40 cursor-pointer"
              >
                <ChevronLeft className="w-4 h-4" />
              </button>
              <span className="text-xs text-slate-600 font-medium px-2">
                Page {currentPage + 1} of {totalPages}
              </span>
              <button
                disabled={currentPage >= totalPages - 1}
                onClick={() => fetchJobs(currentPage + 1)}
                className="p-2 border border-slate-200 rounded-lg hover:bg-slate-50 disabled:opacity-40 cursor-pointer"
              >
                <ChevronRight className="w-4 h-4" />
              </button>
            </div>
          )}
        </div>
      </div>

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
