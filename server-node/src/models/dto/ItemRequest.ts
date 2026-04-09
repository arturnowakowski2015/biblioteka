export class ItemRequest {
    id: number;
    author: string;
    title: string;
    isbn: string;

    constructor(id: number, author: string, title: string, isbn: string) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.isbn = isbn;
    }
}
