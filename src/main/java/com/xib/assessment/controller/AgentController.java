/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xib.assessment.controller;

import com.xib.assessment.entity.Agent;
import com.xib.assessment.entity.Manager;
import com.xib.assessment.entity.Team;
import com.xib.assessment.repository.AgentRepository;
import com.xib.assessment.repository.ManagerRepository;
import com.xib.assessment.repository.TeamRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Vee
 */
@RestController
public class AgentController {
    
    @Autowired
    private AgentRepository agentRepository;
    
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private ManagerRepository managerRepository;
    
    @GetMapping("agent/{id}")
    public Agent findAgent(@PathVariable("id") Long id) {
        Agent a = new Agent();
        a.setId(id);
        return a;
    }
    
    /*
     * **GET /teams/** - Returns a list of teams in the database in JSON format
     */
    @GetMapping("teams/**")
    public ResponseEntity<List<Team>> allTeams(){
        
        List<Team> teams = teamRepository.findAll();    //retrieve all teams from db
        
        if(teams.size() >= 1)
            return new ResponseEntity<>(teams, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        
    }
    
    /*
     * **GET /team/{id}/** - Returns a detail view of the specified team in JSON format
     */
    @GetMapping("team/{id}/**")
    public ResponseEntity<Team> getTeamById(@PathVariable("id") Long id){
        Team team = teamRepository.findById(id).get(); 
        
        if(team != null)
            return new ResponseEntity<>(team, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
    
    /* **GET /agents/** - Returns a list of all agents in the database in JSON format */
    /*@GetMapping("agents/**")
    public ResponseEntity<List<Agent>> allAgents(){
        List<Agent> agents = agentRepository.findAll();
        
        if(agents.size() >= 1)
            return new ResponseEntity<>(agents, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }*/
    
    /*
     * **GET /agents/** - Implement pagination and query parameters
     *
     * @param page - zero-based page index, must be positive
     * @param size - number of items in a page to be returned, must be greater than 0.
     */
    @GetMapping("agents/**")
    public ResponseEntity<List<Agent>> allAgents(@RequestParam("page") int page,
                @RequestParam("size") int size){
        
        List<Agent> agents = null;
        List<Agent> list = new ArrayList<>();
        try{
            Pageable pagination = PageRequest.of(page, size);
            
            agents = agentRepository.findAll(pagination).getContent();
            if(agents.size() >= 1){
                
                for(Agent a: agents){
                    a.setIdNumber(null);
                    
                    list.add(a);
                }
                
                return new ResponseEntity<>(list, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
     /*
     * **GET /agent/{id}/** - Returns a detail view of the specified agent in JSON format.
     *  Includes team details.
     */
    @GetMapping("agent/{id}/**")
    public ResponseEntity<Agent> getAgentById(@PathVariable("id") Long id){
        
        Agent agent = agentRepository.findById(id).get();     //retrieve agent from db
        
        if(agent != null)
            return new ResponseEntity<>(agent, HttpStatus.OK);  //return agent detail in JSON format
        else
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
    
    /*
     * **POST /team/** - Creates a new team with the specified details
     */
    @PostMapping("team/**")
    public ResponseEntity<String> addTeam(@RequestBody Team team){
        
        try{
            teamRepository.save(team);
            return new ResponseEntity<>("NEW TEAM "+ team.getName() +" CREATED", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("TEAM NOT CREATED", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /*
     * **POST /agent/** - Creates a new agent with the specified details
     */
    @PostMapping("agent/**")
    public ResponseEntity<String> addAgent(@RequestBody Agent agent){
        try{
            agentRepository.save(agent);
            return new ResponseEntity<>("NEW AGENT "+agent.getFirstName()+" CREATED", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("AGENT NOT CREATED", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /*
     * **POST /manager/** - Creates a new manager with the specified details
     */
    @PostMapping("manager/**")
    public ResponseEntity<String> addManager(@RequestBody Manager manager){
        try{
            managerRepository.save(manager);
            return new ResponseEntity<>("NEW MANAGER "+manager.getFirstName()+" CREATED", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("MANAGER NOT CREATED", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /*
     * **PUT /team/{{id}}/agent** - Assigns an agent to the specified team
     */
    @PutMapping("team/{id}/agent**")
    public ResponseEntity<String> editAgent(@RequestBody Agent agent, @PathVariable("id") Long id){
        try{
            Agent a = agentRepository.findById(id).get();
            if(a != null){
                a.setTeam(agent.getTeam());
                
                agentRepository.save(a);
                return new ResponseEntity<>("AGENT "+a.getFirstName()+" HAS BEEN ASSIGNED TO A NEW TEAM", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("AGENT NOT FOUND", HttpStatus.NOT_FOUND);
            }
            
        }catch(Exception e){
            return new ResponseEntity<>("AGENT NOT ASSIGNED", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /*
     * returns a list of all empty teams
     */
    @GetMapping("teams/empty/**")
    public ResponseEntity<List<Team>> allEmptyTeams(){
        
        List<Team> teams = teamRepository.findAll();    //retrieve all teams from db
        ResponseEntity<List<Team>> res = null;
        
        for(int i = 0; i < teams.size(); i++){
            List<Agent> emptyTeams = teams.get(i).getAgents();
            if(emptyTeams.isEmpty()){
                res = new ResponseEntity<>(teams, HttpStatus.OK);
            }else{
                res = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }
        return res;
    }
}
