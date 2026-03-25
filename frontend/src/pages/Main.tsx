import { useState } from "react";
import { Outlet, Link, useNavigate } from 'react-router-dom';

import { User } from "../model/User.tsx";
import { useLibrary } from "../ctx/Authctx.tsx";

const columns = [
    { id: 'id', label: 'ID' },
    { id: 'customer', label: 'Klient' },
];
const data = [
    { "id": 1, "customer": "Jan Kowalski" },
    { "id": 2, "customer": "Anna Nowak" },
    { "id": 3, "customer": "Piotr Wiśniewski" },
    { "id": 4, "customer": "Maria Wójcik" },
    { "id": 5, "customer": "Krzysztof Kowalczyk" },
    { "id": 6, "customer": "Agnieszka Kamińska" },
    { "id": 7, "customer": "Tomasz Lewandowski" },
    { "id": 8, "customer": "Barbara Zielińska" },
    { "id": 9, "customer": "Marcin Woźniak" },
    { "id": 10, "customer": "Magdalena Szymańska" },
    { "id": 11, "customer": "Michał Dąbrowski" },
    { "id": 12, "customer": "Katarzyna Kozłowska" },
    { "id": 13, "customer": "Andrzej Jankowski" },
    { "id": 14, "customer": "Joanna Mazur" },
    { "id": 15, "customer": "Łukasz Kwiatkowski" },
    { "id": 16, "customer": "Małgorzata Krawczyk" },
    { "id": 17, "customer": "Robert Kaczmarek" },
    { "id": 18, "customer": "Elżbieta Piotrowska" },
    { "id": 19, "customer": "Grzegorz Grabowski" },
    { "id": 20, "customer": "Zofia Pawłowska" },
    { "id": 21, "customer": "Paweł Michalski" },
    { "id": 22, "customer": "Beata Nowicka" },
    { "id": 23, "customer": "Adam Adamczyk" },
    { "id": 24, "customer": "Monika Dudek" },
    { "id": 25, "customer": "Artur Wieczorek" },
    { "id": 26, "customer": "Justyna Jabłońska" },
    { "id": 27, "customer": "Damian Majewski" },
    { "id": 28, "customer": "Sylwia Olszewska" },
    { "id": 29, "customer": "Radosław Stępień" },
    { "id": 30, "customer": "Natalia Jaworska" }
]

const menuItems = [
    { id: 'pulpit', label: 'Pulpit', icon: '🏠' },
    { id: 'raporty', label: 'Raporty', icon: '📊' },
    { id: 'uzytkownicy', label: 'Użytkownicy', icon: '👥' },
    { id: 'ustawienia', label: 'Ustawienia', icon: '⚙️' },
];


const Main = () => {
    const [isOpen, setIsOpen] = useState(false);
    const ctx = useLibrary();
    const navigate = useNavigate();
    // Funkcja przełączająca stan
    const toggleMenu = () => {
        setIsOpen(!isOpen);
    };
    return (

        <div className={`container  `}>
            <nav className={`sidebar ${isOpen ? 'active' : ''}`}>

                <button className="toggle-btn" onClick={toggleMenu}>
                    {isOpen ? '✖' : '☰'}
                </button>

                {/* Menu - klasa 'active' dodawana warunkowo */}
                <div className="flex-direction-column">

                    {ctx.currentUser.status === "admin" ?
                        <nav>
                            <Link to="admin"><span className="icon">🏠</span> {!isOpen && "Admin"}</Link>
                            <Link to="books"><span className="icon">👤</span> {!isOpen && "Books"}</Link>
                        </nav>
                        :
                        <nav>
                            <Link to="order"><span className="icon">👤</span> {!isOpen && "Order"}</Link>
                            <Link to="books"><span className="icon">👤</span> {!isOpen && "Books"}</Link>
                        </nav>
                    }
                </div>
            </nav>
            <main className={`content ${isOpen ? "pushed" : ""}`}    >

                <header className="content-header">
                    <h1>Podsumowanie Analityczne</h1>
                    <button className="btn-primary">Generuj Raport</button>
                    <div className="who" onClick={() => navigate("auth")}>{ctx.currentUser.status}</div>
                </header>



                <h3>Ostatnie Transakcje</h3>
                <Outlet />

            </main>
        </div>

    );
};

export default Main;
