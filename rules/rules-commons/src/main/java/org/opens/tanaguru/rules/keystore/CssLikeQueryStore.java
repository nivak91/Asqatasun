/*
 *  Tanaguru - Automated webpage assessment
 *  Copyright (C) 2008-2013  Open-S Company
 * 
 *  This file is part of Tanaguru.
 * 
 *  Tanaguru is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 * 
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *  Contact us by mail: open-s AT open-s DOT com
 */
package org.opens.tanaguru.rules.keystore;

/**
 * Utility class that stores css-like queries as static String
 */
public final class CssLikeQueryStore {
    
    // Image theme css-like queries
    public static final String IMG_WITHOUT_ALT_CSS_LIKE_QUERY="img:not([alt])"; 
    public static final String IMG_WITH_ALT_CSS_LIKE_QUERY="img[alt]"; 
    public static final String IMG_WITH_ALT_NOT_IN_LINK_CSS_LIKE_QUERY=
            "img[alt]:not(a img)"; 
    public static final String IMG_NOT_IN_LINK_CSS_LIKE_QUERY="img:not(a img)"; 
    public static final String IMG_WITH_ISMAP_ATTR_CSS_LIKE_QUERY="img[ismap]"; 
    public static final String IMG_WITH_ALT_WITHOUT_LONGDESC_CSS_LIKE_QUERY=
                    "img[alt]:not([longdesc])"; 
    public static final String APPLET_WITH_ALT_CSS_LIKE_QUERY=
                    "applet[alt]"; 
    public static final String APPLET_WITH_ALT_NOT_IN_LINK_CSS_LIKE_QUERY=
                    "applet[alt]:not(a applet)"; 
    public static final String APPLET_NOT_IN_LINK_CSS_LIKE_QUERY=
                    "applet:not(a applet)"; 
    public static final String OBJECT_TYPE_IMG_CSS_LIKE_QUERY=
                    "object[type^=image]"; 
    public static final String OBJECT_TYPE_IMG_NOT_IN_LINK_CSS_LIKE_QUERY=
                    "object[type^=image]:not(a object)"; 
    public static final String EMBED_TYPE_IMG_CSS_LIKE_QUERY=
                    "embed[type^=image]"; 
    public static final String EMBED_TYPE_IMG_NOT_IN_LINK_CSS_LIKE_QUERY=
                    "embed[type^=image]:not(a embed)"; 
    public static final String AREA_WITH_ALT_CSS_LIKE_QUERY=
                    "area[alt]"; 
    public static final String AREA_WITH_ALT_NOT_IN_LINK_CSS_LIKE_QUERY=
                    "area[alt]:not(a area)"; 
    public static final String AREA_NOT_IN_LINK_CSS_LIKE_QUERY=
                    "area:not(a area)"; 
    public static final String AREA_WITH_ALT_WITHOUT_HREF_ATTR_CSS_LIKE_QUERY=
                    "area[alt]:not([href])"; 
    public static final String FORM_BUTTON_CSS_LIKE_QUERY="input[type=image]"; 
    public static final String FORM_BUTTON_WITH_ALT_CSS_LIKE_QUERY=
                    "input[type=image][alt]";
    public static final String MAP_WITH_AREA_CHILD_AND_NAME_ATTR_CSS_LIKE_QUERY = 
                    "map:has(area)[name]";

    // Table theme css-like queries
    public static final String TABLE_WITH_SUMMARY_CSS_LIKE_QUERY="table[summary]"; 
    public static final String TABLE_WITH_CAPTION_CSS_LIKE_QUERY="table:has(caption)"; 
    public static final String TABLE_WITH_TH_CSS_LIKE_QUERY="table:has(th)";
    public static final String TABLE_WITH_TH_OR_TD_CSS_LIKE_QUERY=
                    "table:has(th), table:has(td)";
    public static final String DATA_TABLE_MARKUP_CSS_LIKE_QUERY =
                      "caption , "
                    + "th , "
                    + "thead , "
                    + "tfoot , "
                    + "colgroup , "
                    + "td[scope] , "
                    + "td[headers] , "
                    + "td[axis]";
    
    // Frame theme css-like queries
    public static final String FRAME_WITH_TITLE_CSS_LIKE_QUERY="frame[title]"; 
    public static final String IFRAME_WITH_TITLE_CSS_LIKE_QUERY="iframe[title]"; 
    public static final String IFRAME_WITH_NOT_EMPTY_TITLE_CSS_LIKE_QUERY=
                    "iframe[title]:not([title~=^\\s*$])"; 
    public static final String FRAME_WITH_NOT_EMPTY_TITLE_CSS_LIKE_QUERY=
                    "frame[title]:not([title~=^\\s*$])"; 

    // Form theme css-like queries
    public static final String FORM_WITHOUT_FIELDSET_CSS_LIKE_QUERY = 
                    "form:not(:has(fieldset))"; 
    public static final String FORM_ELEMENT_CSS_LIKE_QUERY=
                    "textarea , "
                   +"select , "
                   +"input[type=password] , "
                   +"input[type=checkbox] , "
                   +"input[type=file] , "
                   +"input[type=text] , "
                   +"input[type=radio]"; 
    public static final String FORM_ELEMENT_WITH_ID_CSS_LIKE_QUERY = 
                    "textarea[id] , "
                    + "select[id] , "
                    + "input[type=password][id] , "
                    + "input[type=checkbox][id] , "
                    + "input[type=file][id] , "
                    + "input[type=text][id] , "
                    + "input[type=radio][id]";
    public static final String FORM_ELEMENT_WITH_TITLE_CSS_LIKE_QUERY = 
                    "textarea[title] , "
                    + "select[title] , "
                    + "input[type=password][title] , "
                    + "input[type=checkbox][title] , "
                    + "input[type=file][title] , "
                    + "input[type=text][title] , "
                    + "input[type=radio][title]";
    public static final String FORM_TEXT_INPUT_CSS_LIKE_QUERY = 
                    "form:has(textarea) , "
                    + "form:has(input[type=password]) , "
                    + "form:has(input[type=text])";
    public static final String LABEL_WITHIN_FORM_CSS_LIKE_QUERY=
                    "form:has("+ FORM_ELEMENT_CSS_LIKE_QUERY +") label"; 
    public static final String FORM_LABEL_WITH_INNER_FORM_ELEMENT_CSS_LIKE_QUERY=
                    "form label:has(input[type=text]) , "
                    + "form label:has(input[type=password]) , "
                    + "form label:has(input[type=checkbox]) , "
                    + "form label:has(input[type=radio]) , "
                    + "form label:has(input[type=file]) , "
                    + "form label:has(textarea) , "
                    + "form label:has(select)";
    public static final String LEGEND_WITHIN_FIELDSET_CSS_LIKE_QUERY = 
                    "fieldset legend";
    public static final String SELECT_WITHOUT_OPTGROUP_CSS_LIKE_QUERY = 
                    "select:not(:has(optgroup))";     
    public static final String SELECT_WITHIN_FORM_CSS_LIKE_QUERY = 
                    "form select";
    public static final String OPTGROUP_WITH_LABEL_ATTR_CSS_LIKE_QUERY = 
                    "optgroup[label]";
    public static final String OPTGROUP_WITHIN_SELECT_WITH_LABEL_ATTR_CSS_LIKE_QUERY = 
                    "select optgroup[label]";
    public static final String OPTGROUP_WITHIN_SELECT_CSS_LIKE_QUERY = 
                    "select optgroup";
    public static final String BUTTON_FORM_CSS_LIKE_QUERY = 
                      "form input[type=submit] , "
                    + "form input[type=reset] , "
                    + "form input[type=button] , "
                    + "form input[type=image] , "
                    + "form button ";

    // Lang css-like queries
    public static final String ELEMENT_WITH_LANG_ATTR_CSS_LIKE_QUERY = 
                    "body[lang], body *[lang], body[xml:lang], body *[xml:lang]";
    public static final String ELEMENT_WITHOUT_LANG_ATTR_CSS_LIKE_QUERY = 
                      "body *:not(:matchesOwn(^\\s*$)):not([lang]):not([xml:lang]), "
                    + "body *[alt]:not([alt~=^\\s*$]):not([lang]):not([xml:lang]), "
                    + "body *[title]:not([title~=^\\s*$]):not([lang]):not([xml:lang]), "
                    + "body *[summary]:not([summary~=^\\s*$]):not([lang]):not([xml:lang])"
                    + "body *[content]:not([content~=^\\s*$]):not([lang]):not([xml:lang])"
                    + "body *[value]:not([value~=^\\s*$]):not([lang]):not([xml:lang])";

    // Mandatory elements css-like queries
    public static final String TITLE_WITHIN_HEAD_CSS_LIKE_QUERY = 
                    "head title";
    public static final String HTML_WITH_LANG_CSS_LIKE_QUERY = 
                    "html[lang], html[xml:lang]";
    
    /**
     * Private constructor. This class handles keys and must not be instanciated
     */
    private CssLikeQueryStore() {}

}