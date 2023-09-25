import { Request, Response } from "express";
import * as HistoryService from "../services/history.service";

export const getCommandHistoryByQuery = async (req: Request, res: Response) => {
  try {
    const clientId = req.params.clientId;  // Extract clientId from the path parameter
    const result = await HistoryService.getCommandHistoryByQuery(clientId);
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message);
  }
};
