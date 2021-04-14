# Gateways
Hi, this is a simple project to improve my skills with Spring Boot.
It contains my own solution for [a test](TEST.md) requested to get a job in [a software company](https://www.musala.com/).

Is basically made with:

1: Spring MVC

2: Spring DATA JPA.

3: H2 Database

4: Junit5, [OpenPojo](https://github.com/OpenPojo/openpojo) and Mockito

5: Java 11 and Maven 


### Installation and run
Clone the repo and extract

Open a terminal and get to the extracted folder and run:

    ./mvnw spring-boot:run


Testing API with Postman:

#### Gateways
##### ![Get all gateways](/docs/get_gateways.png)

Request

    GET http://localhost:8080/api/gateways

##### ![Create gateway](/docs/post_gateways.png)
Request

    POST http://localhost:8080/api/gateways

Request body:

    {
        "serialNumber": "ssss",
        "name": "name",
        "address": "15.2.5.45"
    }
##### ![Create gateway with peripherals](/docs/post_gateways_with_peripherals.png)

Request
        
    POST http://localhost:8080/api/gateways

Request body:

    {
        "serialNumber": "ssss",
        "name": "name",
        "address": "15.2.5.45",
        "peripherals": [
            {
            "vendor": "vendor 1",
            "status": "OFFLINE"
            }
        ]
    }

##### ![Update gateway](/docs/put_gateways.png)

Request

    PUT http://localhost:8080/api/gateways

Request body:

    {
        "id": {gatewayId} 
        "serialNumber": "ssss",
        "name": "updatedName",
        "address": "15.2.5.45"
    }

##### ![Delete gateway](/docs/delete_gateways.png)

    DELETE http://localhost:8080/api/gateways/{gatewayId}

#### Peripherals
##### ![Get all peripherals](/docs/get_peripherals.png)

Request

    GET http://localhost:8080/api/peripherals

##### ![![Create peripheral](/docs/post_peripherals.png)

Request

    POST http://localhost:8080/api/peripherals

Request body:

    {
        "vendor": "ssss",
        "status": "ONLINE"
    }
##### ![![Update peripheral](/docs/put_peripherals.png)

Request

    PUT http://localhost:8080/api/peripherals

Request body:

    {
        "id": {peripheralId} 
        "vendor": "ssss",
        "status": "ONLINE"
    }
##### ![Delete peripheral](/docs/delete_peripherals.png)

Request

    DELETE http://localhost:8080/api/peripherals
