package com.proxyseller.twitter.dto;

import com.proxyseller.twitter.document.Comment;
import com.proxyseller.twitter.document.Like;

import java.util.Date;
import java.util.List;

public record PostDTO(String id, String userId, String message, Date createDate, List<Comment> comments, List<Like> likes) {
}