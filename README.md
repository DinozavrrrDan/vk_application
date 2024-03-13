# REST API для перенаправления запросов 
 [![Java CI with Maven](https://github.com/DinozavrrrDan/vk_application/actions/workflows/maven.yml/badge.svg)](https://github.com/DinozavrrrDan/vk_application/actions/workflows/maven.yml)
 
REST API для перенаправления запросов на https://jsonplaceholder.typicode.com/ с базовой авторизацией, ведением аудита действий, inmemmory cache для уменьшения числа запросов и использованием баз данных для хравнения аккаунтов и аудита.

Реализованы следующие обработчики (GET, POST, PUT, DELETE), которые проксируют запросы к https://jsonplaceholder.typicode.com/: 
- /api/posts/**
- /api/users/**
- /api/albums/**
  
Также реализованы для работы с аккаунтами и получения аудита действий:
- /api/accounts/**
- /api/audit/**

В проекте присутствует ролевая модель:
- ROLE_ADMIN - имеет доступ ко всем обработчикам, созданию и мониторингу пользователей и получение информации аудита действий.
- ROLE_ALBUMS_ADMIN, ROLE_POSTS_ADMIN, ROLE_USERS_ADMIN - имеют полный доступ к работа с соответствующими обработчикам.
- ROLE_ALBUMS_VIEWER, ROLE_POSTS_VIEWER, ROLE_USERS_VIEWER - имеют доступ к просмотру информации о соответствующих обработчиках.

Присутствует базовый аккаунт admin:admin с ролью ROLE_ADMIN.

Написанный код покрыт unit тестами.
