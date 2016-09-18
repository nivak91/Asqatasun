# RGAA 3.2016 - Rule 1.3.1

## Summary
This test consists in checking whether the `alt` attribute of each image that convey information is pertinent.

## Business description

### Criterion
[1.3](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#crit-1-3)

### Test
[1.3.1](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#test-1-3-1)

### Description
<div lang="fr">Chaque image (balise <code lang="en">img</code>) <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#image-porteuse-dinformation">porteuse d&#x2019;information</a>, ayant un attribut <code lang="en">alt</code>, v&#xE9;rifie-t-elle ces conditions (hors <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/cas-particuliers.html#cp-1-3" title="Cas particuliers pour le crit&#xE8;re 1.3">cas particuliers</a>)&nbsp;? <ul><li>Le contenu de l&#x2019;attribut <code lang="en">alt</code> est pertinent&nbsp;;</li> <li>S&#x2019;il est pr&#xE9;sent, le contenu de l&#x2019;attribut <code lang="en">title</code> est identique au contenu de l&#x2019;attribut <code lang="en">alt</code>&nbsp;;</li> <li>S&#x2019;il est pr&#xE9;sent, le contenu de la propri&#xE9;t&#xE9; <code lang="en">aria-label</code> est identique au contenu de l&#x2019;attribut <code lang="en">alt</code>&nbsp;;</li> <li>S&#x2019;il est pr&#xE9;sent, le contenu du passage de texte li&#xE9; <i>via</i> la propri&#xE9;t&#xE9; <code lang="en">aria-labelledby</code> est identique au contenu de l&#x2019;attribut <code lang="en">alt</code>.</li> </ul></div>

### Level
**A**

## Technical description

### Scope
**Page**

### Decision level
**Decidable**

## Algorithm

### Selection

##### Set1

All the `<img>` tags of the page not within a link not identified as captcha (see Notes about captcha detection) (css selector : `img:not(a img):not([longdesc])`)

#### Set2

All the elements of **Set1** with a `"longdesc"` attribute or identified as informative image by marker usage (see Notes for details about detection through marker)

#### Set3

All the elements of **Set1** identified neither as informative image, nor as decorative image by marker usage (see Notes for details about detection through marker)

### Process

#### Tests 

##### Test1

For each element of **Set2**, check whether the content of the `"alt"` attribute is not relevant (see Notes for details about relevancy test). 

For each occurrence of true-result of **Test1**, raise a MessageA.

For each occurrence of false-result of **Test1**, raise a MessageB.

##### Test2

For each element of **Set2**, check whether the content of the `"alt"` attribute is identical to the content of the `"title"` attribute.

For each occurrence of false-result of **Test2**, raise a MessageC.

##### Test3

For each element of **Set2**, check whether the content of the `"alt"` attribute is not relevant (see Notes for details about relevancy test). 

For each occurrence of true-result of **Test3**, raise a MessageD.

For each occurrence of false-result of **Test3**, raise a MessageE.

##### Test4

For each element of **Set2**, check whether the content of the `"alt"` attribute is identical to the content of the `"title"` attribute.

For each occurrence of false-result of **Test4**, raise a MessageD.

#### Messages

##### MessageA 

-    code : **NotPertinentAlt** 
-    status: Failed
-    parameter : `"alt"` attribute, `"title"` attribute, `"src"` attribute, tag name
-    present in source : yes

##### MessageB 

-    code : **CheckPertinenceOfAltAttributeOfInformativeImage** 
-    status: Pre-Qualified
-    parameter : `"alt"` attribute, `"title"` attribute, `"src"` attribute, tag name
-    present in source : yes

##### MessageC

-    code : **TitleNotIdenticalToAlt** 
-    status: Pre-Qualified
-    parameter : `"alt"` attribute, `"title"` attribute, `"src"` attribute, tag name
-    present in source : yes

##### MessageD

-    code : **CheckNatureOfImageWithNotPertinentAlt** 
-    status: Pre-Qualified
-    parameter : `"alt"` attribute, `"title"` attribute, `"src"` attribute, tag name
-    present in source : yes

##### MessageE

-    code : **CheckNatureOfImageAndAltPertinence** 
-    status: Pre-Qualified
-    parameter : `"alt"` attribute, `"title"` attribute, `"src"` attribute, tag name
-    present in source : yes

### Analysis

#### Failed

At least one `<img>` tag identified as informative has an irrelevant `"alt"` attribute (**Test1** returns true for at least one element)

#### Pre-qualified

The alternatives of all the `<img>` tags need to be manually checked (**Test1** returns false for all the elements of **Set1**) 

#### Not Applicable

The page has no `<img>` tag with an `"alt"` attribute (**Set1** is empty)

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

### Alt attribute relevancy 

The content of the `"alt"` attribute is seen as not relevant if :

- empty
- only composed of non-alphanumerical characters
- identical to the `"src"` attribute
- it has an extension of image type (loaded by the nomenclature named **ImageFileExtensions** composed of : jpg, gif, jpeg, png, bmp)



##  TestCases

[TestCases files for rule 1.3.1](https://github.com/Asqatasun/Asqatasun/tree/develop/rules/rules-rgaa3.2016/src/test/resources/testcases/rgaa32016/Rgaa32016Rule010301/)


