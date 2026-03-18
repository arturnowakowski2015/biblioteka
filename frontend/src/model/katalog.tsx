import { Item } from "./Item.tsx";
import { Copy } from "./Copy.tsx";
import type { User } from "./User.tsx";
export class Katalog {
    Set: Item[];
    Setofcopies: Copy[];
    nazwa: string;
    opis: string;
    constructor(Set: Item[], Setofcopies: Copy[], nazwa: string, opis: string,) {
        this.Set = Set;
        this.Setofcopies = Setofcopies;
        this.nazwa = nazwa;
        this.opis = opis;
    }
    registerItem(book: Item, user: User) {
        if (user.status === "admin") {
            const exist = this.Set.some((item) => item.tytul === book.tytul)
            if (!exist) {
                this.Set.push(book);
                const arrids: number[] = this.Setofcopies.map((copy) => copy.id);
                const book_id: number = arrids.length > 0 ? Math.max(...arrids) + 1 : 1;
                this.Setofcopies.push(new Copy(book_id, book.id));
            }
        }
    }
    getItem(id: number): Item | undefined {
        return
        this.Setofcopies.filter(item => item.id === id)
    }

    getAllItems(): Item[] | undefined {
        return this.Set
    }
    getAllofCopies(): Copy[] | undefined {
        return this.Setofcopies
    }
}