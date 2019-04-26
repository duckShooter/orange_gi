# Toy Store RESTful API
I've divided this doc into several small sections for clarity sake

* [Frontend](#frontend)
* [Deployment](#deployment)
* [Running The Application](#running-the-application)
* [Postman Collection](#postman-collection)

## __Business Assupmtions__
I came up with a couple of assumptions regarding the problem domain to address a few unclear requirements
1. A Product can belong to only one category, that is, a many-to-one relationship
2. Uncategorized products are not supported, so a product must exist within a category (as per your requiremnt),  
which implies that when a category is deleted all containted products will be also deleted

---

## __API Design Decisions__
1. the REST API is designed to accept JSON only as a payload for resource creation/update
2. A client must be authenticated first before using any of the API endpoints, this is done through 
using the Login API with the correct credentials. 
    > Basic authentication makes the Login API somehow useless. Refer to [issue #1](https://github.com/duckShooter/orange_gi/issues/1) and this [commit](https://github.com/duckShooter/orange_gi/commit/563af214d21c37895204d09480bbb16e182218b7)
---

## __Note About The Population of Database Tabels__
The database will be automatically created by Hibernate/ORM if not constructed already using the script in `database/schema.sql` file.

I've provided a `database/data.sql` file which contains sql scripts for populating tables with data manually.  
Alternatively, if you don't manually add any data in the database, the application will bootstrap it with data
automatically.  

---
## __Frontend__
I'm using __Angular 7__, You'll need Angular CLI or npm for building the application (use whichever you prefer). 

* ### __Cross Origin__ (BACKEND)
    I'm using @CrossOrigin annotation on controllers to configure them for cross origin requests (no CORS filters used as not needed)  
    The backend is configured to allow CORS to any host just in case you're using something other than `localhost:4200` to run Angular app so you don't have to modify anything in the backend.

Steps (with CLI):
1. This step is required to build the `node_modules` folder:
    - Create a new angular app (CLI command `ng new your-app --skipGit`), delete all files/folders except for `node_moules` and copy the files from `frontend` to the new app folder.
    - __Or__, copy `node_modules` folder from an existing Angular app into `frontend` folder (again, whichever you prefer).  
2. You'll need Angular Materials package (If you don't have it installed) alongside some other packeges (CLI command `npm install --save @angular/material @angular/cdk @angular/animations`).
3. Run with (CLI command `ng serve --open`)   

---

## __Deployment__
I'm using __Maven 3.5__ as build tool and __Tomcat 9__ as servlet container for deployment  
You can deploy on another application server if you like, nonetheless, I only use Tomcat 
* ### __Configuring the Data Source__
    You need to specify the required information for your data source (i.e. URL, schema name, username, password ... etc).  
    you can do so by modifying `source code/Toy Store/src/main/resources/application.yml`

* ### __Packaging with Maven__
    Navigate to the application main directory `source code/Toy Store/`  
    and run the maven package command with error stack trace turned on
    ```Shell
    mvn clean package -e
    ``` 
    or if you like to skip the unit tests run the above command with defining test.skip property
    ```Shell
    mvn clean package -e -Dmaven.test.skip
    ```

    Or alternatively, import the project in your IDE and package using maven run configuration.  
    This process will produce a __WAR__ deployment unit which can be found in the generated  
    directory `source code/Toy Store/target` under the name `toy_store.war`


* ### __Deploying to Tomcat__
    You need to download [Tomcat 9](https://tomcat.apache.org/download-90.cgi). Older versions of Tomcat may also work.
    > ❗️ __NOTE__: For earlier versions (8, 7) Tomcat will complain and throw an exception on startup related to one of the JAR files  required as a dependency for Hibernate (older versions of that JAR file  will work fine with no exceptions thrown by Tomcat),  *However*, __this will not affect the execution of the app__ so you can simply ignore those exceptions or Use Tomcat 9.x.x

    The easiest way to deploy is by copying the `toy_store.war` file into `tomcat_directory/webapps` folder and start tomcat.  
    There are other deployment methods which require extra configuration in maven.

---
## __Running The Application__
With Tomcat running (or the application server of your choice), I'm assuming you're on localhost and Tomcat is configured to run on port 8080 (the default)  
then you can access the application via `localhost:8080/toy_store/`

* ### __Authentication__
    As I mentioned above, all API calls will require authentication  
    I'm using Spring Security to provide simple basic authentication

    Since there's no registeration option to add users, you have to manually add a user in the database  
    ⬆️ __Update:__ This step isn't necessary now, the following user will be automatically persisted in the database please refer to [this note](#note-about-the-population-of-database-tabels).
    ```SQL
    INSERT INTO `user` (`username`, `password`) VALUES ('user', '$2a$10$a8r484Ht4fOSYUbVR3mZZOlMOEJu17PuRakkCBz07dSxrWifU.krK');
    ```
    This is equivalent to
    ```
        login credentials  
        * username: user  
        * password: secret
    ```
    First you need to login through a POST http request to  `localhost:8080/toy_store/login` with JSON payload
    ```javascript
    {
        "username": "user"
        "password": "secret"
    }
    ```
    you can add other users if you like, the password is hashed using bcrypt hashing function with 10 rounds  
      
    You will receive 200 http repsonse upon successful login or 401 http response for wrong credentials indicating  
    that there was no user found for the provided credentials.  
    Once logged in and authenticated you can access the API `localhost:8080/toy_store/api/...`  
      
    A brief list for the required APIs:  

    API name | URL
    --- | --- 
    Login | `POST localhost:8080/toy_store/login`
    Add new category | `PUT localhost:8080/toy_store/api/categories`
    List all categories | `GET localhost:8080/toy_store/api/categories`
    Delete an existing category | `DELETE localhost:8080/toy_store/api/categories/{{id}}`
    Rename an existing category | `POST localhost:8080/toy_store/api/categories/{{id}}?name=`
    Add new product | `PUT localhost:8080/toy_store/api/categories/{{id}}`<br>or `PUT localhost:8080/toy_store/api/products`
    List all products | `GET localhost:8080/toy_store/api/products`
    Delete an exisiting product | `DELETE localhost:8080/toy_store/api/products/{{id}}`
    Update existing product | `POST localhost:8080/toy_store/api/products/{{id}}`
    List all products in a sepcific category | `GET localhost:8080/toy_store/api/categories/{{id}`


---
## __Postman Collection__ 
[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/e3831137ece7101e539b)  
If you're using Postman to test the API, then I'm already sharing a collection containing all the requests. Launch it directly by clicking the button above.