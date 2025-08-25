import React, { useState, useEffect } from 'react';
import Header from '../components/Header';
import Sidebar from '../components/Sidebar';

const Layout = ({ children }) => {
 
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const [isMobile, setIsMobile] = useState(false);

  useEffect(() => {
   
    const checkMobile = () => {
      if (typeof window !== 'undefined') {
        return window.innerWidth <= 768;
      }
      return false;
    };

    // Set initial mobile state
    const mobile = checkMobile();
    setIsMobile(mobile);
    if (mobile) {
      setSidebarCollapsed(true);
    }

    const handleResize = () => {
      const mobile = checkMobile();
      setIsMobile(mobile);
      if (mobile) {
        setSidebarCollapsed(true);
      }
    };

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  const toggleSidebar = () => {
    setSidebarCollapsed(!sidebarCollapsed);
  };

  const scrollToTop = () => {
    if (typeof window !== 'undefined') {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  };

  return (
    <div className="d-flex position-relative min-vh-100 overflow-hidden">
      
      {isMobile && !sidebarCollapsed && (
        <div 
          className="position-fixed w-100 h-100"
          style={{
            top: 0,
            left: 0,
            background: 'rgba(0,0,0,0.5)',
            zIndex: 999,
            backdropFilter: 'blur(5px)'
          }}
          onClick={toggleSidebar}
        />
      )}

      
      <div 
        className={`sidebar-container ${sidebarCollapsed ? 'collapsed' : ''}`}
        style={{
          transform: sidebarCollapsed && isMobile ? 'translateX(-100%)' : 'translateX(0)',
          transition: 'transform 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
          zIndex: 1000,
          position: isMobile ? 'fixed' : 'relative'
        }}
      >
        <Sidebar />
      </div>

      
      <div 
        className="flex-grow-1 d-flex flex-column"
        style={{
          marginLeft: isMobile ? '0' : (sidebarCollapsed ? '0' : '0'),
          transition: 'margin-left 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
          background: 'linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)',
          minHeight: '100vh'
        }}
      >
       
        <div className="position-relative">
          <Header />
          
          
          {isMobile && (
            <button
              className="btn position-absolute d-flex align-items-center justify-content-center"
              onClick={toggleSidebar}
              style={{
                top: '50%',
                left: '16px',
                transform: 'translateY(-50%)',
                width: '42px',
                height: '42px',
                background: 'rgba(255,255,255,0.9)',
                border: 'none',
                borderRadius: '12px',
                boxShadow: '0 4px 15px rgba(0,0,0,0.1)',
                backdropFilter: 'blur(10px)',
                zIndex: 10,
                transition: 'all 0.3s ease'
              }}
              onMouseEnter={(e) => {
                e.target.style.background = '#ffffff';
                e.target.style.transform = 'translateY(-50%) scale(1.05)';
              }}
              onMouseLeave={(e) => {
                e.target.style.background = 'rgba(255,255,255,0.9)';
                e.target.style.transform = 'translateY(-50%) scale(1)';
              }}
            >
              <i 
                className="bi bi-list text-primary"
                style={{ 
                  fontSize: '20px',
                  transition: 'transform 0.3s ease'
                }}
              ></i>
            </button>
          )}
        </div>

        
        <main 
          className="flex-grow-1 p-4 position-relative"
          style={{
            background: 'transparent',
            overflow: 'visible'
          }}
        >
          
          <div 
            className="container-fluid"
            style={{
              background: 'rgba(255,255,255,0.7)',
              backdropFilter: 'blur(15px)',
              borderRadius: '20px',
              border: '1px solid rgba(255,255,255,0.2)',
              boxShadow: '0 8px 32px rgba(0,0,0,0.1)',
              padding: '2rem',
              position: 'relative',
              overflow: 'visible'
            }}
          >
            
            <div 
              className="position-absolute"
              style={{
                top: 0,
                left: 0,
                right: 0,
                bottom: 0,
                backgroundImage: `
                  radial-gradient(circle at 20% 80%, rgba(120, 119, 198, 0.1) 0%, transparent 50%),
                  radial-gradient(circle at 80% 20%, rgba(255, 107, 107, 0.1) 0%, transparent 50%),
                  radial-gradient(circle at 40% 40%, rgba(78, 205, 196, 0.1) 0%, transparent 50%)
                `,
                pointerEvents: 'none',
                zIndex: -1
              }}
            />
            
            
            <div className="position-relative z-1">
              {children}
            </div>
          </div>

          
          <button
            className="btn position-fixed d-flex align-items-center justify-content-center"
            style={{
              bottom: '30px',
              right: '30px',
              width: '56px',
              height: '56px',
              background: 'linear-gradient(135deg, #667eea, #764ba2)',
              border: 'none',
              borderRadius: '50%',
              boxShadow: '0 8px 25px rgba(102, 126, 234, 0.4)',
              zIndex: 100,
              transition: 'all 0.3s ease'
            }}
            onMouseEnter={(e) => {
              e.target.style.transform = 'scale(1.1)';
              e.target.style.boxShadow = '0 12px 35px rgba(102, 126, 234, 0.6)';
            }}
            onMouseLeave={(e) => {
              e.target.style.transform = 'scale(1)';
              e.target.style.boxShadow = '0 8px 25px rgba(102, 126, 234, 0.4)';
            }}
            onClick={scrollToTop}
          >
            <i className="bi bi-arrow-up text-white fs-5"></i>
          </button>
        </main>
      </div>

      <style>{`
        /*
        main {
          scrollbar-width: thin;
          scrollbar-color: rgba(102, 126, 234, 0.3) transparent;
        }
        
        main::-webkit-scrollbar {
          width: 6px;
        }
        
        main::-webkit-scrollbar-track {
          background: transparent;
        }
        
        main::-webkit-scrollbar-thumb {
          background: linear-gradient(135deg, rgba(102, 126, 234, 0.3), rgba(118, 75, 162, 0.3));
          border-radius: 10px;
        }
        
        main::-webkit-scrollbar-thumb:hover {
          background: linear-gradient(135deg, rgba(102, 126, 234, 0.5), rgba(118, 75, 162, 0.5));
        }
        */
        
        
        @media (max-width: 768px) {
          .container-fluid {
            padding: 1rem !important;
            margin-top: 1rem;
            border-radius: 15px !important;
          }
        }
        
        
        .container-fluid {
          animation: slideInFromBottom 0.5s ease-out;
        }
        
        @keyframes slideInFromBottom {
          from {
            opacity: 0;
            transform: translateY(20px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }
        
        
        button:focus-visible {
          outline: 2px solid #667eea;
          outline-offset: 2px;
        }
        
        
        .sidebar-container {
          flex-shrink: 0;
        }
      `}</style>
    </div>
  );
};

export default Layout;