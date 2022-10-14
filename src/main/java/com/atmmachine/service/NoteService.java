package com.atmmachine.service;

import com.atmmachine.entity.Notes;
import com.atmmachine.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public List<Notes> getAvailableNoteLimitsInATM(){
        return noteRepository.findAll();
    }

    public List<Notes>createEmptyNotesList(){
        List<Notes>notesList = new ArrayList<>();
        notesList.add(new Notes(50,0));
        notesList.add(new Notes(20,0));
        notesList.add(new Notes(10,0));
        notesList.add(new Notes(5,0));

        return notesList;
    }
}
