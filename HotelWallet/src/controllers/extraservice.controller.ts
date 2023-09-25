import { Request, Response } from "express";
import * as ExtarServicesService from "../services/extra-service.service"
import { createApiResponse } from "../utils/helper";

/*export const getServices = async (req: Request, res: Response) => {
  try {
    let adminId = req.headers['adminid'];
    let result = await ExtarServicesService.getServices(adminId.toString());
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message);
  }
};
*/

/*export const getServiceById = async (req: Request, res: Response) => {
  try 
  {
    let serviceId=req.params.id;
    let result = await ExtarServicesService.getServiceById(serviceId);
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message); 
  }
};
*/
export const addService = async (req: any, res: Response) => {
  try {
    let adminId = req.headers['adminid'].toString();
    let service;
    let imageName = req.file?.filename ?? "";
    if (imageName == "") {
      service = req.body; //keep body with old image
      delete service['image']; // image not triggered by upload image middleware so remove it.
    }
    else {
      service = { ...req.body, photo: imageName }; //replace old image with new uploaded image.
    }
    
    let result = await ExtarServicesService.addService(service,adminId);

    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
  };
};

export const updateService = async (req: any, res: Response) => {
  try {
    let adminId = req.headers['adminid'].toString();
    let service;
    let imageName = req.file?.filename ?? "";
    if (imageName == "") {
      service = req.body; //keep body with old image
      delete service['image']; // image not triggered by upload image middleware so remove it.
    }
    else {
      service = { ...req.body, photo: imageName }; //replace old image with new uploaded image.
    }

    let id = req.params.id;
    let result = await ExtarServicesService.updateService(service, id, adminId);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
  };
};

export const deleteServiceById = async (req: Request, res: Response) => {
  try {
    let id = req.params.id;
    let result = await ExtarServicesService.deleteServiceById(id);
    return res.status(result.status).json(result);
  } catch (error: any) {
    let response = createApiResponse<boolean>(null, 500, error.message);
    return res.status(response.status).json(response);
  };
};

export const GetServices = async (req: Request, res: Response) => {
  try {
    
    let result = await ExtarServicesService.GetServices();
    return res.status(200).json(result);
  } catch (error: any) {
    console.log(error);
    return res.status(500).json(error.message);
  }
};