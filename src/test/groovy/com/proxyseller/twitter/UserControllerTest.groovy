package com.proxyseller.twitter

import com.fasterxml.jackson.databind.ObjectMapper
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.UserDTO
import com.proxyseller.twitter.dto.TokenDTO
import com.proxyseller.twitter.service.UserService
import com.proxyseller.twitter.springdata.IUser
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

    def "step 1 post /api/auth/signup"() {
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
    def "step 2 edit user /api/users/edit/{userid}"() {
        when:
            def userDTO = new UserDTO(newUserName, newEmail, newPassword, isActive)
            def resultAction = mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/edit/" + userId)
                    .content(objectMapper.writeValueAsString(userDTO))
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            def response = resultAction.andExpect(MockMvcResultMatchers.status().is(200))
                    .andReturn().response.contentAsString
            def responseUser = objectMapper.readValue(response, User)
        then:
            with(responseUser) {
                responseUser.username == newUserName
                responseUser.email == newEmail
                responseUser.isActive == isActive
            }
        where:
            newUserName         || newEmail             || newPassword   || isActive
            "newUserName"       || "newEmail@gmail.com" || "newPassword" || true
            "secondUserName"    || "second@gmail.com"   || null          || false
    }

    def "step 3 delete user /api/users/delete"() {
        given:
        when:
            def resultAction = mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/delete")
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