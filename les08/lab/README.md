# Отчет о лабораторной работе №4
 
## Цель работы
 
Выполнить рефакторинг проекта магазина зоотоваров: перейти с Spring JDBC на ORM Hibernate и Spring Data JPA, расширить приложение новыми сущностями и привести структуру в соответствие со слоистой архитектурой.

## Выполнение работы

1. Проект организован в соответствии со слоистой архитектурой

![img1.png](img1.png)

2. В классе `AppConfig` настроен `HikariDataSource` с базой данных H2 в памяти, а также `LocalContainerEntityManagerFactoryBean` для работы с Hibernate. Схема базы данных создаётся автоматически на основе JPA-аннотаций `hibernate.hbm2ddl.auto=create-drop`

![img2.png](img2.png)

3. В пакете `ru.bsuedu.cad.lab.entity` созданы следующие сущности

![img3.png](img3.png)

4. В пакете `ru.bsuedu.cad.lab.repository` определены интерфейсы для каждой сущности с методами `save`, `findById`, `findAll` и дополнительными методами поиска. Реализации в подпакете `impl` используют `EntityManager` напрямую через `@PersistenceContext`

![img4.png](img4.png)

5. В пакете `ru.bsuedu.cad.lab.service` реализованы два сервиса

- **DataLoaderService** — загружает начальные данные из CSV-файлов (`category.csv`, `customer.csv`, `product.csv`) в транзакции через `@Transactional`.
- **OrderService** — создаёт заказ в рамках транзакции: проверяет наличие товара, сохраняет `Order` и `OrderDetail`, обновляет остатки на складе. Также предоставляет метод получения всех заказов.

6. Класс `OrderApplication` 

- Загружает данные из CSV через `DataLoaderService`
- Выводит список категорий, покупателей и товаров
- Создаёт новый заказ для покупателя Артём Захаров (товары: Монитор UltraView 34 x1, Клавиатура Click-Z x2, Аккумулятор Nova 20k x3)
- Выводит информацию о созданном заказе
- Проверяет сохранение заказа, получая все заказы из БД
- Показывает обновлённые остатки товаров на складе
Создание заказа выполняется в рамках транзакции (`@Transactional` в `OrderService`).

7. UML-диаграмма классов

```mermaid
classDiagram

%% ========================
%% Основные классы
%% ========================

    class App {
        +main(String[] args)
    }

    class AppConfig {
        +dataSource() DataSource
    }

%% ========================
%% Сущности
%% ========================

    class Product {
        -long id
        -String name
        -String description
        -double price
        -int stockQuantity
        -long categoryId
    }

    class Category {
        -long id
        -String name
        -String description
    }

%% ========================
%% Интерфейсы
%% ========================

    class Reader {
        <<interface>>
        +read() List~String~
    }

    class Parser {
        <<interface>>
        +parse(List~String~) List~Product~
    }

    class Renderer {
        <<interface>>
        +render(List~Product~)
    }

    class ProductProvider {
        <<interface>>
        +getProducts() List~Product~
    }

%% ========================
%% Реализации Reader
%% ========================

    class ResourceFileReader {
        -String filename
        +read() List~String~
    }

    class CategoryFileReader {
        -String filename
        +read() List~String~
    }

    Reader <|.. ResourceFileReader
    Reader <|.. CategoryFileReader

%% ========================
%% Реализация Parser
%% ========================

    class CSVParser {
        +parse(List~String~) List~Product~
    }

    Parser <|.. CSVParser

    class ConcreteProductProvider {
        -Reader reader
        -Parser parser
        +getProducts() List~Product~
    }

    ProductProvider <|.. ConcreteProductProvider
    ConcreteProductProvider --> Reader
    ConcreteProductProvider --> Parser


    class ConcreteCategoryProvider {
        -Reader reader
        +getCategories() List~Category~
    }

    ConcreteCategoryProvider --> Reader
    ConcreteCategoryProvider --> Category


    class ConsoleTableRenderer {
        +render(List~Product~)
    }

    class HTMLTableRenderer {
        +render(List~Product~)
    }

    class DataBaseRenderer {
        -DataSource dataSource
        -ConcreteCategoryProvider categoryProvider
        +render(List~Product~)
    }

    Renderer <|.. ConsoleTableRenderer
    Renderer <|.. HTMLTableRenderer
    Renderer <|.. DataBaseRenderer

    DataBaseRenderer --> ConcreteCategoryProvider
    DataBaseRenderer --> Product
    DataBaseRenderer --> Category


    class CategoryRequest {
        -DataSource dataSource
        +execute()
    }

    class ParsingTimeAspect {
        +measureTime(ProceedingJoinPoint)
    }

    ParsingTimeAspect ..> CSVParser
    App --> ProductProvider
    App --> Renderer
    App --> CategoryRequest
    App --> AppConfig
```

## Выводы
 
В ходе выполнения лабораторной работы приложение было переведено с Spring JDBC на Hibernate/JPA. Создана полная схема базы данных из пяти связанных таблиц. Данные загружаются из CSV-файлов, заказ успешно сохраняется в H2 и подтверждается повторным чтением из базы.
