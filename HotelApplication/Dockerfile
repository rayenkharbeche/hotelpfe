# Utiliser l'image de base avec OpenJDK 11
FROM openjdk:11-jdk

# Définir les variables d'environnement Android
ENV ANDROID_COMPILE_SDK 33
ENV ANDROID_BUILD_TOOLS 30.0.3
ENV ANDROID_SDK_TOOLS 7583922_latest
ENV ANDROID_HOME /usr/local/android-sdk

# Ajouter les chemins d'exécution des outils Android à la variable PATH
ENV PATH $PATH:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools

# Mettre à jour le système et installer les outils nécessaires
RUN apt-get update && apt-get install -y wget unzip

# Créer le répertoire cmdline-tools dans ANDROID_HOME
RUN mkdir -p ${ANDROID_HOME}/cmdline-tools

# Téléchargement et extraction des outils Android SDK
RUN wget -q https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_SDK_TOOLS}.zip -O android-sdk.zip && \
    unzip -q android-sdk.zip -d ${ANDROID_HOME}/cmdline-tools && \
    mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest && \
    rm android-sdk.zip

# Accepter automatiquement les licences Android SDK
RUN yes | ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager --licenses

# Configuration du répertoire de travail
WORKDIR /app

# Copier les fichiers de l'application dans le conteneur
COPY HotelApplication /app/

# Changer les permissions du script Gradle
RUN chmod +x gradlew
RUN ./gradlew clean

# Exécuter les étapes Gradle sans --stacktrace (utilisez --info ou --debug au besoin)
RUN ./gradlew build
