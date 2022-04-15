---
layout: page
title: User Guide
---
Welcome to PeopleSoft!

PeopleSoft is a desktop app for **calculating the salary for shift-based contractors**, optimized for use via a **Command Line Interface (CLI)**. If you are a **HR manager** and you can type fast, PeopleSoft can get your payroll tasks done **much faster** than traditional GUI apps.

You can input your employees' data and the jobs that you want to keep track of.
Then, you can assign the employees to the jobs that they are working on.
After the job is completed, you can mark the job as paid, and PeopleSoft will calculate how much each employee is to be paid based on their hourly rates.
You can also generate a payslip in comma-separated values (CSV) format for you and your employees to refer to.

The program simulates a real life workflow:
  * Company receives a new job.
  * The HR manager `add`s the job to PeopleSoft, and `assign`s employees to work on it.
  * Employees start working. 
  * When the employees complete the job, the HR manager `mark`s the job as completed.
  * Once it is time to pay the employees, `pay` out the job and `export` the payslips for the employees.

<div markdown="block" class="alert alert-info">

**:information_source: How to use this guide:**<br>

* Words in `monospace font` are commands to be typed into PeopleSoft.<br>

* Boxes like these (with a blue background and the :information_source: icon) contain relevant tips for using PeopleSoft.

* Boxes with a yellow background and the :warning: icon contain important warnings.

</div>

--------------------------------------------------------------------------------------------------------------------
* Table of Contents 
{:toc}
--------------------------------------------------------------------------------------------------------------------

## Quick start

### Running PeopleSoft for the first time

1. Ensure you have Java 11 or above installed on your computer. Follow [**this guide**](https://docs.oracle.com/en/java/javase/11/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A) to do so.

2. **Download** the latest version of `peoplesoft.jar` from [**here**](https://github.com/AY2122S2-CS2103T-T11-4/tp/releases).

3. Place the `peoplesoft.jar` file anywhere on your computer (preferably within a new folder, as the configuration and data files will be stored in the same location).

4. **Double-click** the file to start the app. A window (similar to the one below) should appear shortly.

<div markdown="block" class="alert alert-info">

**:information_source: Note:** If double-clicking the file does not start the application, then open the command line interpreter for your system (e.g. Command Prompt or Windows Terminal for Windows, or Terminal on macOS and Linux), navigate to directory where the file is located (e,g, with `cd path/to/folder`), and run `java -jar peoplesoft.jar`.

</div>

![Ui](images/Ui_label.png)<br>
_The PeopleSoft interface_

When the application is started for the first time, it will be populated with sample data. You may delete this data with the `clear` command.

A `data` folder and some configuration files (with a `.json` extension) will also be created in the folder that you run `peoplesoft.jar` from; this is where the application data will be stored.

### Exploring the sample data

The sample data is meant to help users get started with PeopleSoft. This is a tutorial of some of the basic features of PeopleSoft using the sample data. As such, it is not a comprehensive overview of every feature in the application. You can refer to the [features](#features) section for more information about specific features.

To start off, notice that the sample data contains some employees under the table of employees. Here, you can see the details of the employees, including their name, base pay and tags.

Since the sample data does not include any jobs, we will need to create new ones.

#### Create a job

When your company receives a new job, you can add it to PeopleSoft. To create a job, you can use the `add` command. You will have to specify a name and a duration using the `n/` and `d/` prefixes respectively. For this tutorial, we will create a 2-hour-long aircon repair job.

1. Type `add n/Repair aircon d/2` in the command window.

    This command will add a new job with the name 'Repair aircon' and a duration of two hours.

    <div markdown="block" class="alert alert-info">

    **:information_source: Note:** The order of the prefixes `n/` and `d/` does not matter.

    </div>

2. Hit **enter** to run the command.

    A new job should be created in the table of jobs. It should have the name 'Repair aircon' with a duration of '2h'.

#### Assign person to a job

PeopleSoft allows you to indicate which employees are in-charge of a certain job with the `assign` command. The `assign` command requires 1 job *index*, and at laest 1 employee *index* (each prefixed by `i/`). These indexes can be found under the `#` column in both tables.

1. Type `assign 1 i/1` in the command window.

    The first number in the command (`1`) refers to the *index* of the job. In this case, it refers to the first job in the table of jobs, which is the aircon repair job created earlier.
    
    The prefix `i/` denotes the index of the employee to be assigned to the job. In this case, it references the first employee in the table of employees, which is 'Nicole Tan'. Additional employees can also be assigned to this job by adding additional `i/` attributes.

2. Hit **enter** to run the command.

    A message should appear, indicating that the job 'Repair aircon' is assigned to 'Nicole Tan'.
    
<br>

Index-based commands depend on the ordering of the items displayed in their respective tables. The search and list commands for employees and jobs can cause this order to vary.

1. Type `personfind aircon` in the command window.

    This command searches for 'aircon' in the names or tags of employees. Search terms are not case-sensitive.

2. Hit **enter** to run the command.

    There should be two persons listed: 'Nicole Tan', and 'Arjun Khatau'. Notice that the table of employees now only shows employees with the 'Aircon' tag.

3. Type `assign 1 i/2` in the command window.

    This assigns the second person in the table (Arjun Khatau) to the first job. Notice that the employee index (2) now refers to the second employee in the current list (Arjun Khatau), instead of in the original list (Kavya Singh).

4. Hit **enter** to run the command.

    A message should appear, indicating that the job 'Repair aircon' is assigned to 'Arjun Khatau'.

#### Complete a job and pay employees

A key feature of PeopleSoft is tracking the state of job completion and whether its payment has been processed; this can be done with the `mark` and `pay` commands, respectively.

1. Type `mark 1` in the command window.

    This command marks the first job as completed.

2. Hit **enter** to run the command.

    The job 'Repair aircon' should be marked as completed, and a checkmark should appear under the *Done* column.

Marking a job as completed creates pending payments for the job. The amounts pending payment are reflected in the *Unpaid* column in the employees list.

Now that the 'Repair aircon' job has been marked as completed, 'Nicole Tan' and 'Arjun Khatau' should have non-zero values under the *Unpaid* column.

This value reflects the amount of money that is pending payment to the employees. It is calculated from the employee's base rate and the job's duration.

1. Type `pay 1 y/` in the command window. 

    The prefix `y/` is a safeguard against accidental misuse of this command. This command finalizes payments for the given job, and is **irreversible**. After the job is finalized, it cannot be further modified, so do make sure that you intend to run this command before running it.
   
2. Hit **enter** to run the command.

    The payments for the job is now finalized, indicating that the employees have been paid for the job. This is also reflected in the checkmark under the *Paid* column.

#### Export employee payslips

PeopleSoft also allows users to export a payslip for each user as a comma-separated values (CSV) spreadsheet.

1. Type `export 1` in the command window.

    This command exports the payslip for the first user (Nicole Tan) to a CSV spreadsheet.

2. Hit **enter** to run the command.

    The payslip is now saved in the PeopleSoft `data` folder under a name beginning with the employee's name.

This concludes the tutorial on the basic usage of PeopleSoft. You can refer to the [features](#features) section for more information about specific features. To clear the sample data, run the `clear` command.

--------------------------------------------------------------------------------------------------------------------
## Command summary

A handy reference for more experienced users who just need to know the format of a command.

| Command                                                     | Format                                                                                        | Examples                                                                                                      |
|:------------------------------------------------------------|:----------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------|
| [`personadd`](#personadd-add-an-employee)                   | `personadd n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS r/RATE [t/TAG]...​`                         | `personadd n/Nicole Tan p/99338558 e/nicole@stffhub.org  a/1 Tech Drive, S138572 r/37.50 t/Hardware t/Senior` |
| [`personedit`](#personedit-edit-an-employees-information)   | `personedit PERSON_INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [r/RATE] [t/TAG]...​` | `personedit 2 n/Nicole Lee t/OS`                                                                              |
| [`persondelete`](#persondelete-delete-an-employee)          | `persondelete PERSON_INDEX`                                                                   | `persondelete 3`                                                                                              |
| [`personfind`](#personfind-find-an-employee-by-name-or-tag) | `personfind KEYWORD [MORE_KEYWORDS]...​`                                                       | `personfind Nicole Hardware`, `personfind Aircon`                                                             |
| [`personlist`](#personlist-list-all-employees)              | `personlist`                                                                                  | NA                                                                                                            |
| [`export`](#export-export-jobs-done-by-an-employee)         | `export PERSON_INDEX`                                                                         | `export 2`                                                                                                    |
| [`clear`](#clear-clear-all-app-data)                        | `clear`                                                                                       | NA                                                                                                            |
| [`add`](#add-add-a-job)                                     | `add n/NAME d/DURATION`                                                                       | `add n/Fix HDB Lock d/1`                                                                                      |
| [`find`](#find-search-for-a-job-by-name)                    | `find NAME`                                                                                   | `find Painting`                                                                                               |
| [`list`](#list-list-all-jobs)                               | `list`                                                                                        | NA                                                                                                            |
| [`delete`](#delete-delete-a-job)                            | `delete JOB_INDEX`                                                                            | `delete 3`                                                                                                    |
| [`assign`](#assign-assign-a-job-to-an-employee)             | `assign JOB_INDEX i/PERSON_INDEX [i/PERSON_INDEX]...​`                                         | `assign 2 i/1`                                                                                                |
| [`mark`](#mark-mark-or-unmark-a-job-as-done)                | `mark JOB_INDEX`                                                                              | `mark 2`                                                                                                      |
| [`pay`](#pay-finalize-payments-for-a-job)                   | `pay JOB_INDEX y/`                                                                            | `pay 2 y/`                                                                                                    |
| [`exit`](#exit-exit-the-program)                            | `exit`                                                                                        | NA                                                                                                            |
| [`help`](#help-show-help-page)                              | `help`                                                                                        | NA                                                                                                            |

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the attributes to be filled in by you.<br>
  e.g. in `personadd n/NAME`, `NAME` can be replaced with an actual name like in `personadd n/John Doe`.

* Attributes can be in any order.<br>
  e.g. if the command asks for `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also fine.

* If an attribute is expected only once but is typed multiple times, only the last occurrence of the attribute will be taken.<br>
  e.g. if you specify `n/Jake n/Jason`, only `n/Jason` will be taken.

* For commands that do not need attributes (like `help`, `list`, `exit` and `clear`), anything typed after the command word will be ignored.<br>
  e.g. `help 123` will be interpreted as `help`.

* Items in square brackets are optional.<br>
  e.g. `n/NAME [t/TAG]` can be specified as `n/John Doe t/friend` or `n/John Doe`.

* Items with ellipses (`...​`) after them indicate that the items can be repeated any number of times.<br>
  e.g. `[t/TAG]...​` can be interpreted as `[t/TAG]`, `[t/TAG] [t/TAG]`, and so on.

</div>

--------------------------------------------------------------------------------------------------------------------

## Features

### Employee-related commands

#### `personadd`: Add an employee

Adds a new employee to the system with the given attributes.

Rate refers to the hourly pay of the employee.

Format: `personadd n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS r/RATE [t/TAG] [t/TAG]...`

Examples:

* `personadd n/Nicole Tan p/99338558 e/nicole@stffhub.org a/1 Tech Drive, S138572 r/37.50 t/Hardware t/Senior` will create a new employee with name "Nicole Tan", phone number "99338558", email "nicole@stffhub.org", address "1 Tech Drive, S138572", an hourly rate of $37.50, and with tags "Hardware" and "Senior".
* `personadd n/Jennifer Tan p/88473219 e/jennifer@stffhub.org a/13 Tech Drive, S182562 r/25` will create a new employee with name "Jennifer Tan", phone number "88473219", email "jennifer@stffhub.org", address "13 Tech Drive, S182562", an hourly rate of $25. No tags are added since it's an optional attribute.

<div markdown="block" class="alert alert-info">

**:information_source: Note:**<br>

* Attributes containing `/` are not accepted. Replace `/` with another character (e.g. `-`) if any attribute contains `/`.

  For example: Use `Ravi s-o Veegan` instead of `Ravi s/o Veegan`, and `3-5 Jalan Trus` instead of `3/5 Jalan Trus`.

* The maximum value for the hourly rate of an employee is $1,000,000.

</div>

#### `personedit`: Edit an employee's information

Edit the information of an existing employee. Use this in the event that an employee's details change.

Rate updates will only take effect with jobs that are pending completion; payout amounts for already-completed (i.e. marked with `mark`) jobs will not change.

Format: `personedit PERSON_INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [r/RATE] [t/TAG]...​`

Examples:

* `personedit 2 p/62353535` changes the second person's phone number to 62353535.
* `personedit 3 t/Hardware t/Network` changes the third person's tags to `Hardware` and `Network` instead of their original tags.

<div markdown="block" class="alert alert-info">

**:information_source: Note:**<br>

* Attributes containing `/` are not accepted. Replace `/` with another character (e.g. `-`) if any attribute contains `/`.

  For example: Use `Ravi s-o Veegan` instead of `Ravi s/o Veegan`, and `3-5 Jalan Trus` instead of `3/5 Jalan Trus`.

* The maximum value for the hourly rate of an employee is $1,000,000.

* When editing tags, new tags will not be added to the existing tags. Instead, all existing tags will be replaced by new tags.

* To clear all tags, add `t/` to the command without specifying any other tags.
  
</div>

#### `persondelete`: Delete an employee

Deletes the employee referred to by the index. This also removes the deleted employee from all associated jobs.<br>

<div markdown="block" class="alert alert-warning">

**:warning: Caution:**<br>

This is irreversible.

</div>

Format: `persondelete PERSON_INDEX`

Example: `persondelete 3` deletes the third person in the list.

#### `personfind`: Find employees by name or tag

Finds all employees that have the given keyword(s) in their names or tags, and lists them in the employee table.

If multiple keywords are entered, only entries that match **all** keywords are returned.

Keywords are case-insensitive.

Format: `personfind KEYWORD [MORE_KEYWORDS]...​`

Examples:

* `personfind Nicole Hardware` finds all employees with ‘Nicole’ **and** ‘Hardware’ in their name and/or tags.
* `personfind Nicole` finds all employees with ‘Nicole’ in their name and/or tags.
* `personfind Nicole Hardware Display` finds all employees with ‘Nicole’, 'Hardware', **and** 'Display' in their name and/or tags.


![before](images/screenshots/personfind/before.png)
![after](images/screenshots/personfind/after.png)

#### `personlist`: List all employees

Lists all employees added to the application.

Format: `personlist`

Example: `personlist` shows all employees.

#### `export`: Export jobs done by an employee

Exports a `.csv` file containing the jobs that the employee was assigned to, including:
* job IDs,
* job descriptions,
* job statuses (incomplete, pending payment, paid),
* effective rate(s) for the employee,
* job durations, and
* the amount paid, or to be paid to the employee.

Format: `export PERSON_INDEX`

Example: `export 3` exports the third person in the list.

<div markdown="block" class="alert alert-info">

**:information_source: Note:**<br>

This command updates the job list to show all jobs assigned to that person.

</div>

--------------------------------------------------------------------------------------------------------------------

### Job-related commands

#### `add`: Add a job

Adds a new job with the given attributes. `DURATION` refers to the duration required for job, in hours.

Format: `add n/NAME d/DURATION`

Examples:

* `add n/Fix HDB Lock d/1` creates a job named "Fix HDB Lock" with a duration of 1 hour.
* `add n/Repair aircon d/4` creates a job named "Repair aircon" with a duration of 4 hours.

<div markdown="block" class="alert alert-info">

**:information_source: Note:**<br>

* The maximum value for the duration of a job is 1,000,000 hours.

* Multiple jobs of the same name can be added. These jobs can then be differentiated by their internal ID, the order in which they were added, and the employees that were assigned to it, although practically it can easily result in confusion. It is thus recommended that a user differentiates jobs through naming to avoid any confusion.
  
</div>

#### `find`: Find jobs by name

Finds all jobs that have the given keyword(s) in their names or tags, and lists them in the employee table.

If multiple keywords are entered, only entries that match **all** keywords are returned.

Keywords are case-insensitive.

Format: `find KEYWORD [MORE_KEYWORDS]...`

Examples:

* `find Painting` finds all jobs with 'Painting' in their names.
* `find paint istana` finds all jobs with 'paint' **and** 'istana' in their names.

![before](images/screenshots/find/before.png)
![after](images/screenshots/find/after.png)

#### `list`: List all jobs

Lists all the jobs added to the application, including jobs that have been paid for and those that have not been paid for yet.

Format: `list`

Example: `list` shows all jobs.

#### `delete`: Delete a job

Deletes the job with the given index.

<div markdown="block" class="alert alert-warning">

**:warning: Caution:**<br>

This is irreversible.

</div>

Format: `delete JOB_INDEX`

Example: `delete 2` deletes the second job.

#### `assign`: Assign a job to an employee

Assigns a job to an employee to indicate that they are to be paid for the job.

Format: `assign JOB_INDEX i/PERSON_INDEX [i/PERSON_INDEX]...`

Examples:

* `assign 2 i/3` assigns the second job to the third employee.
* `assign 1 i/5 i/7` assigns the first job to the fifth and seventh employees.

<div markdown="block" class="alert alert-info">

**:information_source: Note:**<br>

A job that has been [marked](#mark-mark-or-unmark-a-job-as-done) as completed cannot be assigned. If a job
is completed, it makes little sense to assign more employees to it. In the event more employees need to be
assigned to a job, un-mark the job first before assigning them.

![before](images/screenshots/assign/before.png)
![after](images/screenshots/assign/after.png)

</div>

#### `mark`: Mark or unmark a job as done

Marks a job as done if it was not already marked as done, or marks a job as undone otherwise.

Marking a job as done indicates that a job has been completed and is pending payment. Unmarking a job causes the pending payment amounts to be subtracted from assigned employees.

Jobs are initially **not** marked as done when first created, and have to have at least one person [assigned](#assign-assign-a-job-to-an-employee) to it before it can be marked.

<div markdown="block" class="alert alert-info">

**:information_source: Note:**<br>

The hourly rate(s) paid out to each employee for a job is fixed once the job is marked as done; further changes to any employee's rate will not cause the payout amounts to change.

To update the payout amounts to reflect the new hourly rates, un-mark and mark the job again.

</div>

Format: `mark JOB_INDEX`

Examples:

* `mark 2` marks the second job, assuming it is not already marked as done.
* `mark 2` un-marks the second job if it has already been marked as done.

![Command](images/screenshots/mark/before.png) ![Result](images/screenshots/mark/after.png)

#### `pay`: Finalize payments for a job

Finalizes the payments of a job. A job needs to be [marked](#mark-mark-or-unmark-a-job-as-done) as done before it can be finalized.

<div markdown="block" class="alert alert-warning">

**:warning: Caution:**<br>

This is irreversible. The finalized job cannot be modified in any way, and can only be removed with [`clear`](#clear-clear-all-app-data).

</div>

Format: `pay JOB_INDEX y/`

Example: `pay 2 y/` finalizes the payments of the second job

![before](images/screenshots/pay/before.png)
![after](images/screenshots/pay/after.png)

--------------------------------------------------------------------------------------------------------------------

### Miscellaneous commands

#### `clear`: Clear all app data

Removes **all** data stored in the application. Useful for removing the sample data created when the application is started for the first time.

<div markdown="block" class="alert alert-warning">

**:warning: Caution:**<br>

This is irreversible; deleted data cannot be recovered afterwards without a backup.

</div>

Format: `clear`

Example: `clear` removes all employees and jobs from the application.

#### `exit`: Exit the program

Exits the program immediately.

Format: `exit`

Example: `exit` exits the program immediately.

#### `help`: Show help page

Opens the help page, which includes a list of commands, command formats, and example usages.

Format: `help`

Example: `help` opens up the help page.

![help](images/screenshots/help/help.png)

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I save my data?

**A**: PeopleSoft automatically saves application data to the folder that it was started from after every command. There is no need to save manually.

<br>

**Q**: How can I edit the application data manually?

**A**: PeopleSoft data is saved as a JSON file under `data/peoplesoft.json` in the folder that it was started from. It is possible (although not recommended) to modify application data by editing that file.

<div markdown="block" class="alert alert-warning">

**:warning: Caution:**<br>

Do not edit the data directly unless you know what you are doing. If your changes cause the data file to become invalid, PeopleSoft will discard all data and start with an empty data file the next time it is started.

</div>

<br>

**Q**: Can I get back the initial sample data?

**A**: Deleting the `data/peoplesoft.json` file and restarting PeopleSoft will cause the sample data to be reloaded.

<br>

**Q**: How do I transfer my data to another computer?

**A**: Install the app on the other computer and overwrite the `data/peoplesoft.json` file it creates with your existing `data/peoplesoft.json` file.

<br>

**Q**: How do I report a bug? How do I suggest a feature?

**A**: You may report a bug or suggest a new feature on the [PeopleSoft issue tracker](https://github.com/AY2122S2-CS2103T-T11-4/tp/issues).

--------------------------------------------------------------------------------------------------------------------

## Glossary

**CLI**: Command-line interface. A primarily text-based interface, which is typically operated with text commands.

**CSV**: Comma-seperated values. A common file format for storing tabular data, similar to a spreadsheet.

**Index**: The item's number that is displayed in its respective list.

e.g. The second person in the displayed list has an `INDEX` of 2.

**JSON**: JavaScript Object Notation. A structured file format used to store arbitrary text data. This is the file format used by PeopleSoft to store data and settings.

**Keyword**: A word to search for in a set of data, e.g. in the list of employees or jobs.
