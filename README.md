# notice-management
A REST API implementation of notice management
### Table of contents
- [Features](#features)
- [How to get started](#How-to-get-started)
- [Prerequisites](#prerequisites)
- [Technologies](#technologies)
- [Installation](#installation)
- [Key problem solving strategies and implementation methods](#Key-problem-solving-strategies-and-implementation-methods)
### Features
- User : can get, create, delete a user.
- Notice : can get all notices, get by id, get notice of current user, create, update and delete a notice.
  + Get all notice : Can get all notice is valid ( based on start date, end date, current date ), can filter by title
  + Get all notice by User : Users can view all the notice they have created when they login.
  + Get notice by Id : show information of notice by id.
  + Create a notice : a user can create a notice with multiple file. Input will be validate before creation.
  + Update a notice : a notice will be update by id and modifyBy, modifyDate will be automatically updated.
  + Delete a notice by Id : a notice is deleted when isActive changes its status to false.
- JWT : generate access token and use this token to login.
- Using cache, exception handler, validation request, mapper , auditing.
### How To Get Started
- You need to run project and create one user
- Use information user you just created to get  token by api POST/authenicate
- Use that token to get, delete, update notice and user
### Prerequisites
- JDK 11
- Maven 3
- Mysql
### Technologies
- Spring boot
- JWT
- Hibernate
### Installation
- Clone the repository
```bash
$ git clone https://github.com/huuhiep2505/notice.git
```

- Install dependencies
```bash
$ cd [project_name]
$ mvn install
```
- Create the configuration file and update with your local config
```bash
$ cd src/main/resources
$ cp application-example.properties application.properties
```
- Start Application
```bash
$ mvn spring-boot:run
```
### Key problem solving strategies and implementation methods
- I will save file on server instead of save it on database because It will impact performance on the database.
- To get the author who created the notice I used jwt to be able to get the current user.
- I used logger to recording information that is notified, saved during the operation of an application in a centralized place.
- To resolve large-capacity traffic ( future ) :
  + Using cache to improve data access performance and reduce system load.
  + Using index in DB to queries faster.
  + Using load balancing to efficiently distributing incoming network traffic across a group of backend server.
  

