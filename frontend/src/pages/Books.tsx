import { useMemo, useState } from "react";
import { Table } from "../components/ui/table/Table.tsx";
import { useLibrary } from "../ctx/Authctx.tsx";
import { Audiobook } from "../model/Audiobook.tsx";
import { Book } from "../model/Book.tsx";
import { Item } from "../model/Item.tsx";
import { NewsPaper } from "../model/NewsPaper.tsx";
import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom'; import axios from 'axios';



import { useMutation, useQueryClient } from '@tanstack/react-query';
import Random from "../services/Random.ts";



interface Imie {
    firstName: string;
}

interface Items {
    [key: string | number]: any; // Pozwala na dostęp do dowolnych kluczy
}
const fetchData = async (url: string) => {
    const { data } = await axios.get(url, { timeout: 5000 });
    return data;
};
const updateItem = async (updatedItem: Items) => {
    console.log(JSON.stringify(updatedItem));
    await axios.put(`http://localhost:8081/library/items/${Object.values(updatedItem)[0]}`, updatedItem);
}

export const Books = () => {


    const books1: Item[] = [
        new Book(1, 'Ferdydurke Tom 51', 'Olga Tokarczuk', '239-23-38607-5'),
        new Book(2, 'Wiedźmin Tom 42', 'J.K. Rowling', '823-17-51468-2'),
        new Book(3, 'Chyłka Tom 13', 'J.K. Rowling', '800-25-54715-7'),
        new Book(4, 'Chyłka Tom 54', 'J.K. Rowling', '259-50-62313-4'),
        new Book(5, 'Lalka Tom 35', 'Adam Mickiewicz', '526-28-19266-9'),
        new Book(6, 'Lalka Tom 46', 'Remigiusz Mróz', '448-72-75219-7'),
        new Book(7, 'Harry Potter Tom 27', 'Adam Mickiewicz', '924-11-71919-3'),
        new Book(8, 'Lśnienie Tom 38', 'Harlan Coben', '328-87-97854-9'),
        new Book(9, 'Harry Potter Tom 39', 'Harlan Coben', '773-77-82382-7'),
        new Book(10, 'Lalka Tom 311', 'Adam Mickiewicz', '785-80-74441-4'),
        new Book(11, 'Bieguni Tom 412', 'Olga Tokarczuk', '973-98-79160-5'),
        new Book(12, 'Wiedźmin Tom 113', 'Olga Tokarczuk', '219-27-59581-7'),
        new Book(13, 'Chyłka Tom 214', 'Adam Mickiewicz', '399-46-58813-6'),
        new Audiobook(14, 'Ferdydurke Tom 215', 6000, 'Andrzej Sapkowski'),
        new Audiobook(14, 'Ferdydurke Tom 216', 6000, 'Andrzej Sapkowski'),
        new NewsPaper(16, 'Wiedźmin Tom 117', 22, new Date('2025-01-25')),
        new Book(17, 'Pan Tadeusz Tom 318', 'Harlan Coben', '428-52-15875-0'),
        new Book(18, 'Bieguni Tom 519', 'Stephen King', '391-54-98226-4'),
        new Book(19, 'Kordian Tom 120', 'Andrzej Sapkowski', '650-78-50407-8'),
        new Book(20, 'Potop Tom 421', 'Remigiusz Mróz', '161-35-86093-8')
    ];

    const tabConfigs = [
        {
            key: "loans",
            label: "Wypozyczenia",
            endpoint: "http://localhost:8081/library/loans",
            state: [1, 0, 0] as number[],
        },
        {
            key: "items",
            label: "dostepne Ksiazki",
            endpoint: "http://localhost:8081/library/items",
            state: [0, 1, 0] as number[],
        },
        {
            key: "history",
            label: "Odsyła cz 3",
            endpoint: "http://localhost:8081/library/loans-history",
            state: [0, 0, 1] as number[],
        },
    ];
    const [activeTab, setActiveTab] = useState<string>("books");
    const [tab, setTab] = useState<number[]>([1, 0, 0]);
    const selectedTab = tabConfigs.find((item) =>
        item.state.every((value, index) => value === tab[index])) ?? tabConfigs[0];

    const ctx = useLibrary();
    const katalog = ctx.manager.getKatalog();
    for (const book of books1) {
        katalog.registerItem(book, ctx.currentUser);
    }



    const { data, error, isLoading } = useQuery<Items[]>({
        queryKey: ['library-data', selectedTab.key],
        queryFn: () => fetchData(selectedTab.endpoint),
        staleTime: 1000 * 60 * 5, // Dane są "świeże" przez 5 minut
        retry: 1,
        refetchOnWindowFocus: false,
    });
    // Używamy useMemo, aby nie przeliczać kluczy przy każdym renderze

    const queryClient = useQueryClient();
    interface ItemRequest {
        id: number;
        title: string;
        author: string,
        isbn: string
    }


    const mutation = useMutation({
        mutationFn: (newItems: ItemRequest[]) => {
            return axios.post('http://localhost:8081/library/save', newItems);
        },
        // To jest kluczowe:
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['library-data'] });
        }
    });

    const addNames = useMutation({
        mutationFn: (newItems: Oni[]) => {
            return axios.post('http://localhost:8081/library/imiona', newItems);
        },
        // To jest kluczowe:
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['library-data'] });
        }
    });


    const [updatedUser, setUpdatedUser] = useState<Items | null>(null);
    const upDateMutation = useMutation({
        mutationFn: (updatedUser: Items) => updateItem(updatedUser),

        onMutate: async (newUser) => {
            await queryClient.cancelQueries({ queryKey: ['library-data'] });

            const previousUser = queryClient.getQueryData<Items[]>(['library-data', selectedTab.key]);

            // Optymistycznie aktualizujemy TYLKO jeden wiersz w tablicy
            queryClient.setQueryData<Items[]>(['library-data', selectedTab.key], (old) => {
                if (!old) return [];

                return old.map((item) =>
                    // Zakładam, że Twoim ID jest loanId. Jeśli nie, użyj właściwego klucza
                    item.loanId === newUser.loanId ? { ...item, ...newUser } : item
                );
            });

            return { previousUser };
        },



        // 2. Jeśli serwer zwróci błąd, przywróć stare dane
        onError: (err, newUser, context) => {
            if (context?.previousUser) {
                queryClient.setQueryData(['library-data', selectedTab.key], context.previousUser);
            }
        },

        // 3. Po zakończeniu (sukces lub błąd) odśwież dane z serwera, by mieć pewność synchronizacji
        onSettled: () => {
            queryClient.invalidateQueries({ queryKey: ['library-data'] });
        },
    });


    const addItemMutation = useMutation({
        mutationFn: (newItem: Item) => axios.post('/items', newItem),
        onMutate: async (newItem) => {
            await queryClient.cancelQueries({ queryKey: ['items'] });
            const previousItems = queryClient.getQueryData<Item[]>(['items']);

            // Optymistycznie dodajemy nowy element do tablicy
            queryClient.setQueryData<Item[]>(['items'], (old) =>
                old ? [newItem, ...old] : [newItem]
            );

            return { previousItems };
        },
        onError: (err, newItem, context) => {
            queryClient.setQueryData(['items'], context?.previousItems);
        },
        onSettled: () => {
            queryClient.invalidateQueries({ queryKey: ['items'] });
        },
    });


    const handleSubmit = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        console.log(JSON.stringify(katalog.Setofcopies));
        console.log(JSON.stringify(katalog.Set));

        const datatosave = katalog.Set.map((item) => {
            //
            const itemAuthor = item.getCreator();

            return {
                id: item.id,
                author: itemAuthor || "Unknown",
                title: item.tytul,
                isbn: Random.getRandomInt(1000000000, 9999999999).toString(),
            };
        });
        const imiona: Imie[] = ["a", "s", "d", "f", "g", "a", "h", "j", "k", "l"].map((name) => ({ firstName: name }));
        const imieimie: Map<string, number> = new Map();
        imiona.forEach((imie) => {
            imieimie.set(imie.firstName, (imieimie.get(imie.firstName) || 0) + 1);
        }
        )
        reverseNames(imieimie)
        //addNames.mutate(imiona);
        // mutation.mutate(datatosave);
        // Odśwież dane po mutacji
    };

    const reverseNames = (imieimie: Map<string, number>) => {
        const rev: Map<number, string[]> = new Map();
        imieimie.forEach((count, name) => {
            if (!rev.has(count)) rev.set(count, [])
            else rev.get(count)?.push(name)

        });


        [...rev].sort((a, b) => b[0] - a[0]).forEach(([count, names]) => {
            console.log(`Count: ${count}, Names: ${names.join(", ")}`);
        });
    }

    interface User { id: number; name: string }
    const user: User = { id: 1, name: "Jan" };



    const headers = data && data.length > 0 ? (Object.keys(data[0]) as (keyof Items)[]) : [];

    interface Oni {
        on: string;
        ona: string;
    }
    const [imie, setImie] = useState<Oni[]>([{ on: "", ona: "" }]);
    const handleSubmitImie = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        // addNames.mutate(imie);
        setImie([{ on: "", ona: "" }])
        // TS widzi 'entries' jako [string, any][]
        Object.entries(user).forEach(([key, value]) => {
            console.log("key: " + key + ", value: " + value);
            // key jest typu string, a nie "id" | "name"
        });
    }
    const u = { a: "dd", b: 123, c: true };
    const u2 = { ...u }; // nowy obiekt (shallow copy)
    const uu = { ...u, a: "zmienione" }; // nowy obiekt z nadpisanym polem 'a'
    Object.assign(u2, { a: "zmienione" });

    console.log(u.a);  // "dd"
    console.log(u2.a); //
    function printId(id: number | string) {
        if (typeof id === "string") {
            console.log(id.toUpperCase()); // Tutaj id jest traktowane jako string
        } else {
            console.log(id); // Tutaj id jest traktowane jako number
        }
        // Wywołanie z liczbą
    } printId("abc"); // Wywołanie z stringiem
    printId(123);

    type Color = string | [number, number, number];
    type Palette = Record<string, Color>;

    // 1. Użycie ADNOTACJI
    const palette1: Palette = {
        red: [255, 0, 0],
        green: "green"
    }

    // BŁĄD: TypeScript widzi 'red' tylko jako string | [number, number, number]
    // Nie wie na 100%, że to tablica, więc nie pozwala na metody tablicowe bez rzutowania.
    if (Array.isArray(palette1.red)) {
        palette1.red.map(x => x);
    }

    interface User {
        id: number;
        name: string;
        email: string;
        role: 'admin' | 'user';
    }

    const firstCol = 0;
    console.log(`First column: ${firstCol}`); // Błąd! Index 7 jest poza zakresem tablicy, ale TypeScript tego nie wykryje, bo widzi tylko typ string | number | symbol.
    const roles = ["admin", "user"] as const;

    type RoleUnion = (typeof roles)[number];
    const userRole1: RoleUnion = "admin";
    const role1s = { "admin": "admin", "user": " user" } as const;

    type RoleUnion2 = (keyof typeof role1s)[];
    const userRole2: RoleUnion = "admin";

    function List<T>({ items, renderItem }: {
        items: T[],
        renderItem: (item1: T) => React.ReactNode
    }) {
        return <div>{items.map(renderItem)}</div>;
    }













    // 2. Model danych (np. stan Twojej aplikacji)
    interface UserState {
        id: number;
        profile: {
            firstName: string;
            lastName: string;
            settings: {
                theme: 'light' | 'dark';
                notifications: boolean;
            };
        };
        preferences: string[];
    }
    type DeepPartial<T> = {
        [P in keyof T]?: T[P] extends object ? DeepPartial<T[P]> : T[P];
    };

    class UserStore {
        private state: UserState = {
            id: 1,
            profile: {
                firstName: "Jan",
                lastName: "Kowalski",
                settings: { theme: 'dark', notifications: true }
            },
            preferences: ["js", "ts"]
        };

        // Implementacja logiki Deep Merge
        private merge<T>(target: T, source: DeepPartial<T>): T {
            const output = { ...target };

            for (const key in source) {
                const sourceValue = source[key];
                const targetValue = target[key];

                // Jeśli oba są obiektami (i nie tablicami), wchodzimy głębiej
                if (
                    sourceValue && typeof sourceValue === 'object' && !Array.isArray(sourceValue) &&
                    targetValue && typeof targetValue === 'object' && !Array.isArray(targetValue)
                ) {
                    output[key] = this.merge(targetValue, sourceValue as DeepPartial<typeof targetValue>);
                } else {
                    // W przeciwnym razie nadpisujemy wartość (prymitywy, tablice)
                    output[key] = sourceValue as T[Extract<keyof T, string>];
                }
            }

            return output;
        }

        public updateState(changes: DeepPartial<UserState>) {
            this.state = this.merge(this.state, changes);
            console.log("Nowy stan:", this.state);
        }
    }

    // UŻYCIE:
    const store = new UserStore();

    // Zmieniamy tylko notifications, reszta (theme, firstName) zostaje bez zmian
    store.updateState({
        profile: {
            settings: { notifications: false }
        }
    });










    function pluck<T, K extends keyof T>(obj: T, key: K): T[K] {
        return obj[key];
    }

    // --- TEST ---

    const car = {
        brand: "Tesla",
        year: 2023,
        isElectric: true
    };

    const brand = pluck(car, "brand"); // Powinno być typu 'string'
    const year = pluck(car, "year");   // Powinno być typu 'number'


    type Getters<T> = {
        [K in keyof T as `get${Capitalize<string & K>}`]: () => T[K]
    };

    interface User1 {
        name: string;
        age: number;
    }

    // Wynik: { getName: () => string; getAge: () => number; }
    type UserGetters = Getters<User1>;
    const userGetters: UserGetters = {
        getName: () => "Alice",
        getAge: () => 30
    };

    if (isLoading) return <span>
        <List
            items={[{ 2: "dd" }, { 2: "qk" }]}
            renderItem={(n) => <div key={n[2]}>Liczba: {n[2]}</div>}
        />{userGetters.getAge()}Ładowanie...</span>;
    if (error) return <span>Błąd: {error.message}</span>;

    return (
        <>
            <div className="tabs-header" style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
                <form onSubmit={(e: React.FormEvent<HTMLFormElement>) => { handleSubmitImie(e); }}>
                    <input type="text" value={imie[0].on} onChange={(e) =>
                        setImie(prev => prev.map((t, i) => i === 0 ? { ...t, on: e.target.value } : t))} />

                    <input type="text" value={imie[0].ona} onChange={(e) =>
                        setImie(prev => prev.map((t, i) => i === 0 ? { ...t, ona: e.target.value } : t))} />
                    <button type="submit">Dodaj Imię</button>
                </form>

                <button onClick={handleSubmit}>load</button>
                <button
                    onClick={() => setActiveTab("books")}
                    style={{ fontWeight: activeTab === "books" ? "bold" : "normal" }}
                >
                    Lista Książek
                </button>
                <button
                    onClick={() => setActiveTab("users")}
                    style={{ fontWeight: activeTab === "users" ? "bold" : "normal" }}
                >
                    Użytkownicy
                </button>
            </div>

            <ul style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
                <li className="menu1"><div style={{ backgroundColor: tab[0] ? "red" : "white" }}
                    onClick={() => { setTab(tabConfigs[0].state); }}>{tabConfigs[0].label}</div></li>
                <li className="menu1"><div style={{ backgroundColor: tab[1] ? "red" : "white" }}
                    onClick={() => { setTab(tabConfigs[1].state); }}>{tabConfigs[1].label}</div></li>
                <li className="menu1"><div style={{ backgroundColor: tab[2] ? "red" : "white" }}
                    onClick={() => { setTab(tabConfigs[2].state); }}>{tabConfigs[2].label}</div></li>
            </ul>

            <Table headers={headers} data={data} handleSubmit={upDateMutation.mutate} />
        </>
    )
}