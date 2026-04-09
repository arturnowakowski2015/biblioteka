import { Router } from "express";
import * as controller from "../controllers/libraryController";

const router = Router();

router.get("/loans", controller.getLoans);
router.get("/items", controller.getItems);
router.get("/loans-history", controller.getLoansHistory);
router.post("/imiona", controller.saveNames);
router.post("/save", controller.saveLoans);
router.put("/items/:id", controller.updateItem);

export default router;
