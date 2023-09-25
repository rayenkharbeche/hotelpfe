import { prisma } from "../utils/prisma.server";
import { Command, CommandQuery } from "../models/models";

export const getCommandHistoryByQuery = async (clientId: string): Promise<Command[]> => {
  const result = await prisma.command.findMany({
    where: {
      clientId: clientId
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


async function getComaandByDate(gte, lt) {
  try {
    const data = await prisma.command.findMany({
      where: {
        createdAt: {
          gte: new Date(gte),
          lt: new Date(lt)
        }
      }
    });
    return data;
  } catch (err) {
    return err;
  }
}
// Example usage:
//getComaandByDate('2022-01-01', '2022-02-01');
