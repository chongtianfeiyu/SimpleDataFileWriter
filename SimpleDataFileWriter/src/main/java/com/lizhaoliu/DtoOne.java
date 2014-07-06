package com.lizhaoliu;

import com.lizhaoliu.annotation.DataField;

public final class DtoOne {

  @DataField(name = "DtoOne11", position = 0)
  private final String val11;
  
  @DataField(name = "DtoOne12", position = 2)
  private final String val12;

  public DtoOne(String val11, String val12) {
    super();
    this.val11 = val11;
    this.val12 = val12;
  }
}
