import { Request, Response } from "express";
import * as ItemService from "../services/item.service"
import { createApiResponse } from "../utils/helper";

export const getItems = async (req: Request, res: Response) => {
  try {
    let serviceId = req.headers['serviceid'].toString();

    let result = await ItemService.getItems(serviceId);
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message);
  }
};

export const addItem = async (req: any, res: Response) => {
  try {
    let serviceId = req.headers['serviceid'].toString();

    let item;
    let imageName = req.file?.filename ?? "";
    if (imageName == "") {
      item = req.body; //keep body with old image
      delete item['image']; // image not triggered by upload image middleware so remove it.
    }
    else {
      item = { ...req.body, photo: imageName }; //replace old image with new uploaded image.
    }
    let result = await ItemService.addItem(item,serviceId);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
};
};
export const updateItem = async (req: any, res: Response) => {
  try {
    let serviceId = req.headers['serviceid'].toString();

    let item;
    let imageName = req.file?.filename ?? "";
    if (imageName == "") {
      item = req.body; //keep body with old image
      delete item['image']; // image not triggered by upload image middleware so remove it.
    }
    else {
      item = { ...req.body, photo: imageName }; //replace old image with new uploaded image.
    }

    let id =req.params.id;
    let result = await ItemService.updateItem(item,id,serviceId);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
};
};
export const deleteItemById = async (req: Request, res: Response) => {
  try {
    let id =req.params.id;
    let result = await ItemService.deleteItemById(id);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
};
};

export const getItemByItemCId = async (req: Request, res: Response) => {
  try 
  {
    let itemCategoryId=req.params.itemCategoryId;
    let result = await ItemService.getItemByItemCId(itemCategoryId);
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message); 
  }
};

export const getItemss = async (req: Request, res: Response) => {
  try {
   
    let result = await ItemService.GetServices();
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message);
  }
};

export const getItemByServiceId = async (req: Request, res: Response) => {
  try 
  {
    let serviceId=req.params.serviceId;
    let result = await ItemService.getItemByServiceId(serviceId);
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message); 
  }
};