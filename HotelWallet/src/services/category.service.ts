import { prisma } from "../utils/prisma.server";
import { ApiResponse, ItemCategory } from "../models/models";
import { createApiResponse } from "../utils/helper";

export const getCategories = async (serviceId: string): Promise<ItemCategory[]> => {
  const result = await prisma.itemCategory.findMany({
    where: {
      serviceId: serviceId
    }
  });
  return result;
};

export const getCategoriesByServiceId = async (serviceId: string): Promise<ItemCategory[]> => {
  const result = await prisma.itemCategory.findMany({
    where: {
      serviceId: serviceId
    },
    include:{
      items:true
    }
  });
  return result;
};

export const addCategory = async (category: Omit<ItemCategory, "id"|"items">  ,serviceId: string):  Promise<ApiResponse<boolean>> => {
  let itemCategory = await findByName(category.name,serviceId);

  if (itemCategory) {
    let response = createApiResponse<boolean>(null, 403, "this category already in use.");
    return response;
  }

  const result = await prisma.itemCategory.create({
    data: category,
  });
  let response = createApiResponse<boolean>(true, 200);
  return response;
}


export const updateCategory = async (category: Omit<ItemCategory, "id"|"items">, id: string,serviceId:string):  Promise<ApiResponse<boolean>> => {
  let itemCategory = await findByName(category.name,serviceId);

  if (itemCategory && itemCategory.id != id) {
    let response = createApiResponse<boolean>(null, 403, "this category already in use.");
    return response;
  }

  const result = await prisma.itemCategory.update(
    {
      where: {
        id: id
      },
      data: category,
      
    }
  );
  let response = createApiResponse<boolean>(true, 200);
  return response;
}


export const deleteCategoryById = async (id: string): Promise<ApiResponse<boolean>>  => {

  const result = await prisma.itemCategory.delete(
    {
      where: {
        id: id
      },
    }
  );
  let response = createApiResponse<boolean>(true, 200);
  return response;
}



async function findByName(name: string,serviceId: string) {
  let result = await prisma.itemCategory.findFirst({
    where: {
      name: {
        equals: name,
        mode: 'insensitive',
      },
      serviceId: serviceId
    },
    
  });
  return result;
}


