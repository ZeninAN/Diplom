# Инструкция по запуску SUT и симулятора
1. Склонировать проект git clone

2. Открыть проект в IDEA

3. Перейти в папку gate-simulator;

4. Запустить банковский симулятор командой npm start;

5. Для запуска контейнеров в docker использовать команду docker-compose up

6. Запустить джарник в общем терминале использовав команду для mysql
java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar aqa-shop.jar

7. В терминале ввести команду ./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"

8. Открыть в Google Chrome ссылку http://localhost:8080

9. После окончания тестов завершить работу приложения (Ctrl+C), остановить контейнеры командой docker-compose down

## Отчеты в MySql

В терминале ввести команду ./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"

## Отчеты в postgresql

Запуск джарника для postgresql
java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar aqa-shop.jar

В терминале ввести команду ./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"
