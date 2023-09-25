import { CommandStatus, TokenType } from "@prisma/client";

export type ItemCategory = {
  id: string;
  createdAt?: Date;
  updatedAt?: Date;
  name: string;
  serviceId: string;
  items?:Item[];
};

export type ExtarServices = {
  id: string;
  name: string;
  phone: string;
  photo: string;
  timeIn: string;
  timeOut: string;
  status: boolean;
  adminId: string;
  description:string;
};

export type Admin = {
  id: string;
  name: string;
  email: string;
  password: string;
}
export type ServiceProvider = {
  id: string;
  name: string;
  email: string;
  password: string;
  serviceId: string;
  adminId: string;
};


export type Item = {
  id: string;
  name: string;
  description: string;
  photo: string;
  price: number;
  status: boolean;
  serviceId: string;
  itemCategoryId: string;
  //commandIds?:string[];
};
export type Client = {
  id: string
  name: string
  createdAt: Date
  updatedAt: Date
  email: string
  phone: string
  checkIn: Date
  checkOut: Date
  photo: string
  credit: number
  room: number
  adminId: string
  tokens?:Token[]
};

export type Command = {
  id: string
  date: Date
  createdAt: Date
  updatedAt: Date
  treatementDuration: string
  totalPrice: number
  commandStatus: CommandStatus
  itemIds: string[]
  providerId: string
  clientId: string
  items?: Item[]
};

export type Token = {
  id?: string;
  createdAt?: Date;
  updatedAt?: Date;
  type: TokenType;
  emailToken?: string | null;
  valid?: boolean;
  expiration: Date;
  clientId?: string;
}

export interface ApiResponse<T> {
  success: boolean;
  data?: T | null;
  error?: string | null;
  status: number;
}

export enum Role {
  ADMIN = "ADMIN",
  CLIENT = "CLIENT",
  SERVICEPROVIDER = "SERVICEPROVIDER",
}

export interface CommandQuery{
  //id?:string,
  providerId?:string,
  clientId?:string,
  commandStatus?:CommandStatus,
}