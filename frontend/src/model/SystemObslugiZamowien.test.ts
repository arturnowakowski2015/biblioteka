import { it, expect } from 'vitest';
import {
    SystemObslugiZamowien, CzyElektronika, EmailNotification,
    NotificationStrategie
} from './Zamowienie';

it('powinien poprawnie naliczyć rabat dla elektroniki', async () => {
    // 1. Setup (Arange)
    const mockDb = { zamawiam: async () => { } };
    const notifier = new NotificationStrategie([new EmailNotification()]);
    const system = new SystemObslugiZamowien(new CzyElektronika(), mockDb, notifier);

    // 2. Punkt przerwania (Breakpoint)
    // Postaw tutaj czerwoną kropkę w VS Code!
    await system.ProcesujZamowienie(1, "Laptop Elektronika", 3000);

    // 3. Sprawdzenie (Assert)
    // Tutaj możesz sprawdzić czy cena końcowa to 2700
});