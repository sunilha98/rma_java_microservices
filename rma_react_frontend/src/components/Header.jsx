import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Header = () => {
  const { user, logout } = useAuth();

  return (
    <header 
      className="navbar navbar-expand-lg navbar-dark px-4 shadow-lg border-bottom"
      style={{
        background: 'linear-gradient(135deg, #FF6B6B 0%, #4ECDC4 25%, #45B7D1 50%, #96CEB4 75%, #FECA57 100%)',
        backdropFilter: 'blur(15px)',
        borderBottom: '1px solid rgba(255,255,255,0.2)',
        boxShadow: '0 8px 32px rgba(0,0,0,0.1)'
      }}
    >
      <Link 
        to="/dashboard" 
        className="d-flex align-items-center text-white text-decoration-none hover-lift"
        style={{ transition: 'all 0.3s ease' }}
        onMouseEnter={(e) => e.target.style.transform = 'translateY(-1px)'}
        onMouseLeave={(e) => e.target.style.transform = 'translateY(0)'}
      >
        <div 
          className="d-flex align-items-center justify-content-center me-3 rounded-3"
          style={{
            background: 'linear-gradient(145deg, #667eea, #764ba2)',
            width: '52px',
            height: '52px',
            backdropFilter: 'blur(10px)',
            border: '2px solid rgba(255,255,255,0.2)',
            boxShadow: 'inset 5px 5px 10px rgba(0,0,0,0.1), inset -5px -5px 10px rgba(255,255,255,0.1), 0 5px 15px rgba(0,0,0,0.2)'
          }}
        >
          <div className="d-flex align-items-center justify-content-center">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none">
              
              <defs>
                <linearGradient id="iconGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                  <stop offset="0%" style={{stopColor: '#FF6B6B', stopOpacity: 1}} />
                  <stop offset="50%" style={{stopColor: '#4ECDC4', stopOpacity: 1}} />
                  <stop offset="100%" style={{stopColor: '#FFE66D', stopOpacity: 1}} />
                </linearGradient>
                <filter id="shadow">
                  <feDropShadow dx="0" dy="2" stdDeviation="2" floodColor="rgba(0,0,0,0.3)"/>
                </filter>
              </defs>
              
            
              <polygon 
                points="12,2 22,8.5 22,15.5 12,22 2,15.5 2,8.5" 
                fill="url(#iconGradient)" 
                filter="url(#shadow)"
                opacity="0.9"
              />
              
              
              <circle cx="8" cy="8" r="2" fill="white" opacity="0.9"/>
              <circle cx="16" cy="8" r="2" fill="white" opacity="0.9"/>
              <circle cx="8" cy="16" r="2" fill="white" opacity="0.9"/>
              <circle cx="16" cy="16" r="2" fill="white" opacity="0.9"/>
              <circle cx="12" cy="12" r="2.5" fill="white" opacity="1"/>
              
              
              <line x1="8" y1="8" x2="12" y2="12" stroke="white" strokeWidth="2" opacity="0.7"/>
              <line x1="16" y1="8" x2="12" y2="12" stroke="white" strokeWidth="2" opacity="0.7"/>
              <line x1="8" y1="16" x2="12" y2="12" stroke="white" strokeWidth="2" opacity="0.7"/>
              <line x1="16" y1="16" x2="12" y2="12" stroke="white" strokeWidth="2" opacity="0.7"/>
              
              
              <circle cx="12" cy="12" r="1" fill="#FFE66D" opacity="1"/>
            </svg>
          </div>
        </div>
        <span className="navbar-brand fs-3 fw-bold mb-0" 
              style={{ 
                letterSpacing: '-0.8px',
                color: 'white',
                textShadow: '2px 2px 4px rgba(0,0,0,0.3)',
                fontFamily: 'system-ui, -apple-system, sans-serif'
              }}>
          Resource Management Hub
        </span>
      </Link>
      
      <div className="ms-auto d-flex align-items-center">
        <div 
          className="d-flex align-items-center text-white me-4 px-4 py-3 rounded-pill"
          style={{
            background: 'linear-gradient(135deg, rgba(255,255,255,0.2) 0%, rgba(255,255,255,0.1) 100%)',
            backdropFilter: 'blur(15px)',
            border: '1px solid rgba(255,255,255,0.25)',
            boxShadow: '0 4px 15px rgba(0,0,0,0.1)'
          }}
        >
          <div 
            className="d-flex align-items-center justify-content-center me-3 rounded-circle"
            style={{
              background: 'linear-gradient(135deg, #A8E6CF 0%, #FFD93D 100%)',
              width: '38px',
              height: '38px',
              boxShadow: '0 4px 12px rgba(168,230,207,0.4)'
            }}
          >
            <i className="bi bi-emoji-sunglasses-fill" 
               style={{ 
                 color: '#2D3436', 
                 fontSize: '16px',
                 filter: 'drop-shadow(0 1px 2px rgba(0,0,0,0.1))'
               }}></i>
          </div>
          <div className="d-flex flex-column">
            <span className="fw-semibold" style={{ fontSize: '14px', lineHeight: '1.2' }}>
              {user?.username}
            </span>
            <span 
              className="badge rounded-pill mt-1"
              style={{
                background: 'linear-gradient(135deg, #74b9ff 0%, #0984e3 100%)',
                color: '#ffffff',
                fontSize: '11px',
                fontWeight: '700',
                letterSpacing: '0.8px',
                textTransform: 'uppercase',
                padding: '4px 12px',
                boxShadow: '0 2px 8px rgba(116,185,255,0.4)'
              }}
            >
              🔥 {user?.role}
            </span>
          </div>
        </div>
        
        <button 
          className="btn btn-sm px-5 py-3 fw-bold rounded-pill"
          onClick={logout}
          style={{
            background: 'linear-gradient(135deg, #fd79a8 0%, #fdcb6e 100%)',
            color: '#2d3436',
            border: 'none',
            transition: 'all 0.3s ease',
            backdropFilter: 'blur(15px)',
            boxShadow: '0 4px 15px rgba(253,121,168,0.4)',
            textTransform: 'uppercase',
            letterSpacing: '1px',
            fontSize: '12px'
          }}
          onMouseEnter={(e) => {
            e.target.style.background = 'linear-gradient(135deg, #e84393 0%, #f39c12 100%)';
            e.target.style.transform = 'translateY(-2px) scale(1.05)';
            e.target.style.boxShadow = '0 8px 25px rgba(253,121,168,0.6)';
          }}
          onMouseLeave={(e) => {
            e.target.style.background = 'linear-gradient(135deg, #fd79a8 0%, #fdcb6e 100%)';
            e.target.style.transform = 'translateY(0) scale(1)';
            e.target.style.boxShadow = '0 4px 15px rgba(253,121,168,0.4)';
          }}
        >
          <i className="bi bi-power me-2" style={{ fontSize: '14px' }}></i>
          Logout 🚀
        </button>
      </div>
    </header>
  );
};

export default Header;