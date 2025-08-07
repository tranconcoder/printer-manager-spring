package com.tvconss.printermanagerspring.enums;

import lombok.Getter;

@Getter
public enum MediaSize {
//    Avatar
    AVATAR_SMALL(100, 100),
    AVATAR_MEDIUM(200, 200),
    AVATAR_LARGE(400, 400);

    private final int width;
    private final int height;

    MediaSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
