import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Sidebar = () => {
  const { user } = useAuth();


  const getIconForLink = (label) => {
    const iconMap = {
      'User Management': 'bi bi-people-fill',
      'Location Master': 'bi bi-geo-alt-fill',
      'Resource Master': 'bi bi-tools',
      'Reports': 'bi bi-bar-chart-fill',
      'Clients': 'bi bi-building-fill',
      'Shifts': 'bi bi-clock-fill',
      'SoW Upload': 'bi bi-cloud-upload-fill',
      'Projects': 'bi bi-kanban-fill',
      'Resource Allocation': 'bi bi-diagram-3-fill',
      'Fulfillment Request': 'bi bi-inbox-fill',
      'Fulfillment Tracking': 'bi bi-truck',
      'Resource Release Request': 'bi bi-arrow-return-right',
      'Lessons': 'bi bi-journal-bookmark-fill',
      'Resource Requests': 'bi bi-clipboard-check-fill',
      'Project Status Update': 'bi bi-arrow-clockwise',
      'Financial Reports': 'bi bi-currency-dollar',
      'Approvals': 'bi bi-check-circle-fill',
      'Governance': 'bi bi-shield-fill-check'
    };
    return iconMap[label] || 'bi bi-circle-fill';
  };

  const links = {
    'SUPER_ADMIN': [
      { path: '/users', label: 'User Management' },
      { path: '/masters/locations', label: 'Location Master' },
      { path: '/masters/resources', label: 'Resource Master' },
      { path: '/reports', label: 'Reports' },
      { path: '/clients', label: 'Clients' },
      { path: '/shifts', label: 'Shifts' }
    ],
    'RMT': [
      { path: '/sow', label: 'SoW Upload' },
      { path: '/projects', label: 'Projects' },
      { path: '/allocate', label: 'Resource Allocation' },
      { path: '/request-fulfillments', label: 'Fulfillment Request' },
      { path: '/fulfillments', label: 'Fulfillment Tracking' },
      { path: '/resource-release', label: 'Resource Release Request' },
      { path: '/lessons', label: 'Lessons' }
    ],
    'PROJECT_MANAGER': [
      { path: '/release-requests', label: 'Resource Requests' },
      { path: '/status-update', label: 'Project Status Update' },
    ],
    'Finance Controllers': [
      { path: '/financial-reports', label: 'Financial Reports' }
    ],
    'Practice Heads': [
      { path: '/approvals', label: 'Approvals' },
      { path: '/governance', label: 'Governance' }
    ]
  };

  return (
    <aside 
      className="border-end shadow-lg d-flex flex-column"
      style={{ 
        width: '280px', 
        minHeight: '100vh',
        background: 'linear-gradient(180deg, #667eea 0%, #764ba2 25%, #4ECDC4 50%, #45B7D1 75%, #96CEB4 100%)',
        backdropFilter: 'blur(10px)',
        borderRight: '1px solid rgba(255,255,255,0.1)'
      }}
    >
      
      <div 
        className="p-4 border-bottom"
        style={{ 
          borderBottom: '1px solid rgba(255,255,255,0.15)',
          background: 'rgba(255,255,255,0.05)'
        }}
      >
        <div className="d-flex align-items-center">
          <div 
            className="d-flex align-items-center justify-content-center me-3 rounded-3"
            style={{
              background: 'linear-gradient(135deg, rgba(255,255,255,0.2), rgba(255,255,255,0.1))',
              width: '42px',
              height: '42px',
              backdropFilter: 'blur(10px)',
              border: '1px solid rgba(255,255,255,0.2)'
            }}
          >
            <i className="bi bi-list-ul text-white fs-5"></i>
          </div>
          <div>
            <h6 className="text-white fw-bold mb-0" style={{ fontSize: '16px' }}>
              Quick Access
            </h6>
            <small className="text-white-50" style={{ fontSize: '12px' }}>
              {user?.role?.replace('_', ' ') || 'Dashboard'}
            </small>
          </div>
        </div>
      </div>

      <div className="flex-grow-1 p-3">
        <ul className="list-unstyled mb-0">
          {(links[user?.role] || []).map((link, index) => (
            <li key={index} className="mb-2">
              <Link
                to={link.path}
                className="sidebar-link d-flex align-items-center px-4 py-3 rounded-3 text-decoration-none position-relative overflow-hidden"
                style={{ 
                  transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
                  color: 'white',
                  background: 'rgba(255,255,255,0.08)',
                  backdropFilter: 'blur(10px)',
                  border: '1px solid rgba(255,255,255,0.1)',
                  fontSize: '14px',
                  fontWeight: '500'
                }}
              >
                <div 
                  className="d-flex align-items-center justify-content-center me-3 rounded-2"
                  style={{
                    width: '36px',
                    height: '36px',
                    background: 'linear-gradient(135deg, rgba(255,255,255,0.15), rgba(255,255,255,0.05))',
                    backdropFilter: 'blur(10px)',
                    border: '1px solid rgba(255,255,255,0.1)'
                  }}
                >
                  <i className={`${getIconForLink(link.label)} text-white`} style={{ fontSize: '16px' }}></i>
                </div>
                
                
                <span className="flex-grow-1">{link.label}</span>
              
                <i 
                  className="bi bi-chevron-right text-white-50 sidebar-arrow" 
                  style={{ 
                    fontSize: '12px',
                    transition: 'transform 0.3s ease'
                  }}
                ></i>

                <div 
                  className="position-absolute top-0 start-0 w-100 h-100 sidebar-overlay"
                  style={{
                    background: 'linear-gradient(135deg, rgba(255,255,255,0.15), rgba(255,255,255,0.05))',
                    opacity: '0',
                    transition: 'opacity 0.3s ease',
                    borderRadius: 'inherit',
                    zIndex: '-1'
                  }}
                ></div>
              </Link>
            </li>
          ))}
        </ul>
      </div>

      <div 
        className="p-4 border-top"
        style={{ 
          borderTop: '1px solid rgba(255,255,255,0.15)',
          background: 'rgba(255,255,255,0.05)'
        }}
      >
        <div 
          className="d-flex align-items-center justify-content-center p-3 rounded-3"
          style={{
            background: 'rgba(255,255,255,0.1)',
            backdropFilter: 'blur(10px)',
            border: '1px solid rgba(255,255,255,0.15)'
          }}
        >
          <i className="bi bi-emoji-smile-fill text-warning me-2"></i>
          <small className="text-white fw-semibold">
            Welcome, {user?.username}!
          </small>
        </div>
      </div>

      <style>{`
        .sidebar-link:hover {
          transform: translateX(5px) scale(1.02);
          background: rgba(255,255,255,0.15) !important;
          box-shadow: 0 8px 25px rgba(0,0,0,0.15);
          border-color: rgba(255,255,255,0.2) !important;
        }
        
        .sidebar-link:hover .sidebar-overlay {
          opacity: 1;
        }
        
        .sidebar-link:hover .sidebar-arrow {
          transform: translateX(3px);
          color: rgba(255,255,255,0.9) !important;
        }
        
        .sidebar-link:active {
          transform: translateX(3px) scale(1.01);
        }
        
        aside {
          scrollbar-width: thin;
          scrollbar-color: rgba(255,255,255,0.3) transparent;
        }
        
        aside::-webkit-scrollbar {
          width: 4px;
        }
        
        aside::-webkit-scrollbar-track {
          background: transparent;
        }
        
        aside::-webkit-scrollbar-thumb {
          background: rgba(255,255,255,0.3);
          border-radius: 10px;
        }
        
        aside::-webkit-scrollbar-thumb:hover {
          background: rgba(255,255,255,0.5);
        }
        
        
        @media (max-width: 768px) {
          aside {
            width: 100% !important;
            position: fixed;
            top: 0;
            left: -100%;
            z-index: 1000;
            transition: left 0.3s ease;
          }
          
          aside.show {
            left: 0;
          }
        }
      `}</style>
    </aside>
  );
};

export default Sidebar;