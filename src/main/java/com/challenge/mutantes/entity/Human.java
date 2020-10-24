package com.challenge.mutantes.entity;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;

@Generated
@Entity
@Table(name = "human", schema = "mutantsdb")
@Getter
@Setter
@TypeDefs({
        @TypeDef(
                name = "string-array",
                typeClass = StringArrayType.class
        )
})
public class Human implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Type( type = "string-array" )
    @Column(name = "dna", columnDefinition = "text[]")
    private String[] dna;

    @Column(name = "mutant")
    private Boolean isMutant;
}
