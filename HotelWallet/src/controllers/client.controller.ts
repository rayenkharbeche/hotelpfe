import { Request, Response } from "express";
//import {body, validationResult }from "express-validator";
import * as ClientService from "../services/client.service"
import { createApiResponse } from "../utils/helper";
import jwt from "jsonwebtoken";
const secret = "12345";
export const getClients = async (req: Request, res: Response) => {
  try {
    let adminId = req.headers['adminid'].toString();
    let result = await ClientService.getClients(adminId);
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message);
  }
};

export const getClientss = async (req: Request, res: Response) => {
  try {
   
    let result = await ClientService.GetClients();
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message);
  }
};

export const getClientById = async (req: Request, res: Response) => {
  try 
  {
    let clientId=req.params.id;
    let result = await ClientService.getClientById(clientId);
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message); 
  }
};


export const addClient = async (req: any, res: Response) => {
  try {
    let adminId = req.headers['adminid'].toString();
    let client ;
    let imageName = req.file?.filename ?? "";
    if (imageName == "") {
      client = req.body; //keep body with old image
      delete client['image']; // image not triggered by upload image middleware so remove it.
    }
    else {
      client = { ...req.body, photo: imageName }; //replace old image with new uploaded image.
    }
    let result = await ClientService.addClient(client,adminId);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
};
};
export const updateClient = async (req: any, res: Response) => {
  try {
    let adminId = req.headers['adminid'].toString();
    const payload=req.app.locals;
    
    let id =req.params.id;
    let client ;
    let imageName = req.file?.filename ?? "";
    if (imageName == "") {
      client = req.body; //keep body with old image
      delete client['image']; // image not triggered by upload image middleware so remove it.
    }
    else {
      client = { ...req.body, photo: imageName }; //replace old image with new uploaded image.
    }
    let result = await ClientService.updateClient(client,id,payload.role,adminId);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
};
};
export const deleteClientById = async (req: Request, res: Response) => {
  try {
    let id =req.params.id;
    let result = await ClientService.deleteClientById(id);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
};
};
export const updateClientt = async (req: Request, res: Response) => {
  try {
    const { email, phone,name } = req.body;
    const id = req.params.id;
    const result = await ClientService.updateClientt(email, phone,name, id);
    return res.status(200).json(result);
  } catch (error: any) {
    return res.status(500).json(error.message);
  }
};

export const addClientt = async (req: Request, res: Response) => {
  try {
    let client = req.body;
    let result = await ClientService.addClientt(client);
    const token = jwt.sign({ client }, secret, { expiresIn: "1h" });
    const registerResponse = { client: result, token: token };
    return res.status(200).json(registerResponse);
  } catch (error: any) {
    return res.status(500).json(error.message);
  }
};


