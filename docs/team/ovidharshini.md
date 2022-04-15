---
layout: page

title: Oviya's Project Portfolio Page
---

### Project: PeopleSoft

PeopleSoft is a CLI-based payroll management app for companies handling contractor-based services.

Given below are my contributions to the project.

* **New Feature**: Display of an overview of commands on the HelpWindow page
    * What it does: Allow for the display of messages from all command classes on a singular UI component.
    * Justification: In previous iterations, the HelpWindow contained a button that linked to the user guide on Github. While this technically fits the wording of the `help` command provided in the user guide, displaying the commands directly would be more user-friendly.
      * Highlights: A previous implementation was considered following the implementation for `Person` with similar `CommandHelpMessageCard` and `CommandHelpMessageListPanel` classes. TableView was selectively chosen over this implementation given its relative ease of use.
    * Credits: [Amos Chepchieng from Medium](https://medium.com/@keeptoo/adding-data-to-javafx-tableview-stepwise-df582acbae4f) for populating TableView with data programmatically

* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2122s2.github.io/tp-dashboard/?search=ovidharshini&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2022-02-18)

* **Team tasks**:
  * Reviewed [16 PRs](https://github.com/AY2122S2-CS2103T-T11-4/tp/pulls?q=type%3Apr+reviewed-by%3Aovidharshini+) and offered non-trivial comments. (Eg. PR [\#225](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/225))

* **Enhancements to existing features**:
  * Added checks for input validation of `Rate` and `Duration` [\#79](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/79)
  * Handle the addition of duplicate employees(PR [\#217](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/217))

* **Documentation**:
    * User Guide:
        * Updated command names after refactoring code
        * Fixed error with command summary display
    * Developer Guide:
        * Added user stories [\#233](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/233), [\#37](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/37)
        * Added proposed feature of pay multipliers [\#237](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/237)
        * Made cosmetic edits of various parts of DG, and adapted parts of AB-3 to better fit PeopleSoft. [\#233](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/233)
        * Added use cases

* **Community**:
    * Submitted [16 bugs](https://github.com/AY2122S2-CS2103T-T09-2/tp/issues?q=ovidharshini) during the PE dry-run

* **Tools**:
    * Sketched a [mockup](images/oviya_gui.png) of a possible graphical user interface in one of the first few weeks.
