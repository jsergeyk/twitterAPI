package com.proxyseller.twitter.dto;

import java.util.Date;

public record LikeDTO(String id, String postId, Date createDate) {
}