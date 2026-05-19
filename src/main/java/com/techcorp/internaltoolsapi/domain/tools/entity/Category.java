package com.techcorp.internaltoolsapi.domain.tools.entity;

import jakarta.persistence.*;

/**
 * Category entity representing tool categories.
 */
@Entity
@Table(name = "categories")
public class Category {

    // ---------- ATTRIBUTES ---------- //

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    // ---------- CONSTRUCTORS ---------- //

    public Category() {
    }

    public Category(Integer id,
                    String name) {

        this.id = id;
        this.name = name;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(
            Integer id
    ) {
        this.id = id;
    }

    public void setName(
            String name
    ) {
        this.name = name;
    }

    // ---------- OBJECT METHODS ---------- //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Category{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}