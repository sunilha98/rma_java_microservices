import React, { useEffect, useState } from 'react';
import Layout from '../components/Layout';
import Table from '../components/Table';
import CustomModal from '../components/CustomModal';
import api from '../services/api';
import { formatDate } from '../utils/dateUtils';

const UserManagementPage = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    role: ''
  });
  const [editingId, setEditingId] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterRole, setFilterRole] = useState('');

  const roles = [
    'SUPER_ADMIN',
    'RMT',
    'PROJECT_MANAGER',
    'Finance Controllers',
    'Practice Heads'
  ];

  const getRoleIcon = (role) => {
    const iconMap = {
      'SUPER_ADMIN': 'bi bi-shield-fill-check',
      'RMT': 'bi bi-tools',
      'PROJECT_MANAGER': 'bi bi-kanban-fill',
      'Finance Controllers': 'bi bi-currency-dollar',
      'Practice Heads': 'bi bi-people-fill'
    };
    return iconMap[role] || 'bi bi-person-circle';
  };

  const safeDateFormat = (dateString) => {
    try {
      if (!dateString) return 'N/A';
      return formatDate(dateString);
    } catch (error) {
      console.warn('Date formatting error:', error);
      return dateString || 'N/A';
    }
  };

  const fetchUsers = async () => {
    try {
      setLoading(true);
      setError(null);
      
      console.log('Fetching users...');
      const res = await api.get('/users');
      console.log('Raw API response:', res);
      
      if (!res || !res.data) {
        throw new Error('Invalid API response structure');
      }

      
      const userData = Array.isArray(res.data) ? res.data : [];
      console.log('User data:', userData);

      const formatted = userData.map((user, index) => {
        console.log(`Processing user ${index}:`, user);
        
        return {
          id: user.id || index,
          username: user.username || 'Unknown',
          email: user.email || 'No email',
          firstName: user.firstName || '',
          lastName: user.lastName || '',
          role: user.role || 'No role',
          createdAt: safeDateFormat(user.createdAt),
          updatedAt: safeDateFormat(user.updatedAt),
          createdBy: user.createdBy || 'System',
          updatedBy: user.updatedBy || 'System',
          ...user
        };
      });

      console.log('Formatted users:', formatted);
      setUsers(formatted);
    } catch (err) {
      console.error('Failed to fetch users:', err);
      console.error('Error response:', err.response?.data);
      console.error('Error status:', err.response?.status);
      
      setError(err.message || 'Failed to fetch users');
      setUsers([]); 
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleAdd = () => {
    setFormData({ 
      username: '', 
      email: '', 
      password: '', 
      firstName: '', 
      lastName: '', 
      role: '' 
    });
    setEditingId(null);
    setModalOpen(true);
  };

  const handleEdit = (user) => {
    setFormData({
      username: user.username || '',
      email: user.email || '',
      password: '',
      firstName: user.firstName || '',
      lastName: user.lastName || '',
      role: user.role || ''
    });
    setEditingId(user.id);
    setModalOpen(true);
  };

  const handleDelete = async (user) => {
    if (window.confirm(`Delete user "${user.username}"?`)) {
      try {
        await api.delete(`/users/${user.id}`);
        fetchUsers();
      } catch (err) {
        console.error('Failed to delete user:', err);
        alert('Failed to delete user. Please try again.');
      }
    }
  };

  const handleSave = async () => {
    try {
      if (editingId) {
        await api.put(`/users/${editingId}`, formData);
      } else {
        await api.post('/users', formData);
      }
      setModalOpen(false);
      fetchUsers();
    } catch (err) {
      console.error('Failed to save user:', err);
      alert('Failed to save user. Please try again.');
    }
  };

  const filteredUsers = users.filter(user => {
    if (!user) return false;
    
    const searchFields = [
      user.username,
      user.email,
      user.firstName,
      user.lastName
    ].filter(Boolean); 
    
    const matchesSearch = !searchTerm || searchFields.some(field => 
      field.toLowerCase().includes(searchTerm.toLowerCase())
    );
    
    const matchesRole = !filterRole || user.role === filterRole;
    return matchesSearch && matchesRole;
  });

  if (error && !loading) {
    return (
      <div className="d-flex justify-content-center align-items-center py-5">
        <div className="text-center">
          <i className="bi bi-exclamation-triangle display-1 text-warning mb-3"></i>
          <h4 className="mb-3">Error Loading Users</h4>
          <p className="text-muted mb-3">{error}</p>
          <button 
            className="btn btn-primary" 
            onClick={fetchUsers}
          >
            <i className="bi bi-arrow-clockwise me-2"></i>
            Try Again
          </button>
        </div>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center py-5">
        <div className="text-center">
          <div className="spinner-border text-primary mb-3" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="text-muted">Loading users...</p>
        </div>
      </div>
    );
  }

  const columns = [
    { 
      header: 'User', 
      accessor: 'username',
      render: (user) => (
        <div className="d-flex align-items-center">
          <div 
            className="d-flex align-items-center justify-content-center me-2 rounded-circle"
            style={{
              width: '32px',
              height: '32px',
              background: 'linear-gradient(135deg, rgba(102, 126, 234, 0.2), rgba(118, 75, 162, 0.2))',
              border: '2px solid rgba(102, 126, 234, 0.3)'
            }}
          >
            <i className={`${getRoleIcon(user.role)} text-primary`} style={{ fontSize: '14px' }}></i>
          </div>
          <div>
            <div className="fw-semibold text-dark" style={{ fontSize: '13px' }}>{user.username || 'Unknown'}</div>
            <small className="text-muted" style={{ fontSize: '11px' }}>{user.email || 'No email'}</small>
          </div>
        </div>
      )
    },
    { 
      header: 'Role', 
      accessor: 'role',
      render: (user) => (
        <span 
          className="badge rounded-pill px-2 py-1 d-inline-flex align-items-center"
          style={{
            background: 'linear-gradient(135deg, rgba(102, 126, 234, 0.1), rgba(118, 75, 162, 0.1))',
            color: '#667eea',
            border: '1px solid rgba(102, 126, 234, 0.2)',
            fontSize: '10px',
            fontWeight: '500'
          }}
        >
          <i className={`${getRoleIcon(user.role)} me-1`} style={{ fontSize: '8px' }}></i>
          {user.role?.replace('_', ' ') || 'No role'}
        </span>
      )
    },
    { 
      header: 'Name', 
      accessor: 'firstName',
      render: (user) => (
        <span style={{ fontSize: '13px' }}>
          {user.firstName || user.lastName ? `${user.firstName || ''} ${user.lastName || ''}`.trim() : '-'}
        </span>
      )
    },
    { 
      header: 'Created', 
      accessor: 'createdAt',
      render: (user) => (
        <div style={{ fontSize: '12px' }}>
          <div className="fw-normal text-dark">{user.createdAt || 'Unknown'}</div>
          <small className="text-muted" style={{ fontSize: '10px' }}>by {user.createdBy || 'System'}</small>
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

  return (
    <>
      <style>{`
        .gradient-header {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 25%, #4ECDC4 50%, #45B7D1 75%, #96CEB4 100%);
          backdrop-filter: blur(10px);
        }
        
        .glass-card {
          background: rgba(255, 255, 255, 0.95);
          backdrop-filter: blur(10px);
          border: 1px solid rgba(255, 255, 255, 0.2);
          box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
        }
        
        .gradient-btn {
          background: linear-gradient(135deg, #667eea, #764ba2);
          border: none;
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        }
        
        .gradient-btn:hover {
          transform: translateY(-2px);
          box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
          background: linear-gradient(135deg, #764ba2, #667eea);
        }
        
        .search-input, .filter-select {
          background: rgba(255, 255, 255, 0.9);
          border: 1px solid rgba(102, 126, 234, 0.2);
          backdrop-filter: blur(10px);
          transition: all 0.3s ease;
        }
        
        .search-input:focus, .filter-select:focus {
          border-color: #667eea;
          box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.15);
          background: rgba(255, 255, 255, 1);
        }
        
        .stats-card {
          background: linear-gradient(135deg, rgba(102, 126, 234, 0.1), rgba(118, 75, 162, 0.1));
          border: 1px solid rgba(102, 126, 234, 0.2);
          backdrop-filter: blur(10px);
          transition: transform 0.3s ease;
        }
        
        .stats-card:hover {
          transform: translateY(-2px);
        }
        
        .modal-glass {
          background: rgba(255, 255, 255, 0.95) !important;
          backdrop-filter: blur(20px) !important;
          border: 1px solid rgba(255, 255, 255, 0.2) !important;
        }
        
        .form-control-glass {
          background: rgba(255, 255, 255, 0.8);
          border: 1px solid rgba(102, 126, 234, 0.2);
          backdrop-filter: blur(10px);
        }
        
        .form-control-glass:focus {
          background: rgba(255, 255, 255, 0.95);
          border-color: #667eea;
          box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.15);
        }
      `}</style>

      <div 
        className="gradient-header p-4 mb-4 rounded-4 text-white position-relative overflow-hidden"
        style={{ marginTop: '-20px', marginLeft: '-20px', marginRight: '-20px' }}
      >
        <div className="position-absolute top-0 start-0 w-100 h-100" style={{
          background: 'radial-gradient(circle at 20% 50%, rgba(255,255,255,0.1) 0%, transparent 50%)',
        }}></div>
        
        <div className="position-relative">
          <div className="d-flex justify-content-between align-items-center">
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
                <h3 className="mb-1 fw-bold">User Management</h3>
                <p className="mb-0 opacity-75">Manage system users and their roles</p>
              </div>
            </div>
            
            <button 
              className="btn btn-light gradient-btn text-white px-4 py-2 rounded-3 d-flex align-items-center"
              onClick={handleAdd}
            >
              <i className="bi bi-person-plus-fill me-2"></i>
              Add User
            </button>
          </div>
        </div>
      </div>

      <div className="row mb-4">
        <div className="col-md-3">
          <div className="stats-card p-3 rounded-3 text-center">
            <i className="bi bi-people text-primary fs-2 mb-2"></i>
            <h4 className="fw-bold mb-1 text-primary">{users.length}</h4>
            <small className="text-muted">Total Users</small>
          </div>
        </div>
        <div className="col-md-3">
          <div className="stats-card p-3 rounded-3 text-center">
            <i className="bi bi-shield-check text-success fs-2 mb-2"></i>
            <h4 className="fw-bold mb-1 text-success">
              {users.filter(u => u.role === 'SUPER_ADMIN').length}
            </h4>
            <small className="text-muted">Super Admins</small>
          </div>
        </div>
        <div className="col-md-3">
          <div className="stats-card p-3 rounded-3 text-center">
            <i className="bi bi-kanban text-info fs-2 mb-2"></i>
            <h4 className="fw-bold mb-1 text-info">
              {users.filter(u => u.role === 'PROJECT_MANAGER').length}
            </h4>
            <small className="text-muted">Project Managers</small>
          </div>
        </div>
        <div className="col-md-3">
          <div className="stats-card p-3 rounded-3 text-center">
            <i className="bi bi-tools text-warning fs-2 mb-2"></i>
            <h4 className="fw-bold mb-1 text-warning">
              {users.filter(u => u.role === 'RMT').length}
            </h4>
            <small className="text-muted">RMT Users</small>
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
                placeholder="Search users by name, username, or email..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
          </div>
          <div className="col-md-4">
            <select
              className="form-select filter-select rounded-3"
              value={filterRole}
              onChange={(e) => setFilterRole(e.target.value)}
            >
              <option value="">All Roles</option>
              {roles.map((role, idx) => (
                <option key={idx} value={role}>{role.replace('_', ' ')}</option>
              ))}
            </select>
          </div>
          <div className="col-md-2">
            <div className="text-muted small">
              Showing {filteredUsers.length} of {users.length} users
            </div>
          </div>
        </div>
      </div>

      <div className="glass-card rounded-3 overflow-hidden">
        <div className="table-responsive" style={{ overflowX: 'visible' }}>
          <table className="table table-hover mb-0" style={{ minWidth: 'auto' }}>
            <thead style={{ background: 'linear-gradient(135deg, #667eea, #764ba2)' }}>
              <tr>
                {columns.map((column, index) => (
                  <th key={index} className="border-0 text-white fw-semibold py-3 px-2" style={{ whiteSpace: 'nowrap' }}>
                    <div className="d-flex align-items-center">
                      <span style={{ fontSize: '14px' }}>{column.header}</span>
                      {column.sortable && (
                        <i className="bi bi-chevron-expand ms-2 opacity-75" style={{ fontSize: '12px' }}></i>
                      )}
                    </div>
                  </th>
                ))}
                <th className="border-0 text-white fw-semibold py-3 px-2 text-center" style={{ width: '120px' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredUsers.length === 0 ? (
                <tr>
                  <td colSpan={columns.length + 1} className="text-center py-5 text-muted">
                    <div className="d-flex flex-column align-items-center">
                      <i className="bi bi-inbox display-1 mb-3 opacity-25"></i>
                      <h5 className="mb-2">No users found</h5>
                      <p className="mb-0">Try adjusting your search or filter criteria</p>
                    </div>
                  </td>
                </tr>
              ) : (
                filteredUsers.map((user, index) => (
                  <tr key={user.id || index} style={{ backgroundColor: 'rgba(255,255,255,0.8)' }}>
                    {columns.map((column, colIndex) => (
                      <td key={colIndex} className="border-0 py-2 px-2 align-middle" style={{ fontSize: '13px' }}>
                        {column.render ? column.render(user) : (
                          <span className="text-dark fw-normal">
                            {user[column.accessor] || '-'}
                          </span>
                        )}
                      </td>
                    ))}
                    <td className="border-0 py-2 px-2 align-middle text-center">
                      <div className="btn-group" role="group">
                        {actions.map((action, actionIndex) => (
                          <button
                            key={actionIndex}
                            type="button"
                            className={`btn btn-sm ${action.className || 'btn-outline-primary'}`}
                            onClick={() => action.onClick(user)}
                            title={action.label}
                            style={{ fontSize: '11px', padding: '4px 8px' }}
                          >
                            <i className={action.icon}></i>
                            <span className="d-none d-lg-inline ms-1">{action.label}</span>
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

        {filteredUsers.length > 0 && (
          <div 
            className="px-4 py-3 border-top d-flex justify-content-between align-items-center"
            style={{ 
              background: 'rgba(102, 126, 234, 0.05)',
              borderTop: '1px solid rgba(102, 126, 234, 0.1)' 
            }}
          >
            <div className="d-flex align-items-center text-muted small">
              <i className="bi bi-info-circle me-2"></i>
              Showing {filteredUsers.length} of {users.length} users
            </div>
            <div className="d-flex align-items-center">
              <span className="text-muted small me-3">
                {searchTerm || filterRole ? 'Filtered results' : 'All users displayed'}
              </span>
              {(searchTerm || filterRole) && (
                <button 
                  className="btn btn-sm btn-outline-secondary rounded-pill"
                  onClick={() => { setSearchTerm(''); setFilterRole(''); }}
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
                background: 'linear-gradient(135deg, #667eea, #764ba2)',
                width: '40px',
                height: '40px'
              }}
            >
              <i className={`bi ${editingId ? 'bi-pencil-square' : 'bi-person-plus'} text-white`}></i>
            </div>
            <span>{editingId ? 'Edit User' : 'Add New User'}</span>
          </div>
        }
        onSave={handleSave}
        className="modal-glass"
      >
        <div className="row">
          <div className="col-md-6 mb-3">
            <label className="form-label fw-semibold">
              <i className="bi bi-person me-1"></i>Username
            </label>
            <input
              type="text"
              className="form-control form-control-glass rounded-3"
              value={formData.username}
              onChange={(e) => setFormData({ ...formData, username: e.target.value })}
              placeholder="Enter username"
            />
          </div>
          <div className="col-md-6 mb-3">
            <label className="form-label fw-semibold">
              <i className="bi bi-envelope me-1"></i>Email
            </label>
            <input
              type="email"
              className="form-control form-control-glass rounded-3"
              value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              placeholder="Enter email address"
            />
          </div>
        </div>
        
        <div className="mb-3">
          <label className="form-label fw-semibold">
            <i className="bi bi-lock me-1"></i>Password
          </label>
          <input
            type="password"
            className="form-control form-control-glass rounded-3"
            value={formData.password}
            onChange={(e) => setFormData({ ...formData, password: e.target.value })}
            autoComplete="new-password"
            placeholder={editingId ? "Leave empty to keep current password" : "Enter password"}
          />
        </div>
        
        <div className="row">
          <div className="col-md-6 mb-3">
            <label className="form-label fw-semibold">
              <i className="bi bi-person-badge me-1"></i>First Name
            </label>
            <input
              type="text"
              className="form-control form-control-glass rounded-3"
              value={formData.firstName}
              onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
              placeholder="Enter first name"
            />
          </div>
          <div className="col-md-6 mb-3">
            <label className="form-label fw-semibold">
              <i className="bi bi-person-badge-fill me-1"></i>Last Name
            </label>
            <input
              type="text"
              className="form-control form-control-glass rounded-3"
              value={formData.lastName}
              onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
              placeholder="Enter last name"
            />
          </div>
        </div>
        
        <div className="mb-3">
          <label className="form-label fw-semibold">
            <i className="bi bi-shield-check me-1"></i>Role
          </label>
          <select
            className="form-select form-control-glass rounded-3"
            value={formData.role}
            onChange={(e) => setFormData({ ...formData, role: e.target.value })}
          >
            <option value="">Select Role</option>
            {roles.map((role, idx) => (
              <option key={idx} value={role}>
                {role.replace('_', ' ')}
              </option>
            ))}
          </select>
        </div>
      </CustomModal>
    </>
  );
};

export default UserManagementPage;