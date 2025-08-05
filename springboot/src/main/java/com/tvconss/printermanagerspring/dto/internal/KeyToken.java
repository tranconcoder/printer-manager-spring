package com.tvconss.printermanagerspring.dto.internal;

import lombok.Data;

@Data
public class KeyToken {

    private Long userId;

    private String publicKey;

    private String jit;

}
