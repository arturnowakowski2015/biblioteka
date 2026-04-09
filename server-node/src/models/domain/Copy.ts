import { Item } from "./Item"

export class Copy<T extends Item> {
    public id: number;
    public item: T;

    constructor(id: number, item: T) {
        this.id = id;
        this.item = item;
    }
    getId(): number { return this.id; }
    getItem(): T { return this.item; }

}