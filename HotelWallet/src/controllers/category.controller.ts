import { Request, Response } from "express";
import * as ItemCategoryService from "../services/category.service"
import { createApiResponse } from "../utils/helper";

export const getCategories = async (req: Request, res: Response) => {
  try 
  {
    let serviceId = req.headers['serviceid'].toString();
    let result = await ItemCategoryService.getCategories(serviceId);
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message); 
  }
};

export const getCategoriesByServiceId = async (req: Request, res: Response) => {
  try 
  {
    let serviceId=req.params.serviceId;
    let result = await ItemCategoryService.getCategoriesByServiceId(serviceId);
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message); 
  }
};

export const addCategory = async (req: Request, res: Response) => {
  try {
    let serviceId = req.headers['serviceid'].toString();
    let category = req.body;
    let result = await ItemCategoryService.addCategory(category,serviceId);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
};
}
export const updateCategory = async (req: Request, res: Response) => {
  try {
    let serviceId = req.headers['serviceid'].toString();
    let category = req.body;
    let id =req.params.id;
    let result = await ItemCategoryService.updateCategory(category,id,serviceId);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
};
}
export const deleteCategoryById = async (req: Request, res: Response) => {
  try {
    let id =req.params.id;
    let result = await ItemCategoryService.deleteCategoryById(id);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
};
}