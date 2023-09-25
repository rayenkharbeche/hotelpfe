import express from "express";
import { addAdmin, getAdmin,updateAdmin,deleteAdminById ,updateClientStatus} from "../controllers/admin.controller";
import { authenticateToken, authorize } from "../middleware";
import { updateClientStatusAuth } from "../utils/roles-definition";
export const Router_admin = express.Router()
Router_admin.put("/" ,authenticateToken,authorize(updateClientStatusAuth), updateClientStatus);
Router_admin.get("/get" , getAdmin);
Router_admin.post("/" , addAdmin);
Router_admin.put("/:id" , updateAdmin);
Router_admin.delete("/:id" , deleteAdminById);



