---
layout: page
title: Zhongfu's Project Portfolio Page
---

### Project: PeopleSoft

PeopleSoft is a CLI-based contractor payroll management app. It helps companies which offer contractor services with managing how much each contractor is paid. You can:

- manage contractors
- manage jobs
- calculate monthly salary

Given below are my contributions to the project.

* **New Feature**: Reusable styled table component for tables in app (part of [\#215](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/215))
  * What it does: Implements a reusable JavaFX control component that provides a table styled according to the intended design of the app.
  * Justification: At the moment, there are three tables used in the app, all of which have practically the same styling. However, each of them are implemented separately, which results in redundant code and inconsistencies when adjusting table styling, etc.
  * Highlights: Some workarounds had to be used in order to allow for the component to be easily used and instantiated in FXML files. Additionally, a significant amount of time had to be allocated to testing in multiple environments to ensure that the control appears as expected in all scenarios.
<br><br>
* **New Feature**: New model classes and supporting code required to support planned features (e.g. Payments, IDs)
  * What it does: Allow some new entities to be represented as objects, and allow for relationships not easily represented with objects
  * Justification: These model classes are required for supporting new features. Additionally, the planned features require many entity relationships for which having multiple references to the same object(s) would be troublesome, compounded by the fact that new objects are created when editing existing entities.
  * Highlights: Some care had to be taken when deciding where IDs were to be used, as overzealous use may negate some of the benefits of OOP.
<br><br>
* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2122s2.github.io/tp-dashboard/?search=zhongfu)
<br><br>
* **Team tasks**:
  * Handled initial triage for PE-D issues, and followed up on PRs and old issues
  * Reviewed [24 pull requests](https://github.com/AY2122S2-CS2103T-T11-4/tp/pulls?q=reviewed-by%3Azhongfu)
<br><br>
* **Enhancements to existing feature**:
  * New serializer/deserializer architecture for model classes [\#65](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/65), [\#88](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/88)
    * What it does: (De)serialize model classes to/from JSON with custom (de)serializers (bundled together with the model classes, as nested classes).
    * Justification: The previous (de)serialization mechanism is not as intuitive for new developers, as the relevant classes are spread across multiple packages and types reused in multiple classes cannot be automatically (de)serialized. It also makes implementing novel features (e.g. backward-compatibility) more difficult. This enhancement aims to alleviate some of those issues by allowing for increased flexibility, and by bundling the deser classes with the associated object classes.
    * Highlights: Deciding on an architecture that serves all our existing needs (and allows for easy extensibility) was somewhat complicated, as Jackson is a complex library that (as a result) presents many ways to perform any given task.
  * GUI bug fixes: Allow window size to increase/decrease w/ elements resizing to fit [#218](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/218), tag wrapping [#209](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/209)
  * Quality-of-life bug fixes (e.g. email regex changes [#69](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/69), `Desktop.browse()` hanging on Linux [#208](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/208))
<br><br>
* **Documentation**:
  * User Guide:
    * Changes to reflect feature changes and fix breakages (e.g. [\#200](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/200))
  * Developer Guide:
    * Explain serdes architecture and write tutorials on writing new serdes classes [\#87](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/87), [\#89](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/89)
<br><br>
* **Community**:
  * Participated in [13 forum discussions](https://github.com/nus-cs2103-AY2122S2/forum/issues?q=commenter%3Azhongfu)
  * Submitted [11 bugs](https://github.com/AY2122S2-CS2103T-T13-2/tp/issues?q=zhongfu) during the PE dry-run, 10 of which were accepted as-is (except for a [very-low severity bug](https://github.com/AY2122S2-CS2103T-T13-2/tp/issues/169) eventually marked as a documentation bug)
<br><br>
* **Tools**:
  * Added git pre-commit hooks that enforce correct style and all tests passing, as well as fix common issues (e.g. incorrect FXML versions caused by SceneBuilder [\#212](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/212))
  * Set up the official GitHub Telegram notification bot to post notifications of repo comments/new PRs/new commits to the team group chat
