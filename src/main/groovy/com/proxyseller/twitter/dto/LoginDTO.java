package com.proxyseller.twitter.dto;

import javax.validation.constraints.NotBlank;

public record LoginDTO (@NotBlank String username, @NotBlank String password) {

}