class Random {
    private static instance: Random;
    private constructor() { }
    public static getInstance(): Random { if (!Random.instance) { Random.instance = new Random(); } return Random.instance; }
    public getRandomInt(min: number, max: number): number {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }
    getArray(range: number): number[] {
        return Array.from({ length: range }, () => this.getRandomInt(0, range - 1));
    }
}

export default Random.getInstance();    