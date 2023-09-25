import express from "express";
import { addCategory, deleteCategoryById, getCategories, getCategoriesByServiceId, updateCategory } from "../controllers/category.controller";
import { authenticateToken, authorize } from "../middleware";
import { addCategoryAuth, deleteCategoryAuth, getCategoryAuth, updateCategoryAuth } from "../utils/roles-definition";

export const Router_category = express.Router()
//get categories
Router_category.post("/" ,authenticateToken,authorize(addCategoryAuth),addCategory );
Router_category.get("/" , authenticateToken,authorize(getCategoryAuth),getCategories);
Router_category.get("/:serviceId" , authenticateToken,authorize(getCategoryAuth),getCategoriesByServiceId);
Router_category.put("/:id" ,authenticateToken,authorize(updateCategoryAuth), updateCategory);
Router_category.delete("/:id" , authenticateToken,authorize(deleteCategoryAuth),deleteCategoryById);

