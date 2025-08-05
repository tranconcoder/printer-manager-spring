package com.tvconss.printermanagerspring.enums;

import lombok.Getter;

@Getter
public enum MediaCategory {
    MEDIA_AVATAR("avatars");

    private final String mediaCategory;

    MediaCategory(String mediaCategory) {
        this.mediaCategory = mediaCategory;
    }
}
