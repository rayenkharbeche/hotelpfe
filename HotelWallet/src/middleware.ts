import * as authService from "./services/passwardless-auth.service"
import { Request, Response } from "express";

export async function authenticateToken(req: Request, res: Response, next: () => void) {

  let bearerToken = req.headers['authorization'];
  if (!bearerToken) { return res.status(401).json({ message: 'unauthorized' }) }
  let array = bearerToken.split(" ");
  let token = array[1];
  let response = await authService.validateAPIToken(token);
  if (!response.success) { return res.status(401).json({ message: 'unauthorized' }) }
  req.app.locals=response.data;
  next()
}

export const authorize = (roles: string[]) => {
  return (req: Request, res: Response, next: () => void) => {
    const payload=req.app.locals;

    let isMatch=roles.includes(payload.role)
    if (!isMatch) { return res.status(401).json({ message: 'unauthorized' }) }
    next();
  }
}
