import React, { useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const reportsList = [
  { name: 'In-Flight Projects', path: '/reports/in-flight' },
  { name: 'Proposed Projects', path: '/reports/proposed' },
  { name: 'Spend Tracking', path: '/reports/spend-tracking' },
  { name: 'Project Status', path: '/reports/project-status' },
  { name: 'Risks & Issues', path: '/reports/risks-issues' },
  { name: 'Resource Allocation', path: '/reports/resource-allocation' },
  { name: 'Bench Tracking', path: '/reports/bench-tracking' },
  { name: 'Forecasting', path: '/reports/forecasting' },
  { name: 'Financial Metrics', path: '/reports/financial-metrics' },
  { name: 'Governance', path: '/reports/governance' },
  { name: 'Portfolio Dashboard', path: '/reports/portfolio' },
  { name: 'Lessons Learned', path: '/reports/lessons-learned' },
];

const iconFor = (name) => ({
  'In-Flight Projects': 'bi-rocket-takeoff',
  'Proposed Projects': 'bi-lightbulb',
  'Spend Tracking': 'bi-cash-coin',
  'Project Status': 'bi-graph-up',
  'Risks & Issues': 'bi-exclamation-triangle',
  'Resource Allocation': 'bi-people',
  'Bench Tracking': 'bi-person-workspace',
  'Forecasting': 'bi-calendar-week',
  'Financial Metrics': 'bi-currency-dollar',
  Governance: 'bi-shield-check',
  'Portfolio Dashboard': 'bi-grid-3x3-gap',
  'Lessons Learned': 'bi-journal-check',
}[name] || 'bi-file-earmark-bar-graph');

const Reports = () => {
  const navigate = useNavigate();
  const [search, setSearch] = useState('');

  const filtered = useMemo(
    () => reportsList.filter(r => r.name.toLowerCase().includes(search.toLowerCase())),
    [search]
  );

  return (
    <>
     
      <style>{`
        .gradient-header {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 25%, #f093fb 50%, #f5576c 75%, #4facfe 100%);
          backdrop-filter: blur(10px);
        }
        .glass-card {
          background: rgba(255, 255, 255, 0.95);
          backdrop-filter: blur(10px);
          border: 1px solid rgba(255, 255, 255, 0.2);
          box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
        }
        .search-input {
          background: rgba(255, 255, 255, 0.9);
          border: 1px solid rgba(102, 126, 234, 0.2);
          backdrop-filter: blur(10px);
          transition: all 0.3s ease;
        }
        .search-input:focus {
          border-color: #667eea;
          box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.15);
          background: rgba(255, 255, 255, 1);
        }
        .resource-card {
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          background: rgba(255, 255, 255, 0.8);
          backdrop-filter: blur(10px);
          border: 1px solid rgba(255, 255, 255, 0.2);
          box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
        }
        .resource-card:hover {
          transform: translateY(-4px);
          box-shadow: 0 12px 32px rgba(102, 126, 234, 0.15);
        }
        .report-icon-wrap {
          background: rgba(102,126,234,0.15);
          width: 44px;
          height: 44px;
          border: 1px solid rgba(102,126,234,0.25);
        }
      `}</style>

     
      <div
        className="gradient-header p-4 mb-4 rounded-4 text-white position-relative overflow-hidden"
        style={{ marginTop: '-20px', marginLeft: '-20px', marginRight: '-20px' }}
      >
        <div className="position-absolute top-0 start-0 w-100 h-100"
             style={{ background: 'radial-gradient(circle at 30% 20%, rgba(255,255,255,0.1) 0%, transparent 50%)' }} />
        <div className="position-relative">
          <div className="d-flex justify-content-between align-items-center">
            <div className="d-flex align-items-center">
              <div className="d-flex align-items-center justify-content-center me-3 rounded-3"
                   style={{ background: 'rgba(255,255,255,0.15)', width: '50px', height: '50px',
                            backdropFilter: 'blur(10px)', border: '1px solid rgba(255,255,255,0.2)' }}>
                <i className="bi bi-bar-chart-steps fs-4"></i>
              </div>
              <div>
                <h3 className="mb-1 fw-bold">Reports & Dashboards</h3>
                <p className="mb-0 opacity-75">Explore project insights and governance</p>
              </div>
            </div>
          </div>
        </div>
      </div>

     
      <div className="glass-card p-3 rounded-3 mb-4">
        <div className="row align-items-center">
          <div className="col-md-8">
            <div className="position-relative">
              <i className="bi bi-search position-absolute top-50 start-0 translate-middle-y ms-3 text-muted"></i>
              <input
                type="text"
                className="form-control search-input ps-5 rounded-3"
                placeholder="Search reports..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>
          </div>
          <div className="col-md-4 text-md-end mt-3 mt-md-0">
            <span className="text-muted small">{filtered.length} of {reportsList.length}</span>
          </div>
        </div>
      </div>

      <div className="row">
        {filtered.map((report, index) => (
          <div className="col-lg-4 col-md-6 mb-4" key={index}>
            <div
              className="resource-card p-4 rounded-3 h-100"
              role="button"
              onClick={() => navigate(report.path)}
            >
              <div className="d-flex align-items-center">
                <div className="d-flex align-items-center justify-content-center me-3 rounded-3 report-icon-wrap">
                  <i className={`bi ${iconFor(report.name)} text-primary`}></i>
                </div>
                <div className="flex-grow-1">
                  <h6 className="mb-1 fw-bold">{report.name}</h6>
                  <small className="text-muted">Click to open</small>
                </div>
                <i className="bi bi-chevron-right text-muted"></i>
              </div>
            </div>
          </div>
        ))}
      </div>
    </>
  );
};

export default Reports;
