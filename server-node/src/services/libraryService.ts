import { Book } from "../models/domain/Book";
import { Loan } from "../models/domain/Loan";
import { User } from "../models/domain/User";
import { UserRole } from "../models/domain/UserRole";
import { LibraryResponse } from "../models/dto/LibraryResponse";
import { FirstName } from "../models/dto/FirstName";
import { Item } from "../models/domain/Item";
type MyReturnType<T> = T extends (...args: any) => infer ReturnType
    ? ReturnType
    : never
const COPY_STATUS = {
    AVAILABLE: "AVAILABLE",
    BORROWED: "BORROWED",
    RESERVED: "RESERVED",
} as const;

class LibraryService {
    private items: Item[] = [];
    private loans: Loan[] = [];
    private names: FirstName[] = [];
    private nextItemId: number = 1004;
    private nextLoanId: number = 4;
    private admin: User;

    constructor() {
        this.admin = new User(1, "Jan", "Nowak", "jan.nowak@biblioteka.pl", UserRole.ADMIN);
        this.seed();
    }

    private seed(): void {
        const baseItems = [
            new Book(1, "Ferdydurke Tom 51", "Olga Tokarczuk", "239-23-38607-5"),
            new Book(2, "Wiedzmin Tom 42", "J.K. Rowling", "823-17-51468-2"),
            new Book(3, "Chylka Tom 13", "J.K. Rowling", "800-25-54715-7"),
        ];

        this.items.push(...baseItems);
        this.loans.push(
            new Loan(1, baseItems[0], COPY_STATUS.BORROWED, new Date(), null),
            new Loan(2, baseItems[1], COPY_STATUS.AVAILABLE, new Date(), null),
            new Loan(3, baseItems[2], COPY_STATUS.BORROWED, new Date(), null),
        );
    }

    getLoans(): LibraryResponse[] {
        return this.loans.map(
            (loan) =>
                new LibraryResponse(
                    loan.id,
                    loan.item.getAuthor(),
                    loan.item.tytul,
                    loan.status,
                    loan.from.toISOString(),
                    loan.end ? loan.end.toISOString() : "ACTIVE",
                ),
        );
    }

    getItems(): { id: number; author: string; title: string; isbn: string }[] {
        return this.items.map((item) => ({
            id: item.id,
            author: item.getAuthor(),
            title: item.tytul,
            isbn: item instanceof Book ? item.isbn : "",
        }));
    }

    getLoansHistory(): LibraryResponse[] {
        return this.loans
            .filter((loan) => loan.status !== COPY_STATUS.BORROWED)
            .map(
                (loan) =>
                    new LibraryResponse(
                        loan.id,
                        loan.item.getAuthor(),
                        loan.item.tytul,
                        loan.status,
                        loan.from.toISOString(),
                        loan.end ? loan.end.toISOString() : "ACTIVE",
                    ),
            );
    }

    saveLoans(incomingLoans: unknown): string {
        if (!Array.isArray(incomingLoans)) {
            throw new Error("Niepoprawne dane: oczekiwano tablicy");
        }

        for (const payload of incomingLoans) {
            const item = new Book(
                this.nextItemId++,
                payload.title || "Brak tytulu",
                payload.author || "nieznany autor",
                payload.isbn || "000-00-00000-0",
            );

            this.items.push(item);
            this.loans.push(new Loan(this.nextLoanId++, item, COPY_STATUS.BORROWED, new Date(), null));
        }

        return `Zapisano ${incomingLoans.length} rekordow`;
    }

    updateItemStatus(id: string, status: string): boolean {
        const loan = this.loans.find((entry) => entry.id === Number(id));
        if (!loan) {
            return false;
        }
        loan.status = String(status || "").toUpperCase() || loan.status;
        if (loan.status !== COPY_STATUS.BORROWED && !loan.end) {
            loan.end = new Date();
        }
        return true;
    }

    saveFirstNames(names: unknown): string {
        if (!Array.isArray(names)) {
            throw new Error("Niepoprawne dane: oczekiwano tablicy");
        }

        const counter = new Map<string, number>();
        for (const entry of names) {
            const current = String(entry.firstName || "");
            if (current === "-") {
                break;
            }
            counter.set(current, (counter.get(current) || 0) + 1);
        }

        this.names = names;
        let wynik = "";
        counter.forEach((value, key) => {
            wynik += `key: ${key}, value: ${value}\n`;
        });
        return `Zapisano \n${wynik}`;
    }
}

class Animal { eat() { } }
class Dog extends Animal { bark() { } }

// Typ funkcji, której potrzebujemy:
type DogHandler = (d: Dog) => void;

// 1. Funkcja z typem SZERSZYM (Animal) - POZYCJA KONTRAWARIANTNA
const handleAnimal = (a: Animal) => {
    a.eat(); // To jest bezpieczne dla każdego Psa
};

// 2. Funkcja z typem WĘŻSZYM (Dog)
const handleDog = (d: Dog) => {
    d.bark();
};
// TEST PODSTAWIENIA:
let logger: DogHandler;

logger = handleAnimal; // OK! (Kontrawariancja) - Animal jest "większy", więc obsłuży Psa.
logger = handleDog;    // OK! (To ten sam typ)
//logger(new Animal());


const dane = [
    [1, "Anna"],
    [2, "Borys"],
    [3, "Cezary"]
] as const; // 'as const' jest kluczowe dla precyzyjnych typów

// Twój typ wyciągający pierwszy element (ID)
type FirstElement<T> = T extends readonly [infer U, ...any[]] ? U : never;

// Tworzymy unię dostępnych ID: 1 | 2 | 3
type DostepneIDs = FirstElement<typeof dane[number]>;

// REDUCE: Tworzymy mapę ID -> Imię
const mapa = dane.reduce((acc, [id, imie]) => {
    acc[id] = imie;
    return acc;
}, {} as Record<DostepneIDs, string>);
const owoce = ["jabłko", "banan"] as const;

type CoJestWTablicy = typeof owoce[number];
// Wynik: "jabłko" | "banan"


mapa[1]; // Typ: string




const libraryService = new LibraryService();

export default libraryService;

const executedFile = process.argv[1] || "";
const isDirectRun =
    executedFile.endsWith("libraryService.ts") || executedFile.endsWith("libraryService.js");

if (isDirectRun) {

    const t: [number, string][] = [
        [1, "Ferdydurke Tom 51"],
        [2, "Wiedzmin Tom 42"],
        [3, "Chylka Tom 13"],
    ];


    // console.log("Items:");
    // console.log(JSON.stringify(libraryService.getItems(), null, 2));
    // console.log("Loans:");
    // console.log(JSON.stringify(libraryService.getLoans(), null, 2));
}