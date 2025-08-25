import React, { useEffect, useState } from 'react';
import { getSpendTrackingReport } from '../../services/reportService';

const SpendTrackingReport = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const result = await getSpendTrackingReport();
        setData(result);
      } catch (error) {
        console.error('Error fetching spend tracking report:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  return (
    <>
      <style>{`
        .gradient-header {
          background: linear-gradient(135deg, #4ECDC4 0%, #45B7D1 30%, #667eea 70%, #764ba2 100%);
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
          background: linear-gradient(135deg, #4ECDC4 0%, #45B7D1 50%, #667eea 100%);
          position: relative;
        }
        .gradient-header-table::after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 0;
          right: 0;
          height: 3px;
          background: linear-gradient(90deg, #4ECDC4, #45B7D1, #667eea, #764ba2);
        }
        .table-row-custom {
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          background: rgba(255, 255, 255, 0.7);
        }
        .table-row-custom:hover {
          background: rgba(78, 205, 196, 0.08) !important;
          transform: translateX(5px);
          box-shadow: 0 4px 15px rgba(78, 205, 196, 0.1);
        }
        .table-row-custom:nth-child(even) {
          background: rgba(255, 255, 255, 0.5);
        }
        .table-row-custom:nth-child(even):hover {
          background: rgba(78, 205, 196, 0.08) !important;
        }
      `}</style>

      <div
        className="gradient-header p-4 mb-4 rounded-4 text-white position-relative overflow-hidden"
        style={{ marginTop: '-20px', marginLeft: '-20px', marginRight: '-20px' }}
      >
        <div
          className="position-absolute top-0 start-0 w-100 h-100"
          style={{
            background: 'radial-gradient(circle at 80% 20%, rgba(255,255,255,0.1) 0%, transparent 50%)',
          }}
        ></div>
        <div className="position-relative">
          <div className="d-flex align-items-center">
            <div
              className="d-flex align-items-center justify-content-center me-3 rounded-3"
              style={{
                background: 'rgba(255,255,255,0.15)',
                width: '50px',
                height: '50px',
                backdropFilter: 'blur(10px)',
                border: '1px solid rgba(255,255,255,0.2)',
              }}
            >
              <i className="bi bi-cash-stack fs-4"></i>
            </div>
            <div>
              <h3 className="mb-1 fw-bold">Spend Tracking Report</h3>
              <p className="mb-0 opacity-75">Planned vs actual spending with variance</p>
            </div>
          </div>
        </div>
      </div>

      <div className="glass-card rounded-3 overflow-hidden">
        <div className="table-responsive">
          <table className="table table-hover mb-0 custom-table">
            <thead>
              <tr className="gradient-header-table text-white">
                <th className="border-0 fw-semibold py-3 px-4">Client</th>
                <th className="border-0 fw-semibold py-3 px-4">Project</th>
                <th className="border-0 fw-semibold py-3 px-4">Planned Spend</th>
                <th className="border-0 fw-semibold py-3 px-4">Actual Spend</th>
                <th className="border-0 fw-semibold py-3 px-4">Variance</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan="5" className="text-center py-5 text-muted">
                    Loading...
                  </td>
                </tr>
              ) : data.length === 0 ? (
                <tr>
                  <td colSpan="5" className="text-center py-5 text-muted">
                    <div className="d-flex flex-column align-items-center">
                      <i className="bi bi-cash display-1 mb-3 opacity-25"></i>
                      <h5 className="mb-2">No spend tracking data found</h5>
                      <p className="mb-0">Check back later for updates</p>
                    </div>
                  </td>
                </tr>
              ) : (
                data.map((item, index) => (
                  <tr key={index} className="table-row-custom">
                    <td className="py-3 px-4">{item.clientName}</td>
                    <td className="py-3 px-4">{item.projectName}</td>
                    <td className="py-3 px-4">{item.plannedSpend}</td>
                    <td className="py-3 px-4">{item.actualSpend}</td>
                    <td
                      className="py-3 px-4 fw-bold"
                      style={{
                        color:
                          parseFloat(item.variance) > 0
                            ? 'red'
                            : parseFloat(item.variance) < 0
                            ? 'green'
                            : 'black',
                      }}
                    >
                      {item.variance}
                    </td>
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

export default SpendTrackingReport;
