# cdss-util-buildtools #

This repository contains tools used to build Colorado's Decision Support Systems (CDSS) Java software,
used within Eclipse (and initially also NetBeans).
The tools use Ant, NSIS, and Launch4J software to create software installers for Windows.
The installers when run install the software in standard locations and configure
the ***Start*** menu.
**An alternative such as Maven may be used in the future;
however, migrating to Maven will take some effort.**

Although these utilities could be used for other software,
they are not envisioned as general tools for any software.
Enhancements to the build utilities usually occur while developing an application such as TSTool.

See the following online resources:

* [CDSS](http://cdss.state.co.us)
* [OpenCDSS](http://learn.openwaterfoundation.org/cdss-website-opencdss/)
* [TSTool Developer Documentation](http://learn.openwaterfoundation.org/cdss-app-tstool-doc-dev/) - example of application that uses these tools

The developer documentation and guidelines will be updated as the development environment is used in development.  See the following sections in this page:

* [Repository Folder Structure](#repository-folder-structure)
* [Repository Dependencies](#repository-dependencies)
* [Development Environment Folder Structure](#development-environment-folder-structure)
* [Version](#version)
* [Contributing](#contributing)
* [License](#license)
* [Contact](#contact)

--------

## Repository Folder Structure ##

The following are the main folders and files in this repository, listed alphabetically.
See also the [Development Environment Folder Structure](#development-environment-folder-structure)
for overall folder structure recommendations.

```
cdss-util-buildtools/              Source code and development working files.
  .git/                            Git repository folder (DO NOT MODIFY THIS except with Git tools).
  .gitattributes                   Git configuration file for repository.
  .gitignore                       Git configuration file for repository.
  .project                         Eclipse configuration file (general file project).
  .pydevproject                    Eclipse Python configuration file (experimental Python integration).
  bin/                             Eclipse folder for compiled files (dynamic so ignored from repo).
  common*.xml                      Ant XML files to control the build process.
  conf/                            Configuration files for installer build tools.
  dist/                            Folder used to build distributable installer (ignored from repo).
  doc/                             Working folder for build process auto-generated code.
  externals/                       Third-party libraries and dependencies.
  graphics/                        Graphics used by software.
  install/                         Use tools that need to be installed in develoepr environment.
  lib/                             Third-party libraries.
  LICENSE.txt                      Library license file.
  nbproject/                       NetBeans project files (may be removed in the future).
  product-tester.xml               Used to test the build tools.
  README.md                        This file.
  resources/                       Additional resources.
  scripts/                         Utility scripts (may be moved/removed).
  setup/                           Setup files that can be used to add new products to the build process.
  src/                             Source code.
  svntool/                         Subversion version control system tools (was used before Git).
```

## Repository Dependencies ##

Repository dependencies fall into two categories as indicated below.

### Repository Dependencies for this Repository ###

This library does not depend on other repositories
Needed software components are saved in this repository
to ensure compatibility.

### Repositories that Depend on this Repository ###

The following repositories are known to depend on this repository:

|**Repository**|**Description**|
|----------------------------------------------------------------------------------------------------------------|----------------------------------------------------|
|[`cdss-app-statedmi-main`](https://github.com/OpenWaterFoundation/cdss-app-statedmi-main)                       |Main StateDMI application code.|
|[`cdss-app-tstool-main`](https://github.com/OpenWaterFoundation/cdss-app-tstool-main)                           |Main TSTool application code.|
|StateView - repository is being finalized                                                                       |HydroBase database viewer.|
|StateMod GUI - repository is being finalized                                                                    |StateMod model graphical user interface.|

## Development Environment Folder Structure ##

The following folder structure is recommended for software development.
Top-level folders should be created as necessary.
Repositories are expected to be on the same folder level to allow cross-referencing
scripts in those repositories to work.
TSTool is used as an example, and normally use of this repository will occur
through development of the main CDSS applications.
See the application's developer documentation for more information.

```
C:\Users\user\                               Windows user home folder (typical development environment).
/home/user/                                  Linux user home folder (not tested).
/cygdrive/C/Users/user                       Cygdrive home folder (not tested).
  cdss-dev/                                  Projects that are part of Colorado's Decision Support Systems.
    TSTool/                                  TSTool product folder (will be similar for other applications).
      eclipse-workspace/                     Folder for Eclipse workspace, which references Git repository folders.
                                             The workspace folder is not maintained in Git.
      git-repos/                             Git repositories for TSTool.
        cdss-util-buildtools/                This repository.
        ...others...                         See application developer documenation and README for full list.

```

## Version ##

A version for the tools is typically not defined.
Instead, repository tags are used to cross-reference with a TSTool (or other software) commit.
This allows checking out a version of the library consistent with an application version.

## Contributing ##

Contributions to this project can be submitted using the following options:

1. Software developers with commit privileges can write to this repository
as per normal OpenCDSS development protocols.
2. Post an issue on GitHub with suggested change.  Provide information using the issue template.
3. Fork the repository, make changes, and do a pull request.
Contents of the current master branch should be merged with the pull request to minimize
code review that is necessary.

See also the [OpenCDSS / protocols](http://learn.openwaterfoundation.org/cdss-website-opencdss/) for each software application.

## License ##

The software is licensed under GPL v3+.  See the [LICENSE.md](LICENSE.md) file.

## Contact ##

See the [OpenCDSS information](http://learn.openwaterfoundation.org/cdss-website-opencdss) for overall contacts and contacts for each software product.
