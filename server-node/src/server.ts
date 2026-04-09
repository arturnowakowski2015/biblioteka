import express from "express";
import cors from "cors";
import libraryRouter from "./routes/libraryRouter";

const app = express();
const PORT = process.env.PORT || 8081;

app.use(
    cors({
        origin: [
            "http://localhost:5173",
            "http://localhost:5174",
            "http://localhost:5175",
            "http://localhost:5176",
        ],
        methods: ["GET", "POST", "PUT", "DELETE"],
    }),
);

app.use(express.json());
app.use("/library", libraryRouter);

app.listen(PORT, () => {
    console.log(`Library API is running on http://localhost:${PORT}`);
});
