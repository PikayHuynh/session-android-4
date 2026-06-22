package com.zzz.ss4.model;

public class Team {
  private Integer id;
  private String name;

  private String stadium;
  private byte[] logo;
  private String capacity;

  public Team() {}

  public Team(Integer id, String name, byte[] logo, String capacity, String stadium) {
    this.id = id;
    this.name = name;
    this.logo = logo;
    this.capacity = capacity;
    this.stadium = stadium;
  }

  public String getCapacity() {
    return capacity;
  }

  public void setCapacity(String capacity) {
    this.capacity = capacity;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public byte[] getLogo() {
    return logo;
  }

  public void setLogo(byte[] logo) {
    this.logo = logo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStadium() {
    return stadium;
  }

  public void setStadium(String stadium) {
    this.stadium = stadium;
  }
}
