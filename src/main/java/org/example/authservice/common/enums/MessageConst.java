package org.example.authservice.common.enums;

import lombok.Getter;

@Getter
public enum MessageConst {
  // Auth - error messages
  AUTH_ERROR("E-AUTH-001"),

  // System - error messages
  SYSTEM_INTERNAL_ERROR("E-SYS-001");

  private final String code;

  MessageConst(String code) {
    this.code = code;
  }
}
