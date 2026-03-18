export abstract class Item {
    id: number;
    tytul: string;
    constructor(id: number, tytul: string,) {
        this.id = id;
        this.tytul = tytul;
    }
}