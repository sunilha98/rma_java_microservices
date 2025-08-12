package com.resourcemgmt.reports.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.resourcemgmt.reports.entity.Project;
import com.resourcemgmt.reports.entity.Resource;
import com.resourcemgmt.reports.entity.Skillset;
import com.resourcemgmt.reports.entity.Sow;
import com.resourcemgmt.reports.reports.dto.BenchResourceDTO;
import com.resourcemgmt.reports.reports.dto.FinancialMetricDTO;
import com.resourcemgmt.reports.reports.dto.ForecastingDTO;
import com.resourcemgmt.reports.reports.dto.GovernanceDTO;
import com.resourcemgmt.reports.reports.dto.LessonLearnedDTO;
import com.resourcemgmt.reports.reports.dto.PortfolioDTO;
import com.resourcemgmt.reports.reports.dto.ProjectReportDTO;
import com.resourcemgmt.reports.reports.dto.ResourceAllocationDTO;
import com.resourcemgmt.reports.reports.dto.RiskIssueDTO;
import com.resourcemgmt.reports.reports.dto.SpendTrackingDTO;
import com.resourcemgmt.reports.repository.AllocationRepository;
import com.resourcemgmt.reports.repository.LessonLearnedRepository;
import com.resourcemgmt.reports.repository.ProjectRepository;
import com.resourcemgmt.reports.repository.ProjectStatusUpdateRepository;
import com.resourcemgmt.reports.repository.ResourceRepository;
import com.resourcemgmt.reports.repository.SowRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportsService {

	private final ProjectRepository projectRepository;
	private final AllocationRepository allocationRepository;
	private final ResourceRepository resourceRepository;
	private final SowRepository sowRepository;
	private final ProjectStatusUpdateRepository statusUpdateRepository;
	private final LessonLearnedRepository lessonLearnedRepository;

	public List<ProjectReportDTO> getInFlightProjects() {
		return projectRepository
				.findByStatus("IN_FLIGHT").stream().map(p -> new ProjectReportDTO(p.getId(), p.getName(),
						p.getClient().getName(), p.getStatus(), p.getStartDate(), p.getEndDate()))
				.collect(Collectors.toList());
	}

	public List<ProjectReportDTO> getProposedProjects() {
		return projectRepository
				.findByStatus("PROPOSED").stream().map(p -> new ProjectReportDTO(p.getId(), p.getName(),
						p.getClient().getName(), p.getStatus(), p.getStartDate(), p.getEndDate()))
				.collect(Collectors.toList());
	}

	public List<SpendTrackingDTO> getSpendTrackingReport() {
		List<Project> projects = projectRepository.findByStatus("IN_FLIGHT");

		return projects.stream().map(p -> {
			BigDecimal planned = Optional.ofNullable(p.getBudget()).orElse(BigDecimal.ZERO);
			BigDecimal actual = Optional.ofNullable(p.getActualCost()).orElse(BigDecimal.ZERO);
			BigDecimal variance = actual.subtract(planned);

			return new SpendTrackingDTO(p.getClient().getName(), p.getName(), planned.doubleValue(),
					actual.doubleValue(), variance.doubleValue());
		}).collect(Collectors.toList());
	}

	public List<RiskIssueDTO> getRisksAndIssuesReport() {
		return statusUpdateRepository.findAllWithRisks().stream().map(r -> new RiskIssueDTO(r.getId(),
				r.getProject().getName(), r.getRisks(), r.getIssues(), r.getProgress())).collect(Collectors.toList());
	}

	public List<ResourceAllocationDTO> getResourceAllocationReport() {
		return allocationRepository.findAll().stream()
			.map(a -> {
				Resource resource = a.getResource();
				String skillNames = resource.getSkillsets().stream()
					.map(Skillset::getName)
					.collect(Collectors.joining(", "));

				return new ResourceAllocationDTO(
					resource.getId(),
					resource.getFirstName(),
					resource.getLastName(),
					resource.getTitle() != null ? resource.getTitle().getName() : null,
					skillNames,
					a.getProject() != null ? a.getProject().getName() : null,
					a.getAllocationPercentage()
				);
			})
			.collect(Collectors.toList());
	}


	public List<BenchResourceDTO> getBenchTrackingReport() {
		List<Long> allocatedIds = allocationRepository.findAllAllocatedResourceIds();

		return resourceRepository.findAll().stream()
			.filter(r -> !allocatedIds.contains(r.getId()))
			.map(r -> {
				String skillNames = r.getSkillsets().stream()
					.map(Skillset::getName)
					.collect(Collectors.joining(", "));

				return new BenchResourceDTO(
					r.getId(),
					r.getFirstName(),
					r.getLastName(),
					r.getTitle() != null ? r.getTitle().getName() : null,
					skillNames,
					true
				);
			})
			.collect(Collectors.toList());
	}


	public List<ForecastingDTO> getForecastingReport() {
		// Group SoWs by title and skill (assuming skill is part of description or
		// another field)
		Map<String, List<Sow>> groupedSows = sowRepository.findAll().stream()
				.collect(Collectors.groupingBy(sow -> sow.getTitle(), 
						Collectors.toList()));

		List<ForecastingDTO> result = new ArrayList<>();

		for (Map.Entry<String, List<Sow>> entry : groupedSows.entrySet()) {
			String role = entry.getKey();
			int demand = entry.getValue().size();

			// Count available resources with matching title
			int available = resourceRepository.countByTitleName(role);

			result.add(new ForecastingDTO(role, "N/A", demand, available));
		}

		return result;
	}

	public List<FinancialMetricDTO> getFinancialMetricsReport() {
		return projectRepository.findAll().stream()
				.map(p -> new FinancialMetricDTO(p.getId(), p.getName(), p.getBudget(),
						p.getActualCost()))
				.collect(Collectors.toList());
	}

	public List<GovernanceDTO> getGovernanceReport() {
		return sowRepository.findAll().stream().map(s -> new GovernanceDTO(s.getId(), s.getClient().getName(),
				String.valueOf(s.getStatus()), s.getUpdatedAt())).collect(Collectors.toList());
	}

	public List<PortfolioDTO> getPortfolioDashboard() {
		return projectRepository.findAll().stream()
				.map(p -> new PortfolioDTO(p.getClient().getName(), p.getPractice().getName(), p.getStatus()))
				.collect(Collectors.toList());
	}

	public List<LessonLearnedDTO> getLessonsLearnedRepository() {
		return lessonLearnedRepository.findAll().stream().map(l -> new LessonLearnedDTO(l.getProject().getId(),
				l.getProject().getName(), l.getDescription(), l.getCategory())).collect(Collectors.toList());
	}
}
