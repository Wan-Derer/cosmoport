package com.space.model;

import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ship")
public class Ship {
    private static final int CURRENT_YEAR = 3019;
    private static final int YEAR_SHIFT = 1900;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // ID корабля

    //    @Column(name = "name")
    private String name;            // Название корабля (до 50 знаков включительно)
    private String planet;          // Планета пребывания (до 50 знаков включительно)

    @Enumerated(EnumType.STRING)
    //@Column(name = "shipType")
    private ShipType shipType;      // Тип корабля
    private Date prodDate;          // Дата выпуска. Диапазон значений года 2800..3019 включительно
    private Boolean isUsed;         // Использованный / новый
    private Double speed;           // Максимальная скорость корабля.
    // Диапазон значений 0,01..0,99 включительно.
    // Используй математическое округление до сотых.
    private Integer crewSize;       // Количество членов экипажа. Диапазон значений 1..9999 включительно

    // calculated fields
    @Access(AccessType.PROPERTY)
    private Double rating;      // Рейтинг корабля. Используй математическое округление до сотых


    public boolean isAllFieldsCorrect() {

        return (getName() != null &&
                getPlanet() != null &&
                getShipType() != null &&
                getProdDate() != null &&
//                getUsed() != null &&
                getSpeed() != null &&
                getCrewSize() != null
        );
    }

    public void update(Ship shipParams) {
        if (shipParams == null) throw new IllegalArgumentException();

        if (shipParams.getName() != null) setName(shipParams.getName());
        if (shipParams.getPlanet() != null) setPlanet(shipParams.getPlanet());
        if (shipParams.getShipType() != null) setShipType(shipParams.getShipType());
        if (shipParams.getProdDate() != null) setProdDate(shipParams.getProdDate());
        if (shipParams.getUsed() != null) setUsed(shipParams.getUsed());
        if (shipParams.getSpeed() != null) setSpeed(shipParams.getSpeed());
        if (shipParams.getCrewSize() != null) setCrewSize(shipParams.getCrewSize());

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @JsonSetter
    public void setName(String name) {
        if (name == null || name.isEmpty() || name.length() > 50) throw new IllegalArgumentException("Incorrect Name");
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    @JsonSetter
    public void setPlanet(String planet) {
        if (planet == null || planet.isEmpty() || planet.length() > 50)
            throw new IllegalArgumentException("Incorrect Planet");
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    @JsonSetter
    public void setShipType(ShipType shipType) {
        if (shipType == null) throw new IllegalArgumentException("Incorrect Ship type");
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    @JsonSetter
    public void setProdDate(Date prodDate) {
        if (prodDate == null) throw new IllegalArgumentException("Incorrect Prod Date");

        int year = prodDate.getYear() + YEAR_SHIFT;
        if (year < 2800 || year > CURRENT_YEAR) throw new IllegalArgumentException("Incorrect Prod Date");

        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    @JsonSetter
    public void setUsed(Boolean used) {

        isUsed = used != null && used;
//        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    @JsonSetter
    public void setSpeed(Double speed) {
        if (speed == null || speed < 0.01 || speed > 0.99) throw new IllegalArgumentException("Incorrect Speed");
        this.speed = Math.round(speed * 100) / 100.0;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    @JsonSetter
    public void setCrewSize(Integer crewSize) {
        if (crewSize == null || crewSize < 1 || crewSize > 9999) throw new IllegalArgumentException("Incorrect Crew size");
        this.crewSize = crewSize;
    }

    public Double getRating() {
        Double k = getUsed() ? 0.5 : 1.0;
        rating = (80 * getSpeed() * k) / (CURRENT_YEAR - prodDate.getYear() + YEAR_SHIFT + 1.0);
        rating = Math.round(rating * 100) / 100.0;
        return rating * 100;
    }

    @JsonSetter
    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", planet='" + planet + '\'' +
                ", shipType=" + shipType +
                ", prodDate=" + prodDate +
                ", isUsed=" + isUsed +
                ", speed=" + speed +
                ", crewSize=" + crewSize +
                ", rating=" + rating +
                '}';
    }
}
