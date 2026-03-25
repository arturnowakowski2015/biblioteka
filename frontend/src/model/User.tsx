
export type UserStatus = "admin" | "user";

export class User {
    id: number;
    name: string;
    surname: string;
    email: string;
    status: UserStatus;
    constructor(id: number, name: string, surname: string, email: string, status: UserStatus) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.status = status;
    }
    public changeUser(id: number, name: string, surname: string, email: string, status: UserStatus): void {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.status = status;
    }
}