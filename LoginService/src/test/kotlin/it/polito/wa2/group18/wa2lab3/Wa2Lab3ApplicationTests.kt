package it.polito.wa2.group18.wa2lab3

import it.polito.wa2.group18.wa2lab3.dtos.UserDTO
import it.polito.wa2.group18.wa2lab3.dtos.toDTO
import it.polito.wa2.group18.wa2lab3.entities.Activation
import it.polito.wa2.group18.wa2lab3.entities.Role
import it.polito.wa2.group18.wa2lab3.entities.User
import it.polito.wa2.group18.wa2lab3.repositories.ActivationRepository
import it.polito.wa2.group18.wa2lab3.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.lang.Thread.sleep
import java.util.*


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class SpringApplicationTests
{
    companion object{
        @Container
        val postgres = PostgreSQLContainer<Nothing>("postgres:latest").apply {
            //withDatabaseName("postgres")
            withUsername("postgres")
            withPassword("postgres")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry){
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") {"create-drop"}
        }
    }
    @LocalServerPort
    protected var port : Int = 0
    @Autowired
    lateinit var restTemplate: TestRestTemplate
    @Autowired
    lateinit var userRepo : UserRepository
    @Autowired
    lateinit var actRepo : ActivationRepository
    
    val tempmail = "vofovi8510@carsik.com"

    /******************
     *  REGISTRATION  *
     *****************/

    @Test
    fun emptyUsernameRegistration() {
        val baseUrl = "http://localhost:$port"
        val newuser = UserDTO("",tempmail,"P4\$\$word.", Role.CUSTOMER)
        val request = HttpEntity(newuser)
        val response = restTemplate.postForEntity<Unit>("${baseUrl}/user/register", request )
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
    }
    @Test
    fun emptyEmailRegistration() {
        val baseUrl = "http://localhost:$port"
        val newuser = UserDTO("Monkey D. Luffy","","P4\$\$word.", Role.CUSTOMER)
        val request = HttpEntity(newuser)
        val response = restTemplate.postForEntity<Unit>("${baseUrl}/user/register", request )
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
    }

    @Test
    fun invalidEmailRegistration() {
        val baseUrl = "http://localhost:$port"
        val newuser = UserDTO("Monkey D. Luffy", "pirateking@gmailcom","P4\$\$word.", Role.CUSTOMER)
        val request = HttpEntity(newuser)
        val response = restTemplate.postForEntity<Unit>("${baseUrl}/user/register", request )
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
    }
    @Test
    fun emptyPasswordRegistration() {
        val baseUrl = "http://localhost:$port"
        val newuser = UserDTO("Monkey D. Luffy",tempmail,"", Role.CUSTOMER)
        val request = HttpEntity(newuser)
        val response = restTemplate.postForEntity<Unit>("${baseUrl}/user/register", request )
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
    }
    @Test
    fun weakPasswordRegistration() {
        val baseUrl = "http://localhost:$port"
        val newuser = UserDTO("Monkey D. Luffy",tempmail,"password", Role.CUSTOMER)
        val request = HttpEntity(newuser)
        val response = restTemplate.postForEntity<Unit>("${baseUrl}/user/register", request )
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
    }

    @Test @DirtiesContext
    fun validRegistration()
    {
        val baseUrl = "http://localhost:$port"
        val newuser = UserDTO("Monkey D. Luffy",tempmail,"P4\$\$word.", Role.CUSTOMER)
        val request = HttpEntity(newuser)
        val response = restTemplate.postForEntity<Unit>("${baseUrl}/user/register", request )
        assert(response.statusCode == HttpStatus.ACCEPTED)
    }

    @Test @DirtiesContext
    fun duplicateRegistration()
    {
        val baseUrl = "http://localhost:$port"
        val newuser = UserDTO("Monkey D. Luffy",tempmail,"P4\$\$word.", Role.CUSTOMER)
        val request = HttpEntity(newuser)
        val response = restTemplate.postForEntity<Unit>("${baseUrl}/user/register", request )
        assert(response.statusCode == HttpStatus.ACCEPTED)
        val response2 = restTemplate.postForEntity<Unit>("${baseUrl}/user/register", request )
        assert(response2.statusCode == HttpStatus.BAD_REQUEST)
    }

    @Test @DirtiesContext
    fun duplicateEmailRegistration()
    {
        val baseUrl = "http://localhost:$port"
        val newuser = UserDTO("Monkey D. Luffy",tempmail,"P4\$\$word.", Role.CUSTOMER)
        val request = HttpEntity(newuser)
        val response = restTemplate.postForEntity<Unit>("${baseUrl}/user/register", request )
        assert(response.statusCode == HttpStatus.ACCEPTED)
        val newuser2 = UserDTO("Trafalgar D. Law",tempmail,"P4\$\$word.", Role.CUSTOMER)
        val request2 = HttpEntity(newuser2)
        val response2 = restTemplate.postForEntity<Unit>("${baseUrl}/user/register", request2 )
        assert(response2.statusCode == HttpStatus.BAD_REQUEST)
    }

    @Test @DirtiesContext
    fun duplicateUsernameRegistration()
    {
        val baseUrl = "http://localhost:$port"
        val newuser = UserDTO("Monkey D. Luffy",tempmail,"P4\$\$word.", Role.CUSTOMER)
        val request = HttpEntity(newuser)
        val response = restTemplate.postForEntity<Unit>("${baseUrl}/user/register", request )
        assert(response.statusCode == HttpStatus.ACCEPTED)
        val newuser2 = UserDTO("Monkey D. Luffy",tempmail+"m","P4\$\$word.", Role.CUSTOMER)
        val request2 = HttpEntity(newuser2)
        val response2 = restTemplate.postForEntity<Unit>("${baseUrl}/user/register", request2 )
        assert(response2.statusCode == HttpStatus.BAD_REQUEST)
    }

    /******************
     *  VALIDATION    *
     *****************/

    @Test
    fun validationIdNotExists()
    {
        val baseUrl = "http://localhost:$port"
        val headers = HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        val request = HttpEntity("{\n" +
                "    \"provisional_id\": \"44834edd-74e3-4839-8d1d-313d960dcce7\",\n" +
                "    \"activation_code\": \"MK0QJC\"\n" +
                "}", headers)
        val response = restTemplate.postForEntity<Unit>("${baseUrl}/user/validate", request )
        assert(response.statusCodeValue == 404)
    }

    @Test @DirtiesContext
    fun validationOutOfAttempts()
    {
        class ResponseData (var provisionalId:UUID, var email:String)

        //create new user
        val baseUrl = "http://localhost:$port"
        val newuser = UserDTO("Monkey D. Luffy",tempmail,"P4\$\$word.", Role.CUSTOMER)
        val request = HttpEntity(newuser)
        val response = restTemplate.postForEntity<ResponseData>("${baseUrl}/user/register", request )
        assert(response.statusCode == HttpStatus.ACCEPTED)
        //retrieve activation code (can't from mail)
        var activationData = Activation()
        try { activationData = actRepo.getById(response.body!!.provisionalId)!! }
        catch(e:Exception) { assert(false) }
        //post request
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        for (i in 0..4 )
        {
            val request2 = HttpEntity("{\n" +
                    "    \"provisional_id\": \"${activationData.id}\",\n" +
                    "    \"activation_code\": \"${activationData.activationCode +i}\"\n" +
                    "}", headers)
            val response2 = restTemplate.postForEntity<Unit>("${baseUrl}/user/validate", request2 )
            assert(response2.statusCodeValue == 404)
        }
        try{ actRepo.getById(activationData.id!!) }
        catch(e:Exception) { assert(true)}
    }

    @Test @DirtiesContext
    fun validationOutOfTime()
    {
        class ResponseData (var provisionalId:UUID, var email:String)
        //create new user
        val baseUrl = "http://localhost:$port"
        val newuser = UserDTO("Monkey D. Luffy",tempmail,"P4\$\$word.", Role.CUSTOMER)
        val request = HttpEntity(newuser)
        val response = restTemplate.postForEntity<ResponseData>("${baseUrl}/user/register", request )
        assert(response.statusCode == HttpStatus.ACCEPTED)
        //retrieve activation code (can't from mail)
        var activationData = Activation()
        try { activationData = actRepo.getById(response.body!!.provisionalId)!! }
        catch(e:Exception) { assert(false) }
        //change validation time into the past
        activationData.expirationTime=Date()
        val c = Calendar.getInstance()
        c.time=activationData.expirationTime
        c.add(Calendar.DATE, -1)
        activationData.expirationTime=c.time
        actRepo.save(activationData)
        //post request
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request2 = HttpEntity("{\n" +
                    "    \"provisional_id\": \"${activationData.id}\",\n" +
                    "    \"activation_code\": \"${activationData.activationCode}\"\n" +
                "}", headers)
        val response2 = restTemplate.postForEntity<Unit>("${baseUrl}/user/validate", request2)
        assert(response2.statusCodeValue == 404)
    }

    @Test @DirtiesContext
    fun validationOK()
    {
        class ResponseData (var provisionalId:UUID, var email:String)
        //create new user
        val baseUrl = "http://localhost:$port"
        val newuser = UserDTO("Monkey D. Luffy",tempmail,"P4\$\$word.", Role.CUSTOMER)
        val request = HttpEntity(newuser)
        val response = restTemplate.postForEntity<ResponseData>("${baseUrl}/user/register", request )
        assert(response.statusCode == HttpStatus.ACCEPTED)
        //retrieve activation code (can't from mail)
        var activationData = Activation()
        try { activationData = actRepo.getById(response.body!!.provisionalId)!! }
        catch(e:Exception) { assert(false) }
        //post request
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request2 = HttpEntity("{\n" +
                "    \"provisional_id\": \"${activationData.id}\",\n" +
                "    \"activation_code\": \"${activationData.activationCode}\"\n" +
                "}", headers)
        val response2 = restTemplate.postForEntity<Unit>("${baseUrl}/user/validate", request2)
        assert(response2.statusCodeValue == 201)
        assert(userRepo.getByEmail(tempmail).pending==false)
    }

    /******************
     * RATE LIMITER   *
     ******************/

    @Test @DirtiesContext
    fun rateLimitRegistration()
    {
        val baseUrl = "http://localhost:$port"
        val newuser = UserDTO("Monkey D. Luffy",tempmail,"P4\$\$word.", Role.CUSTOMER)
        val request = HttpEntity(newuser)
        var response : ResponseEntity<*>
        for(i in 0..15)
        {
            response = restTemplate.postForEntity<Unit>("${baseUrl}/user/register", request)
            println("REQUEST $i : ${response.statusCodeValue}")
            if ( i > 11)
                assert(response.statusCode == HttpStatus.TOO_MANY_REQUESTS)
        }
    }

    @Test @DirtiesContext
    fun rateLimitValidation()
    {
        val baseUrl = "http://localhost:$port"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity("{\n" +
                "    \"provisional_id\": \"ab31752c-b0f8-4f9e-a4ac-29f8ea92fa54\",\n" +
                "    \"activation_code\": \"randomString\"\n" +
                "}", headers)
        var response : ResponseEntity<*>
        for(i in 0..15)
        {
            response = restTemplate.postForEntity<Unit>("${baseUrl}/user/validate", request)
            println("REQUEST $i : ${response.statusCodeValue}")
            if ( i > 10)
                assert(response.statusCode == HttpStatus.TOO_MANY_REQUESTS)
        }
    }

    /******************
     * JOB            *
     ******************/

    @Test @DirtiesContext
    fun testErasure ()
    {
        val u1 = userRepo.save(User(email=tempmail, password = "P4\$\$word", username="u1", role=Role.CUSTOMER) )
        val u2 = userRepo.save(User(email=tempmail+"1", password = "P4\$\$word", username="u2", role=Role.CUSTOMER) )
        val u3 = userRepo.save(User(email=tempmail+"2", password = "P4\$\$word", username="u3", role = Role.CUSTOMER) )
        val data = Date()
        val c = Calendar.getInstance()
        c.time=data
        actRepo.save(Activation(expirationTime = c.time, user=u1))
        c.add(Calendar.YEAR, -1)
        actRepo.save(Activation(expirationTime = c.time, user=u2))
        c.add(Calendar.YEAR, 2)
        actRepo.save(Activation(expirationTime = c.time, user=u3))
        assert(actRepo.count()==3L)
        sleep(10000+2000)
        assert(actRepo.count()==1L)
    }

}

class UnitTests {

    @Test
    fun userToDTO ()
    {
        val tempmail = "vofovi8510@carsik.com"
        val user = User (username="Mario Rossi", email=tempmail, password="something very secret", role = Role.CUSTOMER)
        val dto = user.toDTO()
        assert(dto.username=="Mario Rossi")
        assert(dto.email==tempmail)
        assert(dto.password=="something very secret")
    }

    @Test
    fun activationToDTO ()
    {
        val user = User (username="Mario Rossi", email = "", password = "", role = Role.CUSTOMER)
        val act = Activation()
        act.user=user
        assert(act.user!!.username=="Mario Rossi")
        assert(act.attempts==5)
        assert(act.activationCode!!.matches("[A-Z0-9]{6}".toRegex()))
        assert(act.expirationTime!! > Date())
    }
}
