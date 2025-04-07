# Приложение для учета питания и калорий

## Описание проекта

Приложение представляет собой REST API сервис для учета питания и калорий. Пользователи могут регистрироваться, указывать свои параметры (возраст, вес, рост, цель), на основе которых система автоматически рассчитывает дневную норму калорий. Пользователи могут добавлять блюда, создавать приемы пищи, состоящие из нескольких блюд, и отслеживать свое питание.

### Основные функции:

- Регистрация и управление пользователями
- Создание и управление блюдами с указанием калорийности и БЖУ
- Создание приемов пищи (завтрак, обед, ужин, перекус)
- Отслеживание дневной нормы калорий
- Формирование отчетов о питании за день
- Проверка соответствия дневной норме калорий
- История питания за период

## Структура проекта

```
src/main/java/testtask/testtask/
├── controller/         # REST контроллеры
├── dto/                # Объекты передачи данных
├── exception/          # Обработка исключений
├── model/              # Сущности базы данных
├── repository/         # Репозитории для работы с БД
├── service/            # Бизнес-логика
└── TestTaskApplication.java  # Точка входа
```

## Инструкция по запуску

### 1. Клонирование репозитория

```bash
git clone <url-репозитория>
cd testTask
```

### 2. Сборка проекта

```bash
mvn clean install
```

### 3. Запуск приложения

```bash
mvn spring-boot:run
```

или

```bash
java -jar target/testtask-0.0.1-SNAPSHOT.jar
```

### 4. Доступ к API

После запуска приложение будет доступно по адресу: http://localhost:8080

## API Endpoints

### Пользователи

- `POST /api/users/register` - Регистрация нового пользователя
- `GET /api/users/{id}` - Получение информации о пользователе
- `GET /api/users` - Получение списка всех пользователей
- `GET /api/users/{id}/profile` - Получение профиля пользователя
- `PUT /api/users/{id}/profile` - Обновление профиля пользователя
- `DELETE /api/users/{id}` - Удаление пользователя

### Блюда

- `POST /api/dishes` - Создание нового блюда
- `GET /api/dishes/{id}` - Получение информации о блюде
- `GET /api/dishes` - Получение списка всех блюд
- `GET /api/dishes/search?name=...` - Поиск блюд по названию
- `PUT /api/dishes/{id}` - Обновление информации о блюде
- `DELETE /api/dishes/{id}` - Удаление блюда

### Приемы пищи

- `POST /api/meals` - Создание нового приема пищи
- `GET /api/meals/{id}` - Получение информации о приеме пищи
- `GET /api/meals/user/{userId}` - Получение всех приемов пищи пользователя
- `GET /api/meals/user/{userId}/date/{date}` - Получение приемов пищи пользователя за день
- `GET /api/meals/user/{userId}/calories/date/{date}` - Подсчет калорий за день
- `GET /api/meals/user/{userId}/within-norm/date/{date}` - Проверка соответствия норме калорий
- `PUT /api/meals/{id}` - Обновление информации о приеме пищи
- `DELETE /api/meals/{id}` - Удаление приема пищи

### Отчеты

- `GET /api/reports/daily/{userId}/date/{date}` - Отчет о питании за день
- `GET /api/reports/compliance/{userId}/date/{date}` - Проверка соответствия норме калорий
- `GET /api/reports/history/{userId}/from/{startDate}/to/{endDate}` - История питания за период

## Примеры запросов

### Регистрация пользователя

```json
POST /api/users/register
{
  "name": "Иван Иванов",
  "email": "ivan@example.com",
  "password": "password123",
  "age": 30,
  "gender": "MALE",
  "height": 180,
  "weight": 80.0,
  "activityLevel": "MODERATE",
  "goal": "WEIGHT_LOSS"
}
```

### Создание блюда

```json
POST /api/dishes
{
  "name": "Овсяная каша",
  "calories": 88,
  "proteins": 3.2,
  "fats": 1.8,
  "carbohydrates": 14.5
}
```

### Создание приема пищи

```json
POST /api/meals
{
  "userId": 1,
  "dateTime": "2023-05-15T08:30:00",
  "mealType": "BREAKFAST",
  "dishIds": [1, 2, 3]
}
```