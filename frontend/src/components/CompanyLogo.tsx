import React, { useState } from 'react';

interface CompanyLogoProps {
  name?: string;
  logoUrl?: string | null;
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  className?: string;
}

// Generate consistent, professional corporate palettes
const getPalette = (name: string) => {
  const palettes = [
    { bg: 'bg-indigo-600', text: 'text-white', ring: 'ring-indigo-100', border: 'border-indigo-700/20' },
    { bg: 'bg-slate-800', text: 'text-white', ring: 'ring-slate-100', border: 'border-slate-700/20' },
    { bg: 'bg-emerald-600', text: 'text-white', ring: 'ring-emerald-100', border: 'border-emerald-700/20' },
    { bg: 'bg-blue-600', text: 'text-white', ring: 'ring-blue-100', border: 'border-blue-700/20' },
    { bg: 'bg-violet-600', text: 'text-white', ring: 'ring-violet-100', border: 'border-violet-700/20' },
    { bg: 'bg-amber-600', text: 'text-white', ring: 'ring-amber-100', border: 'border-amber-700/20' },
    { bg: 'bg-teal-600', text: 'text-white', ring: 'ring-teal-100', border: 'border-teal-700/20' },
  ];

  if (!name) return palettes[0];
  let hash = 0;
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash);
  }
  const index = Math.abs(hash) % palettes.length;
  return palettes[index];
};

const getInitials = (name?: string): string => {
  if (!name) return 'CO';
  const parts = name.trim().split(/\s+/).filter(Boolean);
  if (parts.length === 1) {
    return parts[0].substring(0, 2).toUpperCase();
  }
  return (parts[0][0] + parts[1][0]).toUpperCase();
};

export const CompanyLogo: React.FC<CompanyLogoProps> = ({
  name = 'Company',
  logoUrl,
  size = 'md',
  className = '',
}) => {
  const [imageError, setImageError] = useState(false);

  // Filter out known generic stock placeholders or unsplash links
  const isStockPlaceholder = logoUrl && (logoUrl.includes('unsplash.com') || logoUrl.includes('placeholder'));
  const shouldRenderImage = logoUrl && !imageError && !isStockPlaceholder;

  const sizeStyles = {
    xs: { box: 'w-6 h-6 rounded-md text-[10px]', font: 'text-[10px] tracking-wider' },
    sm: { box: 'w-8 h-8 rounded-lg text-xs', font: 'text-xs tracking-wider' },
    md: { box: 'w-11 h-11 rounded-xl text-sm', font: 'text-sm tracking-wider' },
    lg: { box: 'w-14 h-14 rounded-2xl text-base', font: 'text-base tracking-wider' },
    xl: { box: 'w-20 h-20 rounded-2xl text-2xl', font: 'text-2xl tracking-wider' },
  }[size];

  const palette = getPalette(name);
  const initials = getInitials(name);

  if (shouldRenderImage) {
    return (
      <div
        className={`${sizeStyles.box} bg-white border border-slate-200 overflow-hidden shrink-0 flex items-center justify-center p-1 shadow-2xs ${className}`}
      >
        <img
          src={logoUrl!}
          alt={name}
          onError={() => setImageError(true)}
          className="w-full h-full object-contain"
        />
      </div>
    );
  }

  return (
    <div
      className={`${sizeStyles.box} ${palette.bg} ${palette.text} ${palette.border} border shadow-2xs flex items-center justify-center font-bold font-mono select-none shrink-0 tracking-tight transition-transform ${className}`}
      title={name}
    >
      <span>{initials}</span>
    </div>
  );
};
