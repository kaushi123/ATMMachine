package com.atmmachine.repository;

import com.atmmachine.entity.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository  extends JpaRepository<Notes,String> {
}
