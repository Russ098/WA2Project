package it.polito.wa2.group18.wa2lab3.services.userService

import it.polito.wa2.group18.wa2lab3.dtos.LoginDataDTO
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt


interface LoginLayer {
    fun login (username:String, password:String ) : String?
}