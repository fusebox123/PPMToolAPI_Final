package com.cg.ppmtoolapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.ppmtoolapi.domain.Backlog;
import com.cg.ppmtoolapi.domain.Project;
import com.cg.ppmtoolapi.domain.ProjectTask;
import com.cg.ppmtoolapi.exception.ProjectNotFoundException;
import com.cg.ppmtoolapi.repository.BacklogRepository;
import com.cg.ppmtoolapi.repository.ProjectRepository;
import com.cg.ppmtoolapi.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	
	@Autowired
	private BacklogRepository backlogRepository;
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	@Autowired
	private ProjectRepository projectRepository;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		
		
		try {
			//ProjectTasks to be added to specific project, project!=null, Backlog Exists
			Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
			
			//Set the Backlog to project task
			projectTask.setBacklog(backlog);
			
			//We want our project Sequence to be like this. IDPRO-1 IDPRO-2 ...100 101

			Integer backLogSequence = backlog.getPTSequence();
			//Update the BacklogSequence
			backLogSequence++;
			backlog.setPTSequence(backLogSequence);
			
			//Add backlogSequence to ProjectTask
			projectTask.setProjectSequence(projectIdentifier+"-"+backLogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			//Initial priority when priority is null
			if(projectTask.getPriority()==null) {
				projectTask.setPriority(3);
			}
			//INITIAL Status when status is null
			if(projectTask.getStatus()=="" ||projectTask.getStatus()==null ) {
				projectTask.setStatus("TO_DO");
			}

			return projectTaskRepository.save(projectTask);
		} catch (Exception ex) {
			throw new ProjectNotFoundException("Specified Project Not Found, Please check your input");
		}
	}
	
	public Iterable<ProjectTask> findBacklogById(String projectIdentifier) {
		Project project = projectRepository.findByProjectIdentifier(projectIdentifier);
		if(project==null) {
			throw new ProjectNotFoundException("Project with id : "+projectIdentifier.toUpperCase()+" does not exist.");
		}
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier);
	}
	
	public ProjectTask findPTByProjectSequence(String backlog_id,String pt_id) {
		// Make sure that backlog_id exists - ProjectNotFoundException
		Backlog backlog =backlogRepository.findByProjectIdentifier(backlog_id);
		if(backlog==null) {
			throw new ProjectNotFoundException("Project with id : "+backlog_id.toUpperCase()+" does not exists");
		}
		
		// Make sure that project task id exists  -ProjectNotFoundException
		
		ProjectTask projectTask =  projectTaskRepository.findByProjectSequence(pt_id);
		if(projectTask==null) {
			throw new ProjectNotFoundException("Project Task with id : "+pt_id.toUpperCase()+" does not exists");
		}
		// Make sure that backlog_id and projectIdentifier is same -ProjectNotFoundException
		if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Backlog id : "+backlog_id.toUpperCase()+" does not match with project identifier "+projectTask.getProjectIdentifier().toUpperCase());
		}
		
		return projectTask;
		
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updateProjectTask, String backlog_id, String pt_id) {
		// Find the existing project task
		ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id);
		// Replace project task with updateProjectTask
		projectTask = updateProjectTask;
		// Save the projecttask
		return projectTaskRepository.save(projectTask);
		
	}
	
	public void deletePTByProjectSequence(String backlog_id, String pt_id) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id);
		Backlog backlog = projectTask.getBacklog();
		List<ProjectTask> pts =  backlog.getProjectTasks();
		pts.remove(projectTask);
		backlogRepository.save(backlog);
		projectTaskRepository.delete(projectTask);
		
	}

}
