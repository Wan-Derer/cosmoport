package com.space.service;  // in this package: some logic and repository wrapper

import com.space.controller.ShipDisplayOptions;
import com.space.controller.ShipFilter;
import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.repository.ShipRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingService {

    private final ShipRepo shipRepo;

    @Autowired
    public ShoppingService(ShipRepo shipRepo) {
        this.shipRepo = shipRepo;
    }

    public List<Ship> getShips(ShipFilter filter, ShipDisplayOptions displayOptions) {
        if (filter == null || displayOptions == null) throw new IllegalArgumentException();

        Sort sort;
        if (displayOptions.getOrder() != null) {
            sort = Sort.by(Sort.Direction.ASC, displayOptions.getOrder().getFieldName());
//            System.out.println("Sort: " + sort.toString());
        } else {
            sort = Sort.by(Sort.Direction.ASC, ShipOrder.ID.getFieldName());    // sort by default
//            System.out.println("Sort by default: " + sort.toString());
        }

        Integer pageNum = displayOptions.getPageNumber();
        if (pageNum == null) pageNum = 0;
        Integer pageSize = displayOptions.getPageSize();
        if (pageSize == null) pageSize = 3;
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        return shipRepo.findAll(filterSpec(filter), pageable).getContent();
    }

    public long getShipsCount(ShipFilter filter) {
        if (filter == null) throw new IllegalArgumentException();
        return shipRepo.count(filterSpec(filter));
    }

    public Ship getShipById(long id) {
        Optional<Ship> ship = shipRepo.findById(id);
        return ship.orElse(null);
    }

    public Ship updateShip(Ship ship, Ship shipParams) {
        if (ship == null || shipParams == null) throw new IllegalArgumentException();

        ship.update(shipParams);

        return shipRepo.save(ship);
    }

    public void deleteShip(Ship ship) {
        if (ship == null) throw new IllegalArgumentException();

        shipRepo.delete(ship);
    }


// =======================================================================================================

    private Specification<Ship> filterSpec(ShipFilter filter) {
        if (filter == null) throw new IllegalArgumentException();

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"));
            }

            if (filter.getPlanet() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("planet")),
                        "%" + filter.getPlanet().toLowerCase() + "%"));
            }

            if (filter.getShipType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("shipType"), filter.getShipType()));
            }

            if (filter.getAfter() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("prodDate"), new Date(filter.getAfter())));
            }

            if (filter.getBefore() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("prodDate"), new Date(filter.getBefore())));
            }

            if (filter.getUsed() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isUsed"), filter.getUsed()));
            }

            if (filter.getMinSpeed() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), filter.getMinSpeed()));
            }

            if (filter.getMaxSpeed() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("speed"), filter.getMaxSpeed()));
            }

            if (filter.getMinCrewSize() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), filter.getMinCrewSize()));
            }

            if (filter.getMaxCrewSize() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), filter.getMaxCrewSize()));
            }

            if (filter.getMinRating() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), filter.getMinRating()));
            }

            if (filter.getMaxRating() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("rating"), filter.getMaxRating()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}












