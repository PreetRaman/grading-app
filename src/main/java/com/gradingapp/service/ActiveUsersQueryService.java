package com.gradingapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.gradingapp.domain.ActiveUsers;
import com.gradingapp.domain.*; // for static metamodels
import com.gradingapp.repository.ActiveUsersRepository;
import com.gradingapp.service.dto.ActiveUsersCriteria;
import com.gradingapp.service.dto.ActiveUsersDTO;
import com.gradingapp.service.mapper.ActiveUsersMapper;

/**
 * Service for executing complex queries for ActiveUsers entities in the database.
 * The main input is a {@link ActiveUsersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ActiveUsersDTO} or a {@link Page} of {@link ActiveUsersDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActiveUsersQueryService extends QueryService<ActiveUsers> {

    private final Logger log = LoggerFactory.getLogger(ActiveUsersQueryService.class);

    private final ActiveUsersRepository activeUsersRepository;

    private final ActiveUsersMapper activeUsersMapper;

    public ActiveUsersQueryService(ActiveUsersRepository activeUsersRepository, ActiveUsersMapper activeUsersMapper) {
        this.activeUsersRepository = activeUsersRepository;
        this.activeUsersMapper = activeUsersMapper;
    }

    /**
     * Return a {@link List} of {@link ActiveUsersDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ActiveUsersDTO> findByCriteria(ActiveUsersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ActiveUsers> specification = createSpecification(criteria);
        return activeUsersMapper.toDto(activeUsersRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ActiveUsersDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ActiveUsersDTO> findByCriteria(ActiveUsersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ActiveUsers> specification = createSpecification(criteria);
        return activeUsersRepository.findAll(specification, page)
            .map(activeUsersMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ActiveUsersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ActiveUsers> specification = createSpecification(criteria);
        return activeUsersRepository.count(specification);
    }

    /**
     * Function to convert ActiveUsersCriteria to a {@link Specification}
     */
    private Specification<ActiveUsers> createSpecification(ActiveUsersCriteria criteria) {
        Specification<ActiveUsers> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ActiveUsers_.id));
            }
            if (criteria.getUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsername(), ActiveUsers_.username));
            }
            if (criteria.getLogin_time() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLogin_time(), ActiveUsers_.login_time));
            }
            if (criteria.getLogout_time() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLogout_time(), ActiveUsers_.logout_time));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), ActiveUsers_.active));
            }
        }
        return specification;
    }
}
