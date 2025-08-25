import React, { useEffect, useState } from 'react';
import ProjectStatusForm from '../components/ProjectStatusForm';
import ProjectStatusTable from '../components/ProjectStatusTable';
import api from '../services/api';

const ProjectStatusPage = () => {
  const [updates, setUpdates] = useState([]);
  const [projects, setProjects] = useState([]);
  const [selectedProjectId, setSelectedProjectId] = useState('');

  const loadAllUpdates = async () => {
    const { data } = await api.get('/project-status');
    setUpdates(data);
  };

  const loadUpdatesByProject = async (projectId) => {
    const { data } = await api.get(`/project-status/${projectId}`);
    setUpdates(data);
  };

  const loadProjects = async () => {
    const { data } = await api.get('/projects');
    setProjects(data);
  };

  const handleSubmit = async (formData) => {
    await api.post('/project-status', formData);
    if (selectedProjectId) {
      loadUpdatesByProject(selectedProjectId);
    } else {
      loadAllUpdates();
    }
  };

  const handleProjectFilter = (e) => {
    const projectId = e.target.value;
    setSelectedProjectId(projectId);
    if (projectId) {
      loadUpdatesByProject(projectId);
    } else {
      loadAllUpdates();
    }
  };

  useEffect(() => {
    loadProjects();
    loadAllUpdates();
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
        .table-row-custom {
          transition: all 0.3s ease;
          background: rgba(255, 255, 255, 0.7);
        }
        .table-row-custom:hover {
          background: rgba(78, 205, 196, 0.08) !important;
          transform: translateX(5px);
          box-shadow: 0 4px 15px rgba(78, 205, 196, 0.1);
        }
      `}</style>

      
      <div
        className="gradient-header p-4 mb-4 rounded-4 text-white position-relative overflow-hidden"
        style={{ marginTop: '-20px', marginLeft: '-20px', marginRight: '-20px' }}
      >
        <div
          className="position-absolute top-0 start-0 w-100 h-100"
          style={{
            background:
              'radial-gradient(circle at 80% 20%, rgba(255,255,255,0.1) 0%, transparent 50%)',
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
              <i className="bi bi-clipboard-data fs-4"></i>
            </div>
            <div>
              <h3 className="mb-1 fw-bold">Project Status Update</h3>
              <p className="mb-0 opacity-75">Track latest progress for all projects</p>
            </div>
          </div>
        </div>
      </div>

      
      <div className="glass-card rounded-3 p-4">
       
        <div className="mb-4">
          <label className="form-label fw-bold">Filter by Project</label>
          <select
            className="form-select"
            value={selectedProjectId}
            onChange={handleProjectFilter}
          >
            <option value="">All Projects</option>
            {projects.map((project) => (
              <option key={project.id} value={project.id}>
                {project.name} ({project.projectCode})
              </option>
            ))}
          </select>
        </div>

       
        <div className="mb-4 p-3 border rounded bg-light">
          <h6 className="fw-bold mb-3">Add New Update</h6>
          <ProjectStatusForm onSubmit={handleSubmit} />
        </div>

        
        <div className="table-responsive">
          {updates.length > 0 ? (
            <ProjectStatusTable updates={updates} rowClassName="table-row-custom" />
          ) : (
            <p className="text-muted text-center py-4 mb-0">
              No status updates available.
            </p>
          )}
        </div>
      </div>
    </>
  );
};

export default ProjectStatusPage;
