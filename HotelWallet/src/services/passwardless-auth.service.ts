import { TokenType } from "@prisma/client";
import { ApiResponse, Client, Role, Token } from "../models/models";
import { add } from 'date-fns';
import { prisma } from "../utils/prisma.server";
import { sendEmailToken, sendJwtToken } from "../plugins/email";
import jwt from "jsonwebtoken";
import { createApiResponse, generateAuthToken } from "../utils/helper";
import { Request, Response } from 'express';

const EMAIL_TOKEN_EXPIRATION_MINUTES = 60;
const AUTHENTICATION_TOKEN_EXPIRATION_DAYS = 60;
let globalAuthToken: string | null = null;

export const SignUpHandler = async (client: Omit<Client, "id">): Promise<ApiResponse<boolean>> => {
  const emailToken = generateEmailToken();
  const tokenExpiration = add(new Date(), {
    minutes: EMAIL_TOKEN_EXPIRATION_MINUTES,
  });
  const token = await prisma.token.create({
    data: {
      emailToken: emailToken,
      type: TokenType.EMAIL,
      expiration: tokenExpiration,
      client: {
        connectOrCreate: { 
          where: {
            email: client.email
          },
          create: client,
        },
      },
    },
  });

  if (token) {
    let sendResult = await sendEmailToken(client.email, emailToken);
    let response = createApiResponse<boolean>(true, 200);
    return response;
  }

  let response = createApiResponse<boolean>(null, 403, "Register failed");
  return response;
};

// Generate a random 8-digit number as the email token
function generateEmailToken(): string {
  return Math.floor(10000000 + Math.random() * 90000000).toString();
}

export const SignInHandler = async (email: string, emailToken: string): Promise<ApiResponse<string>> => {
  // Get short-lived email token
  console.log(emailToken);
  const fetchedEmailToken = await prisma.token.findUnique({
    where: {
      emailToken: emailToken,
    },
    include: {
      client: true,
    },
  });

  if (!fetchedEmailToken) {
    // If the token doesn't exist or is not valid, return 403 unauthorized
    let response = createApiResponse<string>(null, 403, "The token doesn't exist.");
    return response;
  }

  if (!fetchedEmailToken.valid) {
    // If the token is not valid, return 403 unauthorized
    let response = createApiResponse<string>(null, 403, "The token is not valid.");
    return response;
  }

  if (fetchedEmailToken.expiration < new Date()) {
    // If the token has expired, return 403 unauthorized
    let response = createApiResponse<string>(null, 403, "The token has expired.");
    return response;
  }

  // If token matches the user email passed in the payload, generate a long-lived API token
  if (fetchedEmailToken.client?.email !== email) {
    let response = createApiResponse<string>(null, 403, "The token does not match the user email.");
    return response;
  }

  const tokenExpiration = add(new Date(), {
    days: AUTHENTICATION_TOKEN_EXPIRATION_DAYS,
  });

  // Persist token in the DB so it's stateful
  const createdToken = await prisma.token.create({
    data: {
      type: TokenType.API,
      expiration: tokenExpiration,
      clientId: fetchedEmailToken.clientId,
    },
  });

  // Invalidate the email token after it's been used
  await prisma.token.update({
    where: {
      id: fetchedEmailToken.id,
    },
    data: {
      valid: false,
    },
  });

  const jwtPayload = {
    role: Role.CLIENT,
    adminId: fetchedEmailToken.client.adminId,
    tokenId: createdToken.id,
    clientId: fetchedEmailToken.clientId,
  };

  const authToken = generateAuthToken(jwtPayload, "60d");
  globalAuthToken = authToken;
  await sendJwtToken(fetchedEmailToken.client.email, authToken);
  let response = createApiResponse<string>(authToken, 200);
  return response;
};

export const validateAPIToken = async (authToken: string): Promise<ApiResponse<Client | null>> => {
  // Verify token
  console.log(authToken);
  let payload;
  try {
    payload = jwt.verify(authToken, process.env.JWT_SECRET);
    console.log(process.env.JWT_SECRET);
  } catch (error) {
    let response = createApiResponse<Client | null>(null, 403, "Invalid token");
    return response;
  }

  if (!payload) {
    let response = createApiResponse<Client | null>(null, 403, "Payload doesn't exist");
    return response;
  }

  if (payload.role === Role.CLIENT || payload.role === Role.ADMIN) {
    let tokenId = payload.tokenId;
    const tokenObj = await prisma.token.findUnique({
      where: {
        id: tokenId,
      },
      include: {
        client: {
          select: {
            id: true,
            name: true,
            createdAt: true,
            updatedAt: true,
            email: true,
            phone: true,
            checkIn: true,
            checkOut: true,
            photo: true,
            credit: true,
            room: true,
            adminId: true,
          },
        },
      },
    });

    if (!tokenObj || !tokenObj.valid) {
      // If the token is not valid, return 403 unauthorized
      let response = createApiResponse<Client | null>(null, 403, "The token is not valid");
      return response;
    }

    const client = tokenObj.client;
    console.log(client);
    if (!client) {
      let response = createApiResponse<Client | null>(null, 403, "Client not found");
      return response;
    }

    let response = createApiResponse<Client | null>(client, 200);
    return response;
  }

  let response = createApiResponse<Client | null>(null, 403, "Invalid token");
  return response;
};


export const getCurrentClient = async (authToken: string): Promise<ApiResponse<Client | null>> => {
  console.log('Auth token:', authToken);

  const validationResponse = await validateAPIToken(authToken);
  console.log('Validation response:', validationResponse);

  if (validationResponse.status !== 200) {
    console.log('Validation failed. Returning error response.');
    return {
      success: false,
      status: validationResponse.status,
      data: null,
    };
  }

  const client = validationResponse.data;
  if (!client) {
    console.log('Client not found. Returning error response.');
    return {
      success: false,
      status: 404,
      data: null,
    };
  }

  // Check if the client is currently connected based on their check-in and check-out dates
  const currentDate = new Date();
  if (client.checkIn && client.checkOut && client.checkIn <= currentDate && currentDate <= client.checkOut) {
    console.log('Client is connected. Returning success response.');
    return {
      success: true,
      status: 200,
      data: client,
    };
  } else {
    console.log('Client is not connected. Returning error response.');
    return {
      success: false,
      status: 401,
      data: null,
    };
  }
};

