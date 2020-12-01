# Sales Statistics

The application can be run via the following command: ./mvnw spring-boot:run

You are working at eBay and your team is asked to build a microservice that will calculate
real-time statistics of item sales on eBay marketplace platform. This microservice will feed
data to a dashboard installed in a business team’s room.
The microservice shall have a REST interface with two endpoints. The first endpoint will be
called by the checkout service whenever a new payment is received and the second
endpoint will provide statistics about the total order amount and average amount per order
for the last 60 seconds. (Orders between t and t - 60 sec, given t = request time)
