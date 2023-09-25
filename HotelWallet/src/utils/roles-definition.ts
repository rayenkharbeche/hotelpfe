import { Role } from "../models/models";

//command roles
export const addCommandAuth=[Role.CLIENT];
export const getCommandAuth=[Role.ADMIN,Role.SERVICEPROVIDER];
export const updateCommandAuth=[Role.SERVICEPROVIDER];
//category roles
export const addCategoryAuth=[Role.SERVICEPROVIDER];
export const getCategoryAuth=[Role.SERVICEPROVIDER,Role.CLIENT];
export const updateCategoryAuth=[Role.SERVICEPROVIDER];
export const deleteCategoryAuth=[Role.SERVICEPROVIDER];
// EXTRA service roles
export const addServiceAuth=[Role.ADMIN];
export const getServiceAuth=[Role.ADMIN ,Role.CLIENT];
export const updateServiceAuth=[Role.ADMIN];
export const deleteServiceAuth=[Role.ADMIN];
//items roles
export const addItemAuth=[Role.SERVICEPROVIDER];
export const getItemAuth=[Role.SERVICEPROVIDER];
export const updateItemAuth=[Role.SERVICEPROVIDER];
export const deleteItemAuth=[Role.SERVICEPROVIDER];
//service provider roles


export const getServiceProviderByIdAuth=[Role.ADMIN];

export const getServiceProviderAuth=[Role.ADMIN];
export const updateServiceProviderAuth=[Role.ADMIN];
export const deleteServiceProviderAuth=[Role.ADMIN];
export const addServiceProviderAuth=[Role.ADMIN];
//client roles 
export const getClientAuth=[Role.ADMIN];
export const getClientByIdAuth=[Role.CLIENT,Role.ADMIN];
export const updateClientAuth=[Role.ADMIN,Role.CLIENT];

//admin roles

export const updateClientStatusAuth=[Role.ADMIN];

//history roles 
export const getCommandByqueryAuth=[Role.CLIENT,Role.ADMIN];

//admin/signUp:open
//admin/signIn:open

//client/sigUp:open
//client/signIn;open

//serviceProvider/signUp:open
//serviceProvider/signIp:open
