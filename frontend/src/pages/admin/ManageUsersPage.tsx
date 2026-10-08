import React, { useState, useEffect } from 'react';
import { Users, Shield, UserX, UserCheck, Search, Mail } from 'lucide-react';
import type { User as UserType } from '../../types';
import api from '../../api/client';
import { useAuth } from '../../context/AuthContext';

export const ManageUsersPage: React.FC = () => {
  const { user: currentUser } = useAuth();
  const [users, setUsers] = useState<UserType[]>([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);

  const fetchUsers = () => {
    setLoading(true);
    api.get('/admin/users')
      .then((res) => {
        if (res.data.success) {
          setUsers(res.data.data);
        }
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleToggleStatus = (id: number, currentEnabled: boolean) => {
    const newStatus = !currentEnabled;
    const action = newStatus ? 'activate' : 'deactivate';
    if (!confirm(`Are you sure you want to ${action} this user's account?`)) return;

    api.put(`/admin/users/${id}/status`, { enabled: newStatus })
      .then((res) => {
        if (res.data.success) {
          setUsers((prev) =>
            prev.map((u) => (u.id === id ? { ...u, enabled: newStatus } : u))
          );
        }
      })
      .catch((err) => {
        alert(err.response?.data?.message || 'Error updating user status');
      });
  };

  if (loading) {
    return (
      <div className="min-h-[50vh] flex items-center justify-center">
        <div className="w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin"></div>
      </div>
    );
  }

  const filtered = users.filter((u) => {
    if (!search) return true;
    const term = search.toLowerCase();
    return u.fullName.toLowerCase().includes(term) || u.email.toLowerCase().includes(term);
  });

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Platform Users</h1>
          <p className="text-xs text-slate-500 mt-0.5">
            Monitor registered accounts across Candidates, Recruiters, and Administrators
          </p>
        </div>

        {/* Search */}
        <div className="flex items-center px-3 py-2 bg-white rounded-xl border border-slate-200 gap-2 w-full sm:w-64">
          <Search className="w-4 h-4 text-slate-400" />
          <input
            type="text"
            placeholder="Search by name or email..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full text-xs bg-transparent focus:outline-none"
          />
        </div>
      </div>

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden shadow-xs">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead className="bg-slate-50 border-b border-slate-200 text-slate-600 uppercase font-semibold">
              <tr>
                <th className="px-5 py-3.5">User Details</th>
                <th className="px-5 py-3.5">Role</th>
                <th className="px-5 py-3.5">Company Association</th>
                <th className="px-5 py-3.5">Account Status</th>
                <th className="px-5 py-3.5">Registered</th>
                <th className="px-5 py-3.5 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100 text-slate-700">
              {filtered.map((u) => {
                const isSelf = currentUser?.id === u.id;

                return (
                  <tr key={u.id} className="hover:bg-slate-50/50 transition-colors">
                    <td className="px-5 py-4">
                      <div className="font-bold text-slate-900 text-sm">{u.fullName}</div>
                      <div className="text-slate-400 text-[11px] mt-0.5 flex items-center gap-1">
                        <Mail className="w-3 h-3" /> {u.email}
                      </div>
                    </td>

                    <td className="px-5 py-4">
                      <span className={`px-2 py-0.5 rounded font-bold text-[10px] uppercase ${
                        u.role === 'ROLE_ADMIN' ? 'bg-indigo-100 text-indigo-800' :
                        u.role === 'ROLE_RECRUITER' ? 'bg-purple-100 text-purple-800' :
                        'bg-blue-100 text-blue-800'
                      }`}>
                        {u.role.replace('ROLE_', '')}
                      </span>
                    </td>

                    <td className="px-5 py-4">
                      {u.companyName ? (
                        <div>
                          <span className="font-semibold text-slate-800">{u.companyName}</span>
                          <span className="text-[10px] text-slate-400 block">
                            {u.companyVerified ? 'Verified' : 'Pending Verification'}
                          </span>
                        </div>
                      ) : (
                        <span className="text-slate-400 italic">None</span>
                      )}
                    </td>

                    <td className="px-5 py-4">
                      <span className={`px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase ${
                        u.enabled
                          ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                          : 'bg-rose-50 text-rose-700 border border-rose-200'
                      }`}>
                        {u.enabled ? 'Active' : 'Deactivated'}
                      </span>
                    </td>

                    <td className="px-5 py-4 text-slate-500">
                      {new Date(u.createdAt).toLocaleDateString([], {
                        month: 'short',
                        day: 'numeric',
                        year: 'numeric',
                      })}
                    </td>

                    <td className="px-5 py-4 text-right">
                      {!isSelf && (
                        <button
                          onClick={() => handleToggleStatus(u.id, u.enabled)}
                          className={`px-3 py-1.5 rounded-lg text-xs font-semibold cursor-pointer transition-colors ${
                            u.enabled
                              ? 'text-rose-600 hover:bg-rose-50 border border-rose-200'
                              : 'text-emerald-700 bg-emerald-50 hover:bg-emerald-100 border border-emerald-200'
                          }`}
                        >
                          {u.enabled ? 'Deactivate' : 'Activate'}
                        </button>
                      )}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
