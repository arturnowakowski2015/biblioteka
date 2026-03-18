export class LoansHistory {
    private id: number;
    private baseId: number;
    private userId: number;
    private from: number;
    private end?: number;
    constructor(id: number, bookId: number, userId: number, from: number) {
        this.id = id;
        this.baseId = bookId;
        this.userId = userId;
        this.from = from;
    }

}