import express from "express";
import { getClients,updateClient,deleteClientById,addClient, getClientById ,updateClientt} from "../controllers/client.controller";
import { authenticateToken, authorize } from "../middleware";
import { getClientAuth, getClientByIdAuth, updateClientAuth } from "../utils/roles-definition";
export const Router_client = express.Router()
import multer from 'multer'

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, "../../HotelWallet/HotelWalletAngular/src/assets/img"); // set the destination folder
  },
  filename: (req, file, cb) => {
    const fileName = `${Date.now()}-${file.originalname}`; // set the file name
    cb(null, fileName);
  },
});
 
const upload = multer({ storage });

Router_client.post("/"  ,authenticateToken ,authorize(getClientAuth), upload.single("image"), addClient);
Router_client.get("/"  ,authenticateToken ,authorize(getClientAuth), getClients);
Router_client.get("/clientid/:id" , getClientById);
Router_client.put("/:id" ,authenticateToken,authorize(updateClientAuth),  upload.single("image"),updateClient);
Router_client.delete("/:id" , deleteClientById);
Router_client.put("/update/:id",updateClientt)