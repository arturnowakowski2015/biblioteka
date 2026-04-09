interface User {
    id: number;
    name: string;
}
interface U {
    set: User[];
}

const users: U = {
    set: [
        { id: 1, name: "Alice" },
        { id: 2, name: "Bob" },
        { id: 3, name: "Charlie" }
    ]
};



// 1. Problemy ze strukturami danych(Frontend & Backend)
// Wyszukiwanie w dużych zbiorach: Zamiast array.find()(wolne



// ), zamień tablicę na Map lub Object / Dictionary(błyskawiczne



// Unikalność: Zamiast filter + indexOf, użyj struktury Set.
// Łączenie danych (Joins): Masz listę Posts i listę Users. Nie rób .find() użytkownika dla każdego posta w pętli. Najpierw zrób mapę użytkowników po ID, a potem przypisz ich do postów jednym przejściem pętli.
// 2. Problemy z wydajnością UI (Frontend)
// Debouncing & Throttling: Optymalizacja zdarzeń takich jak wpisywanie w searchbox (nie strzelaj do API przy każdym znaku) lub scrollowanie.
// Memoizacja: Unikanie ciężkich obliczeń przy każdym renderowaniu komponentu (w React: useMemo, useCallback, React.memo).
// Virtual Scrolling: Jak wyświetlić 100 000 wierszy w tabeli, żeby przeglądarka nie "zdechła"? (Renderujesz tylko to, co widać na ekranie).
// 3. Problemy z komunikacją (API & Network)
// N+1 Problem (Kluczowe!): Na backendzie (ORM) lub frontendzie (wielokrotne fetche). Pobierasz 10 postów, a potem robisz 10 osobnych zapytań o autorów. Rozwiązanie: Eager Loading lub jeden zbiorczy endpoint.
// Pagination & Infinite Scroll: Przesyłanie 5MB JSON-a na raz to błąd. Musisz umieć wdrożyć stronicowanie oparte na offset lub cursor.
// Caching: Kiedy trzymać dane w LocalStorage, kiedy w Redis, a kiedy polegać na nagłówkach ETag.
// 4. Problemy z asynchronicznością
// Race Conditions: Co jeśli użytkownik kliknie przycisk "Pobierz" dwa razy, a druga odpowiedź przyjdzie szybciej niż pierwsza? (Użycie AbortController).
// Parallel vs Sequential: Kiedy użyć await w pętli (powolne), a kiedy Promise.all() (szybkie, równoległe).
// 5. Problemy z bazą danych (SQL/NoSQL)
// Indeksowanie: Dlaczego zapytanie trwa 5 sekund? Brak indeksu na kolumnie, po której filtrujesz (WHERE).
// Niewłaściwe typy danych: Przechowywanie dat jako stringi zamiast Date/Timestamp (uniemożliwia sortowanie i szybkie filtrowanie).
// Twoja "checklist" Mida:




// Prosta funkcja //
// , która działa tak samo dobrze dla tablicy imion (string), jak i tablicy cen (number).
function getRandomElement<T>(list: T[]): T {
    const randomIndex = Math.floor(Math.random() * list.length);
    return list[randomIndex];
}