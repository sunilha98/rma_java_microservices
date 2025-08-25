import React, { useEffect, useState } from 'react';
import { getBenchTrackingReport } from '../../services/reportService';
import ReportTable from '../../components/ReportTable';

const BenchTrackingReport = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);

  const columns = [
    { header: 'Resource First Name', accessor: 'firstName' },
    { header: 'Resource Last Name', accessor: 'lastName' },
    { header: 'Title', accessor: 'title' },
    { header: 'Skill', accessor: 'skills' },
  ];

  useEffect(() => {
    const fetchData = async () => {
      try {
        const result = await getBenchTrackingReport();
        setData(result);
      } catch (error) {
        console.error('Error fetching report:', error);
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
          background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 30%, #b2fefa 70%, #0ed2f7 100%);
          backdrop-filter: blur(10px);
        }
        .glass-card {
          background: rgba(255, 255, 255, 0.95);
          backdrop-filter: blur(10px);
          border: 1px solid rgba(255, 255, 255, 0.2);
          box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
        }
        .gradient-header-table {
          background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 50%, #b2fefa 100%);
          position: relative;
        }
        .gradient-header-table::after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 0;
          right: 0;
          height: 3px;
          background: linear-gradient(90deg, #a1c4fd, #c2e9fb, #b2fefa, #0ed2f7);
        }
        .table-row-custom {
          transition: all 0.3s ease;
          background: rgba(255, 255, 255, 0.7);
        }
        .table-row-custom:hover {
          background: rgba(161, 196, 253, 0.08) !important;
          transform: translateX(5px);
          box-shadow: 0 4px 15px rgba(161, 196, 253, 0.1);
        }
        .table-row-custom:nth-child(even) {
          background: rgba(255, 255, 255, 0.5);
        }
      `}</style>

      
      <div
        className="gradient-header p-4 mb-4 rounded-4 text-white position-relative overflow-hidden"
        style={{ marginTop: '-20px', marginLeft: '-20px', marginRight: '-20px' }}
      >
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
              <i className="bi bi-people-fill fs-4"></i>
            </div>
            <div>
              <h3 className="mb-1 fw-bold">Bench Tracking</h3>
              <p className="mb-0 opacity-75">Resources currently on bench & available</p>
            </div>
          </div>
        </div>
      </div>

      
      <div className="glass-card rounded-3 overflow-hidden">
        {loading ? (
          <div className="p-5 text-center text-muted">Loading...</div>
        ) : data.length === 0 ? (
          <div className="p-5 text-center text-muted">
            <i className="bi bi-people display-1 mb-3 opacity-25"></i>
            <h5>No resources on bench</h5>
            <p>Everyone is currently allocated</p>
          </div>
        ) : (
          <div className="table-responsive">
            <table className="table table-hover mb-0">
              <thead>
                <tr className="gradient-header-table text-white">
                  {columns.map((col, idx) => (
                    <th key={idx} className="border-0 fw-semibold py-3 px-4">
                      {col.header}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {data.map((row, index) => (
                  <tr key={index} className="table-row-custom">
                    <td className="py-3 px-4">{row.firstName}</td>
                    <td className="py-3 px-4">{row.lastName}</td>
                    <td className="py-3 px-4">{row.title}</td>
                    <td className="py-3 px-4">{row.skills}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </>
  );
};

export default BenchTrackingReport;
