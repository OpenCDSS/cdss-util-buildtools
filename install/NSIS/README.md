# NSIS 3.03 Add-ons #

This folder contains the necessary files for NSIS add-ons and for configuring the
appropriate development environment for CDSS Java sofware
including TSTool, StateDMI, StateView, and StateMod GUI.
NSIS is necessary to build a windows installer for the above program.
This script has been tested with NSIS 3.03 and 3.06 and should work OK with
other versions of NSIS unless substantial NSIS changes occur.

After downloading and installing [NSIS 3.03](https://sourceforge.net/projects/nsis/),
the files/folders found in this directory should be copied into the corresponding
folders and sub-folders under `C:\Program Files (x86)\NSIS`. To do so:

1. Open a Git Bash window as Administrator.
2. Change directories to this folder.
3. Run the `install-to-nsis-3.03.sh` script.

For more information about these specific files and how to add them to NSIS 3.03 see
the online [TSTool Developer documentation](http://learn.openwaterfoundation.org/cdss-app-tstool-doc-dev/dev-env/nsis/).
