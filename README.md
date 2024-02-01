# REST-сервис управления хостелом

REST-сервис для управления данными о комнатах и постояльцах в хостеле.

## Используемые технологии

* Java 17
* Maven
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Liquibase
* MapStruct
* Docker
* Docker Compose

## Методы

### Комнаты

#### Получить все комнаты

* Получение списка всех комнат.
* Фильтрация по типу комнаты (мужская, женская).
* Фильтрация по наличию свободных мест.
* Фильтрация по типу комфорта.

#### Добавить комнату

* Добавление новой комнаты в систему.

#### Изменить данные комнаты

* Изменение информации о комнате.

#### Удалить комнату

* Удаление комнаты из системы, если в ней нет постояльцев.

### Постояльцы

#### Получить всех постояльцев

* Получение списка всех постояльцев.
* Фильтрация по полу.
* Фильтрация по комнате для просмотра всех жильцов.
* Фильтрация по комнате и типу комфорта для просмотра жильцов.

#### Добавить постояльца

* Добавление нового постояльца в подходящую комнату с доступными местами.

#### Изменить данные постояльца

* Изменение информации о постояльце.

#### Удалить постояльца

* Удаление постояльца из комнаты.

## Дополнительные ограничения

* Постоялец может быть добавлен только в комнату соответствующего типа (мужская, женская).
* Постоялец может быть добавлен в комнату только при наличии свободных мест.

## Запуск приложения

* Клонировать репозиторий из GitHub.
* Убедитесь, что у вас установлены Docker и Docker Compose.
* Запустите `docker-compose up` в директории проекта.
* Приложение будет доступно по адресу [http://localhost:8080](http://localhost:8080).

