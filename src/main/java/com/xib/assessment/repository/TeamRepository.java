/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xib.assessment.repository;

import com.xib.assessment.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Vee
 */
public interface TeamRepository extends JpaRepository<Team, Long> {
    
}
