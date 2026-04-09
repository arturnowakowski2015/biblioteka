export class LibraryResponse {
    loanId: number;
    author: string;
    tytul: string;
    status: string;
    createdAt: string;
    dueDate: string;
    label: string;

    constructor(
        loanId: number,
        author: string,
        tytul: string,
        status: string,
        createdAt: string,
        dueDate: string,
        label: string = "Termin zwrotu",
    ) {
        this.loanId = loanId;
        this.author = author;
        this.tytul = tytul;
        this.status = status;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.label = label;
    }
}
