// import { Request, Response } from "express";
// import * as ServiceProviderauthService from "../services/serviceprovider-auth.service"
// import { createApiResponse } from "../utils/helper";


// export const signUpHandler = async (req: Request, res: Response) => {
//   try {
//     let serviceProvider = req.body;
//     let result = await ServiceProviderauthService.signUpHandler(serviceProvider);
//     return res.status(result.status).json(result);
//   } catch (error: any) {
//     let response = createApiResponse<boolean>(null, 500, error.message);
//     return res.status(response.status).json(response);
//   }
// };
