import express from "express";
import { SignInHandler, SignUpHandler,  validateAPITokenHandler, getCurrentClientHandler} from "../controllers/passwardless-auth.controller";

export const RouterClientAuth = express.Router()
RouterClientAuth.post("/login" ,SignUpHandler);
RouterClientAuth.post("/register" , SignInHandler);
RouterClientAuth.get('/validate-token/:authToken', validateAPITokenHandler);
RouterClientAuth.get('/connected-client', getCurrentClientHandler);
