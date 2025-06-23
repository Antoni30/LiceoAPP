import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {  faTriangleExclamation,faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { useNavigate } from 'react-router-dom';

export default function NotFound() {
  const navigate = useNavigate();

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 p-4">
      <div className="max-w-md w-full bg-white rounded-lg shadow-md p-8 text-center">
        <FontAwesomeIcon 
          icon={faTriangleExclamation} 
          className="text-yellow-500 text-6xl mb-4" 
        />
        <h1 className="text-3xl font-bold text-gray-800 mb-2">404 - Página no encontrada</h1>
        <p className="text-gray-600 mb-6">
          Lo sentimos, la página que estás buscando no existe o ha sido movida.
        </p>
        <button
          onClick={() => navigate(-1)}
          className="bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded-md transition duration-300 flex items-center justify-center mx-auto"
        >
          <FontAwesomeIcon icon={faArrowLeft} className="mr-2" />
          Regresar
        </button>
      </div>
    </div>
  );
}