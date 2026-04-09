import { Item } from "./Item";



export class Book extends Item {
    public author: string;
    public isbn: string;
    constructor(id: number, tytul: string, author: string, isbn: string) {
        super(id, tytul);
        this.author = author;
        this.isbn = isbn;
    }

    getAuthor(): string {
        return this.author;
    }
}


