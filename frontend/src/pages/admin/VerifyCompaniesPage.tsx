import React, { useState, useEffect } from 'react';
import { Building2, CheckCircle2, XCircle, Globe, MapPin, Briefcase, ExternalLink, ShieldCheck, AlertCircle } from 'lucide-react';
import type { Company } from '../../types';
import api from '../../api/client';
import { CompanyLogo } from '../../components/CompanyLogo';

export const VerifyCompaniesPage: React.FC = () => {
  const [companies, setCompanies] = useState<Company[]>([]);
  const [filter, setFilter] = useState<string>('all');
  const [loading, setLoading] = useState(true);
  const [processingId, setProcessingId] = useState<number | null>(null);

  const fetchCompanies = () => {
    setLoading(true);
    const param = filter === 'pending' ? '?verified=false' : filter === 'verified' ? '?verified=true' : '';
    api.get(`/admin/companies${param}`)
      .then((res) => {
        if (res.data.success) {
          setCompanies(res.data.data);
        }
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchCompanies();
  }, [filter]);

  const handleVerify = (id: number, verified: boolean) => {
    const action = verified ? 'verify and approve' : 'reject verification for';
    if (!confirm(`Are you sure you want to ${action} this organization?`)) return;

    setProcessingId(id);
    api.put(`/admin/companies/${id}/verify`, { verified })
      .then((res) => {
        if (res.data.success) {
          setCompanies((prev) =>
            prev.map((c) => (c.id === id ? res.data.data : c))
          );
        }
      })
      .catch((err) => {
        alert(err.response?.data?.message || 'Error updating verification status');
      })
      .finally(() => setProcessingId(null));
  };

  if (loading) {
    return (
      <div className="min-h-[50vh] flex items-center justify-center">
        <div className="w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin"></div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Company Verification</h1>
          <p className="text-xs text-slate-500 mt-0.5">
            Admin review queue: Verify employers to enable them to post genuine job vacancies
          </p>
        </div>

        {/* Filter buttons */}
        <div className="flex items-center gap-1.5 p-1 bg-slate-100 rounded-xl text-xs font-semibold">
          {['all', 'pending', 'verified'].map((f) => (
            <button
              key={f}
              onClick={() => setFilter(f)}
              className={`px-3 py-1.5 rounded-lg capitalize transition-colors cursor-pointer ${
                filter === f
                  ? 'bg-white text-indigo-600 shadow-xs'
                  : 'text-slate-600 hover:text-slate-900'
              }`}
            >
              {f}
            </button>
          ))}
        </div>
      </div>

      {companies.length === 0 ? (
        <div className="bg-white p-12 rounded-2xl border border-slate-200 text-center">
          <Building2 className="w-12 h-12 text-slate-300 mx-auto mb-3" />
          <h3 className="text-base font-bold text-slate-800">No companies in this view</h3>
          <p className="text-xs text-slate-500 mt-1">
            {filter === 'pending'
              ? 'Great! There are no company registrations waiting for verification.'
              : 'No companies match the chosen filter.'}
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {companies.map((c) => (
            <div key={c.id} className="bg-white rounded-2xl border border-slate-200 p-6 shadow-xs flex flex-col justify-between space-y-4">
              <div>
                <div className="flex items-start justify-between gap-3">
                  <div className="flex items-center gap-3">
                    <CompanyLogo name={c.name} logoUrl={c.logoUrl} size="md" />
                    <div>
                      <h3 className="text-base font-bold text-slate-900">{c.name}</h3>
                      <p className="text-xs text-slate-500">{c.industry || 'General Industry'}</p>
                    </div>
                  </div>

                  <span className={`px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase ${
                    c.verified
                      ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                      : 'bg-amber-50 text-amber-700 border border-amber-200'
                  }`}>
                    {c.verified ? 'Verified' : 'Pending Verification'}
                  </span>
                </div>

                <div className="space-y-2 mt-4 text-xs text-slate-600">
                  {c.location && (
                    <div className="flex items-center gap-2">
                      <MapPin className="w-3.5 h-3.5 text-slate-400 shrink-0" />
                      <span>{c.location}</span>
                    </div>
                  )}

                  {c.website && (
                    <div className="flex items-center gap-2">
                      <Globe className="w-3.5 h-3.5 text-slate-400 shrink-0" />
                      <a href={c.website} target="_blank" rel="noreferrer" className="text-indigo-600 hover:underline flex items-center gap-1">
                        {c.website} <ExternalLink className="w-3 h-3" />
                      </a>
                    </div>
                  )}

                  <div className="flex items-center gap-2">
                    <Briefcase className="w-3.5 h-3.5 text-slate-400 shrink-0" />
                    <span>{c.activeJobsCount} Active Job Vacancies</span>
                  </div>

                  {c.description && (
                    <p className="mt-2 p-2.5 bg-slate-50 rounded-xl text-slate-600 text-[11px] line-clamp-3">
                      {c.description}
                    </p>
                  )}
                </div>
              </div>

              {/* Action buttons */}
              <div className="pt-3 border-t border-slate-100 flex items-center justify-end gap-2">
                {!c.verified ? (
                  <>
                    <button
                      onClick={() => handleVerify(c.id, true)}
                      disabled={processingId === c.id}
                      className="px-4 py-2 bg-emerald-600 hover:bg-emerald-700 text-white font-semibold text-xs rounded-xl shadow-xs flex items-center gap-1.5 cursor-pointer disabled:opacity-50 transition-colors"
                    >
                      <CheckCircle2 className="w-4 h-4" /> Approve & Verify
                    </button>
                    <button
                      onClick={() => handleVerify(c.id, false)}
                      disabled={processingId === c.id}
                      className="px-3 py-2 text-rose-600 hover:bg-rose-50 border border-rose-200 font-semibold text-xs rounded-xl cursor-pointer disabled:opacity-50 transition-colors"
                    >
                      Reject
                    </button>
                  </>
                ) : (
                  <button
                    onClick={() => handleVerify(c.id, false)}
                    disabled={processingId === c.id}
                    className="px-3 py-1.5 text-slate-600 hover:text-rose-600 hover:bg-rose-50 border border-slate-200 font-medium text-xs rounded-lg cursor-pointer disabled:opacity-50 transition-colors"
                  >
                    Revoke Verification
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
