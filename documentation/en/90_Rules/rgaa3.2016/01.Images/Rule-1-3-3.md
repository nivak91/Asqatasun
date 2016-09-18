# RGAA 3.2016 - Rule 1.3.3

## Summary
This test consists in checking whether each button associated with an image that handles information has a relevant alternative.

## Business description

### Criterion
[1.3](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#crit-1-3)

### Test
[1.3.3](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#test-1-3-3)

### Description
<div lang="fr">Chaque <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#bouton-formulaire">bouton</a> associ&#xE9; &#xE0; une image (balise <code lang="en">input</code> avec l&#x2019;attribut <code lang="en">type="image"</code>), ayant un attribut <code lang="en">alt</code>, v&#xE9;rifie-t-il ces conditions (hors <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/cas-particuliers.html#cp-1-3" title="Cas particuliers pour le crit&#xE8;re 1.3">cas particuliers</a>)&nbsp;? <ul><li>Le contenu de l&#x2019;attribut <code lang="en">alt</code> est pertinent&nbsp;;</li> <li>S&#x2019;il est pr&#xE9;sent, le contenu de l&#x2019;attribut <code lang="en">title</code> est identique au contenu de l&#x2019;attribut <code lang="en">alt</code>&nbsp;;</li> <li>S&#x2019;il est pr&#xE9;sent, le contenu de la propri&#xE9;t&#xE9; <code lang="en">aria-label</code> est identique au contenu de l&#x2019;attribut <code lang="en">alt</code>&nbsp;;</li> <li>S&#x2019;il est pr&#xE9;sent, le contenu du passage de texte li&#xE9; <i>via</i> la propri&#xE9;t&#xE9; <code lang="en">aria-labelledby</code> est identique au contenu de l&#x2019;attribut <code lang="en">alt</code>.</li> </ul></div>

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

All the `<input>` tags with a `"type"` attribute equals to "image" and an `"alt"` attribute (css selector : `input[type=image][alt]`)

### Process

#### Test1

For all elements of **Set1**, check whether the content of the `"alt"` attribute is not relevant (see Notes for details about relevancy test). 

For each occurrence of true-result of **Test1**, raise a MessageA.

For each occurrence of false-result of **Test1**, raise a MessageB

##### MessageA 

-    code : **NotPertinentAlt** 
-    status: Failed
-    parameter : `"alt"` attribute, `"src"` attribute, tag name
-    present in source : yes

##### MessageB 

-    code : **CheckPertinenceOfAltAttributeOfInformativeImage** 
-    status: Pre-Qualified
-    parameter : `"alt"` attribute, `"src"` attribute, tag name
-    present in source : yes

### Analysis

#### Failed

At least one `<input>` tag with a `"type"` attribute equals to "image" has an irrelevant `"alt"` attribute (**Test1** returns true for at least one element)

#### Pre-qualified

The alternatives of all the `<input>` tags with a `"type"` attribute equals to "image" need to be manually checked (**Test1** returns false for all the elements of **Set1**) 

#### Not Applicable

The page has no `<input>` tag with a `"type"` attribute equals to "image" tag and an `"alt"` attribute (**Set1** is empty)

## Notes

The content of the `"alt"` attribute is seen as not relevant if :

- empty
- only composed of non-alphanumerical characters
- identical to the `"src"` attribute
- it has an extension of image type (loaded by the nomenclature named **ImageFileExtensions** composed of : jpg, gif, jpeg, png, bmp)



##  TestCases

[TestCases files for rule 1.3.3](https://github.com/Asqatasun/Asqatasun/tree/develop/rules/rules-rgaa3.2016/src/test/resources/testcases/rgaa32016/Rgaa32016Rule010303/)


