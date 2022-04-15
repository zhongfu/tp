---
layout: page
title: Elliot's Project Portfolio Page
---

## Project: PeopleSoft

PeopleSoft is a payroll management app for companies handling contractor-based services.

### Summary of Contributions

Here are some ways I have contributed to this project.

* **New Feature**: Added basic create and delete functionality for jobs. [\#81](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/81)
  * Details: Added `add` and `delete` commands which allows the user to create and delete new jobs
    respectively.
  * Justification: These are basic features which are necessary for a payroll management application.
* **New Feature**: Added basic search functionality for jobs. [\#109](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/109)
  * Details: Added `list` which allows users to see all the jobs that have been stored. Also added `find` to
    allow users to search for jobs by keyword queries.
  * Justification: As much of the application relies on the state of the jobs being displayed on the UI, these
    features were necessary to help the user search through jobs to be able to use other features of the
    application more efficiently.
* **New Feature**: Added the functionality of marking jobs as completed. [\#81](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/81)
  * Details: Added `mark` which allows users to mark a job as completed.
  * Justification: As a payroll management application, the state of the completion of a job needs to be
    tracked. This feature models how there can be synchronous state change of jobs when they are completed
    in the real-world. However, the ability to reverse the completed state is also available in the event of
    user errors.
* **New Feature**: Added the functionality of assigning jobs to persons. [\#81](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/81)
  * Details: Added `assign` which allows users to assign a job to one or more persons.
  * Justification: As a payroll management application, the association between jobs and persons is important.
    It was necessary to include this association which models the real-world where one or more persons can
    take on jobs.
* **New Feature**: Added the functionality of finalizing payments for jobs. [\#128](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/128)
  * Details: Added `pay` which allows users to finalize payments for a job.
  * Justification: As a payroll management application, one defining feature to have is the ability to track
    payments. As this application is created to simplify the process of managing and calculating pay, it was
    necessary to add this feature to fulfill the product specifications of payroll management software.
* **New Implementation**: Added base implementation for jobs. [\#46](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/46)
  * Details: Adds a simple abstraction for a job, including its related attributes, encapsulated within
    the `Job` class.
  * Justification: As the application is a payroll management application built off a basic address book
    design, it was initially missing an implementation for jobs. It was necessary to add a base
    implementation for jobs in order to fulfill the basic product specifications of payroll management
    software, which includes managing jobs.
* **New Implementation**: Added associations for jobs and persons. [\#81](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/81), [\#106](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/106)
  * Details: Adds an abstraction for the association between jobs and persons, encapsulated within the
    `Employment` class.
  * Justification: Similar to how people can take on jobs in the real-world, the role of `Employment` is to
    model this same association that people can have with taking on jobs. This is the framework for which
    certain functionality such as `assign` are built upon.
* **Enhancement**: Added implementation for the generation of job IDs. [\#81](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/81)
  * Details: Job IDs play the role of uniquely identifying jobs internally. This allows for the assignment of
    unique job IDs in the creation of new jobs.
* **Enhancement**: Added logic for the creation and management of payment objects. [\#128](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/128)
  * Details: As `Payment` objects are used internally to calculate the actual payment of a person, some logic
    was required in the creation and management of these `Payment` objects.
* **Enhancement**: Supported in adding internal JSON serialization/deserialization sub-classes for
    job-related classes. [\#81](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/81)
  * Details: As PeopleSoft creates local data to store the state of the application, some implementations
    were added to handle JSON serialization/deserialization of some classes.
* **Enhancement**: Added some tests for automated testing of new features. (e.g. [\#109](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/109))
  * Details: Some tests were added to increase the test coverage of new features, including but not limited to
    those mentioned above. The tests give some assurance of correctness in the main functionality and error
    handling, and helps to manage regressions caused by new changes.
* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2122s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2022-02-18&tabOpen=true&tabType=authorship&tabAuthor=Spyobird&tabRepo=AY2122S2-CS2103T-T11-4%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false)
* **Project management**: Managed Continuous Integration (CI) of team repository.
* **Team tasks**:
  * Reviewed [23 pull requests](https://github.com/AY2122S2-CS2103T-T11-4/tp/pulls?q=type%3Apr+reviewed-by%3Aspyobird) for the team repository.
* **Documentation**:
  * User Guide: Contributed to the Quick Start tutorial and minor changes. [\#234](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/234)
  * Developer Guide: Contributed to the `Logic` and `Model` components. [\#240](https://github.com/AY2122S2-CS2103T-T11-4/tp/pull/240)
* **Community**:
  * Participated in [14 forum posts](https://github.com/nus-cs2103-AY2122S2/forum/issues?q=commenter%3Aspyobird) for the module.
  * Reported [11 issues](https://github.com/Spyobird/ped/issues) for other team projects during practical exam dry-run.
