# Étape 1 : Utiliser une image OpenJDK pour construire l'application
FROM openjdk:11-jdk AS builder

# Définir les variables d'environnement Android
ENV ANDROID_COMPILE_SDK 33
ENV ANDROID_BUILD_TOOLS 30.0.3
ENV ANDROID_SDK_TOOLS 7583922_latest
ENV ANDROID_HOME /usr/local/android-sdk

# Mettre à jour le système et installer les outils nécessaires
RUN apt-get update && apt-get install -y wget unzip

# Créer le répertoire cmdline-tools dans ANDROID_HOME
RUN mkdir -p ${ANDROID_HOME}/cmdline-tools

# Créer le répertoire /usr/local/android-sdk à la racine
RUN mkdir -p /usr/local/android-sdk

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

# Changer les permissions du script Gradle et exécuter la construction
RUN chmod +x gradlew 
RUN ./gradlew assembleDebug --stacktrace
RUN ./gradlew build --stacktrace

# Étape 2 : Créer l'image Docker finale
FROM openjdk:11-jdk

# Copier les fichiers de l'application depuis l'étape précédente
COPY --from=builder /app /app

# Configuration du répertoire de travail
WORKDIR /app

# Vous pouvez ajouter d'autres instructions Docker pour déployer votre application ici

# Par exemple, si vous exécutez votre application avec une commande spécifique, vous pouvez l'ajouter ici
# CMD ["java", "-jar", "votre_application.jar"]

# Port par défaut à exposer
# EXPOSE 8080

# Autres instructions Docker pour la configuration de votre application

# Exemple d'instruction ENTRYPOINT si nécessaire
# ENTRYPOINT ["votre_script_d'entrée.sh"]
