import express from "express";
import { getItems, addItem,updateItem,deleteItemById,getItemByItemCId, getItemss,getItemByServiceId} from "../controllers/item.controller";
import { authenticateToken, authorize } from "../middleware";
import { addItemAuth, deleteItemAuth, getItemAuth, updateItemAuth } from "../utils/roles-definition";
export const Router_item = express.Router()

import multer from 'multer'
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    // cb(null, "./uploads"); // set the destination folder
    cb(null, "../../HotelWallet/HotelWalletAngular/src/assets/img"); // set the destination folder

  },
  filename: (req, file, cb) => {
    const fileName = `${Date.now()}-${file.originalname}`; // set the file name
    cb(null, fileName);
  },
});
 
const upload = multer({ storage });

Router_item.get("/" , authenticateToken,authorize(getItemAuth),getItems);
Router_item.post("/" ,authenticateToken,authorize(addItemAuth),upload.single("image"), addItem);
Router_item.put("/:id" ,authenticateToken,authorize(updateItemAuth),upload.single("image"), updateItem);
Router_item.delete("/:id" , authenticateToken,authorize(deleteItemAuth),deleteItemById);
Router_item.get("/liste/:itemCategoryId" ,getItemByItemCId);
Router_item.get("/liste" ,getItemss);
Router_item.get("/listes/:serviceId",getItemByServiceId );