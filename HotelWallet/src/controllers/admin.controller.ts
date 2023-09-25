import { Request, Response } from "express";
import * as AdminService from "../services/admin.service"
import { createApiResponse } from "../utils/helper";


export const updateClientStatus = async (req: Request, res: Response) => {
  try {
    let obj ={status:true,id:req.body.id};
    let result = await AdminService.updateClientStatus(obj.id,obj.status);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<String>(null, 500, error.message);
    return res.status(response.status).json(response);
  }
};

export const getAdmin = async (req: Request, res: Response) => {
  try {
    let result = await AdminService.getAdmin();
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message);
  }
};

export const addAdmin = async (req: Request, res: Response) => {
  try {
    let admin = req.body;
    let result = await AdminService.addAdmin(admin);
    return res.status(200).json(result);
  } catch (error: any) {
    return res.status(500).json(error.message);
  }
};

export const updateAdmin = async (req: Request, res: Response) => {
  try {
    let admin = req.body;
    let id =req.params.id;
    let result = await AdminService.updateAdmin(admin,id);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
};
};
export const deleteAdminById = async (req: Request, res: Response) => {
  try {
    let id =req.params.id;
    let result = await AdminService.deleteAdminById(id);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
};
};