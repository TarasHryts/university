package com.task.university.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "degree")
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "degree_id")
    private Long degreeId;
    @Column(name = "degree_name")
    @Enumerated(EnumType.STRING)
    private DegreeName degreeName;

    public Long getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Long degreeId) {
        this.degreeId = degreeId;
    }

    public DegreeName getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(DegreeName degreeName) {
        this.degreeName = degreeName;
    }

    @Override
    public String toString() {
        return "Degree{" +
                "degreeId=" + degreeId +
                ", degreeName=" + degreeName +
                '}';
    }

    public enum DegreeName {
        ASSISTANT("ASSISTANT"),
        ASSOCIATE_PROFESSOR("ASSOCIATE PROFESSOR"),
        PROFESSOR("PROFESSOR");
        private final String name;

        DegreeName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
