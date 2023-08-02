package com.proxyseller.twitter

import com.fasterxml.jackson.databind.ObjectMapper
import com.proxyseller.twitter.dto.SignupDTO
import com.proxyseller.twitter.dto.TokenDTO
import com.proxyseller.twitter.repository.PostRepository
import com.proxyseller.twitter.repository.RefreshTokenRepository
import com.proxyseller.twitter.repository.UserRepository
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
    MockMvc mockMvc
    @Autowired
    ObjectMapper objectMapper
    @Autowired
    UserRepository userRepository
    @Autowired
    PostRepository postRepository
    @Autowired
    RefreshTokenRepository tokenRepository

    def "post /api/auth/signup"() {
        given:
            def userId = null
            def userName = "testUser"
            def userPassword = "123456"
            def signupDTO = new SignupDTO(userName, "testUser1@gmail.com", userPassword, null)
        when:
        userRepository.deleteByUsername(userName)
            def resultAction = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .content(objectMapper.writeValueAsString(signupDTO))
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
            userRepository.existsById(userId)
        cleanup:
            def user = userRepository.findById(userId)
            postRepository.deleteByUser(user.get());
            tokenRepository.deleteByUser(user.get());
            userRepository.delete(user.get())
    }
}