import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

const DashboardPage = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState({});
  const [activities, setActivities] = useState([]);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const statsResponse = await api.get('/dashboard/metrics');
        setStats(statsResponse.data);

        const activityResponse = await api.get('/activity/recent');
        setActivities(activityResponse.data);
      } catch (err) {
        console.error('Error fetching dashboard data:', err);
      }
    };
    fetchDashboardData();
  }, []);

  const getModuleIcon = (moduleName = '') => {
    const name = moduleName.toLowerCase();
    if (name.includes('client')) return 'bi-person-square';
    if (name.includes('user')) return 'bi-people-fill';
    if (name.includes('project')) return 'bi-kanban';
    if (name.includes('resource')) return 'bi-diagram-3-fill';
    if (name.includes('sow')) return 'bi-file-earmark-arrow-up';
    if (name.includes('fulfillment')) return 'bi-check2-circle';
    if (name.includes('lesson')) return 'bi-lightbulb';
    return 'bi-info-circle';
  };

  const timeSince = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    const seconds = Math.floor((new Date() - date) / 1000);
    let interval = seconds / 31536000;
    if (interval > 1) return Math.floor(interval) + ' years ago';
    interval = seconds / 2592000;
    if (interval > 1) return Math.floor(interval) + ' months ago';
    interval = seconds / 86400;
    if (interval > 1) return Math.floor(interval) + ' days ago';
    interval = seconds / 3600;
    if (interval > 1) return Math.floor(interval) + ' hours ago';
    interval = seconds / 60;
    if (interval > 1) return Math.floor(interval) + ' minutes ago';
    if (seconds < 10) return 'just now';
    return Math.floor(seconds) + ' seconds ago';
  };

  return (
    <>
      <style>{`
        .dashboard-container {
          background: linear-gradient(135deg, #f8faff 0%, #f0f4ff 100%);
          min-height: 100vh;
          padding: 20px;
        }
        
        .welcome-header {
          background: linear-gradient(135deg, #e8f2ff 0%, #f5f7ff 100%);
          border: 1px solid rgba(99, 132, 255, 0.08);
          border-radius: 16px;
          padding: 24px 28px;
          margin-bottom: 24px;
          box-shadow: 0 2px 12px rgba(99, 132, 255, 0.05);
        }
        
        .welcome-title {
          color: #2563eb;
          font-weight: 600;
          font-size: 1.5rem;
          margin-bottom: 4px;
        }
        
        .welcome-subtitle {
          color: #64748b;
          font-size: 0.95rem;
          margin: 0;
        }
        
        .stats-container {
          margin-bottom: 28px;
        }
        
        .stat-card {
          background: white;
          border-radius: 16px;
          padding: 24px;
          border: 1px solid rgba(0,0,0,0.04);
          box-shadow: 0 2px 8px rgba(0,0,0,0.04);
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          height: 100%;
          position: relative;
          overflow: hidden;
        }
        
        .stat-card::before {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          height: 4px;
        }
        
        .stat-card:hover {
          transform: translateY(-2px);
          box-shadow: 0 8px 25px rgba(0,0,0,0.08);
          border-color: rgba(0,0,0,0.08);
        }
        
        .stat-card.projects::before {
          background: linear-gradient(90deg, #a7f3d0 0%, #6ee7b7 100%);
        }
        
        .stat-card.resources::before {
          background: linear-gradient(90deg, #bfdbfe 0%, #93c5fd 100%);
        }
        
        .stat-card.bench::before {
          background: linear-gradient(90deg, #fed7aa 0%, #fdba74 100%);
        }
        
        .stat-icon {
          width: 48px;
          height: 48px;
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-bottom: 16px;
        }
        
        .stat-icon.projects {
          background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
          color: #059669;
        }
        
        .stat-icon.resources {
          background: linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%);
          color: #2563eb;
        }
        
        .stat-icon.bench {
          background: linear-gradient(135deg, #fff7ed 0%, #fed7aa 100%);
          color: #ea580c;
        }
        
        .stat-number {
          font-size: 2.25rem;
          font-weight: 700;
          color: #1e293b;
          margin-bottom: 4px;
          line-height: 1;
        }
        
        .stat-label {
          color: #64748b;
          font-size: 0.875rem;
          font-weight: 500;
          margin: 0;
        }
        
        .activity-card {
          background: white;
          border-radius: 16px;
          padding: 28px;
          border: 1px solid rgba(0,0,0,0.04);
          box-shadow: 0 2px 8px rgba(0,0,0,0.04);
        }
        
        .activity-header {
          color: #1e293b;
          font-size: 1.25rem;
          font-weight: 600;
          margin-bottom: 24px;
          display: flex;
          align-items: center;
          gap: 8px;
        }
        
        .activity-item {
          background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
          border: 1px solid rgba(148, 163, 184, 0.1);
          border-radius: 12px;
          padding: 20px;
          margin-bottom: 12px;
          transition: all 0.3s ease;
          position: relative;
        }
        
        .activity-item:hover {
          transform: translateX(4px);
          box-shadow: 0 4px 12px rgba(0,0,0,0.08);
          border-color: rgba(148, 163, 184, 0.2);
        }
        
        .activity-icon {
          width: 40px;
          height: 40px;
          border-radius: 10px;
          background: linear-gradient(135deg, #e0e7ff 0%, #c7d2fe 100%);
          color: #4338ca;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          flex-shrink: 0;
        }
        
        .activity-content h6 {
          color: #1e293b;
          font-size: 0.95rem;
          font-weight: 600;
          margin-bottom: 4px;
        }
        
        .activity-module {
          color: #6366f1;
          font-size: 0.8rem;
          font-weight: 500;
          background: rgba(99, 102, 241, 0.1);
          padding: 2px 8px;
          border-radius: 6px;
          display: inline-block;
          margin-bottom: 6px;
        }
        
        .activity-details {
          color: #64748b;
          font-size: 0.85rem;
          margin: 0;
          font-style: italic;
        }
        
        .activity-time {
          color: #94a3b8;
          font-size: 0.8rem;
          position: absolute;
          top: 16px;
          right: 16px;
        }
        
        .no-activity {
          text-align: center;
          color: #94a3b8;
          padding: 40px;
          background: linear-gradient(135deg, #fafafa 0%, #f4f4f5 100%);
          border-radius: 12px;
          border: 2px dashed #e2e8f0;
        }
        
        .no-activity i {
          font-size: 3rem;
          margin-bottom: 16px;
          color: #cbd5e1;
        }
      `}</style>

      <div className="dashboard-container">
        <div className="welcome-header">
          <h2 className="welcome-title">Good morning, {user?.name || 'User'}! 👋</h2>
          <p className="welcome-subtitle">Here's what's happening with your organization today</p>
        </div>

        <div className="row stats-container">
          <div className="col-md-4 mb-3">
            <div className="stat-card projects">
              <div className="stat-icon projects">
                <i className="bi bi-kanban fs-4"></i>
              </div>
              <div className="stat-number">{stats.activeProjects || 0}</div>
              <p className="stat-label">Active Projects</p>
            </div>
          </div>
          <div className="col-md-4 mb-3">
            <div className="stat-card resources">
              <div className="stat-icon resources">
                <i className="bi bi-people fs-4"></i>
              </div>
              <div className="stat-number">{stats.totalResources || 0}</div>
              <p className="stat-label">Total Resources</p>
            </div>
          </div>
          <div className="col-md-4 mb-3">
            <div className="stat-card bench">
              <div className="stat-icon bench">
                <i className="bi bi-person-dash fs-4"></i>
              </div>
              <div className="stat-number">{stats.benchResources || 0}</div>
              <p className="stat-label">Bench Resources</p>
            </div>
          </div>
        </div>

        <div className="activity-card">
          <h4 className="activity-header">
            <i className="bi bi-activity text-primary"></i>
            Recent Activity
          </h4>
          {activities.length > 0 ? (
            activities.map((item) => (
              <div key={item.id} className="activity-item">
                <div className="d-flex">
                  <div className="activity-icon">
                    <i className={`bi ${getModuleIcon(item.module)}`}></i>
                  </div>
                  <div className="activity-content flex-grow-1">
                    <h6>
                      {item.performedBy} performed "{item.action}"
                    </h6>
                    <div className="activity-module">{item.module}</div>
                    {item.details && (
                      <p className="activity-details">{item.details}</p>
                    )}
                  </div>
                </div>
                <div className="activity-time">{timeSince(item.timestamp)}</div>
              </div>
            ))
          ) : (
            <div className="no-activity">
              <i className="bi bi-inbox"></i>
              <p className="mb-0">No recent activity to display</p>
              <small>Activity will appear here as team members interact with the system</small>
            </div>
          )}
        </div>
      </div>
    </>
  );
};

export default DashboardPage;