class Random {
    private static instance: Random;
    private constructor() { }
    public static getInstance(): Random { if (!Random.instance) { Random.instance = new Random(); } return Random.instance; }
    public getRandomInt(min: number, max: number): number {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }
}

export default Random.getInstance();    