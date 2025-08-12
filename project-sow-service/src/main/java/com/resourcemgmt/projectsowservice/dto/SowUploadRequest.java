package com.resourcemgmt.projectsowservice.dto;

import java.util.List;

public class SowUploadRequest {
	private String priority;
	private String clientName;
	private String projectName;
	private List<PositionRequest> positions;

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<PositionRequest> getPositions() {
		return positions;
	}

	public void setPositions(List<PositionRequest> positions) {
		this.positions = positions;
	}

	public static class PositionRequest {
		private String title;
		private String experience;
		private String skills;
		private String location;
		private String shift;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getExperience() {
			return experience;
		}

		public void setExperience(String experience) {
			this.experience = experience;
		}

		public String getSkills() {
			return skills;
		}

		public void setSkills(String skills) {
			this.skills = skills;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getShift() {
			return shift;
		}

		public void setShift(String shift) {
			this.shift = shift;
		}

	}
}
