import React, { useEffect, useState } from 'react';
import api from '../../services/api';

const ForecastingReport = () => {
  const [data, setData] = useState([]);

  useEffect(() => {
    api.get('/reports/forecasting')
      .then(res => setData(res.data))
      .catch(err => console.error(err));
  }, []);

  return (
    <>
      <style>{`
        .gradient-header {
          background: linear-gradient(135deg, #89f7fe 0%, #66a6ff 30%, #c3cfe2 70%, #fbc2eb 100%);
          backdrop-filter: blur(10px);
        }
        .glass-card {
          background: rgba(255, 255, 255, 0.95);
          backdrop-filter: blur(10px);
          border: 1px solid rgba(255, 255, 255, 0.2);
          box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
        }
        .custom-table {
          background: transparent;
        }
        .gradient-header-table {
          background: linear-gradient(135deg, #89f7fe 0%, #66a6ff 50%, #c3cfe2 100%);
          position: relative;
        }
        .gradient-header-table::after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 0;
          right: 0;
          height: 3px;
          background: linear-gradient(90deg, #89f7fe, #66a6ff, #c3cfe2, #fbc2eb);
        }
        .table-row-custom {
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          background: rgba(255, 255, 255, 0.7);
        }
        .table-row-custom:hover {
          background: rgba(137, 247, 254, 0.08) !important;
          transform: translateX(5px);
          box-shadow: 0 4px 15px rgba(137, 247, 254, 0.1);
        }
        .table-row-custom:nth-child(even) {
          background: rgba(255, 255, 255, 0.5);
        }
        .table-row-custom:nth-child(even):hover {
          background: rgba(137, 247, 254, 0.08) !important;
        }
      `}</style>

      <div
        className="gradient-header p-4 mb-4 rounded-4 text-white position-relative overflow-hidden"
        style={{ marginTop: '-20px', marginLeft: '-20px', marginRight: '-20px' }}
      >
        <div className="position-absolute top-0 start-0 w-100 h-100" style={{
          background: 'radial-gradient(circle at 80% 20%, rgba(255,255,255,0.1) 0%, transparent 50%)',
        }}></div>
        <div className="position-relative">
          <div className="d-flex align-items-center">
            <div
              className="d-flex align-items-center justify-content-center me-3 rounded-3"
              style={{
                background: 'rgba(255,255,255,0.15)',
                width: '50px',
                height: '50px',
                backdropFilter: 'blur(10px)',
                border: '1px solid rgba(255,255,255,0.2)'
              }}
            >
              <i className="bi bi-graph-up-arrow fs-4"></i>
            </div>
            <div>
              <h3 className="mb-1 fw-bold">Forecasting Report</h3>
              <p className="mb-0 opacity-75">Future demand vs available resources</p>
            </div>
          </div>
        </div>
      </div>

      <div className="glass-card rounded-3 overflow-hidden">
        <div className="table-responsive">
          <table className="table table-hover mb-0 custom-table">
            <thead>
              <tr className="gradient-header-table text-white">
                <th className="border-0 fw-semibold py-3 px-4">Role</th>
            
                <th className="border-0 fw-semibold py-3 px-4">Future Demand</th>
                <th className="border-0 fw-semibold py-3 px-4">Available Resources</th>
              </tr>
            </thead>
            <tbody>
              {data.length === 0 ? (
                <tr>
                  <td colSpan="3" className="text-center py-5 text-muted">
                    <div className="d-flex flex-column align-items-center">
                      <i className="bi bi-graph-up display-1 mb-3 opacity-25"></i>
                      <h5 className="mb-2">No forecasting data found</h5>
                      <p className="mb-0">Check back later for updated projections</p>
                    </div>
                  </td>
                </tr>
              ) : (
                data.map((item, index) => (
                  <tr key={index} className="table-row-custom">
                    <td className="py-3 px-4">{item.role}</td>
                    
                    <td className="py-3 px-4">{item.futureDemand}</td>
                    <td className="py-3 px-4">{item.availableResources}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
};

export default ForecastingReport;
