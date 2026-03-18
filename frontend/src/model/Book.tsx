import { Item } from "./Item.tsx";
export class Book extends Item {
    autor: string;
    isbn: string;
    constructor(id: number, tytul: string, autor: string, isbn: string) {
        super(id, tytul);
        this.autor = autor;
        this.isbn = isbn;
    }
}