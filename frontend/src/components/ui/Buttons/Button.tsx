import React, { type ButtonHTMLAttributes, type ReactNode } from 'react';

// 1. Definiujemy Unię dla wariantów (typ bezpieczny)
type ButtonVariant = 'primary' | 'secondary' | 'danger';

// 2. Rozszerzamy standardowe atrybuty przycisku HTML
interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: ButtonVariant;
  children: ReactNode; // Pozwala na przekazywanie tekstu, ikonek, innych komponentów
}

const Button: React.FC<ButtonProps> = ({
  children,
  ...props // Tu trafiają: onClick, type, disabled, title itd.
}) => {
  // Przykład prostego stylowania opartego na wariancie

  return (
    <button className={`btn ${props.variant ? `btn-${props.variant}` : ''}`}
      {...props}
    >
      {children}
    </button>
  );
};

export default Button;
