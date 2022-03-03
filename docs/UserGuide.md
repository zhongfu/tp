---
layout: page
title: User Guide
---

PeopleSoft is a desktop app for **calculating the salary for shift-based contractors**, optimized for use via a **Command Line Interface (CLI)** while still having the benefits of a **Graphical User Interface (GUI)**. If you can type fast, PeopleSoft can get your payroll tasks done much faster than traditional GUI apps.


* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `11` or above installed in your Computer.

1. Download the latest `peoplesoft.jar` from [here]().

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Double-click the file to start the app. The GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>You can try out any command from the [Command Summary](#command-summary) below.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------
## Command summary

| Command | Format                                                                        | Examples                                                                                               |
|---------|-------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
| `help`    | `help`                                                                          | NA                                                                                                    |
|   `help`      | `help c\COMMAND`                                                                | `help c\edit`                                                                                              |
| `add`     | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS h/HOURS [t/TAG]`                  | `add n/Nicole Tan  p/99338558  e/nicole@stffhub.org  a/1 Tech Drive, S138572  h/32 t/Hardware  t/Senior` |
| `edit`    | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [h/HOURS] [t/TAG]` | `edit 2 n/Nicole Lee   t/OS`                                                                             |
| `delete`  | `delete INDEX`                                                                  | `delete 3`                                                                                               |
| `find`    | `find [KEYWORD] [MORE_KEYWORDS]`                                                | `find Nicole Hardware`                                                                                   |
| `list`    | `list`                                                                          | NA                                                                                                 |
|  `list`       | `list t\[TAG]`                                                                    | `list t\Senior`                                                                                          |
| `clear`   | `clear`                                                                         | NA                                                                                                  |
| `save`    | `save`                                                                          | NA                                                                                                   |

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* If a parameter is expected only once in the command but you specified it multiple times, only the last occurrence of the parameter will be taken.<br>
  e.g. if you specify `p/12341234 p/56785678`, only `p/56785678` will be taken.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

</div>

### List all the commands: `help`
Provides a list of all the commands (as defined below) with a short description of each of them.

Format: `help`

Prompted when the user makes a typo (i.e. tries to use an invalid command) :
Format: `Looks like you used an invalid command. Use the command help to access a list of all available commands.`

Additional Feature:

When given a command as a parameter, it provides a detailed description of it.

Format: `help c/COMMAND`

Example: `help c/edit`

### Add an employee : `add`

Adds a new employee to the system with the given attributes. `HOURS` refers to the number of hours worked by the employee.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS h/HOURS [t/TAG] [t/TAG]…`
Example: `add n/Nicole Tan p/99338558 e/nicole@stffhub.org a/1 Tech Drive, S138572 h/32 t/Hardware t/Senior`

### Edit an employee’s information : `edit`
Use command to edit the information of an existing employee. This is in the event that employee details change.

Format: `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]`

### Delete an employee : `delete`
Deletes the employee referred to by the index. This is irreversible.

Format: `delete INDEX`
Example: `delete 3`

### Search for a person by name or tag : `find`
Finds all people by a certain name and/or tag. If the search is to be conducted by tags alone, a ‘*’ in place of the name parameter. If multiple tags are passed, only entries that match all tags are returned.

Format: `find [*/NAME] [,Tags]`
Examples:

`find Nicole Hardware` finds all entries with a person named ‘Nicole’, tagged with ‘Hardware’

`find Nicole` finds all entries with a person named ‘Nicole’

`find * Hardware` finds all entries tagged with ‘Hardware’

`find Nicole Hardware Display` finds all entries with a person named ‘Nicole’, tagged with ‘Hardware’ AND ‘Display’ BOTH

### List all persons : `list`
Lists all the employees in the company

Format: `list`

Example: `list` shows all the employees in the company

### Clear all entries : `clear`

Removes all the employees’ information in the company from the app. Useful for clearing out sample data. WARNING: You cannot recover the data afterwards.

[coming in v1.2] Requests for confirmation from the user before clearing.

Format: `clear`

Example: `clear` removes all the employees from the app.

### Exit the program : `exit`
Exits the program immediately.
[coming in v1.3] Requests for confirmation from the user before exiting.

Format: `exit`

### Save the data
PeopleSoft saves the data to the hard disk automatically after any command that changes the data. There is no need to save manually.

### Edit the data externally
PeopleSoft data is saved as a JSON file [JAR file location]/data/peoplesoft.json. Advanced users are welcome to update data directly by editing that data file.

❗️Caution: If your changes to the data file makes its format invalid, PeopleSoft will discard all data and start with an empty data file at the next run.


### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with your existing PeopleSoft data file.

--------------------------------------------------------------------------------------------------------------------

