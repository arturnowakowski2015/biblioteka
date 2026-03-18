import { useState } from "react";
import { Table } from "../components/ui/table/Table.tsx";
import { useLibrary } from "../ctx/Authctx.tsx";
import { Audiobook } from "../model/Audiobook.tsx";
import { Book } from "../model/Book.tsx";
import { Item } from "../model/Item.tsx";
import { Katalog } from "../model/katalog.tsx";
import { NewsPaper } from "../model/NewsPaper.tsx";
export const Books = () => {
    const books1: Item[] = [
        new Book(1, 'Ferdydurke Tom 5', 'Olga Tokarczuk', '239-23-38607-5'),
        new Book(2, 'Wiedźmin Tom 4', 'J.K. Rowling', '823-17-51468-2'),
        new Book(3, 'Chyłka Tom 1', 'J.K. Rowling', '800-25-54715-7'),
        new Book(4, 'Chyłka Tom 5', 'J.K. Rowling', '259-50-62313-4'),
        new Book(5, 'Lalka Tom 3', 'Adam Mickiewicz', '526-28-19266-9'),
        new Book(6, 'Lalka Tom 4', 'Remigiusz Mróz', '448-72-75219-7'),
        new Book(7, 'Harry Potter Tom 2', 'Adam Mickiewicz', '924-11-71919-3'),
        new Book(8, 'Lśnienie Tom 3', 'Harlan Coben', '328-87-97854-9'),
        new Book(9, 'Harry Potter Tom 3', 'Harlan Coben', '773-77-82382-7'),
        new Book(10, 'Lalka Tom 3', 'Adam Mickiewicz', '785-80-74441-4'),
        new Book(11, 'Bieguni Tom 4', 'Olga Tokarczuk', '973-98-79160-5'),
        new Book(12, 'Wiedźmin Tom 1', 'Olga Tokarczuk', '219-27-59581-7'),
        new Book(13, 'Chyłka Tom 2', 'Adam Mickiewicz', '399-46-58813-6'),
        new Audiobook(14, 'Ferdydurke Tom 2', 6000, 'Andrzej Sapkowski'),
        new Audiobook(14, 'Ferdydurke Tom 2', 6000, 'Andrzej Sapkowski'),
        new NewsPaper(16, 'Wiedźmin Tom 1', 22, new Date('2025-01-25')),
        new Book(17, 'Pan Tadeusz Tom 3', 'Harlan Coben', '428-52-15875-0'),
        new Book(18, 'Bieguni Tom 5', 'Stephen King', '391-54-98226-4'),
        new Book(19, 'Kordian Tom 1', 'Andrzej Sapkowski', '650-78-50407-8'),
        new Book(20, 'Potop Tom 4', 'Remigiusz Mróz', '161-35-86093-8')
    ];
    const [activeTab, setActiveTab] = useState<string>("books");

    const ctx = useLibrary();
    const katalog = ctx.manager.getKatalog();
    for (const book of books1) {
        katalog.registerItem(book, ctx.currentUser);
    }
    const books: Item[] = katalog.Set;

    const uniquekes = books.reduce((acc, item) => {
        const cl = item.constructor.name;
        acc[cl] = [... new Set(Object.keys(item))];
        return acc;
    }, {} as Record<string, string[]>);
    const headers = books.length > 0 ? (Object.keys(books[0]) as (keyof Item)[]) : [];

    return (
        <>
            <div className="tabs-header" style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
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

            <ul>
                <li className="menu1"><a href="#">Odsyłacz 1</a></li>
                <li className="menu1"><a href="#">Odsyłacz 2</a></li>
                <li className="menu1"><a href="#">Odsyłacz 3</a></li>
            </ul>

            <Table headers={headers} data={books} />
        </>
    )
}