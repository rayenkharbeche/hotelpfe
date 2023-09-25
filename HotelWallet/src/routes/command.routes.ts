import express from "express";
import { addCommand, getCommand, getCommandByQuery, updateCommandStatus,postCommand} from "../controllers/command.controller";
import { authenticateToken, authorize} from "../middleware";
import { addCommandAuth, getCommandAuth, updateCommandAuth } from "../utils/roles-definition";
export const Router_command = express.Router();

Router_command.post("/" ,authenticateToken,authorize(addCommandAuth), addCommand);
Router_command.get("/" ,authenticateToken,authorize(getCommandAuth), getCommand);
Router_command.put("/updateCommandStatus/:id" ,authenticateToken,authorize(updateCommandAuth), updateCommandStatus);

Router_command.post("/getCommandByQuery" ,authenticateToken,authorize(getCommandAuth), getCommandByQuery);

Router_command.post("/postC" , postCommand );