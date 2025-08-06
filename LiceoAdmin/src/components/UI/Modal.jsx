function Modal({ 
  isOpen, 
  onClose, 
  title, 
  children, 
  footer,
  size = "md",
  closeOnOverlay = true 
}) {
  if (!isOpen) return null;

  const getSizeClasses = () => {
    switch (size) {
      case "sm": return "max-w-md";
      case "lg": return "max-w-2xl";
      case "xl": return "max-w-4xl";
      default: return "max-w-md";
    }
  };

  const handleOverlayClick = (e) => {
    if (closeOnOverlay && e.target === e.currentTarget) {
      onClose();
    }
  };

  return (
    <div 
      className="fixed inset-0 bg-amber-50 bg-opacity-50 flex items-center justify-center z-50"
      onClick={handleOverlayClick}
    >
      <div className={`bg-white rounded-lg shadow-xl p-6 w-full ${getSizeClasses()}`}>
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold text-gray-800">{title}</h2>
          <button
            onClick={onClose}
            className="text-gray-500 hover:text-gray-700 transition-colors"
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <div className="mb-6">
          {children}
        </div>
        
        {footer && (
          <div className="flex justify-end space-x-3">
            {footer}
          </div>
        )}
      </div>
    </div>
  );
}

export default Modal;