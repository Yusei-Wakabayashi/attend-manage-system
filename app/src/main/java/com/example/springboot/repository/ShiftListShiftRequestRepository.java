package com.example.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot.model.Shift;
import com.example.springboot.model.ShiftListShiftRequest;
import com.example.springboot.model.ShiftRequest;

public interface ShiftListShiftRequestRepository extends JpaRepository<ShiftListShiftRequest, Long>
{
    ShiftListShiftRequest findByShiftId(Shift id);
    ShiftListShiftRequest findByShiftRequestId(ShiftRequest shiftRequestId);
    List<ShiftListShiftRequest> findByShiftIdIn(List<Shift> ids);
}
