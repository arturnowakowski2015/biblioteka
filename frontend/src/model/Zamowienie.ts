

interface IVal {
    validate(item: string, cena: number): number;
}
export class CzyElektronika implements IVal {
    validate(item: string, cena: number): number {
        if (item.includes("Elektronika")) return cena * 0.1;
        return 0;
    }
}
class CzyZywnosc implements IVal {
    validate(item: string, cena: number): number {
        if (item.includes("Zywnosc")) return cena * 0.05;
        return 0;
    }
}
interface IBaza {
    zamawiam(id: number, ck: number): Promise<void>;
}
type Sterownik = { save: (id: number, ck: number) => Promise<void> };
class Mysql implements IBaza {
    private sterownik: Sterownik
    constructor(sterownik: Sterownik) {
        this.sterownik = sterownik;
    }
    async zamawiam(id: number, ck: number) {
        console.log("saving to mysql");
        await this.sterownik.save(id, ck);
    }
}

interface INot {
    send(item: string): void;
}
export class EmailNotification implements INot {
    send(item: string) {
        console.log("sending email");
    }
}
class SmsNotification implements INot {
    send(item: string) {
        console.log("sending sms");
    }
}
export class NotificationStrategie {
    private strategie: INot[] = [];
    constructor(strategie: INot[]) {
        this.strategie = strategie
    }
    inform(text: string) {
        this.strategie.forEach(item => item.send(text))
    }
}


//dekorator o zalogowanie
class LoggingDatabaseDecorator implements IBaza {
    private decoratedBase: IBaza
    constructor(decoratedBase: IBaza) {
        this.decoratedBase = decoratedBase;
    }

    async zamawiam(id: number, ck: number): Promise<void> {
        console.log(`[LOG]: Rozpoczynam zapis zamówienia ${id}...`);
        const start = Date.now();

        // Wywołujemy oryginalną metodę
        await this.decoratedBase.zamawiam(id, ck);

        const duration = Date.now() - start;
        console.log(`[LOG]: Zapis zakończony sukcesem w ${duration}ms. Kwota: ${ck}`);
    }
}





export class SystemObslugiZamowien {
    // Łamie SRP: Klasa zajmuje się wszystkim (logika, DB, e-mail, raporty)
    // Łamie OCP: Każdy nowy typ produktu wymaga zmiany metody ObliczRabaty (if/else)
    // Łamie DIP: Klasa sama tworzy instancję bazy danych (new SqlDatabase)
    private val: IVal;
    private store: IBaza;
    private notification: NotificationStrategie;
    //private IPres pres;
    constructor(val: IVal, store: IBaza, notificator: NotificationStrategie,
        // private pres: IPres
    ) { this.val = val; this.store = store; this.notification = notificator; }

    ProcesujZamowienie(id: number, typProduktu: string, cena: number) {
        let cenaKoncowa = cena - this.val.validate(typProduktu, cena);

        this.store.zamawiam(id, cenaKoncowa);

        // 3. Wysyłka powiadomienia (Zasada SRP - logika komunikacji wewnątrz zamówienia)
        this.notification.inform("cena koncowa");
        // 4. Generowanie raportu (Zasada SRP - logika prezentacji)
        //pres.present("cena koncowa")
    }
}



const sterownikDummy = { save: async () => { } };

// 1. Tworzymy konkretną bazę
const realDb = new Mysql(sterownikDummy);

// 2. "Dekorujemy" ją logowaniem
const dbWithLogging = new LoggingDatabaseDecorator(realDb);

// 3. Wstrzykujemy do systemu
const system = new SystemObslugiZamowien(
    new CzyElektronika(),
    dbWithLogging, // <--- System widzi IBaza, ale to jest już udekorowany obiekt
    new NotificationStrategie([new EmailNotification()])
);

system.ProcesujZamowienie(1, "Laptop Elektronika", 3000);
