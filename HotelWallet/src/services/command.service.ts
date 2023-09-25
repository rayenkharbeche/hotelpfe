import { ApiResponse, Command, CommandQuery } from "../models/models";
import { createApiResponse } from "../utils/helper";
import { prisma } from "../utils/prisma.server";

export const getCommandByQuery = async (commandQuery: CommandQuery): Promise<Command[]> => {
  const result = await prisma.command.findMany({
    where: {
      providerId: commandQuery.providerId,
      commandStatus : commandQuery.commandStatus
    },
    include: { items: true }
  });

  result.forEach(command => {
    command.items.forEach(item => {
      item.commandIds = [];
    });
  });
  return result;
};

export const getCommand = async (): Promise<Command[]> => {
  const result = await prisma.command.findMany({ include: { items: true } });
  result.forEach(command => {
    command.items.forEach(item => {
      item.commandIds = [];
    });
  });
  return result;
};

export const addCommand = async (command: Omit<Command, "id">): Promise<ApiResponse<boolean>> => {

  //load list of items to calculate total price.
  const items = await prisma.item.findMany({
    where: {
      id: { in: command.itemIds },
    },
    select: {
      id: true,
      price: true,
    }
  });

  let somme = 0;
  const counts = {};
  //counting duplicate items. a,a
  for (const id of command.itemIds) {
    counts[id] = counts[id] ? counts[id] + 1 : 1;
  }

  for (let item of items) {
    let occurence = counts[item.id];
    let price = + item.price * occurence;
    somme = somme + price;
  }
  command.totalPrice = somme;

  //get client by id
  const fetchedClient = await prisma.client.findFirst({
    where: {
      id: command.clientId,
    },

  });
  if (fetchedClient.credit < command.totalPrice) {
    let response = createApiResponse<boolean>(false, 200, "your solde is insuffisent.");
    return response;
  }

  command.date = new Date();

  let data = {
    ...command,
    items: {
      connect: command.items
    }
  };

  const result = await prisma.command.create({
    data: data,
    include: { items: true },
  });

  if (result) {
    let newCredit = fetchedClient.credit - command.totalPrice;
    const updatedClient = await prisma.client.update({

      where: {
        id: command.clientId,
      },
      data: {
        credit: newCredit
      }
    });
  }
  
  result.items.forEach(item => {
    item.commandIds = [];
  });

  let response = createApiResponse<boolean>(true, 200);
  return response;
}

export const updateCommandStatus = async (commandQuery: CommandQuery, id: string): Promise<ApiResponse<boolean>> => {

  const result = await prisma.command.update(
    {
      where: {
        id: id
      },
      data: {
        commandStatus:commandQuery.commandStatus
      }
    }
  );
  let response = createApiResponse<boolean>(true, 200);
  return response;
}

export const postCommand = async (command: Omit<Command, "id">): Promise<ApiResponse<boolean>> => {

  // Load list of items to calculate total price.
  const items = await prisma.item.findMany({
    where: {
      id: { in: command.itemIds },
    },
    select: {
      id: true,
      price: true,
    }
  });

  let somme = 0;
  const counts = {};
  // Counting duplicate items
  for (const id of command.itemIds) {
    counts[id] = counts[id] ? counts[id] + 1 : 1;
  }

  for (let item of items) {
    let occurence = counts[item.id];
    let price = +item.price * occurence;
    somme = somme + price;
  }
  command.totalPrice = somme;

  // Get client by id
  const fetchedClient = await prisma.client.findFirst({
    where: {
      id: command.clientId,
    },
  });

  if (!fetchedClient) {
    let response = createApiResponse<boolean>(false, 200, "Client not found.");
    return response;
  }

  if (fetchedClient.credit < command.totalPrice) {
    let response = createApiResponse<boolean>(false, 200, "Your balance is insufficient.");
    return response;
  }

  command.date = new Date();

  let data = {
    ...command,
    items: {
      connect: command.itemIds.map(id => ({ id }))
    }
  };

  const result = await prisma.command.create({
    data: data,
    include: { items: true },
  });

  if (result) {
    let newCredit = fetchedClient.credit - command.totalPrice;
    const updatedClient = await prisma.client.update({
      where: {
        id: command.clientId,
      },
      data: {
        credit: newCredit
      }
    });
  }

  result.items.forEach(item => {
    item.commandIds = [];
  });

  let response = createApiResponse<boolean>(true, 200);
  return response;
}
