/*
 * Asqatasun - Automated webpage assessment
 * Copyright (C) 2008-2015  Asqatasun.org
 *
 * This file is part of Asqatasun.
 *
 * Asqatasun is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact us by mail: asqatasun AT asqatasun DOT org
 */

package org.asqatasun.entity.dao.statistics;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;


import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.asqatasun.entity.audit.TestSolution;
import org.asqatasun.entity.reference.Criterion;
import org.asqatasun.entity.reference.Theme;
import org.asqatasun.entity.statistics.CriterionStatistics;
import org.asqatasun.entity.statistics.CriterionStatisticsImpl;
import org.asqatasun.entity.statistics.WebResourceStatistics;
import org.asqatasun.entity.subject.WebResource;
import org.asqatasun.entity.subject.WebResourceImpl;
import org.asqatasun.sdk.entity.dao.jpa.AbstractJPADAO;

/**
 *
 * @author jkowalczyk
 */
public class CriterionStatisticsDAOImpl extends AbstractJPADAO<CriterionStatistics, Long>
        implements CriterionStatisticsDAO {

    private static final Logger LOGGER = Logger.getLogger(CriterionStatisticsDAOImpl.class);
    private static final String JOIN_PROCESS_RESULT =" JOIN r.processResultSet pr";
    private static final String JOIN_TEST =" JOIN pr.test t";

    @Override
    protected Class<? extends CriterionStatistics> getEntityClass() {
        return CriterionStatisticsImpl.class;
    }

    @Override
    public Class<? extends WebResource> getWebResourceEntityClass() {
        return WebResourceImpl.class;
    }
    
    private String selectAllThemeKey;
    public String getSelectAllThemeKey() {
        return selectAllThemeKey;
    }

    public void setSelectAllThemeKey(String selectAllThemeKey) {
        this.selectAllThemeKey = selectAllThemeKey;
    }
    

    @Override
    public Long findResultCountByResultTypeAndCriterion(
            WebResource webResource,
            TestSolution testSolution,
            Criterion criterion) {
        Query query = entityManager.createQuery(
                "SELECT count(pr.id)FROM "
                + getWebResourceEntityClass().getName() + " r"
                + JOIN_PROCESS_RESULT
                + JOIN_TEST
                + " JOIN t.criterion cr"
                + " WHERE (r.id=:id OR r.parent.id=:id)"
                + " AND pr.definiteValue = :value"
                + " AND cr = :criterion");
        query.setParameter("id", webResource.getId());
        query.setParameter("value", testSolution);
        query.setParameter("criterion", criterion);
        try {
            return (Long)query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @Override
    public Long findResultCountByResultTypeAndTheme(
            WebResource webResource,
            TestSolution testSolution,
            Theme theme) {
        
        Query query = entityManager.createQuery(
                "SELECT count(pr.id)FROM "
                + getWebResourceEntityClass().getName() + " r"
                + JOIN_PROCESS_RESULT
                + JOIN_TEST
                + " JOIN t.criterion cr"
                + " JOIN t.criterion cr"
                + " WHERE (r.id=:id OR r.parent.id=:id)"
                + " AND pr.definiteValue = :value"
                + " AND cr = :criterion");
        query.setParameter("id", webResource.getId());
        query.setParameter("value", testSolution);
//        query.setParameter("criterion", criterion);
        try {
            return (Long)query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Collection<CriterionStatistics> findCriterionStatisticsByWebResource(
            WebResource webResource,
            String theme,
            Collection<String> testSolutions) {
        boolean hasTheme = false;
        if (theme != null && !StringUtils.equals(theme, selectAllThemeKey)) {
            hasTheme = true;
        }
        boolean hasTestSolution = false;
        if (!testSolutions.isEmpty()) {
            hasTestSolution = true;
        }
        StringBuilder strb = new StringBuilder();
        strb.append("SELECT cs FROM ");
        strb.append(getEntityClass().getName());
        strb.append(" cs ");
        strb.append(" JOIN cs.webResourceStatistics wrs ");
        if (hasTheme) {
            strb.append(" JOIN cs.criterion cr ");
        }
        strb.append(" WHERE wrs.webResource=:webResource ");
        if (hasTheme) {
            strb.append(" AND cr.theme.code=:theme ");
        }
        if (hasTestSolution) {
            strb.append(" AND cs.criterionResult IN (:testSolution) ");
        }
        Query query = entityManager.createQuery(strb.toString());
        query.setParameter("webResource", webResource);
        if (hasTestSolution) {
            Collection<TestSolution> solutions = new ArrayList<TestSolution>();
            for (String solution : testSolutions) {
                solutions.add(TestSolution.valueOf(solution));
            }
            query.setParameter("testSolution", solutions);
        }
        if (hasTheme) {
            query.setParameter("theme", theme);
        }
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Long findCriterionStatisticsCountByWebResource(Long webResourceId) {
        StringBuilder strb = new StringBuilder();
        strb.append("SELECT count(cs.id) FROM ");
        strb.append(getEntityClass().getName());
        strb.append(" cs ");
        strb.append(" JOIN cs.webResourceStatistics wrs ");
        strb.append(" WHERE wrs.webResource.id=:webResourceId ");
        Query query = entityManager.createQuery(strb.toString());
        query.setParameter("webResourceId", webResourceId);
        try {
            return (Long)query.getSingleResult();
        } catch (NoResultException e) {
            return Long.valueOf(0);
        }
    }

    /**
     * {@inheritDoc}
     * */
	@Override
	public CriterionStatistics findCriterionStatisticsByWebResource(
			Criterion criterion, WebResourceStatistics webResourceStatistics) {
		StringBuilder strb = new StringBuilder();
		strb.append("SELECT cs FROM ");
		strb.append(getEntityClass().getName());
		strb.append(" cs ");
		strb.append(" WHERE cs.webResourceStatistics=:webResourceId ");
		strb.append(" AND cs.criterion=:criterion ");
		Query query = entityManager.createQuery(strb.toString());
		query.setParameter("webResourceId", webResourceStatistics);
		query.setParameter("criterion", criterion);
		try {
			return (CriterionStatistics) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

    @Override
    public Integer FindWebResourceTestCount(Long IdWebResourceStatistics) {
        Query query = entityManager.createNativeQuery("SELECT SUM(Nb_Failed+Nb_Passed+Nb_Nmi) FROM" + getEntityClass().getName() + "cs" + "WHERE cs.Id_Web_Resource_Statistics = ?1");
        query.setParameter(1, IdWebResourceStatistics);
        try{
            return (Integer) query.getSingleResult();
        }
        catch (NoResultException e) {
            LOGGER.debug("WebResourceTestCountFail");
            return null;
        }
    }

    @Override
    public Integer FindWebResourceTestCountByTheme(Long IdWebResourceStatistics, int IdTheme) {
        Query query = entityManager.createNativeQuery("SELECT SUM(Nb_Failed+Nb_Passed+Nb_Nmi) FROM" + "(SELECT Nb_Failed,Nb_Passed,Nb_Nmi from" + getEntityClass().getName() + "cs" + "INNER JOIN CRITERION" + "cr" + "on cs.Id_Criterion=cr.Id_criterion" + "WHERE cs.Id_Web_Resource_Statistics = ?1" + "AND cr.Theme_Id_Theme = ?2)" + "as a");
        query.setParameter(1, IdWebResourceStatistics);
        query.setParameter(2, IdTheme);
        try{
            return (Integer) query.getSingleResult();
        }
        catch (NoResultException e) {
            LOGGER.debug("WebResourceTestCountByThemeFail");
            return null;
        }

    }

    @Override
    public Integer FindWebResourceTestCountByThemeAndResult(Long IdWebResourceStatistics, int IdTheme, TestSolution CriterionResult) {
        Query query = entityManager.createNativeQuery("SELECT SUM(Nb_Failed+Nb_Passed+Nb_Nmi) FROM" + "(SELECT Nb_Failed,Nb_Passed,Nb_Nmi from" + getEntityClass().getName() + "cs" + "INNER JOIN CRITERION" + "cr" + "on" + "cs.Id_Criterion=cr.Id_criterion" + "WHERE cs.Id_Web_Resource_Statistics= ?1" +  "AND cr.Theme_Id_Theme = ?2" + "AND cs.Criterion_Result = ?3)" + "as a");
        query.setParameter(1, IdWebResourceStatistics);
        query.setParameter(2, IdTheme);
        query.setParameter(3, CriterionResult);
        try{
            return (Integer) query.getSingleResult();
        }
        catch (NoResultException e) {
            LOGGER.debug("WebResourceTestCountByThemeAndResultFail");
            return null;
        }
    }

    @Override
    public Integer FindBarriers(Long IdWebResourceStatistics, int IdTheme, TestSolution CriterionResult, int IdLevel) {
        Query query = entityManager.createNativeQuery("SELECT SUM(Nb_Nmi) FROM" + "(SELECT  Nb_Nmi  FROM" + getEntityClass().getName() + "cs" + "inner join CRITERION" + "cr" + "ON cs.Id_Criterion=cr.Id_Criterion" + "inner join TEST" + "t" + "on t.Id_Criterion=cr.Id_Criterion" + "where cs.Id_Web_Resource_Statistics=?1" + "and cr.Theme_Id_Theme=?2" + "and cs.Criterion_Result=?3" + "and t.Id_Level=?4" + "group by Id_Criterion_Statistics)" + "AS a");
        query.setParameter(1, IdWebResourceStatistics);
        query.setParameter(2, IdTheme);
        query.setParameter(3, CriterionResult);
        query.setParameter(4, IdLevel);
        try{
            return (Integer) query.getSingleResult();
        }
        catch (NoResultException e) {
            LOGGER.debug("FindBarriersFail");
            return null;
        }
    }

    @Override
    public Integer FindPossibleBarriers(Long IdWebResourceStatistics, int IdTheme, TestSolution CriterionResult, int IdLevel) {
        Query query = entityManager.createNativeQuery("SELECT sum(Nb_Passed+Nb_nmi+Nb_Failed) FROM" + "(SELECT Nb_Passed, Nb_Nmi , Nb_Failed  FROM" + getEntityClass().getName() + "cs" + "inner join CRITERION" + "cr" + "ON cs.Id_Criterion=cr.Id_Criterion" + "inner join TEST" + "t" + "on t.Id_Criterion=cr.Id_Criterion" + "where cs.Id_Web_Resource_Statistics=?1" + "and cr.Theme_Id_Theme=?2" + "and cs.Criterion_Result=?3" + "and t.Id_Level=?4" + "group by Id_Criterion_Statistics)" + "AS a");
        query.setParameter(1, IdWebResourceStatistics);
        query.setParameter(2, IdTheme);
        query.setParameter(3, CriterionResult);
        query.setParameter(4, IdLevel);
        try{
            return (Integer) query.getSingleResult();
        }
        catch (NoResultException e) {
            LOGGER.debug("FindPossibleBarriersFail");
            return null;
        }
    }
}