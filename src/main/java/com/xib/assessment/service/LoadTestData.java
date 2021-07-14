/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xib.assessment.service;

import com.xib.assessment.entity.Agent;
import com.xib.assessment.entity.Manager;
import com.xib.assessment.entity.Team;
import com.xib.assessment.repository.AgentRepository;
import com.xib.assessment.repository.ManagerRepository;
import com.xib.assessment.repository.TeamRepository;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Vee
 */
@Component
public class LoadTestData {
    
    @Autowired
    AgentRepository agentRepository;

    @Autowired
    TeamRepository teamRepository;
    
    @Autowired
    ManagerRepository managerRepository;

    @PostConstruct
    @Transactional
    public void execute() {
        Manager m1 = createManager("Lisa", "Shaw", "5555555555555");
        Manager m2 = createManager("Kate", "Pink", "22222222222222");
        
        Team team1 = createTeam("Marvel", m1);
        Team team2 = createTeam("DC", m2);

        createAgent("Bruce", "Banner", "1011125190081", team1);
        createAgent("Tony", "Stark", "6912115191083", team1);
        createAgent("Peter", "Parker", "7801115190084", team1);
        createAgent("Bruce", "Wayne", "6511185190085", team2);
        createAgent("Clark", "Kent", "5905115190086", team2);
    }
    
    private Team createTeam(String name, Manager manager) {
        Team t = new Team();
        t.setName(name);
        t.setManager(manager);
        
        return teamRepository.save(t);
    }

    private Agent createAgent(String firstName, String lastName, String idNumber, Team team) {
        Agent a = new Agent();
        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setIdNumber(idNumber);
        a.setTeam(team);
        
        return agentRepository.save(a);
    }
    
    private Manager createManager(String firstName, String lastName, String idNumber){
        Manager a = new Manager();
        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setIdNumber(idNumber);
        
        return managerRepository.save(a);
    }
}
