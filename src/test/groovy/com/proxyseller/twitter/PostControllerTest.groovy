package com.proxyseller.twitter

import com.fasterxml.jackson.databind.ObjectMapper
import com.proxyseller.twitter.dto.PostDTO
import com.proxyseller.twitter.dto.TokenDTO
import com.proxyseller.twitter.dto.UserDTO
import com.proxyseller.twitter.service.PostService
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
class PostControllerTest extends BasicItSpec {

    @Autowired
    MockMvc mockMvc
    @Autowired
    ObjectMapper objectMapper
    @Autowired
    UserService userService
    @Autowired
    PostService postService

    static def userId = null
    static def postId = null
    static def accessToken = null

    def "step 1 method POST /api/auth/signup"() {
        given:
            def userName = "testPostUser"
            def email = "testPos@gmail.com"
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
    def "step 2 method POST /api/posts"() {
        when:
            def postDTO = new PostDTO(null, userId, message, createDate, null, null)
            def resultAction = mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                    .content(objectMapper.writeValueAsString(postDTO))
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            def response = resultAction.andExpect(MockMvcResultMatchers.status().is(200))
                    .andReturn().response.contentAsString
            def responsePost = objectMapper.readValue(response, PostDTO)
            postId = responsePost.id()
        then:
            with(responsePost) {
                responsePost.message() == message
            }
        where:
            message             || createDate
            "twit1. Just Try"   || null
            "some twit2."       || null
    }

    @Unroll
    def "step 3 method PUT /api/posts/{id}"() {
        when:
            def postDTO = new PostDTO(postId, userId, message, createDate, null, null)
            def resultAction = mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/" + postId)
                    .content(objectMapper.writeValueAsString(postDTO))
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            def response = resultAction.andExpect(MockMvcResultMatchers.status().is(200))
                    .andReturn().response.contentAsString
            def responsePost = objectMapper.readValue(response, PostDTO)
        then:
            with(responsePost) {
                responsePost.message() == message
            }
        where:
            message                   || createDate
            "edit Twit1. Just Try"    || null
            "edit2 some twit2."       || null
    }

    def "step 4 method DELETE post /api/posts/{id}"() {
        given:
        when:
            def resultAction = mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/" + postId)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            def response = resultAction.andExpect(MockMvcResultMatchers.status().is(200)).andReturn().response.contentAsString
        then:
            !postService.existsById(postId)
    }

    def "step 5 method DELETE user /api/users/{id}"() {
        given:
        when:
            def resultAction = mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + userId)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            def response = resultAction.andExpect(MockMvcResultMatchers.status().is(200)).andReturn().response.contentAsString
            def responseDTO = objectMapper.readValue(response, Map<String, String>)
        then:
            responseDTO.get("description") == "User successfully deleted"
            !userService.existsById(userId)
    }
}