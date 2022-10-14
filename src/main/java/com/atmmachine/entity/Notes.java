package com.atmmachine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Notes {

    @Id
    @Column(name="NOTE_CATEGORY",nullable = false)
    private int noteCategory;

    @Column(name="QUANTITY",nullable = false)
    private int quantity;

    public Notes() {
    }

    public Notes(int noteCategory, int quantity) {
        this.noteCategory = noteCategory;
        this.quantity = quantity;
    }

    public int getNoteCategory() {
        return noteCategory;
    }

    public void setNoteCategory(int noteCategory) {
        this.noteCategory = noteCategory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
