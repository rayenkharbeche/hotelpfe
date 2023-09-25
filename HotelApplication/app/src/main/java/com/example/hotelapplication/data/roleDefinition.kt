package com.example.hotelapplication.data

import com.example.hotelapplication.model.Role

class roleDefinition {

    // command roles
    val addCommandAuth = listOf(Role.CLIENT)
    val getCommandAuth = listOf(Role.ADMIN, Role.SERVICEPROVIDER)
    val updateCommandAuth = listOf(Role.SERVICEPROVIDER)

    // category roles
    val addCategoryAuth = listOf(Role.SERVICEPROVIDER)
    val getCategoryAuth = listOf(Role.SERVICEPROVIDER, Role.CLIENT)
    val updateCategoryAuth = listOf(Role.SERVICEPROVIDER)
    val deleteCategoryAuth = listOf(Role.SERVICEPROVIDER)

    // EXTRA service roles
    val addServiceAuth = listOf(Role.ADMIN)
    val getServiceAuth = listOf(Role.ADMIN, Role.CLIENT)
    val updateServiceAuth = listOf(Role.ADMIN)
    val deleteServiceAuth = listOf(Role.ADMIN)

    // items roles
    val addItemAuth = listOf(Role.SERVICEPROVIDER)
    val getItemAuth = listOf(Role.SERVICEPROVIDER)
    val updateItemAuth = listOf(Role.SERVICEPROVIDER)
    val deleteItemAuth = listOf(Role.SERVICEPROVIDER)

    // service provider roles
    val getServiceProviderByIdAuth = listOf(Role.ADMIN)
    val getServiceProviderAuth = listOf(Role.ADMIN)
    val updateServiceProviderAuth = listOf(Role.ADMIN)
    val deleteServiceProviderAuth = listOf(Role.ADMIN)
    val addServiceProviderAuth = listOf(Role.ADMIN)

    // client roles
    val getClientAuth = listOf(Role.ADMIN)
    val getClientByIdAuth = listOf(Role.CLIENT, Role.ADMIN)
    val updateClientAuth = listOf(Role.ADMIN, Role.CLIENT)

    // admin roles
    val updateClientStatusAuth = listOf(Role.ADMIN)

    // history roles
    val getCommandByqueryAuth = listOf(Role.CLIENT, Role.ADMIN)
}