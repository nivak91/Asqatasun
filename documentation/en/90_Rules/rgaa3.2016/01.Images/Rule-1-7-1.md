# RGAA 3.2016 - Rule 1.7.1

## Summary
This test consists in detecting informative images and informative image form inputs and thus defining the applicability of the test.

Human check will be then needed to determine whether the detected elements provide a detailed description.

## Business description

### Criterion
[1.7](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#crit-1-7)

### Test
[1.7.1](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#test-1-7-1)

### Description
<div lang="fr">Chaque image (balise <code lang="en">img</code>) <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#image-porteuse-dinformation">porteuse d&#x2019;information</a>, ayant une <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#description-dtaille-image">description d&#xE9;taill&#xE9;e</a>, v&#xE9;rifie-t-elle une de ces conditions&nbsp;? <ul><li>La description d&#xE9;taill&#xE9;e <i>via</i> l&#x2019;adresse r&#xE9;f&#xE9;renc&#xE9;e dans l&#x2019;attribut <code lang="en">longdesc</code> est pertinente&nbsp;;</li> <li>La description d&#xE9;taill&#xE9;e dans la page et signal&#xE9;e dans l&#x2019;attribut <code lang="en">alt</code> est pertinente&nbsp;;</li> <li>La description d&#xE9;taill&#xE9;e <i>via</i> un <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#lien-adjacent">lien adjacent</a> est pertinente.</li> </ul></div>

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

All the `<img>` tags of the page not within a link and not identified as captcha (see Notes about captcha detection) (css selector : `img:not(a img)`)

##### Set2

All the `<input>` tags with the "type" attribute equals to "image" and not identified as captcha (see Notes about captcha detection) (css selector : `input[type=image]`)

#### Set3

All the elements of **Set1** identified as informative image by marker usage (see Notes for details about detection through marker)

#### Set4

All the elements of **Set2** identified as informative image by marker usage (see Notes for details about detection through marker)

#### Set5

All the elements of **Set1** identified neither as informative image, nor as decorative image by marker usage (see Notes for details about detection through marker)

#### Set6

All the elements of **Set2** identified neither as informative image, nor as decorative image by marker usage (see Notes for details about detection through marker)

### Process

#### Test1

For each element of **Set3** and **Set4**, raise a MessageA.

#### Test2

For each element of **Set5** and **Set6**, raise a MessageB.

##### MessageA : Check detailed description of informative images

-    code : **CheckDescriptionPertinenceOfInformativeImage** 
-    status: Pre-Qualified
-    parameter : `"src"` attribute, tag name, snippet
-    present in source : yes

##### MessageB : Check nature of image and detailed description 

-    code : **CheckNatureOfImageAndDescriptionPertinence** 
-    status: Pre-Qualified
-    parameter : `"src"` attribute, tag name, snippet
-    present in source : yes

### Analysis

#### Not Applicable 

The page has no <img> tag and image form inputs (**Set1** and **Set2** are empty)

#### Pre-Qualified

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

[TestCases files for rule 1.7.1](https://github.com/Asqatasun/Asqatasun/tree/develop/rules/rules-rgaa3.2016/src/test/resources/testcases/rgaa32016/Rgaa32016Rule010701/)


