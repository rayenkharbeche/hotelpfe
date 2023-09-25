import { prisma } from "../utils/prisma.server";
import { ApiResponse, Item } from "../models/models";
import { createApiResponse } from "../utils/helper";

export const getItems = async (serviceId:string): Promise<Item[]> => {
  const result = await prisma.item.findMany({
    where: {
      serviceId: serviceId
   
    },
    include:{
      itemCategory:true
    }
  });
  return result;
};

export const addItem =async(item :Omit<Item, "id">,serviceId: string): Promise<ApiResponse<boolean>>=> {
  
  let Item = await findByName(item.name,serviceId);

  if (Item) {
    let response = createApiResponse<boolean>(null, 403, "this item already in use.");
    return response;
  }
  let data: Omit<Item, "id"> = {
    ...item,
    price: +item.price,
    status:convertToBoolean(item.status)
  }

  const result = await prisma.item.create({
    data: data,
  });
  let response = createApiResponse<boolean>(true, 200);
  return response;
}

export const updateItem = async (item: Omit<Item, "id">, id: string,serviceId: string):  Promise<ApiResponse<boolean>> => {
  let Item = await findByName(item.name,serviceId);

  if (Item && Item.id != id) {
    let response = createApiResponse<boolean>(null, 403, "this item already in use.");
    return response;
  }
  let data: Omit<Item, "id"> = {
    ...item,
    price: +item.price,
    status:convertToBoolean(item.status)
  }

  const result = await prisma.item.update(
    {
      where: {
        id: id
      },
      data: data,
    }
  );
  let response = createApiResponse<boolean>(true, 200);
  return response;}


export const deleteItemById = async (id: string):  Promise<ApiResponse<boolean>> => {

  const result = await prisma.item.delete(
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
  let response = createApiResponse<boolean>(null, 403, "delete failed.");
    return response;
}



async function findByName(name: string,serviceId: string) {
  let result = await prisma.item.findFirst({
    where: {
      name: {
        equals: name,
        mode: 'insensitive',
      },
      serviceId: serviceId
    },
    select: {
      id: true,
      name: true,
    },
  });

  return result;
}

function convertToBoolean(value:boolean):boolean{
  let stringValue= <unknown>value;
  return stringValue == 'true'
}

export const getItemByItemCId = async (itemCategoryId: string): Promise<Item[]> => {
  const result = await prisma.itemCategory.findMany({
    where: {
      id: itemCategoryId,
    },
    include:{
      items: true
    }
  });

  const items: Item[] = result.flatMap((itemCategory) => itemCategory.items);

  return items;
};

export const GetServices = async (): Promise<Item[]> => {
  const result = await prisma.item.findMany();
  return result.map((service) => ({
    id: service.id,
    name: service.name,
    description: service.description,
    photo: service.photo,
    price: service.price,
    status: service.status,
    serviceId: service.serviceId,
    itemCategoryId: service.itemCategoryId,
  }));
};


/*export const getItemByServiceId = async (serviceId: string): Promise<Item[]> => {
  const result = await prisma.service.findMany({
    where: {
      id: serviceId,
    },
    include:{
      items: true
    }
  });

  const items: Item[] = result.flatMap((service) => service.items);

  return items;
};*/
export const getItemByServiceId = async (serviceId: string): Promise<Item[]> => {
  const service = await prisma.service.findUnique({
    where: {
      id: serviceId,
    },
    include: {
      items: true,
    },
  });

  if (!service) {
    // Gérer le cas où le service n'existe pas
    return [];
  }

  const items: Item[] = service.items ?? [];

  return items;
};




