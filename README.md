# LOGIN SERVICE  (port 8081)
- [/user/register](http://localhost:8081/user/register) :
    - **POST**
        - body:`{
          "username":/*String*/,
          "email": /*String*/,
          "password":/*String*/
          }`
        - description : This API is used to let the user register himself.

[//]: # (    RICONTROLLARE PER LA QUESTIONE RELATIVA ALL'AGGIORNAMENTO DI GOOGLE)

- [/user/validate](http://localhost:8081/user/validate) :
    - **POST**
        - body:`{
          "provisional_id": /*String*/,
          "activation_code": /*String*/
          }`
        - description : This API is used to prove that the user is the owner of the provided e-mail.

- [/user/login](http://localhost:8081/user/login) :
    - **POST**
        - body:`{
          "username":/*String*/,
          "password": /*String*/
          }`
        - description : This API is used to authenticate the user.

# TRAVELER SERVICE  (port 8082)
... (TO DO)

# TICKETCATALOGUE SERVICE  (port 8083)
... (TO DO)

# PAYMENT SERVICE  (port 8084)

- [/admin/transactions](http://localhost:8084/admin/transactions) :
    - **GET**
      - header : `{"Authorization": /*Admin Bearer Token*/}`
      - description : This API is used to list all transactions.

- [/transactions](http://localhost:8084/transactions) :
    - **GET**
        - header : `{"Authorization": /*Customer Bearer Token*/}`
        - description : This API is used to list all transactions performed by the customer.


# TRANSIT SERVICE  (port 8085)
- [/readers/validate](http://localhost:8085/readers/validate) :
   - **POST**
      - body:`{
            "qrcode" : /*Base64*/,
            "readerID" : /*Long*/ }`
      - description : This API is used to validate the received QRCodes.

- [/admin/all/transits](http://localhost:8085/admin/all/transits) :
  - **POST**
      - body:`{
        "after" : /*Long*/,
        "before" : /*Long*/
        }`
      - header : `{"Authorization": /*Admin Bearer Token*/}`
      - description : This API is used to retrieve the total count of transits given an interval between 2 timestamps (defined as milliseconds since epoch).

- [/admin/user/transits](http://localhost:8085/admin/user/transits) :
    - **POST**
        - body:`{
          "after": /*Long*/ ,
          "before":/*Long*/,
          "user": /*Long*/}`
        - header : `{"Authorization": /*Admin Bearer Token*/}`
        - description : This API is used to retrieve the count of transits for the specific user given an interval between 2 timestamps (defined as milliseconds since epoch).

