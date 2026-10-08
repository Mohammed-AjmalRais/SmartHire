import React, { useState, useEffect } from 'react';
import { Building2, Globe, MapPin, Briefcase, CheckCircle, AlertTriangle, ShieldCheck } from 'lucide-react';
import type { Company } from '../../types';
import api from '../../api/client';
import { CompanyLogo } from '../../components/CompanyLogo';

export const CompanyProfilePage: React.FC = () => {
  const [company, setCompany] = useState<Company | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [success, setSuccess] = useState(false);

  // Fields
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [website, setWebsite] = useState('');
  const [industry, setIndustry] = useState('');
  const [location, setLocation] = useState('');
  const [logoUrl, setLogoUrl] = useState('');

  useEffect(() => {
    api.get('/recruiter/company')
      .then((res) => {
        if (res.data.success && res.data.data) {
          const c: Company = res.data.data;
          setCompany(c);
          setName(c.name || '');
          setDescription(c.description || '');
          setWebsite(c.website || '');
          setIndustry(c.industry || '');
          setLocation(c.location || '');
          setLogoUrl(c.logoUrl || '');
        }
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setSuccess(false);

    api.post('/recruiter/company', {
      name,
      description,
      website,
      industry,
      location,
      logoUrl,
    })
      .then((res) => {
        if (res.data.success) {
          setCompany(res.data.data);
          setSuccess(true);
        }
      })
      .catch((err) => {
        alert(err.response?.data?.message || 'Error updating company profile');
      })
      .finally(() => {
        setSaving(false);
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
      <div>
        <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Company Profile</h1>
        <p className="text-xs text-slate-500 mt-0.5">
          Configure organization identity, verified status, and candidate-facing branding
        </p>
      </div>

      {/* Verification Status Card */}
      <div className={`p-4 rounded-2xl border flex items-center justify-between gap-4 ${
        company?.verified
          ? 'bg-emerald-50 border-emerald-200 text-emerald-900'
          : 'bg-amber-50 border-amber-200 text-amber-900'
      }`}>
        <div className="flex items-center gap-3">
          {company?.verified ? (
            <ShieldCheck className="w-6 h-6 text-emerald-600 shrink-0" />
          ) : (
            <AlertTriangle className="w-6 h-6 text-amber-600 shrink-0" />
          )}
          <div>
            <h4 className="font-bold text-xs">
              Status: {company?.verified ? 'Verified Employer' : 'Pending Verification'}
            </h4>
            <p className="text-[11px] opacity-90 mt-0.5">
              {company?.verified
                ? 'Your organization is verified. You can post and manage public job vacancies.'
                : 'Your organization is waiting for administrator approval before jobs can be published.'}
            </p>
          </div>
        </div>
      </div>

      {success && (
        <div className="p-3 bg-emerald-50 border border-emerald-200 text-emerald-800 text-xs rounded-xl flex items-center gap-2">
          <CheckCircle className="w-4 h-4 text-emerald-600" />
          <span>Company profile updated successfully!</span>
        </div>
      )}

      <form onSubmit={handleSubmit} className="bg-white p-6 sm:p-8 rounded-2xl border border-slate-200 shadow-xs space-y-5 text-xs">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label className="block font-semibold text-slate-700 mb-1">Company Name *</label>
            <input
              type="text"
              required
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full p-2.5 rounded-xl border border-slate-200 focus:outline-none"
            />
          </div>

          <div>
            <label className="block font-semibold text-slate-700 mb-1">Industry</label>
            <input
              type="text"
              value={industry}
              onChange={(e) => setIndustry(e.target.value)}
              placeholder="e.g. Information Technology & Cloud Services"
              className="w-full p-2.5 rounded-xl border border-slate-200 focus:outline-none"
            />
          </div>

          <div>
            <label className="block font-semibold text-slate-700 mb-1">Website URL</label>
            <input
              type="url"
              value={website}
              onChange={(e) => setWebsite(e.target.value)}
              placeholder="https://example.com"
              className="w-full p-2.5 rounded-xl border border-slate-200 focus:outline-none"
            />
          </div>

          <div>
            <label className="block font-semibold text-slate-700 mb-1">Headquarters Location</label>
            <input
              type="text"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              placeholder="e.g. Hyderabad, India"
              className="w-full p-2.5 rounded-xl border border-slate-200 focus:outline-none"
            />
          </div>

          <div className="sm:col-span-2">
            <label className="block font-semibold text-slate-700 mb-1">Company Brand Mark / Logo URL</label>
            <div className="flex items-center gap-3">
              <CompanyLogo name={name || 'Company'} logoUrl={logoUrl} size="lg" />
              <div className="flex-1">
                <input
                  type="url"
                  value={logoUrl}
                  onChange={(e) => setLogoUrl(e.target.value)}
                  placeholder="https://yourcompany.com/logo.png (optional — defaults to executive monogram)"
                  className="w-full p-2.5 rounded-xl border border-slate-200 focus:outline-none"
                />
                <p className="text-[11px] text-slate-500 mt-1">
                  Leave blank to automatically render a clean enterprise typographic monogram.
                </p>
              </div>
            </div>
          </div>

          <div className="sm:col-span-2">
            <label className="block font-semibold text-slate-700 mb-1">Company Overview & About Us</label>
            <textarea
              rows={4}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Brief description of products, culture, and achievements..."
              className="w-full p-2.5 rounded-xl border border-slate-200 focus:outline-none"
            />
          </div>
        </div>

        <div className="flex justify-end pt-3">
          <button
            type="submit"
            disabled={saving}
            className="px-6 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold rounded-xl shadow-xs transition-colors cursor-pointer disabled:opacity-50"
          >
            {saving ? 'Saving...' : 'Save Company Profile'}
          </button>
        </div>
      </form>
    </div>
  );
};
