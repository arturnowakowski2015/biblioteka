import { Copy } from "./Copy";
import { Item } from "./Item";

class Catalog<T extends Item> {
    private copys: Map<string, Copy<T>[]> = new Map();
    getCopies(id: string): Copy<T>[] | undefined { return this.copys.get(id); }
    addCopy(item: Copy<T>): void {
        const key = item.getItem().constructor.name;
        if (!this.copys.has(key)) {
            this.copys.set(key, []);
        }
        this.copys.get(key)!.push(item);
    }
}
