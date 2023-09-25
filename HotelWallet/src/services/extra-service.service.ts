import { prisma } from "../utils/prisma.server";
import { ApiResponse, ExtarServices } from "../models/models";
import { createApiResponse } from "../utils/helper";

/*export const getServices = async (adminId: string): Promise<ExtarServices[]> => {
  const result = await prisma.service.findMany(
    {
      where: {
        adminId: adminId
      }
    });
  return result;
};*/

/*export const getServiceById = async (id: string): Promise<ExtarServices> => {
  const result = await prisma.service.findUnique({
    where: {
      id: id,
    },
    include:{
     serviceProvider:true
    }
  });
  return result;
};*/

export const addService = async (service: Omit<ExtarServices, "id">, adminId:string): Promise<ApiResponse<boolean>> => {

  let ExtarServices = await findByName(service.name,adminId);

  if (ExtarServices) {
    let response = createApiResponse<boolean>(null, 403, "Name already in use.");
    return response;
  }

  service.status = convertToBoolean(service.status);
  const result = await prisma.service.create({
    data: service,
  });
  let response = createApiResponse<boolean>(true, 200);
  return response;
}

export const updateService = async (service: Omit<ExtarServices, "id">, id: string, adminId:string): Promise<ApiResponse<boolean>> => {
  let ExtarServices = await findByName(service.name,adminId);

  if (ExtarServices && ExtarServices.id != id) {
    let response = createApiResponse<boolean>(null, 403, "this service already in use.");
    return response;
  }
  service.status = convertToBoolean(service.status);
  const result = await prisma.service.update(
    {
      where: {
        id: id
      },
      data: service,
    }
  );
  let response = createApiResponse<boolean>(true, 200);
  return response;
}


export const deleteServiceById = async (id: string): Promise<ApiResponse<boolean>> => {

  const result = await prisma.service.delete(
    {
      where: {
        id: id
      },
    }
  );

  if (result) {
    let response = createApiResponse<boolean>(true, 200);
    return response;
  }
  let response = createApiResponse<boolean>(null, 403, "failed to delete.");
  return response;
}


async function findByName(name: string, adminId:string) {
  let result = await prisma.service.findFirst({
    where: {
      name: {
        equals: name,
        mode: 'insensitive',
      },
      adminId:adminId
    },
    select: {
      id: true,
      name: true,
    },
  });

  return result;
}

function convertToBoolean(value: boolean): boolean {
  let stringValue = <unknown>value;
  return stringValue == 'true'
}

export const GetServices = async (): Promise<ExtarServices[]> => {
  const result = await prisma.service.findMany();
  return result.map((service) => ({
    id: service.id,
    name: service.name,
    phone: service.phone,
    photo: service.photo,
    timeIn: service.timeIn,
    timeOut: service.timeOut,
    status: service.status,
    adminId: service.adminId,
    description: service.description,
  }));
};
