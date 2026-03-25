import { Item } from "./Item";

export class Audiobook extends Item {
    private timeLength: number;
    private lector: string;
    constructor(id: number, title: string, timeLength: number, lector: string) {
        super(id, title)
        this.timeLength = timeLength;
        this.lector = lector;
    }
    getCreator(): string {
        return this.lector;
    }


}