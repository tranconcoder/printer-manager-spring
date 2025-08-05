package com.tvconss.printermanagerspring.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "location")
@DynamicInsert
@DynamicUpdate
public class LocationEntity {

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
    private LocationEntity locationFirstParent;

//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "location_first_parent")
//    private List<LocationDTO> locationParents;

}
