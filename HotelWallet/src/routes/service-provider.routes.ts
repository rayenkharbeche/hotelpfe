import express from "express";
import {  getServiceProvider,updateServiceProvider,deleteServiceProviderById,getServiceProviderById, addServiceProvider } from "../controllers/service-provider.controller";
import { authenticateToken, authorize } from "../middleware";
import { addServiceProviderAuth, deleteServiceProviderAuth, getServiceProviderAuth, getServiceProviderByIdAuth, updateServiceProviderAuth } from "../utils/roles-definition";
export const Router_serviceProvider = express.Router()
Router_serviceProvider.get("/:id" , authenticateToken,authorize(getServiceProviderByIdAuth),getServiceProviderById);
Router_serviceProvider.get("/" , authenticateToken,authorize(getServiceProviderAuth),getServiceProvider);
Router_serviceProvider.put("/:id" , authenticateToken,authorize(updateServiceProviderAuth),updateServiceProvider);
Router_serviceProvider.delete("/:id" ,authenticateToken,authorize(deleteServiceProviderAuth), deleteServiceProviderById);
Router_serviceProvider.post("/" , authenticateToken,authorize(addServiceProviderAuth),addServiceProvider);
