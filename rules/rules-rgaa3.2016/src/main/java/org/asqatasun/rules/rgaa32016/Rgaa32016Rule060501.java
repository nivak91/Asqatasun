/*
 * Asqatasun - Automated webpage assessment
 * Copyright (C) 2008-2015  Asqatasun.org
 *
 * This program is free software: you can redistribute it and/or modify
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
package org.asqatasun.rules.rgaa32016;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jsoup.nodes.Element;
import org.asqatasun.entity.audit.TestSolution;
import org.asqatasun.processor.SSPHandler;
import org.asqatasun.ruleimplementation.AbstractPageRuleMarkupImplementation;
import org.asqatasun.ruleimplementation.ElementHandler;
import org.asqatasun.ruleimplementation.ElementHandlerImpl;
import org.asqatasun.ruleimplementation.TestSolutionHandler;
import org.asqatasun.rules.elementchecker.ElementChecker;
import org.asqatasun.rules.elementchecker.element.ElementPresenceChecker;
import org.asqatasun.rules.elementselector.ElementSelector;
import org.asqatasun.rules.elementselector.SimpleElementSelector;
import static org.asqatasun.rules.keystore.AttributeStore.ALT_ATTR;
import static org.asqatasun.rules.keystore.AttributeStore.HREF_ATTR;
import static org.asqatasun.rules.keystore.CssLikeQueryStore.NOT_ANCHOR_LINK_CSS_LIKE_QUERY;
import static org.asqatasun.rules.keystore.RemarkMessageStore.EMPTY_LINK_MSG;

/**
 * Implementation of the rule 6.5.1 of the referential RGAA 3.2016
 * <br/>
 * For more details about the implementation, refer to <a href="http://doc.asqatasun.org/en/90_Rules/rgaa3.2016/06.Links/Rule-6-5-1.html">the rule 6.5.1 design page.</a>
 * @see <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#test-6-5-1">6.5.1 rule specification</a>
 *
 */
public class Rgaa32016Rule060501 extends AbstractPageRuleMarkupImplementation {
    
    ElementHandler<Element> emptyLinksHandler = new ElementHandlerImpl();
    ElementHandler<Element> linksHandler = new ElementHandlerImpl();
    
    /**
     * Default constructor
     */
    public Rgaa32016Rule060501  () {
        super();
    }

    @Override
    protected void select(SSPHandler sspHandler) {
        ElementSelector elementsSelector = 
                new SimpleElementSelector(NOT_ANCHOR_LINK_CSS_LIKE_QUERY);
        elementsSelector.selectElements(sspHandler, linksHandler);
        for (Element el : linksHandler.get()) {
            if (StringUtils.isBlank(el.text()) && 
                    el.getElementsByAttributeValueMatching(ALT_ATTR, "^(?=\\s*\\S).*$").isEmpty()) {
                emptyLinksHandler.add(el);
            }
        }
    }

    @Override
    protected void check(
            SSPHandler sspHandler, 
            TestSolutionHandler testSolutionHandler) {

        if (linksHandler.isEmpty()) {
            testSolutionHandler.addTestSolution(TestSolution.NOT_APPLICABLE);
            return;
        }
        if (emptyLinksHandler.isEmpty()) {
            testSolutionHandler.addTestSolution(TestSolution.PASSED);
            return;
        }
        ElementChecker ec= new ElementPresenceChecker(
                        new ImmutablePair(TestSolution.FAILED,EMPTY_LINK_MSG),
                        new ImmutablePair(TestSolution.PASSED,""),
                        HREF_ATTR);
        ec.check(sspHandler, emptyLinksHandler, testSolutionHandler);
    }
    
    @Override
    public int getSelectionSize () {
        return linksHandler.size();
    }
}
