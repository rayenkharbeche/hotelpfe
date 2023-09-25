import { prisma } from "../utils/prisma.server";
import { Admin, ApiResponse, Role } from "../models/models";
import * as bcrypt from 'bcrypt';
import { createApiResponse, generateAuthToken, isEmailUsed } from "../utils/helper";

//admin signUp
export const signUpHandler = async (admin: Omit<Admin, "id">): Promise<ApiResponse<boolean>> => {
  //test is email in use
  const fecthedAdmin = await isEmailUsed(admin.email)

  if (fecthedAdmin) {
    let response = createApiResponse<boolean>(null, 403, "email alreay in use");
    return response;
  }

  const salt = await bcrypt.genSalt();
  const hashedPassword = await bcrypt.hash(admin.password, salt);
  const result = await prisma.admin.create({
    data: {
      name: admin.name,
      email: admin.email,
      password: hashedPassword,
    },
  });

  let response = createApiResponse<boolean>(true, 200);
  return response;
}

//admin signIn
export const signInHandler = async (authData: Omit<Admin, "id">): Promise<ApiResponse<string>> => {
  const fecthedAdmin = await prisma.admin.findUnique({
    where: {
      email: authData.email,
    },
  });

  const fecthedServiceProvider = await prisma.serviceProvider.findUnique({
    where: {
      email: authData.email,
    },
  });

  if (!fecthedAdmin && !fecthedServiceProvider) {
    let response = createApiResponse<string>(null, 403, "email not found");
    return response;
  }

  if (fecthedAdmin) {
    let isEuqal = await bcrypt.compare(authData.password, fecthedAdmin.password);
    if (isEuqal) {

      const jwtPayload = {
        role: Role.ADMIN,
        adminId: fecthedAdmin.id,
      };

      const authToken = generateAuthToken(jwtPayload, "360d");

      let response = createApiResponse<string>(authToken, 200);
      return response;
    }
  }

  if (fecthedServiceProvider) {
    let isEuqal = await bcrypt.compare(authData.password, fecthedServiceProvider.password);

    if (isEuqal) {
      const jwtPayload = {
        role: Role.SERVICEPROVIDER,
        adminId: fecthedServiceProvider.adminId,
        serviceId: fecthedServiceProvider.serviceId,
        providerId: fecthedServiceProvider.id,
      };

      const authToken = generateAuthToken(jwtPayload, "360d");
      let response = createApiResponse<string>(authToken, 200);
      return response;
    }
  }

  let response = createApiResponse<string>(null, 403, "incorrect password");
  return response;
}


