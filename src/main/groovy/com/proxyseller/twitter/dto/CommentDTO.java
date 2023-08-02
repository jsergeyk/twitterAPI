package com.proxyseller.twitter.dto;

import java.util.Date;

public record CommentDTO(String id, String postId, String message, Date createDate) {
}