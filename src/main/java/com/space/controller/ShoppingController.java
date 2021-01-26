package com.space.controller;

import com.space.model.Ship;
import com.space.repository.ShipRepo;
import com.space.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rest/ships")
public class ShoppingController {

    private ShipRepo shipRepo;
    private ShoppingService shoppingService;

    @Autowired
    public ShoppingController(ShipRepo shipRepo, ShoppingService shoppingService) {
        this.shipRepo = shipRepo;
        this.shoppingService = shoppingService;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Ship>> GetShipsList(ShipFilter filter, ShipDisplayOptions displayOptions) {
//        System.out.println("GET /rest/ships");
        List<Ship> ships;
        try {
            ships = shoppingService.getShips(filter, displayOptions);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ships, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getShipsCount(ShipFilter filter) {
        long shipsCount;
        try {
            shipsCount = shoppingService.getShipsCount(filter);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(shipsCount, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {
//        System.out.println(ship);
        HttpHeaders headers = new HttpHeaders();

        if (ship == null || !ship.isAllFieldsCorrect()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (ship.getUsed() == null) ship.setUsed(false);
//ship = shipRepo.save(ship);
//System.out.println(ship);
        return new ResponseEntity<>(shipRepo.save(ship), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Ship> getShip(@PathVariable long id) {
        if (id < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship ship = shoppingService.getShipById(id);
        if (ship == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @PostMapping("{id}")
    public ResponseEntity<Ship> updateShip(@PathVariable long id, @RequestBody Ship shipParams) {
        ResponseEntity<Ship> shipEntity = getShip(id);

        if (shipEntity.getStatusCode() != HttpStatus.OK) return new ResponseEntity<>(shipEntity.getStatusCode());

        Ship ship;
        try{
            ship = shoppingService.updateShip(shipEntity.getBody(), shipParams);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Ship> deleteShip(@PathVariable long id) {
        ResponseEntity<Ship> shipEntity = getShip(id);

        if (shipEntity.getStatusCode() != HttpStatus.OK) return new ResponseEntity<>(shipEntity.getStatusCode());

        try {
            shoppingService.deleteShip(shipEntity.getBody());
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }



    // ==============





}






