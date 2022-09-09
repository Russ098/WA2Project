# LOGIN SERVICE  (port 8081)
- [/user/register](http://localhost:8081/user/register) :
    - **POST**
        - body:`{"username":/*String*/,
          "email": /*String*/,
          "password":/*String*/}`
        - description : This API is used to let the user register himself.
        - return value(s):  `{"provisionalId":/*String*/,
          "email": /*String*/}`
- [/admin/register](http://localhost:8081/admin/register) :
    - **POST**
        - header : `{"Authorization": /*SUPER_ADMIN Bearer Token*/}` 
        - body:`{
          "username":/*String*/,
          "email": /*String*/,
          "password":/*String*/, 
          "roles":/*String*/
          }`
        - description : This API is used to allow an _ADMIN_ with enrolling capabilities (a _SUPER_ADMIN_) to create a user with a given set of roles. In this case, no verification is required. 
- [/user/validate](http://localhost:8081/user/validate) :
    - **POST**
        - body:`{
          "provisional_id": /*String*/,
          "activation_code": /*String*/
          }`
        - description : This API is used to prove that the user is the owner of the provided e-mail.
      - return value(s):  `{"id":/*Long*/,
        "username": /*String*/, "email":/*String*/}`
- [/user/login](http://localhost:8081/user/login) :
    - **POST**
        - body:`{
          "username":/*String*/,
          "password": /*String*/
          }`
        - description : This API is used to authenticate the user.
        - return value(s):  `jwt : /*String*/`

# TRAVELER SERVICE  (port 8082)
- [/my/profile](http://localhost:8082/my/profile) :
    - **PUT**
        - header : `{"Authorization": /*CUSTOMER/ADMIN/SUPER_ADMIN Bearer Token*/}`
        - body : `{
          "name": /*String*/,
          "address": /*String*/,
          "date_of_birth": /*Date in format aaaa-MM-dd*/,
          "telephone_number": /*String*/
          }`
        - description : This API is used to allow a _CUSTOMER_ (or higher role) to insert his user details
    - **GET**
        - header : `{"Authorization": /*CUSTOMER/ADMIN/SUPER_ADMIN Bearer Token*/}`
        - description : This API is used to allow a _CUSTOMER_ (or higher role) to see his user details
        - return value(s):  `{
          "id": Long,
          "name": /*String*/,
          "address": /*String*/,
          "date_of_birth": /*Timestamp*/,
          "telephone_number": /*String*/
          }`
- [/my/tickets](http://localhost:8082/my/tickets) :
    - **POST**
        - header : `{"Authorization": /*CUSTOMER/ADMIN/SUPER_ADMIN Bearer Token*/}`
        - body : `{
          "cmd" : "buy_tickets",
          "quantity" : /*Long*/,
          "ticket" : {
          "zid" : /*String*/,
          "exp" : /*Date*/,
          "iat" : /*Date*/,
          "validFrom" : /*Date*/,
          "ticketType" : /*String*/
          }
          }`
        - description : This API is used to add Tickets to the list of Purchased Tickets of a _CUSTOMER_ (or higher role)
    - **GET**
        - header : `{"Authorization": /*CUSTOMER/ADMIN/SUPER_ADMIN Bearer Token*/}`
        - description : This API is used retrieve the list of tickets owned by a _CUSTOMER_ (or higher role)
        - return value(s):  `list of {
          "sub": /*Long*/,
          "iat": /*Timestamp*/,
          "exp": /*Timestamp*/,
          "zid": "/*String*/",
          "validFrom": /*String*/,
          "ticketType": /*String*/,
          "jws": /*String*/,
          "userId": /*Long*/
          }`
        
- [/admin/travelers](http://localhost:8082/admin/travelers) :
  - **GET**
    - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
    - description : This API is used to retrieve the complete list of UserDetails
    - return value(s): `{
      "id": /*Long*/,
      "roles": [
      /*String*/
      ],
      "username": /*String*/
      }`
- [/admin/traveler/{userId}/tickets](http://localhost:8082/admin/traveler/{userId}/tickets) :
    - **GET**
        - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
        - description : This API is used to retrieve the list of Tickets belonging to _userId_
        - return value(s):  `list of {
          "sub": /*Long*/,
          "iat": /*Timestamp*/,
          "exp": /*Timestamp*/,
          "zid": "/*String*/",
          "validFrom": /*String*/,
          "ticketType": /*String*/,
          "jws": /*String*/,
          "userId": /*Long*/
          }`
- [/admin/traveler/{userId}/profile](http://localhost:8082/admin/traveler/{userId}/profile) :
    - **GET**
        - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
        - description : This API is used to retrieve the profile of _userId_
        - return value(s): `{
          "id": /*Long*/,
          "name": /*String*/,
          "address": /*String*/,
          "date_of_birth": /*Timestamp*/,
          "telephone_number": /*String*/
          }`
      
- [/secret](http://localhost:8082/secret) :
    - **GET**
        - header : `{"Authorization": /*DEVICE Bearer Token*/}`
        - description : This API is used to allow a device to obtain the secret to verify the validity of tickets' jwts
        - return value(s): `/*String*/`

- [/admin/getUserPurchases](http://localhost:8082/admin/getUserPurchases) :
    - **POST**
        - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
        - body:`{
          "before": /*Long*/,
          "after": /*Long*/,
          "userId": /*Long*/
        }`
        - description : This API is used to count the tickets bought by a user in an interval of dates expressed as milliseconds since the Unix Epoch
        - return value(s): `/*Int*/`
- [/admin/getAllPurchases](http://localhost:8082/admin/getAllPurchases) :
    - **POST**
        - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
        - body:`{
          "before": /*Long*/,
          "after": /*Long*/
          }`
        - description : This API is used to count the tickets bought by all users in an interval of dates expressed as milliseconds since the Unix Epoch
        - return value(s): `/*Int*/`
- [/my/qrcode/{ticketID}](http://localhost:8082/my/qrcode/{ticketID}) :
    - **GET**
        - description : This API is used to obtain the Base64 encoding of the ticket _ticketID_
        - return value(s): `/*String*/`
    
# TICKETCATALOGUE SERVICE  (port 8083)
- [/tickets](http://localhost:8083/tickets) :
    - **GET**
        - description : This API is used to retrieve all the types of ticket available.
- [/admin/orders](http://localhost:8083/admin/orders) :
    - **GET**
        - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
        - description : This API is used to allow an _ADMIN_ (or higher role) to see all performed orders.
        - return value(s): `list of {
          "id": /*Long*/,
          "userid": /*Long*/,
          "ticketTypeId": /*Long*/,
          "ticketNumber": /*Long*/,
          "status": /*STATUS*/
      }`
- [/orders](http://localhost:8083/orders) :
  - **GET**
    - header : `{"Authorization": /*CUSTOMER/ADMIN/SUPER_ADMIN Bearer Token*/}`
    - description : This API is used to allow a _CUSTOMER_ (or higher role) to retrieve the list of his orders.
    - return value(s): `list of {
      "id": /*Long*/,
      "userid": /*Long*/,
      "ticketTypeId": /*Long*/,
      "ticketNumber": /*Long*/,
      "status": /*STATUS*/
      }`
- [/orders/{orderId}](http://localhost:8083/orders/{orderId}) :
    - **GET**
        - header : `{"Authorization": /*CUSTOMER/ADMIN/SUPER_ADMIN Bearer Token*/}`
        - description : This API is used to allow a _CUSTOMER_ (or higher role) to retrieve _orderId_'s info.
        - return value(s): `{
          "id": /*Long*/,
          "userid": /*Long*/,
          "ticketTypeId": /*Long*/,
          "ticketNumber": /*Long*/,
          "status": /*STATUS*/
          }`
- [/admin/orders/{userId}](http://localhost:8083/admin/orders/{userId}) :
    - **GET**
        - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
        - description : This API is used to allow an _ADMIN_ (or higher role) to retrieve _userId_'s list of orders.
        - return value(s): `list of {
          "id": /*Long*/,
          "userid": /*Long*/,
          "ticketTypeId": /*Long*/,
          "ticketNumber": /*Long*/,
          "status": /*STATUS*/
          }`
- [/admin/tickets](http://localhost:8083/admin/tickets) :
    - **POST**
        - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
        - body: `{
          "ticketType" : /*String*/,
          "price": /*Float*/,
          "ageBelow" : /*Long*/,
          "duration": /*Long*/,
          "zones": /*String*/
          }`
        - description : This API is used to allow an _ADMIN_ (or higher role) to create a new ticket type. _AgeBelow_ represents age limitations on a ticket (-1 means no restriction). _Duration_ represents the validity of the ticket in days since its activation.
  - **PUT**
      - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
      - body: `{
        "id":/*Long*/,
        "ticketType" : /*String*/,
        "price": /*Float*/,
        "ageBelow" : /*Long*/,
        "duration": /*Long*/,
        "zones": /*String*/
        }`
      - description : This API is used to allow an _ADMIN_ (or higher role) to update an existing ticket type.
- [/shop](http://localhost:8083/shop) :
    - **POST**
        - header : `{"Authorization": /*CUSTOMER/ADMIN/SUPER_ADMIN Bearer Token*/}`
        - body: `{
          "ticketId": /*Long*/,
          "ticketNumber": /*Long*/,
          "card":{
          "creditCardNumber": /*String*/,
          "expirationDate": /*Date*/,
          "cvv": /*String*/,
          "cardHolder": /*String*/
          }
          }`
        - description : This API is used to allow a _CUSTOMER_ (or higher role) to purchase _ticketNumber_ tickets of type _ticketId_, providing its credit card information to perform the payment. Credit card's expiration date format must be "YYYY-MM-DD"
        - return value(s): `{ "order_id" : /*Long*/ }`
# PAYMENT SERVICE  (port 8084)
- [/admin/transactions](http://localhost:8084/admin/transactions) :
    - **GET**
      - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
      - description : This API is used to list all transactions.
      - return value(s): `list of {
        "id": /*Long*/,
        "userId": /*Long*/
        }`
- [/transactions](http://localhost:8084/transactions) :
    - **GET**
        - header : `{"Authorization": /*CUSTOMER/ADMIN/SUPER_ADMIN Bearer Token*/}`
        - description : This API is used to list all transactions performed by the customer.
        - return value(s): `list of {
          "id": /*Long*/,
          "userId": /*Long*/
          }`
# TRANSIT SERVICE  (port 8085)
- [/readers/validate](http://localhost:8085/readers/validate) :
   - **POST**
      - body:`{
            "qrcode" : /*Base64*/,
            "readerID" : /*Long*/ }`
      - description : This API is used to validate the received QRCodes.
- [/admin/all/transits](http://localhost:8085/admin/all/transits) :
  - **POST**
      - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
      - body:`{
        "after" : /*Long*/,
        "before" : /*Long*/
        }`
      - description : This API is used to retrieve the total count of transits given an interval between 2 timestamps (defined as milliseconds since epoch).
      - return value(s): `/*Int*/`
- [/admin/user/transits](http://localhost:8085/admin/user/transits) :
    - **POST**
        - body:`{
          "after": /*Long*/,
          "before":/*Long*/,
          "userId": /*Long*/}`
        - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
        - description : This API is used to retrieve the count of transits for the specific user given an interval between 2 timestamps (defined as milliseconds since epoch).
        - return value(s): `/*Int*/`

# REPORT SERVICE (port 8086)
- [/admin/getAllTransits](http://localhost:8086/admin/getAllTransits) :
    - **POST**
      - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
      - body:`{
              "after": /*Long*/,
              "before":/*Long*/,
          }`
      - description : This API is used to retrieve the total count of transits given an interval between 2 timestamps (defined as milliseconds since epoch).
      - return value(s): `/*Int*/`
- [/admin/getUserTransits](http://localhost:8086/admin/getUserTransits) :
    - **POST**
        - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
        - body:`{
          "after": /*Long*/,
          "before":/*Long*/,
          "userId":/*Long*/
          }`
        - description : This API is used to retrieve a user's count of transits given an interval between 2 timestamps (defined as milliseconds since epoch).
        - return value(s): `/*Int*/`
- [/admin/getAllPurchases](http://localhost:8086/admin/getAllPurchases) :
  - **POST**
      - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
      - body:`{
        "after": /*Long*/,
        "before":/*Long*/
        }`
      - description : This API is used to retrieve the total count of purchases given an interval between 2 timestamps (defined as milliseconds since epoch).
      - return value(s): `/*Int*/`
- [/admin/getUserPurchases](http://localhost:8086/admin/getUserPurchases) :
    - **POST**
        - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
        - body:`{
          "after": /*Long*/,
          "before":/*Long*/,
          "userId":/*Long*/
          }`
        - description : This API is used to retrieve a user's count of purchases given an interval between 2 timestamps (defined as milliseconds since epoch).
        - return value(s): `/*Int*/`
- [/admin/getAllPurchasesAndTransits](http://localhost:8086/admin/getAllPurchasesAndTransits) :
    - **POST**
      - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
      - body:`{
            "after": /*Long*/,
            "before":/*Long*/
            }`
      - description : This API is used to retrieve the total count of purchases and transits given an interval between 2 timestamps (defined as milliseconds since epoch).
      - return value(s): `{
        "transits": /*String*/,
        "purchases": /*String*/
        }`
- [/admin/getUserPurchasesAndTransits](http://localhost:8086/admin/getUserPurchasesAndTransits) :
    - **POST**
        - header : `{"Authorization": /*ADMIN/SUPER_ADMIN Bearer Token*/}`
        - body:`{
          "after": /*Long*/ ,
          "before":/*Long*/,
          "userId":/*Long*/
          }`
        - description : This API is used to retrieve a user's count of purchases and transits given an interval between 2 timestamps (defined as milliseconds since epoch).
        - return value(s): `{
          "transits": /*String*/,
          "purchases": /*String*/
          }`
