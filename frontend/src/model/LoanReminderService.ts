import { LoansHistory } from "./LoansHistory";

export interface LoanReminder {
    historyId: number;
    userId: number;
    bookId: number;
    dueDate: number;
    daysLeft: number;
    isOverdue: boolean;
    message: string;
}

export class LoanReminderService {
    public static getUpcomingReminders(
        history: LoansHistory[],
        daysAhead: number,
        now: number = Date.now(),
    ): LoanReminder[] {
        const daysAheadMs = daysAhead * 24 * 60 * 60 * 1000;

        return history
            .filter((entry) => !entry.isReturned())
            .map((entry) => {
                const dueDate = entry.getDueDate();
                const deltaMs = dueDate - now;
                const daysLeft = Math.ceil(deltaMs / (24 * 60 * 60 * 1000));
                const isOverdue = deltaMs < 0;

                return {
                    historyId: entry.getId(),
                    userId: entry.getUserId(),
                    bookId: entry.getBookId(),
                    dueDate,
                    daysLeft,
                    isOverdue,
                    message: this.buildMessage(entry, daysLeft, isOverdue),
                };
            })
            .filter((reminder) => reminder.isOverdue || reminder.dueDate - now <= daysAheadMs)
            .sort((a, b) => a.dueDate - b.dueDate);
    }

    private static buildMessage(entry: LoansHistory, daysLeft: number, isOverdue: boolean): string {
        if (isOverdue) {
            return `Wypozyczenie #${entry.getId()} jest po terminie.`;
        }

        if (daysLeft === 0) {
            return `Wypozyczenie #${entry.getId()} konczy sie dzisiaj.`;
        }

        return `Wypozyczenie #${entry.getId()} konczy sie za ${daysLeft} dni.`;
    }
}
