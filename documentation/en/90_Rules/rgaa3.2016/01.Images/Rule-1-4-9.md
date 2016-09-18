# RGAA 3.2016 - Rule 1.4.9

## Summary
This test consists in detecting captcha svg and thus defining the applicability of the test.

Human check will be then needed to determine whether the alternative is well rendered by assistive technologies.

## Business description

### Criterion
[1.4](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#crit-1-4)

### Test
[1.4.9](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#test-1-4-9)

### Description
<div lang="fr">Pour chaque image vectorielle (balise <code lang="en">svg</code>) utilis&#xE9;e comme <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#captcha">CAPTCHA</a> ou comme <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#image-test">image-test</a>, poss&#xE9;dant une <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#alternative-svg">alternative</a>, cette alternative est-elle <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#correctement-restitue-par-les-technologies-dassistance">correctement restitu&#xE9;e</a> par les technologies d&#x2019;assistance&nbsp;?</div>

### Level
**A**

## Technical description

### Scope
**Page**

### Decision level
**Semi-Decidable**

## Algorithm

### Selection

#### Set1

All the `<svg>` tags of the page not within a link with a not empty `"aria-label"` attribute or a not empty `<desc>` child tag  (css selector : svg[aria-label]:not([aria-label~=^\\s*$]:not(a svg), svg:not(a svg):has(desc:not(:matchesOwn(^\\s*$)))

#### Set2

All the elements of **Set1** identified as a CAPTCHA (see Notes for details about CAPTCHA characterisation).

### Process

#### Test1

For each element of **Set2**, raise a MessageA

##### MessageA : Check captcha alternative restitution by assistive technologies

-    code : **CheckAtRestitutionOfAlternativeOfCaptcha** 
-    status: Pre-Qualified
-    parameter : `"title"` attribute, `"aria-label"` attribute, tag name, snippet
-    present in source : yes

### Analysis

#### Not Applicable

The page has no `<svg>` tag with an `"aria-label"` attribute or a `<desc>` child tag identified as a captcha (**Set2** is empty)

#### Pre-qualified

In all other cases

## Notes

### Captcha detection

An element is identified as a CAPTCHA when the "captcha" occurrence is found :

- on one attribute of the element
- or within the text of the element
- or on one attribute of one parent of the element
- or within the text of one parent of the element
- or on one attribute of a sibling of the element
- or within the text of a sibling of the element



##  TestCases

[TestCases files for rule 1.4.9](https://github.com/Asqatasun/Asqatasun/tree/develop/rules/rules-rgaa3.2016/src/test/resources/testcases/rgaa32016/Rgaa32016Rule010409/)


