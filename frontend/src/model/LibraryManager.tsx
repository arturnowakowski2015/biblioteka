import type { Loans } from "./Loans";
import type { LoansHistory } from "./LoansHistory";
import type { User } from "./User";
import { Katalog } from "./katalog"

export class LibraryManager {
    private katalog: Katalog;
    private users: User[];
    private loans: Loans[];
    private historyLoans: LoansHistory[];
    private static instance: LibraryManager;
    private constructor() {
        // 1. Pobieramy dane i parsujemy JSON lub przypisujemy puste tablice/obiekty
        this.users = JSON.parse(localStorage.getItem('lib_users') || '[]');
        this.loans = JSON.parse(localStorage.getItem('lib_loans') || '[]');
        this.historyLoans = JSON.parse(localStorage.getItem('lib_history') || '[]');

        // 2. Katalog to zazwyczaj obiekt, więc inicjalizujemy go nową instancją lub danymi
        const savedKatalog = localStorage.getItem('lib_katalog');
        this.katalog = savedKatalog ? JSON.parse(savedKatalog) :
            new Katalog([], [], "nazwa", "pusty katalog");

    }
    // Symulacja computeIfAbsent
    public getOrCreate<K, V>(map: Map<K, V>, key: K, factory: (k: K) => V): V {
        if (!map.has(key)) {
            map.set(key, factory(key));
        }
        return map.get(key)!;
    }

    // Użycie:
    //const katalog = getOrCreate(catalogs, UserClass, (c) => new GenericKatalog(c.name));
    public static getManagerInstance(): LibraryManager {
        if (!LibraryManager.instance) {
            LibraryManager.instance = new LibraryManager();
        }
        return LibraryManager.instance;
    }

    public getKatalog(): Katalog {
        return this.katalog;
    }

    public getLoans(): Loans[] {
        return this.loans;
    }

    public getHistoryLoans(): LoansHistory[] {
        return this.historyLoans;
    }

    // Metoda pomocnicza, którą warto mieć do zapisywania zmian
    public saveData(): void {
        localStorage.setItem('lib_users', JSON.stringify(this.users));
        localStorage.setItem('lib_loans', JSON.stringify(this.loans));
        localStorage.setItem('lib_history', JSON.stringify(this.historyLoans));
        localStorage.setItem('lib_katalog', JSON.stringify(this.katalog));
    }
}