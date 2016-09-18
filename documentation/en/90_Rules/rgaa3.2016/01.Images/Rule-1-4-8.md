# RGAA 3.2016 - Rule 1.4.8

## Summary
This test consists in detecting captcha svg and thus defining the applicability of the test.

Human check will be then needed to determine whether the alternative is pertinent.

## Business description

### Criterion
[1.4](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#crit-1-4)

### Test
[1.4.8](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#test-1-4-8)

### Description
<div lang="fr">Chaque image vectorielle (balise <code lang="en">svg</code>) utilis&#xE9;e comme <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#captcha">CAPTCHA</a> ou comme <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#image-test">image-test</a>, en l&#x2019;absence d&#x2019;<a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#alternative-svg">alternative</a>, v&#xE9;rifie-t-elle ces conditions&nbsp;? <ul><li>La balise <code lang="en">svg</code> poss&#xE8;de un <code lang="en">role="img"</code>&nbsp;;</li> <li>La balise <code lang="en">svg</code> poss&#xE8;de une propri&#xE9;t&#xE9; <code lang="en">aria-label</code> dont le contenu permet de comprendre la nature et la fonction de l&#x2019;image et identique &#xE0; l&#x2019;attribut <code lang="en">title</code> s&#x2019;il est pr&#xE9;sent&nbsp;;</li> <li>La balise <code lang="en">svg</code> poss&#xE8;de une balise <code lang="en">desc</code> dont le contenu permet de comprendre la nature et la fonction de l&#x2019;image et identique &#xE0; la propri&#xE9;t&#xE9; <code lang="en">aria-label</code> et &#xE0; l&#x2019;attribut <code lang="en">title</code> de la balise <code lang="en">svg</code> s&#x2019;il est pr&#xE9;sent&nbsp;;</li> <li>Un <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#lien-adjacent">lien adjacent</a> permet d&#x2019;acc&#xE9;der &#xE0; une alternative dont le contenu permet de comprendre la nature et la fonction de l&#x2019;image et identique &#xE0; la propri&#xE9;t&#xE9; <code lang="en">aria-label</code> et &#xE0; l&#x2019;attribut <code lang="en">title</code> de la balise <code lang="en">svg</code> s&#x2019;il est pr&#xE9;sent.</li> </ul></div>

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

##### MessageA : Check captcha alternative

-    code : **CheckCaptchaAlternative** 
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

[TestCases files for rule 1.4.8](https://github.com/Asqatasun/Asqatasun/tree/develop/rules/rules-rgaa3.2016/src/test/resources/testcases/rgaa32016/Rgaa32016Rule010408/)


