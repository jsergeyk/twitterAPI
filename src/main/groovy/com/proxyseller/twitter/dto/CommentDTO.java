package com.proxyseller.twitter.dto;

import java.util.Date;

public record CommentDTO(String id, String postId, String userId, String message, Date createDate) {
}