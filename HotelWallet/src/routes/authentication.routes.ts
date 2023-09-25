import express from "express";
import { signUpHandler,signInHandler  } from "../controllers/authentication.controller";
export const Router_authentication = express.Router()
Router_authentication.post("/register" ,signUpHandler);
Router_authentication.post("/login" , signInHandler);