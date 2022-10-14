package com.atmmachine.entity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NotesTest {

    private Notes notes;

    @Before
    public void setupNotes() {
        notes = new Notes();
        notes.setNoteCategory(50);
        notes.setQuantity(10);
    }

    @Test
    public void testGettersSetters(){
        Assert.assertEquals(50,notes.getNoteCategory());
        Assert.assertEquals(10,notes.getQuantity());

    }
}
