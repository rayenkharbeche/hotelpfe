import { ApiResponse } from "../models/models";
import jwt from "jsonwebtoken";
import { prisma } from "./prisma.server";

export function createApiResponse<T>(data: T, status: number, error: string | null = null): ApiResponse<T> {
  return {
    success: !error,
    data,
    error,
    status
  };
}

export function generateAuthToken(jwtPayload: any, expiresIn: string): string {
  return jwt.sign(jwtPayload, process.env.JWT_SECRET, {
    algorithm: "HS256",
    expiresIn: expiresIn,
  })
}

export async function isEmailUsed(email: string) {
  let isProvider = await prisma.serviceProvider.findFirst({
    where: {
      email: {
        equals: email,
        mode: 'insensitive',
      }
    },
  });

  let isAdmin = await prisma.admin.findFirst({
    where: {
      email: {
        equals: email,
        mode: 'insensitive',
      }
    },
  });

  return isProvider || isAdmin;
}