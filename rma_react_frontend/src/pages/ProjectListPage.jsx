import React, { useEffect, useState } from 'react';
import api from '../services/api';

const ProjectListPage = () => {
  const [projects, setProjects] = useState([]);

  useEffect(() => {
    fetchProjects();
  }, []);

  const fetchProjects = async () => {
    try {
      const response = await api.get('/projects');
      setProjects(response.data);
    } catch (error) {
      console.error('Error fetching projects:', error);
    }
  };

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
              <i className="bi bi-list-task fs-4"></i>
            </div>
            <div>
              <h3 className="mb-1 fw-bold">Project List</h3>
              <p className="mb-0 opacity-75">All projects with client and status details</p>
            </div>
          </div>
        </div>
      </div>

      <div className="glass-card rounded-3 overflow-hidden">
        <div className="table-responsive">
          <table className="table table-hover mb-0 custom-table">
            <thead>
              <tr className="gradient-header-table text-white">
                <th className="border-0 fw-semibold py-3 px-4">Project Code</th>
                <th className="border-0 fw-semibold py-3 px-4">Name</th>
                <th className="border-0 fw-semibold py-3 px-4">Client</th>
                <th className="border-0 fw-semibold py-3 px-4">Practice</th>
                <th className="border-0 fw-semibold py-3 px-4">Status</th>
                <th className="border-0 fw-semibold py-3 px-4">Start Date</th>
                <th className="border-0 fw-semibold py-3 px-4">End Date</th>
              </tr>
            </thead>
            <tbody>
              {projects.length === 0 ? (
                <tr>
                  <td colSpan="7" className="text-center py-5 text-muted">
                    <div className="d-flex flex-column align-items-center">
                      <i className="bi bi-list-task display-1 mb-3 opacity-25"></i>
                      <h5 className="mb-2">No projects found</h5>
                      <p className="mb-0">Try adding some projects to see them here</p>
                    </div>
                  </td>
                </tr>
              ) : (
                projects.map((project) => (
                  <tr key={project.id} className="table-row-custom">
                    <td className="py-3 px-4">{project.projectCode}</td>
                    <td className="py-3 px-4">{project.name}</td>
                    <td className="py-3 px-4">{project.clientName}</td>
                    <td className="py-3 px-4">{project.practice}</td>
                    <td className="py-3 px-4">
                      <span className={`badge rounded-pill px-3 py-2 ${
                        project.status?.toLowerCase() === 'active'
                          ? 'bg-success'
                          : 'bg-secondary'
                      }`}>
                        {project.status}
                      </span>
                    </td>
                    <td className="py-3 px-4">{project.startDate ? project.startDate.substring(0, 10) : ''}</td>
                    <td className="py-3 px-4">{project.endDate ? project.endDate.substring(0, 10) : ''}</td>
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

export default ProjectListPage;
