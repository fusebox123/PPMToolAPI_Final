package com.cg.ppmtoolapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.ppmtoolapi.domain.Backlog;
import com.cg.ppmtoolapi.domain.Project;
import com.cg.ppmtoolapi.domain.ProjectTask;
import com.cg.ppmtoolapi.exception.ProjectIdException;
import com.cg.ppmtoolapi.exception.ProjectNotFoundException;
import com.cg.ppmtoolapi.repository.BacklogRepository;
import com.cg.ppmtoolapi.repository.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private BacklogRepository backlogRepository;

	public Project saveOrUpdate(Project project) {
		try {
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			// When project is getting saved first time
			if (project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

			}
			// When project is getting updated
			if (project.getId() != null) {
				project.setBacklog(
						backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
			}
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException(
					"Project Id : " + project.getProjectIdentifier().toUpperCase() + " already exists");
		}
	}

	public Project findProjectByProjectIdentifier(String projectIdentifier) {
		Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
		if (project == null) {
			throw new ProjectIdException("Project Id : " + projectIdentifier.toUpperCase() + " does not exist");
		}
		return project;
	}

	public Iterable<Project> getAllProjects() {
		return projectRepository.findAll();
	}

	public void deleteProjectByIdentifier(String projectIdentifier) {
		Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
		if (project == null) {
			throw new ProjectIdException("Can not delete project with id : " + projectIdentifier.toUpperCase()
					+ ". This project does not exist");
		}
		projectRepository.delete(project);
	}

	

}
