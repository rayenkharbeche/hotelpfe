import { prisma } from "../utils/prisma.server";
import { ApiResponse, ServiceProvider } from "../models/models";
import { createApiResponse, isEmailUsed } from "../utils/helper";
import * as bcrypt from 'bcrypt';


export const getServiceProvider = async (adminId: string): Promise<ServiceProvider[]> => {
  const result = await prisma.serviceProvider.findMany({
    where: {
      adminId: adminId,
    },
    include: {
      service: true
    },
  });
  return result;
};

export const getServiceProviderById = async (id: string): Promise<ServiceProvider> => {
  const result = await prisma.serviceProvider.findUnique({
    where: {
      id: id,
    },
  });
  return result;
};

export const addServiceProvider = async (serviceProvider: Omit<ServiceProvider, "id">): Promise<ApiResponse<boolean>> => {
  
  const fecthedServiceProvider =  await isEmailUsed(serviceProvider.email);

  if (fecthedServiceProvider) {
    let response = createApiResponse<boolean>(null, 403, "email alreay in use");
    return response;
  }

  const salt = await bcrypt.genSalt();
  const hashedPassword = await bcrypt.hash(serviceProvider.password, salt);
  const result = await prisma.serviceProvider.create({
    data: {
      name: serviceProvider.name,
      email: serviceProvider.email,
      password: hashedPassword,
      serviceId: serviceProvider.serviceId,
      adminId: serviceProvider.adminId,

    },
  });

  let response = createApiResponse<boolean>(true, 200);
  return response;
}


export const updateServiceProvider = async (service_Provider: Omit<ServiceProvider, "id">, id: string): Promise<ApiResponse<boolean>> => {
  let serviceProvider = await findByEmail(service_Provider.email);

  if (serviceProvider && serviceProvider.id != id) {
    let response = createApiResponse<boolean>(null, 403, "this ServiceProvider already in use.");
    return response;
  }
  const result = await prisma.serviceProvider.update(
    {
      where: {
        id: id
      },
      data: service_Provider,
    }
  );
  let response = createApiResponse<boolean>(true, 200);
  return response;
}

export const deleteServiceProviderById = async (id: string): Promise<boolean> => {

  const result = await prisma.serviceProvider.delete(
    {
      where: {
        id: id
      },
    }
  );

  if (result) {
    return true;
  }
  return false;
}

async function findByEmail(email: string) {
  let result = await prisma.serviceProvider.findFirst({
    where: {
      email: {
        equals: email,
        mode: 'insensitive',
      }
    },
  });
  return result;
}