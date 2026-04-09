export class User {
    public id: number;
    public name: string;
    public surname: string;
    public email: string;
    public role: string;

    constructor(id: number, name: string, surname: string, email: string, role: string) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.role = role;
    }
}
