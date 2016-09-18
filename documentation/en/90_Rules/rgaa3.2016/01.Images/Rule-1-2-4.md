# RGAA 3.2016 - Rule 1.2.4

## Summary
This test consists in checking whether the ARIA attribute of each decorative vector image (`<svg>` tag) are implemented correctly and checking each decorative vector image or children have no `"title"` or `"desc"` attribute unless they are empty.

## Business description

### Criterion
[1.2](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#crit-1-2)

### Test
[1.2.4](http://references.modernisation.gouv.fr/rgaa-accessibilite/criteres.html#test-1-2-4)

### Description
<div lang="fr">Chaque image vectorielle (balise <code lang="en">svg</code>) <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#image-de-dcoration">de d&#xE9;coration</a>, sans <a href="http://references.modernisation.gouv.fr/rgaa-accessibilite/glossaire.html#lgende-dimage">l&#xE9;gende</a>, v&#xE9;rifie-t-elle ces conditions&nbsp;? <ul><li>La balise svg poss&#xE8;de un attribut <code lang="en">aria-hidden="true"</code>&nbsp;;</li> <li>Les balises <code lang="en">title</code> et <code lang="en">desc</code> sont absentes ou vides&nbsp;;</li> <li>La balise <code lang="en">svg</code> ou l&#x2019;un de ses enfants est d&#xE9;pourvue d&#x2019;attribut <code lang="en">title</code>&nbsp;;</li> <li>La balise <code lang="en">svg</code> ou l&#x2019;un de ses enfants est d&#xE9;pourvue de r&#xF4;le, propri&#xE9;t&#xE9; ou &#xE9;tat ARIA visant &#xE0; labelliser l&#x2019;image vectorielle (<code lang="en">aria-label</code>, <code lang="en">aria-describedby</code>, <code lang="en">aria-labelledby</code> par exemple).</li> </ul></div>

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

All the `<svg>` tags of the page, not within a link not identified as captcha (see Notes about captcha detection) (css selector : `svg:not(a svg)`) 

#### Set2

All the elements of **Set1** identified as decorative image by marker usage (see Notes for details about detection through marker)

#### Set3

All the elements of **Set1** identified neither as informative image, nor as decorative image by marker usage (see Notes for details about detection through marker)

#### Set4

All the elements of **Set1** without a `"role"` attribute equals to  `"img"`.

#### Set5

All the elements of **Set1** with a not empty `<title>` or `<desc>` tag as child tag.

#### Set6

All the elements of **Set1** with a `"aria-label"`, `"aria-labelledby"` or  `"aria-describedby"` attribute on the element or one of its children.

#### Set7

All the elements of **Set1** with a `"title"` attribute.

#### Set8

All the elements of **Set2** with a not empty `<title>` or `<desc>` tag as child tag.

#### Set9

All the elements of **Set2** with a `"aria-label"`, `"aria-labelledby"` or  `"aria-describedby"` attribute on the element or one of its children.

#### Set10

All the elements of **Set2** with a `"title"` attribute.

#### Set11

All the elements of **Set2** without `"title"`, `"aria-label"`, `"aria-labelledby"`, `"aria-describedby"` attributes, without a not empty `<title>` or `<desc>` tag as child tag and with a `"role"` attribute equals to  `"img"`.

### Process

#### Tests

##### Test1

For each element of **Set4**, raise a MessageA.

##### Test2

For each element of **Set5**, raise a MessageB.

##### Test3 

For each element of **Set6**, raise a MessageC.

##### Test4 

For each element of **Set7**, raise a MessageD.

##### Test5 

For each element of **Set8**, raise a MessageE.

##### Test6 

For each element of **Set9**, raise a MessageF.

##### Test7 

For each element of **Set10**, raise a MessageG.

##### Test8 

For each element of **Set11**, raise a MessageH.

#### Messages

##### MessageA : Decorative svg without role img attribute 

-    code : DecorativeSvgWithoutRoleImgAttribute
-    status: Failed
-    parameter : tag name, Snippet
-    present in source : yes

##### MessageB : Decorative svg with a not empty `<title>` or `<desc>` child tag

-    code : DecorativeSvgWithNotEmptyTitleOrDescTags
-    status: Failed
-    parameter : tag name, Snippet
-    present in source : yes

##### MessageC : Decorative svg or children with Aria attribute

-    code : DecorativeSvgOrChildrenWithAriaAttribute
-    status: Failed
-    parameter : tag name, Snippet
-    present in source : yes

##### MessageD : Decorative svg or children with `title` attribute

-    code : DecorativeSvgWithTitleAttribute
-    status: Failed
-    parameter : tag name, Snippet
-    present in source : yes

##### MessageE : Suspected informative svg with a not empty `<title>` or  `<desc>` child tag

-    code : SuspectedInformativeSvgWithDescOrTitleChildTag
-    status: Pre-Qualified
-    parameter : tag name
-    present in source : yes

##### MessageF : Suspected informative svg with aria attribute on element or child

-    code : SuspectedInformativeSvgWithAriaAttributeDetectedOnElementOrChild
-    status: Pre-Qualified
-    parameter : tag name, Snippet
-    present in source : yes

##### MessageG : Suspected informative svg with title attribute on element or child

-    code : SuspectedInformativeSvgWithTitleAttributeOnElementOrChild
-    status: Pre-Qualified
-    parameter : tag name, Snippet
-    present in source : yes

##### MessageH : Suspected decorative svg without alternative

-    code : SuspectedWellFormedDecorativeSvg
-    status: Pre-Qualified
-    parameter : tag name, Snippet
-    present in source : yes

### Analysis

#### Not Applicable

The page has no `<svg>` tag (**Set1** is are empty)

#### Failed

At least one `<svg>` identified as decorative don't have a role img or have a `aria-label`, `aria-describedby`, `aria-labelledby` or `title` attribute or have a not empty `<title>` or `<desc>` as child tag (**Set4** OR **Set5** OR **Set6** OR **Set7** is not empty)

#### Passed

All the `<svg>` identified as decorative, have role img, no `aria-label`, `aria-describedby`, `aria-labelledby` or `title` attributes, no `<title>` or `<desc>` as child tag (**Set4** AND **Set5** AND **Set6** AND **Set7** are empty)

#### Pre-qualified

In all other cases

## Notes

The `<svg>` not identified by marker, without `"role"` attribute equals to "img" are not treated in this test. The test 1.3.5 asks to check that is attribute is present for informative `<svg>`. We can deduce this attribute has to be present for all `<svg>` in any way. To avoid to invalidate a same element twice, we decided to invalid this pattern in the test 1.3.5
 
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

[TestCases files for rule 1.2.4](https://github.com/Asqatasun/Asqatasun/tree/develop/rules/rules-rgaa3.2016/src/test/resources/testcases/rgaa32016/Rgaa32016Rule010204/)


