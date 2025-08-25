import React, { useEffect, useState } from 'react';
import Layout from '../components/Layout';
import Table from '../components/Table';
import CustomModal from '../components/CustomModal';
import api from '../services/api';
import { formatDate } from '../utils/dateUtils';

const LocationMasterPage = () => {
  const [locations, setLocations] = useState([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [formData, setFormData] = useState({ name: '', description: '', address: '', isActive: true });
  const [editingId, setEditingId] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatus, setFilterStatus] = useState('');

  const fetchLocations = async () => {
    try {
      const res = await api.get('/locations');
      const formatted = res.data.map(loc => ({
        ...loc,
        createdAt: formatDate(loc.createdAt),
        isActiveDisplay: loc.isActive ? 'Active' : 'Inactive',
        isActive: loc.isActive
      }));
      setLocations(formatted);
    } catch (err) {
      console.error('Failed to fetch locations:', err);
    }
  };

  useEffect(() => {
    fetchLocations();
  }, []);

  const handleAdd = () => {
    setFormData({ name: '', description: '', address: '', isActive: true });
    setEditingId(null);
    setModalOpen(true);
  };

  const handleEdit = (location) => {
    setFormData({
      name: location.name,
      description: location.description || '',
      address: location.address || '',
      isActive: location.isActive
    });
    setEditingId(location.id);
    setModalOpen(true);
  };

  const handleDelete = async (location) => {
    if (window.confirm(`Delete location "${location.name}"?`)) {
      await api.delete(`/locations/${location.id}`);
      fetchLocations();
    }
  };

  const handleSave = async () => {
    try {
      if (editingId) {
        await api.put(`/locations/${editingId}`, formData);
      } else {
        await api.post('/locations', formData);
      }
      setModalOpen(false);
      fetchLocations();
    } catch (err) {
      console.error('Failed to save location:', err);
    }
  };

  
  const filteredLocations = locations.filter(location => {
    const matchesSearch = location.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         location.address?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         location.createdBy?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = !filterStatus || 
                         (filterStatus === 'active' && location.isActive) ||
                         (filterStatus === 'inactive' && !location.isActive);
    return matchesSearch && matchesStatus;
  });

  const getLocationIcon = () => 'bi bi-geo-alt-fill';

  const columns = [
    { 
      header: 'Location', 
      accessor: 'name',
      render: (location) => (
        <div className="d-flex align-items-center">
          <div 
            className="d-flex align-items-center justify-content-center me-3 rounded-circle"
            style={{
              width: '42px',
              height: '42px',
              background: 'linear-gradient(135deg, rgba(78, 205, 196, 0.2), rgba(69, 183, 209, 0.2))',
              border: '2px solid rgba(78, 205, 196, 0.3)'
            }}
          >
            <i className="bi bi-geo-alt-fill text-info" style={{ fontSize: '18px' }}></i>
          </div>
          <div>
            <div className="fw-semibold text-dark">{location.name}</div>
            {location.address && (
              <small className="text-muted">
                <i className="bi bi-map me-1"></i>
                {location.address}
              </small>
            )}
          </div>
        </div>
      )
    },
    { 
      header: 'Status', 
      accessor: 'isActive',
      render: (location) => (
        <span 
          className={`badge rounded-pill px-3 py-2 d-inline-flex align-items-center ${
            location.isActive ? 'status-active' : 'status-inactive'
          }`}
          style={{
            fontSize: '12px',
            fontWeight: '500'
          }}
        >
          <i 
            className={`bi ${location.isActive ? 'bi-check-circle-fill' : 'bi-x-circle-fill'} me-1`} 
            style={{ fontSize: '10px' }}
          ></i>
          {location.isActive ? 'Active' : 'Inactive'}
        </span>
      )
    },
    { 
      header: 'Created', 
      accessor: 'createdAt',
      render: (location) => (
        <div>
          <div className="fw-normal text-dark">{location.createdAt}</div>
          <small className="text-muted">by {location.createdBy}</small>
        </div>
      )
    }
  ];

  const actions = [
    { 
      label: 'Edit', 
      onClick: handleEdit,
      icon: 'bi bi-pencil-square',
      className: 'btn-outline-primary'
    },
    { 
      label: 'Delete', 
      onClick: handleDelete,
      icon: 'bi bi-trash3',
      className: 'btn-outline-danger'
    }
  ];

  const activeLocations = locations.filter(loc => loc.isActive).length;
  const inactiveLocations = locations.length - activeLocations;

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
        
        .gradient-btn {
          background: linear-gradient(135deg, #4ECDC4, #45B7D1);
          border: none;
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        }
        
        .gradient-btn:hover {
          transform: translateY(-2px);
          box-shadow: 0 8px 25px rgba(78, 205, 196, 0.3);
          background: linear-gradient(135deg, #45B7D1, #4ECDC4);
        }
        
        .search-input, .filter-select {
          background: rgba(255, 255, 255, 0.9);
          border: 1px solid rgba(78, 205, 196, 0.2);
          backdrop-filter: blur(10px);
          transition: all 0.3s ease;
        }
        
        .search-input:focus, .filter-select:focus {
          border-color: #4ECDC4;
          box-shadow: 0 0 0 0.2rem rgba(78, 205, 196, 0.15);
          background: rgba(255, 255, 255, 1);
        }
        
        .stats-card {
          background: linear-gradient(135deg, rgba(78, 205, 196, 0.1), rgba(69, 183, 209, 0.1));
          border: 1px solid rgba(78, 205, 196, 0.2);
          backdrop-filter: blur(10px);
          transition: transform 0.3s ease;
        }
        
        .stats-card:hover {
          transform: translateY(-2px);
        }

        .status-active {
          background: linear-gradient(135deg, rgba(40, 167, 69, 0.1), rgba(25, 135, 84, 0.1));
          color: #198754;
          border: 1px solid rgba(40, 167, 69, 0.2);
        }

        .status-inactive {
          background: linear-gradient(135deg, rgba(220, 53, 69, 0.1), rgba(214, 51, 108, 0.1));
          color: #dc3545;
          border: 1px solid rgba(220, 53, 69, 0.2);
        }
        
        .form-control-glass {
          background: rgba(255, 255, 255, 0.8);
          border: 1px solid rgba(78, 205, 196, 0.2);
          backdrop-filter: blur(10px);
        }
        
        .form-control-glass:focus {
          background: rgba(255, 255, 255, 0.95);
          border-color: #4ECDC4;
          box-shadow: 0 0 0 0.2rem rgba(78, 205, 196, 0.15);
        }

        /* Enhanced Table Styles */
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
          position: relative;
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

        .action-btn {
          transition: all 0.3s ease;
          border-radius: 6px !important;
          font-size: 12px;
          padding: 6px 12px;
          margin: 0 2px;
        }

        .action-btn:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        .btn-outline-primary.action-btn {
          border-color: rgba(78, 205, 196, 0.3);
          color: #4ECDC4;
          background: rgba(78, 205, 196, 0.05);
        }

        .btn-outline-primary.action-btn:hover {
          background: #4ECDC4;
          border-color: #4ECDC4;
          color: white;
        }

        .btn-outline-danger.action-btn {
          border-color: rgba(220, 53, 69, 0.3);
          color: #dc3545;
          background: rgba(220, 53, 69, 0.05);
        }

        .btn-outline-danger.action-btn:hover {
          background: #dc3545;
          border-color: #dc3545;
          color: white;
        }

        
        .location-pulse {
          animation: locationPulse 2s infinite;
        }

        @keyframes locationPulse {
          0% { transform: scale(1); }
          50% { transform: scale(1.05); }
          100% { transform: scale(1); }
        }

        
        @media (max-width: 768px) {
          .table-row-custom:hover {
            transform: none;
          }
          
          .action-btn span {
            display: none !important;
          }
          
          .action-btn {
            padding: 8px;
            margin: 0 1px;
          }
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
          <div className="d-flex justify-content-between align-items-center">
            <div className="d-flex align-items-center">
              <div 
                className="d-flex align-items-center justify-content-center me-3 rounded-3 location-pulse"
                style={{
                  background: 'rgba(255,255,255,0.15)',
                  width: '50px',
                  height: '50px',
                  backdropFilter: 'blur(10px)',
                  border: '1px solid rgba(255,255,255,0.2)'
                }}
              >
                <i className="bi bi-geo-alt-fill fs-4"></i>
              </div>
              <div>
                <h3 className="mb-1 fw-bold">Location Master</h3>
                <p className="mb-0 opacity-75">Manage your office locations and branches</p>
              </div>
            </div>
            
            <button 
              className="btn btn-light gradient-btn text-white px-4 py-2 rounded-3 d-flex align-items-center"
              onClick={handleAdd}
            >
              <i className="bi bi-plus-circle-fill me-2"></i>
              Add Location
            </button>
          </div>
        </div>
      </div>

      
      <div className="row mb-4">
        <div className="col-md-3">
          <div className="stats-card p-3 rounded-3 text-center">
            <i className="bi bi-buildings text-info fs-2 mb-2"></i>
            <h4 className="fw-bold mb-1 text-info">{locations.length}</h4>
            <small className="text-muted">Total Locations</small>
          </div>
        </div>
        <div className="col-md-3">
          <div className="stats-card p-3 rounded-3 text-center">
            <i className="bi bi-check-circle-fill text-success fs-2 mb-2"></i>
            <h4 className="fw-bold mb-1 text-success">{activeLocations}</h4>
            <small className="text-muted">Active Locations</small>
          </div>
        </div>
        <div className="col-md-3">
          <div className="stats-card p-3 rounded-3 text-center">
            <i className="bi bi-x-circle-fill text-danger fs-2 mb-2"></i>
            <h4 className="fw-bold mb-1 text-danger">{inactiveLocations}</h4>
            <small className="text-muted">Inactive Locations</small>
          </div>
        </div>
        <div className="col-md-3">
          <div className="stats-card p-3 rounded-3 text-center">
            <i className="bi bi-globe-americas text-warning fs-2 mb-2"></i>
            <h4 className="fw-bold mb-1 text-warning">
              {Math.round((activeLocations / locations.length) * 100) || 0}%
            </h4>
            <small className="text-muted">Active Rate</small>
          </div>
        </div>
      </div>

      
      <div className="glass-card p-3 rounded-3 mb-4">
        <div className="row align-items-center">
          <div className="col-md-6">
            <div className="position-relative">
              <i className="bi bi-search position-absolute top-50 start-0 translate-middle-y ms-3 text-muted"></i>
              <input
                type="text"
                className="form-control search-input ps-5 rounded-3"
                placeholder="Search locations by name, address, or creator..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
          </div>
          <div className="col-md-4">
            <select
              className="form-select filter-select rounded-3"
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
            >
              <option value="">All Status</option>
              <option value="active">Active Only</option>
              <option value="inactive">Inactive Only</option>
            </select>
          </div>
          <div className="col-md-2">
            <div className="text-muted small">
              Showing {filteredLocations.length} of {locations.length}
            </div>
          </div>
        </div>
      </div>

      
      <div className="glass-card rounded-3 overflow-hidden">
        <div className="table-responsive">
          <table className="table table-hover mb-0 custom-table">
            <thead>
              <tr className="gradient-header-table">
                {columns.map((column, index) => (
                  <th key={index} className="border-0 text-white fw-semibold py-3 px-4">
                    <div className="d-flex align-items-center">
                      <span>{column.header}</span>
                    </div>
                  </th>
                ))}
                <th className="border-0 text-white fw-semibold py-3 px-4 text-center">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredLocations.length === 0 ? (
                <tr>
                  <td colSpan={columns.length + 1} className="text-center py-5 text-muted">
                    <div className="d-flex flex-column align-items-center">
                      <i className="bi bi-geo display-1 mb-3 opacity-25"></i>
                      <h5 className="mb-2">No locations found</h5>
                      <p className="mb-0">Try adjusting your search criteria or add a new location</p>
                    </div>
                  </td>
                </tr>
              ) : (
                filteredLocations.map((location, index) => (
                  <tr key={location.id || index} className="table-row-custom">
                    {columns.map((column, colIndex) => (
                      <td key={colIndex} className="border-0 py-3 px-4 align-middle">
                        {column.render ? column.render(location) : (
                          <span className="text-dark fw-normal">
                            {location[column.accessor] || '-'}
                          </span>
                        )}
                      </td>
                    ))}
                    <td className="border-0 py-3 px-4 align-middle text-center">
                      <div className="btn-group" role="group">
                        {actions.map((action, actionIndex) => (
                          <button
                            key={actionIndex}
                            type="button"
                            className={`btn btn-sm ${action.className || 'btn-outline-primary'} action-btn d-flex align-items-center`}
                            onClick={() => action.onClick(location)}
                            title={action.label}
                          >
                            {action.icon && <i className={`${action.icon} me-1`}></i>}
                            <span className="d-none d-md-inline">{action.label}</span>
                          </button>
                        ))}
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        
        {filteredLocations.length > 0 && (
          <div 
            className="px-4 py-3 border-top d-flex justify-content-between align-items-center"
            style={{ 
              background: 'rgba(78, 205, 196, 0.05)',
              borderTop: '1px solid rgba(78, 205, 196, 0.1)' 
            }}
          >
            <div className="d-flex align-items-center text-muted small">
              <i className="bi bi-info-circle me-2"></i>
              Showing {filteredLocations.length} of {locations.length} locations
            </div>
            <div className="d-flex align-items-center">
              <span className="text-muted small me-3">
                {searchTerm || filterStatus ? 'Filtered results' : 'All locations displayed'}
              </span>
              {(searchTerm || filterStatus) && (
                <button 
                  className="btn btn-sm btn-outline-secondary rounded-pill"
                  onClick={() => { setSearchTerm(''); setFilterStatus(''); }}
                >
                  <i className="bi bi-x-lg me-1"></i>Clear filters
                </button>
              )}
            </div>
          </div>
        )}
      </div>

      
      <CustomModal
        show={modalOpen}
        onHide={() => setModalOpen(false)}
        title={
          <div className="d-flex align-items-center">
            <div 
              className="d-flex align-items-center justify-content-center me-3 rounded-2"
              style={{
                background: 'linear-gradient(135deg, #4ECDC4, #45B7D1)',
                width: '40px',
                height: '40px'
              }}
            >
              <i className={`bi ${editingId ? 'bi-pencil-square' : 'bi-geo-alt-fill'} text-white`}></i>
            </div>
            <span>{editingId ? 'Edit Location' : 'Add New Location'}</span>
          </div>
        }
        onSave={handleSave}
        className="modal-glass"
      >
        <div className="mb-3">
          <label className="form-label fw-semibold">
            <i className="bi bi-geo-alt me-1"></i>Location Name
          </label>
          <input
            type="text"
            className="form-control form-control-glass rounded-3"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            placeholder="Enter location name"
          />
        </div>

        <div className="mb-3">
          <label className="form-label fw-semibold">
            <i className="bi bi-card-text me-1"></i>Description
          </label>
          <textarea
            className="form-control form-control-glass rounded-3"
            rows="3"
            value={formData.description}
            onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            placeholder="Enter location description (optional)"
          />
        </div>

        <div className="mb-3">
          <label className="form-label fw-semibold">
            <i className="bi bi-map me-1"></i>Address
          </label>
          <input
            type="text"
            className="form-control form-control-glass rounded-3"
            value={formData.address}
            onChange={(e) => setFormData({ ...formData, address: e.target.value })}
            placeholder="Enter full address (optional)"
          />
        </div>

        <div className="mb-3">
          <div className="form-check">
            <input
              className="form-check-input"
              type="checkbox"
              checked={formData.isActive}
              onChange={(e) => setFormData({ ...formData, isActive: e.target.checked })}
              style={{ 
                accentColor: '#4ECDC4',
              }}
            />
            <label className="form-check-label fw-semibold">
              <i className="bi bi-check-circle me-1"></i>Active Location
            </label>
            <small className="form-text text-muted d-block">
              Inactive locations won't be available for selection in other modules
            </small>
          </div>
        </div>
      </CustomModal>
    </>
  );
};

export default LocationMasterPage;