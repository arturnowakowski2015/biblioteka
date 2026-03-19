import React, { type ButtonHTMLAttributes, type ReactNode } from 'react';

// 1. Definiujemy Unię dla wariantów (typ bezpieczny)
type ButtonVariant = 'primary' | 'secondary' | 'danger';

// 2. Rozszerzamy standardowe atrybuty przycisku HTML
interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: ButtonVariant;
  children: ReactNode; // Pozwala na przekazywanie tekstu, ikonek, innych komponentów
}

const Button: React.FC<ButtonProps> = ({ 
  variant = 'primary', 
  children, 
  className = '', 
  ...props // Tu trafiają: onClick, type, disabled, title itd.
}) => {
  // Przykład prostego stylowania opartego na wariancie
  const variantClass = `btn-${variant}`;

  return (
    <button 
      className={`base-button ${variantClass} ${className}`} 
      {...props} 
    >
      {children}
    </button>
  );
};

export default Button;
