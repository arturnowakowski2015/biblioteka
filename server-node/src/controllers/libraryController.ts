import { Request, Response } from "express";
import libraryService from "../services/libraryService";

export function getLoans(_req: Request, res: Response): void {
    res.json(libraryService.getLoans());
}

export function getItems(_req: Request, res: Response): void {
    res.json(libraryService.getItems());
}

export function getLoansHistory(_req: Request, res: Response): void {
    res.json(libraryService.getLoansHistory());
}

export function saveLoans(req: Request, res: Response): void {
    try {
        const message = libraryService.saveLoans(req.body);
        res.status(200).send(message);
    } catch (error) {
        res.status(500).send(`Blad zapisu: ${(error as Error).message}`);
    }
}

export function saveNames(req: Request, res: Response): void {
    try {
        const message = libraryService.saveFirstNames(req.body);
        res.status(200).send(message);
    } catch (error) {
        res.status(500).send(`Blad zapisu: ${(error as Error).message}`);
    }
}

export function updateItem(req: Request, res: Response): void {
    const itemId = String(req.params.id ?? "");
    const status = typeof req.body?.status === "string" ? req.body.status : "";
    const updated = libraryService.updateItemStatus(itemId, status);
    res.status(200).json(updated);
}
