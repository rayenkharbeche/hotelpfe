import { Request, Response } from "express";
import * as authentificationService from "../services/authentication.service"
import { createApiResponse } from "../utils/helper";


export const signUpHandler = async (req: Request, res: Response) => {
  try {
    let admin = req.body;
    let result = await authentificationService.signUpHandler(admin);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
  }
};

export const signInHandler = async (req: Request, res: Response) => {
  try {
    let admin = req.body;
    let result = await authentificationService.signInHandler(admin);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<string>(null, 500, error.message);
    return res.status(response.status).json(response);
  }
};