import React, { useState, useEffect } from 'react';
import { User, Phone, MapPin, Globe, Briefcase, GraduationCap, FileText, Upload, Plus, Trash2, CheckCircle2, Download, AlertCircle } from 'lucide-react';
import type { CandidateProfile, Education, WorkExperience, Resume } from '../../types';
import api from '../../api/client';
import { useAuth } from '../../context/AuthContext';

export const CandidateProfilePage: React.FC = () => {
  const { user } = useAuth();
  const [profile, setProfile] = useState<CandidateProfile | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  // Form states
  const [fullName, setFullName] = useState('');
  const [phone, setPhone] = useState('');
  const [location, setLocation] = useState('');
  const [professionalSummary, setProfessionalSummary] = useState('');
  const [githubUrl, setGithubUrl] = useState('');
  const [linkedinUrl, setLinkedinUrl] = useState('');
  const [portfolioUrl, setPortfolioUrl] = useState('');
  const [yearsOfExperience, setYearsOfExperience] = useState<number>(0);
  const [skills, setSkills] = useState<string[]>([]);
  const [skillInput, setSkillInput] = useState('');

  const [educations, setEducations] = useState<Education[]>([]);
  const [workExperiences, setWorkExperiences] = useState<WorkExperience[]>([]);
  const [resumes, setResumes] = useState<Resume[]>([]);
  const [uploadingResume, setUploadingResume] = useState(false);

  const fetchProfile = () => {
    setLoading(true);
    api.get('/candidate/profile')
      .then((res) => {
        if (res.data.success && res.data.data) {
          const p: CandidateProfile = res.data.data;
          setProfile(p);
          setFullName(p.fullName || '');
          setPhone(p.phone || '');
          setLocation(p.location || '');
          setProfessionalSummary(p.professionalSummary || '');
          setGithubUrl(p.githubUrl || '');
          setLinkedinUrl(p.linkedinUrl || '');
          setPortfolioUrl(p.portfolioUrl || '');
          setYearsOfExperience(p.yearsOfExperience || 0);
          setSkills(p.skills || []);
          setEducations(p.educations || []);
          setWorkExperiences(p.workExperiences || []);
          setResumes(p.resumes || []);
        }
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchProfile();
  }, []);

  const handleAddSkill = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && skillInput.trim()) {
      e.preventDefault();
      if (!skills.includes(skillInput.trim())) {
        setSkills([...skills, skillInput.trim()]);
      }
      setSkillInput('');
    }
  };

  const handleRemoveSkill = (skillToRemove: string) => {
    setSkills(skills.filter((s) => s !== skillToRemove));
  };

  const handleAddEducation = () => {
    setEducations([
      ...educations,
      { institution: '', degree: '', fieldOfStudy: '', startDate: '', endDate: '', grade: '' },
    ]);
  };

  const handleRemoveEducation = (index: number) => {
    setEducations(educations.filter((_, i) => i !== index));
  };

  const handleAddExperience = () => {
    setWorkExperiences([
      ...workExperiences,
      { companyName: '', jobTitle: '', location: '', startDate: '', isCurrent: true, description: '' },
    ]);
  };

  const handleRemoveExperience = (index: number) => {
    setWorkExperiences(workExperiences.filter((_, i) => i !== index));
  };

  const handleResumeUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);
    setUploadingResume(true);
    setErrorMsg(null);

    api.post('/resumes/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
      .then((res) => {
        if (res.data.success) {
          setSuccessMsg('Resume uploaded successfully!');
          fetchProfile();
        }
      })
      .catch((err) => {
        setErrorMsg(err.response?.data?.message || 'Failed to upload resume. PDF/DOCX max 10MB.');
      })
      .finally(() => {
        setUploadingResume(false);
      });
  };

  const handleSetPrimaryResume = (id: number) => {
    api.put(`/resumes/${id}/primary`)
      .then(() => {
        setResumes((prev) => prev.map((r) => ({ ...r, isPrimary: r.id === id })));
        setSuccessMsg('Primary resume updated');
      })
      .catch(() => {});
  };

  const handleDeleteResume = (id: number) => {
    if (!confirm('Are you sure you want to delete this resume?')) return;
    api.delete(`/resumes/${id}`)
      .then(() => {
        setResumes((prev) => prev.filter((r) => r.id !== id));
      })
      .catch(() => {});
  };

  const handleSaveProfile = (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setErrorMsg(null);
    setSuccessMsg(null);

    api.put('/candidate/profile', {
      fullName,
      phone,
      location,
      professionalSummary,
      githubUrl,
      linkedinUrl,
      portfolioUrl,
      yearsOfExperience: Number(yearsOfExperience),
      skills,
      educations,
      workExperiences,
    })
      .then((res) => {
        if (res.data.success) {
          setSuccessMsg('Profile updated successfully!');
          setProfile(res.data.data);
        }
      })
      .catch((err) => {
        setErrorMsg(err.response?.data?.message || 'Error updating profile');
      })
      .finally(() => {
        setSaving(false);
      });
  };

  const handleDownloadProfilePdf = () => {
    if (!user) return;
    window.open(`/api/reports/candidate/${user.id}/pdf`, '_blank');
  };

  if (loading) {
    return (
      <div className="min-h-[50vh] flex items-center justify-center">
        <div className="w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin"></div>
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Professional Profile</h1>
          <p className="text-xs text-slate-500 mt-0.5">Manage your candidate credentials, skills, and resume versions</p>
        </div>

        <button
          onClick={handleDownloadProfilePdf}
          className="inline-flex items-center gap-1.5 px-4 py-2 text-xs font-semibold text-slate-700 bg-white hover:bg-slate-50 border border-slate-200 rounded-xl shadow-xs cursor-pointer transition-colors"
        >
          <Download className="w-3.5 h-3.5" /> Download Profile PDF
        </button>
      </div>

      {successMsg && (
        <div className="p-3 bg-emerald-50 border border-emerald-200 text-emerald-800 text-xs rounded-xl flex items-center gap-2">
          <CheckCircle2 className="w-4 h-4 text-emerald-600 shrink-0" />
          <span>{successMsg}</span>
        </div>
      )}

      {errorMsg && (
        <div className="p-3 bg-rose-50 border border-rose-200 text-rose-700 text-xs rounded-xl flex items-center gap-2">
          <AlertCircle className="w-4 h-4 shrink-0" />
          <span>{errorMsg}</span>
        </div>
      )}

      <form onSubmit={handleSaveProfile} className="space-y-6">
        {/* Personal Details */}
        <div className="bg-white p-6 rounded-2xl border border-slate-200 space-y-4">
          <h2 className="text-sm font-bold text-slate-900 flex items-center gap-2">
            <User className="w-4 h-4 text-indigo-600" /> Basic Information
          </h2>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1">Full Name</label>
              <input
                type="text"
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
                className="w-full text-xs p-2.5 rounded-xl border border-slate-200 focus:outline-none"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1">Email Address</label>
              <input
                type="email"
                disabled
                value={profile?.email || ''}
                className="w-full text-xs p-2.5 rounded-xl border border-slate-200 bg-slate-50 text-slate-500 cursor-not-allowed"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1">Phone Number</label>
              <input
                type="text"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                placeholder="+91 98765 43210"
                className="w-full text-xs p-2.5 rounded-xl border border-slate-200 focus:outline-none"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1">Location</label>
              <input
                type="text"
                value={location}
                onChange={(e) => setLocation(e.target.value)}
                placeholder="e.g. Hyderabad, India"
                className="w-full text-xs p-2.5 rounded-xl border border-slate-200 focus:outline-none"
              />
            </div>

            <div className="sm:col-span-2">
              <label className="block text-xs font-semibold text-slate-700 mb-1">Professional Summary</label>
              <textarea
                rows={3}
                value={professionalSummary}
                onChange={(e) => setProfessionalSummary(e.target.value)}
                placeholder="Write a concise overview of your background, technical interests, and achievements..."
                className="w-full text-xs p-2.5 rounded-xl border border-slate-200 focus:outline-none"
              />
            </div>
          </div>
        </div>

        {/* Links & Experience */}
        <div className="bg-white p-6 rounded-2xl border border-slate-200 space-y-4">
          <h2 className="text-sm font-bold text-slate-900 flex items-center gap-2">
            <Globe className="w-4 h-4 text-indigo-600" /> Links & Experience Level
          </h2>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1">Years of Total Experience</label>
              <input
                type="number"
                min={0}
                value={yearsOfExperience}
                onChange={(e) => setYearsOfExperience(Number(e.target.value))}
                className="w-full text-xs p-2.5 rounded-xl border border-slate-200 focus:outline-none"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1">GitHub Profile URL</label>
              <input
                type="url"
                value={githubUrl}
                onChange={(e) => setGithubUrl(e.target.value)}
                placeholder="https://github.com/username"
                className="w-full text-xs p-2.5 rounded-xl border border-slate-200 focus:outline-none"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1">LinkedIn Profile URL</label>
              <input
                type="url"
                value={linkedinUrl}
                onChange={(e) => setLinkedinUrl(e.target.value)}
                placeholder="https://linkedin.com/in/username"
                className="w-full text-xs p-2.5 rounded-xl border border-slate-200 focus:outline-none"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1">Portfolio / Personal Website</label>
              <input
                type="url"
                value={portfolioUrl}
                onChange={(e) => setPortfolioUrl(e.target.value)}
                placeholder="https://portfolio.dev"
                className="w-full text-xs p-2.5 rounded-xl border border-slate-200 focus:outline-none"
              />
            </div>
          </div>
        </div>

        {/* Skills */}
        <div className="bg-white p-6 rounded-2xl border border-slate-200 space-y-4">
          <h2 className="text-sm font-bold text-slate-900 flex items-center gap-2">
            <Briefcase className="w-4 h-4 text-indigo-600" /> Technical Skills
          </h2>

          <div>
            <input
              type="text"
              value={skillInput}
              onChange={(e) => setSkillInput(e.target.value)}
              onKeyDown={handleAddSkill}
              placeholder="Type a skill (e.g. Java, Docker, REST API) and press Enter..."
              className="w-full text-xs p-2.5 rounded-xl border border-slate-200 focus:outline-none mb-3"
            />

            <div className="flex flex-wrap gap-2">
              {skills.map((s, idx) => (
                <span
                  key={idx}
                  className="inline-flex items-center gap-1.5 px-3 py-1 bg-indigo-50 text-indigo-700 border border-indigo-100 rounded-lg text-xs font-medium"
                >
                  {s}
                  <button
                    type="button"
                    onClick={() => handleRemoveSkill(s)}
                    className="hover:text-rose-600 cursor-pointer"
                  >
                    ×
                  </button>
                </span>
              ))}
            </div>
          </div>
        </div>

        {/* Education History */}
        <div className="bg-white p-6 rounded-2xl border border-slate-200 space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-sm font-bold text-slate-900 flex items-center gap-2">
              <GraduationCap className="w-4 h-4 text-indigo-600" /> Education History
            </h2>
            <button
              type="button"
              onClick={handleAddEducation}
              className="text-xs font-semibold text-indigo-600 hover:text-indigo-800 flex items-center gap-1 cursor-pointer"
            >
              <Plus className="w-3.5 h-3.5" /> Add Degree
            </button>
          </div>

          <div className="space-y-3">
            {educations.map((edu, idx) => (
              <div key={idx} className="p-4 rounded-xl border border-slate-200 bg-slate-50/50 space-y-3 relative">
                <button
                  type="button"
                  onClick={() => handleRemoveEducation(idx)}
                  className="absolute top-3 right-3 text-slate-400 hover:text-rose-600 cursor-pointer"
                >
                  <Trash2 className="w-4 h-4" />
                </button>

                <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
                  <div>
                    <label className="block text-[11px] font-semibold text-slate-600 mb-1">Institution</label>
                    <input
                      type="text"
                      value={edu.institution}
                      onChange={(e) => {
                        const copy = [...educations];
                        copy[idx].institution = e.target.value;
                        setEducations(copy);
                      }}
                      placeholder="e.g. Stanford University"
                      className="w-full text-xs p-2 bg-white rounded-lg border border-slate-200"
                    />
                  </div>

                  <div>
                    <label className="block text-[11px] font-semibold text-slate-600 mb-1">Degree</label>
                    <input
                      type="text"
                      value={edu.degree}
                      onChange={(e) => {
                        const copy = [...educations];
                        copy[idx].degree = e.target.value;
                        setEducations(copy);
                      }}
                      placeholder="e.g. B.Tech Computer Science"
                      className="w-full text-xs p-2 bg-white rounded-lg border border-slate-200"
                    />
                  </div>

                  <div>
                    <label className="block text-[11px] font-semibold text-slate-600 mb-1">Grade / CGPA</label>
                    <input
                      type="text"
                      value={edu.grade || ''}
                      onChange={(e) => {
                        const copy = [...educations];
                        copy[idx].grade = e.target.value;
                        setEducations(copy);
                      }}
                      placeholder="e.g. 8.5 CGPA"
                      className="w-full text-xs p-2 bg-white rounded-lg border border-slate-200"
                    />
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Work Experiences */}
        <div className="bg-white p-6 rounded-2xl border border-slate-200 space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-sm font-bold text-slate-900 flex items-center gap-2">
              <Briefcase className="w-4 h-4 text-indigo-600" /> Work Experience
            </h2>
            <button
              type="button"
              onClick={handleAddExperience}
              className="text-xs font-semibold text-indigo-600 hover:text-indigo-800 flex items-center gap-1 cursor-pointer"
            >
              <Plus className="w-3.5 h-3.5" /> Add Experience
            </button>
          </div>

          <div className="space-y-3">
            {workExperiences.map((exp, idx) => (
              <div key={idx} className="p-4 rounded-xl border border-slate-200 bg-slate-50/50 space-y-3 relative">
                <button
                  type="button"
                  onClick={() => handleRemoveExperience(idx)}
                  className="absolute top-3 right-3 text-slate-400 hover:text-rose-600 cursor-pointer"
                >
                  <Trash2 className="w-4 h-4" />
                </button>

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                  <div>
                    <label className="block text-[11px] font-semibold text-slate-600 mb-1">Company Name</label>
                    <input
                      type="text"
                      value={exp.companyName}
                      onChange={(e) => {
                        const copy = [...workExperiences];
                        copy[idx].companyName = e.target.value;
                        setWorkExperiences(copy);
                      }}
                      placeholder="e.g. CloudTech Systems"
                      className="w-full text-xs p-2 bg-white rounded-lg border border-slate-200"
                    />
                  </div>

                  <div>
                    <label className="block text-[11px] font-semibold text-slate-600 mb-1">Job Title</label>
                    <input
                      type="text"
                      value={exp.jobTitle}
                      onChange={(e) => {
                        const copy = [...workExperiences];
                        copy[idx].jobTitle = e.target.value;
                        setWorkExperiences(copy);
                      }}
                      placeholder="e.g. Junior Backend Engineer"
                      className="w-full text-xs p-2 bg-white rounded-lg border border-slate-200"
                    />
                  </div>

                  <div className="sm:col-span-2">
                    <label className="block text-[11px] font-semibold text-slate-600 mb-1">Role Description & Achievements</label>
                    <textarea
                      rows={2}
                      value={exp.description || ''}
                      onChange={(e) => {
                        const copy = [...workExperiences];
                        copy[idx].description = e.target.value;
                        setWorkExperiences(copy);
                      }}
                      placeholder="Key responsibilities, tools, and business impacts..."
                      className="w-full text-xs p-2 bg-white rounded-lg border border-slate-200"
                    />
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Resumes */}
        <div className="bg-white p-6 rounded-2xl border border-slate-200 space-y-4">
          <div className="flex items-center justify-between">
            <div>
              <h2 className="text-sm font-bold text-slate-900 flex items-center gap-2">
                <FileText className="w-4 h-4 text-indigo-600" /> Resume Management
              </h2>
              <p className="text-[11px] text-slate-400 mt-0.5">Maintain multiple versions of your resume (PDF/DOCX, max 10MB)</p>
            </div>

            <label className="px-3 py-1.5 bg-indigo-50 hover:bg-indigo-100 text-indigo-600 text-xs font-semibold rounded-lg flex items-center gap-1.5 cursor-pointer">
              <Upload className="w-3.5 h-3.5" />
              <span>{uploadingResume ? 'Uploading...' : 'Upload New Version'}</span>
              <input type="file" accept=".pdf,.doc,.docx" onChange={handleResumeUpload} className="hidden" disabled={uploadingResume} />
            </label>
          </div>

          <div className="divide-y divide-slate-100 border border-slate-200 rounded-xl overflow-hidden">
            {resumes.length === 0 ? (
              <div className="p-6 text-center text-xs text-slate-400">
                No resumes uploaded yet. Upload a PDF or DOCX file to use when applying for vacancies.
              </div>
            ) : (
              resumes.map((r) => (
                <div key={r.id} className="p-3.5 flex items-center justify-between hover:bg-slate-50/50">
                  <div className="flex items-center gap-3">
                    <FileText className="w-5 h-5 text-indigo-600 shrink-0" />
                    <div>
                      <div className="flex items-center gap-2">
                        <span className="text-xs font-semibold text-slate-800">{r.originalFileName}</span>
                        {r.isPrimary && (
                          <span className="text-[10px] font-bold px-1.5 py-0.5 rounded bg-emerald-50 text-emerald-700 border border-emerald-200">
                            Primary
                          </span>
                        )}
                      </div>
                      <span className="text-[11px] text-slate-400 block mt-0.5">
                        Version {r.version} • {(r.fileSize / 1024).toFixed(0)} KB •{' '}
                        {new Date(r.uploadedAt).toLocaleDateString()}
                      </span>
                    </div>
                  </div>

                  <div className="flex items-center gap-2">
                    <a
                      href={`/api/resumes/${r.id}/download`}
                      download
                      className="p-1.5 text-slate-400 hover:text-indigo-600 rounded-md"
                      title="Download file"
                    >
                      <Download className="w-4 h-4" />
                    </a>
                    {!r.isPrimary && (
                      <button
                        type="button"
                        onClick={() => handleSetPrimaryResume(r.id)}
                        className="px-2.5 py-1 text-xs font-medium text-slate-600 hover:text-indigo-600 border border-slate-200 rounded-lg cursor-pointer"
                      >
                        Set Primary
                      </button>
                    )}
                    <button
                      type="button"
                      onClick={() => handleDeleteResume(r.id)}
                      className="p-1.5 text-slate-400 hover:text-rose-600 rounded-md cursor-pointer"
                      title="Delete version"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

        <div className="flex justify-end pt-2">
          <button
            type="submit"
            disabled={saving}
            className="px-8 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold text-xs sm:text-sm rounded-xl shadow-sm transition-colors cursor-pointer disabled:opacity-50"
          >
            {saving ? 'Saving changes...' : 'Save Profile Changes'}
          </button>
        </div>
      </form>
    </div>
  );
};
