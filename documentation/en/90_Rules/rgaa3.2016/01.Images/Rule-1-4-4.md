# RGAA 3.2016 - Rule 1.4.4

## Summary
This test consists in detecting captcha object images and thus defining the applicability of the test.

Human check will be then needed to determine whether the alternative is pertinent.

## Business description

### Criterion
[1.4](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#crit-1-4)

### Test
[1.4.4](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#test-1-4-4)

### Description
<div lang="fr">Chaque <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#image-objet">image objet</a> (balise <code lang="en">object</code> avec l&#x2019;attribut <code lang="en">type="image/…"</code>) utilis&#xE9;e comme <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#captcha">CAPTCHA</a> ou comme <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#image-test">image-test</a> v&#xE9;rifie-t-elle une de ces conditions&nbsp;? <ul><li>L&#x2019;image objet est imm&#xE9;diatement suivie d&#x2019;un <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#lien-adjacent">lien adjacent</a> permettant d&#x2019;afficher une page ou un passage de texte contenant une alternative permettant de comprendre la nature et la fonction de l&#x2019;image&nbsp;;</li> <li>Un m&#xE9;canisme permet &#xE0; l&#x2019;utilisateur de remplacer l&#x2019;image objet par un texte alternatif permettant de comprendre la nature et la fonction de l&#x2019;image&nbsp;;</li> <li>Un m&#xE9;canisme permet &#xE0; l&#x2019;utilisateur de remplacer l&#x2019;image objet par une image poss&#xE9;dant une alternative permettant de comprendre la nature et la fonction de l&#x2019;image.</li> </ul></div>

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

All the `<object>` tags with a `"type"` attribute that starts with "image/...", not within a link  (css selector : object[type^=image]:not(a object))

#### Set2

All the elements of **Set1** identified as a CAPTCHA (see Notes for details about CAPTCHA characterisation).

### Process

#### Test1

For each element of **Set2**, raise a MessageA

##### MessageA : Check captcha alternative

-    code : **CheckCaptchaAlternative** 
-    status: Pre-Qualified
-    parameter : text, `"data"` attribute, tag name, snippet
-    present in source : yes

### Analysis

#### Not Applicable

The page has no `<object>` tag with an `"type"` attribute that starts with "image" identified as a captcha (**Set2** is empty)

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

[TestCases files for rule 1.4.4](https://github.com/Asqatasun/Asqatasun/tree/develop/rules/rules-rgaa3.2016/src/test/resources/testcases/rgaa32016/Rgaa32016Rule010404/)


