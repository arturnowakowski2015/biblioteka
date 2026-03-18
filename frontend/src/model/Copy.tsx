type CopyStatus = "available" | "borrowed" | "reserved";
export class Copy {
    id: number = 0;
    itemId: number = 0;
    status: CopyStatus = 'available';
    constructor(id: number, itemId: number) {
        this.id = id;
        this.itemId = itemId;
    }
}