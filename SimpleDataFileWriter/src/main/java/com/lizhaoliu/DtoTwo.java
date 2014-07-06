package com.lizhaoliu;

import com.lizhaoliu.annotation.DataField;

public final class DtoTwo {

  @DataField(name = "DtoTwo21", position = 1)
  private final String val21;

  @DataField(name = "DtoTwo22", position = 3)
  private final String val22;

  public DtoTwo(String val21, String val22) {
    super();
    this.val21 = val21;
    this.val22 = val22;
  }
}
