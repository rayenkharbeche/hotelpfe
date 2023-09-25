import { prisma } from "../utils/prisma.server";
import { ApiResponse, Client, Role } from "../models/models";
import { createApiResponse } from "../utils/helper";
import { signInHandler } from "./authentication.service";
import { SignUpHandler, validateAPIToken } from "./passwardless-auth.service";

export const getClients = async (adminId: string): Promise<Client[]> => {
  const result = await prisma.client.findMany({
    where: {
      adminId: adminId
    }
  });
  return result;
};
export const GetClients =async (): Promise<Client[]> => {
  const result = await prisma.client.findMany();
  return result;
};

export const getClientById = async (id: string): Promise<Client> => {
  const result = await prisma.client.findUnique({
    where: {
      id: id,
    },
  });
  return result;
};


export const addClient = async (client: Omit<Client, "id">, adminId: string): Promise<ApiResponse<boolean>> => {
  let fetchedClient = await findByEmail(client.email, adminId);

  if (fetchedClient) {
    let response = createApiResponse<boolean>(null, 403, "this client already in use.");
    return response;
  }

  let data: Omit<Client, "id"> = {
    ...client,
    room: +client.room,
    credit: +client.credit,
    checkIn: new Date(client.checkIn),
    checkOut: new Date(client.checkOut),

  }

  let result = await SignUpHandler(data);
  return result;
}


export const updateClient = async (client: Omit<Client, "id">, id: string, role: string, adminId: string): Promise<ApiResponse<boolean>> => {
  let fetchedClient = await prisma.client.findFirst({
    where: {
      id:id
    },
  });

  if (fetchedClient && fetchedClient.id != id) {
    let response = createApiResponse<boolean>(null, 403, "this client already in use.");
    return response;
  }
  let response
  
  if (role == Role.ADMIN) {
    let data: Omit<Client, "id"> = {
      ...client,
      room: +client.room,
      credit: +client.credit,
      checkIn:new Date(client.checkIn),
      checkOut:new Date(client.checkOut),
    }

    const result = await prisma.client.update(
      {
        where: {
          id: id
        },
        data: {
          name : client.name,
          phone : client.phone,
          photo : client.photo,
          email:client.email
        },
      }
    );
    response = createApiResponse<boolean>(true, 200);
  }
  else {

    const result = await prisma.client.update(
      {
        where: {
          id: id
        },
        data: {
          name : client.name,
          phone : client.phone,
          photo : client.photo,
          email:client.email
        },
      }
    );
    response = createApiResponse<boolean>(true, 200);
  }
  return response;
}

export const deleteClientById = async (id: string): Promise<ApiResponse<boolean>> => {

  const result = await prisma.client.delete(
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

async function findByEmail(email: string, adminId: string) {

  let result = await prisma.client.findFirst({
    where: {
      email: {
        equals: email,
        mode: 'insensitive',
      },
      adminId: adminId
    },
  });
  return result;
}
async function findByEmaill(email: string) {

  let result = await prisma.client.findFirst({
    where: {
      email: {
        equals: email,
        mode: 'insensitive',
      },
     
    },
  });
  return result;
}
export const updateClientt = async (
  email: string,
  phone: string,
  name: string,
  id: string
): Promise<Client> => {
  try {
    const result = await prisma.client.update({
      where: {
        id: id
      },
      data: {
        email: email,
        phone: phone,
        name: name
      },
      select: {
        id: true,
        name: true,
        email: true,
        phone: true,
        photo: true,
        credit: true,
        createdAt: true,
        updatedAt: true,
        checkIn: true,
        checkOut: true,
        room: true,
        adminId:true,
        tokens:true
        
      }
    });
    return result;
  } catch (e) {
    throw new Error(`Client with ID ${id} not found.`);
  }
};



export const addClientt = async (client: Omit<Client, "id">): Promise<ApiResponse<boolean>> => {
  let fetchedClient = await findByEmaill(client.email);

  if (fetchedClient) {
    let response = createApiResponse<boolean>(null, 403, "this client already in use.");
    return response;
  }

  let data: Omit<Client, "id"> = {
    ...client,
    room: +client.room,
    credit: +client.credit,
    checkIn:new Date(client.checkIn),
    checkOut:new Date(client.checkOut),
  }

  let result =await SignUpHandler(data);
  return result;
}


