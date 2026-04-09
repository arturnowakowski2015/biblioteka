export class Item {
    public id: number;
    public tytul: string;
    constructor(id: number, tytul: string) {
        this.id = id;
        this.tytul = tytul;
    }

    getAuthor(): string {
        return "nieznany autor";
    }

    getId(): number { return this.id; }
}

