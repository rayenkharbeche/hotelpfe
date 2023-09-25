import express from "express";
import {  addService, updateService, deleteServiceById, GetServices } from "../controllers/extraservice.controller";
import { authenticateToken, authorize } from "../middleware";
import { addServiceAuth, deleteServiceAuth, getServiceAuth, updateServiceAuth } from "../utils/roles-definition";
export const Router_service = express.Router()
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

//Router_service.get("/", authenticateToken, authorize(getServiceAuth), getServices);
//Router_service.get("/:id", authenticateToken, authorize(getServiceAuth), getServiceById);

Router_service.post("/", authenticateToken, authorize(addServiceAuth), upload.single("image"), addService);
Router_service.put("/:id", authenticateToken, authorize(updateServiceAuth), upload.single("image"), updateService);
Router_service.delete("/:id", authenticateToken, authorize(deleteServiceAuth), deleteServiceById);

Router_service.get("/listSer",GetServices);

Router_service.get("/findByName",GetServices);