package com.tvconss.printermanagerspring.dto;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Table(name = "location")
@DynamicInsert
@DynamicUpdate
public class LocationDTO {

    @Id
    @Column(name = "location_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @Column(name = "location_name", nullable = false)
    private String locationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type", nullable = false)
    private LocationType locationType;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_first_parent")
    private LocationDTO locationFirstParent;

//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "location_first_parent")
//    private List<LocationDTO> locationParents;

}
