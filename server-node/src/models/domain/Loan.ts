import { Item } from "./Item";

export class Loan {
    public id: number;
    public item: Item;
    public status: string;
    public from: Date;
    public end: Date | null;

    constructor(id: number, item: Item, status: string, from: Date, end: Date | null) {
        this.id = id;
        this.item = item;
        this.status = status;
        this.from = from;
        this.end = end;
    }
}

