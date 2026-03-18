import { Item } from "./Item.tsx";

export class NewsPaper extends Item {
    numerWydania: number;
    dataPublikacji: Date;
    constructor(id: number, tytul: string, numer: number, dataPublikacji: Date) {
        super(id, tytul);
        this.numerWydania = numer;
        this.dataPublikacji = dataPublikacji;
    }
}