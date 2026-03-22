package com.patrykb.PatFin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Category(){
    }

    @Override
    public Category clone() {
        try {
            Category cloned = (Category) super.clone();
            cloned.id = null;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Klonowanie Category nie powiodło się", e);
        }
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

    public void setName(String name) {
        this.name = name;
    }
}
