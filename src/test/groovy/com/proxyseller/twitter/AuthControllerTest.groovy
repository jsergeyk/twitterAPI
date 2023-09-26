package com.proxyseller.twitter

import com.fasterxml.jackson.databind.ObjectMapper
import com.proxyseller.twitter.dto.TokenDTO
import com.proxyseller.twitter.dto.UserDTO
import com.proxyseller.twitter.repositories.RefreshTokenRepository
import com.proxyseller.twitter.service.PostService
import com.proxyseller.twitter.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest extends BasicItSpec {

    @Autowired
    private MockMvc mockMvc
    @Autowired
    private ObjectMapper objectMapper
    @Autowired
    private UserService userService
    @Autowired
    private PostService postService
    @Autowired
    private RefreshTokenRepository tokenRepository

    def "post /api/auth/signup"() {
        given:
            def userId = null
            def userName = "testUser"
            def userPassword = "123456"
            def userDTO = new UserDTO(userName, "testUser1@gmail.com", userPassword, null)
        when:
            userService.deleteByUsername(userName)
            def resultAction = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .content(objectMapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            def response = resultAction.andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn().response.contentAsString
            def responseDTO = objectMapper.readValue(response, TokenDTO)
            userId = responseDTO.userId()
        then:
            with(responseDTO) {
                userId != null
                responseDTO.accessToken() != null
                responseDTO.refreshToken() != null
            }
            userService.existsById(userId)
        cleanup:
            def user = userService.findById(userId)
            postService.deleteByUser(user)
            tokenRepository.deleteByUser(user)
            userService.delete(user)
    }
}