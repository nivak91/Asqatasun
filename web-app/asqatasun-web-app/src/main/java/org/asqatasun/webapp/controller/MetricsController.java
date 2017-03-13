
package org.asqatasun.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.asqatasun.entity.audit.Audit;
import org.asqatasun.entity.service.statistics.WebResourceStatisticsDataService;
import org.asqatasun.entity.statistics.WebResourceStatistics;
import org.asqatasun.entity.subject.WebResource;
import org.asqatasun.webapp.exception.ForbiddenPageException;
import org.asqatasun.webapp.util.TgolKeyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.log4j.Logger;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



@Controller
public class MetricsController extends AbstractAuditDataHandlerController{

    private static final Logger LOGGER = Logger.getLogger(MetricsController.class);


    /**
     *MySQL queries
     */
    static final String NumberOfPages = "SELECT COUNT(*) FROM asqatasun.WEB_RESOURCE_STATISTICS wrs WHERE wrs.Id_Audit = ? and wrs.Http_Status_Code = 200" ;
    static final String min_page_ID = "SELECT MIN(Id_Web_Resource_Statistics) FROM asqatasun.WEB_RESOURCE_STATISTICS WHERE Id_Audit = ? and Http_Status_Code = 200";
    static final String max_page_ID = "SELECT MAX(Id_Web_Resource_Statistics) FROM asqatasun.WEB_RESOURCE_STATISTICS WHERE Id_Audit = ? and Http_Status_Code = 200";
    static final String HttpStatusCode = "SELECT Http_Status_Code from asqatasun.WEB_RESOURCE_STATISTICS where Id_Web_Resource_Statistics = ?";
    static final String NumberOfTests = "SELECT SUM(Nb_Failed+Nb_Passed+Nb_Nmi) FROM asqatasun.CRITERION_STATISTICS WHERE Id_Web_Resource_Statistics = ?";
    static final String NumberOfTestsOfThemeX = "SELECT SUM(Nb_Failed+Nb_Passed+Nb_Nmi) FROM (SELECT Nb_Failed,Nb_Passed,Nb_Nmi FROM asqatasun.CRITERION_STATISTICS cs INNER JOIN asqatasun.CRITERION cr on cs.Id_Criterion=cr.Id_criterion WHERE cs.Id_Web_Resource_Statistics = ? AND cr.Theme_Id_Theme = ?) as a";
    static final String NumberOfTestsOfThemeXandResultY = "SELECT SUM(Nb_Failed+Nb_Passed+Nb_Nmi) FROM (SELECT Nb_Failed,Nb_Passed,Nb_Nmi from asqatasun.CRITERION_STATISTICS cs INNER JOIN asqatasun.CRITERION cr on cs.Id_Criterion=cr.Id_criterion WHERE cs.Id_Web_Resource_Statistics= ? AND cr.Theme_Id_Theme = ? AND cs.Criterion_Result = ?) as a";
    static final String BarriersOfThemeXpriorityZresultNeedMoreInfo = "SELECT SUM(Nb_Nmi) FROM (SELECT  Nb_Nmi  FROM asqatasun.CRITERION_STATISTICS cs inner join asqatasun.CRITERION cr ON cs.Id_Criterion=cr.Id_Criterion inner join asqatasun.TEST t on t.Id_Criterion=cr.Id_Criterion where cs.Id_Web_Resource_Statistics=? and cr.Theme_Id_Theme=? and cs.Criterion_Result=? and t.Id_Level=? group by Id_Criterion_Statistics) AS a";
    static final String BarriersOfThemeXpriorityZresultFailed = "SELECT SUM(Nb_Failed) FROM (SELECT  Nb_Failed  FROM asqatasun.CRITERION_STATISTICS cs inner join asqatasun.CRITERION cr ON cs.Id_Criterion=cr.Id_Criterion inner join asqatasun.TEST t on t.Id_Criterion=cr.Id_Criterion where cs.Id_Web_Resource_Statistics=? and cr.Theme_Id_Theme=? and cs.Criterion_Result=? and t.Id_Level=? group by Id_Criterion_Statistics) AS a";
    static final String PossibleBarriersXYZ = "SELECT sum(Nb_Passed+Nb_nmi+Nb_Failed) FROM (SELECT Nb_Passed, Nb_Nmi , Nb_Failed FROM asqatasun.CRITERION_STATISTICS cs inner join asqatasun.CRITERION cr ON cs.Id_Criterion=cr.Id_Criterion inner join asqatasun.TEST t on t.Id_Criterion=cr.Id_Criterion where cs.Id_Web_Resource_Statistics=? and cr.Theme_Id_Theme=? and cs.Criterion_Result=? and t.Id_Level=? group by Id_Criterion_Statistics) AS a";
    static final String BarriersOfThemeXpriorityZseverityLresultNeedMoreInfo = "SELECT SUM(Nb_Nmi) FROM (SELECT  Nb_Nmi  FROM asqatasun.CRITERION_STATISTICS cs inner join asqatasun.CRITERION cr ON cs.Id_Criterion=cr.Id_Criterion inner join asqatasun.TEST t on t.Id_Criterion=cr.Id_Criterion where cs.Id_Web_Resource_Statistics=? and cr.Theme_Id_Theme=? and cs.Criterion_Result=? and t.Id_Level=? and cr.criterion_severity= ?  group by Id_Criterion_Statistics) AS a";
    static final String BarriersOfThemeXpriorityZseverityLresultFailed = "SELECT SUM(Nb_Failed) FROM (SELECT  Nb_Failed  FROM asqatasun.CRITERION_STATISTICS cs inner join asqatasun.CRITERION cr ON cs.Id_Criterion=cr.Id_Criterion inner join asqatasun.TEST t on t.Id_Criterion=cr.Id_Criterion where cs.Id_Web_Resource_Statistics=? and cr.Theme_Id_Theme=? and cs.Criterion_Result=? and t.Id_Level=? and cr.criterion_severity= ? group by Id_Criterion_Statistics) AS a";
    static final String PossibleBarriersXYZL = "SELECT sum(Nb_Passed+Nb_nmi+Nb_Failed) FROM (SELECT Nb_Passed, Nb_Nmi , Nb_Failed FROM asqatasun.CRITERION_STATISTICS cs inner join asqatasun.CRITERION cr ON cs.Id_Criterion=cr.Id_Criterion inner join asqatasun.TEST t on t.Id_Criterion=cr.Id_Criterion where cs.Id_Web_Resource_Statistics=? and cr.Theme_Id_Theme = ? and cs.Criterion_Result=? and t.Id_Level=?  and cr.criterion_severity = ? group by Id_Criterion_Statistics) AS a";
    static final String NumberOfSitesWithLowerWQAM = "SELECT COUNT(*) FROM asqatasun.WEB_RESOURCE_STATISTICS wrs where wrs.Category = ? AND wrs.WQAM < ? ";
    static final String NumberOfSitesWithEqualWQAM = "SELECT COUNT(*) FROM asqatasun.WEB_RESOURCE_STATISTICS wrs where wrs.Category= ? AND wrs.WQAM = ? ";
    static final String NumberOfSitesWithLowerEnhancedWQAMlowVision = "SELECT COUNT(*) FROM asqatasun.WEB_RESOURCE_STATISTICS wrs where wrs.Category = ? AND wrs.Enhanced_WQAM_LowVision < ? ";
    static final String NumberOfSitesWithEqualEnhancedWQAMlowVision = "SELECT COUNT(*) FROM asqatasun.WEB_RESOURCE_STATISTICS wrs where wrs.Category= ? AND wrs.Enhanced_WQAM_LowVision = ? ";
    static final String NumberOfSitesWithLowerEnhancedWQAMcognitive = "SELECT COUNT(*) FROM asqatasun.WEB_RESOURCE_STATISTICS wrs where wrs.Category = ? AND wrs.Enhanced_WQAM_Cognitive < ? ";
    static final String NumberOfSitesWithEqualEnhancedWQAMcognitive = "SELECT COUNT(*) FROM asqatasun.WEB_RESOURCE_STATISTICS wrs where wrs.Category= ? AND wrs.Enhanced_WQAM_Cognitive = ? ";
    static final String NumberOfSitesWithLowerEnhancedWQAMmotor = "SELECT COUNT(*) FROM asqatasun.WEB_RESOURCE_STATISTICS wrs where wrs.Category = ? AND wrs.Enhanced_WQAM_Motor < ? ";
    static final String NumberOfSitesWithEqualEnhancedWQAMmotor = "SELECT COUNT(*) FROM asqatasun.WEB_RESOURCE_STATISTICS wrs where wrs.Category= ? AND wrs.Enhanced_WQAM_Motor = ? ";
    static final String NumberOfSitesInCategory = "SELECT COUNT(*) FROM asqatasun.WEB_RESOURCE_STATISTICS wrs where wrs.Category = ?";
    static final double[] W = {0.80, 0.16, 0.04}; /*weights for each prority level*/
    static final double[] L = {0.20, 0.40, 0.60, 0.80, 1}; /*weights for each severity level*/

    private WebResourceStatisticsDataService webResourceStatisticsDataService;
    public WebResourceStatisticsDataService getwebResourceStatisticsDataService() {
        return webResourceStatisticsDataService;
    }

    @Autowired
    public void setWebResourceStatisticsDataService(
        WebResourceStatisticsDataService webResourceStatisticsDataService) {
        this.webResourceStatisticsDataService = webResourceStatisticsDataService;
    }


    public Connection getConnection(String Url,String username,String password) throws Exception {
        // create our mysql database connection
        String myDriver = "org.gjt.mm.mysql.Driver";
        String myUrl = Url;
        Class.forName(myDriver);
        Connection conn = DriverManager.getConnection(myUrl, username, password);
        return conn;
    }

    public float calculate_Score(double B, double P){
        double a = 0.3;
        double b = 20;
        float A;
        if(B/P<(a-100)/(a/P-100/b)){
            A=(float)(B*(-100/b)+100);
        }
        else{
            A=(float)((-a*B/P)+a);
        }
        return A;

    }

    public float computeEnhancedWQAM (Long AuditId) {
        Connection conn = null;
        String Url = "jdbc:mysql://localhost:3306/asqatasun";
        String username = "asqatasun";
        String password = "asqaP4sswd";
        float EnhancedWQAM;
        try {
            conn = getConnection(Url,username,password);
            PreparedStatement stmt = conn.prepareStatement(NumberOfPages);
            stmt.setLong(1, AuditId);
            PreparedStatement stmt1 = conn.prepareStatement(min_page_ID);
            stmt1.setLong(1, AuditId);
            PreparedStatement stmt2 = conn.prepareStatement(max_page_ID);
            stmt2.setLong(1, AuditId);
            ResultSet rs = stmt.executeQuery();
            ResultSet rs1 = stmt1.executeQuery();
            ResultSet rs2 = stmt2.executeQuery();
            rs.next();
            rs1.next();
            rs2.next();
            long pages = rs.getLong(1);
            int min = rs1.getInt(1);
            int max = rs2.getInt(1);
            double NT_xy, N_T, NT_x, B, P;
            String Criterion_result;
            float Score1,Score2,Score3,Score4;
            float Score5 = 0;
            for (int i = 0; i <= max-min; i++) {
                PreparedStatement stmt0 = conn.prepareStatement(HttpStatusCode);
                stmt0.setInt(1,i+min);
                ResultSet rs0 = stmt0.executeQuery();
                rs0.next();
                int HttpStatusCode = rs0.getInt(1);
                if (HttpStatusCode != 200) continue;
                Score4 = 0;
                PreparedStatement stmt3 = conn.prepareStatement(NumberOfTests);
                stmt3.setInt(1, i+min);
                ResultSet rs3 = stmt3.executeQuery();
                rs3.next();
                N_T = rs3.getDouble(1);
                if (N_T == 0) continue;
                for (int x = 0; x <= 12; x++) {
                    Score3 = 0;
                    PreparedStatement stmt4 = conn.prepareStatement(NumberOfTestsOfThemeX);
                    stmt4.setInt(1, i+min);
                    stmt4.setInt(2, x+44);
                    ResultSet rs4 = stmt4.executeQuery();
                    rs4.next();
                    NT_x = rs4.getDouble(1);
                    if (NT_x == 0) continue;
                    for (int y = 0; y <= 1; y++) {
                        Score2 = 0;
                        if (y == 0) {
                            Criterion_result = "NEED_MORE_INFO";
                            PreparedStatement stmt5 = conn.prepareStatement(NumberOfTestsOfThemeXandResultY);
                            stmt5.setInt(1, i+min);
                            stmt5.setInt(2, x+44);
                            stmt5.setString(3, Criterion_result);
                            ResultSet rs5 = stmt5.executeQuery();
                            rs5.next();
                            NT_xy = rs5.getDouble(1);
                        } else {
                            Criterion_result = "FAILED";
                            PreparedStatement stmt5 = conn.prepareStatement(NumberOfTestsOfThemeXandResultY);
                            stmt5.setInt(1, i+min);
                            stmt5.setInt(2, x+44);
                            stmt5.setString(3, Criterion_result);
                            ResultSet rs5 = stmt5.executeQuery();
                            rs5.next();
                            NT_xy = rs5.getDouble(1);
                        }
                        if (NT_xy == 0) continue;
                        for (int z = 0; z <= 2; z++) {
                            Score1=0;
                            for (int l = 0; l <= 4; l++) {
                                if (y == 0) {
                                    PreparedStatement stmt6 = conn.prepareStatement(BarriersOfThemeXpriorityZseverityLresultNeedMoreInfo);
                                    stmt6.setInt(1, i+min);
                                    stmt6.setInt(2, x+44);
                                    stmt6.setString(3, Criterion_result);
                                    stmt6.setInt(4, z + 1);
                                    stmt6.setInt(5, l+1);
                                    ResultSet rs6 = stmt6.executeQuery();
                                    rs6.next();
                                    B = rs6.getDouble(1);
                                } else {
                                    PreparedStatement stmt6 = conn.prepareStatement(BarriersOfThemeXpriorityZseverityLresultFailed);
                                    stmt6.setInt(1, i+min);
                                    stmt6.setInt(2, x+44);
                                    stmt6.setString(3, Criterion_result);
                                    stmt6.setInt(4, z + 1);
                                    stmt6.setInt(5, l+1);
                                    ResultSet rs6 = stmt6.executeQuery();
                                    rs6.next();
                                    B = rs6.getDouble(1);
                                }
                                PreparedStatement stmt7 = conn.prepareStatement(PossibleBarriersXYZL);
                                stmt7.setInt(1, i+min);
                                stmt7.setInt(2, x+44);
                                stmt7.setString(3, Criterion_result);
                                stmt7.setInt(4, z + 1);
                                stmt7.setInt(5, l+1);

                                ResultSet rs7 = stmt7.executeQuery();

                                rs7.next();

                                P = rs7.getDouble(1);
                                if (P != 0) {
                                    double A = calculate_Score(B, P);
                                    Score1 += L[l] * A;
                                    LOGGER.debug("Score1 is:"+Score1);
                                }
                            }
                            Score2 += W[z]*Score1;
                            LOGGER.debug("Score2 is:"+Score2);
                        }
                        Score3 += (float) ((NT_xy / NT_x) * Score2);
                        LOGGER.debug("Score3 is"+Score3);
                    }
                    Score4 += (float)(NT_x / N_T * Score3);
                    LOGGER.debug("Score4 is:"+Score4);
                }
                Score5 += Score4;
                LOGGER.debug("Score5is:"+Score5);

            }
            EnhancedWQAM = Score5 / pages;
            LOGGER.debug(EnhancedWQAM);
            return EnhancedWQAM;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return 0;

        }

    }


    public float computeWQAM (Long AuditId) {
        Connection conn = null;
        String Url = "jdbc:mysql://localhost:3306/asqatasun";
        String username = "asqatasun";
        String password = "asqaP4sswd";
        float WQAM;
        try {
            conn = getConnection(Url,username,password);
            PreparedStatement stmt = conn.prepareStatement(NumberOfPages);
            stmt.setLong(1, AuditId);
            PreparedStatement stmt1 = conn.prepareStatement(min_page_ID);
            stmt1.setLong(1, AuditId);
            PreparedStatement stmt2 = conn.prepareStatement(max_page_ID);
            stmt2.setLong(1, AuditId);
            ResultSet rs = stmt.executeQuery();
            ResultSet rs1 = stmt1.executeQuery();
            ResultSet rs2 = stmt2.executeQuery();
            rs.next();
            rs1.next();
            rs2.next();
            long pages = rs.getLong(1);
            int min = rs1.getInt(1);
            int max = rs2.getInt(1);
            double NT_xy, N_T, NT_x, B, P;
            String Criterion_result;
            WQAM = 0;
            float Score1,Score2,Score3;
            float Score4 = 0;
            for (int i = 0; i <= max-min; i++) {
                PreparedStatement stmt0 = conn.prepareStatement(HttpStatusCode);
                stmt0.setInt(1,i+min);
                ResultSet rs0 = stmt0.executeQuery();
                rs0.next();
                int HttpStatusCode = rs0.getInt(1);
                if (HttpStatusCode != 200) continue;
                Score3 = 0;
                PreparedStatement stmt3 = conn.prepareStatement(NumberOfTests);
                stmt3.setInt(1, i+min);
                ResultSet rs3 = stmt3.executeQuery();
                rs3.next();
                N_T = rs3.getDouble(1);
                if (N_T == 0) continue;
                for (int x = 0; x <= 12; x++) {
                    Score2 = 0;
                    PreparedStatement stmt4 = conn.prepareStatement(NumberOfTestsOfThemeX);
                    stmt4.setInt(1, i+min);
                    stmt4.setInt(2, x+44);
                    ResultSet rs4 = stmt4.executeQuery();
                    rs4.next();
                    NT_x = rs4.getDouble(1);
                    if (NT_x == 0) continue;
                    for (int y = 0; y <= 1; y++) {
                        Score1 = 0;
                        if (y == 0) {
                            Criterion_result = "NEED_MORE_INFO";
                            PreparedStatement stmt5 = conn.prepareStatement(NumberOfTestsOfThemeXandResultY);
                            stmt5.setInt(1, i+min);
                            stmt5.setInt(2, x+44);
                            stmt5.setString(3, Criterion_result);
                            ResultSet rs5 = stmt5.executeQuery();
                            rs5.next();
                            NT_xy = rs5.getDouble(1);
                        } else {
                            Criterion_result = "FAILED";
                            PreparedStatement stmt5 = conn.prepareStatement(NumberOfTestsOfThemeXandResultY);
                            stmt5.setInt(1, i+min);
                            stmt5.setInt(2, x+44);
                            stmt5.setString(3, Criterion_result);
                            ResultSet rs5 = stmt5.executeQuery();
                            rs5.next();
                            NT_xy = rs5.getDouble(1);
                        }
                        if (NT_xy == 0) continue;
                        for (int z = 0; z <= 2; z++) {
                            if (y == 0) {
                                PreparedStatement stmt6 = conn.prepareStatement(BarriersOfThemeXpriorityZresultNeedMoreInfo);
                                stmt6.setInt(1, i+min);
                                stmt6.setInt(2, x+44);
                                stmt6.setString(3, Criterion_result);
                                stmt6.setInt(4, z + 1);
                                ResultSet rs6 = stmt6.executeQuery();
                                rs6.next();
                                B = rs6.getDouble(1);
                            } else {
                                PreparedStatement stmt6 = conn.prepareStatement(BarriersOfThemeXpriorityZresultFailed);
                                stmt6.setInt(1, i+min);
                                stmt6.setInt(2, x+44);
                                stmt6.setString(3, Criterion_result);
                                stmt6.setInt(4, z + 1);
                                ResultSet rs6 = stmt6.executeQuery();
                                rs6.next();
                                B = rs6.getDouble(1);
                            }


                            PreparedStatement stmt7 = conn.prepareStatement(PossibleBarriersXYZ);
                            stmt7.setInt(1, i+min);
                            stmt7.setInt(2, x+44);
                            stmt7.setString(3, Criterion_result);
                            stmt7.setInt(4, z + 1);

                            ResultSet rs7 = stmt7.executeQuery();

                            rs7.next();

                            P = rs7.getDouble(1);
                            if (P != 0) {
                                double A = calculate_Score(B, P);
                                Score1 += W[z] * A;

                            }

                        }
                        Score2 = (float) (Score2 + ((NT_xy / NT_x) * Score1));
                    }
                    Score3 = (float) (Score3 + (NT_x / N_T * Score2));
                }
                Score4 += Score3;

            }
            WQAM = Score4 / pages;
            return  WQAM;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return 0;

        }

    }
    @RequestMapping(value = TgolKeyStore.AUDIT_SYNTHESIS_CONTRACT_METRICS_PAGE_URL, method = RequestMethod.GET)
    public String MetricsPage(@RequestParam(TgolKeyStore.AUDIT_ID_KEY) String auditId,
                         HttpServletRequest request, HttpServletResponse response, Model model){
        Long aId,wId;
        float wqam,enhanced_wqam;

        try {
            aId = Long.valueOf(auditId);
        } catch (NumberFormatException nfe) {
            throw new ForbiddenPageException();
        }
        Audit audit = getAuditDataService().read(aId);
        wId=audit.getSubject().getId();
        model.addAttribute(TgolKeyStore.WEBRESOURCE_ID_KEY,wId);
        WebResource webresource = getWebResourceDataService().ligthRead(wId);
        WebResourceStatistics wrStat = getwebResourceStatisticsDataService().getWebResourceStatisticsByWebResource(webresource);
        wqam=computeWQAM(aId);
        enhanced_wqam=computeEnhancedWQAM(aId);
        wrStat.setWQAM(wqam);
        wrStat.setEnhancedWQAM(enhanced_wqam);
        getwebResourceStatisticsDataService().saveOrUpdate(wrStat);
        String SiteCategory = wrStat.getCategory();
        String DisabilityType = wrStat.getDisabilityType();
        model.addAttribute(TgolKeyStore.WQAMpercentileRank,WQAMpercentileRank(wqam, SiteCategory));
        model.addAttribute(TgolKeyStore.EnhancedWQAMpercentileRank,EnhancedWQAMpercentileRank(enhanced_wqam, SiteCategory, DisabilityType));
        model.addAttribute(TgolKeyStore.AUDIT_ID_KEY, aId);
        model.addAttribute(TgolKeyStore.WQAM, wqam);
        model.addAttribute(TgolKeyStore.EnhancedWQAM, enhanced_wqam);
        return TgolKeyStore.METRICS_VIEW_NAME;
    }

    public float WQAMpercentileRank(float WQAM, String siteCategory){

        Connection conn = null;
        try {
            String OkeanosVMUrl = "jdbc:mysql://83.212.101.228:3306/asqatasun";
            String username = "nikos";
            String password = "password";
            conn = getConnection(OkeanosVMUrl,username,password);
            PreparedStatement stmt = conn.prepareStatement(NumberOfSitesWithLowerWQAM);
            stmt.setString(1, siteCategory);
            stmt.setFloat(2, WQAM);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int SitesWithLowerWQAM = rs.getInt(1);
            LOGGER.debug(SitesWithLowerWQAM);
            PreparedStatement stmt1 = conn.prepareStatement(NumberOfSitesWithEqualWQAM);
            stmt1.setString(1, siteCategory);
            stmt1.setFloat(2, WQAM);
            ResultSet rs1 = stmt1.executeQuery();
            rs1.next();
            int SitesWithEqualWQAM = rs1.getInt(1);
            LOGGER.debug(SitesWithEqualWQAM);
            PreparedStatement stmt2 = conn.prepareStatement(NumberOfSitesInCategory);
            stmt2.setString(1, siteCategory);
            ResultSet rs2 = stmt2.executeQuery();
            rs2.next();
            int SitesOfSameCategory = rs2.getInt(1);
            LOGGER.debug(SitesOfSameCategory);
            return (float)(SitesWithLowerWQAM + SitesWithEqualWQAM * 0.5)/SitesOfSameCategory*100;
        }
        catch(Exception e){
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public float EnhancedWQAMpercentileRank(float EnhancedWQAM, String siteCategory, String DisabilityType){

        Connection conn = null;
        try {
            String OkeanosVMUrl = "jdbc:mysql://83.212.101.228:3306/asqatasun";
            String username = "nikos";
            String password = "password";
            conn = getConnection(OkeanosVMUrl, username, password);
            PreparedStatement stmt2 = conn.prepareStatement(NumberOfSitesInCategory);
            stmt2.setString(1, siteCategory);
            ResultSet rs2 = stmt2.executeQuery();
            rs2.next();
            int SitesOfSameCategory = rs2.getInt(1);
            LOGGER.debug(SitesOfSameCategory);
            float result = 0;
            if (DisabilityType.equals("Motor")) {
                PreparedStatement stmt = conn.prepareStatement(NumberOfSitesWithLowerEnhancedWQAMmotor);
                PreparedStatement stmt1 = conn.prepareStatement(NumberOfSitesWithEqualEnhancedWQAMmotor);
                stmt.setString(1, siteCategory);
                stmt.setFloat(2, EnhancedWQAM);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                int SitesWithLowerEnhancedWQAM = rs.getInt(1);
                LOGGER.debug(SitesWithLowerEnhancedWQAM);
                stmt1.setString(1, siteCategory);
                stmt1.setFloat(2, EnhancedWQAM);
                ResultSet rs1 = stmt1.executeQuery();
                rs1.next();
                int SitesWithEqualEnhancedWQAM = rs1.getInt(1);
                LOGGER.debug(SitesWithEqualEnhancedWQAM);
                result = (float) (SitesWithLowerEnhancedWQAM + SitesWithEqualEnhancedWQAM * 0.5) / SitesOfSameCategory * 100;
            } else if (DisabilityType.equals("LowVision")) {
                PreparedStatement stmt = conn.prepareStatement(NumberOfSitesWithLowerEnhancedWQAMlowVision);
                PreparedStatement stmt1 = conn.prepareStatement(NumberOfSitesWithEqualEnhancedWQAMlowVision);
                stmt.setString(1, siteCategory);
                stmt.setFloat(2, EnhancedWQAM);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                int SitesWithLowerEnhancedWQAM = rs.getInt(1);
                LOGGER.debug(SitesWithLowerEnhancedWQAM);
                stmt1.setString(1, siteCategory);
                stmt1.setFloat(2, EnhancedWQAM);
                ResultSet rs1 = stmt1.executeQuery();
                rs1.next();
                int SitesWithEqualEnhancedWQAM = rs1.getInt(1);
                LOGGER.debug(SitesWithEqualEnhancedWQAM);
                result =  (float) (SitesWithLowerEnhancedWQAM + SitesWithEqualEnhancedWQAM * 0.5) / SitesOfSameCategory * 100;
            } else if (DisabilityType.equals("Cognitive")) {
                PreparedStatement stmt = conn.prepareStatement(NumberOfSitesWithLowerEnhancedWQAMcognitive);
                PreparedStatement stmt1 = conn.prepareStatement(NumberOfSitesWithEqualEnhancedWQAMcognitive);
                stmt.setString(1, siteCategory);
                stmt.setFloat(2, EnhancedWQAM);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                int SitesWithLowerEnhancedWQAM = rs.getInt(1);
                LOGGER.debug(SitesWithLowerEnhancedWQAM);
                stmt1.setString(1, siteCategory);
                stmt1.setFloat(2, EnhancedWQAM);
                ResultSet rs1 = stmt1.executeQuery();
                rs1.next();
                int SitesWithEqualEnhancedWQAM = rs1.getInt(1);
                LOGGER.debug(SitesWithEqualEnhancedWQAM);
                result = (float) (SitesWithLowerEnhancedWQAM + SitesWithEqualEnhancedWQAM * 0.5) / SitesOfSameCategory * 100;
            }
            return result;
        }
        catch(Exception e){
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

}