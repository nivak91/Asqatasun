# RGAA 3.2016 - Rule 1.2.1

## Summary
This test consists in checking whether the `alt` attribute of each decorative image is empty.

## Business description

### Criterion
[1.2](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#crit-1-2)

### Test
[1.2.1](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#test-1-2-1)

### Description
<div lang="fr">Chaque image (balise <code lang="en">img</code>) <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#image-de-dcoration">de d&#xE9;coration</a>, sans <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#lgende-dimage">l&#xE9;gende</a>, et ayant un attribut <code lang="en">alt</code>, v&#xE9;rifie-t-elle ces conditions&nbsp;? <ul><li>Le contenu de l&#x2019;attribut <code lang="en">alt</code> est vide (<code lang="en">alt=""</code>)&nbsp;;</li> <li>L&#x2019;image de d&#xE9;coration ne poss&#xE8;de pas d&#x2019;attribut <code lang="en">title</code>&nbsp;;</li> <li>La balise <code lang="en">img</code> est d&#xE9;pourvue de r&#xF4;le, propri&#xE9;t&#xE9; ou &#xE9;tat ARIA visant &#xE0; labelliser l&#x2019;image (<code lang="en">aria-label</code>, <code lang="en">aria-describedby</code>, <code lang="en">aria-labelledby</code> par exemple).</li> </ul></div>

### Level
**A**

## Technical description

### Scope
**Page**

### Decision level
**Semi-Decidable**

## Algorithm

### Selection

##### Set1

All the `<img>` tags of the page not within a link, without `<longdesc>` attribute and not identified as captcha (see Notes about captcha detection) (css selector : `img:not(a img):not([longdesc])`)

#### Set2

All the elements of **Set1** identified as decorative image by marker usage (see Notes for details about detection through marker)

#### Set3

All the elements of **Set1** identified neither as informative image, nor as decorative image by marker usage (see Notes for details about detection through marker)

### Process

#### Tests

##### Test1 

For each element of **Set2** (decorative `<img>` identified by a html marker), check that the content of the `"alt"` attribute is empty. 

For each element returning false in **Test1**, raise a MessageA.

##### Test2 

For each element of **Set2** (decorative `<img>` identified by a html marker), check that the `"title"` attribute is missing. 

For each element returning false in **Test2**, raise a MessageB. 

##### Test3

For each element of **Set3** (`<img>` tags not identified as decorative image), check that the content of the `"alt"` attribute empty. 

For each element returning false in **Test3**, raise a MessageC.
For each element returning true in **Test3**, raise a MessageD.

##### Test4 

For each element of **Set3** (decorative `<img>` identified by a html marker), check that the `"title"` attribute is missing. 

For each element returning false in **Test4**, raise a MessageC.
For each element returning true in **Test4**, raise a MessageD.

#### Messages

##### MessageA : Decorative image with not empty alternative

-    code : DecorativeElementWithNotEmptyAltAttribute
-    status: Failed
-    parameter : `"alt"` attribute, `"title"` attribute, `"src"` attribute, Snippet
-    present in source : yes

##### MessageB : Decorative image with a title attribute

-    code : DecorativeElementWithTitleAttribute
-    status: Failed
-    parameter : `"alt"` attribute, `"title"` attribute, `"src"` attribute, Snippet
-    present in source : yes

##### MessageC : Check the nature of the image with a not empty alternative

-    code : CheckNatureOfElementWithNotEmptyAltAttribute
-    status: Pre-qualified
-    parameter : Snippet
-    present in source : yes

##### MessageD : Check the nature of the image with a empty alternative

-    code : CheckNatureOfElementWithEmptyAltAttribute
-    status: Pre-qualified
-    parameter : Snippet
-    present in source : yes

### Analysis

#### Not Applicable

The page has no `<img>` tag with an `"alt"` attribute (**Set1** is empty)

#### Failed

At least one `<img>` identified as decorative has a not empty alternative (**Test1** OR **Test2** returns false for at least one element)

#### Passed

All the `<img>` of the page are identified as decorative and have an empty alternative (**Set3** is empty and **Test1** AND **Test2** returns true for all element)

#### Pre-qualified

In all other cases

## Notes

### Markers 

**Informative images** markers are set through the **INFORMATIVE_IMAGE_MARKER** parameter.

**Decorative images** markers are set through the **DECORATIVE_IMAGE_MARKER** parameter.

The value(s) passed as marker(s) will be checked against the following attributes:

- `class`
- `id`
- `role`

### Captcha detection

An element is identified as a CAPTCHA when the "captcha" occurrence is found :

- on one attribute of the element
- or within the text of the element
- or on one attribute of one parent of the element
- or within the text of one parent of the element
- or on one attribute of a sibling of the element
- or within the text of a sibling of the element



##  TestCases

[TestCases files for rule 1.2.1](https://github.com/Asqatasun/Asqatasun/tree/develop/rules/rules-rgaa3.2016/src/test/resources/testcases/rgaa32016/Rgaa32016Rule010201/)


