import express from "express";
import { getCommandHistoryByQuery } from "../controllers/history.controller";
import { authenticateToken, authorize } from "../middleware";
import { getCommandByqueryAuth } from "../utils/roles-definition";
export const Router_history = express.Router()

//Router_history.post("/getCommandHistoryByQuery", getCommandHistoryByQuery);
// Router_history.get("/:id" , getCommandByClientId);
Router_history.get("/getCommandHistoryByQuery/:clientId", getCommandHistoryByQuery);

