import type { Copy } from "./Copy";
import type { User } from "./User";

export class Loans {
    id: number;
    whoHas: User;
    whatHas: Copy;
    constructor(id: number, whoHas: User, whatHas: Copy) {
        this.id = id;
        this.whoHas = whoHas;
        this.whatHas = whatHas;
    }
}