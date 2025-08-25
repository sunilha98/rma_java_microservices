import React from 'react';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

const LoginPage = () => {
  const navigate = useNavigate();
  const { login } = useAuth();

  const formik = useFormik({
    initialValues: { username: '', password: '' },
    validationSchema: Yup.object({
      username: Yup.string().required('Username is required'),
      password: Yup.string().required('Password is required'),
    }),
    onSubmit: async (values, { setSubmitting, setErrors }) => {
      try {
        const response = await api.post('/auth/login', values);
        const { token, user } = response.data;
        localStorage.setItem('token', token);
        login(user);
        navigate('/dashboard');
      } catch (error) {
        setErrors({ password: 'Invalid username or password' });
      } finally {
        setSubmitting(false);
      }
    },
  });

  return (
    <>
      <style>{`
        body, html {
          height: 100vh;
          margin: 0;
          padding: 0;
          font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
          background: url('https://images.unsplash.com/photo-1557804506-669a67965ba0?auto=format&fit=crop&w=1974&q=80') no-repeat center center fixed;
          background-size: cover;
          display: flex;
          align-items: center;
          justify-content: center;
          min-height: 100vh;
          box-sizing: border-box;
        }
        
        body::before {
          content: '';
          position: fixed;
          top: 0;
          left: 0;
          width: 100vw;
          height: 100vh;
          background: url('https://images.unsplash.com/photo-1557804506-669a67965ba0?auto=format&fit=crop&w=1974&q=80') no-repeat center center;
          background-size: cover;
          filter: blur(4px);
          transform: scale(1.1);
          z-index: -2;
        }
        
        .overlay {
          position: fixed;
          top: 0;
          left: 0;
          width: 100vw;
          height: 100vh;
          background: linear-gradient(135deg, rgba(30,58,138,0.6) 0%, rgba(67,56,202,0.5) 50%, rgba(16,185,129,0.4) 100%);
          z-index: 1;
        }
        
        .login-card {
          position: relative;
          z-index: 2;
          width: 100%;
          max-width: 420px;
          padding: 40px 30px;
          background: rgba(255, 255, 255, 0.1);
          backdrop-filter: blur(20px);
          border: 1px solid rgba(255, 255, 255, 0.2);
          border-radius: 24px;
          box-shadow: 0 25px 60px rgba(0,0,0,0.4), 0 0 0 1px rgba(255,255,255,0.1) inset;
          text-align: center;
          margin: 20px;
          box-sizing: border-box;
        }
        
        .login-card::before {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
          border-radius: 24px;
          z-index: -1;
        }
        
        .login-card h2 {
          font-size: 32px;
          font-weight: 700;
          margin-bottom: 10px;
          color: #ffffff;
          text-shadow: 0 2px 10px rgba(0,0,0,0.3);
        }
        
        .subtitle {
          font-size: 16px;
          color: rgba(255,255,255,0.8);
          margin-bottom: 40px;
          font-weight: 400;
        }
        
        .form-control {
          width: 100%;
          padding: 18px 20px;
          margin-bottom: 24px;
          font-size: 16px;
          border-radius: 16px;
          border: 1px solid rgba(255,255,255,0.2);
          background: rgba(255,255,255,0.1);
          backdrop-filter: blur(10px);
          transition: all 0.3s ease;
          color: #ffffff;
          box-sizing: border-box;
        }
        
        .form-control::placeholder {
          color: rgba(255,255,255,0.6);
        }
        
        .form-control:focus {
          border-color: rgba(79,172,254,0.8);
          outline: none;
          box-shadow: 0 0 0 3px rgba(79,172,254,0.2);
          background: rgba(255,255,255,0.15);
          transform: translateY(-2px);
        }
        
        .btn-primary {
          width: 100%;
          padding: 18px 0;
          font-size: 16px;
          font-weight: 600;
          border-radius: 16px;
          border: none;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: #fff;
          cursor: pointer;
          transition: all 0.3s ease;
          margin-top: 10px;
          box-shadow: 0 10px 25px rgba(102,126,234,0.4);
        }
        
        .btn-primary:hover:not(:disabled) {
          transform: translateY(-3px);
          box-shadow: 0 15px 35px rgba(102,126,234,0.5);
          background: linear-gradient(135deg, #5a67d8 0%, #6b46c1 100%);
        }
        
        .btn-primary:disabled {
          opacity: 0.7;
          cursor: not-allowed;
          transform: none;
        }
        
        .text-danger {
          font-size: 14px;
          margin-top: -18px;
          margin-bottom: 20px;
          display: block;
          color: #ff6b7a;
          text-align: left;
          margin-left: 4px;
          font-weight: 500;
        }
        
        .signup-link {
          margin-top: 25px;
          font-size: 15px;
          color: rgba(255,255,255,0.8);
        }
        
        .signup-link a {
          color: #a78bfa;
          text-decoration: none;
          font-weight: 600;
          transition: color 0.3s ease;
        }
        
        .signup-link a:hover {
          color: #c4b5fd;
          text-decoration: underline;
        }
        
        .loading-spinner {
          display: inline-block;
          width: 18px;
          height: 18px;
          border: 2px solid rgba(255,255,255,0.3);
          border-radius: 50%;
          border-top: 2px solid #ffffff;
          animation: spin 1s linear infinite;
          margin-right: 8px;
        }
        
        @keyframes spin {
          0% { transform: rotate(0deg); }
          100% { transform: rotate(360deg); }
        }
        
        .floating-elements {
          position: absolute;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
          pointer-events: none;
          z-index: 1;
        }
        
        .floating-circle {
          position: absolute;
          border-radius: 50%;
          background: rgba(255,255,255,0.1);
          filter: blur(60px);
          animation: float 6s ease-in-out infinite;
        }
        
        .circle-1 {
          width: 300px;
          height: 300px;
          top: -150px;
          left: -150px;
          animation-delay: 0s;
        }
        
        .circle-2 {
          width: 200px;
          height: 200px;
          bottom: -100px;
          right: -100px;
          animation-delay: 3s;
        }
        
        @keyframes float {
          0%, 100% { transform: translateY(0px) rotate(0deg); }
          50% { transform: translateY(-20px) rotate(180deg); }
        }
      `}</style>

      <div className="floating-elements">
        <div className="floating-circle circle-1"></div>
        <div className="floating-circle circle-2"></div>
      </div>
      
      <div className="overlay"></div>
      <div className="login-card">
        <h2>Welcome Back</h2>
        <p className="subtitle">Sign in to continue to your account</p>
        
        <div>
          <input
            type="text"
            name="username"
            placeholder="Username"
            className="form-control"
            onChange={formik.handleChange}
            value={formik.values.username}
          />
          {formik.errors.username && <span className="text-danger">{formik.errors.username}</span>}

          <input
            type="password"
            name="password"
            placeholder="Password"
            className="form-control"
            onChange={formik.handleChange}
            value={formik.values.password}
          />
          {formik.errors.password && <span className="text-danger">{formik.errors.password}</span>}

          <button 
            type="submit" 
            className="btn-primary" 
            disabled={formik.isSubmitting}
            onClick={formik.handleSubmit}
          >
            {formik.isSubmitting ? (
              <>
                <span className="loading-spinner"></span>
                Logging in...
              </>
            ) : (
              'Login'
            )}
          </button>
        </div>
        
        <div className="signup-link">
          Don't have an account? <a href="/signup">Create one</a>
        </div>
      </div>
    </>
  );
};

export default LoginPage;