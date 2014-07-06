package com.lizhaoliu;

public final class DtoTotal {

  private final DtoOne dtoOne;

  public DtoOne getDtoOne() {
    return dtoOne;
  }

  public DtoTwo getDtoTwo() {
    return dtoTwo;
  }

  private final DtoTwo dtoTwo;

  public DtoTotal(DtoOne dtoOne, DtoTwo dtoTwo) {
    super();
    this.dtoOne = dtoOne;
    this.dtoTwo = dtoTwo;
  }
}
