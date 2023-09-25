import { Request, Response } from "express";
//import {body, validationResult }from "express-validator";
import * as ServiceProviderService from "../services/service-provider.service"
import { createApiResponse } from "../utils/helper";

export const getServiceProvider = async (req: Request, res: Response) => {
  try {
    let adminId = req.headers['adminid'].toString();
    let result = await ServiceProviderService.getServiceProvider(adminId);
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message);
  }
};

export const getServiceProviderById = async (req: Request, res: Response) => {
  try {
    let id =req.params.id;
    let result = await ServiceProviderService.getServiceProviderById(id);
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message);
  }
};

export const addServiceProvider = async (req: Request, res: Response) => {
  try {
    let sp = req.body;
    let result = await ServiceProviderService.addServiceProvider(sp);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
};
};

export const updateServiceProvider = async (req: Request, res: Response) => {
  try {
    let service_Provider = req.body;
    let id =req.params.id;
    let result = await ServiceProviderService.updateServiceProvider(service_Provider,id);
    return res.status(200).json(result);
  } catch (error: any) {
    return res.status(500).json(error.message);
  }
};
export const deleteServiceProviderById = async (req: Request, res: Response) => {
  try {
    let id =req.params.id;
    let result = await ServiceProviderService.deleteServiceProviderById(id);
    return res.status(200).json({success:result});
  } catch (error: any) {
    return res.status(500).json(error.message);
  }
};