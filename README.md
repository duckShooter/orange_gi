# Toy Store RESTful API
I've divided this doc into several small sections for clarity sake

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

---

## __Deployment__
I'm using __Maven__ as build tool and __Tomcat 9__ as servlet container for deployment  
You can deploy on another application server if you like, nonetheless, I only use Tomcat 
* ### __Configuring the Data Source__
    You need to specify the required information for your data source (i.e. URL, username, password ... etc).  
    you can do so by modifying `source code/Toy Store/src/main/resources/application.yml`

* ### __Packaging with Maven__
    Navigate to the application main directory `source code/Toy Store/`  
    and run the maven package command with -e option
    ```Shell
    mvn clean package -e
    ``` 
    Or alternatively, import the project in your IDE and package using maven run configuration.  
    This process will produce a __WAR__ deployment unit which can be found in the generated  
    directory `source code/Toy Store/target` under the name `toy_store.war`


* ### __Deploying to Tomcat__
    You need to download [Tomcat 9](https://tomcat.apache.org/download-90.cgi)  
    > ❗️ __NOTE__: For earlier versions (8, 7) Tomcat will complain and throw an exception on startup related to one of the JAR files  
    required as a dependency for Hibernate (older versions of that JAR file  will work fine with no exceptions thrown by Tomcat),  
    *However*, this will not affect the execution of the app so you can simply ignore those exceptions or Use Tomcat 9.x.x 

    The easiest way to deploy is by copying the `toy_store.war` file into `tomcat_directory/webapps` folder
    and start tomcat.  
    There are other deployment methods which require extra configuration in maven.

---
## __Running The Application__
With Tomcat running, I'm assuming you're on localhost and Tomcat is configured to run on port 8080 (the default)  
then you can access the application via `localhost:8080/toy_store/`

* ### __Authentication__
    As I mentioned above, all API calls will require authentication  
    I'm using Spring Security to provide simple basic authentication

    Since there's no registeration option to add users, you have to manually add a user in the database
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
      
    You will receive 200 http repsonse upon successful login or 404 http response for wrong credentials indicating  
    that the user wasn't found.  
    Once logged in and authenticated you can access the API `localhost:8080/toy_store/api/...`  
      
    A brief list for the required APIs:
    | API name | URL |
    | --- | --- |
    | Login | `POST localhost:8080/toy_store/login` |
    | Add new category | `PUT localhost:8080/toy_store/api/categories` |
    | List all categories | `GET localhost:8080/toy_store/api/categories` |
    | Delete an existing category | `DELETE localhost:8080/toy_store/api/categories/{{id}}` |
    | Rename an existing category | `POST localhost:8080/toy_store/api/categories/{{id}}?name=` |
    | Add new product | `PUT localhost:8080/toy_store/api/categories/{{id}}`<br>or `PUT localhost:8080/toy_store/api/products` |
    | List all products | `GET localhost:8080/toy_store/api/products` |
    | Delete an exisiting product | `DELETE localhost:8080/toy_store/api/products/{{id}}` |
    | Update existing product | `POST localhost:8080/toy_store/api/products/{{id}}` |
    | List all products in a sepcific category | `GET localhost:8080/toy_store/api/categories/{{id}` |


---
## __Postman Collection__ 
[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/55851a7302a2a4ea8598)  
If you're using Postman to test the API, then I'm already sharing a collection containing all the requests. Launch it directly by clicking the button above.

---


