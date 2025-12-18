package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListShiftRequest;

public interface ShiftListShiftRequestRepository extends JpaRepository<ShiftListShiftRequest, Long>
{
    ShiftListShiftRequest findByShiftId(Shift id);
    List<ShiftListShiftRequest> findByShiftIdIn(List<Shift> ids);
}
