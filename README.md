# Introduction

This code is an accompaniment for [Deep Crawling the Web: A Comprehensive Guide with JAVA Spring Boot and Crawlbase Crawler](https://crawlbase.com/blog/deep-crawling-with-spring-boot-and-crawler) blog.


# Getting Started

### Softwares Needed

1. [JAVA 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
2. [Spring Tool Suites IDE (STS)](https://spring.io/tools)
3. [Maven](https://maven.apache.org/)
4. [MySQL 8.0 or higher version](https://dev.mysql.com/downloads/mysql/)
5. [Ngrok](https://ngrok.com/docs/getting-started/)

### Create a Database in MySQL

Create a database with the name of your choosing in MySQL which you will set in `application.properties` file

```sql
CREATE DATABASE database_name;
```

### Update Settings

In `application.properties`, configure the settings along with `token` and `crawler` as per your created [Crawlbase Crawler](https://crawlbase.com/dashboard/crawler/crawlers)


Example:

```bash
# PROJECT_FOLDER/src/main/resources/application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/<database_name>
spring.datasource.username=<MySQL_username>
spring.datasource.password=<MySQL_password>

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update

crawlbase.token=<Your_Crawlbase_Normal_Token>
crawlbase.crawler=<Your_TCP_Crawler_Name>

logging.file.name=logs/<log-file-name>.log
```

Note: At starting, just give dummy name to the crawler. After successfully running this Application create the crawler with the webhook provided by this Application. `@POST /webhook/crawlbase`

### Download JARS using Maven

Build the project using Maven by running following command in project directory.

```bash
mvn clean install
```

### Start ngrok (For local Testing)

Run the ngrok on port 8080. Open a new terminal and run the command below:

```bash
$ ngrok http 8080
```

Ngrok will provide you public forwarding url which you canuse to make your webook public for 2 hours. You will use this address as base address for the webhook and create the crawler after running the project. e.g `forwarding_url/webhook/crawlbase`. 

### Running the Project

You can run the project by choosing "Spring Boot App" from "Run As" menu with will appear by right clicking on the project inside Spring Tool Suite. Locally Apache Tomcat will run the app on port 8080


### List of available APIs

#### API to push Urls to Crawler

- **URL**: `POST /scrape/push-urls`
- **Headers**:

```json
{
  "Content-Type": "application/json"
}
```

- **Body**:

```json
{
  "urls": ["List of Urls"]
}
```

#### API for Webhook

- **URL**: `POST /webhook/crawlbase`
- **Headers**:

```json
{
  "Content-Type": "text/plain",
  "Content-Encoding": "gzip",
  "Original-Status": 200,
  "PC-Status": 200,
  "rid": "The RID you received in the push call",
  "url": "The URL which was crawled",
  "type": "parent or child"
}
```

- **Body**: `The HTML of the page`
  
### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.2/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.1.2/reference/htmlsingle/index.html#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.1.2/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
