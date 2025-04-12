
# Используем официальный образ OpenJDK
FROM openjdk:21-jdk-slim AS build
LABEL authors="alexmozhaykin"
# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы Gradle Wrapper и конфигурации
COPY gradlew .
COPY gradlew.bat .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
RUN chmod +x gradlew

# Копируем исходный код проекта
COPY src ./src

# Сборка проекта
RUN ./gradlew build --no-daemon

# Создаем финальный образ
FROM openjdk:21-jdk-slim


# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR-файл из предыдущего этапа
COPY --from=build /app/build/libs/*.jar app.jar

# Указываем порт, который будет использовать приложение
EXPOSE 9080

# Указываем команду для запуска приложения
CMD ["java", "-jar", "app.jar"]