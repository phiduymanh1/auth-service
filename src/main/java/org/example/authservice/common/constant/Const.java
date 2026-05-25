package org.example.authservice.common.constant;

public class Const {

  /** Regex patterns */
  public static final String REGEX_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

  public static final String PHONE_REGEX = "^0[35789]\\d{8}$";

  /** Text constants */
  public static final String APPLICATION_ID = "applicationId";

  public static final String TEXT_ACCESS_TOKEN = "accessToken";

  public static final String TEXT_REFRESH_TOKEN = "refreshToken";

  public static final String AUTH_HEADER_CLIENT_ID = "X-Client-Id";
}
