# Utilisez une image Node.js en tant qu'image de base
FROM node:16

# Définit le répertoire de travail dans le conteneur
WORKDIR /app

# Copie les fichiers du répertoire actuel (où se trouve le Dockerfile) dans le répertoire de travail du conteneur
COPY HotelWallet ./

# Installe les dépendances
RUN npm install

# Expose le port sur lequel l'application va écouter
EXPOSE 3000

# Commande de démarrage de l'application
CMD ["npm", "start"]
