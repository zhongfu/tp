---
layout: page
title: Wrik's Project Portfolio Page
---
### Project: PeopleSoft

PeopleSoft is a Payroll management app for companies handling contractor-based services.

### Summary of Contributions

* **Role in team**: Developer, Documentation, Testing

* **New Feature**: Added the export feature for payslips.
  * **What it does**: Gives the user ability to view exact earnings and job assignments.
  * **Justification**: Helps contractors keep track of their earnings transparently.

* **New Feature**: Created the job list interface and a skeletal implementation
  * **What it does**: Core functionality of the product.
  * **Justification**: Helps contractors keep track of their earnings transparently.

* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2122s2.github.io/tp-dashboard/?search=thewrik)

* **Enhancements to existing features**:
  * Find Functionality
    * **Originally** no wildcard support
    * After development, finds all people by a certain name and/or tag. If multiple keywords are queried, only entries
      that match **all** tags are returned.
    * The motivation behind this logic is that users want to increasingly narrow down the number
      of persons they see in the outlay.
    * Feature was later extended to similar behaviour on jobs as well.

* **Documentation**:
  * User Guide:
    * Set up the original draft adapting from the meeting notes.
    * Detailed relevant features, specifically elaborated on `find`, `list`, and `export` functionalities.

* **Testing**
  * Wrote the testing suite for Payments.
  * Wrote tests for `personfind` and `joblist`.

* **Community**:
  * Reported [6 bugs and suggestions for other teams](https://github.com/thewrik/ped/issues) in the class during Practical Exam Dry run (PE-D)

