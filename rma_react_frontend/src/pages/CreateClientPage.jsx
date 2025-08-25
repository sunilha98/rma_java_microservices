import React, { useState } from 'react';
import api from '../services/api';

const CreateClientPage = () => {
  const [form, setForm] = useState({
    name: '',
    code: '',
    contactEmail: '',
    contactPhone: '',
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/clients', form);
      alert('🎉 Client created successfully!');
      setForm({ name: '', code: '', contactEmail: '', contactPhone: '' });
    } catch (err) {
      console.error(err);
      alert('⚠️ Error creating client');
    }
  };

  return (
    <div className="container mt-5 d-flex justify-content-center">
      <div 
        className="card p-4 shadow-lg"
        style={{
          maxWidth: 520,
          width: '100%',
          borderRadius: '20px',
          background: 'linear-gradient(145deg, rgba(255,255,255,0.15), rgba(255,255,255,0.05))',
          backdropFilter: 'blur(20px)',
          border: '1px solid rgba(255,255,255,0.2)',
          boxShadow: '0 8px 32px rgba(0,0,0,0.1)'
        }}
      >
        <h3 
          className="mb-4 text-center fw-bold"
          style={{
            background: 'linear-gradient(135deg, #FF6B6B, #4ECDC4, #45B7D1)',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent'
          }}
        >
          <i className="bi bi-building me-2"></i> Create New Client
        </h3>

        <form onSubmit={handleSubmit}>
          {[
            { label: 'Client Name', name: 'name', placeholder: 'Client Name', icon: 'bi-person-badge' },
            { label: 'Client Code', name: 'code', placeholder: 'Client Code', icon: 'bi-upc-scan' },
            { label: 'Contact Email', name: 'contactEmail', placeholder: 'Contact Email', icon: 'bi-envelope' },
            { label: 'Contact Phone', name: 'contactPhone', placeholder: 'Contact Phone', icon: 'bi-telephone' }
          ].map(({ label, name, placeholder, icon }) => (
            <div className="mb-3" key={name}>
              <label className="form-label fw-semibold">{label}</label>
              <div className="input-group">
                <span 
                  className="input-group-text"
                  style={{
                    background: 'linear-gradient(135deg, #FF6B6B, #4ECDC4)',
                    border: 'none',
                    color: 'white'
                  }}
                >
                  <i className={`bi ${icon}`}></i>
                </span>
                <input
                  name={name}
                  className="form-control"
                  placeholder={placeholder}
                  value={form[name]}
                  onChange={handleChange}
                  style={{
                    backgroundColor: 'rgba(255,255,255,0.8)',
                    border: '1px solid rgba(0,0,0,0.1)',
                    borderRadius: '0 8px 8px 0'
                  }}
                  required={name !== 'contactEmail' && name !== 'contactPhone' ? true : false}
                />
              </div>
            </div>
          ))}

          <button 
            type="submit" 
            className="btn w-100 fw-bold py-2 mt-3"
            style={{
              background: 'linear-gradient(135deg, #FF6B6B, #4ECDC4, #45B7D1)',
              border: 'none',
              borderRadius: '12px',
              color: 'white',
              letterSpacing: '0.8px',
              boxShadow: '0 6px 20px rgba(0,0,0,0.15)',
              transition: 'all 0.3s ease'
            }}
            onMouseEnter={(e) => {
              e.target.style.transform = 'translateY(-2px) scale(1.02)';
              e.target.style.boxShadow = '0 8px 25px rgba(0,0,0,0.25)';
            }}
            onMouseLeave={(e) => {
              e.target.style.transform = 'translateY(0) scale(1)';
              e.target.style.boxShadow = '0 6px 20px rgba(0,0,0,0.15)';
            }}
          >
            <i className="bi bi-plus-circle me-2"></i> Create Client
          </button>
        </form>
      </div>
    </div>
  );
};

export default CreateClientPage;
