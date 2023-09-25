import { Request, Response } from "express";
import * as authentificationService from "../services/passwardless-auth.service";
import { createApiResponse } from "../utils/helper";
import { Client, ApiResponse } from "../models/models";

export const validateAPITokenHandler = async (req: Request, res: Response) => {
  const authToken = req.params.authToken;

  try {
    const response: ApiResponse<Client | null> = await authentificationService.validateAPIToken(authToken);
    if (response.data) {
      const clientData = {
        id: response.data.id,
        name: response.data.name,
        createdAt: response.data.createdAt,
        updatedAt: response.data.updatedAt,
        email: response.data.email,
        phone: response.data.phone,
        checkIn: response.data.checkIn,
        checkOut: response.data.checkOut,
        photo: response.data.photo,
        credit: response.data.credit,
        room: response.data.room,
        adminId: response.data.adminId,
      };
      const responseData = {
        client: clientData,
        token: authToken
      };
      res.status(200).json(responseData);
    } else {
      res.status(403).json({ error: 'Invalid token' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

export const SignUpHandler = async (req: Request, res: Response) => {
  try {
    let client = req.body;
    let result = await authentificationService.SignUpHandler(client);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
  }
};

export const SignInHandler = async (req: Request, res: Response) => {
  try {
    let { email, emailToken } = req.body;
    let result = await authentificationService.SignInHandler(email, emailToken);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<string>(null, 500, error.message);
    return res.status(response.status).json(response);
  }
};

export const getCurrentClientHandler = async (req: Request, res: Response): Promise<void> => {
  const authToken = req.headers.authorization;
  if (!authToken) {
    res.status(401).json({ error: "Authorization token is missing" });
    return;
  }

  const response: ApiResponse<Client | null> = await authentificationService.getCurrentClient(authToken);
  res.status(response.status).json(response);
};
