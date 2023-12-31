package com.proxyseller.twitter

import com.fasterxml.jackson.databind.ObjectMapper
import com.proxyseller.twitter.dto.TokenDTO
import com.proxyseller.twitter.dto.UserDTO
import com.proxyseller.twitter.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Stepwise
import spock.lang.Unroll

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Stepwise
class UserControllerTest extends BasicItSpec {

    @Autowired
    MockMvc mockMvc
    @Autowired
    ObjectMapper objectMapper
    @Autowired
    UserService userService

    static def userId = null
    static def accessToken = null

    def "step 1 method POST /api/auth/signup"() {
        given:
            def userName = "testUserAndDelete"
            def email = "test@gmail.com"
            def userPassword = "123456"
            def userDTO = new UserDTO(userName, email, userPassword, null)
        when:
            userService.deleteByUsername(userName)
            userService.deleteByUsername("newUserName")
            def resultAction = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .content(objectMapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            def response = resultAction.andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn().response.contentAsString
            def responseDTO = objectMapper.readValue(response, TokenDTO)
            userId = responseDTO.userId()
            accessToken = responseDTO.accessToken()
        then:
            with(responseDTO) {
                userId != null
                responseDTO.accessToken() != null
                responseDTO.refreshToken() != null
            }
        userService.existsById(userId)
    }

    @Unroll
    def "step 2 method PATCH user /api/users/{id}"() {
        when:
            def userDTO = new UserDTO(newUserName, newEmail, newPassword, isActive)
            def resultAction = mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/" + userId)
                    .content(objectMapper.writeValueAsString(userDTO))
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            def response = resultAction.andExpect(MockMvcResultMatchers.status().is(200))
                    .andReturn().response.contentAsString
            def responseUser = objectMapper.readValue(response, UserDTO)
        then:
            with(responseUser) {
                responseUser.username() == newUserName
                responseUser.email() == newEmail
                responseUser.isActive() == isActive
            }
        where:
            newUserName         || newEmail             || newPassword   || isActive
            "newUserName"       || "newEmail@gmail.com" || null          || true
            "secondUserName"    || "second@gmail.com"   || null          || true
    }

    @Unroll
    def "step 3 method PUT user /api/users/{id}"() {
        when:
            def userDTO = new UserDTO(newUserName, newEmail, null, isActive)
            def resultAction = mockMvc.perform(MockMvcRequestBuilders.put("/api/users/" + userId)
                    .content(objectMapper.writeValueAsString(userDTO))
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            def response = resultAction.andExpect(MockMvcResultMatchers.status().is(200))
                    .andReturn().response.contentAsString
            def responseUser = objectMapper.readValue(response, UserDTO)
        then:
            with(responseUser) {
                responseUser.username() == newUserName
                responseUser.email() == newEmail
                responseUser.isActive() == isActive
            }
        where:
            newUserName         || newEmail                 || isActive
            "otherUserName"     || "otherEmail@gmail.com"   || true
            "other2UserName"    || "other2Email@gmail.com"  || false
    }

    def "step 4 method DELETE user /api/users/{id}"() {
        given:
        when:
            def resultAction = mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + userId)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            def response = resultAction.andExpect(MockMvcResultMatchers.status().is(200))
                    .andReturn().response.contentAsString
            def responseDTO = objectMapper.readValue(response, Map<String, String>)
        then:
            responseDTO.get("description") == "User successfully deleted"
            !userService.existsById(userId)
    }
}