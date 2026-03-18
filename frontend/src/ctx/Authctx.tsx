import { createContext, useContext, useState, } from 'react';
import { LibraryManager } from '../model/LibraryManager';
import { User } from '../model/User';

// Definiujemy co będzie dostępne w kontekście
interface LibraryContextType {
    manager: LibraryManager;
    currentUser: User;
    setCurrentUser: (user: User) => void;
    refreshData: () => void; // Funkcja do wymuszenia przerenderowania po zmianach w managerze
}

const LibraryContext = createContext<LibraryContextType | undefined>(undefined);

export const LibraryProvider = ({ children }: { children: React.ReactNode }) => {
    // Pobieramy instancję Singletona
    const manager = LibraryManager.getManagerInstance();

    // Trzymamy użytkownika w stanie Reacta, żeby UI reagowało na zmiany
    const [currentUser, setCurrentUser] = useState<User>(
        new User(1, "John", "Smith", "a@p.pl", "admin")
    );
    // Helper do odświeżania UI po operacjach na managerze (np. dodaniu książki)
    const [, setTick] = useState(0);
    const refreshData = () => setTick(prev => prev + 1);

    return (
        <LibraryContext.Provider value={{ manager, currentUser, setCurrentUser, refreshData }}>
            {children}
        </LibraryContext.Provider>
    );
};

// Własny hook dla łatwiejszego użycia
export const useLibrary = () => {
    const context = useContext(LibraryContext);
    if (!context) throw new Error('useLibrary must be used within LibraryProvider');
    return context;
};
