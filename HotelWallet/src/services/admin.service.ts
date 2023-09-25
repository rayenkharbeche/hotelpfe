import { prisma } from "../utils/prisma.server";
import { Admin, ApiResponse, Client } from "../models/models";
import { createApiResponse } from "../utils/helper";
import { TokenType } from "@prisma/client";


export const updateClientStatus = async (id: string, status: boolean): Promise<ApiResponse<Client>> => {

  const fetchedclient = await prisma.client.findUnique({
    where: {
      id: id,
    },
    include: { tokens: true },
  });

  const token = fetchedclient.tokens.find(({ type }) => type == TokenType.API);
  const updatedToken = await prisma.token.update({
    where: {
      id: token.id,
    },
    data: {
      valid: status
    },
  });
  fetchedclient.tokens=[];
  fetchedclient.tokens.push(updatedToken);
  let response = createApiResponse<Client>(fetchedclient, 200);
  return response;
}



export const getAdmin = async (): Promise<Admin[]> => {
  const result = await prisma.admin.findMany({

  });
  return result;
};
export const addAdmin = async (admin: Omit<Admin, "id">): Promise<ApiResponse<boolean>> => {
  let administrator = await findByName(admin.name);

  if (administrator) {
    let response = createApiResponse<boolean>(null, 403, "this admin already in use");
    return response;
  }

  const result = await prisma.admin.create({
    data: admin,
  });
  let response = createApiResponse<boolean>(true, 200);
  return response;
}


export const updateAdmin = async (admin: Omit<Admin, "id">, id: string): Promise<ApiResponse<boolean>> => {
  let administrator = await findByName(admin.name);

  if (administrator && administrator.id != id) {
    let response = createApiResponse<boolean>(null, 403, "this admin already in use");
    return response;
  }

  const result = await prisma.admin.update(
    {
      where: {
        id: id
      },
      data: admin,
    }
  );
  let response = createApiResponse<boolean>(true, 200);
  return response;
}

export const deleteAdminById = async (id: string): Promise<ApiResponse<boolean>> => {

  const result = await prisma.admin.delete(
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


async function findByName(name: string) {
  let result = await prisma.admin.findFirst({
    where: {
      name: {
        equals: name,
        mode: 'insensitive',
      }
    },
    select: {
      id: true,
      name: true,

    },
  });
  return result;
}

