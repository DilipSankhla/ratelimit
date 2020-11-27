# API Rate Limit Service

API Rate limiter is a REST application which will rate limit API calls a particular  
for a given user and API name. 

The call limit and time window for calls are configurable for client + API name.

It has 2 variants single and multi node. 
* Single node application can works with in-memory hash map to store client call details
where as Multi node version uses redis to store call details.
* Mode of use can be configure using property in application.properties before application startup

All client meta data API name, calls allowed and time window are stored in Redis.
This data store is also configurable. 

  
 
## Installation

Run docker-compose up

OR

Run ApiRateLimitApplication application. Application require redis running locally on default port.

## Usage

```
To set client meta data

curl -vvv -X POST "http://localhost:8080/clientmetadata/calllimit?apiname=/this/get/something&clientid=xxxx&calllimit=5”

To Test application

curl -vvv "http://localhost:8080/ratelimit?apiname=/this/get/something&clientid=xxxx"

```

## ToDo
* Dockerization