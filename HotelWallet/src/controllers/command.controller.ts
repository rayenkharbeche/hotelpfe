import { Request, Response } from "express";
//import {body, validationResult }from "express-validator";
import * as commandService from "../services/command.service"
//import * as authService from "../services/passwardless-auth.service"
import { createApiResponse } from "../utils/helper";

export const getCommand = async (req: Request, res: Response) => {
  try {
    let result = await commandService.getCommand();
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message);
  }
};

export const addCommand = async (req: Request, res: Response) => {
  try {
    let command = req.body;
    let result = await commandService.addCommand(command);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
};
};

export const updateCommandStatus = async (req: Request, res: Response) => {
  try {
    let commandQuery = req.body;
    let id =req.params.id;
    let result = await commandService.updateCommandStatus(commandQuery,id);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
};
};

export const getCommandByQuery = async (req: Request, res: Response) => {
  try {
    let commandQuery = req.body;
    let result = await commandService.getCommandByQuery(commandQuery);
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message);
  }
};

export const postCommand = async (req: Request, res: Response) => {
  try {
    let command = req.body;
    let result = await commandService.postCommand(command);
    if (result.success && result.data) {
      return res.status(200).json(result);
    } else {
      return res.status(400).json(result);
    }
  } catch (error: any) {
    return res.status(500).json({
      success: false,
      data: null,
      error: error.message,
      status: 500
    });
  }
};
