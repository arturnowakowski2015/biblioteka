export class LoansHistory {
    private id: number;
    private baseId: number;
    private userId: number;
    private dueDate: number;
    private returnedDate?: number;
    constructor(id: number, bookId: number, userId: number, dueDate: number) {
        this.id = id;
        this.baseId = bookId;
        this.userId = userId;
        this.dueDate = dueDate;
    }

    public getId(): number {
        return this.id;
    }

    public getBookId(): number {
        return this.baseId;
    }

    public getUserId(): number {
        return this.userId;
    }

    public getDueDate(): number {
        return this.dueDate;
    }

    public getReturnedDate(): number | undefined {
        return this.returnedDate;
    }

    public isReturned(): boolean {
        return this.returnedDate !== undefined;
    }

    public markReturned(returnedDate: number): void {
        this.returnedDate = returnedDate;
    }

}