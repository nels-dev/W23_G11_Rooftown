package csis3175.w23.g11.rooftown.messages.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import csis3175.w23.g11.rooftown.posts.data.model.Post;

public class ChatAndRelatedPost {
    @Embedded
    Chat chat;
    @Relation(parentColumn = "related_post", entityColumn = "postId")
    Post relatedPost;

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Post getRelatedPost() {
        return relatedPost;
    }

    public void setRelatedPost(Post relatedPost) {
        this.relatedPost = relatedPost;
    }
}
