# Web API - Json Util

An example of a Web API tool to compare Json.

#### Technologies
- Gradle
- JDK 1.8
- Spring Boot
- Spring-Web
- Mockito
- Junit
- H2 (Database)

### Installation
#### Requirements
- JDK 1.8

Executing the command below, it'll install all the project dependencies and build the package.

```
    ./gradlew build
```

### Running

```
    java -jar build/libs/jsonutil-1.0-SNAPSHOT.jar
```


### How to compare the Json payloads

1. First of all you need to be authenticated before use the Web Api. The application is using the **Basic Auth** authentication.

    ```
    Basic Auth: services:123456
    ```

2. Second of all, you need to register a entry point
    ```
    [POST] http://localhost:8080/api/v1/diff

    [Payload]
    {
      "name": "Compare orders"
    }
    ```

3. Now we need to register two payloads, one to be the left payload and another as the right payload.

    1. To register the left payload, you need to inform the *id* of the entry created in the previous step.

        ```
        [POST] http://localhost:8080/api/v1/diff/{id}/left

        [Payload]
        {
            "order": 10,
            "client": {
                "document": "123456",
                "name": "Jhon Doe"
            },
            "payment": {
                "type": "Credit Card",
                "value": 15.30
            }
        }
        ```

    2. Once again, to register the right payload, you need to inform the *id* gathered into the step 2.
        ```
        [POST] http://localhost:8080/api/v1/diff/{id}/right

        [Payload]
        {
            "order": 10,
            "client": {
                "document": "123456",
                "name": "Jhon F. Doe"
            },
            "seller": {
                "id": 51,
                "name": "Walmart.com"
            },
            "payment": {
                "type": "Credit Card",
                "value": 16.30
            }
        }
        ```

4. Now we are able to perform the comparison between the json payloads registered. You
need to inform the *id* obtained into the step 2.

    ```
    [GET] http://localhost:8080/api/v1/diff/{id}

    [Result]
    {
      "identical": false,
      "sameSize": false,
      "entriesOnlyOnLeft": {},
      "entriesOnlyOnRight": {
        "seller": {
          "id": 51,
          "name": "Walmart.com"
        }
      },
      "entriesInCommon": {
        "order": 10
      },
      "entriesDiffering": {
        "client": {
          "left": {
            "document": "123456",
            "name": "Jhon Doe"
          },
          "right": {
            "document": "123456",
            "name": "Jhon F. Doe"
          }
        },
        "payment": {
          "left": {
            "type": "Credit Card",
            "value": 15.3
          },
          "right": {
            "type": "Credit Card",
            "value": 16.3
          }
        }
      },
      "payloadLeftSize": 138,
      "payloadRightSize": 194
    }
    ```

5. Suggestions to improve the Json Tool Web Api:
    * Change the storage from a **database** to a **cache**, like *redis* or *memcached*
    * Change the authentication mechanism and enable SSL, from Basic Auth to a more secured approach. You can find an example [here](https://github.com/antonioreuter/hateoas-oms-sec).
    * Create new endpoints to perform more operations related to Json







