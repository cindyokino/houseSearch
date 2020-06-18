# About the project

This is a simulation of an enterprise project, I'm trying to get used to the following technologies in this project:
* Sprints
* Kanban
* REST API using Spring Boot 
* Unit tests with JUnit
* Usage of Mockito, learning Mockito API
* Component tests with Junit
* Code coverage
* Small UI with vanilla JavaScript, HTML and CSS to create calls to the backend


## Sprint 1

### Create a feature to manage house informations:
* Endpoint to register one or more houses (POST "/houses")

Request body:

[
    {
        "id": "14158660",
        "price": "288800",
        "address": "2000, Rue Jean-Baptiste",
        "city": "Starcity",
        "neighborhood": "Quartier Centre"
    },
    {
        "id": "13262772",
        "price": "1120000",
        "address": "864, boulevard Prince",
        "city": "Saint-Louis"
    }
]

Response body:

[
    {
        "id": "14158660",
        "price": "288800",
        "address": "2000, Rue Jean-Baptiste",
        "city": "Starcity",
        "neighborhood": "Quartier Centre"
    },
    {
        "id": "13262772",
        "price": "1120000",
        "address": "864, boulevard Prince",
        "city": "Saint-Louis"
    }
]

* Endpoint to update a house (PUT "/houses") 

Request body:

{
    "id": "14158660",
     "price": "288800",
     "address": "2000, Rue Jean-Baptiste",
      "city": "Starcity",
     "neighborhood": "Quartier Centre"
}

Response body:

{
    "id": "14158660",
     "price": "288800",
     "address": "2000, Rue Jean-Baptiste",
      "city": "Starcity",
     "neighborhood": "Quartier Centre"
}

* Endpoint to delete a house (DELETE "/houses/{id}")
Response: 200 ok

* Endpoint to list all houses, also filter by house price (GET "/houses" or "/houses?minPrice=100000&maxPrice=300000")

Response body:

{
    "id": "14158660",
     "price": "288800",
     "address": "2000, Rue Jean-Baptiste",
      "city": "Starcity",
     "neighborhood": "Quartier Centre"
}

* Endpoint to find house by house id (GET "/houses/{id}")

Response body:

{
    "id": "14158660",
     "price": "288800",
     "address": "2000, Rue Jean-Baptiste",
      "city": "Starcity",
     "neighborhood": "Quartier Centre"
}

## Sprint 2
### Create history of house price changes
* To be defined

