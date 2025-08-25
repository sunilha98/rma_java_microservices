import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { useForm } from 'react-hook-form';

const ShiftCreationPage = () => {
  const [shifts, setShifts] = useState([]);
  const { register, handleSubmit, reset } = useForm();

  const fetchShifts = async () => {
    try {
      const response = await api.get('/shifts');
      setShifts(response.data);
    } catch (error) {
      console.error('Failed to fetch shifts:', error);
    }
  };

  useEffect(() => {
    fetchShifts();
  }, []);

  const onSubmit = async (data) => {
    try {
      await api.post('/shifts', data);
      alert('Shift created successfully');
      reset();
      fetchShifts();
    } catch (error) {
      console.error('Failed to create shift:', error);
      alert('Error creating shift');
    }
  };

  return (
    <>
      <style>{`
        .gradient-header {
          background: linear-gradient(135deg, #ffb347 0%, #ffcc33 50%, #ff7e5f 100%);
          backdrop-filter: blur(10px);
        }
        .glass-card {
          background: rgba(255, 255, 255, 0.95);
          backdrop-filter: blur(10px);
          border: 1px solid rgba(255, 255, 255, 0.2);
          box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
          padding: 20px;
        }
        .custom-table {
          background: transparent;
        }
        .gradient-header-table {
          background: linear-gradient(135deg, #ffb347 0%, #ffcc33 50%, #ff7e5f 100%);
          position: relative;
        }
        .gradient-header-table::after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 0;
          right: 0;
          height: 3px;
          background: linear-gradient(90deg, #ffb347, #ffcc33, #ff7e5f);
        }
        .table-row-custom {
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          background: rgba(255, 255, 255, 0.7);
        }
        .table-row-custom:hover {
          background: rgba(255, 179, 71, 0.08) !important;
          transform: translateX(5px);
          box-shadow: 0 4px 15px rgba(255, 179, 71, 0.1);
        }
        .table-row-custom:nth-child(even) {
          background: rgba(255, 255, 255, 0.5);
        }
        .table-row-custom:nth-child(even):hover {
          background: rgba(255, 179, 71, 0.08) !important;
        }
        .form-control {
          backdrop-filter: blur(8px);
          border: 1px solid rgba(0,0,0,0.1);
          background: rgba(255,255,255,0.8);
        }
        .btn-primary {
          background: linear-gradient(135deg, #ffb347, #ffcc33);
          border: none;
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
              <i className="bi bi-calendar2-check-fill fs-4"></i>
            </div>
            <div>
              <h3 className="mb-1 fw-bold">Shift Timings Master</h3>
              <p className="mb-0 opacity-75">Manage shift creation and timings</p>
            </div>
          </div>
        </div>
      </div>

      
      <div className="glass-card mb-4">
        <form onSubmit={handleSubmit(onSubmit)} className="mb-0">
          <div className="row g-2">
            <div className="col-md-4">
              <input
                {...register('name')}
                placeholder="Shift Name"
                className="form-control"
                required
              />
            </div>
            <div className="col-md-3">
              <input
                {...register('startTime')}
                type="time"
                className="form-control"
                required
              />
            </div>
            <div className="col-md-3">
              <input
                {...register('endTime')}
                type="time"
                className="form-control"
                required
              />
            </div>
            <div className="col-md-2">
              <button type="submit" className="btn btn-primary w-100">
                Add Shift
              </button>
            </div>
          </div>
        </form>
      </div>

      <div className="glass-card">
        <div className="table-responsive">
          <table className="table table-hover mb-0 custom-table">
            <thead>
              <tr className="gradient-header-table text-white">
                <th className="border-0 fw-semibold py-3 px-4">Shift Name</th>
                <th className="border-0 fw-semibold py-3 px-4">Start Time</th>
                <th className="border-0 fw-semibold py-3 px-4">End Time</th>
              </tr>
            </thead>
            <tbody>
              {shifts.length === 0 ? (
                <tr>
                  <td colSpan="3" className="text-center py-5 text-muted">
                    <div className="d-flex flex-column align-items-center">
                      <i className="bi bi-calendar2-fill display-1 mb-3 opacity-25"></i>
                      <h5 className="mb-2">No shifts available</h5>
                      <p className="mb-0">Create new shifts using the form above</p>
                    </div>
                  </td>
                </tr>
              ) : (
                shifts.map((shift) => (
                  <tr key={shift.id} className="table-row-custom">
                    <td className="py-3 px-4">{shift.name}</td>
                    <td className="py-3 px-4">{shift.startTime}</td>
                    <td className="py-3 px-4">{shift.endTime}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
};

export default ShiftCreationPage;
