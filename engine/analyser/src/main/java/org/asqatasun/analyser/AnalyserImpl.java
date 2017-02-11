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
package org.asqatasun.analyser;

import java.sql.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import org.asqatasun.entity.audit.Audit;
import org.asqatasun.entity.audit.ProcessResult;
import org.asqatasun.entity.audit.TestSolution;
import org.asqatasun.entity.parameterization.Parameter;
import org.asqatasun.entity.reference.Criterion;
import org.asqatasun.entity.reference.Test;
import org.asqatasun.entity.reference.Theme;
import org.asqatasun.entity.service.audit.AuditDataService;
import org.asqatasun.entity.service.audit.ProcessResultDataService;
import org.asqatasun.entity.service.statistics.CriterionStatisticsDataService;
import org.asqatasun.entity.service.statistics.TestStatisticsDataService;
import org.asqatasun.entity.service.statistics.ThemeStatisticsDataService;
import org.asqatasun.entity.service.statistics.WebResourceStatisticsDataService;
import org.asqatasun.entity.statistics.CriterionStatistics;
import org.asqatasun.entity.statistics.TestStatistics;
import org.asqatasun.entity.statistics.ThemeStatistics;
import org.asqatasun.entity.statistics.WebResourceStatistics;
import org.asqatasun.entity.subject.Page;
import org.asqatasun.entity.subject.Site;
import org.asqatasun.entity.subject.WebResource;

/**
 *
 * @author jkowalczyk
 */
public class AnalyserImpl implements Analyser {

    /**
     * The webResource used to extract statistics
     */
    private WebResource webResource;

    /**
     * The audit used to extract statistics
     */
    private Audit audit;
    
    /**
     * The auditStatisticsDataService instance needed to retrieve and save
     * auditStatistics instances
     */
    private final WebResourceStatisticsDataService webResourceStatisticsDataService;
    public WebResourceStatisticsDataService getWebResourceStatisticsDataService() {
        return webResourceStatisticsDataService;
    }
    
    /**
     * The ThemeStatisticsDataService instance
     */
    private final ThemeStatisticsDataService themeStatisticsDataService;
    
    /**
     * The CriterionStatisticsDataService instance
     */
    private final CriterionStatisticsDataService criterionStatisticsDataService;
    
    /**
     * THe testStatisticsDataService instance
     */
    private final TestStatisticsDataService testStatisticsDataService;
    
    /**
     * The auditDataService instance
     */
    private final AuditDataService auditDataService;
    
    /**
     * The ProcessResultDataService instance
     */
    private final ProcessResultDataService processResultDataService;
    
    private Map<Criterion, Integer> criterionMap;
    private Map<Theme, Integer> themeMap;
    private Collection<Test> testSet;
    private Map<Criterion, CriterionStatistics> csMap;
    private Map<Theme, ThemeStatistics> tsMap;
    
    private Collection<ProcessResult> netResultList;
    @Override
    public List<ProcessResult> getNetResultList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setNetResultList(final List<ProcessResult> netResultList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     * This attribute is used to compute the number of not tested tests. In case
     * of Page audit, this value is supposed to be 1 and thus, the not tested
     * value is computed from the difference between the total number of tests
     * for the given level and the qualified results. In case of Site audit, the
     * total number of tests (or criterions) has to multiplied by this value
     * before substracting the qualified results.
     *
     */
    private int nbOfWr = 0;
    /**
     * the set of audit parameters that handles some overridden values for test
     * weight (needed to compute the raw mark)
     */
    private final Collection<Parameter> paramSet;
    private static final BigDecimal ZERO = BigDecimal.valueOf(0.0);

    public AnalyserImpl(
            AuditDataService auditDataService,
            TestStatisticsDataService testStatisticsDataService,
            ThemeStatisticsDataService themeStatisticsDataService,
            WebResourceStatisticsDataService webResourceStatisticsDataService,
            CriterionStatisticsDataService criterionStatisticsDataService,
            ProcessResultDataService processResultDataService,
            WebResource webResource,
            Collection<Parameter> paramSet,
            int nbOfWr) {
        this.auditDataService = auditDataService;
        this.testStatisticsDataService = testStatisticsDataService;
        this.themeStatisticsDataService = themeStatisticsDataService;
        this.webResourceStatisticsDataService = webResourceStatisticsDataService;
        this.criterionStatisticsDataService = criterionStatisticsDataService;
        this.processResultDataService = processResultDataService;
        this.setWebResource(webResource);
        this.paramSet = paramSet;
        this.nbOfWr = nbOfWr;
    }

    @Override
    public float getResult() {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    @Override
    public void run() {
        WebResourceStatistics wrStats = webResourceStatisticsDataService.create();

        // Regardind the webResource type the computation of the statitics is 
        // done in memory or through the db
        if (webResource instanceof Page) {
            extractTestSet(false);
            netResultList = getProcessResultWithNotTested(
                    testSet,
                    processResultDataService.getNetResultFromAuditAndWebResource(audit, webResource));
            wrStats = computeAuditStatisticsFromPrList(wrStats);
            wrStats = computeHttpStatusCode(wrStats);
        } else if (webResource instanceof Site) {
            extractTestSet(true);
            wrStats = computeAuditStatisticsFromDb(wrStats);
            wrStats = computeCriterionStatisticsFromDb(wrStats);
            wrStats = computeTestStatisticsFromDb(wrStats);
            wrStats = computeThemeStatisticsFromDb(wrStats);
            /*wrStats = computeWQAM(wrStats);*/
        }
        /*wrStats = computeWQAM(wrStats);*/
        wrStats = computeMark(wrStats);
        wrStats = computeRawMark(wrStats);
        wrStats = computeNumberOfFailedOccurrences(wrStats);
        
        wrStats.setAudit(audit);
        wrStats.setWebResource(webResource);

        webResourceStatisticsDataService.saveOrUpdate(wrStats);
    }

    @Override
    public final void setWebResource(final WebResource webResource) {
        this.webResource = webResource;
        if (webResource instanceof Site) {
            this.audit = webResource.getAudit();
        } else if (webResource instanceof Page) {
            if (webResource.getAudit() != null) {
                this.audit = webResource.getAudit();
            } else if (webResource.getParent() != null) {
                this.audit = webResource.getParent().getAudit();
            }
        }
    }

    @Override
    public WebResource getWebResource() {
        return webResource;
    }

    /**
     * Gather the Http status code for a given web resource.
     *
     * @param wrStatistics
     * @return
     */
    private WebResourceStatistics computeHttpStatusCode(WebResourceStatistics wrStatistics) {
        wrStatistics.setHttpStatusCode(webResourceStatisticsDataService.getHttpStatusCodeByWebResource(webResource.getId()));
        return wrStatistics;
    }

    /**
     * To avoid multiple count requests to the db, the audits statistics are
     * computing by iterating through the ProcessResult list. The criterion
     * statistics and the theme statistics are collected on the fly while
     * parsing the collection of ProcessResult
     *
     * @param wrStatistics
     * @return
     */
    private WebResourceStatistics computeAuditStatisticsFromPrList(WebResourceStatistics wrStatistics) {
        
        int nbOfPassed = 0;
        int nbOfFailed = 0;
        int nbOfNmi = 0;
        int nbOfNa = 0;
        int nbOfDetected = 0;
        int nbOfSuspected = 0;
        int nbOfNt = 0;

        for (ProcessResult pr : netResultList) {
            TestSolution prResult = (TestSolution) pr.getValue();
            switch (prResult) {
                case PASSED:
                    nbOfPassed++;
                    break;
                case FAILED:
                    nbOfFailed++;
                    break;
                case NOT_APPLICABLE:
                    nbOfNa++;
                    break;
                case NEED_MORE_INFO:
                case DETECTED:
                case SUSPECTED_FAILED:
                case SUSPECTED_PASSED:
                    nbOfNmi++;
                    break;
                case NOT_TESTED:
                    nbOfNt++;
                    break;
            }
            addResultToCriterionCounterMap(prResult, pr.getTest().getCriterion());
            addResultToThemeCounterMap(prResult, pr.getTest().getCriterion().getTheme());
        }
        // if no test have been processed for any reason, mostly cause the source
        // code couldn't have been adapted, all theses values are set to -1
        if (nbOfFailed + nbOfNa + nbOfNmi + nbOfPassed + nbOfDetected + nbOfSuspected == 0) {
            nbOfFailed = nbOfNa = nbOfNmi = nbOfPassed = nbOfSuspected = nbOfDetected = -1;
        }
        wrStatistics.setNbOfFailed(nbOfFailed);
        wrStatistics.setNbOfInvalidTest(nbOfFailed);
        wrStatistics.setNbOfPassed(nbOfPassed);
        wrStatistics.setNbOfNmi(nbOfNmi);
        wrStatistics.setNbOfNa(nbOfNa);
        wrStatistics.setNbOfDetected(nbOfDetected);
        wrStatistics.setNbOfSuspected(nbOfSuspected);
        wrStatistics.setNbOfNotTested(nbOfNt);

        setWeightedResult(wrStatistics);
        
        // Compute criterion Result for each criterion and link each 
        // criterionStatistics to the current webResourceStatistics
        for (CriterionStatistics cs : csMap.values()) {
            computeCriterionResult(cs);
            wrStatistics.addCriterionStatistics(cs);
        }
        // Link each themeStatistics to the current webResourceStatistics
        for (ThemeStatistics ts : tsMap.values()) {
            wrStatistics.addThemeStatistics(ts);
        }

        wrStatistics.setAudit(audit);
        return wrStatistics;
    }

    /**
     *
     * @param testSolution
     * @param criterion
     */
    private void addResultToCriterionCounterMap(
            TestSolution testSolution,
            Criterion criterion) {
        if (csMap == null) {
            csMap = new HashMap();
        }
        if (csMap.containsKey(criterion)) {
            CriterionStatistics cs = csMap.get(criterion);
            incrementCriterionCounterFromTestSolution(cs, testSolution);
        } else {
            CriterionStatistics cs = criterionStatisticsDataService.create();
            cs.setCriterion(criterion);
            incrementCriterionCounterFromTestSolution(cs, testSolution);
            csMap.put(criterion, cs);
        }
    }

    /**
     * 
     * @param cs
     * @param testSolution 
     */
    private void incrementCriterionCounterFromTestSolution(
            CriterionStatistics cs,
            TestSolution testSolution) {
        switch (testSolution) {
            case PASSED:
                cs.setNbOfPassed(cs.getNbOfPassed() + 1);
                break;
            case FAILED:
                cs.setNbOfFailed(cs.getNbOfFailed() + 1);
                break;
            case NOT_APPLICABLE:
                cs.setNbOfNa(cs.getNbOfNa() + 1);
                break;
            case NEED_MORE_INFO:
            case DETECTED:
            case SUSPECTED_FAILED:
            case SUSPECTED_PASSED:
                cs.setNbOfNmi(cs.getNbOfNmi() + 1);
                break;
            case NOT_TESTED:
                cs.setNbOfNotTested(cs.getNbOfNotTested() + 1);
                break;
        }
    }

    /**
     *
     * @param testSolution
     * @param criterion
     */
    private void addResultToThemeCounterMap(
            TestSolution testSolution,
            Theme theme) {
        if (tsMap == null) {
            tsMap = new HashMap();
        }
        if (tsMap.containsKey(theme)) {
            ThemeStatistics ts = tsMap.get(theme);
            incrementThemeCounterFromTestSolution(ts, testSolution);
        } else {
            ThemeStatistics ts = themeStatisticsDataService.create();
            ts.setTheme(theme);
            incrementThemeCounterFromTestSolution(ts, testSolution);
            tsMap.put(theme, ts);
        }
    }

    /**
     * 
     * @param ts
     * @param testSolution 
     */
    private void incrementThemeCounterFromTestSolution(
            ThemeStatistics ts,
            TestSolution testSolution) {
        switch (testSolution) {
            case PASSED:
                ts.setNbOfPassed(ts.getNbOfPassed() + 1);
                break;
            case FAILED:
                ts.setNbOfFailed(ts.getNbOfFailed() + 1);
                break;
            case NOT_APPLICABLE:
                ts.setNbOfNa(ts.getNbOfNa() + 1);
                break;
            case NEED_MORE_INFO:
            case DETECTED:
            case SUSPECTED_FAILED:
            case SUSPECTED_PASSED:
                ts.setNbOfNmi(ts.getNbOfNmi() + 1);
                break;
            case NOT_TESTED:
                ts.setNbOfNotTested(ts.getNbOfNotTested() + 1);
                break;
        }
    }
    
    /**
     * Gather the audit statistics informations : - Number of passed results -
     * Number of failed results - Number of need_more_information results -
     * Number of not applicable results - Number of failed tests
     *
     * @param wrStatistics
     * @return
     */
    private WebResourceStatistics computeAuditStatisticsFromDb(WebResourceStatistics wrStatistics) {
        int nbOfPassed = webResourceStatisticsDataService.getResultCountByResultType(webResource.getId(),
                TestSolution.PASSED).intValue();

        int nbOfFailed = webResourceStatisticsDataService.getResultCountByResultType(webResource.getId(),
                TestSolution.FAILED).intValue();

        int nbOfNmi = webResourceStatisticsDataService.getResultCountByResultType(webResource.getId(),
                TestSolution.NEED_MORE_INFO).intValue();

        int nbOfNa = webResourceStatisticsDataService.getResultCountByResultType(webResource.getId(),
                TestSolution.NOT_APPLICABLE).intValue();

        int nbOfDetected = webResourceStatisticsDataService.getResultCountByResultType(webResource.getId(),
                TestSolution.DETECTED).intValue();

        int nbOfSuspected = webResourceStatisticsDataService.getResultCountByResultType(webResource.getId(),
                TestSolution.SUSPECTED_FAILED).intValue()
                + webResourceStatisticsDataService.getResultCountByResultType(webResource.getId(),
                TestSolution.SUSPECTED_PASSED).intValue();

        // if no test have been processed for any reason, mostly cause the source
        // code couldn't have been adapted, all theses values are set to -1
        if (nbOfFailed + nbOfNa + nbOfNmi + nbOfPassed + nbOfDetected + nbOfSuspected == 0) {
            nbOfFailed = nbOfNa = nbOfNmi = nbOfPassed = nbOfSuspected = nbOfDetected = -1;
        }

        wrStatistics.setNbOfFailed(nbOfFailed);
        wrStatistics.setNbOfInvalidTest(nbOfFailed);
        wrStatistics.setNbOfPassed(nbOfPassed);
        wrStatistics.setNbOfNmi(nbOfNmi);
        wrStatistics.setNbOfNa(nbOfNa);
        wrStatistics.setNbOfDetected(nbOfDetected);
        wrStatistics.setNbOfSuspected(nbOfSuspected);
        wrStatistics.setNbOfNotTested(
                testSet.size() * nbOfWr
                - nbOfDetected
                - nbOfSuspected
                - nbOfFailed
                - nbOfNa
                - nbOfNmi
                - nbOfPassed);

        setWeightedResult(wrStatistics);
        wrStatistics.setAudit(audit);
        return wrStatistics;
    }

    /**
     *
     * @param wrStatistics
     */
    private void setWeightedResult(WebResourceStatistics wrStatistics) {
        BigDecimal weightedPassed = webResourceStatisticsDataService.getWeightedResultByResultType(
                webResource.getId(),
                paramSet,
                TestSolution.PASSED,
                false);

        BigDecimal weightedFailed = webResourceStatisticsDataService.getWeightedResultByResultType(
                webResource.getId(),
                paramSet,
                TestSolution.FAILED,
                false);

        BigDecimal weightedNa = webResourceStatisticsDataService.getWeightedResultByResultType(
                webResource.getId(),
                paramSet,
                TestSolution.NOT_APPLICABLE,
                false);

        BigDecimal weightedNmi = webResourceStatisticsDataService.getWeightedResultByResultType(
                webResource.getId(),
                paramSet,
                TestSolution.NEED_MORE_INFO,
                false);
        wrStatistics.setWeightedFailed(weightedFailed);
        wrStatistics.setWeightedPassed(weightedPassed);
        wrStatistics.setWeightedNmi(weightedNmi);
        wrStatistics.setWeightedNa(weightedNa);
    }

     /**
     * This method computes WQAM metric
     * @param wrStatistics
     */
    /*public WebResourceStatistics computeWQAM (WebResourceStatistics wrStatistics)
    {
        Connection conn = null;
        try
        {
            conn = getConnection();
            Long AuditId=wrStatistics.getAudit().getId();
            PreparedStatement stmt = conn.prepareStatement(NP);
            stmt.setLong(1,AuditId);
            PreparedStatement stmt1 = conn.prepareStatement(min_page_ID);
            stmt1.setLong(1,AuditId);
            PreparedStatement stmt2 = conn.prepareStatement(max_page_ID);
            stmt2.setLong(1,AuditId);
            ResultSet rs = stmt.executeQuery();
            ResultSet rs1 = stmt1.executeQuery();
            ResultSet rs2 = stmt2.executeQuery();
            rs.next();
            rs1.next();
            rs2.next();
            int pages = rs.getInt(1);
            int min = rs1.getInt(1);
            int max = rs2.getInt(1);
            double NT_xy,N_T,NT_x,B,P;
            String Criterion_result;
            float WQAM=0,Score1=0,Score2=0,Score3=0,Score4=0;
            for(int i=min; i <= max; i++){
                Score3=0;
                PreparedStatement stmt3 = conn.prepareStatement(NT);
                stmt3.setInt(1,i);
                ResultSet rs3 = stmt3.executeQuery();
                rs3.next();
                N_T=rs3.getDouble(1);
                if (N_T==0) continue;
                for(int x=44; x<57; x++){
                    Score2=0;
                    PreparedStatement stmt4 = conn.prepareStatement(NTx);
                    stmt4.setInt(1,i);
                    stmt4.setInt(2,x);
                    ResultSet rs4 = stmt4.executeQuery();
                    rs4.next();
                    NT_x=rs4.getDouble(1);
                    if (NT_x==0) continue;
                    for(int y=0; y<=1; y++){
                        Score1=0;
                        if (y==0){
                            Criterion_result="NEED_MORE_INFO";
                            PreparedStatement stmt5 = conn.prepareStatement(NTxy);
                            stmt5.setInt(1,i);
                            stmt5.setInt(2,x);
                            stmt5.setString(3,Criterion_result);
                            ResultSet rs5 = stmt5.executeQuery();
                            rs5.next();
                            NT_xy=rs5.getDouble(1);
                        }
                        else{
                            Criterion_result="FAILED";
                            PreparedStatement stmt5 = conn.prepareStatement(NTxy);
                            stmt5.setInt(1,i);
                            stmt5.setInt(2,x);
                            stmt5.setString(3,Criterion_result);
                            ResultSet rs5 = stmt5.executeQuery();
                            rs5.next();
                            NT_xy=rs5.getDouble(1);
                        }
                        if(NT_xy==0) continue;
                        for(int z=0; z<=2; z++){
                            if(y==0){
                                PreparedStatement stmt6 = conn.prepareStatement(Bxyz1);
                                stmt6.setInt(1,i);
                                stmt6.setInt(2,x);
                                stmt6.setString(3,Criterion_result);
                                stmt6.setInt(4,z+1);
                                ResultSet rs6 = stmt6.executeQuery();
                                rs6.next();
                                B = rs6.getDouble(1);
                            }

                            else{
                                PreparedStatement stmt6 = conn.prepareStatement(Bxyz2);
                                stmt6.setInt(1,i);
                                stmt6.setInt(2,x);
                                stmt6.setString(3,Criterion_result);
                                stmt6.setInt(4,z+1);
                                ResultSet rs6 = stmt6.executeQuery();
                                rs6.next();
                                B = rs6.getDouble(1);
                            }



                            PreparedStatement stmt7 = conn.prepareStatement(Pxyz);
                            stmt7.setInt(1,i);
                            stmt7.setInt(2,x);
                            stmt7.setString(3,Criterion_result);
                            stmt7.setInt(4,z+1);

                            ResultSet rs7 = stmt7.executeQuery();

                            rs7.next();

                            P = rs7.getDouble(1);
                            if(P!=0){
                                double A=calculate_Score(B,P);
                                Score1+= W[z]*A;

                            }

                        }
                        Score2=(float)(Score2+((NT_xy/NT_x)*Score1));
                    }
                    Score3=(float)(Score3+(NT_x/N_T*Score2));
                }
                Score4+=Score3;

            }
            WQAM =Score4/pages;
            wrStatistics.setWQAM(WQAM);
            return wrStatistics;

        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

        Long AuditId=wrStatistics.getAudit().getId();
        Long NumberOfPages = webResourceStatisticsDataService.getNumberOfPages(AuditId);
        Long minWebResourceId = webResourceStatisticsDataService.getMinIdWebResourceStatistics(AuditId);
        Long maxWebResourceId = webResourceStatisticsDataService.getMaxIdWebResourceStatistics(AuditId);
        float Score=0,Score1=0,Score2=0,Score3=0,Score4=0,WQAM;
        double a=0.3,b=20;
        double W[] = {0.8,0.16,0.04};
        for(Long i = minWebResourceId; i <= maxWebResourceId; i++){
            Score3 = 0;
            Integer NumberOfTests = criterionStatisticsDataService.GetWebResourceTestCount(wrStatistics.getId());
            if(NumberOfTests==0) continue;

            for( int IdTheme=44; IdTheme<=57; IdTheme++) {
                Score2=0;
                Integer NumberOfTestsByTheme = criterionStatisticsDataService.GetWebResourceTestCountByTheme(i, IdTheme);
                if(NumberOfTestsByTheme == 0) continue;
                TestSolution[] test = {TestSolution.FAILED,TestSolution.NEED_MORE_INFO};
                for(TestSolution item : test){
                    Score1=0;
                    Integer NumberOfTestsByThemeAndCriterionResult = criterionStatisticsDataService.GetWebResourceTestCountByThemeAndResult(i, IdTheme, item);
                    if(NumberOfTestsByThemeAndCriterionResult == 0) continue;
                    for(int IdLevel=1; IdLevel<=3; IdLevel++){
                        Integer NumberOfBarriers = criterionStatisticsDataService.GetBarriers(i,IdTheme,item,IdLevel);
                        Integer NumberOfPossibleBarriers = criterionStatisticsDataService.GetPossibleBarriers(i,IdTheme,item,IdLevel);
                        if(NumberOfPossibleBarriers != 0){
                            if(NumberOfBarriers/NumberOfPossibleBarriers < (a-100) / (a / NumberOfPossibleBarriers - 100 / b)){
                                Score = (float) (NumberOfBarriers*(-100/b)+100);
                            }
                            else Score = (float) ((-a*NumberOfBarriers/NumberOfPossibleBarriers)+a);
                            Score1+=W[IdLevel-1]*Score;
                        }
                    }
                    Score2+=((NumberOfTestsByThemeAndCriterionResult/NumberOfTestsByTheme)*Score1);
                }
                Score3=Score3+(NumberOfTestsByTheme/NumberOfTests*Score2);
            }
            Score4+=Score3;
        }
        WQAM = Score4/NumberOfPages;
        wrStatistics.setWQAM(WQAM);
        return wrStatistics;
    }*/

    /**
     * This method compute the mark of the audit. Here is the algorithm formula
     * : ((1-ratioNMI) * passed/(passed+failed) + ratioNMI *
     * needMoreInfo/(passed+failed+needMoreInfo)) *100f where ratioNMI =
     * needMoreInfo / (passed+failed+needMoreInfo)
     *
     * @param wrStatistics
     * @return
     */
    public WebResourceStatistics computeMark(WebResourceStatistics wrStatistics) {
        float passed = wrStatistics.getNbOfPassed();
        // page on error, mark set to -1
        if (passed == -1) {
            wrStatistics.setRawMark(Float.valueOf(-1));
            return wrStatistics;
        }
        float failed = wrStatistics.getNbOfFailed();
        float needMoreInfo = wrStatistics.getNbOfNmi();
        if (failed == 0 && passed == 0) {
            wrStatistics.setMark(Float.valueOf(0));
            return wrStatistics;
        }
        float ratioNMI = needMoreInfo / (passed + failed + needMoreInfo);
        float result = ((1 - ratioNMI) * passed / (passed + failed) + ratioNMI * needMoreInfo / (passed + failed + needMoreInfo)) * 100f;
        wrStatistics.setMark(result);
        return wrStatistics;
    }

    /**
     * This method compute the raw mark of the audit. Here is the algorithm
     * formula : passed/(passed+failed)
     *
     * @param wrStatistics
     * @return
     */
    public WebResourceStatistics computeRawMark(WebResourceStatistics wrStatistics) {
        float passed = wrStatistics.getNbOfPassed();
        // page on error, mark set to -1
        if (passed == -1) {
            wrStatistics.setRawMark(Float.valueOf(-1));
            return wrStatistics;
        }
        BigDecimal weightedPassed = BigDecimal.valueOf(passed);
        BigDecimal weightedFailed = wrStatistics.getWeightedFailed();
        if ((weightedFailed.equals(BigDecimal.ZERO) || weightedFailed.equals(ZERO))
                && (weightedPassed.equals(BigDecimal.ZERO) || weightedPassed.equals(ZERO))) {
            wrStatistics.setRawMark(Float.valueOf(0));
            return wrStatistics;
        }

        float result = weightedPassed.divide(weightedPassed.add(weightedFailed), 4, RoundingMode.HALF_UP).floatValue() * 100f;
        wrStatistics.setRawMark(result);
        return wrStatistics;
    }

    /**
     * Gather the number of failed occurrence for a given web resource.
     *
     * @param wrStatistics
     * @return
     */
    private WebResourceStatistics computeNumberOfFailedOccurrences(WebResourceStatistics wrStatistics) {
        int nbOfFailedOccurences =
                webResourceStatisticsDataService.getNumberOfOccurrencesByWebResourceAndResultType(
                webResource.getId(),
                TestSolution.FAILED,
                false).intValue();
        wrStatistics.setNbOfFailedOccurences(nbOfFailedOccurences);
        return wrStatistics;
    }

    /**
     *
     * @param wrStatistics
     * @return
     */
    private WebResourceStatistics computeThemeStatisticsFromDb(WebResourceStatistics wrStatistics) {
        for (Theme theme : themeMap.keySet()) {
            ThemeStatistics themeStatistics = themeStatisticsDataService.create();
            themeStatistics.setTheme(theme);

            int nbOfFailed = themeStatisticsDataService.getResultCountByResultTypeAndTheme(webResource, TestSolution.FAILED, theme).intValue();
            themeStatistics.setNbOfFailed(nbOfFailed);

            int nbOfPassed = themeStatisticsDataService.getResultCountByResultTypeAndTheme(webResource, TestSolution.PASSED, theme).intValue();
            themeStatistics.setNbOfPassed(nbOfPassed);

            int nbOfNa = themeStatisticsDataService.getResultCountByResultTypeAndTheme(webResource, TestSolution.NOT_APPLICABLE, theme).intValue();
            themeStatistics.setNbOfNa(nbOfNa);

            int nbOfNmi = themeStatisticsDataService.getResultCountByResultTypeAndTheme(webResource, TestSolution.NEED_MORE_INFO, theme).intValue();
            nbOfNmi += themeStatisticsDataService.getResultCountByResultTypeAndTheme(webResource, TestSolution.SUSPECTED_FAILED, theme).intValue();
            nbOfNmi += themeStatisticsDataService.getResultCountByResultTypeAndTheme(webResource, TestSolution.SUSPECTED_PASSED, theme).intValue();
            themeStatistics.setNbOfNmi(nbOfNmi);

            int themeTestListSize = themeMap.get(theme);

            themeStatistics.setNbOfNotTested(
                    themeTestListSize * nbOfWr
                    - nbOfFailed
                    - nbOfNa
                    - nbOfNmi
                    - nbOfPassed);

            wrStatistics.addThemeStatistics(themeStatistics);
        }
        return wrStatistics;
    }

    /**
     *
     * @param wrStatistics
     * @return
     */
    private WebResourceStatistics computeCriterionStatisticsFromDb(WebResourceStatistics wrStatistics) {
        for (Criterion cr : criterionMap.keySet()) {
            CriterionStatistics criterionStatistics = criterionStatisticsDataService.create();
            criterionStatistics.setCriterion(cr);
            int nbOfFailed = criterionStatisticsDataService.getResultCountByResultTypeAndCriterion(webResource, TestSolution.FAILED, cr).intValue();
            criterionStatistics.setNbOfFailed(nbOfFailed);

            int nbOfNa = criterionStatisticsDataService.getResultCountByResultTypeAndCriterion(webResource, TestSolution.NOT_APPLICABLE, cr).intValue();
            criterionStatistics.setNbOfNa(nbOfNa);

            int nbOfPassed = criterionStatisticsDataService.getResultCountByResultTypeAndCriterion(webResource, TestSolution.PASSED, cr).intValue();
            criterionStatistics.setNbOfPassed(nbOfPassed);

            int nbOfNmi = criterionStatisticsDataService.getResultCountByResultTypeAndCriterion(webResource, TestSolution.NEED_MORE_INFO, cr).intValue();
            nbOfNmi += criterionStatisticsDataService.getResultCountByResultTypeAndCriterion(webResource, TestSolution.SUSPECTED_FAILED, cr).intValue();
            nbOfNmi += criterionStatisticsDataService.getResultCountByResultTypeAndCriterion(webResource, TestSolution.SUSPECTED_PASSED, cr).intValue();
            nbOfNmi += criterionStatisticsDataService.getResultCountByResultTypeAndCriterion(webResource, TestSolution.DETECTED, cr).intValue();
            criterionStatistics.setNbOfNmi(nbOfNmi);

            int criterionTestListSize = criterionMap.get(cr);
            criterionStatistics.setNbOfNotTested(
                    criterionTestListSize * nbOfWr
                    - nbOfFailed
                    - nbOfNa
                    - nbOfNmi
                    - nbOfPassed);
            computeCriterionResult(criterionStatistics);
            wrStatistics.addCriterionStatistics(criterionStatistics);
        }
        return wrStatistics;
    }

    /**
     * * Gather the following statistics informations for each theme: - Number
     * of passed results - Number of failed results - Number of
     * need_more_information results - Number of not applicable results - Number
     * of failed tests
     *
     * @param wrStatistics
     * @return
     */
    private WebResourceStatistics computeTestStatisticsFromDb(WebResourceStatistics wrStatistics) {
        for (Test test : testSet) {
            TestStatistics testStatistics = testStatisticsDataService.create();
            testStatistics.setTest(test);

            int nbOfFailed = testStatisticsDataService.getResultCountByResultTypeAndTest(webResource, TestSolution.FAILED, test).intValue();
            testStatistics.setNbOfFailed(nbOfFailed);

            int nbOfPassed = testStatisticsDataService.getResultCountByResultTypeAndTest(webResource, TestSolution.PASSED, test).intValue();
            testStatistics.setNbOfPassed(nbOfPassed);

            int nbOfNmi = testStatisticsDataService.getResultCountByResultTypeAndTest(webResource, TestSolution.NEED_MORE_INFO, test).intValue();
            testStatistics.setNbOfNmi(nbOfNmi);

            int nbOfNa = testStatisticsDataService.getResultCountByResultTypeAndTest(webResource, TestSolution.NOT_APPLICABLE, test).intValue();
            testStatistics.setNbOfNa(nbOfNa);

            testStatistics.setNbOfNotTested(nbOfWr - nbOfFailed - nbOfPassed - nbOfNmi - nbOfNa);

            wrStatistics.addTestStatistics(testStatistics);
        }
        return wrStatistics;
    }

    /**
     * This method extracts a collection of themes for a given audit
     *
     * @return
     */
    private void extractThemeAndCriterionSet() {
        themeMap = new HashMap();
        criterionMap = new HashMap();
        for (Test test : testSet) {

            //Collect criterions given the set of tests for the audit, and keep
            // the number of tests for each criterion (needed to calculate the
            // not tested
            Criterion criterion = test.getCriterion();
            if (criterionMap.containsKey(criterion)) {
                Integer testCounter = criterionMap.get(criterion) + 1;
                criterionMap.put(criterion, testCounter);
            } else {
                criterionMap.put(criterion, 1);
            }

            //Collect themes given the set of tests for the audit, and keep
            // the number of tests for each criterion (needed to calculate the
            // not tested
            Theme theme = criterion.getTheme();
            if (themeMap.containsKey(theme)) {
                Integer testCounter = themeMap.get(theme) + 1;
                themeMap.put(theme, testCounter);
            } else {
                themeMap.put(theme, 1);
            }

        }
    }

    /**
     * This method extracts a collection of tests for a given audit
     *
     * @return
     */
    private void extractTestSet(boolean extractThemeAndCriterion) {
        testSet = new HashSet();
        testSet.addAll(auditDataService.getAuditWithTest(this.audit.getId()).getTestList());
        if (extractThemeAndCriterion) {
            extractThemeAndCriterionSet();
        }
    }

    /**
     * This computation is based on the priority of the results : - priority 1 :
     * Failed - priority 2 : NMI - priority 3 : Not Tested - priority 4 : Passed
     * - priority 5 : NA
     *
     * If at least one of the result type is found regarding the priority
     * definition, the criterion result is the result type
     *
     * @param crs
     * @param criterionTestListSize
     */
    private void computeCriterionResult(CriterionStatistics crs) {
        if (crs.getNbOfFailed() > 0) {  // at least one test is failed, the criterion is failed
            crs.setCriterionResult(TestSolution.FAILED);
        } else if (crs.getNbOfNmi() > 0) { // at least one test is nmi and no failed test encountered, the criterion is nmi
            crs.setCriterionResult(TestSolution.NEED_MORE_INFO);
        } else if (crs.getNbOfNotTested() > 0) {
            crs.setCriterionResult(TestSolution.NOT_TESTED);
        } else if (crs.getNbOfPassed() > 0) {
            crs.setCriterionResult(TestSolution.PASSED);
        } else if (crs.getNbOfNa() > 0) {
            crs.setCriterionResult(TestSolution.NOT_APPLICABLE);
        } else {
            crs.setCriterionResult(TestSolution.NEED_MORE_INFO);
        }
    }

    /**
     * Some tests may have not ProcessResult, but are needed to be displayed as
     * not tested test. For each test without ProcessResult, we create a new
     * ProcessResult with NOT_TESTED as the result.
     *
     * @param testList
     * @param themeCode
     * @param netResultList
     * @return
     */
    private Collection<ProcessResult> getProcessResultWithNotTested(
            Collection<Test> testList,
            Collection<ProcessResult> netResultList) {

        Collection<Test> testedTestList = new ArrayList();
        for (ProcessResult pr : netResultList) {
            testedTestList.add(pr.getTest());
        }

        Collection<ProcessResult> fullProcessResultList = new ArrayList();
        fullProcessResultList.addAll(netResultList);

        for (Test test : testList) {
            // if the test has no ProcessResult and its theme is part of the user
            // selection, a NOT_TESTED result ProcessRemark is created
            if (!testedTestList.contains(test)) {
                ProcessResult pr = 
                        processResultDataService.getDefiniteResult(
                                test, 
                                TestSolution.NOT_TESTED);
                fullProcessResultList.add(pr);
            }
        }
        return fullProcessResultList;
    }

}