// // ❌ KLASA ŁAMIĄCA SOLID (The "God Object")


// interface IDatabase {
//     save(data: any): void;
// }
// class Store {
//     constructor(private base: IDatabase) { }
//     purchase(product: string) {
//         this.base.save(product);
//     }
// }
// class MySQLDatabase implements IDatabase {
//     save(data: any): void {
//         console.log("Zapisano do MySQL: ", data);
//     }
// }
// class Communication { }
// class SendGridEmailer {
//     send(email: string, message: string) {
//         console.log(`Wysłano email do ${email}: ${message}`);
//     }
// }
// interface Validator {
//     validate(item: string): boolean;
// }
// class EmailValidator implements Validator {
//     validate(item: string): boolean {
//         return item.includes("@");
//     }
// }
// class Processor {
//     private validator: Validator[] = [];
//     constructor(validator: Validator) {
//         this.validator.push(validator);
//     }
//     checkItems(items: string[]) {
//         this.validator.forEach(v => {
//             class UserProcessor {
//                 private validator: Validator[];
//                 constructor(validator: Validator) {
//                     this.validator.push(validator);
//                 }

//                 process(user: { id: number; role: string; email: string }) {
//                     // 1. Łamanie SRP (Single Responsibility):
//                     // Klasa waliduje, liczy rabat, zapisuje do bazy i wysyła maile.
//                     if (!this.validator.validate(user.email)) {
//                         console.log("Nieprawidłowy email!");
//                         return;
//                     }
//                     // 2. Łamanie OCP (Open/Closed):
//                     // Jeśli dojdzie nowa rola (np. 'VIP'), musisz dopisać kolejny 'if' w środku tej klasy.
//                     let discount = 0;
//                     if (user.role === 'ADMIN') discount = 0.20;
//                     else if (user.role === 'MANAGER') discount = 0.10;

//                     // 3. Łamanie DIP (Dependency Inversion):
//                     // Klasa tworzy konkretne narzędzia przez 'new'. Nie możesz ich podmienić w testach.
//                     const db = new MySQLDatabase();
//                     db.save(user, discount);

//                     const emailer = new SendGridEmailer();
//                     emailer.send(user.email, "Witaj w systemie!");
//                 }

//                 // 4. Łamanie ISP (Interface Segregation):
//                 // Zmuszamy każdego użytkownika do posiadania metod, których może nie potrzebować.
//                 generateAdminReport() { /* ... */ }
//             }
