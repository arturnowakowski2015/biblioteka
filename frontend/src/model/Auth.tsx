export type UserRole = "admin" | "user";

export interface AuthUser {
    id: number;
    name: string;
    surname?: string;
    email?: string;
    role: UserRole;
}

export interface AuthContextValue {
    user: AuthUser | null;
    login: (user: AuthUser) => void;
    logout: () => void;
}

export interface NewBookData {
    tytul: string;
    autor: string;
    rok: number;
    opis: string;
}
