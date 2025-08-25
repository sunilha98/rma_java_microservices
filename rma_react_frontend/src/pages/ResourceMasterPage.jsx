import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { Dropdown } from 'react-bootstrap';

const ResourceMasterPage = () => {
  const [titles, setTitles] = useState([]);
  const [locations, setLocations] = useState([]);
  const [skillsOptions, setSkillsOptions] = useState([]);
  const [resources, setResources] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterLocation, setFilterLocation] = useState('');
  const [filterTitle, setFilterTitle] = useState('');
  const [showForm, setShowForm] = useState(false);
  
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    employeeId: '',
    email: '',
    titleId: '',
    skills: [],
    experience: '',
    locationId: '',
  });

  const fetchResources = async () => {
    try {
      const res = await api.get('/resources');
      setResources(res.data || []);
    } catch (err) {
      console.error('Failed to fetch resources:', err);
    }
  };

  useEffect(() => {
    const fetchDropdownData = async () => {
      setIsLoading(true);
      try {
        const [titlesRes, skillsRes, locationsRes] = await Promise.all([
          api.get('/titles'),
          api.get('/resources/skills'),
          api.get('/locations'),
        ]);
        setTitles(titlesRes.data);
        setSkillsOptions(skillsRes.data);
        setLocations(locationsRes.data);
        await fetchResources();
      } catch (err) {
        console.error('Failed to load initial form data', err);
        alert('There was an error loading data for the form. Please try refreshing the page.');
      } finally {
        setIsLoading(false);
      }
    };

    fetchDropdownData();
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSkillChange = (skillId) => {
    const { skills } = formData;
    const newSkills = skills.includes(skillId)
      ? skills.filter((id) => id !== skillId)
      : [...skills, skillId];
    setFormData({ ...formData, skills: newSkills });
  };

  const resetForm = () => {
    setFormData({
      firstName: '',
      lastName: '',
      employeeId: '',
      email: '',
      titleId: '',
      skills: [],
      experience: '',
      locationId: ''
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.titleId || !formData.locationId) {
      alert('Please ensure Title and Location are selected.');
      return;
    }
    if (formData.skills.length === 0) {
      alert('Please select at least one skill.');
      return;
    }

    const payload = {
      ...formData,
      titleId: Number(formData.titleId),
      experience: Number(formData.experience),
      locationId: Number(formData.locationId),
      skills: formData.skills.map(id => Number(id)),
    };

    console.log('Submitting payload:', JSON.stringify(payload, null, 2));

    try {
      setIsLoading(true);
      await api.post('/resources', payload);
      alert('Resource added successfully!');
      resetForm();
      setShowForm(false);
      await fetchResources();
    } catch (err) {
      console.error('Failed to add resource:', err);
      alert('Failed to add resource. Check console for details.');
    } finally {
      setIsLoading(false);
    }
  };

  const getSkillNames = (skillIds) => {
    if (!skillIds || skillIds.length === 0) return 'No skills';
    return skillIds.map(id => {
      const skill = skillsOptions.find(s => s.id === id);
      return skill ? skill.name : 'Unknown';
    }).join(', ');
  };

  const getTitleName = (titleId) => {
    const title = titles.find(t => t.id === titleId);
    return title ? title.name : 'Unknown';
  };

  const getLocationName = (locationId) => {
    const location = locations.find(l => l.id === locationId);
    return location ? location.name : 'Unknown';
  };

  
  const filteredResources = resources.filter(resource => {
    const matchesSearch = 
      resource.firstName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      resource.lastName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      resource.employeeId?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      resource.email?.toLowerCase().includes(searchTerm.toLowerCase());
    
    const matchesLocation = !filterLocation || resource.locationId?.toString() === filterLocation;
    const matchesTitle = !filterTitle || resource.titleId?.toString() === filterTitle;
    
    return matchesSearch && matchesLocation && matchesTitle;
  });

  const experienceStats = {
    junior: resources.filter(r => r.experience <= 2).length,
    mid: resources.filter(r => r.experience > 2 && r.experience <= 5).length,
    senior: resources.filter(r => r.experience > 5).length,
    avg: resources.length > 0 ? (resources.reduce((sum, r) => sum + (r.experience || 0), 0) / resources.length).toFixed(1) : 0
  };

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

        .gradient-btn-success {
          background: linear-gradient(135deg, #28a745, #20c997);
          border: none;
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        }
        
        .gradient-btn-success:hover {
          transform: translateY(-2px);
          box-shadow: 0 8px 25px rgba(40, 167, 69, 0.3);
          background: linear-gradient(135deg, #20c997, #28a745);
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
        
        .form-control-glass {
          background: rgba(255, 255, 255, 0.8);
          border: 1px solid rgba(102, 126, 234, 0.2);
          backdrop-filter: blur(10px);
          transition: all 0.3s ease;
        }
        
        .form-control-glass:focus {
          background: rgba(255, 255, 255, 0.95);
          border-color: #667eea;
          box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.15);
        }

        .skills-badge {
          background: linear-gradient(135deg, rgba(245, 87, 108, 0.1), rgba(79, 172, 254, 0.1));
          color: #f5576c;
          border: 1px solid rgba(245, 87, 108, 0.2);
          font-size: 11px;
          padding: 4px 8px;
          margin: 2px;
          border-radius: 12px;
          display: inline-block;
        }

        .experience-badge {
          font-size: 12px;
          padding: 6px 12px;
          border-radius: 20px;
          font-weight: 600;
        }

        .experience-junior {
          background: linear-gradient(135deg, rgba(40, 167, 69, 0.1), rgba(32, 201, 151, 0.1));
          color: #28a745;
          border: 1px solid rgba(40, 167, 69, 0.2);
        }

        .experience-mid {
          background: linear-gradient(135deg, rgba(255, 193, 7, 0.1), rgba(255, 171, 0, 0.1));
          color: #ffc107;
          border: 1px solid rgba(255, 193, 7, 0.3);
        }

        .experience-senior {
          background: linear-gradient(135deg, rgba(220, 53, 69, 0.1), rgba(214, 51, 108, 0.1));
          color: #dc3545;
          border: 1px solid rgba(220, 53, 69, 0.2);
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

        .avatar-circle {
          background: linear-gradient(135deg, #667eea, #764ba2);
          color: white;
          width: 50px;
          height: 50px;
          display: flex;
          align-items: center;
          justify-content-center;
          border-radius: 50%;
          font-weight: bold;
          font-size: 18px;
        }

        .dropdown-glass .dropdown-menu {
          background: rgba(255, 255, 255, 0.95);
          backdrop-filter: blur(10px);
          border: 1px solid rgba(255, 255, 255, 0.2);
          box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
        }

        .loading-spinner {
          display: inline-block;
          width: 20px;
          height: 20px;
          border: 3px solid rgba(255,255,255,.3);
          border-radius: 50%;
          border-top-color: #fff;
          animation: spin 1s ease-in-out infinite;
        }

        @keyframes spin {
          to { transform: rotate(360deg); }
        }

        .form-slide-enter {
          transform: translateY(-20px);
          opacity: 0;
        }

        .form-slide-enter-active {
          transform: translateY(0);
          opacity: 1;
          transition: all 0.3s ease;
        }

        @media (max-width: 768px) {
          .resource-card {
            margin-bottom: 1rem;
          }
        }
      `}</style>

      
      <div 
        className="gradient-header p-4 mb-4 rounded-4 text-white position-relative overflow-hidden"
        style={{ marginTop: '-20px', marginLeft: '-20px', marginRight: '-20px' }}
      >
        <div className="position-absolute top-0 start-0 w-100 h-100" style={{
          background: 'radial-gradient(circle at 30% 20%, rgba(255,255,255,0.1) 0%, transparent 50%)',
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
                <h3 className="mb-1 fw-bold">Resource Master</h3>
                <p className="mb-0 opacity-75">Manage your team resources and skills</p>
              </div>
            </div>
            
            <button 
              className="btn btn-light gradient-btn text-white px-4 py-2 rounded-3 d-flex align-items-center"
              onClick={() => setShowForm(!showForm)}
            >
              <i className={`bi ${showForm ? 'bi-list' : 'bi-person-plus-fill'} me-2`}></i>
              {showForm ? 'View Resources' : 'Add Resource'}
            </button>
          </div>
        </div>
      </div>

      
      <div className="row mb-4">
        <div className="col-md-3">
          <div className="stats-card p-3 rounded-3 text-center">
            <i className="bi bi-people text-primary fs-2 mb-2"></i>
            <h4 className="fw-bold mb-1 text-primary">{resources.length}</h4>
            <small className="text-muted">Total Resources</small>
          </div>
        </div>
        <div className="col-md-3">
          <div className="stats-card p-3 rounded-3 text-center">
            <i className="bi bi-award text-success fs-2 mb-2"></i>
            <h4 className="fw-bold mb-1 text-success">{experienceStats.senior}</h4>
            <small className="text-muted">Senior (5+ years)</small>
          </div>
        </div>
        <div className="col-md-3">
          <div className="stats-card p-3 rounded-3 text-center">
            <i className="bi bi-graph-up text-info fs-2 mb-2"></i>
            <h4 className="fw-bold mb-1 text-info">{experienceStats.mid}</h4>
            <small className="text-muted">Mid-level (3-5 years)</small>
          </div>
        </div>
        <div className="col-md-3">
          <div className="stats-card p-3 rounded-3 text-center">
            <i className="bi bi-mortarboard text-warning fs-2 mb-2"></i>
            <h4 className="fw-bold mb-1 text-warning">{experienceStats.avg}</h4>
            <small className="text-muted">Avg Experience</small>
          </div>
        </div>
      </div>

      {showForm ? (
        
        <div className="glass-card p-4 rounded-3 mb-4 form-slide-enter form-slide-enter-active">
          <div className="d-flex align-items-center mb-4">
            <div 
              className="d-flex align-items-center justify-content-center me-3 rounded-2"
              style={{
                background: 'linear-gradient(135deg, #28a745, #20c997)',
                width: '40px',
                height: '40px'
              }}
            >
              <i className="bi bi-person-plus text-white"></i>
            </div>
            <h4 className="mb-0 fw-bold">Add New Resource</h4>
          </div>

          <form onSubmit={handleSubmit}>
            
            <div className="row mb-3">
              <div className="col-md-6">
                <label className="form-label fw-semibold">
                  <i className="bi bi-person me-1"></i>First Name
                </label>
                <input 
                  name="firstName" 
                  value={formData.firstName} 
                  onChange={handleChange} 
                  placeholder="Enter first name" 
                  className="form-control form-control-glass rounded-3" 
                  required 
                />
              </div>
              <div className="col-md-6">
                <label className="form-label fw-semibold">
                  <i className="bi bi-person me-1"></i>Last Name
                </label>
                <input 
                  name="lastName" 
                  value={formData.lastName} 
                  onChange={handleChange} 
                  placeholder="Enter last name" 
                  className="form-control form-control-glass rounded-3" 
                  required 
                />
              </div>
            </div>

            <div className="row mb-3">
              <div className="col-md-6">
                <label className="form-label fw-semibold">
                  <i className="bi bi-badge-tm me-1"></i>Employee ID
                </label>
                <input 
                  name="employeeId" 
                  value={formData.employeeId} 
                  onChange={handleChange} 
                  placeholder="Enter employee ID" 
                  className="form-control form-control-glass rounded-3" 
                  required 
                />
              </div>
              <div className="col-md-6">
                <label className="form-label fw-semibold">
                  <i className="bi bi-envelope me-1"></i>Email
                </label>
                <input 
                  name="email" 
                  type="email" 
                  value={formData.email} 
                  onChange={handleChange} 
                  placeholder="Enter email address" 
                  className="form-control form-control-glass rounded-3" 
                  required 
                />
              </div>
            </div>

            
            <div className="row mb-3">
              <div className="col-md-6">
                <label className="form-label fw-semibold">
                  <i className="bi bi-briefcase me-1"></i>Title
                </label>
                <select
                  name="titleId"
                  value={formData.titleId}
                  onChange={handleChange}
                  className="form-control form-control-glass rounded-3"
                  required
                >
                  <option value="">Select Title</option>
                  {titles.map((title) => (
                    <option key={title.id} value={title.id}>
                      {title.name}
                    </option>
                  ))}
                </select>
              </div>
              <div className="col-md-6">
                <label className="form-label fw-semibold">
                  <i className="bi bi-geo-alt me-1"></i>Location
                </label>
                <select
                  name="locationId"
                  value={formData.locationId}
                  onChange={handleChange}
                  className="form-control form-control-glass rounded-3"
                  required
                >
                  <option value="">Select Location</option>
                  {locations.map((loc) => (
                    <option key={loc.id} value={loc.id}>
                      {loc.name}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="row mb-3">
              <div className="col-md-6">
                <label className="form-label fw-semibold">
                  <i className="bi bi-calendar-event me-1"></i>Experience (Years)
                </label>
                <input 
                  name="experience" 
                  type="number" 
                  min="0"
                  max="50"
                  value={formData.experience} 
                  onChange={handleChange} 
                  placeholder="Years of experience" 
                  className="form-control form-control-glass rounded-3" 
                  required 
                />
              </div>
              <div className="col-md-6">
                <label className="form-label fw-semibold">
                  <i className="bi bi-tools me-1"></i>Skills
                </label>
                <Dropdown className="dropdown-glass">
                  <Dropdown.Toggle 
                    variant="outline-secondary" 
                    className="form-control-glass rounded-3 text-start d-flex justify-content-between align-items-center w-100"
                    style={{ 
                      border: '1px solid rgba(102, 126, 234, 0.2)',
                      background: 'rgba(255, 255, 255, 0.8)'
                    }}
                  >
                    <span>
                      {formData.skills.length > 0
                        ? `${formData.skills.length} skill(s) selected`
                        : "Select Skills"}
                    </span>
                  </Dropdown.Toggle>

                  <Dropdown.Menu style={{ maxHeight: '200px', overflowY: 'auto', width: '100%' }}>
                    {skillsOptions.map((skill) => (
                      <Dropdown.Item key={skill.id} as="div" onClick={(e) => e.stopPropagation()}>
                        <div className="form-check">
                          <input
                            className="form-check-input"
                            type="checkbox"
                            id={`skill-${skill.id}`}
                            checked={formData.skills.includes(skill.id)}
                            onChange={() => handleSkillChange(skill.id)}
                            style={{ accentColor: '#667eea' }}
                          />
                          <label className="form-check-label" htmlFor={`skill-${skill.id}`}>
                            {skill.name}
                          </label>
                        </div>
                      </Dropdown.Item>
                    ))}
                  </Dropdown.Menu>
                </Dropdown>
              </div>
            </div>

            <div className="d-flex gap-2">
              <button 
                type="submit" 
                className="btn gradient-btn-success text-white px-4 py-2 rounded-3 d-flex align-items-center"
                disabled={isLoading}
              >
                {isLoading ? (
                  <div className="loading-spinner me-2"></div>
                ) : (
                  <i className="bi bi-check-circle me-2"></i>
                )}
                {isLoading ? 'Adding...' : 'Add Resource'}
              </button>
              <button 
                type="button" 
                className="btn btn-outline-secondary px-4 py-2 rounded-3"
                onClick={resetForm}
                disabled={isLoading}
              >
                <i className="bi bi-arrow-clockwise me-2"></i>
                Reset
              </button>
            </div>
          </form>
        </div>
      ) : (
    
        <>
          
          <div className="glass-card p-3 rounded-3 mb-4">
            <div className="row align-items-center">
              <div className="col-md-4">
                <div className="position-relative">
                  <i className="bi bi-search position-absolute top-50 start-0 translate-middle-y ms-3 text-muted"></i>
                  <input
                    type="text"
                    className="form-control search-input ps-5 rounded-3"
                    placeholder="Search resources..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                  />
                </div>
              </div>
              <div className="col-md-3">
                <select
                  className="form-select filter-select rounded-3"
                  value={filterLocation}
                  onChange={(e) => setFilterLocation(e.target.value)}
                >
                  <option value="">All Locations</option>
                  {locations.map((location) => (
                    <option key={location.id} value={location.id}>
                      {location.name}
                    </option>
                  ))}
                </select>
              </div>
              <div className="col-md-3">
                <select
                  className="form-select filter-select rounded-3"
                  value={filterTitle}
                  onChange={(e) => setFilterTitle(e.target.value)}
                >
                  <option value="">All Titles</option>
                  {titles.map((title) => (
                    <option key={title.id} value={title.id}>
                      {title.name}
                    </option>
                  ))}
                </select>
              </div>
              <div className="col-md-2">
                <div className="text-muted small">
                  {filteredResources.length} of {resources.length}
                </div>
              </div>
            </div>
          </div>

          
          <div className="row">
            {isLoading ? (
              <div className="col-12 text-center py-5">
                <div className="loading-spinner me-2" style={{ width: '40px', height: '40px', borderWidth: '4px' }}></div>
                <p className="text-muted mt-3">Loading resources...</p>
              </div>
            ) : filteredResources.length === 0 ? (
              <div className="col-12">
                <div className="glass-card p-5 rounded-3 text-center">
                  <i className="bi bi-person-x display-1 mb-3 opacity-25"></i>
                  <h5 className="mb-2">No resources found</h5>
                  <p className="text-muted mb-3">Try adjusting your search criteria or add new resources</p>
                  <button 
                    className="btn gradient-btn text-white px-4 py-2 rounded-3"
                    onClick={() => setShowForm(true)}
                  >
                    <i className="bi bi-person-plus me-2"></i>
                    Add First Resource
                  </button>
                </div>
              </div>
            ) : (
              filteredResources.map((resource) => (
                <div key={resource.id} className="col-lg-6 col-xl-4 mb-4">
                  <div className="resource-card p-4 rounded-3">
                    <div className="d-flex align-items-center mb-3">
                      <div className="avatar-circle me-3">
                        {resource.firstName?.[0]}{resource.lastName?.[0]}
                      </div>
                      <div className="flex-grow-1">
                        <h6 className="mb-1 fw-bold">
                          {resource.firstName} {resource.lastName}
                        </h6>
                        <small className="text-muted">
                          <i className="bi bi-envelope me-1"></i>
                          {resource.email}
                        </small>
                      </div>
                    </div>

                    <div className="mb-3">
                      <div className="d-flex align-items-center mb-2">
                        <i className="bi bi-badge-tm text-muted me-2"></i>
                        <span className="small text-muted">ID:</span>
                        <span className="ms-1 fw-medium">{resource.employeeId}</span>
                      </div>
                      <div className="d-flex align-items-center mb-2">
                        <i className="bi bi-briefcase text-muted me-2"></i>
                        <span className="fw-medium">{getTitleName(resource.titleId)}</span>
                      </div>
                      <div className="d-flex align-items-center mb-2">
                        <i className="bi bi-geo-alt text-muted me-2"></i>
                        <span className="text-muted">{getLocationName(resource.locationId)}</span>
                      </div>
                    </div>

                    <div className="mb-3">
                      <div className="d-flex align-items-center justify-content-between mb-2">
                        <span className="small text-muted">Experience</span>
                        <span className={`badge experience-badge ${
                          resource.experience <= 2 ? 'experience-junior' :
                          resource.experience <= 5 ? 'experience-mid' : 'experience-senior'
                        }`}>
                          {resource.experience} {resource.experience === 1 ? 'year' : 'years'}
                        </span>
                      </div>
                    </div>

                    <div>
                      <div className="small text-muted mb-2">Skills:</div>
                      <div>
                        {resource.skills && resource.skills.length > 0 ? (
                          resource.skills.slice(0, 3).map((skillId, index) => {
                            const skill = skillsOptions.find(s => s.id === skillId);
                            return skill ? (
                              <span key={skillId} className="skills-badge">
                                {skill.name}
                                                                </span>
                            ) : null;
                          })
                        ) : (
                          <span className="text-muted small">No skills</span>
                        )}

                        
                        {resource.skills && resource.skills.length > 3 && (
                          <span className="text-muted small ms-1">
                            +{resource.skills.length - 3} more
                          </span>
                        )}
                      </div>
                    </div>
                  </div>
                </div>
              ))
            )}
          </div>
        </>
      )}
    </>
  );
};

export default ResourceMasterPage;
